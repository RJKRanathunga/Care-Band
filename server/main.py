import os
from flask import Flask, request, jsonify
from FCM_functions import notify_apps, add_tokens

app = Flask(__name__)

# Set your API key as an environment variable on Render.
API_KEY_OSHADA = os.getenv('API_KEY_OSHADA')
API_KEY_NAYANAJITH =  os.getenv('API_KEY_NAYANAJITH')
API_KEY_PEMITHA = os.getenv('API_KEY_PAWARA')

# Function to authenticate API key.
def authenticate_api_key(func):
    def wrapper(*args, **kwargs):
        api_key = request.headers.get('x-api-key')
        if api_key and (api_key == API_KEY_OSHADA or api_key==API_KEY_PEMITHA or api_key == API_KEY_NAYANAJITH):
            return func(*args, **kwargs)
        else:
            return jsonify({'error': 'Forbidden: Invalid API key'}), 403

    return wrapper


@app.route('/',methods = ["POST","GET"])
@authenticate_api_key  # This secures the '/' route with the API key check.
def chat():
    try:
        data = request.get_json()
        api_key = request.headers.get('x-api-key')
        message = data["message"]

        if not message:
            return jsonify({'error': 'Message is required'}), 400

        print("message: ",message)

        if api_key == API_KEY_OSHADA:
            if message == "save_token":
                add_tokens("oshada",data["token"])
            else:
                notify_apps("oshada","Hi","This is a test message.")

        if api_key == API_KEY_NAYANAJITH:
            if message == "save_token":
                add_tokens("nayanajith",data["token"])
            else:
                notify_apps("nayanajith","Bag","Bag stolen.")

        if api_key == API_KEY_PEMITHA:
            if message == "save_token":
                add_tokens("pawara",data["token"])
            else:
                notify_apps("pawara","Elephant","Elephant in the village.")

        return jsonify(str(message))

    except Exception as e:
        return jsonify({'error': str(e)})


# Ensure the Flask app runs only when executed directly.
if __name__ == '__main__':
    app.run()  # No need to specify the port, let Render handle it.




