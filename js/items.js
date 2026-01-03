// Items management functions
let allItems = [];
let filteredItems = [];

// Load all items (lost and found)
async function loadItems() {
  try {
    showLoading(document.querySelector('.items-container'));
    
    const [lostItems, foundItems] = await Promise.all([
      api.get('/items/lost'),
      api.get('/items/found')
    ]);
    
    // Combine and format items
    allItems = [
      ...lostItems.map(item => ({ ...item, type: 'lost' })),
      ...foundItems.map(item => ({ ...item, type: 'found' }))
    ];
    
    // Sort by date (newest first)
    allItems.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
    
    filteredItems = [...allItems];
    
    renderItems();
  } catch (error) {
    console.error('Error loading items:', error);
    showAlert('Failed to load items. Please try again.', 'danger');
  } finally {
    hideLoading(document.querySelector('.items-container'));
  }
}

// Render items to the page
function renderItems() {
  const container = document.querySelector('.items-container');
  if (!container) return;
  
  if (filteredItems.length === 0) {
    container.innerHTML = `
      <div class="text-center py-8">
        <p class="text-secondary">No items found.</p>
      </div>
    `;
    return;
  }
  
  const itemsHTML = filteredItems.map(item => createItemCard(item)).join('');
  container.innerHTML = `<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">${itemsHTML}</div>`;
}

// Create item card HTML
function createItemCard(item) {
  const badgeClass = item.type === 'lost' ? 'badge-danger' : 'badge-success';
  const badgeText = item.type === 'lost' ? 'Lost' : 'Found';
  
  return `
    <div class="card item-card">
      <div class="item-card-content">
        <div class="flex justify-between items-start mb-3">
          <h3 class="item-card-title">${escapeHtml(item.itemName)}</h3>
          <span class="badge ${badgeClass}">${badgeText}</span>
        </div>
        <p class="item-card-description">${escapeHtml(item.description)}</p>
        <div class="text-sm text-secondary mb-3">
          <p><strong>Location:</strong> ${escapeHtml(item.location)}</p>
          <p><strong>Date:</strong> ${formatDate(item.dateFound || item.dateLost)}</p>
          ${item.contactInfo ? `<p><strong>Contact:</strong> ${escapeHtml(item.contactInfo)}</p>` : ''}
        </div>
        <div class="flex justify-between items-center">
          <span class="text-xs text-secondary">
            ${item.user ? `By ${escapeHtml(item.user.name)}` : 'Anonymous'}
          </span>
          <button onclick="viewItemDetails(${item.id}, '${item.type}')" class="btn btn-outline btn-sm">
            View Details
          </button>
        </div>
      </div>
    </div>
  `;
}

// View item details
function viewItemDetails(itemId, type) {
  // Store item details in sessionStorage for the details page
  sessionStorage.setItem('selectedItem', JSON.stringify({ id: itemId, type }));
  window.location.href = `item-details.html?id=${itemId}&type=${type}`;
}

// Filter items
function filterItems(searchTerm, typeFilter, locationFilter) {
  filteredItems = allItems.filter(item => {
    const matchesSearch = !searchTerm || 
      item.itemName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.location.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesType = !typeFilter || typeFilter === 'all' || item.type === typeFilter;
    const matchesLocation = !locationFilter || 
      item.location.toLowerCase().includes(locationFilter.toLowerCase());
    
    return matchesSearch && matchesType && matchesLocation;
  });
  
  renderItems();
}

// Setup search and filters
function setupItemFilters() {
  const searchInput = document.getElementById('search');
  const typeFilter = document.getElementById('typeFilter');
  const locationFilter = document.getElementById('locationFilter');
  
  if (searchInput) {
    searchInput.addEventListener('input', () => {
      filterItems(
        searchInput.value,
        typeFilter?.value,
        locationFilter?.value
      );
    });
  }
  
  if (typeFilter) {
    typeFilter.addEventListener('change', () => {
      filterItems(
        searchInput?.value,
        typeFilter.value,
        locationFilter?.value
      );
    });
  }
  
  if (locationFilter) {
    locationFilter.addEventListener('change', () => {
      filterItems(
        searchInput?.value,
        typeFilter?.value,
        locationFilter.value
      );
    });
  }
}

// Load item details for details page
async function loadItemDetails() {
  const itemId = getQueryParam('id');
  const itemType = getQueryParam('type');
  
  if (!itemId || !itemType) {
    showAlert('Invalid item details', 'danger');
    return;
  }
  
  try {
    const endpoint = `/items/${itemType}/${itemId}`;
    const item = await api.get(endpoint);
    
    renderItemDetails(item, itemType);
  } catch (error) {
    console.error('Error loading item details:', error);
    showAlert('Failed to load item details', 'danger');
  }
}

// Render item details
function renderItemDetails(item, type) {
  const container = document.querySelector('.item-details');
  if (!container) return;
  
  const badgeClass = type === 'lost' ? 'badge-danger' : 'badge-success';
  const badgeText = type === 'lost' ? 'Lost Item' : 'Found Item';
  
  container.innerHTML = `
    <div class="card">
      <div class="card-header">
        <div class="flex justify-between items-center">
          <h2>${escapeHtml(item.itemName)}</h2>
          <span class="badge ${badgeClass}">${badgeText}</span>
        </div>
      </div>
      <div class="card-body">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h3 class="mb-3">Description</h3>
            <p>${escapeHtml(item.description)}</p>
          </div>
          <div>
            <h3 class="mb-3">Details</h3>
            <p><strong>Location:</strong> ${escapeHtml(item.location)}</p>
            <p><strong>Date:</strong> ${formatDate(item.dateFound || item.dateLost)}</p>
            <p><strong>Category:</strong> ${escapeHtml(item.category || 'Not specified')}</p>
            ${item.contactInfo ? `<p><strong>Contact Info:</strong> ${escapeHtml(item.contactInfo)}</p>` : ''}
          </div>
        </div>
        
        ${item.user ? `
        <div class="mt-6">
          <h3 class="mb-3">Reported By</h3>
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 bg-primary text-white rounded-full flex items-center justify-center">
              ${item.user.name.charAt(0).toUpperCase()}
            </div>
            <div>
              <p class="font-semibold">${escapeHtml(item.user.name)}</p>
              <p class="text-sm text-secondary">${escapeHtml(item.user.email)}</p>
            </div>
          </div>
        </div>
        ` : ''}
        
        <div class="mt-6 flex gap-3">
          ${isLoggedIn() ? `
            <button onclick="claimItem(${item.id}, '${type}')" class="btn btn-primary">
              Claim This Item
            </button>
          ` : `
            <a href="login.html" class="btn btn-primary">
              Login to Claim This Item
            </a>
          `}
          <a href="items.html" class="btn btn-outline">Back to Items</a>
        </div>
      </div>
    </div>
  `;
}

// Claim an item
async function claimItem(itemId, type) {
  if (!requireAuth()) return;
  
  try {
    await api.post('/claims', {
      itemId: itemId,
      itemType: type,
      claimMessage: 'I would like to claim this item.'
    });
    
    showAlert('Claim submitted successfully! The item owner will be notified.', 'success');
  } catch (error) {
    console.error('Error claiming item:', error);
    showAlert('Failed to submit claim. Please try again.', 'danger');
  }
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

// Initialize items page
document.addEventListener('DOMContentLoaded', () => {
  const currentPage = window.location.pathname.split('/').pop();
  
  if (currentPage === 'items.html') {
    loadItems();
    setupItemFilters();
  } else if (currentPage === 'item-details.html') {
    loadItemDetails();
  }
});
