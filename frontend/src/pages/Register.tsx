import React from 'react';
import { Link } from 'react-router-dom';
import { Zap } from 'lucide-react';

const Register: React.FC = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900 flex items-center justify-center py-12 px-4">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <div className="flex items-center justify-center">
            <Zap className="h-12 w-12 text-white" />
            <h1 className="ml-2 text-4xl font-bold text-white">FinFlare</h1>
          </div>
          <h2 className="mt-6 text-3xl font-extrabold text-white">
            Create your account
          </h2>
        </div>

        <div className="glass rounded-2xl shadow-xl p-8">
          <div className="text-center">
            <p className="text-gray-600 mb-4">
              Registration form will be implemented in the complete version.
            </p>
            <Link
              to="/login"
              className="btn-primary"
            >
              Go to Login
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;