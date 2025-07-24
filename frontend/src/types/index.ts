export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  totalPoints: number;
  currentStreak: number;
  maxStreak: number;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
}

export interface Expense {
  id?: number;
  amount: number;
  description: string;
  category: ExpenseCategory;
  expenseDate: string;
  notes?: string;
  receiptImageUrl?: string;
  source: 'MANUAL' | 'OCR' | 'VOICE';
  isRecurring: boolean;
  recurrenceType?: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'YEARLY';
  classificationConfidence?: number;
  aiCategorized: boolean;
  createdAt?: string;
}

export type ExpenseCategory = 
  | 'FOOD_DINING'
  | 'TRANSPORTATION'
  | 'SHOPPING'
  | 'ENTERTAINMENT'
  | 'BILLS_UTILITIES'
  | 'HEALTHCARE'
  | 'EDUCATION'
  | 'TRAVEL'
  | 'GROCERIES'
  | 'INSURANCE'
  | 'INVESTMENTS'
  | 'GIFTS_DONATIONS'
  | 'PERSONAL_CARE'
  | 'HOME_GARDEN'
  | 'BUSINESS'
  | 'OTHER';

export interface Budget {
  id?: number;
  category: ExpenseCategory;
  budgetAmount: number;
  spentAmount: number;
  startDate: string;
  endDate: string;
  period: 'WEEKLY' | 'MONTHLY' | 'QUARTERLY' | 'YEARLY';
  alertEnabled: boolean;
  alertThreshold: number;
  isActive: boolean;
  remainingAmount: number;
  spentPercentage: number;
  isOverBudget: boolean;
}

export interface Investment {
  id?: number;
  symbol: string;
  name: string;
  type: 'STOCK' | 'CRYPTO' | 'BOND' | 'ETF' | 'MUTUAL_FUND';
  quantity: number;
  purchasePrice: number;
  currentPrice?: number;
  purchaseDate: string;
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH' | 'VERY_HIGH';
  sector?: string;
  description?: string;
  totalInvestment: number;
  currentValue: number;
  profitLoss: number;
  profitLossPercentage: number;
}

export interface Achievement {
  id: number;
  type: string;
  title: string;
  description: string;
  pointsAwarded: number;
  iconUrl?: string;
  isUnlocked: boolean;
  unlockedAt?: string;
}

export interface MonthlyReport {
  month: string;
  totalExpenses: number;
  categoryWiseExpenses: Record<ExpenseCategory, number>;
  expensesList: Expense[];
  previousMonthTotal: number;
  monthlyChange: number;
}

export interface FinancialForecast {
  period: string;
  predictedSavings: number;
  overspendingRisk: number;
  recommendations: string[];
  confidenceScore: number;
}

export interface VoiceCommand {
  text: string;
  intent: 'ADD_EXPENSE' | 'VIEW_BUDGET' | 'CHECK_BALANCE' | 'GET_REPORT';
  entities: Record<string, any>;
}

export const EXPENSE_CATEGORIES: Record<ExpenseCategory, string> = {
  FOOD_DINING: 'Food & Dining',
  TRANSPORTATION: 'Transportation',
  SHOPPING: 'Shopping',
  ENTERTAINMENT: 'Entertainment',
  BILLS_UTILITIES: 'Bills & Utilities',
  HEALTHCARE: 'Healthcare',
  EDUCATION: 'Education',
  TRAVEL: 'Travel',
  GROCERIES: 'Groceries',
  INSURANCE: 'Insurance',
  INVESTMENTS: 'Investments',
  GIFTS_DONATIONS: 'Gifts & Donations',
  PERSONAL_CARE: 'Personal Care',
  HOME_GARDEN: 'Home & Garden',
  BUSINESS: 'Business',
  OTHER: 'Other'
};

export const CATEGORY_COLORS: Record<ExpenseCategory, string> = {
  FOOD_DINING: '#FF6B6B',
  TRANSPORTATION: '#4ECDC4',
  SHOPPING: '#45B7D1',
  ENTERTAINMENT: '#96CEB4',
  BILLS_UTILITIES: '#FECA57',
  HEALTHCARE: '#FF9FF3',
  EDUCATION: '#54A0FF',
  TRAVEL: '#5F27CD',
  GROCERIES: '#00D2D3',
  INSURANCE: '#FF9F43',
  INVESTMENTS: '#10AC84',
  GIFTS_DONATIONS: '#EE5A24',
  PERSONAL_CARE: '#F79F1F',
  HOME_GARDEN: '#A3CB38',
  BUSINESS: '#1289A7',
  OTHER: '#C8D6E5'
};