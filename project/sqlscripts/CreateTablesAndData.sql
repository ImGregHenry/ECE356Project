-- **MySQL Default DateTime format 'YYYY-MM-DD HH:MM:SS'
-- **MysQL Time format 'HH:MM:SS'

CREATE TABLE Doctor (
	DoctorID VARCHAR(100) NOT NULL PRIMARY KEY,
	FirstName VARCHAR(100),
	LastName VARCHAR(100),
	Specialty VARCHAR(100),
	Revenue INT NOT NULL
);

INSERT INTO Doctor (DoctorID, FirstName, LastName, Specialty, Revenue)
VALUES ('1', 'Greg', 'Henry', 'Neurology', 750000),
       ('2', 'Ajanthi', 'Vasantharoopan', 'Cardiovascular', 700000),
       ('3', 'Steve', 'Jung', 'Gynecological', 650000),
       ('4', 'Sahiti', 'Seemakurti', 'Orthopedic', 675000),
       ('5', 'Meredith', 'Grey', 'General', 600000),
       ('6', 'Gregory', 'House', 'Pediatric', 625000);


CREATE TABLE Staff 
(
    StaffID VARCHAR(100) NOT NULL PRIMARY KEY,
	FirstName VARCHAR(100),
	LastName VARCHAR(100),
	JobTitle VARCHAR(100)
);

INSERT INTO Staff (StaffID, FirstName, LastName, JobTitle)
VALUES ('1', 'Bob', 'Loblaw', 'Accountant'),
       ('2', 'Vincent', 'Chau', 'Nurse'),
       ('3', 'John', 'Heimer', 'Secretary'),
       ('4', 'Lionel', 'Hutz', 'Lawyer'),
	   ('5', 'Jacob', 'Schmidt', 'Nurse'),
	   ('6', 'Mary', 'Smith', 'Nurse'),
	   ('7', 'Aarti', 'Patel', 'Lab Technician');



CREATE TABLE Patient (
	PatientID VARCHAR(100)  NOT NULL PRIMARY KEY,
	NumberOfVisits INT NOT NULL,
	SocialInsuranceNumber VARCHAR(50),
	FirstName VARCHAR(100),
	LastName VARCHAR(100),
    PhoneNumber VARCHAR(15),
	HealthCardNumber VARCHAR(25),
	Address VARCHAR(200),
	CurrentStatus VARCHAR(255),
	LastVisitDate DATETIME
);


INSERT INTO Patient (PatientID, NumberOfVisits, SocialInsuranceNumber, FirstName, LastName, PhoneNumber, HealthCardNumber, Address, CurrentStatus, LastVisitDate)
VALUES ('1', '2', '905455319', 'Spike', 'Jones', '4169920374', '4129375901AM', '180 Meadowlarke Drive', 'Alive', '2014-11-04'),
 ('2', '1', '922038418', 'Michael', 'Lowry', '2893049412', '8273046198EF', '321 Main Street', 'Deceased', '2014-10-03'),
 ('3', '5', '936648102', 'Fatima', 'Reyhani', '9054553196', '3841030394GS', '420 Low Street', 'Critical', '2014-01-12'),
 ('4', '3', '941670492', 'Alysha', 'Ahmed', '4165218649',  '490283456GH', '100 Buckingham Palace', 'Alive', '2014-04-20'),
 ('5', '10', '830475192', 'Kevin', 'Persaud', '2895227977', '7203844057SJ', '1600 Pennsylvania Avenue', 'Recovery', '2014-08-29'),
 ('6', '4', '882939304', 'Lois', 'Wayne', '9054563392', '6139485720LG', '99 Intel Avenue', 'Discharged', '2014-03-31'),
 ('7', '23', '738920174', 'William', 'Danger', '6472349876', '2937401548SS', '1505 Spruce Circle', 'Critical', '2014-07-08'),
 ('8', '1', '876401923', 'Michael', 'Smith', '6475679302', '57483392034TD', '2385 Hickory Way', 'Alive', '2014-09-15'),
 ('9', '3', '823495664', 'Lucy', 'Wong', '4162899051', '2283049581FC', '84 Grimmauld Place', 'Deceased', '2014-04-01'),
 ('10', '12', '732948576', 'Peter', 'McAvon', '9053101011', '1920482995WE', '5 Sonic  Drive', 'Critical', '2014-11-25');


CREATE TABLE AccessLevels (
	AccessName VARCHAR(50) NOT NULL PRIMARY KEY
);

INSERT INTO AccessLevels(AccessName)
VALUES ('DOCTOR'),
('FINANCE'),
('STAFF'),
('PATIENT'),
('ADMIN');


