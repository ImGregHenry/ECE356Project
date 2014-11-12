
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.Font;



public class MainTabPage {


	private User loginUser;
	private JFrame frmProject;
	private Login.LoginAccessLevel accessLevel = Login.LoginAccessLevel.NONE;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainTabPage window = new MainTabPage();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

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
		frmProject.setBounds(100, 100, 775, 532);
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
		else if(this.accessLevel == Login.LoginAccessLevel.LEGAL)
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
		tabbedPane.addTab("Create Patient Info", new PatientInfoPanel(PatientInfoPanel.PatientLoadMode.CREATE, -1));
		tabbedPane.addTab("Update Patient Info", new PatientInfoPanel(PatientInfoPanel.PatientLoadMode.UPDATE, 1));
		tabbedPane.addTab("Appointments", new AppointmentPanel(loginUser.StaffID));
		tabbedPane.addTab("VisitationRecord", new VisitationRecordPanel(1));

		
		frmProject.getContentPane().add(tabbedPane);
		frmProject.setVisible(true);
	}
	
	
}
