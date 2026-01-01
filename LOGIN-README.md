# Login System Documentation

## Test Credentials

The login system is now fully functional with the following test users:

### Admin User
- **Email:** admin@campus.com
- **Password:** admin123
- **Role:** ADMIN
- **Redirects to:** admin/dashboard.html

### Regular Users
- **Email:** john@campus.com
- **Password:** password123
- **Role:** USER
- **Redirects to:** items.html

- **Email:** jane@campus.com
- **Password:** password123
- **Role:** USER
- **Redirects to:** items.html

- **Email:** mike@campus.com
- **Password:** password123
- **Role:** USER
- **Redirects to:** items.html

- **Email:** sarah@campus.com
- **Password:** password123
- **Role:** USER
- **Redirects to:** items.html

## Features Implemented

### Frontend (login.html)
- ✅ Modern, responsive card-based design
- ✅ Password visibility toggle (👁️/👁️‍🗨️)
- ✅ Real-time form validation
- ✅ Email format validation
- ✅ Loading states with spinner animation
- ✅ Success and error message display
- ✅ Field-level error messages
- ✅ Role-based redirection
- ✅ Mobile responsive design

### Backend Integration
- ✅ JWT token authentication
- ✅ Secure password hashing with BCrypt
- ✅ Role-based access control
- ✅ Proper error handling
- ✅ CORS configuration

### Security Features
- ✅ Password encryption (BCrypt)
- ✅ JWT token-based authentication
- ✅ Input validation
- ✅ SQL injection prevention
- ✅ XSS protection

## API Endpoints

### POST /api/auth/login
**Request:**
```json
{
  "email": "admin@campus.com",
  "password": "admin123"
}
```

**Response (Success):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "userId": 2,
  "email": "admin@campus.com",
  "name": "Admin User",
  "role": "ADMIN"
}
```

**Response (Error):**
```json
{
  "timestamp": "2026-01-01T10:03:27.376055",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Bad credentials"
}
```

## Setup Instructions

1. **Database Setup:**
   ```bash
   # Run the database schema
   mysql -u root -p < database.sql
   
   # Run the sample data with encoded passwords
   mysql -u root -p < sample-data-encoded.sql
   ```

2. **Backend Setup:**
   ```bash
   # Start the Spring Boot application
   mvn spring-boot:run
   ```

3. **Frontend Setup:**
   - Open `login.html` in a web browser
   - Or serve through a web server

## File Structure

```
login.html                 # Main login page with improved UI
frontend/css/style.css      # Updated with alert-error class
frontend/js/api.js          # API utilities and authentication functions
frontend/js/auth.js         # Authentication-specific functions
sample-data-encoded.sql    # Test users with BCrypt-encoded passwords
src/main/java/.../util/PasswordEncoder.java  # Utility to encode passwords
```

## Testing

1. Open `login.html` in your browser
2. Try the test credentials above
3. Verify role-based redirection works correctly
4. Test form validation with invalid inputs
5. Test password visibility toggle
6. Verify error handling for wrong credentials
