import os
from flask import Flask, request, jsonify
from FCM_functions import notify_all

app = Flask(__name__)

# Set your API key as an environment variable on Render.
API_KEY = os.getenv('API_KEY')

# Function to authenticate API key.
def authenticate_api_key(func):
    def wrapper(*args, **kwargs):
        api_key = request.headers.get('x-api-key')
        if api_key and (api_key == API_KEY):
            return func(*args, **kwargs)
        else:
            return jsonify({'error': 'Forbidden: Invalid API key'}), 403

    return wrapper


@app.route('/')
@authenticate_api_key  # This secures the '/' route with the API key check.
def chat():
    try:
        message = request.headers.get('message')

        if not message:
            return jsonify({'error': 'Message is required'}), 400

        print("message: ",message)

        if message == "test1":
            notify_all("Hi","This is a test message")

        # response = chatbot.chat(message)
        return jsonify(str(message))

    except Exception as e:
        return jsonify({'error': str(e)})


# Ensure the Flask app runs only when executed directly.
if __name__ == '__main__':
    app.run()  # No need to specify the port, let Render handle it.




