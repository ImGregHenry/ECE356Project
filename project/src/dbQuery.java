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
			String connectionPassword = "Success100";

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
	
	public static ResultSet GetPatientList(PatientInfoForStaff.PatientLoadMode mode, String staffID, String docID, String selfDocID) {
		String query = "";
		
		if(mode == PatientInfoForStaff.PatientLoadMode.DOCTOR)
		{
			
			
			// Doctor, who is not you, shared with someone else
			if(!docID.equals(selfDocID))
			{
				query = "SELECT p.FirstName, p.LastName, p.PatientID " 
				+ "FROM Patient p "
				+ "INNER JOIN StaffDoctorAccess a ON a.PatientID = p.PatientID "
				+ "WHERE a.AssignedToDoctorID = '" + selfDocID  + "' AND a.DoctorIDSharingPatient = '" + docID + "'";

			}
			// Doctor is yourself
			else
			{
				query = "SELECT p.FirstName, p.LastName, p.PatientID " 
						+ "FROM Patient p "
						+ "INNER JOIN StaffDoctorAccess a ON a.PatientID = p.PatientID "
						+ "WHERE a.AssignedToDoctorID = '" + docID + "' AND a.DoctorIDSharingPatient IS NULL";
			}
		}
		// Staff mode
		else
		{
			query = "select distinct sda2.PatientID, p.firstname, p.lastname " 
				+ "from staffdoctoraccess sda "
				+ "inner join staffdoctoraccess sda2 on sda.assignedtodoctorid = sda2.assignedtodoctorid "
				+ "INNER JOIN patient p ON sda2.patientid = p.patientid "
				+ "where sda.staffid = '" + staffID + "' "
				+ "AND sda2.assignedtodoctorid = '" + docID + "' " 
				+ "AND sda2.DoctorIDsharingPatient is null";
		}

		System.out.println("TRALALA QUERY: " + query);
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	
	public static ResultSet Staff_GetAllDoctorInfo()
	{
		String query = "SELECT d.FirstName, d.LastName, d.DoctorID "
				+ "FROM Doctor d ";
		
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static ResultSet Doctor_GetDoctorList(String DoctorID) {
		
//		String query = "SELECT DISTINCT d.FirstName, d.LastName, d.DoctorID "
//				+ "FROM Doctor d "
//				+ "INNER JOIN StaffDoctorAccess a ON a.AssignedToDoctorID = d.DoctorID "
//				+ "WHERE d.DoctorID = '" + DoctorID + "' OR (a.AssignedToDoctorID = '" + DoctorID + "' AND a.DoctorIDSharingPatient IS NOT NULL)";
		
		String query = "SELECT z.FirstName, z.LastName, z.DoctorID "
+ "FROM Doctor z "
+ "WHERE DoctorID = '1' "
+ "UNION "
+ "SELECT d2.FirstName, d2.LastName, d2.DoctorID " 
+ "FROM Doctor d  "
+ "INNER JOIN StaffDoctorAccess a ON a.AssignedToDoctorID = d.DoctorID " 
+ "INNER JOIN Doctor d2 ON a.DoctorIDSharingPatient = d2.DoctorID "
+ "WHERE (a.AssignedToDoctorID = '1')";
		
		System.out.println("************ QUERY: " + query);
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static ResultSet Patient_GetLastVisitDate(String patientID) {
		String query = "SELECT max(EnteredDate) as LastVisitDate "
				+ "FROM VisitationRecord vr "
				+ "INNER JOIN Appointment a on a.AppointmentID = vr.AppointmentID "
				+ "INNER JOIN Patient p on p.PatientID = a.PatientID "
				+ "WHERE p.PatientID = " + patientID;

		ResultSet rs_date = dbQuery.GetResultSet(query);

		return rs_date;
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

		System.out.println("QUERY: " + query);

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
	public static ResultSet Staff_GetPatientToDoctorAssignments(String staffID)
	{
		String query = "select sda2.AssignedToDoctorID,  CONCAT(d.FirstName, ' ', d.LastName) AS DoctorName, "
			+ "CONCAT(p.FirstName, ' ', p.LastName) AS PatientName, p.PatientID "
			+ "from staffdoctoraccess sda1 "
			+ "inner join staffdoctoraccess sda2 on sda1.assignedtodoctorid = sda2.assignedtodoctorid "
			+ "inner join patient p on sda2.patientid = p.patientid "
			+ "inner join doctor d on sda2.AssignedToDoctorID = d.doctorid "
			+ "where sda1.staffid = '" + staffID + "' "
			+ "AND sda2.DoctorIDSharingPatient is null "
			+ "and sda2.patientid is not null";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static ResultSet Staff_GetAllDoctorInfoAssignedToStaffMember(String staffID) {
		String query = "SELECT d.FirstName, d.LastName, d.DoctorID "
				+ "FROM staffdoctoraccess sda "
				+ "INNER JOIN Doctor d ON sda.AssignedToDoctorID = d.DoctorID " 
				+ "WHERE StaffID = '" + staffID + "' "
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
				+ "WHERE sda.AssignedToDoctorID = '" + doctorID + "' AND sda.DoctorIDSharingPatient IS NULL "
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
			 	+ "AND PatientID = '" + patientID + "' ";
		
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
	// VisitationRecordPanel.java
	// **************************
	
	public static ResultSet ViewVisitation_fillDoctorTable(String userId, String type)
	{
		String query ="";
		if (type.equals("STAFF"))
			query= "SELECT DISTINCT(s.AssignedToDoctorID) AS DoctorID, d.FirstName AS 'FirstName', d.LastName AS 'LastName' FROM staffdoctoraccess s "+ 
				"JOIN doctor d ON d.doctorID = s.AssignedToDoctorID  WHERE s.StaffID = '"+userId+"';";
		else
			query= "SELECT DISTINCT(s.AssignedToDoctorID) AS DoctorID, d.FirstName AS 'FirstName', d.LastName AS 'LastName' FROM staffdoctoraccess s "+ 
					"JOIN doctor d ON d.doctorID = s.AssignedToDoctorID  WHERE s.PatientID = '"+userId+"';";
		ResultSet rs = dbQuery.GetResultSet(query);
		
		return rs;
	}
	
	public static ResultSet Visitation_getAppointments(String from, String to, String doctorId, String userId, String type)
	{
		String query ="SELECT a.AppointmentDate, a.AppointmentLength, CONCAT(d.FirstName,' ',d.LastName) AS 'Doctor'," +
				 "CONCAT(p.FirstName,' ',p.LastName) AS 'Patient', a.doctorID FROM appointment a JOIN doctor d ON  a.DoctorID = d.DoctorID "
				 + "JOIN patient p on  a.PatientID = p.PatientID ";
		String doctorQ = " AND a.doctorID = '"+doctorId+"';";
		String filterDoctors = " WHERE a.patientID IN (SELECT DISTINCT(s.PatientId) FROM staffdoctoraccess s WHERE " + 
				"s.AssignedToDoctorID IN (SELECT s2.AssignedToDoctorID FROM staffdoctoraccess s2 WHERE s2.StaffID = '"+userId+"')) ";
		String filterPatients = " WHERE a.PatientID = '"+userId+"'";
				
		if (type.equals("PATIENT"))
		{
			query+=	filterPatients+ " AND AppointmentDate BETWEEN '"+ from +"'  AND '"+ to +"'";				
		}
		else if (type.equals("STAFF"))
		{
			query+= filterDoctors + " AND AppointmentDate BETWEEN '"+ from +"'  AND '"+ to +"'";						
		}
		else if (type.equals("ADMIN"))
		{
				query += "WHERE AppointmentDate BETWEEN '"+ from +"'  AND '"+ to +"'";
		}
		
		if (!doctorId.equals("-1")) query += doctorQ;
		System.out.println(query);

		ResultSet rs = dbQuery.GetResultSet(query);
		
		return rs;
	}
	
	public static ResultSet Visitation_getAllVisits(String doctorId, String patientId, String userId)
	{
		String query = "SELECT a.appointmentDate, a.AppointmentLength, CONCAT(d.FirstName,' ',d.LastName) AS 'Doctor', "
				+ "CONCAT(p.FirstName, ' ', p.LastName) AS 'Patient', IFNULL(maxDate.DoctorComment,'') AS DoctorComment, "
				+ "IFNULL( maxDate.VisitReason,'') AS VisitReason, IFNULL(maxDate.ProcedureFee, '') AS ProcedureFee, "
				+ "IFNULL(maxDate.ProcedureName, '') AS ProcedureName, IFNULL(maxDate.EnteredDate,'') AS EnteredDate, "
				+ "a.appointmentID,a.DoctorID from appointment a LEFT JOIN (SELECT appointmentID, DoctorComment, "
				+ "VisitReason, ProcedureFee, ProcedureName, EnteredDate from Visitationrecord v where EnteredDate = "
				+ "(SELECT MAX(enteredDate) from visitationrecord v2 where v2.AppointmentID = v.AppointmentID) "
				+ "group by v.AppointmentID) AS maxDate ON maxDate.appointmentID = a.appointmentID "
				+ "LEFT JOIN doctor d ON a.DoctorID = d.DoctorID "
				+ "LEFT JOIN patient p ON a.PatientID = p.PatientID ";
		
		String filterDoctors = " WHERE a.patientID IN(Select s.PatientID from staffdoctoraccess s WHERE s.PatientID IS NOT NULL "
				+ "AND s.AssignedToDoctorID = '"+userId+"' AND s.AssignedToDoctorID IS NOT NULL "
				+ " UNION ALL SELECT a.PatientID AS 'Patient'"
					+ " FROM appointment a WHERE a.DoctorID = '"+userId+"') ";
		
		
		if (doctorId.equals("-1") && !patientId.equals("-1"))
		{			
					if (!userId.equals("")) 
					{
						query+=filterDoctors;
						query+=  "AND a.PatientID = '"+patientId; 
					}
					else {
						query+= "WHERE a.PatientID = '"+patientId+"' ";
					}
		}
		else if (patientId.equals("-1") && !doctorId.equals("-1"))
		{
					if (!userId.equals("")){
						query += filterDoctors;
						query += "AND a.DoctorID = '"+doctorId+"' ";
					}
					else query+= "WHERE a.DoctorID = '"+doctorId+"'"; 
		}
		else if (doctorId.equals("-1") && patientId.equals("-1"))
		{
			if (!userId.equals("")) query+=filterDoctors + "";
		}
		else {

			if (!userId.equals("")) 
				query+=filterDoctors + "AND a.DoctorID = '"+doctorId+"' "+ "AND a.PatientID = '"+patientId+"'"+" ";
			else query+= "WHERE a.DoctorID = '"+doctorId+"' "+ "AND a.PatientID = '"+patientId+"' "+ " ";
		}
		query+=";";
		System.out.println(query);

		ResultSet rs = dbQuery.GetResultSet(query);
		
		return rs;
	}
	
	public static void Visitation_UpdateAppointmentRecord(String apptLength,String apptId, String docId)
	{
		String query = "UPDATE appointment SET AppointmentLength = '"+apptLength+"' WHERE AppointmentID = '"+
				apptId+"' AND DoctorID = '"+docId+"';";
		
		System.out.println(query);
		dbQuery.ExecuteDatabaseQuery(query);

		
	}
	public static void Visitation_InsertVisitationRecord(String apptId, String docComm, String vReason, String procFee, String procName)
	{
		String query = "INSERT INTO visitationrecord (AppointmentID, "
				+ "DoctorComment, VisitReason, ProcedureFee, ProcedureName, EnteredDate) "
				+ "VALUES('"+apptId+"','"+docComm+"','"+vReason+"','"+procFee+"','"+procName+"',NOW());";
		
		System.out.println(query);
		dbQuery.ExecuteDatabaseQuery(query);
	}
	public static ResultSet Visitation_getProcedures()
	{
		String query = "SELECT m.ProcedureName, m.ProcedureFee, m.Prescription FROM medical m;";
				
		ResultSet rs = dbQuery.GetResultSet(query);
		
		return rs;
	}
	public static ResultSet Visitation_getVisitations(String appointmentDate, int doctorId)
	{
		String query = "SELECT a.AppointmentDate, CONCAT(d.FirstName,' ',d.LastName) AS 'Doctor', " +
						"CONCAT(p.FirstName, ' ', p.LastName) AS 'Patient', v.VisitReason, " +
						"v.ProcedureName, v.ProcedureFee, m.Prescription, v.EnteredDate, v.DoctorComment " +
						"FROM visitationrecord v JOIN appointment a ON v.AppointmentID = a.AppointmentID " +
						"JOIN doctor d ON a.DoctorID = d.DoctorID JOIN patient p ON a.PatientID = p.PatientID JOIN medical m ON v.ProcedureName = m.ProcedureName  WHERE " +
						"v.AppointmentID = (SELECT a2.AppointmentID FROM appointment a2 WHERE a2.AppointmentDate = '"+ appointmentDate +"' AND a2.DoctorID = "+ doctorId +");";

		System.out.println(query);
		ResultSet rs = dbQuery.GetResultSet(query);
		return rs;
	}
	// **************************
	// Finance.java
	// **************************
	public static ResultSet Finance_GetAllProcedures()
	{
		String query = "SELECT ProcedureName, ProcedureFee "
				+ "FROM Medical ";
		
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}
	
	public static ResultSet Finance_GetDoctorFinanceInfo(String docID, String patID, String procedureName, Date startDate, Date endDate)
	{
		String query = "select d.FirstName AS DoctorFirstName, d.LastName AS DoctorLastName, a.AppointmentID, a.AppointmentDate, a.AppointmentLength, p.FirstName as PatientFirstName, "
					+ "p.LastName as PatientLastName, m.Prescription ,vr.ProcedureName, vr.ProcedureFee,  '100' as VisitationFee "
					+ "from appointment a "
					+ "inner join visitationrecord vr on a.AppointmentID=vr.AppointmentID "
					+ "inner join medical m on vr.ProcedureName=m.ProcedureName "
					+ "inner join patient p on p.PatientID=a.PatientID "
					+ "inner join doctor d on d.DoctorID=a.DoctorID "
					+ "WHERE a.AppointmentDate > '" + dbDateFormat.format(startDate) + "' "
					+ "AND a.AppointmentDate < '" + dbDateFormat.format(endDate) + "' ";
					
		if(docID != "ALL")
		{
			query += "AND a.DoctorID = '"+ docID + "' ";
		}
		if(patID != "ALL")
		{
			query += "AND a.PatientID = '"+ patID + "' ";
		}
		if(procedureName != "ALL")
		{
			query += "AND vr.ProcedureName = '" + procedureName + "' ";
		}
		
					
		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;

	}
	
	/////////////////////
	// DoctorViewPatientListAndRecords.java
	////////////////////
	
	public static ResultSet Doctor_GetFilteredVRList(boolean isArchivedRecordsAllowed, String patID, String diag, String comments, String proc, String prescription)
	{
		
		String query = "SELECT a.AppointmentID, a.DoctorID, d.FirstName AS DoctorFirstName, d.LastName AS DoctorLastName, "
			+ "vr.ProcedureName, vr.DoctorComment, vr.VisitReason AS Diagnosis, m.Prescription, a.AppointmentDate, vr.EnteredDate, m.ProcedureName ";
		
		if(isArchivedRecordsAllowed)
		{
			query += ", IF(vr.EnteredDate IN (select MAX(EnteredDate) FROM Appointment a INNER JOIN VisitationRecord vr ON vr.AppointmentID = a.AppointmentID "
					+ "GROUP BY vr.AppointmentID), 'no', 'yes') AS isArchived ";
		}
		query += "FROM Appointment a "
			+ "INNER JOIN VisitationRecord vr on vr.AppointmentID = a.AppointmentID "
			+ "INNER JOIN Medical m ON m.ProcedureName = vr.ProcedureName "
			+ "INNER JOIN Doctor d ON d.DoctorID = a.DoctorID "
			+ "WHERE a.PatientID = '" + patID + "' "
			+ "AND vr.VisitReason LIKE '%" + diag + "%' "
			+ "AND vr.DoctorComment LIKE '%" + comments + "%' "
			+ "AND m.ProcedureName LIKE '%" + proc + "%' "
			+ "AND m.Prescription LIKE '%" + prescription + "%' ";
		
		if(!isArchivedRecordsAllowed)
		{
			query += "AND vr.EnteredDate IN "
					+ "(select MAX(EnteredDate) "
					+ "FROM Appointment a "
					+ "INNER JOIN VisitationRecord vr ON vr.AppointmentID = a.AppointmentID "
					+ "GROUP BY vr.AppointmentID) ";
				 
		}
		query += "ORDER BY vr.EnteredDate DESC";
		System.out.println("GETVRLIST QUERY :" + query);

			
		ResultSet rs = dbQuery.GetResultSet(query);
		return rs;
	}
	
	
	public static ResultSet Doctor_GetPatientList(String docID, String firstName, String lastName, String patID, String startDate, String endDate)
	{
		//String query = "select p.FirstName, p.LastName, p.PatientID, (select MAX(EnteredDate) FROM VisitationRecord WHERE p.PatientID = '1') AS LastVisitDate, "
			//+ "IF ((SELECT COUNT(*) FROM staffdoctoraccess sda WHERE sda.PatientID = '1' AND sda.AssignedToDoctorID = '1') > 0, 'yes', 'no') AS HasAccess "
		String query = "select p.FirstName, p.LastName, p.PatientID, (select MAX(EnteredDate) FROM Appointment a INNER JOIN VisitationRecord vz ON a.AppointmentID = vz.AppointmentID WHERE p.PatientID = a.PatientID) AS LastVisitDate, "
					+ "IF ((SELECT COUNT(*) FROM staffdoctoraccess sda WHERE sda.PatientID = p.PatientID AND sda.AssignedToDoctorID = '" + docID + "') > 0, 'yes', 'no') AS HasAccess "	
					+ "from patient p "
			+ "where 1=1 ";
			if(patID != null && !patID.equals(""))
				query += "AND p.PatientID = '" + patID + "' ";
		
			query += "AND p.FirstName LIKE '%" + firstName + "%' "
			+ "AND p.LastName LIKE '%" + lastName + "%'";
					
		System.out.println("GETPATIENTLIST QUERY :" + query);
		ResultSet rs = dbQuery.GetResultSet(query);
		
		return rs;
	}
}
