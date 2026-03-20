# 🏥 DocDost - Hospital Appointment Booking System

![HTML5](https://img.shields.io/badge/Frontend-HTML5_&_CSS3-orange?style=for-the-badge)
![JavaScript](https://img.shields.io/badge/Scripting-JavaScript-yellow?style=for-the-badge)
![Java](https://img.shields.io/badge/Backend-Core_Java-red?style=for-the-badge)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?style=for-the-badge)

**DocDost** is a comprehensive Full-Stack Web Application designed to streamline the hospital appointment booking process. It provides a seamless interface for patients to search for doctors, book appointments, and receive instant digital receipts via WhatsApp. It also includes a secure Admin Dashboard for hospital staff to manage bookings in real-time.

---

## ✨ Key Features

- **Responsive UI/UX:** Premium and user-friendly design built with HTML, CSS (Flexbox/Grid), and modern web fonts.
- **Dynamic Doctor Search:** Real-time search and filter functionality to find specialists.
- **Smart Form Validation:** Strict 10-digit phone number validation with international country code selection.
- **Full-Stack Integration:** Connects frontend inputs to a robust Core Java backend server.
- **Secure Database:** Stores all patient data securely using MySQL and JDBC connectors.
- **Admin Dashboard:** A password-protected portal for admins to view all incoming appointments.
- 🚀 **WhatsApp API Integration:** Auto-redirects patients to WhatsApp with a pre-formatted appointment summary right after successful booking!

---

## 💻 Technology Stack

* **Frontend:** HTML5, CSS3, Vanilla JavaScript
* **Backend Server:** Core Java (Custom `com.sun.net.httpserver.HttpServer`)
* **Database:** MySQL 8.0 (JDBC Connector)
* **Architecture:** RESTful API approach (JSON communication)

---

## ⚙️ How to Run This Project Locally

### 1. Database Setup
* Open MySQL Workbench or XAMPP (phpMyAdmin).
* Run the following SQL Query to create the database and table:
```sql
CREATE DATABASE docdost_db;
USE docdost_db;

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    patient_email VARCHAR(100) NOT NULL,
    patient_phone VARCHAR(20) NOT NULL,
    location VARCHAR(100) NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    appointment_date VARCHAR(20) NOT NULL,
    appointment_time VARCHAR(20) NOT NULL,
    problem TEXT
);


2. Backend Setup (Java)
Ensure you have the mysql-connector-j.jar file in your root folder.

Open your terminal/command prompt.

Compile the Java Server:

Bash
javac DocDostServer.java
Run the Server:

Bash
java -cp ".;mysql-connector-j-9.6.0.jar" DocDostServer
(Server will start running on http://localhost:8080)

3. Frontend Setup
Open index.html using a Live Server extension (in VS Code) or simply double-click the file to open it in your browser.

Fill out the appointment form and test the database connection!

🔒 Admin Panel Access
To access the backend database via the UI, go to the Admin Panel (admin.html) and use the default credentials set in your system.

username - harsh
userpassword - harsh123
