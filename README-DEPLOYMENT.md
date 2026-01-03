# Campus Lost & Found - Deployment Guide

## 🚀 Quick Deployment Options

### Option 1: Docker Compose (Recommended)
```bash
# Deploy with Docker
./deploy.sh docker
```

This will:
- Set up MySQL database
- Deploy Spring Boot application
- Configure Nginx for frontend
- Everything runs on ports 80 (frontend) and 8080 (API)

### Option 2: JAR Package
```bash
# Create deployment package
./deploy.sh jar
```

This creates a `deployment-package/` folder with:
- Application JAR file
- Database schema
- Environment configuration
- Startup scripts
- Systemd service file

### Option 3: Cloud Deployment
```bash
# View cloud deployment options
./deploy.sh cloud
```

---

## 🐳 Docker Deployment Details

### Prerequisites
- Docker installed
- Docker Compose installed

### Services
- **MySQL**: Port 3306
- **Spring Boot App**: Port 8080
- **Nginx**: Port 80 (serves frontend)

### Environment Variables
Edit `.env.production` to customize:
- Database credentials
- JWT secrets
- CORS settings

### Commands
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild and restart
docker-compose down && docker-compose build && docker-compose up -d
```

---

## 📦 JAR Deployment Details

### System Requirements
- Java 17+
- MySQL 8.0+
- 2GB RAM minimum

### Setup Steps
1. Extract deployment package
2. Set up MySQL database
3. Import database schema
4. Configure environment variables
5. Run startup script

### Systemd Service (Linux)
```bash
# Copy service file
sudo cp deployment-package/campus-lost-found.service /etc/systemd/system/

# Reload systemd
sudo systemctl daemon-reload

# Enable and start service
sudo systemctl enable campus-lost-found
sudo systemctl start campus-lost-found

# Check status
sudo systemctl status campus-lost-found
```

---

## ☁️ Cloud Platform Deployment

### Render.com
1. Connect GitHub repository
2. Add MySQL add-on
3. Set environment variables:
   - `DATABASE_URL` (from Render MySQL)
   - `JWT_SECRET`
   - `SPRING_PROFILES_ACTIVE=production`
4. Deploy automatically

### Railway.app
1. Import from GitHub
2. Add MySQL service
3. Configure environment variables
4. Railway handles deployment automatically

### AWS EC2
1. Launch Ubuntu 20.04+ instance
2. Install Docker:
   ```bash
   curl -fsSL https://get.docker.com -o get-docker.sh
   sudo sh get-docker.sh
   sudo usermod -aG docker ubuntu
   ```
3. Clone repository and run:
   ```bash
   ./deploy.sh docker
   ```

### DigitalOcean App Platform
1. Connect GitHub repository
2. Add Dev Database (MySQL)
3. Configure:
   - Build Command: `mvn clean package`
   - Run Command: `java -jar target/campus-lost-found-1.0.0.jar`
   - Environment variables from `.env.production`

---

## 🔧 Configuration

### Production Settings
- Database connection pooling enabled
- CORS configured for production domains
- JWT tokens with 24-hour expiration
- Health checks enabled
- Logging optimized for production

### Security Considerations
- Change default JWT secret in production
- Use strong database passwords
- Enable SSL/HTTPS in production
- Configure firewall rules
- Regular security updates

---

## 📊 Monitoring

### Health Endpoints
- Application: `GET /api/auth/me` (returns 401 if healthy)
- Database: Auto-configured health checks

### Logs
- Docker: `docker-compose logs -f app`
- JAR: Application logs to console
- Systemd: `journalctl -u campus-lost-found -f`

---

## 🚨 Troubleshooting

### Common Issues
1. **Database Connection Failed**
   - Check MySQL is running: `docker-compose ps mysql`
   - Verify credentials in `.env.production`
   - Check database exists

2. **Port Already in Use**
   - Stop other services on ports 80/8080
   - Or modify ports in `docker-compose.yml`

3. **Memory Issues**
   - Increase Docker memory allocation
   - Check JVM heap size settings

4. **CORS Errors**
   - Update allowed origins in `.env.production`
   - Restart application after changes

### Debug Commands
```bash
# Check container status
docker-compose ps

# View application logs
docker-compose logs app

# Access database container
docker-compose exec mysql mysql -u root -p

# Restart specific service
docker-compose restart app
```

---

## 📞 Support

For deployment issues:
1. Check this guide first
2. Review application logs
3. Verify environment configuration
4. Test database connectivity

The application is now **DEPLOYMENT READY**! 🎉
