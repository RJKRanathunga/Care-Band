import os
from flask import Flask, request, jsonify

app = Flask(__name__)

# Set your API key as an environment variable on Render.
API_KEY = os.getenv('API_KEY')

# Log in to huggingface and grant authorization to huggingchat
# EMAIL = os.getenv('Email')
# PASSWD = os.getenv('PassWord')
# cookie_path_dir = "./cookies/"  # NOTE: trailing slash (/) is required to avoid errors
# sign = Login(EMAIL, PASSWD)
# cookies = sign.login(cookie_dir_path=cookie_path_dir, save_cookies=True)

# Create your ChatBot
# chatbot = hugchat.ChatBot(cookies=cookies.get_dict())  # or cookie_path="usercookies/<email>.json"


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

        # response = chatbot.chat(message)
        print(type(message))
        return jsonify(str(message))

    except Exception as e:
        return jsonify({'error': str(e)})


# Ensure the Flask app runs only when executed directly.
if __name__ == '__main__':
    app.run()  # No need to specify the port, let Render handle it.




