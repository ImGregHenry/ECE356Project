
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import javax.swing.JCheckBox;

public class PatientInfoForStaff extends JPanel {

	public static enum PatientLoadMode {
		STAFF, DOCTOR
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
	private JLabel lbl_Doctor;
	private JComboBox<CustomComboBoxItem> comboBox_Doctor;
	private JTable table_Patients;
	private boolean isDoctorComboBoxLoaded = false;
	private JCheckBox chckbx_CreateNew;

	/**
	 * Create the application.
	 */
	public PatientInfoForStaff(PatientLoadMode mode, User _user) {
		this.pageLoadMode = mode;
		initialize();
		
		System.out.println("Loading Patient Info Panel in Mode: " + mode + " and StaffID: " + _user.StaffID);
		System.out.println("Loading Patient Info Panel in Mode: " + mode + " and DoctorID: " + _user.DoctorID);
		if(this.pageLoadMode == PatientLoadMode.STAFF)
		{
			PopulateDoctorDoctorDropDown(_user.StaffID);
		}
		else if(this.pageLoadMode == PatientLoadMode.DOCTOR)
		{
			PopulateDoctorDoctorDropDown(_user.DoctorID);
		}
		
		isDoctorComboBoxLoaded = true;
		PopulatePatientTable();
		
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
				}
				ResultSet rs_date = dbQuery.Patient_GetLastVisitDate(patientID);

				while(rs_date.next()){
					
				txt_LastVisitDate.setText(rs_date.getString("LastVisitDate"));
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
	
	
	private void PopulateDoctorDoctorDropDown(String id)
	{
		try {
			
			ResultSet rs;
			if(this.pageLoadMode == PatientLoadMode.STAFF)
			{
				rs = dbQuery.Staff_GetAllDoctorInfo(id);
			}
			else if(this.pageLoadMode == PatientLoadMode.DOCTOR)
			{
				rs = dbQuery.Doctor_GetDoctorList(id);
			}
			else
				return;
			
			
			while(rs.next())
			{  
				String doctorName = rs.getObject("FirstName") + " " + rs.getObject("LastName"); 
				comboBox_Doctor.addItem(new CustomComboBoxItem(rs.getObject("DoctorID").toString(), doctorName));
			}
			
			System.out.println("Doctor Filter List Loaded.");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erorr loading doctor filter list.");
			e.printStackTrace();
		}
	}
	
	private void ResetPatientTable()
	{
		if(table_Patients != null)
		{
			while(table_Patients.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_Patients.getModel()).removeRow(0);
			}	
		}
	}
	
	private void PopulatePatientTable()
	{
		ResetPatientTable();
		
		CustomComboBoxItem selectedDoctor = (CustomComboBoxItem)comboBox_Doctor.getSelectedItem();
		

		ResultSet rs = dbQuery.GetPatientList(selectedDoctor.getID());

		Object[] row = new Object[TABLE_COLUMN_COUNT];
		try {
			while(rs.next())
			{  
				
			    row[0] = rs.getObject("FirstName") + " " + rs.getObject("LastName");	// Patient Name
			    row[1] = rs.getObject("PatientID");	// Patient ID
			    
			    System.out.println(table_Patients.getModel().getRowCount());
			    ((DefaultTableModel) table_Patients.getModel()).insertRow(rs.getRow()-1, row);
			}

			//System.out.println("Successfully loaded doctor appointment table!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: loading doctor appointment table!");
			e.printStackTrace();
		}
	}
	private int TABLE_PATIENT_ID_COLUMN_INDEX = 1;
	private String tableColumns[] = new String[] { "Patient Name", "Patient ID" };
	private int TABLE_COLUMN_COUNT = 2;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 1155, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);

		txt_PatientID = new JTextField();
		txt_PatientID.setBounds(510, 72, 219, 20);
		this.add(txt_PatientID);
		txt_PatientID.setColumns(10);

		JLabel lbl_PatientID = new JLabel("Patient Num:");
		lbl_PatientID.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_PatientID.setBounds(400, 75, 100, 14);
		this.add(lbl_PatientID);

		lbl_SIN = new JLabel("SIN:");
		lbl_SIN.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_SIN.setBounds(400, 106, 100, 14);
		this.add(lbl_SIN);

		txt_SIN = new JTextField();
		txt_SIN.setColumns(10);
		txt_SIN.setBounds(510, 103, 219, 20);
		this.add(txt_SIN);

		txt_FirstName = new JTextField();
		txt_FirstName.setColumns(10);
		txt_FirstName.setBounds(510, 131, 219, 20);
		this.add(txt_FirstName);

		lbl_FirstName = new JLabel("First Name:");
		lbl_FirstName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_FirstName.setBounds(400, 134, 100, 14);
		this.add(lbl_FirstName);

		lbl_LastName = new JLabel("Last Name");
		lbl_LastName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_LastName.setBounds(400, 165, 100, 14);
		this.add(lbl_LastName);

		txt_LastName = new JTextField();
		txt_LastName.setColumns(10);
		txt_LastName.setBounds(510, 162, 219, 20);
		this.add(txt_LastName);

		txt_HealthCardNum = new JTextField();
		txt_HealthCardNum.setColumns(10);
		txt_HealthCardNum.setBounds(510, 192, 219, 20);
		this.add(txt_HealthCardNum);

		lbl_HealthCardNumber = new JLabel("Health Card Num:");
		lbl_HealthCardNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_HealthCardNumber.setBounds(400, 198, 100, 14);
		this.add(lbl_HealthCardNumber);

