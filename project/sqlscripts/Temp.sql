

select * from StaffDoctorAccess
select * from Doctor


select * from Patient
select * from Appointment

SELECT PatientNumber, d.LastName , VisitationDate, LengthOfVisit, VisitReason, ProcedureFee, ProcedureName 
FROM VisitationRecord v
INNER JOIN Doctor d ON d.DoctorID = v.DoctorID
WHERE PatientNumber = 1

SELECT CONCAT(d.FirstName, ' ', d.LastName) AS DoctorName, 
CONCAT(p.FirstName, ' ', p.LastName) AS PatientName,
CONCAT(s.FirstName, ' ', s.LastName) AS StaffName, a.AppointmentDate, a.AppointmentLength
FROM Appointment a
INNER JOIN Doctor d ON d.DoctorID = a.DoctorID
INNER JOIN Staff s ON s.StaffID = a.StaffID
INNER JOIN Patient p ON p.PatientNumber = a.PatientNumber
WHERE a.AppointmentDate > current_date()

SELECT *
FROM Appointment a
INNER JOIN Doctor d ON d.DoctorID = a.DoctorID
INNER JOIN Staff s ON s.StaffID = a.StaffID
INNER JOIN Patient p ON p.PatientNumber = a.PatientNumber
WHERE a.AppointmentDate > current_date()



SELECT ADDTIME(a.AppointmentDate, a.AppointmentLength) as ApptEndTime, a.AppointmentDate AS ApptStartTime
FROM Appointment a
INNER JOIN Doctor d ON a.DoctorID = d.DoctorID
WHERE CONCAT(d.FirstName, ' ', d.LastName) = 'Greg Henry'
AND a.PatientNumber = 1
AND (('2014-11-26 16:25:00' <= ADDTIME(a.AppointmentDate, a.AppointmentLength)	-- ApptStart between a scheduled appt
	AND '2014-11-26 16:25:00' >= a.AppointmentDate)
or
	('2014-11-28 19:40:00' <= ADDTIME(a.AppointmentDate, a.AppointmentLength)		-- ApptEnd between a schedule appt
	AND '2014-11-28 19:40:00' >= a.AppointmentDate)
or
	('2014-11-28 19:40:00' >= ADDTIME(a.AppointmentDate, a.AppointmentLength)		-- ApptStart & ApptEnd outside scheduled appt
	AND '2014-11-26 16:25:00' <= a.AppointmentDate))

select * from medical
select * from visitationrecord