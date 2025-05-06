import requests
import firebase_admin
from firebase_admin import credentials,messaging
import os

# === CONFIGURATION ===
UPSTASH_REDIS_URL = os.getenv('UPSTASH_REDIS_URL')
UPSTASH_REDIS_TOKEN = os.getenv('UPSTASH_REDIS_TOKEN')

cred = credentials.Certificate("/etc/secrets/edpsemester2-private-key.json")
firebase_admin.initialize_app(cred)

def get_all_tokens():
    url = f"{UPSTASH_REDIS_URL}/smembers/fcm_tokens"
    headers = {"Authorization": UPSTASH_REDIS_TOKEN}
    response = requests.get(url, headers=headers)
    print(response.json().get("result", []))
    return response.json().get("result", [])

def send_fcm_message(token, title, body):
    # Construct the message
    message = messaging.Message(
        token=token,
        data={"title":title, "body":body}
    )

    # Send the message
    try:
        response = messaging.send(message)
        print(f"Successfully sent message: {response}")
    except Exception as e:
        print(f"Error sending message: {e}")



def remove_invalid_token(token):
    url = f"{UPSTASH_REDIS_URL}/srem/fcm_tokens/{token}"
    headers = {"Authorization": UPSTASH_REDIS_TOKEN}
    requests.get(url, headers=headers)
    print(f"Removed invalid token: {token}")

def notify_all(title, body):
    tokens = get_all_tokens()
    print(f"Sending notification to {len(tokens)} devices...")
    for token in tokens:
        send_fcm_message(token, title, body)