import java.awt.EventQueue;

import javax.swing.JFrame;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;


public class Login {


	private boolean IsAutoLogin = true;
	
//	private boolean IsAutoLogin = true;
//	private LoginAccessLevel autoLoginAccessLevel = LoginAccessLevel.DOCTOR;
//	private static String autologinDoctorID = "1";
//	private static String autologinPatientID;
//	private static String autologinStaffID;
	
	public enum LoginAccessLevel {
		NONE,
		PATIENT,
		DOCTOR,
		FINANCE,
		LEGAL,
		STAFF,
		ADMIN
	}
	private LoginAccessLevel autoLoginAccessLevel = LoginAccessLevel.DOCTOR;
	private static String autologinDoctorID = "1";
	private static String autologinPatientID;
	private static String autologinStaffID;
	
	private static User loginUser;
	

	private JFrame frame;
	private JTextField txt_LoginName;
	private JPasswordField txt_Password;
	private JButton btn_Login;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
		dbQuery.StartDBConnection();
		
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 587, 379);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("356 Project");http://downloads.myeclipseide.com/downloads/products/eworkbench/helios/enterprisehttp://downloads.myeclipseide.com/downloads/products/eworkbench/helios/enterprisehttp://downloads.myeclipseide.com/downloads/products/eworkbench/helios/enterprise
		
		txt_LoginName = new JTextField();
		txt_LoginName.setText("admin");
		txt_LoginName.setBounds(181, 120, 201, 20);
		frame.getContentPane().add(txt_LoginName);
		txt_LoginName.setColumns(10);
		
		JLabel lbl_LoginName = new JLabel("Login:");
		lbl_LoginName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_LoginName.setBounds(82, 123, 89, 14);
		frame.getContentPane().add(lbl_LoginName);
		
		JLabel lbl_Password = new JLabel("Password:");
		lbl_Password.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_Password.setBounds(82, 164, 89, 14);
		frame.getContentPane().add(lbl_Password);
		
		final JLabel lbl_ErrorMessage = new JLabel("");
		lbl_ErrorMessage.setBounds(191, 192, 180, 14);
		frame.getContentPane().add(lbl_ErrorMessage);
		
		btn_Login = new JButton("Login");
		btn_Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				GetLoginInformation(txt_LoginName.getText(), txt_Password.getText()); 
				if(loginUser.accessLevel != LoginAccessLevel.NONE)
				{
					lbl_ErrorMessage.setText("SUCCESS: Logging In...");
					
					new MainTabPage(loginUser);
					frame.dispose();
				}
				else
				{
					lbl_ErrorMessage.setText("ERROR: Invalid login information.");
				}
			}
		});
		btn_Login.setBounds(222, 217, 89, 23);
		frame.getContentPane().add(btn_Login);
		
		JLabel lblNewUser = new JLabel("");
		lblNewUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		lblNewUser.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewUser.setBounds(232, 248, 94, 14);
		frame.getContentPane().add(lblNewUser);
		
		txt_Password = new JPasswordField();
		txt_Password.setBounds(181, 161, 201, 20);
		txt_Password.setText("pass");
		frame.getContentPane().add(txt_Password);
		
	}
	
	
	public static void GetLoginInformation(String user, String pass)
	{
		LoginAccessLevel loginAccess = LoginAccessLevel.NONE;
		String loginDoctorID, loginDoctorFirstName, loginDoctorLastName;
		String loginPatientID, loginPatientFirstName, loginPatientLastName;
		String loginStaffID, loginStaffFirstName, loginStaffLastName;
		
		ResultSet rs = dbQuery.Login_GetLoginInformation(user, pass);
		
		try {
			while (rs.next()) {
				
				// Test if login info matches db record
				if(rs.getString("LoginName").equals(user)
						|| rs.getString("Pass").equals(pass))
				{
					// Get access permission level
					
					String access = rs.getString("AccessLevel");
					
					System.out.println("The access Level " +access);
					if(access.equals(LoginAccessLevel.DOCTOR.toString()))
					{
						loginAccess = LoginAccessLevel.DOCTOR;
					}
					else if(access.equals(LoginAccessLevel.LEGAL.toString()))
					{
						loginAccess = LoginAccessLevel.LEGAL;
					}
					else if(access.equals(LoginAccessLevel.FINANCE.toString()))
					{
						loginAccess = LoginAccessLevel.FINANCE;
					}
					else if(access.equals(LoginAccessLevel.STAFF.toString()))
					{
						loginAccess = LoginAccessLevel.STAFF;
					}
					else if(access.equals(LoginAccessLevel.PATIENT.toString()))
					{
						loginAccess = LoginAccessLevel.PATIENT;
					}
					else if(access.equals(LoginAccessLevel.ADMIN.toString()))
					{
						loginAccess = LoginAccessLevel.ADMIN;
					}
					else
					{
						loginAccess = LoginAccessLevel.NONE;
					}
					
					loginStaffID = rs.getString("StaffID");
					loginStaffFirstName = rs.getString("StaffFirstName");
					loginStaffLastName = rs.getString("StaffLastName");
					loginDoctorID = rs.getString("DoctorID");
					loginDoctorFirstName = rs.getString("DoctorFirstName");
					loginDoctorLastName = rs.getString("DoctorLastName");
					loginPatientID = rs.getString("PatientID");
					loginPatientFirstName = rs.getString("PatientFirstName");
					loginPatientLastName = rs.getString("PatientLastName");
					
					loginUser = new User(loginDoctorID, loginDoctorFirstName, loginDoctorLastName, 
							loginStaffID, loginStaffFirstName, loginStaffLastName,
							loginPatientID, loginPatientFirstName, loginPatientLastName, 
							loginAccess);
					
					System.out.println("VALID LOGIN.  AcessLevel: " + loginAccess.toString() + ". " + user + " + " + pass);
					return;
				}
			}
		} catch (SQLException e) {
			try { if (rs != null) rs.close();} catch (SQLException ez) { ez.printStackTrace(); }
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		
		// Failed login
		loginUser = new User();
	}
}
