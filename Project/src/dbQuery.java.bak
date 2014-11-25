//MySQL imports
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class dbQuery {

	private static Connection conn = null;
	private static Statement stmt = null;
	public static SimpleDateFormat dbDateFormat = new SimpleDateFormat(
			"YYYY-MM-dd HH:mm:SS");


	// **************************
	// GENERIC QUERIES
	// **************************
	public static void StartDBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:mysql://localhost:3306/356project";
			String connectionUser = "root";
			String connectionPassword = "password";

			if (conn == null) {
				conn = DriverManager.getConnection(connectionUrl,
						connectionUser, connectionPassword);
				System.out.println("New database connection created.");
			}
			if (stmt == null) {
				System.out.println("New database statement created.");
				stmt = conn.createStatement();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void CloseDBConnection() {
		System.out.println("Database connections closed.");
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void ExecuteDatabaseQuery(String query) {
		try {
			StartDBConnection();
			stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ResultSet GetResultSet(String query) {
		ResultSet rs = null;
		try {
			StartDBConnection();
			rs = stmt.executeQuery(query);

			return rs;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static int GetResultSetLength(ResultSet rs) {
		int size = 0;
		try {

			if (rs != null) {
				rs.beforeFirst();
				rs.last();
				size = rs.getRow();
				return size;
			}
		} catch (Exception e) {
		} finally {
		}

		return 0;
	}


	// **************************
	// Login.java
	// **************************
	public static ResultSet Login_GetLoginInformation(String user, String pass) {
		String query = "SELECT i.LoginName, i.Pass, i.AccessLevel, i.DoctorID, i.StaffID, i.PatientID, "
				+ "p.FirstName AS PatientFirstName, p.LastName AS PatientLastName, "
				+ "d.FirstName AS DoctorFirstName, d.LastName AS DoctorLastName, "
				+ "s.FirstName AS StaffFirstName, s.LastName AS StaffLastName "
				+ "FROM LoginInfo i "
				+ "LEFT JOIN Patient p ON p.PatientID = i.PatientID "
				+ "LEFT JOIN Doctor d ON d.DoctorID = i.DoctorID "
				+ "LEFT JOIN Staff s ON s.StaffID = i.StaffID "
				+ "WHERE LoginName = '" + user + "' " 
				+ "AND Pass = '" + pass + "'";
		
		System.out.println("Get Login Info QUERY: " + query);
		
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}


	// **************************
	// PatientPanel.java
	// **************************
	public static ResultSet Patient_GetPatientVisitationRecord(int patientID) {
		String query = "SELECT a.PatientID, d.LastName AS DoctorName, a.AppointmentDate, a.AppointmentLength, VisitReason, ProcedureFee, ProcedureName"
				+ " FROM VisitationRecord v"
				+ " INNER JOIN Appointment a ON v.AppointmentID = a.AppointmentID"
				+ " INNER JOIN Doctor d ON d.DoctorID = a.DoctorID"
				+ " WHERE a.PatientID = " + patientID;

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static ResultSet Patient_GetPatientInformation(String patientID) {
		String query = "SELECT * FROM Patient WHERE PatientID = " + patientID;

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static void Patient_CreateNewPatientInformation(String sin, 
			String firstName, String lastName, String healthCardNum, 
			String address, String phoneNumber)
	{
//		String query = "INSERT INTO Patient (PatientID, SocialInsuranceNumber, FirstName, LastName, HealthCardNumber, Address, PhoneNumber, CurrentStatus)";
//		query += " VALUES ('" + sin + "', '" + firstName + "', '" + lastName + "', '" + healthCardNum + "', '" + address + "', '" + phoneNumber + "', 'Alive')";

		String query = "INSERT INTO Patient (PatientID, SocialInsuranceNumber, FirstName, LastName, HealthCardNumber, Address, PhoneNumber, CurrentStatus, NumberOfVisits) " 
			+ "SELECT CAST((MAX(CAST(PatientID AS UNSIGNED))+1) AS CHAR) AS ID, "//'123', '123', '123', '123', '123', '123', 'Alive', '0' " +
			+ "'" + sin + "', '" + firstName + "', '" + lastName + "', '" + healthCardNum + "', '" + address + "', '" + phoneNumber + "', 'Alive', '0' "
			+ "FROM Patient ORDER BY ID DESC";
		
		//System.out.println("Create Patient QUERY: " + query);

		dbQuery.ExecuteDatabaseQuery(query);
	}
	
	public static void Patient_UpdatePatientInformation(String patient,
			String sin, String firstName, String lastName,
			String healthCardNum, String address, String phoneNumber) {
		
		String query = "UPDATE Patient SET";
		query += " SocialInsuranceNumber = '" + sin + "'," + " FirstName = '"
				+ firstName + "'," + " LastName = '" + lastName + "',"
				+ " HealthCardNumber ='" + healthCardNum + "',"
				+ " Address = '" + address + "'," + " PhoneNumber = '"
				+ phoneNumber + "'" + " WHERE PatientID = '" + patient + "'";

		//System.out.println("Update Patient Query: " + query);

		dbQuery.ExecuteDatabaseQuery(query);
	}
	
	public static ResultSet GetPatientList(String DoctorID) {
		String query = "SELECT p.FirstName, p.LastName, p.PatientID "
				+ "FROM Patient p "
				+ "INNER JOIN StaffDoctorAccess a ON a.PatientID = p.PatientID "
				+ "WHERE a.AssignedToDoctorID = '" + DoctorID + "' AND a.DoctorIDSharingPatient IS NULL";
		
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static ResultSet Doctor_GetDoctorList(String DoctorID) {
		String query = "SELECT d.FirstName, d.LastName, d.DoctorID "
				+ "FROM Doctor d "
				+ "INNER JOIN StaffDoctorAccess a ON a.DoctorIDSharingPatient = d.DoctorID "
				+ "WHERE a.AssignedToDoctorID = '" + DoctorID;
		
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	// **************************
	// AppointmentPanel.java
	// **************************
	public static boolean Staff_IsAppointmentAvailabile(String doctorID,
			String patientID, Date start, Date end) {
		String startDateString = dbDateFormat.format(start);
		String endDateString = dbDateFormat.format(end);

		// System.out.println("Test StartDate: " + startDateString);
		// System.out.println("Test EndDate: " + endDateString);

		String query = "SELECT ADDTIME(a.AppointmentDate, a.AppointmentLength) as ApptEndTime, a.AppointmentDate AS ApptStartTime "
				+ "FROM Appointment a " + "WHERE a.DoctorID = "
				+ doctorID
				+ " "
				+ "AND a.PatientID = 1 "
				+ "AND (('"
				+ startDateString
				+ "' <= ADDTIME(a.AppointmentDate, a.AppointmentLength) " // ApptStart
																			// between
																			// a
																			// scheduled
																			// appt
				+ "AND '"
				+ startDateString
				+ "' >= a.AppointmentDate) "
				+ "OR "
				+ "('"
				+ endDateString
				+ "' <= ADDTIME(a.AppointmentDate, a.AppointmentLength) " // ApptEnd
																			// between
																			// a
																			// schedule
																			// appt
				+ "AND '"
				+ endDateString
				+ "' >= a.AppointmentDate) "
				+ "OR "
				+ "('"
				+ endDateString
				+ "' >= ADDTIME(a.AppointmentDate, a.AppointmentLength) " // ApptStart
																			// &
																			// ApptEnd
																			// outside
																			// scheduled
																			// appt
				+ "AND '" + startDateString + "' <= a.AppointmentDate))";

		//System.out.println("TestAppt QUERY: " + query);

		ResultSet rs = dbQuery.GetResultSet(query);

		// If any appointments that fall within that time frame, then test fails
		if (dbQuery.GetResultSetLength(rs) > 0) {
			return false;
		}
		// No conflicting appointments found.
		else {
			return true;
		}
	}

	public static void Staff_ScheduleDoctorAppointment(String length,
			String staffID, String patientID, String doctorID, String startDateString) {
		
		String query = "INSERT INTO Appointment (DoctorID, StaffID, PatientID, AppointmentDate, AppointmentLength) "
				+ "VALUES  ("
				+ doctorID
				+ ", '"
				+ staffID
				+ "', '"
				+ patientID
				+ "', '"
				+ startDateString
				+ "', '"
				+ length
				+ "')";

		//System.out.println("ScheduleAppt QUERY: " + query);
		
		ExecuteDatabaseQuery(query);
	}

	public static ResultSet Staff_GetFutureAppointmentInformation(
			String doctorID) {
		String query = "SELECT CONCAT(d.FirstName, ' ', d.LastName) AS DoctorName, "
				+ "CONCAT(p.FirstName, ' ', p.LastName) AS PatientName,  "
				+ "CONCAT(s.FirstName, ' ', s.LastName) AS StaffName, a.AppointmentDate, a.AppointmentLength, a.DoctorID, a.AppointmentID, "
				+ "IF (v.AppointmentID IS NULL,'true','false') as CanDelete "
				+ "FROM Appointment a "
				+ "INNER JOIN Doctor d ON d.DoctorID = a.DoctorID "
				+ "INNER JOIN Staff s ON s.StaffID = a.StaffID "
				+ "INNER JOIN Patient p ON p.PatientID = a.PatientID "
				+ "LEFT JOIN VisitationRecord v ON v.AppointmentID = a.AppointmentID "
				+ "WHERE a.AppointmentDate > current_date() ";

		// Don't filter if "ALL" is selected
		if (doctorID != "ALL") {
			query += "AND a.DoctorID = '" + doctorID + "' ";
		}

		query += "ORDER BY a.AppointmentDate";

		//System.out.println("QUERY: " + query);

		// System.out.println("Query: " + query);

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}


	public static void Staff_DeleteScheduledAppointment(String apptID) {
		String query = "DELETE FROM Appointment WHERE AppointmentID = " + apptID;

		System.out.println("Query: " + query);
		dbQuery.ExecuteDatabaseQuery(query);

	}

	public static ResultSet Staff_GetAllPatientInfo() {
		String query = "SELECT * FROM Patient";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static ResultSet Staff_GetAllStaffInfo() {
		String query = "SELECT * FROM Staff";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static ResultSet Staff_GetAllDoctorInfo(String staffID) {
		String query = "SELECT d.FirstName, d.LastName, d.DoctorID "
				+ "FROM Doctor d "
				+ "INNER JOIN StaffDoctorAccess a ON a.AssignedToDoctorID = d.DoctorID "
				+ "WHERE a.StaffID = '" + staffID + "' AND a.DoctorIDSharingPatient IS NULL";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	// **************************
	// AssignPatientToDoctor.java
	// **************************
	public static ResultSet Staff_GetPatientToDoctorAssignments()
	{
		String query = "SELECT sda.AssignedToDoctorID, sda.PatientID, CONCAT(p.FirstName, ' ', p.LastName) AS PatientName, "
		+ "CONCAT(d.FirstName, ' ', d.LastName) AS DoctorName "
		+ "FROM staffdoctoraccess sda "
		+ "INNER JOIN Doctor d ON sda.AssignedToDoctorID = d.DoctorID "
		+ "INNER JOIN Patient p ON sda.PatientID = p.PatientID " 
		+ "ORDER BY DoctorName, PatientName";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static ResultSet Staff_GetAllDoctorInfoAssignedToStaffMember(String staffID) {
		String query = "SELECT d.FirstName, d.LastName, d.DoctorID "
				+ "FROM staffdoctoraccess sda "
				+ "INNER JOIN Doctor d ON sda.AssignedToDoctorID = d.DoctorID " 
				+ "WHERE StaffID = 1 "
				+ "AND PatientID IS NULL "
				+ "AND DoctorIDSharingPatient IS NULL";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static void Staff_DeletePatientToDoctorAssignment(String patientID, String doctorID)
	{
		String query = "DELETE FROM StaffDoctorAccess "
			 	+ "WHERE AssignedToDoctorID = '" + doctorID + "' "
			 	+ "AND PatientID = '" + patientID + "' " 
			 	+ "AND DoctorIDSharingPatient IS NULL "
			 	+ "AND StaffID IS NULL";
		
		System.out.println("DeletePatient/Doc Assignment QUERY: " + query);
		dbQuery.ExecuteDatabaseQuery(query);
	}
	
	public static void Staff_AssignPatientToDoctor(String patientID, String doctorID)
	{
		String query = "INSERT INTO StaffDoctorAccess(AssignedToDoctorID, PatientID) "
		 	+ "VALUES ('" + doctorID + "', '" + patientID + "')";

		dbQuery.ExecuteDatabaseQuery(query);
	}
	
	public static boolean Staff_CanAssignPatientToDoctor(String patientID, String doctorID)
	{
		// Check for duplicate entries
		String query = "SELECT PatientID, AssignedToDoctorID "
				+ "FROM StaffDoctorAccess "
				+ "WHERE AssignedToDoctorID = '" + doctorID + "' "
				+ "AND PatientID = '" + patientID + "' " 
				+ "AND DoctorIDSharingPatient IS NULL "
				+ "AND StaffID IS NULL";
		
		ResultSet rs = dbQuery.GetResultSet(query);

		// If any entries exist with these parameters, conflicts found.
		if (dbQuery.GetResultSetLength(rs) > 0) {
			return false;
		}
		// No conflicting assignments found.
		else {
			return true;	
		}
	}
	
	// **************************
	// AssignStaffToDoctor.java
	// **************************
	public static ResultSet Doctor_GetStaffToDoctorAssignments(String doctorID)
	{
		String query = "SELECT sda.AssignedToDoctorID, sda.StaffID, "
		+ "CONCAT(d.FirstName, ' ', d.LastName) AS DoctorName, "
		+ "CONCAT(s.FirstName, ' ', s.LastName) AS StaffName "
		+ "FROM staffdoctoraccess sda "
		+ "INNER JOIN Doctor d ON sda.AssignedToDoctorID = d.DoctorID "
		+ "INNER JOIN Staff s ON sda.StaffID = s.StaffID " 
		+ "WHERE sda.AssignedToDoctorID = '" + doctorID + "' "
		+ "ORDER BY DoctorName, StaffName";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static boolean Doctor_CanAssignStaffToDoctor(String doctorID, String staffID)
	{
		// Check for duplicate entries
		String query = "SELECT StaffID, AssignedToDoctorID "
				+ "FROM StaffDoctorAccess "
				+ "WHERE AssignedToDoctorID = '" + doctorID + "' "
				+ "AND staffID = '" + staffID + "' "
				+ "AND PatientID IS NULL "
				+ "AND DoctorIDSharingPatient IS NULL";
		
		ResultSet rs = dbQuery.GetResultSet(query);

		// If any entries exist with these parameters, conflicts found.
		if (dbQuery.GetResultSetLength(rs) > 0) {
			return false;
		}
		// No conflicting assignments found.
		else {
			return true;	
		}
	}
	
	public static void Doctor_AssignStaffToDoctor(String doctorID, String staffID)
	{
		String query = "INSERT INTO StaffDoctorAccess(AssignedToDoctorID, StaffID) "
			 	+ "VALUES ('" + doctorID + "', '" + staffID + "')";

		dbQuery.ExecuteDatabaseQuery(query);
	}
	
	public static void Doctor_DeleteStaffToDoctorAssignment(String doctorID, String staffID)
	{
		String query = "DELETE FROM StaffDoctorAccess "
			 	+ "WHERE AssignedToDoctorID = '" + doctorID + "' "
			 	+ "AND StaffID = '" + staffID + "' "
			 	+ "AND DoctorIDSharingPatient IS NULL "
			 	+ "AND PatientID IS NULL";
		
		//System.out.println("Delete Staff/Doc Assignment QUERY: " + query);
		dbQuery.ExecuteDatabaseQuery(query);
	
	}
	
	// **************************
	// DoctorSharePatientWithDoctor.java
	// **************************
	public static ResultSet Doctor_GetDoctorToDoctorPatientSharing(String sharingDoctorID)
	{
		String query = "SELECT sda.AssignedToDoctorID, sda.PatientID, sda.DoctorIDSharingPatient, "
		+ "CONCAT(d.FirstName, ' ', d.LastName) AS AssignedDoctorName, "
		+ "CONCAT(d2.FirstName, ' ', d2.LastName) AS SharingDoctorName, "
		+ "CONCAT(p.FirstName, ' ', p.LastName) AS PatientName "
		+ "FROM staffdoctoraccess sda "
		+ "INNER JOIN Doctor d ON sda.AssignedToDoctorID = d.DoctorID "
		+ "INNER JOIN Doctor d2 ON sda.DoctorIDSharingPatient = d2.DoctorID "
		+ "INNER JOIN Patient p ON sda.PatientID = p.PatientID " 
		+ "WHERE sda.DoctorIDSharingPatient = '" + sharingDoctorID + "' "
		+ "ORDER BY SharingDoctorName, sda.PatientID, AssignedDoctorName";

		System.out.println("Get DocToDoc Patient Sharing QUERY: " + query);
		
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static ResultSet Doctor_GetAllDoctorsAssignedPatients(String doctorID) {
		String query = "SELECT sda.AssignedToDoctorID, sda.PatientID, sda.DoctorIDSharingPatient, "
				+ "CONCAT(d.FirstName, ' ', d.LastName) AS DoctorName, "
				+ "CONCAT(p.FirstName, ' ', p.LastName) AS PatientName "
				+ "FROM staffdoctoraccess sda "
				+ "INNER JOIN Doctor d ON sda.AssignedToDoctorID = d.DoctorID "
				+ "INNER JOIN Patient p ON sda.PatientID = p.PatientID " 
				+ "WHERE sda.AssignedToDoctorID = '" + doctorID + "' "
				+ "ORDER BY PatientName";

		ResultSet rs = dbQuery.GetResultSet(query);

		System.out.println("Get Doc's Assigned Patients QUERY: " + query);
		
		return rs;
	}

	public static void Doctor_DeleteDoctorToDoctorPatientSharing(String doctorIDSharingPatient, String patientID, String doctorIDAssignedTo)
	{
		String query = "DELETE FROM StaffDoctorAccess "
			 	+ "WHERE AssignedToDoctorID = '" + doctorIDAssignedTo + "' "
			 	+ "AND DoctorIDSharingPatient = '" + doctorIDSharingPatient + "'"
			 	+ "AND PatientID = '" + patientID + "' "
				+ "AND PatientID IS NULL";
		//System.out.println("Delete DocToDoc Patient Sharing QUERY: " + query);
		
		dbQuery.ExecuteDatabaseQuery(query);
	}
	
	public static boolean Doctor_CanDoctorToDoctorSharePatient(String docIDSharingPatient, String patID, String docIDAssignedTo)
	{
		// Check for duplicate entries
		String query = "SELECT * "
				+ "FROM StaffDoctorAccess "
				+ "WHERE DoctorIDSharingPatient = '" + docIDSharingPatient + "' "
				+ "AND AssignedToDoctorID = '" + docIDAssignedTo + "' "
				+ "AND PatientID = '" + patID + "' "
				+ "AND StaffID IS NULL";
		
		ResultSet rs = dbQuery.GetResultSet(query);

		// If any entries exist with these parameters, conflicts found.
		if (dbQuery.GetResultSetLength(rs) > 0) {
			return false;
		}
		// No conflicting assignments found.
		else {
			return true;	
		}
	}
	
	public static void Doctor_SharePatientDoctorToDoctor(String docIDSharingPatient, String patID, String docIDAssignedTo)
	{
		String query = "INSERT INTO StaffDoctorAccess (DoctorIDSharingPatient, PatientID, AssignedToDoctorID) "
				+ "VALUES ('" + docIDSharingPatient + "', '" + patID + "', '" + docIDAssignedTo + "')";
				
		dbQuery.ExecuteDatabaseQuery(query);
	}
	
	public static ResultSet Staff_GetAllDoctorsExceptDocID(String docID)
	{
		String query = "SELECT * "
				+ "FROM Doctor "
				+ "WHERE DoctorID <> '" + docID + "'";
		

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	// **************************
	// Finance.java
	// **************************
	//	SELECT d.DoctorID, d.FirstName, d.LastName, d.Specialty,  SUM(vr.ProcedureFee) AS ApptIncome, AVG(vr.ProcedureFee) AS ApptAvgIncome, COUNT(*) AS ApptCount
	//	FROM Doctor d
	//	INNER JOIN Appointment a ON a.DoctorID = d.DoctorID
	//	INNER JOIN VisitationRecord vr ON vr.AppointmentID = a.AppointmentID
	//	GROUP BY d.DoctorID



}
