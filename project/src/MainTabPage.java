
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Font;



public class MainTabPage {

	private User loginUser;
	private JFrame frmProject;
	private Login.LoginAccessLevel accessLevel = Login.LoginAccessLevel.NONE;

	/**
	 * Create the application.
	 */
	public MainTabPage(User user) {
		loginUser = user;
		initialize();
		tabbedPane.setSize(frmProject.getBounds().width-100, frmProject.getBounds().height-100);
		frmProject.setTitle("356 Project.  Login level: + " + user.accessLevel);
	}

	 
	/**
	 * Initialize the contents of the frame.
	 */
	private JTabbedPane tabbedPane;
	private void initialize() {
		frmProject = new JFrame();
		frmProject.setTitle("356 Project");
		frmProject.setBounds(50, 50, 775, 532);
		frmProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProject.getContentPane().setLayout(null);
		frmProject.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Calibri", Font.BOLD, 18));
		tabbedPane.setBounds(50, 50, 100, 100);
		tabbedPane.setSize(frmProject.getBounds().width, frmProject.getBounds().height);
		
		if(this.accessLevel == Login.LoginAccessLevel.DOCTOR)
		{
			
		}
		else if(this.accessLevel == Login.LoginAccessLevel.FINANCE)
		{
			
		}
		else if(this.accessLevel == Login.LoginAccessLevel.PATIENT)
		{
			
		}
		else if(this.accessLevel == Login.LoginAccessLevel.ADMIN)
		{
			
		}
		else if(this.accessLevel == Login.LoginAccessLevel.NONE)
		{
			
		}
		
		tabbedPane.addTab("Doctor To Doctor Patient Sharing", new DoctorToDoctorPatientSharing(loginUser));
		tabbedPane.addTab("Assign Staff to Doctor", new AssignStaffToDoctor(loginUser));
		tabbedPane.addTab("Assign Patient to Doctor", new AssignPatientToDoctor(loginUser));
		tabbedPane.addTab("Appointments", new AppointmentPanel(loginUser)); 
		//tabbedPane.addTab("Create Patient Info", new PatientInfoPanel(PatientInfoPanel.PatientLoadMode.CREATE, loginUser));
		tabbedPane.addTab("Update Patient Info", new PatientInfoPanel(PatientInfoPanel.PatientLoadMode.UPDATE, loginUser));
		tabbedPane.addTab("Update/Create Patient Info", new PatientInfoForStaff(PatientInfoForStaff.PatientLoadMode.STAFF, loginUser));
		//tabbedPane.addTab("View Patient Info", new PatientInfoForStaff(PatientInfoForStaff.PatientLoadMode.DOCTOR, loginUser));
				
		tabbedPane.addTab("VisitationRecord", new VisitationRecordPanel(1));
		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	            //TODO: handle refreshing between tabs.
	        }
	    });
		frmProject.getContentPane().add(tabbedPane);
		frmProject.setVisible(true);
	}
}
