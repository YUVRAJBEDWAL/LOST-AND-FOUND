# How to Open Campus Lost & Found in Chrome Browser

## Step 1: Make Sure Tomcat is Running

### If using Maven (VS Code):
```bash
mvn clean package
mvn tomcat7:run
```

### If using Eclipse:
- Right-click Tomcat server → **Start**
- Wait until status shows: **Started, Synchronized**

### If using standalone Tomcat:
- Run `bin/startup.bat` (Windows) or `bin/startup.sh` (Linux/Mac)

---

## Step 2: Open Chrome Browser

### Option A: Direct Login Page (Recommended)
Copy and paste this URL in Chrome address bar:

```
http://localhost:8080/campus-lost-found/login.jsp
```

### Option B: Home Page (Auto-redirects to login)
```
http://localhost:8080/campus-lost-found/
```

### Option C: Test Database Connection First
```
http://localhost:8080/campus-lost-found/testdb
```

---

## Step 3: Login Credentials

**Admin Account:**
- Email: `admin@campus.com`
- Password: `admin123`

**Or Register New User:**
- Click "Register here" link on login page

---

## Step 4: Available Pages (After Login)

- **Dashboard:** `http://localhost:8080/campus-lost-found/dashboard.jsp`
- **Report Lost:** `http://localhost:8080/campus-lost-found/addLost.jsp`
- **Report Found:** `http://localhost:8080/campus-lost-found/addFound.jsp`
- **View Lost Items:** `http://localhost:8080/campus-lost-found/viewLost.jsp`
- **View Found Items:** `http://localhost:8080/campus-lost-found/viewFound.jsp`
- **Admin Panel:** `http://localhost:8080/campus-lost-found/admin` (Admin only)

---

## Troubleshooting

### ❌ "This site can't be reached" or "ERR_CONNECTION_REFUSED"
**Solution:** Tomcat is NOT running!
- Start Tomcat first (see Step 1)
- Check if port 8080 is available

### ❌ "404 Not Found"
**Solution:** Check your project name/context path
- The URL path `/campus-lost-found/` must match your deployed WAR name
- If different, replace `campus-lost-found` with your actual project name

### ❌ Page loads but shows errors
**Solution:** Check Tomcat console for errors
- Database connection issues?
- Missing dependencies?
- Check console logs

---

## Quick Test Checklist

✅ Tomcat is running (check `http://localhost:8080/` shows Tomcat page)
✅ Database is running and `campus_lost_found` database exists
✅ MySQL password in `DBConnection.java` is correct
✅ Project is deployed to Tomcat

---

## Bookmark These URLs

Save these in Chrome bookmarks for easy access:

1. **Login:** `http://localhost:8080/campus-lost-found/login.jsp`
2. **Dashboard:** `http://localhost:8080/campus-lost-found/dashboard.jsp`
3. **Test DB:** `http://localhost:8080/campus-lost-found/testdb`

