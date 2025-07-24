# FinFlare - Personal Finance Management Platform

A comprehensive FinTech platform built with Spring Boot (Java) backend and React frontend for personal finance management, expense tracking, budgeting, and investment simulation.

## 🚀 Features

### Core Features
- **User Authentication** - JWT-based secure authentication
- **Expense Tracker** - Manual entry and OCR-based expense tracking
- **AI Categorization** - Smart expense categorization using ML
- **Monthly Reports** - Visual reports with charts and trends
- **Budgeting Engine** - Set budgets with smart alerts
- **Financial Forecasting** - AI-powered savings and spending predictions
- **Investment Simulator** - Mock portfolio management
- **Gamification** - Rewards, leaderboards, and savings streaks
- **Voice Assistant** - NLP-based finance query assistant

### Technology Stack
- **Backend**: Spring Boot 3.x, Java 17, MySQL, JWT Authentication
- **Frontend**: React 18, TypeScript, Chart.js, Tailwind CSS
- **AI/ML**: Python integration for classification and forecasting
- **OCR**: Tesseract.js for receipt processing
- **Voice**: Web Speech API for voice commands

## 📁 Project Structure

```
finflare/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/
│   ├── src/main/resources/
│   └── pom.xml
├── frontend/               # React frontend
│   ├── src/
│   ├── public/
│   └── package.json
├── ml-service/            # Python ML service
│   ├── models/
│   ├── requirements.txt
│   └── app.py
└── docker-compose.yml     # Development environment
```

## 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- Node.js 18+
- Python 3.9+
- MySQL 8.0+

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

### ML Service Setup
```bash
cd ml-service
pip install -r requirements.txt
python app.py
```

## 🔧 Configuration

### Database Configuration
Create a MySQL database named `finflare` and update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finflare
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Environment Variables
```bash
JWT_SECRET=your_jwt_secret_key
ML_SERVICE_URL=http://localhost:5000
```

## 📊 API Documentation

The REST API provides endpoints for:
- Authentication (`/api/auth/*`)
- Expenses (`/api/expenses/*`)
- Budgets (`/api/budgets/*`)
- Investments (`/api/investments/*`)
- Reports (`/api/reports/*`)
- Gamification (`/api/gamification/*`)

## 🎯 Usage

1. Register/Login to access the platform
2. Add expenses manually or upload receipts for OCR processing
3. Set monthly budgets and track spending
4. View AI-generated financial forecasts
5. Simulate investments with the portfolio tracker
6. Earn rewards through the gamification system
7. Use voice commands for quick queries

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.