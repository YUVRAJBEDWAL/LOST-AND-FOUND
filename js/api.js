// API Configuration and Utilities
const API_BASE_URL = 'http://localhost:8080/api';

// Generic API request function
async function apiRequest(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  
  // Get token from localStorage
  const token = localStorage.getItem('jwtToken');
  
  // Default headers
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };
  
  // Add Authorization header if token exists
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  
  // Default options
  const config = {
    ...options,
    headers,
  };
  
  try {
    const response = await fetch(url, config);
    
    // Handle 401 Unauthorized
    if (response.status === 401) {
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('user');
      window.location.href = 'login.html';
      return;
    }
    
    // Handle other HTTP errors
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
    }
    
    // Return JSON response
    return await response.json();
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
}

// Specific API methods
const api = {
  // GET request
  get: (endpoint) => apiRequest(endpoint),
  
  // POST request
  post: (endpoint, data) => apiRequest(endpoint, {
    method: 'POST',
    body: JSON.stringify(data),
  }),
  
  // PUT request
  put: (endpoint, data) => apiRequest(endpoint, {
    method: 'PUT',
    body: JSON.stringify(data),
  }),
  
  // DELETE request
  delete: (endpoint) => apiRequest(endpoint, {
    method: 'DELETE',
  }),
};

// Show alert message
function showAlert(message, type = 'info') {
  // Remove existing alerts
  const existingAlert = document.querySelector('.alert');
  if (existingAlert) {
    existingAlert.remove();
  }
  
  // Create alert element
  const alert = document.createElement('div');
  alert.className = `alert alert-${type}`;
  alert.textContent = message;
  
  // Insert at the top of the page
  const container = document.querySelector('.container');
  if (container) {
    container.insertBefore(alert, container.firstChild);
  } else {
    document.body.insertBefore(alert, document.body.firstChild);
  }
  
  // Auto remove after 5 seconds
  setTimeout(() => {
    if (alert.parentNode) {
      alert.remove();
    }
  }, 5000);
  
  // Scroll to top
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

// Format date
function formatDate(dateString) {
  if (!dateString) return 'Unknown';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

// Get query parameter from URL
function getQueryParam(param) {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(param);
}

// Show loading state
function showLoading(element) {
  if (element) {
    element.innerHTML = '<div class="loading"></div>';
    element.disabled = true;
  }
}

// Hide loading state
function hideLoading(element, originalText) {
  if (element) {
    element.innerHTML = originalText || element.getAttribute('data-original-text') || 'Submit';
    element.disabled = false;
  }
}

// Check if user is logged in
function isLoggedIn() {
  return !!localStorage.getItem('jwtToken');
}

// Get current user
function getCurrentUser() {
  const userStr = localStorage.getItem('user');
  return userStr ? JSON.parse(userStr) : null;
}

// Redirect if not logged in
function requireAuth() {
  if (!isLoggedIn()) {
    window.location.href = 'login.html';
    return false;
  }
  return true;
}

// Redirect if already logged in
function redirectIfLoggedIn() {
  if (isLoggedIn()) {
    window.location.href = 'index.html';
    return true;
  }
  return false;
}

// Update navigation based on auth status
function updateNavigation() {
  const user = getCurrentUser();
  const navLinks = document.querySelector('.nav-links');
  
  if (!navLinks) return;
  
  // Remove auth-related links
  const existingAuthLinks = navLinks.querySelectorAll('.auth-link');
  existingAuthLinks.forEach(link => link.remove());
  
  if (user) {
    // User is logged in
    const profileLink = document.createElement('li');
    profileLink.className = 'auth-link';
    profileLink.innerHTML = `<a href="profile.html">Profile</a>`;
    
    const logoutLink = document.createElement('li');
    logoutLink.className = 'auth-link';
    logoutLink.innerHTML = `<a href="#" onclick="logout()">Logout</a>`;
    
    navLinks.appendChild(profileLink);
    navLinks.appendChild(logoutLink);
    
    // If user is admin, add admin link
    if (user.role === 'ADMIN') {
      const adminLink = document.createElement('li');
      adminLink.className = 'auth-link';
      adminLink.innerHTML = `<a href="admin.html">Admin</a>`;
      navLinks.appendChild(adminLink);
    }
  } else {
    // User is not logged in
    const loginLink = document.createElement('li');
    loginLink.className = 'auth-link';
    loginLink.innerHTML = `<a href="login.html" class="btn btn-primary btn-sm">Login</a>`;
    
    navLinks.appendChild(loginLink);
  }
}

// Initialize navigation on page load
document.addEventListener('DOMContentLoaded', updateNavigation);
