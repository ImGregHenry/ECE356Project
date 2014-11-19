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
VALUES ('1', 'Greg', 'Henry', 'Brain Surgery', 1000000),
       ('2', 'Doctor', 'Who', 'Television', 100000),
       ('3', 'Doctor', 'Kevorkian', 'Mass Murder', 200000);


CREATE TABLE Staff 
(
    StaffID VARCHAR(100) NOT NULL PRIMARY KEY,
	FirstName VARCHAR(100),
	LastName VARCHAR(100),
	JobTitle VARCHAR(100)
);

INSERT INTO Staff (StaffID, FirstName, LastName, JobTitle)
VALUES ('1', 'Bob', 'Loblaw', 'Accountant'),
       ('2', 'Vincent', 'Chau', 'Janitor'),
       ('3', 'JohnJacob', 'JingleHeimerSchmidt', 'Secretary'),
       ('4', 'Lionel', 'Hutz', 'Lawyer'),
	   ('19', 'test', 'icle', 'Testery');



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


INSERT INTO Patient (PatientID, NumberOfVisits, SocialInsuranceNumber, FirstName, LastName, 
                   HealthCardNumber, Address, CurrentStatus, LastVisitDate)
VALUES ('1', '5', '123456789', 'Patient', 'Zero', '19191919191919', '123 Fake Street', 'Alive', '2014-11-04'),
 ('2', '1', '987654321', 'Dumb', 'Dumber', '9191919191919191', '321 Main Street', 'InABetterPlace', '2014-11-03'),
 ('3', '5', '420420420', 'Cheech', 'Chong', '420420420420420420', '420 High Street', 'Amazing', '2014-04-20');


CREATE TABLE AccessLevels (
	AccessName VARCHAR(50) NOT NULL PRIMARY KEY
);

INSERT INTO AccessLevels(AccessName)
VALUES ('DOCTOR'),
('LEGAL'),
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
VALUES ('doctor', 'pass', NULL, NULL, '1','DOCTOR'),
('staff', 'pass', '2', NULL, NULL, 'STAFF'),
('legal', 'pass', '4', NULL, NULL,'LEGAL'),
('admin', 'pass', NULL, NULL, '1', 'ADMIN'),
('patient', 'pass', NULL, '3', NULL, 'PATIENT');


CREATE TABLE Medical (
	ProcedureName VARCHAR(100) NOT NULL,
	ProcedureFee INT NOT NULL,
	Prescription VARCHAR(100),
	PrescriptionLegality VARCHAR(255),
    PRIMARY KEY (ProcedureName)
);

INSERT INTO Medical (ProcedureName, ProcedureFee, Prescription, PrescriptionLegality)
VALUES  ('Consultation', '100', 'Rest', 'legal'),
('Brain Surgery', '10000', 'Morphine', 'legal'),
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
VALUES  ('2', '3'),
('1', '1'),
('2', '1'),
('3', '1'),
('3', '2'),
('1', '2'),
('1', '3'),
('1', '4'),
('2', '2');

-- Assign Patients to Doctor
INSERT INTO StaffDoctorAccess(AssignedToDoctorID, PatientID)
VALUES  ('1', '1'),
('2', '2'),
('3', '3'),
('1', '3'),
('1', '2');

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
VALUES  ('1', '2', '1', '2014-11-27 16:20:00', '04:20:00'),
('1', '3', '2', '2014-11-25 12:30:00', '00:30:00'),
('2', '3', '1', '2014-11-25 14:00:00', '00:45:00'),
('1', '3', '2', '2014-11-22 11:30:00', '01:00:00');
-- 'YYYY-MM-DD HH:MM:SS'
-- 'HH:MM:SS'


-- TODO: remove length of visit from this table -- it is in the appointment table now
CREATE TABLE VisitationRecord (
    AppointmentID INT NOT NULL,
    DoctorComment VARCHAR(100),
    VisitReason VARCHAR(100) NOT NULL,
    ProcedureFee INT NOT NULL,  
    ProcedureName VARCHAR(100) NOT NULL,
    
    PRIMARY KEY (AppointmentID),
    FOREIGN KEY (AppointmentID) REFERENCES Appointment (AppointmentID),
    FOREIGN KEY (ProcedureName) REFERENCES Medical (ProcedureName)
);


INSERT INTO VisitationRecord (AppointmentID, DoctorComment, VisitReason, ProcedureFee, ProcedureName)
VALUES (1, 'What a visit that was.  Amazing patient.', 'Strictly business', 100, 'Consultation'),
(2, 'Things got prickly.', 'Acupunture Session', 80, 'Acupuncture');
