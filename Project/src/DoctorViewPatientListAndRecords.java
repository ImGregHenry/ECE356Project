import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;


public class DoctorViewPatientListAndRecords extends JPanel {

	/**
	 * Create the panel.
	 */
	private User user;

	private JCheckBox chckbxAllowArchivedRecords;
	private JComboBox comboBox_AllPatients;
	private int PATIENT_TABLE_PATIENT_ID_INDEX = 1;
	private String patientTableColumns[] = new String[] { "Have Access", "PatientID", "Name", "Last Visit Date"  };
	private int PATIENT_TABLE_COLUMN_COUNT = 4;
	
	private String vrTableColumns[] = new String[] { "isArchived", "DoctorName", "ApptDate", "Procedure", "Diagnosis", "Prescription", "Comments", "Entered Date" };
	private int VR_TABLE_COLUMN_COUNT = 8;
	
	private JTextField txt_FirstName;
	private JTextField txt_LastName;
	private JTable table_Patients;
	private JTable table_VR;
	private JTextField txt_PatientID;
	private JTextField txt_Diagnosis;
	private JTextField txt_Procedure;
	private JTextField txt_Comments;
	private JTextField txt_Prescription;
	
	public DoctorViewPatientListAndRecords(User _user) {
		this.user = _user;
		
		initialize();	
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
	
	private void ResetVRTable()
	{
		if(table_VR != null)
		{
			while(table_VR.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_VR.getModel()).removeRow(0);
			}	
		}
	}
	
	private void PopulatePatientTable()
	{
		ResetPatientTable();
		
			
		ResultSet rs = dbQuery.Doctor_GetPatientList(this.user.DoctorID, txt_FirstName.getText(),
				txt_LastName.getText(), 
				txt_PatientID.getText(),
				"2013-01-01 00:00:00",
				"2015-01-01 00:00:00");
		//			"YYYY-MM-dd HH:mm:SS");

		
		
		//System.out.println("Querying doctor appointment schedule for doctorID: " + selectedDoctor.getID() + "!");
		
		Object[] row = new Object[PATIENT_TABLE_COLUMN_COUNT];
		int count = 0;
		try {
			while(rs.next())
			{  
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd YYYY");
				//SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
				
				//"Have Access", "PatientID", "Name", "Last Visit Date" 
				
			    row[0] = rs.getObject("HasAccess");	
			    row[1] = rs.getObject("PatientID"); 	
			    row[2] = rs.getObject("FirstName") + " " + rs.getObject("LastName");		
			    row[3] = rs.getObject("LastVisitDate"); //dateFormat.format(
			    System.out.println("ACCESS: " + rs.getString("HasAccess"));  
				if(rs.getString("HasAccess").equals("yes") 
						|| comboBox_AllPatients.getSelectedItem() == "All")
				{
					((DefaultTableModel) table_Patients.getModel()).insertRow(count++, row);
				}
			}
			
			//System.out.println("Successfully loaded doctor appointment table!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: loading doctor appointment table!");
			e.printStackTrace();
		}
	}

