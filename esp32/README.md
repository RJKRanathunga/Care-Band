# Care Band - ESP32 Firmware

This module contains the **ESP32 firmware** for the Care Band wearable device.

The firmware is responsible for energy efficient home detection, GPS fallback handling, fall detection, and communication with the backend system. When safety conditions are triggered, caregiver notifications are delivered via the mobile application.

---

## ✨ Features

- Stores and recognizes known Wi-Fi BSSIDs during initial setup
- Periodically checks (every 5 minutes) for connection to home Wi-Fi
- Falls back to nearby known BSSIDs if direct connection is unavailable
- Activates GPS only if Wi-Fi verification fails (energy efficient design)
- Integrated fall detection with automatic alert triggering
- Sends device data to the backend through an Oracle HTTP relay → Render server
- Caregiver notification logic for:
  - Possible departure from home
  - Confirmed departure from home (GPS verification)
  - Fall detected (location included in alert)

---

## 🏗️ Communication Architecture

The ESP32 firmware communicates with the backend through an **Oracle HTTP relay layer** before reaching the Render-hosted Flask server.

### Why an HTTP Relay is Required

The SIM800I GSM module used in this project only supports **basic HTTP requests** and does not support modern HTTPS/TLS connections required by cloud platforms such as Render.

Because of this limitation:

1. The ESP32 sends plain HTTP requests via the SIM800I module.
2. These requests are received by an Oracle hosted HTTP relay service.
3. The relay forwards the request securely to the Care Band backend server running on Render (HTTPS).
4. The server processes the data and triggers caregiver notifications if necessary.

This architecture enables compatibility with SIM800I while maintaining secure communication with the cloud backend.

> Note: The Oracle HTTP relay implementation is not included in this repository.

---

## 🛠️ Tech Stack

- **Platform:** Arduino framework (C++ for ESP32)
- **Hardware Modules:**
  - ESP32 Wi-Fi module
  - GPS module
  - GSM (SIM800I) module
- **Relay Layer:** Oracle HTTP (external service, not included here)
- **Backend Integration:** Care Band Server (Flask deployed on Render)

---

## 🚀 Setup (Firmware Module Only)

1. Clone the monorepo.
2. Navigate to the `/esp32` directory.
3. Replace the existing server credentials with your own.
4. Flash the firmware to an ESP32 using the Arduino IDE.
5. Ensure the Oracle HTTP relay and Care Band Server are running and accessible.

---

## 📌 Project Context

Developed as part of a **Semester 2 Engineering Design Project**  
University of Moratuwa - ENTC Department  

This module is no longer actively maintained.

---

## ⚠️ Notes

- Some hardcoded credentials may exist in historical commits and must be replaced before use.
- The Oracle HTTP relay implementation is not included in this repository and must be configured separately.
- Proper hardware wiring and module configuration are required for full functionality.

---

## 📜 License

This module is released under a permissive license.

You are free to:

- Use, modify, and redistribute the code
- Integrate it into personal, educational, or commercial systems

Attribution is appreciated but not required.