		lbl_Address = new JLabel("Address:");
		lbl_Address.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_Address.setBounds(400, 259, 100, 14);
		this.add(lbl_Address);

		txt_Address = new JTextField();
		txt_Address.setColumns(10);
		txt_Address.setBounds(510, 256, 219, 20);
		this.add(txt_Address);

		txt_LastVisitDate = new JTextField();
		txt_LastVisitDate.setColumns(10);
		txt_LastVisitDate.setBounds(510, 284, 219, 20);
		this.add(txt_LastVisitDate);

		lbl_LastVisitDate = new JLabel("Last Visit Date");
		lbl_LastVisitDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_LastVisitDate.setBounds(400, 287, 100, 14);
		this.add(lbl_LastVisitDate);
		
		txt_PhoneNumber = new JTextField();
		txt_PhoneNumber.setColumns(10);
		txt_PhoneNumber.setBounds(510, 224, 219, 20);
		this.add(txt_PhoneNumber);

		lbl_PhoneNumber = new JLabel("Phone Number");
		lbl_PhoneNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_PhoneNumber.setBounds(400, 229, 100, 14);
		this.add(lbl_PhoneNumber);

		lbl_UpdateMessage = new JLabel("");
		lbl_UpdateMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_UpdateMessage.setBounds(350, 400, 200, 14);
		this.add(lbl_UpdateMessage);
		
		lbl_Doctor = new JLabel("Doctor:");
		lbl_PhoneNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_Doctor.setBounds(63, 40, 61, 16);
		this.add(lbl_Doctor);
		
		comboBox_Doctor = new JComboBox<CustomComboBoxItem>();
		comboBox_Doctor.addItemListener(new ItemListener() 
		{
			@Override
			public void itemStateChanged(ItemEvent e) {
	            // Prevent this event from firing until the combo box is fully populated (each item added fires this event)
	        	if(e.getStateChange() == ItemEvent.SELECTED
	            		&& isDoctorComboBoxLoaded)
	            {
		        	PopulatePatientTable();
	            }
			}
	    });
		comboBox_Doctor.setBounds(118, 36, 219, 20);
		this.add(comboBox_Doctor);
		
		chckbx_CreateNew = new JCheckBox("Create New");
		chckbx_CreateNew.setBounds(400, 36, 128, 23);
		chckbx_CreateNew.setSelected(true);
		if (pageLoadMode == PatientLoadMode.STAFF) 
		{
			chckbx_CreateNew.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED)
		            {
						txt_PatientID.setText(null);
						txt_PatientID.setEnabled(false);
						txt_SIN.setText(null);
						txt_FirstName.setText(null);
						txt_LastName.setText(null);
						txt_HealthCardNum.setText(null);
						txt_Address.setText(null);
						txt_PhoneNumber.setText(null);
						txt_LastVisitDate.setText(null);
						
						btnUpdate.setText("Create");
		            }
					else
					{
						txt_PatientID.setEnabled(true);
						dbQuery.Patient_UpdatePatientInformation(
								txt_PatientID.getText(), txt_SIN.getText(),
								txt_FirstName.getText(), txt_LastName.getText(),
								txt_HealthCardNum.getText(), txt_Address.getText(),
								txt_PhoneNumber.getText());

						lbl_UpdateMessage.setText("Patient Update Successful.");
					}
				}
			});
		this.add(chckbx_CreateNew);
		}
		else if (pageLoadMode == PatientLoadMode.DOCTOR)
		{
			chckbx_CreateNew.setVisible(false);
		}
		DefaultTableModel model = new DefaultTableModel(tableColumns, 0);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 75, 375, 275);
		this.add(scrollPane);
		
		table_Patients = new JTable(model) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table_Patients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_Patients.getColumnModel().getColumn(0).setPreferredWidth(110);
		table_Patients.removeColumn(table_Patients.getColumnModel().getColumn(TABLE_PATIENT_ID_COLUMN_INDEX));
		
		table_Patients.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) 
			{
				if(table_Patients.getSelectedRow() != -1)
				{
					btnUpdate.setText("Update");
					chckbx_CreateNew.setSelected(false);
					String PatientID = (table_Patients.getModel().getValueAt(table_Patients.getSelectedRow(), TABLE_PATIENT_ID_COLUMN_INDEX).toString());
					loadPatientInformation(PatientID);
				}
			}
		});
		table_Patients.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_Patients.setBounds(782, 495, -382, -100);
		table_Patients.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		System.out.println(table_Patients.getModel());
		scrollPane.setViewportView(table_Patients);

		btnUpdate = new JButton();
		btnUpdate.setText("Create");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO: error checking on patient update

				// Update database with new patient information
				if (!chckbx_CreateNew.isSelected()) 
				{
					dbQuery.Patient_UpdatePatientInformation(
							txt_PatientID.getText(), txt_SIN.getText(),
							txt_FirstName.getText(), txt_LastName.getText(),
							txt_HealthCardNum.getText(), txt_Address.getText(),
							txt_PhoneNumber.getText());

					lbl_UpdateMessage.setText("Patient Update Successful.");
				} 
				else 
				{
					dbQuery.Patient_CreateNewPatientInformation(
							txt_SIN.getText(), txt_FirstName.getText(),
							txt_LastName.getText(),
							txt_HealthCardNum.getText(), txt_Address.getText(),
							txt_PhoneNumber.getText());

					lbl_UpdateMessage.setText("Patient Creation Successful.");
				}
			}
		});
		btnUpdate.setBounds(375, 360, 117, 29);
		
		if (pageLoadMode != PatientLoadMode.DOCTOR) 
		{
			this.add(btnUpdate);
		}
	}
}
