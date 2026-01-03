// Admin dashboard functions
let adminStats = {};
let allUsers = [];
let allAdminItems = [];

// Load admin dashboard data
async function loadAdminDashboard() {
  if (!requireAuth()) return;
  
  const user = getCurrentUser();
  if (!user || user.role !== 'ADMIN') {
    showAlert('Access denied. Admin privileges required.', 'danger');
    window.location.href = 'index.html';
    return;
  }
  
  try {
    showLoading(document.querySelector('.admin-content'));
    
    // Load all data in parallel
    const [stats, users, activity] = await Promise.all([
      api.get('/admin/dashboard'),
      api.get('/admin/users'),
      api.get('/admin/activity')
    ]);
    
    adminStats = stats;
    allUsers = users;
    // Combine lost and found items from activity
    allAdminItems = [
      ...(activity.recentLostItems || []).map(item => ({...item, type: 'lost'})),
      ...(activity.recentFoundItems || []).map(item => ({...item, type: 'found'}))
    ];
    
    renderAdminDashboard();
  } catch (error) {
    console.error('Error loading admin dashboard:', error);
    showAlert('Failed to load admin dashboard', 'danger');
  } finally {
    hideLoading(document.querySelector('.admin-content'));
  }
}

// Render admin dashboard
function renderAdminDashboard() {
  renderStats();
  renderUsers();
  renderItems();
}

// Render statistics
function renderStats() {
  const statsContainer = document.getElementById('adminStats');
  if (!statsContainer) return;
  
  statsContainer.innerHTML = `
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div class="card">
        <div class="card-body text-center">
          <h3 class="text-2xl font-bold text-primary">${adminStats.totalUsers || 0}</h3>
          <p class="text-secondary">Total Users</p>
        </div>
      </div>
      <div class="card">
        <div class="card-body text-center">
          <h3 class="text-2xl font-bold text-warning">${adminStats.totalLostItems || 0}</h3>
          <p class="text-secondary">Lost Items</p>
        </div>
      </div>
      <div class="card">
        <div class="card-body text-center">
          <h3 class="text-2xl font-bold text-success">${adminStats.totalFoundItems || 0}</h3>
          <p class="text-secondary">Found Items</p>
        </div>
      </div>
      <div class="card">
        <div class="card-body text-center">
          <h3 class="text-2xl font-bold text-danger">${adminStats.pendingClaims || 0}</h3>
          <p class="text-secondary">Pending Claims</p>
        </div>
      </div>
    </div>
  `;
}

// Render users table
function renderUsers() {
  const usersContainer = document.getElementById('usersTable');
  if (!usersContainer) return;
  
  if (!allUsers || allUsers.length === 0) {
    usersContainer.innerHTML = '<p class="text-center text-secondary">No users found.</p>';
    return;
  }
  
  const usersHTML = allUsers.map(user => `
    <tr>
      <td>${escapeHtml(user.name)}</td>
      <td>${escapeHtml(user.email)}</td>
      <td><span class="badge ${user.role === 'ADMIN' ? 'badge-primary' : 'badge-secondary'}">${user.role}</span></td>
      <td>${formatDate(user.createdAt)}</td>
      <td>
        <div class="flex gap-2">
          ${user.role !== 'ADMIN' ? `
            <button onclick="toggleUserRole('${user.id}', 'ADMIN')" class="btn btn-sm btn-outline">Make Admin</button>
          ` : `
            <button onclick="toggleUserRole('${user.id}', 'USER')" class="btn btn-sm btn-outline">Remove Admin</button>
          `}
          <button onclick="deleteUser('${user.id}')" class="btn btn-sm btn-danger">Delete</button>
        </div>
      </td>
    </tr>
  `).join('');
  
  usersContainer.innerHTML = `
    <div class="card">
      <div class="card-header">
        <h3>Users Management</h3>
      </div>
      <div class="card-body">
        <div class="table-responsive">
          <table class="w-full">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Joined</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              ${usersHTML}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `;
}

// Render items table
function renderItems() {
  const itemsContainer = document.getElementById('itemsTable');
  if (!itemsContainer) return;
  
  if (!allAdminItems || allAdminItems.length === 0) {
    itemsContainer.innerHTML = '<p class="text-center text-secondary">No items found.</p>';
    return;
  }
  
  const itemsHTML = allAdminItems.map(item => {
    const typeClass = item.type === 'lost' ? 'badge-danger' : 'badge-success';
    const statusClass = item.status === 'RESOLVED' ? 'badge-success' : 'badge-warning';
    
    return `
      <tr>
        <td>${escapeHtml(item.itemName)}</td>
        <td><span class="badge ${typeClass}">${item.type.toUpperCase()}</span></td>
        <td>${escapeHtml(item.category || 'N/A')}</td>
        <td>${escapeHtml(item.location)}</td>
        <td><span class="badge ${statusClass}">${item.status || 'ACTIVE'}</span></td>
        <td>${formatDate(item.createdAt)}</td>
        <td>
          <div class="flex gap-2">
            <button onclick="viewItemDetails(${item.id}, '${item.type}')" class="btn btn-sm btn-outline">View</button>
            ${item.status !== 'RESOLVED' ? `
              <button onclick="resolveItem('${item.id}', '${item.type}')" class="btn btn-sm btn-primary">Resolve</button>
            ` : ''}
            <button onclick="deleteItem('${item.id}', '${item.type}')" class="btn btn-sm btn-danger">Delete</button>
          </div>
        </td>
      </tr>
    `;
  }).join('');
  
  itemsContainer.innerHTML = `
    <div class="card">
      <div class="card-header">
        <h3>Items Management</h3>
      </div>
      <div class="card-body">
        <div class="table-responsive">
          <table class="w-full">
            <thead>
              <tr>
                <th>Item Name</th>
                <th>Type</th>
                <th>Category</th>
                <th>Location</th>
                <th>Status</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              ${itemsHTML}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `;
}

