#!/bin/bash

# Campus Lost & Found Deployment Script
# This script provides multiple deployment options

set -e

echo "🚀 Campus Lost & Found Deployment Script"
echo "=========================================="

# Function to check if Docker is installed
check_docker() {
    if ! command -v docker &> /dev/null; then
        echo "❌ Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        echo "❌ Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
    
    echo "✅ Docker and Docker Compose are installed"
}

# Function to deploy with Docker Compose
deploy_docker() {
    echo "🐳 Deploying with Docker Compose..."
    
    # Build and start containers
    docker-compose down
    docker-compose build
    docker-compose up -d
    
    echo "⏳ Waiting for services to start..."
    sleep 30
    
    # Check if services are running
    if docker-compose ps | grep -q "Up"; then
        echo "✅ Services are running!"
        echo "🌐 Frontend: http://localhost"
        echo "🔧 Backend API: http://localhost/api"
        echo "📊 Database: localhost:3306"
    else
        echo "❌ Some services failed to start. Check logs with: docker-compose logs"
        exit 1
    fi
}

# Function to create JAR deployment package
create_jar_package() {
    echo "📦 Creating JAR deployment package..."
    
    # Create deployment directory
    mkdir -p deployment-package
    
    # Copy necessary files
    cp target/campus-lost-found-1.0.0.jar deployment-package/
    cp database-schema.sql deployment-package/
    cp README-SETUP.md deployment-package/
    cp .env.production deployment-package/.env
    
    # Create start script
    cat > deployment-package/start.sh << 'EOF'
#!/bin/bash
# Campus Lost & Found JAR Deployment

echo "🚀 Starting Campus Lost & Found Application..."

# Check Java version
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17+"
    exit 1
fi

# Set environment variables
export $(cat .env | xargs)

# Start the application
java -jar campus-lost-found-1.0.0.jar
EOF
    
    chmod +x deployment-package/start.sh
    
    # Create systemd service file
    cat > deployment-package/campus-lost-found.service << 'EOF'
[Unit]
Description=Campus Lost & Found Application
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/opt/campus-lost-found
EnvironmentFile=/opt/campus-lost-found/.env
ExecStart=/usr/bin/java -jar /opt/campus-lost-found/campus-lost-found-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF
    
    echo "✅ JAR package created in deployment-package/"
    echo "📋 Package contains:"
    echo "   - campus-lost-found-1.0.0.jar (Application)"
    echo "   - database-schema.sql (Database schema)"
    echo "   - .env (Environment variables)"
    echo "   - start.sh (Startup script)"
    echo "   - campus-lost-found.service (Systemd service)"
}

# Function to deploy to cloud (instructions)
cloud_deployment_info() {
    echo "☁️ Cloud Deployment Options:"
    echo ""
    echo "1. Render.com:"
    echo "   - Connect your GitHub repository"
    echo "   - Add MySQL add-on"
    echo "   - Set environment variables from .env.production"
    echo "   - Deploy automatically"
    echo ""
    echo "2. Railway.app:"
    echo "   - Import from GitHub"
    echo "   - Add MySQL service"
    echo "   - Configure environment variables"
    echo ""
    echo "3. AWS EC2:"
    echo "   - Launch Ubuntu 20.04+ instance"
    echo "   - Install Docker and run: ./deploy.sh docker"
    echo "   - Or use JAR package with systemd service"
    echo ""
    echo "4. DigitalOcean App Platform:"
    echo "   - Connect GitHub repo"
    echo "   - Add Dev Database (MySQL)"
    echo "   - Configure build and run commands"
}

# Main deployment logic
case "${1:-help}" in
    "docker")
        check_docker
        deploy_docker
        ;;
    "jar")
        create_jar_package
        ;;
    "cloud")
        cloud_deployment_info
        ;;
    "help"|*)
        echo "Usage: $0 [docker|jar|cloud|help]"
        echo ""
        echo "Commands:"
        echo "  docker  - Deploy using Docker Compose (recommended)"
        echo "  jar     - Create JAR deployment package"
        echo "  cloud   - Show cloud deployment options"
        echo "  help    - Show this help message"
        echo ""
        echo "Quick start: $0 docker"
        ;;
esac