CREATE TABLE LoginInfo (
	LoginName VARCHAR(50),
    Pass VARCHAR(50),
    AccessLevel VARCHAR(50) NOT NULL,
    StaffID VARCHAR(100),
	PatientID VARCHAR(100),
    DoctorID VARCHAR(100),
    PRIMARY KEY (LoginName),
    FOREIGN KEY (AccessLevel) REFERENCES AccessLevels(AccessName),
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID),
    FOREIGN KEY (StaffID) REFERENCES Staff(StaffID),
    FOREIGN KEY (DoctorID) REFERENCES Doctor(DoctorID)
);

INSERT INTO LoginInfo (LoginName, Pass, StaffID, PatientID, DoctorID, AccessLevel)
VALUES ('gthenry', 'pass', NULL, NULL, '1','DOCTOR'),
('avasant', 'pass', NULL, NULL, '2','DOCTOR'),
('s7jung', 'pass', NULL, NULL, '3','DOCTOR'),
('sseemaku', 'pass', NULL, NULL, '4','DOCTOR'),
('mgrey', 'pass', NULL, NULL, '5','DOCTOR'),
('ghouse', 'pass', NULL, NULL, '6','DOCTOR'),
('bloblaw', 'pass', '1', NULL, NULL, 'FINANCE'),
('vchau', 'pass', '2', NULL, NULL,'STAFF'),
('jheimer', 'pass', '3', NULL, NULL,'STAFF'),
('lhutz', 'pass', '4', NULL, NULL,'FINANCE'),
('jschmidt', 'pass', '5', NULL, NULL,'STAFF'),
('m1smith', 'pass', '6', NULL, NULL,'STAFF'),
('apatel', 'pass', '7', NULL, NULL,'STAFF'),
('sjones', 'pass', NULL, '1', NULL, 'PATIENT'),
('mlowry', 'pass', NULL, '2', NULL, 'PATIENT'),
('freyhani', 'pass', NULL, '3', NULL, 'PATIENT'),
('aahmed', 'pass', NULL, '4', NULL, 'PATIENT'),
('kpersaud', 'pass', NULL, '5', NULL, 'PATIENT'),
('lwayne', 'pass', NULL, '6', NULL, 'PATIENT'),
('wdanger', 'pass', NULL, '7', NULL, 'PATIENT'),
('m2smith', 'pass', NULL, '8', NULL, 'PATIENT'),
('lwong', 'pass', NULL, '9', NULL, 'PATIENT'),
('pmcavon', 'pass', NULL, '10', NULL, 'PATIENT'),
('admin', 'pass', '1', '1', '1', 'ADMIN');


CREATE TABLE Medical (
	ProcedureName VARCHAR(100) NOT NULL,
	ProcedureFee INT NOT NULL,
	Prescription VARCHAR(100),
	PrescriptionLegality VARCHAR(255),
    PRIMARY KEY (ProcedureName)
);

INSERT INTO Medical (ProcedureName, ProcedureFee, Prescription, PrescriptionLegality)
VALUES  ('Consultation', '50', 'Rest', 'legal'),
('Blood Test', '100', 'Rest', 'legal'),
('Biopsy Test', '200', 'Rest', 'legal'),
('Colonoscopy', '500', 'Rest', 'legal'),
('X-Ray', '500', 'Rest', 'legal'),
('MRI', '1000', 'Rest', 'legal'),
('Ultrasound', '1000', 'Rest', 'legal'),
('Dialysis', '1500', 'Rest', 'legal'),
('Chemotheraphy', '5000', 'Benadryl', 'legal'),
('Radiation theraphy', '5000', 'Tylenol', 'legal'),
('Amputation', '15000', 'Morphine', 'legal'),
('Craniotomy', '30000', 'Morphine', 'legal'),
('Heart Bypass Surgery', '25000', 'Aspirin', 'legal'),
('Birth', '10000', 'Advil', 'legal'),
('Acupuncture', '80', 'Exercise', 'legal');


CREATE TABLE StaffDoctorAccess (
	DoctorIDSharingPatient VARCHAR(100),
    StaffID VARCHAR(100),
    PatientID VARCHAR(100),
    AssignedToDoctorID VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (PatientID) REFERENCES Patient (PatientID),
    FOREIGN KEY (DoctorIDSharingPatient) REFERENCES Doctor (DoctorID),
    FOREIGN KEY (StaffID) REFERENCES Staff (StaffID),
    FOREIGN KEY (AssignedToDoctorID) REFERENCES Doctor (DoctorID)
);

