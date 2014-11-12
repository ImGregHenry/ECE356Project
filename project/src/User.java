

public class User {

	public String DoctorID;
	public int PatientID;
	public String StaffID;
	
	public Login.LoginAccessLevel accessLevel;
	
	public User(String docID, String staffID, String patientID, Login.LoginAccessLevel access)
	{
		this.DoctorID = docID;
		
		if(patientID != null)
			this.PatientID = Integer.parseInt(patientID);
		
		
		this.StaffID = staffID;
		
		this.accessLevel = access;
	}
	
	public User()
	{
		this.accessLevel = Login.LoginAccessLevel.NONE;
	}
}
