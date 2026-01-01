# How to Create MySQL Data and Connect Using Username & Password

## Step 1: Create Database and Tables

### Option A: Using MySQL Workbench (Easiest)

1. **Open MySQL Workbench**
2. **Connect to your MySQL server** (enter your MySQL root password)
3. **Open the SQL file:**
   - File → Open SQL Script
   - Select `database.sql` from your project
4. **Run the script:**
   - Click the ⚡ Execute button (or press Ctrl+Shift+Enter)
   - Wait for "Success" message

### Option B: Using MySQL Command Line

1. **Open Command Prompt/Terminal**
2. **Login to MySQL:**
   ```bash
   mysql -u root -p
   ```
   (Enter your MySQL password when prompted)

3. **Run the SQL file:**
   ```sql
   source C:/Users/ranik/Desktop/campus-lost-found/database.sql;
   ```
   Or copy-paste the contents of `database.sql` and execute

---

## Step 2: Configure Connection in Your Code

### Edit `src/main/java/dao/DBConnection.java`

**Current settings:**
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_lost_found";
private static final String DB_USER = "root";           // Your MySQL username
private static final String DB_PASSWORD = "rani";       // Your MySQL password
```

**Change these to match YOUR MySQL credentials:**
- `DB_USER` = Your MySQL username (usually "root")
- `DB_PASSWORD` = Your MySQL password
- `DB_URL` = Keep as is (unless MySQL is on different port/host)

**Example:**
```java
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_actual_password";
```

---

## Step 3: Add Test Data (Optional)

You can insert test data directly in MySQL:

### Using MySQL Workbench:

1. Open MySQL Workbench
2. Select `campus_lost_found` database
3. Run these SQL commands:

```sql
USE campus_lost_found;

-- Add test users
INSERT INTO users (name, email, password, role) VALUES
('John Doe', 'john@campus.com', 'password123', 'user'),
('Jane Smith', 'jane@campus.com', 'password123', 'user');

-- Add test lost items (user_id = 1 is admin, 2 is first user above)
INSERT INTO lost_items (item_name, description, location, lost_date, user_id, status) VALUES
('Laptop', 'Dell Inspiron 15', 'Library', '2024-01-15', 2, 'open'),
('Phone', 'iPhone 13 Pro', 'Cafeteria', '2024-01-20', 2, 'open');

-- Add test found items
INSERT INTO found_items (item_name, description, location, found_date, user_id, status) VALUES
('Keys', 'Car keys with keychain', 'Parking Lot', '2024-01-25', 2, 'open'),
('Wallet', 'Black leather wallet', 'Library', '2024-01-26', 2, 'open');
```

---

## Step 4: Verify Connection

### Test Database Connection:

1. **Start your application:**
   ```bash
   mvn clean package
   mvn tomcat7:run
   ```

2. **Open in browser:**
   ```
   http://localhost:8080/campus-lost-found/testdb
   ```

3. **You should see:**
   - ✓ Database connection successful!
   - ✓ Users table exists
   - List of all users
   - ✓ Admin login test successful!

---

## Step 5: Login and Use

1. **Go to login page:**
   ```
   http://localhost:8080/campus-lost-found/login.jsp
   ```

2. **Login with:**
   - Email: `admin@campus.com`
   - Password: `admin123`

3. **Or create new account** and login

---

## Troubleshooting

### ❌ "Access denied for user"
**Problem:** Wrong username or password
**Solution:** 
- Check `DBConnection.java` - verify username and password
- Test in MySQL Workbench with same credentials

### ❌ "Unknown database 'campus_lost_found'"
**Problem:** Database doesn't exist
**Solution:** Run `database.sql` file again

### ❌ "Table doesn't exist"
**Problem:** Tables not created
**Solution:** Run `database.sql` file completely

### ❌ "Connection refused"
**Problem:** MySQL server not running
**Solution:** Start MySQL service

---

## Quick Reference

### Your MySQL Credentials:
- **Host:** localhost
- **Port:** 3306
- **Database:** campus_lost_found
- **Username:** (check DBConnection.java)
- **Password:** (check DBConnection.java)

### Connection String Format:
```
jdbc:mysql://localhost:3306/campus_lost_found
```

### Files to Edit:
- `src/main/java/dao/DBConnection.java` - Connection settings
- `database.sql` - Database structure

---

## Summary

1. ✅ Run `database.sql` in MySQL → Creates database and tables
2. ✅ Edit `DBConnection.java` → Set your MySQL username/password
3. ✅ Start application → Connects to MySQL using those credentials
4. ✅ Login and use → Data is stored in MySQL

That's it! Your application now connects to MySQL using your username and password! 🎉