	private void PopulateVRTable(String patID)
	{
		ResetVRTable();
		
		
//		ResultSet rs = dbQuery.Doctor_GetPatientList(this.user.DoctorID, true, txt_FirstName.getText(),
//				txt_LastName.getText(), 
//				txt_PatientID.getText(),
//				"2013-01-01 00:00:00",
//				"2015-01-01 00:00:00");
		//			"YYYY-MM-dd HH:mm:SS");

		System.out.println("HERE2! - " + table_Patients.getSelectedRow());
		
		if(table_Patients.getSelectedRow() == -1)
			return;
			
		System.out.println("HERE3!");
		ResultSet rs = dbQuery.Doctor_GetFilteredVRList(chckbxAllowArchivedRecords.isSelected(), patID,
				txt_Diagnosis.getText(),
				txt_Comments.getText(),
				txt_Procedure.getText(),
				txt_Prescription.getText());
		
		
		//System.out.println("Querying doctor appointment schedule for doctorID: " + selectedDoctor.getID() + "!");
		
		Object[] row = new Object[VR_TABLE_COLUMN_COUNT];
		try {
			while(rs.next())
			{  
				//SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd YYYY");
				//SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
				
				//"isArchived", "DoctorName", "ApptDate", "Procedure", "Diagnosis", "Prescription", "Comments", "Entered Date"
				if(!chckbxAllowArchivedRecords.isSelected()){
					row[0] = "no";
				}
				else{
					row[0] = rs.getObject("isArchived");
				}
			    row[1] = rs.getObject("DoctorFirstName") + " " + rs.getObject("DoctorLastName");
			    row[2] = rs.getObject("AppointmentDate");
			    		
			    row[3] = rs.getObject("ProcedureName");
			    row[4] = rs.getObject("Diagnosis");
			    row[5] = rs.getObject("Prescription");
			    row[6] = rs.getObject("DoctorComment");
			    row[7] = rs.getObject("EnteredDate");
			    
			    
			    ((DefaultTableModel) table_VR.getModel()).insertRow(rs.getRow()-1, row);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: loading doctor vr table!");
			e.printStackTrace();
		}
		
	}

	
	public void initialize()
	{
		setLayout(null);
		
		txt_FirstName = new JTextField();
		txt_FirstName.setBounds(82, 84, 214, 22);
		add(txt_FirstName);
		txt_FirstName.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(12, 70, 200, 50);
		add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(10, 112, 200, 50);
		add(lblLastName);
		
		txt_LastName = new JTextField();
		txt_LastName.setColumns(10);
		txt_LastName.setBounds(80, 126, 214, 22);
		add(txt_LastName);
		
		JLabel lblID = new JLabel("Patient ID");
		lblID.setBounds(10, 154, 200, 50);
		add(lblID);
		
		txt_PatientID = new JTextField();
		txt_PatientID.setColumns(10);
		txt_PatientID.setBounds(80, 168, 214, 22);
		add(txt_PatientID);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 250, 605, 402);
		this.add(scrollPane);
		
		JScrollPane scrollPaneVR = new JScrollPane();
		scrollPaneVR.setBounds(700, 250, 1100, 402);
		this.add(scrollPaneVR);
		
		
		JButton btn_FilterPatientList = new JButton();
		btn_FilterPatientList.setText("Filter");
		btn_FilterPatientList.setBounds(80, 200, 75, 22);
		btn_FilterPatientList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//LOAD PATIENT TABLE
				PopulatePatientTable();
			}
		});
		this.add(btn_FilterPatientList);
		
		JButton btn_FilterVRList = new JButton();
		btn_FilterVRList.setText("Filter");
		btn_FilterVRList.setBounds(1150, 200, 75, 22);
		btn_FilterVRList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table_Patients.getSelectedRow() != -1){
					String patID = table_Patients.getValueAt(table_Patients.getSelectedRow(), PATIENT_TABLE_PATIENT_ID_INDEX).toString();
					PopulateVRTable(patID);
				}
			}
		});
		this.add(btn_FilterVRList);
		
		
		DefaultTableModel model = new DefaultTableModel(patientTableColumns, 0);
		
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
		table_Patients.getColumnModel().getColumn(0).setPreferredWidth(100);
		table_Patients.getColumnModel().getColumn(1).setPreferredWidth(100);
		table_Patients.getColumnModel().getColumn(2).setPreferredWidth(200);
		table_Patients.getColumnModel().getColumn(3).setPreferredWidth(200);
		
		table_Patients.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				
				// Prevent this event from firing twice
				// Disable the cancel button if this appointment can't be cancelled
				if (!e.getValueIsAdjusting() 
						&& table_Patients.getSelectedRow() != -1) {
					//POPULATEVRTABLE
					String patID = table_Patients.getValueAt(table_Patients.getSelectedRow(), PATIENT_TABLE_PATIENT_ID_INDEX).toString();
					System.out.println("HERE!");
					PopulateVRTable(patID);
				}
			}
		});
		table_Patients.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_Patients.setBounds(782, 495, -382, -100);
		table_Patients.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//frame.getContentPane().add(table_VisitationRecord);
		scrollPane.setViewportView(table_Patients);

		DefaultTableModel model2 = new DefaultTableModel(vrTableColumns, 0);
		
		table_VR = new JTable(model2) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table_VR.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_VR.getColumnModel().getColumn(0).setPreferredWidth(75);
		table_VR.getColumnModel().getColumn(1).setPreferredWidth(100);
		table_VR.getColumnModel().getColumn(2).setPreferredWidth(200);
		table_VR.getColumnModel().getColumn(3).setPreferredWidth(200);
		table_VR.getColumnModel().getColumn(4).setPreferredWidth(200);
		table_VR.getColumnModel().getColumn(5).setPreferredWidth(200);
		table_VR.getColumnModel().getColumn(6).setPreferredWidth(200);
		table_VR.getColumnModel().getColumn(7).setPreferredWidth(200);
		table_VR.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_VR.setBounds(782, 495, -382, -100);
		table_VR.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//frame.getContentPane().add(table_VisitationRecord);
		scrollPaneVR.setViewportView(table_VR);

		
		JLabel lblDiagnosis = new JLabel("Diagnosis");
		lblDiagnosis.setBounds(849, 54, 200, 50);
		add(lblDiagnosis);
		
		txt_Diagnosis = new JTextField();
		txt_Diagnosis.setColumns(10);
		txt_Diagnosis.setBounds(919, 68, 214, 22);
		add(txt_Diagnosis);
		
		JLabel lblComments = new JLabel("Comments");
		lblComments.setBounds(847, 96, 200, 50);
		add(lblComments);
		
		JLabel lblProcedure = new JLabel("Procedure");
		lblProcedure.setBounds(847, 138, 200, 50);
		add(lblProcedure);
		
		txt_Procedure = new JTextField();
		txt_Procedure.setColumns(10);
		txt_Procedure.setBounds(917, 152, 214, 22);
		add(txt_Procedure);
		
		txt_Comments = new JTextField();
		txt_Comments.setColumns(10);
		txt_Comments.setBounds(917, 110, 214, 22);
		add(txt_Comments);
		
		JLabel lblPrescription = new JLabel("Prescription");
		lblPrescription.setBounds(849, 186, 200, 50);
		add(lblPrescription);
		
		txt_Prescription = new JTextField();
		txt_Prescription.setColumns(10);
		txt_Prescription.setBounds(919, 201, 214, 22);
		add(txt_Prescription);
		
		chckbxAllowArchivedRecords = new JCheckBox("Allow Archived Records");
		chckbxAllowArchivedRecords.setBounds(919, 34, 214, 25);
		chckbxAllowArchivedRecords.setSelected(false);
		add(chckbxAllowArchivedRecords);
		
		comboBox_AllPatients = new JComboBox();
		comboBox_AllPatients.setModel(new DefaultComboBoxModel(new String[] {"All", "Current"}));
		comboBox_AllPatients.setBounds(82, 49, 214, 22);
		add(comboBox_AllPatients);
		
		JLabel lblPatients = new JLabel("Patients");
		lblPatients.setBounds(12, 54, 56, 16);
		add(lblPatients);

	
	}
}
