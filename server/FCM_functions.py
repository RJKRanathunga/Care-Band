import requests
import firebase_admin
from firebase_admin import credentials,messaging
import os

# === CONFIGURATION ===
USERS = ["OSHADA", "HIRUNA", "NAYANAJITH"]

UPSTASH_REDIS = {
    user: {
        "url": os.getenv(f"UPSTASH_REDIS_URL_{user}"),
        "storage_token": os.getenv(f"UPSTASH_REDIS_TOKEN_{user}")
    }
    for user in USERS
}

firebase_apps = {}

def initialize_firebase_apps():
    user_keys = {
        "OSHADA":"/etc/secrets/oshada-private-key.json",
        "NAYANAJITH":"/etc/secrets/nayanajith-private-key.json",
        "HIRUNA": "/etc/secrets/hiruna-private-key.json"
    }

    for user, key_path in user_keys.items():
        cred = credentials.Certificate(key_path)
        app = firebase_admin.initialize_app(cred,name=user)
        firebase_apps[user] = app
    print("Firebase apps initialized:",len(firebase_apps),"apps found")

initialize_firebase_apps()

def add_tokens(user_id,token):
    url = UPSTASH_REDIS[user_id]["url"]
    storage_token = UPSTASH_REDIS[user_id]["storage_token"]
    if token:
        url = f"{url}/sadd/fcm_tokens_{user_id}/{token}"
        headers = {"Authorization": storage_token}
        response = requests.post(url,headers=headers)
        print(response.status_code)
        print("Response Body: ",response.text)
        print(f"Added a token to user: {user_id}")

def get_all_tokens(user_id):
    url = UPSTASH_REDIS[user_id]["url"]
    storage_token = UPSTASH_REDIS[user_id]["storage_token"]
    url = f"{url}/smembers/fcm_tokens_{user_id}"
    headers = {"Authorization": storage_token}
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
        data={"title":title, "message":body}
    )

    # Send the message
    try:
        response = messaging.send(message,app=app)
        print(f"Successfully sent message: {response}")
    except Exception as e:
        print(f"Error sending message: {e}")



def remove_invalid_token(user_id, token):
    url = UPSTASH_REDIS[user_id]["url"]
    storage_token = UPSTASH_REDIS[user_id]["storage_token"]
    url = f"{url}/srem/fcm_tokens/{token}"
    headers = {"Authorization": storage_token}
    requests.get(url, headers=headers)
    print(f"Removed invalid token: {token}")

def notify_apps(user_id,title, body):
    tokens = get_all_tokens(user_id)
    print(f"Sending notification to {len(tokens)} devices...")
    for token in tokens:
        send_fcm_message(user_id,token, title, body)

def send_test_messages(user_id, message):
    url = UPSTASH_REDIS[user_id]["url"]
    storage_token = UPSTASH_REDIS[user_id]["storage_token"]
    url = f"{url}/lpush/test_messages/{message}"
    headers = {"Authorization": storage_token}
    response = requests.post(url, headers=headers)
    print(response.status_code)


def save_location(user_id,location):
    url = UPSTASH_REDIS[user_id]["url"]
    storage_token = UPSTASH_REDIS[user_id]["storage_token"]
    url = f"{url}/lpush/location/{location}"
    headers = {"Authorization": storage_token}
    response = requests.post(url, headers=headers)
    print(response.status_code)

import requests
import json
import os

UPSTASH_REDIS_URL = "https://<your-endpoint>.upstash.io"
UPSTASH_REDIS_TOKEN = "your-access-token"
LIST_KEY = "debug_logs"

def save_to_redis_list(data):
    """
    Takes a dictionary or JSON-serializable object and saves it as a string
    into a Redis list for debugging.
    """
    UPSTASH_REDIS_URL_AKHILA = os.getenv("AKHILA_URL")
    UPSTASH_REDIS_TOKEN_AKHILA = os.getenv("AKHILA_TOKEN")
    json_str = json.dumps(data)  # Convert to JSON string

    response = requests.post(
        f"{UPSTASH_REDIS_URL_AKHILA}/lpush/test",
        headers={
            "Authorization": f"Bearer {UPSTASH_REDIS_TOKEN_AKHILA}"
        },
        data=json_str
    )

    if response.status_code == 200:
        print("Saved to Redis list.")
    else:
        print("Failed to save:", response.text)
