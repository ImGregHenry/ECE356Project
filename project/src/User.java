

public class User {

	public String DoctorID;
	public String DoctorFirstName;
	public String DoctorLastName;
	public String PatientID;
	public String PatientFirstName;
	public String PatientLastName;
	public String StaffID;
	public String StaffFirstName;
	public String StaffLastName;
	
	public Login.LoginAccessLevel accessLevel;
	
	public User(String docID, String docFirstName, String docLastName, 
			String staffID, String staffFirstName, String staffLastName,
			String patID, String patFirstName, String patLastName,
			
			Login.LoginAccessLevel access)
	{
		//test
		this.DoctorID = docID;
		this.DoctorFirstName = docFirstName;
		this.DoctorLastName = docLastName;
		this.PatientID = patID;
		this.PatientFirstName = patFirstName;
		this.PatientLastName = patLastName;
		this.StaffID = staffID;
		this.StaffFirstName = staffFirstName;
		this.StaffLastName = staffLastName;
		
		this.accessLevel = access;
	}
	
	public User()
	{
		this.accessLevel = Login.LoginAccessLevel.NONE;
	}
}
