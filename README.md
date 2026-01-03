# Campus Lost & Found - Frontend

A professional, multi-page frontend for the Campus Lost & Found system built with HTML, CSS, and vanilla JavaScript.

## 📁 Project Structure

```
frontend/
│
├── index.html          (Home page - Landing & dashboard)
├── login.html          (User authentication)
├── register.html       (New user registration)
├── items.html          (Browse lost & found items)
├── item-details.html   (Detailed item view)
├── report-lost.html    (Report lost item form)
├── report-found.html   (Report found item form)
├── profile.html        (User profile & dashboard)
├── admin.html          (Admin dashboard)
├── contact.html        (Contact information & support)
│
├── css/
│   └── style.css       (Professional, modern UI styles)
│
└── js/
    ├── api.js          (Base API configuration & utilities)
    ├── auth.js         (Login, logout, JWT handling)
    ├── items.js        (Items browsing & management)
    ├── report.js       (Form submission & validation)
    └── admin.js        (Admin dashboard functionality)
```

## 🚀 Features

### Core Functionality
- **JWT Authentication**: Secure login/logout with token management
- **API Integration**: Full REST API communication with Spring Boot backend
- **Responsive Design**: Mobile-first, professional UI
- **Form Validation**: Real-time client-side validation
- **Error Handling**: Graceful error messages and loading states

### User Features
- Browse and search lost/found items
- Report lost or found items with detailed forms
- View item details and claim items
- User profile with statistics and history
- Contact campus security

### Admin Features
- User management (role changes, deletion)
- Item management (resolve, delete items)
- Platform statistics and analytics
- Data export functionality
- Search and filtering capabilities

## 🔧 Technical Implementation

### Frontend Architecture
- **Vanilla JavaScript**: No frameworks, pure JS implementation
- **Modular Design**: Separate files for different functionalities
- **API-First**: All backend communication via REST APIs
- **Local Storage**: JWT token and user data persistence
- **Component-Based**: Reusable UI patterns

### Security Features
- JWT token authentication
- Authorization header injection
- Role-based access control
- Input sanitization and validation
- Safe HTML rendering

### API Integration
```javascript
// Example API call
const response = await api.post('/auth/login', { email, password });
localStorage.setItem('jwtToken', response.token);
```

### Authentication Flow
```javascript
// Login
async function login(email, password) {
  const response = await api.post('/auth/login', { email, password });
  localStorage.setItem('jwtToken', response.token);
  localStorage.setItem('user', JSON.stringify(response.user));
}

// API calls with JWT
const headers = {
  'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
};
```

## 🎨 UI/UX Design

### Modern Design System
- **Color Palette**: Professional blue/green theme
- **Typography**: Clean, readable fonts
- **Components**: Cards, buttons, forms, alerts
- **Responsive**: Mobile-first approach
- **Accessibility**: Semantic HTML, ARIA labels

### Key Components
- Navigation bar with dynamic auth links
- Hero sections with call-to-action
- Card-based layouts for items
- Form validation with real-time feedback
- Loading states and error messages
- Tab-based interfaces (profile, admin)

## 📱 Pages Overview

### 1. Home (`index.html`)
- Landing page with hero section
- Recent items display
- Platform statistics
- Feature highlights

### 2. Authentication (`login.html`, `register.html`)
- Clean login/register forms
- Demo account information
- Validation and error handling

### 3. Items Browsing (`items.html`, `item-details.html`)
- Search and filter functionality
- Grid layout for items
- Detailed item views
- Claim functionality

### 4. Reporting (`report-lost.html`, `report-found.html`)
- Comprehensive forms
- Character counters
- Dynamic contact info (based on auth)
- Tips and guidelines

### 5. User Profile (`profile.html`)
- User statistics dashboard
- Tab-based interface
- Settings management
- Report history

### 6. Admin Dashboard (`admin.html`)
- System statistics
- User management
- Item management
- Data export

### 7. Contact (`contact.html`)
- Campus security information
- Contact form
- FAQ section
- Emergency information

## 🔌 API Endpoints Used

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/me` - Get current user

### Items
- `GET /api/items/lost` - Get lost items
- `GET /api/items/found` - Get found items
- `POST /api/items/lost` - Report lost item
- `POST /api/items/found` - Report found item
- `GET /api/items/lost/:id` - Get lost item details
- `GET /api/items/found/:id` - Get found item details

### Claims
- `POST /api/claims` - Submit claim
- `GET /api/claims/my` - Get user claims

### Admin
- `GET /api/admin/stats` - Platform statistics
- `GET /api/admin/users` - All users
- `PUT /api/admin/users/:id/role` - Update user role
- `DELETE /api/admin/users/:id` - Delete user
- `GET /api/admin/items` - All items
- `PUT /api/admin/items/:type/:id/resolve` - Resolve item
- `DELETE /api/admin/items/:type/:id` - Delete item

## 🚀 Getting Started

### Prerequisites
- Spring Boot backend running on `http://localhost:8080`
- Modern web browser

### Setup
1. Open any HTML file in your browser
2. For development, use a local server:
   ```bash
   # Using Python
   python -m http.server 8000
   
   # Using Node.js
   npx serve .
   ```

### Demo Account
- **Email**: admin@campus.edu
- **Password**: admin123

## 🛡️ Security Considerations

- JWT tokens stored in localStorage (production should use httpOnly cookies)
- Input sanitization to prevent XSS
- API calls with proper authorization headers
- Role-based access control
- Form validation on both client and server side

## 📱 Browser Compatibility

- Chrome 80+
- Firefox 75+
- Safari 13+
- Edge 80+

## 🔄 Future Enhancements

- PWA capabilities (offline support)
- Image upload for items
- Real-time notifications
- Advanced search filters
- Mobile app integration
- Email notifications

## 📄 License

This project is part of the Campus Lost & Found system.
