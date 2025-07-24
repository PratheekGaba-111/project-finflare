#!/bin/bash

echo "🚀 Setting up FinFlare Development Environment"
echo "=============================================="

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Check if Java 17 is installed
if ! command -v java &> /dev/null; then
    echo "⚠️  Java is not installed. Installing via package manager..."
    # Try to install Java (Ubuntu/Debian)
    sudo apt update && sudo apt install -y openjdk-17-jdk
fi

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "⚠️  Node.js is not installed. Please install Node.js 18+ first."
    exit 1
fi

# Check if Python is installed
if ! command -v python3 &> /dev/null; then
    echo "⚠️  Python 3 is not installed. Installing via package manager..."
    sudo apt update && sudo apt install -y python3 python3-pip
fi

echo ""
echo "📦 Installing dependencies..."

# Install frontend dependencies
if [ -d "frontend" ]; then
    echo "Installing React dependencies..."
    cd frontend
    npm install
    cd ..
fi

# Install ML service dependencies
if [ -d "ml-service" ]; then
    echo "Installing Python ML dependencies..."
    cd ml-service
    pip3 install -r requirements.txt
    cd ..
fi

# Create necessary directories
mkdir -p sql

# Create basic SQL initialization script
cat > sql/init.sql << 'EOF'
-- FinFlare Database Initialization Script
CREATE DATABASE IF NOT EXISTS finflare;
USE finflare;

-- This file can be used for any initial data seeding
-- Tables will be created automatically by Spring Boot JPA
EOF

echo ""
echo "🔧 Development Setup Options:"
echo "1. Run with Docker Compose (Recommended)"
echo "2. Run services individually"
echo "3. Setup only (no start)"

read -p "Choose an option (1-3): " choice

case $choice in
    1)
        echo ""
        echo "🐳 Starting all services with Docker Compose..."
        docker-compose up --build
        ;;
    2)
        echo ""
        echo "🏃 Starting services individually..."
        
        # Start MySQL
        echo "Starting MySQL..."
        docker run -d --name finflare-mysql \
            -e MYSQL_DATABASE=finflare \
            -e MYSQL_USER=root \
            -e MYSQL_PASSWORD=password \
            -e MYSQL_ROOT_PASSWORD=password \
            -p 3306:3306 \
            mysql:8.0
        
        # Start ML Service
        echo "Starting ML Service..."
        cd ml-service
        python3 app.py &
        ML_PID=$!
        cd ..
        
        # Start Backend
        echo "Starting Spring Boot Backend..."
        cd backend
        ./mvnw spring-boot:run &
        BACKEND_PID=$!
        cd ..
        
        # Start Frontend
        echo "Starting React Frontend..."
        cd frontend
        npm start &
        FRONTEND_PID=$!
        cd ..
        
        echo ""
        echo "✅ All services started!"
        echo "🌐 Frontend: http://localhost:3000"
        echo "🔧 Backend API: http://localhost:8080/api"
        echo "🤖 ML Service: http://localhost:5000"
        echo ""
        echo "Press Ctrl+C to stop all services..."
        
        # Wait for interrupt
        trap "echo 'Stopping services...'; kill $ML_PID $BACKEND_PID $FRONTEND_PID; docker stop finflare-mysql; docker rm finflare-mysql; exit 0" INT
        wait
        ;;
    3)
        echo ""
        echo "✅ Setup completed! You can now start services manually."
        ;;
    *)
        echo "Invalid option. Exiting..."
        exit 1
        ;;
esac

echo ""
echo "🎉 FinFlare setup completed!"
echo ""
echo "📋 Quick Start Guide:"
echo "1. Open http://localhost:3000 in your browser"
echo "2. Create a new account or login"
echo "3. Start managing your finances with AI-powered features!"
echo ""
echo "🔗 Available URLs:"
echo "   Frontend: http://localhost:3000"
echo "   Backend API: http://localhost:8080/api"
echo "   API Documentation: http://localhost:8080/api/swagger-ui.html"
echo "   ML Service: http://localhost:5000"
echo ""
echo "📚 Features:"
echo "   ✓ JWT Authentication"
echo "   ✓ AI-Powered Expense Categorization"
echo "   ✓ Budget Management with Smart Alerts"
echo "   ✓ Investment Portfolio Simulation"
echo "   ✓ Financial Forecasting"
echo "   ✓ Gamification System"
echo "   ✓ OCR Receipt Processing (Frontend)"
echo "   ✓ Voice Commands (Frontend)"
echo "   ✓ Interactive Charts and Reports"
echo ""
echo "Happy coding! 🚀"