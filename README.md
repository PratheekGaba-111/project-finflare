# 🔥 FinFlare - AI-Powered Personal Finance Tracker

A comprehensive full-stack personal finance management platform featuring AI-powered expense categorization, intelligent budgeting, and stunning modern UI with dark/light themes.

## ✨ Features

### 🔒 **Secure Authentication**
- JWT-based authentication with Spring Security
- Secure password hashing
- Login/Register pages with form validation
- Protected routes and user session management

### 💸 **Smart Expense Management**
- Create, read, update, delete expenses with full CRUD operations
- AI-powered expense categorization using OpenAI GPT
- Real-time search and filtering
- Multiple payment method support
- Notes and detailed tracking

### 📊 **Advanced Analytics Dashboard**
- Interactive Recharts with Pie charts, Line charts, and Area charts
- Real-time financial health scoring
- Category-wise spending breakdown
- Monthly spending trends analysis
- Budget progress tracking with visual indicators

### 🤖 **AI-Powered Features**
- OpenAI GPT integration for expense categorization
- AI chatbot for financial advice and insights
- Intelligent spending pattern analysis
- Budget recommendations and savings tips

### 🎯 **Smart Budgeting & Goals**
- Create and manage monthly budgets
- Real-time budget alerts and notifications
- Progress tracking with visual indicators
- Over-budget warnings and recommendations

### 💅 **Modern UI/UX**
- shadcn/ui components with Tailwind CSS
- Framer Motion animations and transitions
- Dark/Light theme support with persistence
- Fully responsive design for all devices
- Gradient backgrounds and modern card layouts

### 🔧 **Technical Excellence**
- Spring Boot 3.x with Java 17
- React 18 with TypeScript
- MySQL database with JPA/Hibernate
- RESTful API design with Swagger documentation
- Error handling and validation

## 🚀 Quick Start

### Prerequisites
- **Java 17+** - [Download](https://adoptium.net/)
- **Node.js 18+** - [Download](https://nodejs.org/)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/)
- **Git** - [Download](https://git-scm.com/)

### 🎯 One-Command Setup

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/finflare.git
cd finflare
```

2. **Database Setup**
```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE finflare;
exit
```

3. **Backend Setup**
```bash
cd backend
# The application will auto-create tables on first run
./mvnw spring-boot:run
```

4. **Frontend Setup** (in new terminal)
```bash
cd frontend
npm install
npm start
```

5. **Access the Application**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## 🔧 Configuration

### Environment Variables

Create `.env` files for sensitive configuration:

**Backend** (`backend/src/main/resources/application-local.properties`):
```properties
# Database
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JWT Secret (generate a strong secret)
app.jwt.secret=your_super_secret_jwt_key_min_256_bits

# OpenAI API (for AI features)
app.openai.api.key=your_openai_api_key
```

**Frontend** (`.env.local`):
```bash
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_OPENAI_API_KEY=your_openai_api_key
```

### 🤖 AI Features Setup

To enable AI categorization and chatbot:

1. Get an OpenAI API key from [OpenAI Platform](https://platform.openai.com/)
2. Add it to your backend configuration
3. Restart the backend server

## 📁 Project Structure

```
finflare/
├── 🎯 backend/                     # Spring Boot Backend
│   ├── src/main/java/com/finflare/
│   │   ├── 🔐 controller/          # REST API Controllers
│   │   ├── 📊 service/             # Business Logic
│   │   ├── 💾 repository/          # Data Access Layer
│   │   ├── 🏗️ model/               # Entity Classes
│   │   ├── 📝 dto/                 # Data Transfer Objects
│   │   └── 🔒 security/            # Security Configuration
│   └── 🔧 src/main/resources/      # Configuration Files
├── 💻 frontend/                    # React Frontend
│   ├── 🎨 src/components/          # Reusable Components
│   │   └── 🧩 ui/                  # shadcn/ui Components
│   ├── 📱 src/pages/               # Page Components
│   ├── 🔄 src/contexts/            # React Contexts
│   ├── 🛠️ src/services/            # API Services
│   └── 🎯 src/types/               # TypeScript Types
└── 📚 README.md                    # This file
```

## 🛠️ API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Expenses
- `GET /api/expenses` - Get all expenses
- `POST /api/expenses` - Create new expense
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

### Budgets
- `GET /api/budgets` - Get all budgets
- `POST /api/budgets` - Create new budget
- `PUT /api/budgets/{id}` - Update budget
- `GET /api/budgets/alerts` - Get budget alerts

### AI Features
- `POST /api/ai/categorize` - AI expense categorization
- `POST /api/ai/chat` - Chat with AI assistant
- `GET /api/ai/spending-analysis` - AI spending insights

### Dashboard
- `GET /api/dashboard` - Get dashboard data
- `GET /api/dashboard/spending-summary` - Spending summary
- `GET /api/dashboard/category-breakdown` - Category breakdown

## 🎨 UI Components

Built with modern, accessible components:

- **Cards** - Information display with shadows and borders
- **Buttons** - Multiple variants (primary, secondary, outline, ghost)
- **Forms** - Input fields, selects, labels with validation
- **Dialogs** - Modal windows for forms and confirmations
- **Charts** - Interactive data visualizations
- **Animations** - Smooth transitions and micro-interactions

## 🌟 Key Features Demo

### 💳 Expense Tracking
- Add expenses with AI-powered categorization
- Real-time search and filtering
- Edit/delete functionality with smooth animations

### 📈 Dashboard Analytics
- Visual spending breakdown with pie charts
- Trend analysis with line charts
- Budget progress indicators

### 🎯 Smart Budgeting
- Set category-specific budgets
- Real-time alerts when approaching limits
- Visual progress tracking

### 🤖 AI Assistant
- Natural language expense categorization
- Financial advice and insights
- Spending pattern analysis

## 🚀 Deployment

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up --build
```

### Manual Deployment
1. Build frontend: `npm run build`
2. Build backend: `./mvnw clean package`
3. Deploy JAR file to your server
4. Configure production database

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 🤝 Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) - Backend framework
- [React](https://reactjs.org/) - Frontend library
- [Tailwind CSS](https://tailwindcss.com/) - CSS framework
- [shadcn/ui](https://ui.shadcn.com/) - UI components
- [Framer Motion](https://www.framer.com/motion/) - Animation library
- [Recharts](https://recharts.org/) - Chart library
- [OpenAI](https://openai.com/) - AI integration

---

**Built with ❤️ for hackathons and production-ready applications**