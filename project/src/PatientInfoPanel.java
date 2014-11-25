
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PatientInfoPanel extends JPanel {

	public static enum PatientLoadMode {
		UPDATE
	}

	private PatientLoadMode pageLoadMode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField txt_PatientID;
	private JLabel lbl_SIN;
	private JTextField txt_SIN;
	private JTextField txt_FirstName;
	private JLabel lbl_FirstName;
	private JLabel lbl_LastName;
	private JTextField txt_LastName;
	private JTextField txt_HealthCardNum;
	private JLabel lbl_HealthCardNumber;
	private JLabel lbl_Address;
	private JTextField txt_Address;
	private JTextField txt_LastVisitDate;
	private JLabel lbl_LastVisitDate;
	private JButton btnUpdate;
	private JTextField txt_PhoneNumber;
	private JLabel lbl_PhoneNumber;
	private JLabel lbl_UpdateMessage;

	// /**
	// * Launch the application.
	// */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// Patient window = new Patient();
	// window.frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the application.
	 */
	public PatientInfoPanel(PatientLoadMode mode, User _user) {
		this.pageLoadMode = mode;
		initialize();
		
		System.out.println("Loading Patient Info Panel in Mode: " + mode + " and PatientID: " + _user.PatientID);
		
		loadPatientInformation(_user.PatientID);
		
		// dbQuery.StartDBConnection();
		//
		// if(pageLoadMode == PatientLoadMode.UPDATE)
		// loadPatientInformation(patientID);

	}

	private void loadPatientInformation(String patientID) {
		ResultSet rs = dbQuery.Patient_GetPatientInformation(patientID);

		if (rs != null) {
			System.out.println("Patient Information Loaded Successfully.");

			try {
				while(rs.next()){

				txt_PatientID.setText(rs.getObject("PatientID").toString());
				txt_SIN.setText(rs.getString("SocialInsuranceNumber"));
				txt_FirstName.setText(rs.getString("FirstName"));
				txt_LastName.setText(rs.getString("LastName"));
				txt_Address.setText(rs.getString("Address"));
				txt_PhoneNumber.setText(rs.getString("PhoneNumber"));
				txt_HealthCardNum.setText(rs.getString("HealthCardNumber"));
				// txt_LastVisitDate.setText(rs.getString(""));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("ERROR: Failed to load patient.");
			// TODO: set error message
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1155, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);

		txt_PatientID = new JTextField();
		txt_PatientID.setEditable(false);
		txt_PatientID.setBounds(120, 72, 219, 20);
		this.add(txt_PatientID);
		txt_PatientID.setColumns(10);

		JLabel lbl_PatientID = new JLabel("Patient Num:");
		lbl_PatientID.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_PatientID.setBounds(10, 75, 100, 14);
		this.add(lbl_PatientID);

		lbl_SIN = new JLabel("SIN:");
		lbl_SIN.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_SIN.setBounds(10, 106, 100, 14);
		this.add(lbl_SIN);

		txt_SIN = new JTextField();
		txt_SIN.setEditable(false);
		txt_SIN.setColumns(10);
		txt_SIN.setBounds(120, 103, 219, 20);
		this.add(txt_SIN);

		txt_FirstName = new JTextField();
		txt_FirstName.setEditable(false);
		txt_FirstName.setColumns(10);
		txt_FirstName.setBounds(120, 131, 219, 20);
		this.add(txt_FirstName);

		lbl_FirstName = new JLabel("First Name:");
		lbl_FirstName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_FirstName.setBounds(10, 134, 100, 14);
		this.add(lbl_FirstName);

		lbl_LastName = new JLabel("Last Name");
		lbl_LastName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_LastName.setBounds(10, 165, 100, 14);
		this.add(lbl_LastName);

		txt_LastName = new JTextField();
		txt_LastName.setEditable(false);
		txt_LastName.setColumns(10);
		txt_LastName.setBounds(120, 162, 219, 20);
		this.add(txt_LastName);

		txt_HealthCardNum = new JTextField();
		txt_HealthCardNum.setEditable(false);
		txt_HealthCardNum.setColumns(10);
		txt_HealthCardNum.setBounds(120, 192, 219, 20);
		this.add(txt_HealthCardNum);

		lbl_HealthCardNumber = new JLabel("Health Card Num:");
		lbl_HealthCardNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_HealthCardNumber.setBounds(10, 198, 100, 14);
		this.add(lbl_HealthCardNumber);

		lbl_Address = new JLabel("Address:");
		lbl_Address.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_Address.setBounds(10, 259, 100, 14);
		this.add(lbl_Address);

		txt_Address = new JTextField();
		txt_Address.setColumns(10);
		txt_Address.setBounds(120, 256, 219, 20);
		this.add(txt_Address);

		txt_LastVisitDate = new JTextField();
		txt_LastVisitDate.setEditable(false);
		txt_LastVisitDate.setColumns(10);
		txt_LastVisitDate.setBounds(120, 284, 219, 20);
		this.add(txt_LastVisitDate);

		lbl_LastVisitDate = new JLabel("Last Visit Date");
		lbl_LastVisitDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_LastVisitDate.setBounds(10, 287, 100, 14);
		this.add(lbl_LastVisitDate);
		
		txt_PhoneNumber = new JTextField();
		txt_PhoneNumber.setColumns(10);
		txt_PhoneNumber.setBounds(120, 224, 219, 20);
		this.add(txt_PhoneNumber);

		lbl_PhoneNumber = new JLabel("Phone Number");
		lbl_PhoneNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_PhoneNumber.setBounds(10, 229, 100, 14);
		this.add(lbl_PhoneNumber);

		lbl_UpdateMessage = new JLabel("");
		lbl_UpdateMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_UpdateMessage.setBounds(160, 331, 123, 14);
		this.add(lbl_UpdateMessage);
		lbl_PhoneNumber.setHorizontalAlignment(SwingConstants.RIGHT);

		if (pageLoadMode == PatientLoadMode.UPDATE)
			btnUpdate = new JButton("Update");

		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO: error checking on patient update

				// Update database with new patient information

				if (pageLoadMode == PatientLoadMode.UPDATE) {
					dbQuery.Patient_UpdatePatientInformation(
							txt_PatientID.getText(), txt_SIN.getText(),
							txt_FirstName.getText(), txt_LastName.getText(),
							txt_HealthCardNum.getText(), txt_Address.getText(),
							txt_PhoneNumber.getText());

					lbl_UpdateMessage.setText("Patient Update Successful.");
				} 
				/*else if (pageLoadMode == PatientLoadMode.CREATE) {
					dbQuery.Patient_CreateNewPatientInformation(
							txt_SIN.getText(), txt_FirstName.getText(),
							txt_LastName.getText(),
							txt_HealthCardNum.getText(), txt_Address.getText(),
							txt_PhoneNumber.getText());

					lbl_UpdateMessage.setText("Patient Creation Successful.");
				}*/

			}
		});
		btnUpdate.setBounds(160, 331, 117, 29);
		this.add(btnUpdate);
	}
}
