BEGIN;
INSERT INTO app_user(id,first_name,last_name,email,password_hash) VALUES
 (1,'Nora','Demo','nurse@demo.local','$2a$12$IvybPGyAhavypX7K4TEOmeVLOzWluLdNZ3IJeUnCwmR0JDi/0F13C'),
 (2,'Samir','Demo','doctor@demo.local','$2a$12$IvybPGyAhavypX7K4TEOmeVLOzWluLdNZ3IJeUnCwmR0JDi/0F13C'),
 (3,'Lina','Demo','specialist@demo.local','$2a$12$IvybPGyAhavypX7K4TEOmeVLOzWluLdNZ3IJeUnCwmR0JDi/0F13C'),
 (4,'Yanis','Demo','dermato@demo.local','$2a$12$IvybPGyAhavypX7K4TEOmeVLOzWluLdNZ3IJeUnCwmR0JDi/0F13C'),
 (5,'Adam','Demo','doctor2@demo.local','$2a$12$IvybPGyAhavypX7K4TEOmeVLOzWluLdNZ3IJeUnCwmR0JDi/0F13C');
INSERT INTO nurse(id) VALUES (1);
INSERT INTO general_practitioner(id) VALUES (2),(5);
INSERT INTO specialist(id,specialty,tariff) VALUES (3,'Cardiologie',250.00),(4,'Dermatologie',200.00);
INSERT INTO patient(id,first_name,last_name,birth_date,social_security_number,phone,address,insurance,history,allergies,current_treatment,waiting,arrival_at) VALUES
 (1,'Amal','Fictive',DATE '1996-05-12','DEMO-001','0000000000','Adresse fictive','Mutuelle fictive','Aucun antécédent signalé','Aucune allergie signalée','Aucun',TRUE,CURRENT_TIMESTAMP),
 (2,'Karim','Fictif',DATE '1988-02-20','DEMO-002','0000000000','Adresse fictive','Mutuelle fictive','Dossier fictif','Aucune','Aucun',FALSE,CURRENT_TIMESTAMP);
INSERT INTO vital_signs(id,patient_id,nurse_id,blood_pressure,heart_rate,temperature,respiratory_rate,weight,height,recorded_at) VALUES
 (1,1,1,'120/80',75,36.8,16,65.00,170.00,CURRENT_TIMESTAMP),
 (2,2,1,'125/80',80,37.0,17,72.00,175.00,CURRENT_TIMESTAMP);
INSERT INTO consultation(id,patient_id,doctor_id,reason,observations,diagnosis,treatment,status,base_cost,created_at) VALUES
 (1,2,2,'Consultation de démonstration','Examen fictif','Diagnostic fictif','Prescription fictive','TERMINEE',150.00,CURRENT_TIMESTAMP);
INSERT INTO medical_act(id,consultation_id,name,price) VALUES (1,1,'Acte de démonstration',50.00);
ALTER TABLE app_user ALTER COLUMN id RESTART WITH 100;
ALTER TABLE patient ALTER COLUMN id RESTART WITH 100;
ALTER TABLE vital_signs ALTER COLUMN id RESTART WITH 100;
ALTER TABLE consultation ALTER COLUMN id RESTART WITH 100;
ALTER TABLE medical_act ALTER COLUMN id RESTART WITH 100;
COMMIT;
