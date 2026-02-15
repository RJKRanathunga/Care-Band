# 🧠 Care Band - Wearable Safety System for Dementia Patients

Care Band is an affordable IoT based wearable safety system designed to improve the well being and protection of dementia patients.

The system combines a wearable device, a cloud backend, and a caregiver mobile application to provide real time monitoring, fall detection, and location based safety alerts.

Developed as a Semester 2 Engineering Design Project at the University of Moratuwa.

---

## 📌 Project Overview

Dementia patients, particularly in advanced stages, are highly vulnerable to wandering and accidental falls. These incidents can lead to serious injury, prolonged exposure outdoors, or life-threatening situations.

Care Band was created as a cost-effective and practical solution tailored for families in Sri Lanka and similar environments where professional elderly care services may be limited or inaccessible.

The goal of the project was to design a wearable device that:

- Is comfortable and lightweight
- Minimizes power consumption
- Operates reliably using GSM networks
- Provides caregivers with immediate alerts and live monitoring

---

## 🎯 Core Capabilities

- 📍 Real-time location tracking
- ⚠️ Automatic fall detection
- 🏠 Home presence verification using Wi-Fi
- 📡 GSM based remote communication
- 📲 Caregiver mobile application for monitoring and alerts
- 🔋 Rechargeable and wearable watch style design

The system notifies caregivers when:

- The patient leaves the safe zone
- A fall is detected
- Location updates confirm unsafe movement

---

## 🧩 System Architecture (High-Level)

The Care Band system consists of three integrated components:

### 1️⃣ Wearable Device
A compact ESP32-based device equipped with:
- GPS module for location tracking
- GSM module for communication
- Motion sensor for fall detection
- Rechargeable battery system
- Custom enclosure and PCB design

The device prioritizes energy efficiency by:
- Using Wi-Fi presence detection before activating GPS
- Falling back to GPS only when necessary

---

### 2️⃣ Cloud Backend
A cloud-hosted server that:
- Receives data from the wearable
- Processes location and fall events
- Triggers caregiver notifications
- Maintains real time synchronization

---

### 3️⃣ Caregiver Mobile Application
An Android application that provides:

- Live map tracking
- Fall alerts
- Safe-zone monitoring
- Simple and accessible interface for caregivers

---

## 🌍 Impact and Motivation

This project was designed with accessibility and affordability in mind:

- Growing elderly population
- Limited access to professional dementia care
- Need for low cost assistive technology
- Use of locally available hardware components

The prototype device cost approximately **LKR 8,000**, with a target retail goal below **LKR 10,000** after optimization.

Care Band demonstrates how IoT based assistive technology can bridge healthcare accessibility gaps using practical engineering solutions.

---

## 👨‍🔬 Team

Developed by Team InnovaTech  
University of Moratuwa - ENTC Department  

- Wedamestrige A.N. - PCB & Circuit Design  
- Ranathunga R.J.K.O.H. - Mobile App, Backend Server & ESP32 Firmware  
- Garusinghe S.B. - Enclosure Design  
- Prabharsha H.W.D. - Enclosure Design  

---

## 📁 Repository Structure

- `/app` → Android caregiver application  
- `/server` → Backend server  
- `/esp32` → Wearable device firmware  

Each module contains its own detailed README for technical setup and implementation details.

---

## 📌 Project Status

This project was completed as an academic Engineering Design Project and is no longer actively maintained.

It remains available for educational reference and further development.

---

## 📜 License

This repository is released under a permissive license.

You are free to use, modify, and adapt the system for personal, educational, or commercial purposes.

Attribution is appreciated but not required.

