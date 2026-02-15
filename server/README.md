# Care Band - Backend Server

This module contains the **backend server** for the Care Band system.

The server acts as the central bridge between the ESP32 wearable device and the caregiver mobile application. It receives patient status and location data, processes safety conditions, and triggers real-time caregiver notifications.

The server is deployed on **Render** using Flask.

---

## ✨ Features

- Receives device data from ESP32 units (via Oracle HTTP relay)
- Processes:
  - Home presence verification
  - Confirmed home departure events
  - Fall detection alerts
- Relays processed updates to the caregiver mobile application
- Cloud deployment on Render for continuous availability

---

## 🏗️ Communication Architecture

Due to limitations of the SIM800I GSM module (HTTP-only support), the ESP32 device cannot communicate directly with HTTPS endpoints on Render.

The communication flow is:

1. ESP32 sends HTTP request via SIM800I.
2. Request reaches an Oracle-hosted HTTP relay.
3. Relay forwards request securely to this Flask backend on Render (HTTPS).
4. Server processes the data and triggers caregiver notifications.

> Note: The Oracle HTTP relay implementation is not included in this repository and must be configured separately.

---

## 🛠️ Tech Stack

- **Language:** Python  
- **Framework:** Flask  
- **Deployment Platform:** Render  
- **Notification Integration:** Firebase Admin SDK  
- **Relay Layer:** Oracle HTTP (external, not included)

---

## 🚀 Setup (Server Module Only)

1. Clone the monorepo.
2. Navigate to the `/server` directory.
3. Install dependencies:

   ```bash
   pip install -r requirements.txt
