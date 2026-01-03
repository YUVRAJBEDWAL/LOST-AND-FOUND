// Report submission functions
async function submitLostItem(formData) {
  try {
    // Check if user is authenticated
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      showAlert('Please login first to report lost items', 'warning');
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
      return;
    }

    const response = await fetch('http://localhost:8080/api/items/lost', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(formData)
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to submit lost item');
    }
    
    showAlert('Lost item reported successfully!', 'success');
    
    // Redirect to items page after successful submission
    setTimeout(() => {
      window.location.href = 'items.html';
    }, 2000);
    
    return await response.json();
  } catch (error) {
    console.error('Error submitting lost item:', error);
    showAlert('Failed to report lost item. Please try again.', 'danger');
    throw error;
  }
}

async function submitFoundItem(formData) {
  try {
    // Check if user is authenticated
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      showAlert('Please login first to report found items', 'warning');
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
      return;
    }

    const response = await fetch('http://localhost:8080/api/items/found', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(formData)
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to submit found item');
    }
    
    showAlert('Found item reported successfully!', 'success');
    
    // Redirect to items page after successful submission
    setTimeout(() => {
      window.location.href = 'items.html';
    }, 2000);
    
    return await response.json();
  } catch (error) {
    console.error('Error submitting found item:', error);
    showAlert('Failed to report found item. Please try again.', 'danger');
    throw error;
  }
}

// Handle lost item form submission
async function handleLostItemSubmit(event) {
  event.preventDefault();
  
  const form = event.target;
  const submitBtn = form.querySelector('button[type="submit"]');
  const originalText = submitBtn.innerHTML;
  
  try {
    showLoading(submitBtn);
    
    const formData = {
      itemName: form.itemName.value.trim(),
      description: form.description.value.trim(),
      category: form.category.value,
      location: form.location.value.trim(),
      dateLost: form.dateLost.value,
      contactInfo: form.contactInfo?.value.trim() || ''
    };
    
    // Validation
    if (!formData.itemName || !formData.description || !formData.location || !formData.dateLost) {
      throw new Error('Please fill in all required fields');
    }
    
    if (formData.description.length < 10) {
      throw new Error('Description must be at least 10 characters long');
    }
    
    // Check if user is logged in
    if (isLoggedIn()) {
      // User is logged in, no need for contact info
      delete formData.contactInfo;
    } else {
      // User is not logged in, contact info is required
      if (!formData.contactInfo) {
        throw new Error('Contact information is required when not logged in');
      }
    }
    
    await submitLostItem(formData);
  } catch (error) {
    console.error('Lost item submission error:', error);
    showAlert('Failed to report lost item. Please try again.', 'danger');
  } finally {
    hideLoading(submitBtn, originalText);
  }
}

// Handle found item form submission
async function handleFoundItemSubmit(event) {
  event.preventDefault();
  
  const form = event.target;
  const submitBtn = form.querySelector('button[type="submit"]');
  const originalText = submitBtn.innerHTML;
  
  try {
    showLoading(submitBtn);
    
    const formData = {
      itemName: form.itemName.value.trim(),
      description: form.description.value.trim(),
      category: form.category.value,
      location: form.location.value.trim(),
      dateFound: form.dateFound.value,
      contactInfo: form.contactInfo?.value.trim() || ''
    };
    
    // Validation
    if (!formData.itemName || !formData.description || !formData.location || !formData.dateFound) {
      throw new Error('Please fill in all required fields');
    }
    
    if (formData.description.length < 10) {
      throw new Error('Description must be at least 10 characters long');
    }
    
    // Check if user is logged in
    if (isLoggedIn()) {
      // User is logged in, no need for contact info
      delete formData.contactInfo;
    } else {
      // User is not logged in, contact info is required
      if (!formData.contactInfo) {
        throw new Error('Contact information is required when not logged in');
      }
    }
    
    await submitFoundItem(formData);
  } catch (error) {
    console.error('Found item submission error:', error);
    showAlert('Failed to report found item. Please try again.', 'danger');
  } finally {
    hideLoading(submitBtn, originalText);
  }
}

