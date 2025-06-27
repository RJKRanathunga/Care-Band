import os
from flask import Flask, request, jsonify
from FCM_functions import notify_apps, add_tokens, send_test_messages

app = Flask(__name__)

# Set your API key as an environment variable on Render.
API_KEY_OSHADA = os.getenv('API_KEY_OSHADA')
API_KEY_NAYANAJITH =  os.getenv('API_KEY_NAYANAJITH')
API_KEY_HIRUNA = os.getenv('API_KEY_HIRUNA')

# Function to authenticate API key.
def authenticate_api_key(func):
    def wrapper(*args, **kwargs):
        api_key = request.get_json()["my-api-key"]
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
        api_key = data['my-api-key']
        command = data["command"]
        message = data["message"]

        if not message or not command:
            return jsonify({'error': 'Message and command is required'}), 400

        print("message: ",message)

        if api_key == API_KEY_OSHADA:
            if command == "save_token":
                add_tokens("OSHADA",data["token"])
            elif command == "notify":
                notify_apps("OSHADA","Title",message)

        if api_key == API_KEY_NAYANAJITH:
            if command == "save_token":
                add_tokens("NAYANAJITH",data["token"])
            elif command == "notify":
                notify_apps("NAYANAJITH","Bag",message)
            elif command == "test":
                send_test_messages("NAYANAJITH",message)

        if api_key == API_KEY_HIRUNA:
            if command == "save_token":
                add_tokens("HIRUNA",data["token"])
            elif command == "notify":
                notify_apps("HIRUNA","Elephant",message)
            elif command == "test":
                send_test_messages("HIRUNA",message)

        return jsonify(str(message))

    except Exception as e:
        return jsonify({'error': str(e)})


# Ensure the Flask app runs only when executed directly.
if __name__ == '__main__':
    app.run()  # No need to specify the port, let Render handle it.




