# How to Run Campus Lost & Found on VS Code

## Prerequisites

1. **Java JDK 11+** installed
2. **Apache Maven** installed (download from https://maven.apache.org/)
3. **MySQL** installed and running
4. **Apache Tomcat 9+** installed (optional, if not using Maven Tomcat plugin)

## Step 1: Install VS Code Extensions

Open VS Code and install these extensions:

1. **Extension Pack for Java** (by Microsoft)
   - Includes: Language Support for Java, Debugger for Java, Test Runner for Java, Maven for Java, etc.

2. **Tomcat for Java** (optional, by Weidong Xu) - for easier Tomcat integration

## Step 2: Setup MySQL Database

1. Open MySQL Workbench or command line
2. Run the `database.sql` file:
   ```sql
   SOURCE C:/Users/ranik/Desktop/campus-lost-found/database.sql;
   ```
   Or copy-paste the contents and execute.

3. Verify database `campus_lost_found` is created with tables: `users`, `lost_items`, `found_items`, `claims`

## Step 3: Configure Database Connection

Edit `src/main/java/dao/DBConnection.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_lost_found";
private static final String DB_USER = "root";  // Your MySQL username
private static final String DB_PASSWORD = "your_password";  // Your MySQL password
```

## Step 4: Build the Project

Open terminal in VS Code (Ctrl + `) and run:

```bash
mvn clean compile
```

This will:
- Download dependencies (MySQL connector, Servlet API, etc.)
- Compile your Java files

## Step 5: Run on Tomcat (Choose ONE method)

### Method A: Using Maven Tomcat Plugin (EASIEST)

```bash
mvn clean package
mvn tomcat7:run
```

Then open browser: `http://localhost:8080/campus-lost-found/login.jsp`

### Method B: Manual WAR Deployment

1. **Build WAR file:**
   ```bash
   mvn clean package
   ```
   This creates `target/campus-lost-found.war`

2. **Deploy to Tomcat:**
   - Copy `target/campus-lost-found.war` to Tomcat's `webapps` folder
   - Start Tomcat: Run `bin/startup.bat` (Windows) or `bin/startup.sh` (Linux/Mac)
   - Open browser: `http://localhost:8080/campus-lost-found/login.jsp`

### Method C: Using VS Code Tomcat Extension

1. Install "Tomcat for Java" extension
2. Configure Tomcat path in VS Code settings
3. Right-click `target/campus-lost-found.war` → "Run on Tomcat Server"

## Step 6: Access the Application

- **Login Page:** `http://localhost:8080/campus-lost-found/login.jsp`
- **Admin Login:** 
  - Email: `admin@campus.com`
  - Password: `admin123`
- **Register:** Click "Register" link on login page

## Troubleshooting

### Port 8080 already in use
- Change port in `pom.xml` (tomcat7-maven-plugin configuration) or stop other services

### MySQL Connection Error
- Check MySQL is running: `mysql -u root -p`
- Verify database exists: `SHOW DATABASES;`
- Check credentials in `DBConnection.java`

### ClassNotFoundException: com.mysql.cj.jdbc.Driver
- Run `mvn clean install` to download dependencies
- Check `pom.xml` has MySQL connector dependency

### 404 Not Found
- Verify WAR file name matches URL path
- Check `web.xml` welcome file is correct
- Ensure Tomcat extracted WAR file (check `webapps` folder)

## Project Structure

```
campus-lost-found/
├── src/main/java/          # Java source files
│   ├── model/              # Model classes
│   ├── dao/                # Data Access Objects
│   └── servlet/            # Servlet classes
├── src/main/webapp/        # Web resources
│   ├── *.jsp               # JSP pages
│   └── WEB-INF/
│       └── web.xml         # Web configuration
├── database.sql            # Database schema
└── pom.xml                 # Maven configuration
```

## Quick Commands Reference

```bash
# Clean and compile
mvn clean compile

# Build WAR file
mvn clean package

# Run with Tomcat plugin
mvn tomcat7:run

# Run tests (if any)
mvn test
```

