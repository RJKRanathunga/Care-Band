# Care Band – Server

The **Care Band Server** is the backend component that connects the ESP32 device and caregiver mobile apps.  
It receives patient status/location data and notifies caregivers in real time.  
The server is deployed on **Render** using Flask.

---

## ✨ Features
- Handles incoming data from ESP32 devices.
- Relays location and status updates (home detection, fall alerts) to the mobile app.
- Deployed on **Render** for reliable access.
- Oracle HTTP relay required as buffer (not included here).

---

## 🛠️ Tech Stack
- **Language:** Python  
- **Framework:** Flask  
- **Deployment:** Render  
- **Relay:** Oracle HTTP (setup required separately)

---

## 🚀 Setup
1. Clone this repository.
2. Install dependencies:
   ```bash
   pip install -r requirements.txt
3. You may need to add the Firebase Admin file to make it work.
