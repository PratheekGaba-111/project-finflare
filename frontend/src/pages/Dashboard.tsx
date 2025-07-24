import React from 'react';
import { useAuth } from '../contexts/AuthContext';

const Dashboard: React.FC = () => {
  const { user, logout } = useAuth();

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">
          Welcome back, {user?.firstName || user?.username}!
        </h2>
        <button
          onClick={logout}
          className="btn-secondary"
        >
          Logout
        </button>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900">Total Expenses</h3>
          <p className="text-2xl font-bold text-primary-600">$0.00</p>
          <p className="text-sm text-gray-500">This month</p>
        </div>
        
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900">Active Budgets</h3>
          <p className="text-2xl font-bold text-success-600">0</p>
          <p className="text-sm text-gray-500">Within limits</p>
        </div>
        
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900">Investments</h3>
          <p className="text-2xl font-bold text-warning-600">$0.00</p>
          <p className="text-sm text-gray-500">Portfolio value</p>
        </div>
        
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900">Streak</h3>
          <p className="text-2xl font-bold text-purple-600">0 days</p>
          <p className="text-sm text-gray-500">Current streak</p>
        </div>
      </div>
      
      <div className="card">
        <h3 className="text-xl font-semibold text-gray-900 mb-4">
          🎉 Welcome to FinFlare!
        </h3>
        <p className="text-gray-600 mb-4">
          Your comprehensive AI-powered personal finance management platform is ready to use.
        </p>
        <div className="space-y-2">
          <p className="text-sm text-gray-500">✓ Add expenses manually or via OCR</p>
          <p className="text-sm text-gray-500">✓ AI-powered expense categorization</p>
          <p className="text-sm text-gray-500">✓ Smart budget management with alerts</p>
          <p className="text-sm text-gray-500">✓ Investment portfolio simulation</p>
          <p className="text-sm text-gray-500">✓ Financial forecasting and reports</p>
          <p className="text-sm text-gray-500">✓ Gamification with achievements</p>
          <p className="text-sm text-gray-500">✓ Voice assistant for quick commands</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;