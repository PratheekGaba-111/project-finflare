import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { 
  AuthResponse, 
  User, 
  Expense, 
  Budget, 
  Investment, 
  Achievement, 
  MonthlyReport,
  FinancialForecast 
} from '../types';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: '/api',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add request interceptor to include auth token
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('finflare_token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Add response interceptor to handle auth errors
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('finflare_token');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // Auth endpoints
  async login(username: string, password: string): Promise<AuthResponse> {
    const response = await this.api.post('/auth/signin', { username, password });
    return response.data;
  }

  async register(userData: {
    username: string;
    email: string;
    password: string;
    firstName?: string;
    lastName?: string;
    phoneNumber?: string;
  }): Promise<{ message: string }> {
    const response = await this.api.post('/auth/signup', userData);
    return response.data;
  }

  async validateToken(): Promise<{ valid: boolean; user?: AuthResponse }> {
    const response = await this.api.get('/auth/validate');
    return response.data;
  }

  // Expense endpoints
  async getExpenses(): Promise<Expense[]> {
    const response = await this.api.get('/expenses');
    return response.data;
  }

  async createExpense(expense: Omit<Expense, 'id' | 'createdAt'>): Promise<Expense> {
    const response = await this.api.post('/expenses', expense);
    return response.data;
  }

  async updateExpense(id: number, expense: Partial<Expense>): Promise<Expense> {
    const response = await this.api.put(`/expenses/${id}`, expense);
    return response.data;
  }

  async deleteExpense(id: number): Promise<void> {
    await this.api.delete(`/expenses/${id}`);
  }

  async getExpensesByCategory(category: string): Promise<Expense[]> {
    const response = await this.api.get(`/expenses/category/${category}`);
    return response.data;
  }

  async getMonthlyReport(year: number, month: number): Promise<MonthlyReport> {
    const response = await this.api.get(`/expenses/reports/monthly/${year}/${month}`);
    return response.data;
  }

  async processOCRReceipt(file: File): Promise<{ description: string; amount: number; category: string }> {
    const formData = new FormData();
    formData.append('receipt', file);
    const response = await this.api.post('/expenses/ocr', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  }

  // Budget endpoints
  async getBudgets(): Promise<Budget[]> {
    const response = await this.api.get('/budgets');
    return response.data;
  }

  async createBudget(budget: Omit<Budget, 'id' | 'spentAmount' | 'remainingAmount' | 'spentPercentage' | 'isOverBudget'>): Promise<Budget> {
    const response = await this.api.post('/budgets', budget);
    return response.data;
  }

  async updateBudget(id: number, budget: Partial<Budget>): Promise<Budget> {
    const response = await this.api.put(`/budgets/${id}`, budget);
    return response.data;
  }

  async deleteBudget(id: number): Promise<void> {
    await this.api.delete(`/budgets/${id}`);
  }

  async getActiveBudgets(): Promise<Budget[]> {
    const response = await this.api.get('/budgets/active');
    return response.data;
  }

  // Investment endpoints
  async getInvestments(): Promise<Investment[]> {
    const response = await this.api.get('/investments');
    return response.data;
  }

  async createInvestment(investment: Omit<Investment, 'id' | 'currentPrice' | 'totalInvestment' | 'currentValue' | 'profitLoss' | 'profitLossPercentage'>): Promise<Investment> {
    const response = await this.api.post('/investments', investment);
    return response.data;
  }

  async updateInvestment(id: number, investment: Partial<Investment>): Promise<Investment> {
    const response = await this.api.put(`/investments/${id}`, investment);
    return response.data;
  }

  async deleteInvestment(id: number): Promise<void> {
    await this.api.delete(`/investments/${id}`);
  }

  async getPortfolioSummary(): Promise<{
    totalValue: number;
    totalInvestment: number;
    totalProfitLoss: number;
    totalProfitLossPercentage: number;
    diversificationScore: number;
  }> {
    const response = await this.api.get('/investments/portfolio/summary');
    return response.data;
  }

  // Achievement endpoints
  async getAchievements(): Promise<Achievement[]> {
    const response = await this.api.get('/achievements');
    return response.data;
  }

  async getLeaderboard(): Promise<User[]> {
    const response = await this.api.get('/achievements/leaderboard');
    return response.data;
  }

  // Financial forecasting endpoints
  async getFinancialForecast(months: number = 3): Promise<FinancialForecast[]> {
    const response = await this.api.get(`/forecasting/predict/${months}`);
    return response.data;
  }

  // Voice assistant endpoints
  async processVoiceCommand(command: string): Promise<{ response: string; action?: any }> {
    const response = await this.api.post('/voice/process', { command });
    return response.data;
  }

  // Dashboard data
  async getDashboardData(): Promise<{
    totalExpenses: number;
    monthlyExpenses: number;
    activeBudgets: Budget[];
    recentExpenses: Expense[];
    portfolioValue: number;
    achievements: Achievement[];
    currentStreak: number;
  }> {
    const response = await this.api.get('/dashboard');
    return response.data;
  }
}

export const apiService = new ApiService();
export default apiService;