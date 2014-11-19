

public class User {

	public String DoctorID;
	public String PatientID;
	public String StaffID;
	
	public Login.LoginAccessLevel accessLevel;
	
	public User(String docID, String staffID, String patientID, Login.LoginAccessLevel access)
	{
		//test
		this.DoctorID = docID;
		this.PatientID = patientID;
		this.StaffID = staffID;
		
		this.accessLevel = access;
	}
	
	public User()
	{
		this.accessLevel = Login.LoginAccessLevel.NONE;
	}
}
