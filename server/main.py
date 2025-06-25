import os
from flask import Flask, request, jsonify
from FCM_functions import notify_apps, add_tokens

app = Flask(__name__)

# Set your API key as an environment variable on Render.
API_KEY_OSHADA = os.getenv('API_KEY_OSHADA')
API_KEY_NAYANAJITH =  os.getenv('API_KEY_NAYANAJITH')
API_KEY_HIRUNA = os.getenv('API_KEY_HIRUNA')

# Function to authenticate API key.
def authenticate_api_key(func):
    def wrapper(*args, **kwargs):
        api_key = request.headers.get('my-api-key')
        if api_key and (api_key == API_KEY_OSHADA or api_key==API_KEY_HIRUNA or api_key == API_KEY_NAYANAJITH):
            return func(*args, **kwargs)
        else:
            return jsonify({'error': 'Forbidden: Invalid API key'}), 403

    return wrapper


@app.route('/',methods = ["POST","GET"])
@authenticate_api_key  # This secures the '/' route with the API key check.
def chat():
    try:
        data = request.get_json()
        api_key = request.headers.get('my-api-key')
        command = data["command"]
        message = data["message"]

        if not message or not command:
            return jsonify({'error': 'Message and command is required'}), 400

        print("message: ",message)

        if api_key == API_KEY_OSHADA:
            if command == "save_token":
                add_tokens("oshada",data["token"])
            elif command == "notify":
                notify_apps("oshada","Title",message)

        if api_key == API_KEY_NAYANAJITH:
            if command == "save_token":
                add_tokens("nayanajith",data["token"])
            elif command == "notify":
                notify_apps("nayanajith","Bag",message)

        if api_key == API_KEY_HIRUNA:
            if command == "save_token":
                add_tokens("hiruna",data["token"])
            elif command == "notify":
                notify_apps("hiruna","Elephant",message)

        return jsonify(str(message))

    except Exception as e:
        return jsonify({'error': str(e)})


# Ensure the Flask app runs only when executed directly.
if __name__ == '__main__':
    app.run()  # No need to specify the port, let Render handle it.




