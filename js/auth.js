// Authentication functions
async function login(email, password) {
  try {
    const response = await api.post('/auth/login', { email, password });
    
    // Store JWT token
    localStorage.setItem('jwtToken', response.token);
    
    // Store user info - backend returns user data directly in response
    const userData = {
      id: response.userId,
      email: response.email,
      name: response.name,
      role: response.role
    };
    localStorage.setItem('user', JSON.stringify(userData));
    
    showAlert('Login successful! Redirecting...', 'success');
    
    // Redirect to home page
    setTimeout(() => {
      window.location.href = 'index.html';
    }, 1500);
    
    return response;
  } catch (error) {
    showAlert(error.message || 'Login failed. Please check your credentials.', 'danger');
    throw error;
  }
}

async function register(userData) {
  try {
    const response = await api.post('/auth/register', userData);
    
    showAlert('Registration successful! Please login.', 'success');
    
    // Redirect to login page
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 1500);
    
    return response;
  } catch (error) {
    showAlert(error.message || 'Registration failed. Please try again.', 'danger');
    throw error;
  }
}

function logout() {
  // Remove token and user data
  localStorage.removeItem('jwtToken');
  localStorage.removeItem('user');
  
  showAlert('Logged out successfully!', 'success');
  
  // Redirect to home page
  setTimeout(() => {
    window.location.href = 'index.html';
  }, 1000);
}

// Handle login form submission
async function handleLogin(event) {
  event.preventDefault();
  
  const form = event.target;
  const submitBtn = form.querySelector('button[type="submit"]');
  const originalText = submitBtn.innerHTML;
  
  try {
    showLoading(submitBtn);
    
    const email = form.email.value;
    const password = form.password.value;
    
    if (!email || !password) {
      throw new Error('Please fill in all fields');
    }
    
    await login(email, password);
  } catch (error) {
    console.error('Login error:', error);
  } finally {
    hideLoading(submitBtn, originalText);
  }
}

// Handle registration form submission
async function handleRegister(event) {
  event.preventDefault();
  
  const form = event.target;
  const submitBtn = form.querySelector('button[type="submit"]');
  const originalText = submitBtn.innerHTML;
  
  try {
    showLoading(submitBtn);
    
    const name = form.name.value;
    const email = form.email.value;
    const password = form.password;
    const confirmPassword = form.confirmPassword.value;
    
    // Validation
    if (!name || !email || !password || !confirmPassword) {
      throw new Error('Please fill in all fields');
    }
    
    if (password !== confirmPassword) {
      throw new Error('Passwords do not match');
    }
    
    if (password.length < 6) {
      throw new Error('Password must be at least 6 characters long');
    }
    
    const userData = { name, email, password };
    await register(userData);
  } catch (error) {
    console.error('Registration error:', error);
  } finally {
    hideLoading(submitBtn, originalText);
  }
}

// Check token validity
async function checkTokenValidity() {
  const token = localStorage.getItem('jwtToken');
  if (!token) return false;
  
  try {
    // Try to access a protected endpoint
    await api.get('/auth/me');
    return true;
  } catch (error) {
    // Token is invalid, remove it
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('user');
    return false;
  }
}

// Initialize auth on page load
document.addEventListener('DOMContentLoaded', async () => {
  // Update navigation
  updateNavigation();
  
  // Check if we're on auth pages
  const currentPage = window.location.pathname.split('/').pop();
  
  // Redirect if needed
  if (currentPage === 'login.html' || currentPage === 'register.html') {
    redirectIfLoggedIn();
  } else if (currentPage !== 'index.html' && currentPage !== 'items.html' && currentPage !== 'contact.html') {
    // For protected pages, check auth
    requireAuth();
  }
});