-- Assign Staff to Doctor
INSERT INTO StaffDoctorAccess(AssignedToDoctorID, StaffID)
VALUES  ('1', '2'),
('2', '2'),
('3', '5'),
('4', '6'),
('5', '6'),
('6', '7');

-- Assign Patients to Doctor
INSERT INTO StaffDoctorAccess(AssignedToDoctorID, PatientID)
VALUES  ('1', '1'),
('1', '3'),
('1', '8'),
('2', '2'),
('2', '4'),
('3', '5'),
('4', '6'),
('5', '7'),
('5', '9'),
('6', '10');


INSERT INTO StaffDoctorAccess(AssignedToDoctorID, PatientID, DoctorIDSharingPatient)
VALUES  ('1', '4', '2'),
('2', '5', '3'),
('3', '3', '1'),
('4', '10', '6'),
('5', '6', '4'),
('6', '7', '5');

CREATE TABLE Appointment (
	AppointmentID int NOT NULL AUTO_INCREMENT,
    DoctorID VARCHAR(100) NOT NULL,
	StaffID VARCHAR(100) NOT NULL,
	PatientID VARCHAR(100) NOT NULL,
	AppointmentDate DATETIME NOT NULL,
    AppointmentLength TIME NOT NULL,
    PRIMARY KEY (AppointmentID),
	FOREIGN KEY (DoctorID) REFERENCES Doctor (DoctorID), 
	FOREIGN KEY (StaffID) REFERENCES Staff (StaffID),
	FOREIGN KEY (PatientID) REFERENCES Patient (PatientID)
);

INSERT INTO Appointment (DoctorID, StaffID, PatientID, AppointmentDate, AppointmentLength)
VALUES  ('1', '2', '1', '2014-11-02 09:00:00', '04:20:00'),
('1', '2', '1', '2014-11-04 12:20:00', '00:30:00'),
('2', '2', '2', '2014-10-03 07:30:00', '04:45:00'),
('1', '2', '3', '2014-01-12 14:00:00', '02:15:00'),
('2', '2', '4', '2014-04-20 03:10:00', '01:00:00'),
('3', '5', '5', '2014-08-29 15:05:00', '00:20:00'),
('4', '6', '6', '2014-03-31 10:30:00', '05:48:00'),
('5', '6', '7', '2014-07-08 13:25:00', '01:20:00'),
('1', '2', '8', '2014-09-15 16:40:00', '02:00:00'),
('5', '6', '9', '2014-04-01 19:50:00', '00:45:00'),
('6', '7', '10', '2014-11-25 11:10:00', '03:06:00');
-- 'YYYY-MM-DD HH:MM:SS'
-- 'HH:MM:SS'


-- TODO: remove length of visit from this table -- it is in the appointment table now
CREATE TABLE VisitationRecord (
    AppointmentID INT NOT NULL,
    DoctorComment VARCHAR(100),
    VisitReason VARCHAR(100) NOT NULL,
    ProcedureFee INT NOT NULL,  
    ProcedureName VARCHAR(100) NOT NULL,
    EnteredDate DATETIME NOT NULL,
    FOREIGN KEY (AppointmentID) REFERENCES Appointment (AppointmentID),
    FOREIGN KEY (ProcedureName) REFERENCES Medical (ProcedureName)
);


INSERT INTO VisitationRecord (AppointmentID, DoctorComment, VisitReason, ProcedureFee, ProcedureName, EnteredDate)
VALUES (1, 'What a visit that was.  Amazing patient.', 'Strictly business', 50, 'Consultation', '2014-11-02 02:00:00' ),
(2, 'Things got prickly.', 'Acupunture Session', 80, 'Acupuncture','2014-11-04 13:00:00'),
(3, 'Lots of brain matter', 'Brain Surgery', 30000, 'Craniotomy','2014-10-03 13:30:27'),
(4, 'Blocked colon', 'Bowel Problems', 500, 'Colonoscopy','2014-01-12 16:30:00'),
(5, 'Hairline fracture in wrist', 'Tripped on dogs', 500, 'X-Ray','2014-04-20 17:00:16'),
(6, 'Anemic', 'Fainting and always tired', 100, 'Blood Test','2014-08-29 16:00:02'),
(7, 'Right foot could not be saved', 'Car Accident', 15000, 'Amputation','2014-03-31 19:02:00'),
(8, 'Still on wait list for new kidney', 'Kidney failure', 1500, 'Dialysis','2014-07-03 17:13:40'),
(9, 'Fetus looks healthy', 'Pregnant second trimester checkup', 1000, 'Ultrasound','2014-09-15 18:22:30'),
(10, '3 arterial blocks', 'Heart Attack', 25000, 'Heart Bypass Surgery','2014-11-25 15:42:55');