// Setup report forms
function setupReportForms() {
  const lostItemForm = document.getElementById('lostItemForm');
  const foundItemForm = document.getElementById('foundItemForm');
  
  if (lostItemForm) {
    lostItemForm.addEventListener('submit', handleLostItemSubmit);
  }
  
  if (foundItemForm) {
    foundItemForm.addEventListener('submit', handleFoundItemSubmit);
  }
  
  // Set default date to today
  const dateInputs = document.querySelectorAll('input[type="date"]');
  const today = new Date().toISOString().split('T')[0];
  dateInputs.forEach(input => {
    if (!input.value) {
      input.value = today;
    }
  });
  
  // Show/hide contact info based on login status
  updateContactInfoVisibility();
}

// Update contact info field visibility
function updateContactInfoVisibility() {
  const contactInfoGroup = document.getElementById('contactInfoGroup');
  const contactInfoLabel = document.querySelector('label[for="contactInfo"]');
  
  if (isLoggedIn()) {
    // Hide contact info for logged in users
    if (contactInfoGroup) {
      contactInfoGroup.style.display = 'none';
    }
    if (contactInfoLabel) {
      contactInfoLabel.textContent = 'Contact Info (optional - will use your profile email)';
    }
  } else {
    // Show contact info for anonymous users
    if (contactInfoGroup) {
      contactInfoGroup.style.display = 'block';
    }
    if (contactInfoLabel) {
      contactInfoLabel.textContent = 'Contact Information *';
    }
  }
}

// Character counter for description fields
function setupCharacterCounters() {
  const descriptionFields = document.querySelectorAll('textarea[name="description"]');
  
  descriptionFields.forEach(field => {
    const counterId = field.id + 'Counter';
    let counter = document.getElementById(counterId);
    
    // Create counter if it doesn't exist
    if (!counter) {
      counter = document.createElement('div');
      counter.id = counterId;
      counter.className = 'text-sm text-secondary mt-1';
      counter.textContent = '0 / 500 characters';
      field.parentNode.insertBefore(counter, field.nextSibling);
    }
    
    // Update counter on input
    field.addEventListener('input', () => {
      const length = field.value.length;
      counter.textContent = `${length} / 500 characters`;
      
      if (length > 500) {
        counter.classList.add('text-danger');
        counter.classList.remove('text-secondary');
      } else {
        counter.classList.remove('text-danger');
        counter.classList.add('text-secondary');
      }
    });
  });
}

// Form validation helpers
function validateField(field) {
  const value = field.value.trim();
  let isValid = true;
  let errorMessage = '';
  
  // Remove existing error
  const existingError = field.parentNode.querySelector('.field-error');
  if (existingError) {
    existingError.remove();
  }
  
  field.classList.remove('is-invalid');
  
  // Required field validation
  if (field.hasAttribute('required') && !value) {
    isValid = false;
    errorMessage = 'This field is required';
  }
  
  // Email validation
  if (field.type === 'email' && value && !isValidEmail(value)) {
    isValid = false;
    errorMessage = 'Please enter a valid email address';
  }
  
  // Date validation (not in future)
  if (field.type === 'date' && value) {
    const selectedDate = new Date(value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (selectedDate > today) {
      isValid = false;
      errorMessage = 'Date cannot be in the future';
    }
  }
  
  // Show error if invalid
  if (!isValid) {
    field.classList.add('is-invalid');
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error text-danger text-sm mt-1';
    errorDiv.textContent = errorMessage;
    field.parentNode.appendChild(errorDiv);
  }
  
  return isValid;
}

// Email validation helper
function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

// Setup real-time validation
function setupFormValidation() {
  const form = document.querySelector('form');
  if (!form) return;
  
  const fields = form.querySelectorAll('input, textarea, select');
  
  fields.forEach(field => {
    // Validate on blur
    field.addEventListener('blur', () => validateField(field));
    
    // Remove error on input
    field.addEventListener('input', () => {
      if (field.classList.contains('is-invalid')) {
        validateField(field);
      }
    });
  });
}

// Initialize report page
document.addEventListener('DOMContentLoaded', () => {
  setupReportForms();
  setupCharacterCounters();
  setupFormValidation();
  updateContactInfoVisibility();
});
