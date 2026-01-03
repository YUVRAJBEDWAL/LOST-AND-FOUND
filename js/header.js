// Header functionality
function toggleMobileMenu() {
  const navLinks = document.querySelector('.nav-links');
  const mobileToggle = document.querySelector('.mobile-menu-toggle');
  
  navLinks.classList.toggle('active');
  
  // Animate hamburger menu
  if (navLinks.classList.contains('active')) {
    mobileToggle.classList.add('active');
  } else {
    mobileToggle.classList.remove('active');
  }
}

// Search functionality
function performSearch() {
  const searchInput = document.getElementById('headerSearch');
  const searchTerm = searchInput.value.trim();
  
  if (searchTerm) {
    // Redirect to items page with search query
    window.location.href = `items.html?search=${encodeURIComponent(searchTerm)}`;
  }
}

// Handle search on Enter key
document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.getElementById('headerSearch');
  if (searchInput) {
    searchInput.addEventListener('keypress', function(e) {
      if (e.key === 'Enter') {
        performSearch();
      }
    });
  }
  
  // Close mobile menu when clicking outside
  document.addEventListener('click', function(e) {
    const navLinks = document.querySelector('.nav-links');
    const mobileToggle = document.querySelector('.mobile-menu-toggle');
    
    if (!navLinks.contains(e.target) && !mobileToggle.contains(e.target)) {
      navLinks.classList.remove('active');
      mobileToggle.classList.remove('active');
    }
  });
  
  // Update active navigation state
  updateActiveNavigation();
});

// Update active navigation based on current page
function updateActiveNavigation() {
  const currentPath = window.location.pathname.split('/').pop() || 'index.html';
  const navLinks = document.querySelectorAll('.nav-links a');
  
  navLinks.forEach(link => {
    link.classList.remove('active');
    const href = link.getAttribute('href');
    
    if (href === currentPath || 
        (currentPath === '' && href === 'index.html') ||
        (currentPath === 'index.html' && href === 'index.html')) {
      link.classList.add('active');
    }
  });
}

// Update user avatar when logged in
function updateUserAvatar() {
  const user = getCurrentUser();
  const userAvatar = document.getElementById('userAvatar');
  const loginBtn = document.querySelector('.nav-links a[href="login.html"]');
  
  if (user && userAvatar) {
    // Show user avatar
    userAvatar.style.display = 'flex';
    userAvatar.textContent = user.name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
    
    // Hide login button
    if (loginBtn) {
      loginBtn.style.display = 'none';
    }
  } else if (userAvatar && loginBtn) {
    // Hide user avatar, show login button
    userAvatar.style.display = 'none';
    loginBtn.style.display = 'flex';
  }
}

// Listen for authentication changes
document.addEventListener('DOMContentLoaded', function() {
  updateUserAvatar();
  
  // Update avatar when auth state changes
  window.addEventListener('storage', function(e) {
    if (e.key === 'user' || e.key === 'jwtToken') {
      updateUserAvatar();
    }
  });
});
