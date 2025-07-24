from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.metrics import accuracy_score
import joblib
import os
import re
from datetime import datetime, timedelta
import nltk
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer

# Initialize Flask app
app = Flask(__name__)
CORS(app)

# Download NLTK data
try:
    nltk.download('punkt', quiet=True)
    nltk.download('stopwords', quiet=True)
except:
    pass

# Initialize stemmer and stopwords
stemmer = PorterStemmer()
try:
    stop_words = set(stopwords.words('english'))
except:
    stop_words = set()

# Expense categories mapping
CATEGORIES = {
    'FOOD_DINING': ['restaurant', 'food', 'dining', 'meal', 'lunch', 'dinner', 'breakfast', 'cafe', 'pizza', 'burger'],
    'TRANSPORTATION': ['gas', 'fuel', 'uber', 'taxi', 'bus', 'train', 'parking', 'toll', 'car', 'vehicle'],
    'SHOPPING': ['store', 'shop', 'amazon', 'ebay', 'mall', 'retail', 'clothing', 'shoes', 'electronics'],
    'ENTERTAINMENT': ['movie', 'cinema', 'game', 'concert', 'music', 'streaming', 'netflix', 'spotify'],
    'BILLS_UTILITIES': ['electric', 'water', 'gas', 'internet', 'phone', 'utilities', 'bill', 'payment'],
    'HEALTHCARE': ['doctor', 'hospital', 'medicine', 'pharmacy', 'dental', 'medical', 'health', 'clinic'],
    'EDUCATION': ['school', 'university', 'course', 'book', 'tuition', 'education', 'learning', 'class'],
    'TRAVEL': ['hotel', 'flight', 'travel', 'vacation', 'trip', 'booking', 'airbnb', 'airline'],
    'GROCERIES': ['grocery', 'supermarket', 'walmart', 'target', 'market', 'produce', 'vegetables'],
    'INSURANCE': ['insurance', 'premium', 'policy', 'coverage', 'deductible'],
    'INVESTMENTS': ['stock', 'bond', 'investment', 'portfolio', 'dividend', 'mutual', 'fund'],
    'GIFTS_DONATIONS': ['gift', 'donation', 'charity', 'present', 'birthday', 'wedding'],
    'PERSONAL_CARE': ['salon', 'barber', 'spa', 'beauty', 'cosmetics', 'hair', 'nail'],
    'HOME_GARDEN': ['home', 'garden', 'hardware', 'furniture', 'appliance', 'decoration'],
    'BUSINESS': ['office', 'business', 'meeting', 'conference', 'supplies', 'professional'],
    'OTHER': ['misc', 'other', 'various', 'general']
}

class ExpenseCategorizer:
    def __init__(self):
        self.model = None
        self.vectorizer = None
        self.is_trained = False
        self._generate_training_data()
        self._train_model()

    def _preprocess_text(self, text):
        """Preprocess text for better classification"""
        if not text:
            return ""
        
        # Convert to lowercase
        text = text.lower()
        
        # Remove special characters and numbers
        text = re.sub(r'[^a-zA-Z\s]', '', text)
        
        # Tokenize
        try:
            tokens = word_tokenize(text)
        except:
            tokens = text.split()
        
        # Remove stopwords and stem
        processed_tokens = []
        for token in tokens:
            if token not in stop_words and len(token) > 2:
                processed_tokens.append(stemmer.stem(token))
        
        return ' '.join(processed_tokens)

    def _generate_training_data(self):
        """Generate synthetic training data based on keywords"""
        training_data = []
        
        for category, keywords in CATEGORIES.items():
            for keyword in keywords:
                # Generate variations of descriptions
                descriptions = [
                    f"payment to {keyword}",
                    f"{keyword} purchase",
                    f"bought from {keyword}",
                    f"{keyword} expense",
                    f"spending on {keyword}",
                    keyword,
                    f"{keyword} service",
                    f"{keyword} bill"
                ]
                
                for desc in descriptions:
                    training_data.append({
                        'description': desc,
                        'category': category
                    })
        
        self.training_df = pd.DataFrame(training_data)

    def _train_model(self):
        """Train the classification model"""
        if len(self.training_df) == 0:
            return
        
        # Preprocess descriptions
        processed_descriptions = self.training_df['description'].apply(self._preprocess_text)
        
        # Create pipeline
        self.pipeline = Pipeline([
            ('tfidf', TfidfVectorizer(max_features=1000, ngram_range=(1, 2))),
            ('classifier', MultinomialNB(alpha=0.1))
        ])
        
        # Train the model
        self.pipeline.fit(processed_descriptions, self.training_df['category'])
        self.is_trained = True

    def categorize(self, description):
        """Categorize an expense description"""
        if not self.is_trained or not description:
            return {'category': 'OTHER', 'confidence': 0.0}
        
        try:
            processed_desc = self._preprocess_text(description)
            
            # Get prediction and probabilities
            prediction = self.pipeline.predict([processed_desc])[0]
            probabilities = self.pipeline.predict_proba([processed_desc])[0]
            
            # Get confidence score
            max_prob_index = np.argmax(probabilities)
            confidence = probabilities[max_prob_index]
            
            return {
                'category': prediction,
                'confidence': float(confidence)
            }
        except Exception as e:
            print(f"Error in categorization: {e}")
            return {'category': 'OTHER', 'confidence': 0.0}

