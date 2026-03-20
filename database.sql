CREATE DATABASE docdost1_db;
USE docdost1_db;

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100),
    patient_email VARCHAR(100),
    patient_phone VARCHAR(20),
	location VARCHAR(100) NOT NULL,    
    doctor_name VARCHAR(100),
    appointment_date VARCHAR(20),
    appointment_time VARCHAR(20),
    
    problem TEXT
);
SELECT * FROM appointments;