// Toggle user role
async function toggleUserRole(userId, newRole) {
  if (!confirm(`Are you sure you want to change this user's role to ${newRole}?`)) {
    return;
  }
  
  try {
    await api.put(`/admin/users/${userId}/role`, { role: newRole });
    showAlert('User role updated successfully!', 'success');
    loadAdminDashboard(); // Reload data
  } catch (error) {
    console.error('Error updating user role:', error);
    showAlert('Failed to update user role', 'danger');
  }
}

// Delete user
async function deleteUser(userId) {
  if (!confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
    return;
  }
  
  try {
    await api.delete(`/admin/users/${userId}`);
    showAlert('User deleted successfully!', 'success');
    loadAdminDashboard(); // Reload data
  } catch (error) {
    console.error('Error deleting user:', error);
    showAlert('Failed to delete user', 'danger');
  }
}

// Resolve item
async function resolveItem(itemId, itemType) {
  if (!confirm('Are you sure you want to mark this item as resolved?')) {
    return;
  }
  
  try {
    await api.put(`/admin/items/${itemType}/${itemId}/resolve`);
    showAlert('Item marked as resolved!', 'success');
    loadAdminDashboard(); // Reload data
  } catch (error) {
    console.error('Error resolving item:', error);
    showAlert('Failed to resolve item', 'danger');
  }
}

// Delete item
async function deleteItem(itemId, itemType) {
  if (!confirm('Are you sure you want to delete this item? This action cannot be undone.')) {
    return;
  }
  
  try {
    await api.delete(`/admin/items/${itemType}/${itemId}`);
    showAlert('Item deleted successfully!', 'success');
    loadAdminDashboard(); // Reload data
  } catch (error) {
    console.error('Error deleting item:', error);
    showAlert('Failed to delete item', 'danger');
  }
}

// Export data
async function exportData(type) {
  try {
    const response = await api.get(`/admin/export/${type}`);
    
    // Create download link
    const blob = new Blob([JSON.stringify(response, null, 2)], { type: 'application/json' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${type}-export-${new Date().toISOString().split('T')[0]}.json`;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
    
    showAlert(`${type} data exported successfully!`, 'success');
  } catch (error) {
    console.error('Error exporting data:', error);
    showAlert('Failed to export data', 'danger');
  }
}

// Setup admin dashboard
function setupAdminDashboard() {
  // Export buttons
  const exportUsersBtn = document.getElementById('exportUsers');
  const exportItemsBtn = document.getElementById('exportItems');
  
  if (exportUsersBtn) {
    exportUsersBtn.addEventListener('click', () => exportData('users'));
  }
  
  if (exportItemsBtn) {
    exportItemsBtn.addEventListener('click', () => exportData('items'));
  }
  
  // Search functionality
  const searchUsers = document.getElementById('searchUsers');
  const searchItems = document.getElementById('searchItems');
  
  if (searchUsers) {
    searchUsers.addEventListener('input', (e) => {
      const searchTerm = e.target.value.toLowerCase();
      const filteredUsers = allUsers.filter(user => 
        user.name.toLowerCase().includes(searchTerm) ||
        user.email.toLowerCase().includes(searchTerm)
      );
      renderFilteredUsers(filteredUsers);
    });
  }
  
  if (searchItems) {
    searchItems.addEventListener('input', (e) => {
      const searchTerm = e.target.value.toLowerCase();
      const filteredItems = allAdminItems.filter(item => 
        item.itemName.toLowerCase().includes(searchTerm) ||
        item.location.toLowerCase().includes(searchTerm)
      );
      renderFilteredItems(filteredItems);
    });
  }
}

// Render filtered users
function renderFilteredUsers(users) {
  const tempUsers = allUsers;
  allUsers = users;
  renderUsers();
  allUsers = tempUsers;
}

// Render filtered items
function renderFilteredItems(items) {
  const tempItems = allAdminItems;
  allAdminItems = items;
  renderItems();
  allAdminItems = tempItems;
}

// Initialize admin page
document.addEventListener('DOMContentLoaded', () => {
  const currentPage = window.location.pathname.split('/').pop();
  
  if (currentPage === 'admin.html') {
    loadAdminDashboard();
    setupAdminDashboard();
  }
});