class FinancialForecaster:
    def __init__(self):
        self.model = LinearRegression()
        
    def predict_spending(self, historical_data, months_ahead=3):
        """Predict future spending based on historical data"""
        try:
            if len(historical_data) < 3:
                return {
                    'predictions': [],
                    'confidence': 0.0,
                    'recommendations': ['More data needed for accurate predictions']
                }
            
            # Convert to DataFrame
            df = pd.DataFrame(historical_data)
            df['date'] = pd.to_datetime(df['date'])
            df = df.sort_values('date')
            
            # Group by month
            monthly_data = df.groupby(df['date'].dt.to_period('M'))['amount'].sum().reset_index()
            monthly_data['month_num'] = range(len(monthly_data))
            
            if len(monthly_data) < 2:
                return {
                    'predictions': [],
                    'confidence': 0.0,
                    'recommendations': ['Need at least 2 months of data']
                }
            
            # Train model
            X = monthly_data[['month_num']]
            y = monthly_data['amount']
            
            self.model.fit(X, y)
            
            # Make predictions
            predictions = []
            last_month = monthly_data['month_num'].max()
            
            for i in range(1, months_ahead + 1):
                future_month = last_month + i
                predicted_amount = self.model.predict([[future_month]])[0]
                predictions.append({
                    'month': i,
                    'predicted_amount': max(0, float(predicted_amount))
                })
            
            # Calculate confidence based on recent trend
            recent_variance = monthly_data['amount'].tail(3).var()
            confidence = max(0.1, 1.0 - (recent_variance / monthly_data['amount'].mean()))
            
            # Generate recommendations
            recommendations = self._generate_recommendations(monthly_data, predictions)
            
            return {
                'predictions': predictions,
                'confidence': float(confidence),
                'recommendations': recommendations
            }
            
        except Exception as e:
            print(f"Error in forecasting: {e}")
            return {
                'predictions': [],
                'confidence': 0.0,
                'recommendations': ['Unable to generate predictions']
            }
    
    def _generate_recommendations(self, historical_data, predictions):
        """Generate spending recommendations"""
        recommendations = []
        
        current_avg = historical_data['amount'].mean()
        future_avg = np.mean([p['predicted_amount'] for p in predictions])
        
        if future_avg > current_avg * 1.1:
            recommendations.append("Spending is predicted to increase. Consider reviewing your budget.")
        elif future_avg < current_avg * 0.9:
            recommendations.append("Great! Your spending is predicted to decrease.")
        else:
            recommendations.append("Your spending pattern appears stable.")
        
        # Check for high variance
        if historical_data['amount'].var() > current_avg * 0.5:
            recommendations.append("Your spending varies significantly. Try to maintain consistent spending habits.")
        
        return recommendations

# Initialize models
categorizer = ExpenseCategorizer()
forecaster = FinancialForecaster()

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({'status': 'healthy', 'model_trained': categorizer.is_trained})

@app.route('/categorize', methods=['POST'])
def categorize_expense():
    """Categorize an expense description"""
    try:
        data = request.json
        description = data.get('description', '')
        
        result = categorizer.categorize(description)
        
        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e), 'category': 'OTHER', 'confidence': 0.0}), 500

@app.route('/forecast', methods=['POST'])
def forecast_spending():
    """Forecast future spending"""
    try:
        data = request.json
        historical_data = data.get('historical_data', [])
        months_ahead = data.get('months_ahead', 3)
        
        result = forecaster.predict_spending(historical_data, months_ahead)
        
        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/retrain', methods=['POST'])
def retrain_model():
    """Retrain the categorization model with new data"""
    try:
        data = request.json
        new_training_data = data.get('training_data', [])
        
        if new_training_data:
            # Add new data to training set
            new_df = pd.DataFrame(new_training_data)
            categorizer.training_df = pd.concat([categorizer.training_df, new_df], ignore_index=True)
            categorizer._train_model()
            
            return jsonify({'message': 'Model retrained successfully'})
        else:
            return jsonify({'error': 'No training data provided'}), 400
            
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=True)