# Campus Lost & Found - Setup Guide

## 🚀 Complete Setup Instructions

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+
- Node.js (optional, for frontend development)

---

## 📋 Database Setup

### 1. Install MySQL
```bash
# On macOS with Homebrew
brew install mysql

# On Ubuntu/Debian
sudo apt-get install mysql-server

# On Windows
# Download from https://dev.mysql.com/downloads/installer/
```

### 2. Start MySQL Service
```bash
# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 3. Create Database
```bash
mysql -u root -p
```

Then run the SQL script:
```sql
source /path/to/project/database-schema.sql
```

Or execute directly:
```bash
mysql -u root -p < database-schema.sql
```

### 4. Update Database Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/campus_lost_found?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

## 🔧 Backend Setup (Spring Boot)

### 1. Build the Project
```bash
# Navigate to project directory
cd "Campus lost and found"

# Update Maven dependencies
mvn clean install

# Or use the new backend-pom.xml
mvn -f backend-pom.xml clean install
```

### 2. Run the Application
```bash
# Using Maven
mvn spring-boot:run

# Or run the JAR file
java -jar target/campus-lost-found-backend-1.0.0.jar
```

### 3. Verify Backend is Running
Open your browser and visit:
- http://localhost:8080/api/auth/me (should return 401 Unauthorized - this is expected)
- http://localhost:8080/api/items/lost (should return empty array or sample data)

---

## 🎨 Frontend Setup

### 1. Serve the Frontend
Since the frontend is static HTML/CSS/JS, you can serve it in multiple ways:

#### Option A: Simple HTTP Server (Recommended for development)
```bash
# Using Python 3
python -m http.server 3000

# Using Node.js
npx serve . -p 3000

# Using PHP
php -S localhost:3000
```

#### Option B: Live Server Extension (VS Code)
Install the "Live Server" extension and right-click `index.html` → "Open with Live Server"

#### Option C: Spring Boot Static Resources
Move the frontend files to `src/main/resources/static/` and they'll be served automatically.

---

## 🔐 Default Credentials

### Admin User
- Email: `admin@campus.edu`
- Password: `admin123`

### Sample Users (from database)
- Email: `john.doe@campus.edu`
- Password: `password123`

---

## 🧪 Testing the Integration

### 1. Test Registration
1. Open http://localhost:3000/register.html
2. Create a new account
3. Should redirect to dashboard after successful registration

### 2. Test Login
1. Open http://localhost:3000/login.html
2. Login with admin credentials
3. Should redirect to index.html

### 3. Test Item Reporting
1. Login to the system
2. Go to "Report Lost" or "Report Found"
3. Fill out the form and submit
4. Check the items list to see your submission

### 4. Test API Endpoints
Use Postman or curl to test the API:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@campus.edu","password":"admin123"}'

# Get all lost items (with JWT token)
curl -X GET http://localhost:8080/api/items/lost \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🚀 Deployment

### Option 1: Traditional VPS
1. Install Java 17, MySQL, and Nginx
2. Build the JAR file: `mvn clean package`
3. Set up MySQL database
4. Configure Nginx to serve frontend and proxy API calls
5. Run the JAR as a service

### Option 2: Docker
```dockerfile
# Create a Dockerfile
FROM openjdk:17-jdk-slim
COPY target/campus-lost-found-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
# Build and run
docker build -t campus-lost-found .
docker run -p 8080:8080 campus-lost-found
```

### Option 3: Cloud Platforms
- **Render**: Connect GitHub repo, add MySQL add-on, set environment variables
- **Railway**: Similar to Render, supports Java and MySQL
- **AWS EC2**: Deploy as traditional VPS or use Elastic Beanstalk

---

## 🔧 Configuration

### Environment Variables
Create `.env` file or set environment variables:
```bash
DB_HOST=localhost
DB_PORT=3306
DB_NAME=campus_lost_found
DB_USER=root
DB_PASSWORD=your_password
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION=86400000
```

### Production application.properties
```properties
# Server
server.port=8080

# Database
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/campus_lost_found}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD}

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}

# CORS
spring.web.cors.allowed-origins=${FRONTEND_URL:http://localhost:3000}
```

---

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure MySQL is running
   - Check credentials in application.properties
   - Verify database exists

2. **CORS Errors**
   - Check CORS configuration in SecurityConfig
   - Ensure frontend URL is in allowed origins

3. **JWT Token Issues**
   - Check JWT secret configuration
   - Verify token is being sent in Authorization header

4. **Maven Build Errors**
   - Run `mvn clean install -U` to update dependencies
   - Check Java version compatibility

5. **Frontend Not Loading**
   - Ensure backend is running on port 8080
   - Check API base URL in assets/js/api.js

### Debug Mode
Enable debug logging:
```properties
logging.level.com.campus.lostfound=DEBUG
logging.level.org.springframework.security=DEBUG
```

---

## 📊 Features Implemented

### ✅ Authentication & Authorization
- JWT-based authentication
- Role-based access control (USER/ADMIN)
- Secure password hashing with BCrypt

### ✅ Core Features
- Report lost items
- Report found items
- Browse and search items
- Claim found items
- User profiles

### ✅ Admin Features
- Dashboard with statistics
- Manage claims
- View all users and items

### ✅ Security Features
- Input validation
- SQL injection prevention
- XSS protection
- CORS configuration

### ✅ Frontend Features
- Responsive design
- Real-time search
- Form validation
- Error handling
- Loading states

---

## 📞 Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Review the application logs
3. Verify database connectivity
4. Test API endpoints individually

The system is now **READY TO HOST**! 🎉
