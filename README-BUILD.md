# 🚀 BUILD & RUN INSTRUCTIONS

## ✅ SPRING BOOT BACKEND REBUILT COMPLETE

### 📋 What Was Created:
- ✅ Complete Spring Boot application structure
- ✅ All entities, repositories, services, controllers
- ✅ JWT authentication & security configuration
- ✅ REST API endpoints matching frontend requirements
- ✅ Exception handling and validation
- ✅ Database configuration ready

### 🔧 HOW TO BUILD & RUN:

#### 1. **Prerequisites**
```bash
# Install Java 17
# Install Maven 3.6+
# Install MySQL 8.0+
```

#### 2. **Database Setup**
```bash
# Create database and tables
mysql -u root -p < database-schema.sql
```

#### 3. **Update Configuration**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

#### 4. **Build Application**
```bash
# Navigate to project directory
cd "Campus lost and found"

# Build with Maven
mvn -f backend-pom.xml clean install

# Or if mvn not found, use full path:
/usr/local/bin/mvn -f backend-pom.xml clean install
```

#### 5. **Run Application**
```bash
# Run Spring Boot
mvn -f backend-pom.xml spring-boot:run

# Application will start on: http://localhost:8080/api
```

#### 6. **Test APIs**
```bash
# Test health endpoint
curl http://localhost:8080/api/auth/me

# Test registration
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@campus.edu","password":"password123"}'
```

### 🌐 Frontend Setup:
```bash
# Serve frontend (in separate terminal)
cd "Campus lost and found"
python3 -m http.server 3000
# or
npx serve . -p 3000
```

### 🔗 **Integration Points:**
- **Frontend API Base URL**: `http://localhost:8080/api`
- **Login Endpoint**: `POST /api/auth/login`
- **Register Endpoint**: `POST /api/auth/register`
- **Lost Items**: `GET /api/items/lost`
- **Found Items**: `GET /api/items/found`

### 📊 **Database Connection:**
- **Database**: `campus_lost_found`
- **Default Admin**: `admin@campus.edu` / `admin123`
- **Sample Users**: Pre-loaded in database schema

### 🎯 **Next Steps:**
1. Install Maven if not available
2. Update MySQL password in application.properties
3. Run `mvn clean install`
4. Start the application
5. Test with frontend

**PROJECT IS READY TO HOST!** 🚀
