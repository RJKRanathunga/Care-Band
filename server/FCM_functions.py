import requests
import firebase_admin
from firebase_admin import credentials,messaging
import os

# === CONFIGURATION ===
UPSTASH_REDIS_URL = os.getenv('UPSTASH_REDIS_URL')
UPSTASH_REDIS_TOKEN = os.getenv('UPSTASH_REDIS_TOKEN')

firebase_apps = {}

def initialize_firebase_apps():
    user_keys = {
        "oshada":"/etc/secrets/oshada-private-key.json",
        "nayanajith":"/etc/secrets/nayanajith-private-key.json",
        "pemitha": "/etc/secrets/oshada-private-key.json"
    }

    for user, key_path in user_keys.items():
        cred = credentials.Certificate(key_path)
        app = firebase_admin.initialize_app(cred,name=user)
        firebase_apps[user] = app

initialize_firebase_apps()

def add_tokens(user_id,token):
    if token:
        url = f"{UPSTASH_REDIS_URL}/sadd/fcm_tokens_{user_id}/{token}"
        headers = {"Authorization": UPSTASH_REDIS_TOKEN}
        requests.get(url,headers=headers)
        print(f"Added a token to user: {user_id}")

def get_all_tokens(user_id):
    url = f"{UPSTASH_REDIS_URL}/smembers/fcm_tokens_{user_id}"
    headers = {"Authorization": UPSTASH_REDIS_TOKEN}
    response = requests.get(url, headers=headers)
    tokens = response.json().get("result", [])
    if not tokens:
        print(f"Not found any tokens for user {user_id}")
    return tokens

def send_fcm_message(user_id,token, title, body):
    app = firebase_apps.get(user_id)
    if not app:
        raise ValueError(f"No firebase app found for user: {user_id}")

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

def notify_apps(user_id,title, body):
    tokens = get_all_tokens(user_id)
    print(f"Sending notification to {len(tokens)} devices...")
    for token in tokens:
        send_fcm_message(user_id,token, title, body)