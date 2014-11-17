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

	public static ResultSet Login_GetLoginInformation(String user, String pass) {
		String query = "SELECT * FROM LoginInfo WHERE LoginName = '" + user
				+ "' AND Pass = '" + pass + "'";

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static ResultSet Patient_GetPatientVisitationRecord(int patientID) {
		String query = "SELECT a.PatientID, d.LastName AS DoctorName, a.AppointmentDate, a.AppointmentLength, VisitReason, ProcedureFee, ProcedureName"
				+ " FROM VisitationRecord v"
				+ " INNER JOIN Appointment a ON v.AppointmentID = a.AppointmentID"
				+ " INNER JOIN Doctor d ON d.DoctorID = a.DoctorID"
				+ " WHERE a.PatientID = " + patientID;

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static ResultSet Patient_GetPatientInformation(int patientID) {
		String query = "SELECT * FROM Patient WHERE PatientID = " + patientID;

		ResultSet rs = dbQuery.GetResultSet(query);

		return rs;
	}

	public static void Patient_CreateNewPatientInformation(String sin, 
			String firstName, String lastName, String healthCardNum, 
			String address, String phoneNumber)
	{
		String query = "INSERT INTO Patient (SocialInsuranceNumber, FirstName, LastName, HealthCardNumber, Address, PhoneNumber, CurrentStatus)";
		query += " VALUES ('" + sin + "', " + firstName + "', " + lastName + "', " + healthCardNum + "', " + address + "', " + phoneNumber + ", 'Alive')";
		
		System.out.println("Create Patient Query: " + query);

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
				+ phoneNumber + "'" + " WHERE PatientID = " + patient;

		System.out.println("Update Patient Query: " + query);

		dbQuery.ExecuteDatabaseQuery(query);
	}

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

		System.out.println("TestAppt QUERY: " + query);

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

		System.out.println("ScheduleAppt QUERY: " + query);
		
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

	public static ResultSet Staff_GetAllDoctorInfo() {
		String query = "SELECT * FROM Doctor";

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
}
