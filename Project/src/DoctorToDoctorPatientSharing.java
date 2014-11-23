import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;


public class DoctorToDoctorPatientSharing extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<CustomComboBoxItem> comboBox_Patient;
	private JComboBox<CustomComboBoxItem> comboBox_Doctor;
	private JLabel lbl_AssignPatientToDocMessage;
	private JLabel lbl_DeletePatientToDocSharing; 
	private JLabel lbl_PageTitle;
	private JLabel lbl_WelcomeDoctor; 
	private JTable table_PatientToDoctorAssignments;
	private User User;
	
	private int TABLE_SHARING_DOCTORID_COLUMN_INDEX = 3;
	private int TABLE_PATIENTID_COLUMN_INDEX = 4;
	private int TABLE_ASSIGNED_DOCTORID_COLUMN_INDEX = 5;
	
	private String tableColumns[] = new String[] { "Sharing Doctor Name", "Patient Name", "Assigned Doctor Name", "DoctorIDSharingPatient", "PatientID", "AssignedToDoctorID" };
	
	/**
	 * Create the panel.
	 */
	public DoctorToDoctorPatientSharing(User _user) {
		this.User = _user;
		
		Initialize();
		Refresh();
	}

	public void Refresh()
	{
		lbl_WelcomeDoctor.setText("Welcome Doctor: " + this.User.DoctorFirstName + " " + this.User.DoctorLastName);
		PopulateAssignToDoctorDropDown();
		PopulatePatientDropDown();
		PopulateDoctorToDoctorPatientSharingTable();
	}
	
	private void Initialize()
	{
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(101, 318, 903, 402);
		add(scrollPane);

		ImageIcon refreshImage = new ImageIcon(getClass().getResource("ref.png"));
		
		JButton btnNewButton = new JButton("", refreshImage);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Refresh();
			}
		});	
		btnNewButton.setBounds(10, 11, 35, 35);
		add(btnNewButton);
		
		DefaultTableModel model = new DefaultTableModel(tableColumns, 0);
	
		table_PatientToDoctorAssignments = new JTable(model);
		table_PatientToDoctorAssignments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_PatientToDoctorAssignments.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_PatientToDoctorAssignments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_PatientToDoctorAssignments.getColumnModel().getColumn(0).setPreferredWidth(300);
		table_PatientToDoctorAssignments.getColumnModel().getColumn(1).setPreferredWidth(300);
		table_PatientToDoctorAssignments.getColumnModel().getColumn(2).setPreferredWidth(300);
		
		table_PatientToDoctorAssignments.removeColumn(table_PatientToDoctorAssignments.getColumnModel().getColumn(TABLE_ASSIGNED_DOCTORID_COLUMN_INDEX));
		table_PatientToDoctorAssignments.removeColumn(table_PatientToDoctorAssignments.getColumnModel().getColumn(TABLE_PATIENTID_COLUMN_INDEX));
		table_PatientToDoctorAssignments.removeColumn(table_PatientToDoctorAssignments.getColumnModel().getColumn(TABLE_SHARING_DOCTORID_COLUMN_INDEX));
		
		table_PatientToDoctorAssignments.setBounds(782, 495, -382, -100);
		scrollPane.setViewportView(table_PatientToDoctorAssignments);
		
		comboBox_Patient = new JComboBox<CustomComboBoxItem>();
		comboBox_Patient.setBounds(197, 140, 300, 20);
		add(comboBox_Patient);
		
		JLabel lbl_PatientName = new JLabel("Patient:");
		lbl_PatientName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_PatientName.setBounds(141, 143, 46, 14);
		add(lbl_PatientName);
		
		JLabel lbl_Doctor = new JLabel("Doctor:");
		lbl_Doctor.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_Doctor.setBounds(141, 189, 46, 14);
		add(lbl_Doctor);
		
		comboBox_Doctor = new JComboBox<CustomComboBoxItem>();
		comboBox_Doctor.setBounds(197, 186, 300, 20);
		add(comboBox_Doctor);
		
		JButton btn_SharePatientWithDoctor = new JButton("Share Patient with Doctor");
		btn_SharePatientWithDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomComboBoxItem selectedPatient = (CustomComboBoxItem)comboBox_Patient.getSelectedItem();
				CustomComboBoxItem selectedDoctor = (CustomComboBoxItem)comboBox_Doctor.getSelectedItem();
				
				
				if(dbQuery.Doctor_CanDoctorToDoctorSharePatient(User.DoctorID, selectedPatient.getID(), selectedDoctor.getID()))
				{
					dbQuery.Doctor_SharePatientDoctorToDoctor(User.DoctorID, selectedPatient.getID(), selectedDoctor.getID());
					lbl_AssignPatientToDocMessage.setText("Successfully assigned staff to doctor.");
					lbl_DeletePatientToDocSharing.setText("");
					
					PopulateDoctorToDoctorPatientSharingTable();
				}
				else
				{
					lbl_AssignPatientToDocMessage.setText("Failed to staff staff to doctor.");
					lbl_DeletePatientToDocSharing.setText("");
				}
			}
		});
		btn_SharePatientWithDoctor.setBounds(539, 166, 243, 23);
		add(btn_SharePatientWithDoctor);
		
		JButton btnDeleteSelectedPatientSharing = new JButton("Delete Selected Patient Sharing");
		btnDeleteSelectedPatientSharing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String patID = table_PatientToDoctorAssignments.getModel().getValueAt(table_PatientToDoctorAssignments.getSelectedRow(), TABLE_PATIENTID_COLUMN_INDEX).toString();
				String assignedDocID = table_PatientToDoctorAssignments.getModel().getValueAt(table_PatientToDoctorAssignments.getSelectedRow(), TABLE_ASSIGNED_DOCTORID_COLUMN_INDEX).toString();
				String sharingDocID = table_PatientToDoctorAssignments.getModel().getValueAt(table_PatientToDoctorAssignments.getSelectedRow(), TABLE_SHARING_DOCTORID_COLUMN_INDEX).toString();
				
				dbQuery.Doctor_DeleteDoctorToDoctorPatientSharing(sharingDocID, patID, assignedDocID);
				lbl_DeletePatientToDocSharing.setText("Successfully deleted staff to doctor assignment.");
				lbl_AssignPatientToDocMessage.setText("");
				
				PopulateDoctorToDoctorPatientSharingTable();
			}
		});
		btnDeleteSelectedPatientSharing.setBounds(197, 267, 308, 23);
		add(btnDeleteSelectedPatientSharing);
		
		lbl_AssignPatientToDocMessage = new JLabel("");
		lbl_AssignPatientToDocMessage.setFont(new Font("Calibri", Font.BOLD, 15));
		lbl_AssignPatientToDocMessage.setBounds(539, 198, 424, 30);
		add(lbl_AssignPatientToDocMessage);
		 
		lbl_DeletePatientToDocSharing = new JLabel("");
		lbl_DeletePatientToDocSharing.setFont(new Font("Calibri", Font.BOLD, 15));
		lbl_DeletePatientToDocSharing.setBounds(551, 260, 444, 30);
		add(lbl_DeletePatientToDocSharing);
		
		lbl_WelcomeDoctor = new JLabel("Welcome Doctor:");
		lbl_WelcomeDoctor.setFont(new Font("Calibri", Font.BOLD, 20));
		lbl_WelcomeDoctor.setBounds(73, 67, 540, 30);
		add(lbl_WelcomeDoctor);
		
		lbl_PageTitle = new JLabel("Share Patient With Doctor");
		lbl_PageTitle.setFont(new Font("Calibri", Font.BOLD, 28));
		lbl_PageTitle.setBounds(281, 11, 372, 30);
		add(lbl_PageTitle);
	}
	
	private void ResetDoctorToDoctorPatientSharingTable()
	{
		if(table_PatientToDoctorAssignments != null)
		{
			while(table_PatientToDoctorAssignments.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_PatientToDoctorAssignments.getModel()).removeRow(0);
			}	
		}
	}
	
	private void PopulateDoctorToDoctorPatientSharingTable()
	{
		ResetDoctorToDoctorPatientSharingTable();
		
		ResultSet rs = dbQuery.Doctor_GetDoctorToDoctorPatientSharing(this.User.DoctorID);
		
		//System.out.println("Querying for Staff to Doctor Assignments.");
		
		Object[] row = new Object[tableColumns.length];
		try {
			while(rs.next())
			{  
				row[0] = rs.getObject("SharingDoctorName");		
			    row[1] = rs.getObject("PatientName");
			    row[2] = rs.getObject("AssignedDoctorName");
			    row[TABLE_SHARING_DOCTORID_COLUMN_INDEX] = rs.getObject("DoctorIDSharingPatient");
			    row[TABLE_PATIENTID_COLUMN_INDEX] = rs.getObject("PatientID");
			    row[TABLE_ASSIGNED_DOCTORID_COLUMN_INDEX] = rs.getObject("AssignedToDoctorID");
			    ((DefaultTableModel) table_PatientToDoctorAssignments.getModel()).insertRow(rs.getRow()-1, row);
			}
			
			System.out.println("Successfully loaded staff to doctor assignment table!");
		} catch (SQLException e) {
			System.out.println("ERROR: loading staff to doctor assignment table!");
			e.printStackTrace();
		}
	}
	
	private void PopulateAssignToDoctorDropDown()
	{
		try {
			comboBox_Doctor.removeAllItems();
			ResultSet rs = dbQuery.Staff_GetAllDoctorsExceptDocID(this.User.DoctorID);
			
			while(rs.next())
			{  
				String docName = rs.getObject("FirstName") + " " + rs.getObject("LastName");
				comboBox_Doctor.addItem(new CustomComboBoxItem(rs.getObject("DoctorID").toString(), docName));
			}
			
			System.out.println("Staff List Drop Down Loaded.");
			
		} catch (SQLException e) {
			System.out.println("ERROR: loading staff drop down list.");
			e.printStackTrace();
		}
	}
	
	private void PopulatePatientDropDown()
	{
		try {
			comboBox_Patient.removeAllItems();
			
			// Exclude the current doctor
			ResultSet rs = dbQuery.Doctor_GetAllDoctorsAssignedPatients(this.User.DoctorID);
			
			while(rs.next())
			{  
				String patientName = rs.getObject("PatientName").toString();
				comboBox_Patient.addItem(new CustomComboBoxItem(rs.getObject("PatientID").toString(), patientName));
			}
			
			System.out.println("Assigned to Doctor Drop Down List Loaded.");
			
		} catch (SQLException e) {
			System.out.println("ERROR: loading assigned to doctor drop down list.");
			e.printStackTrace();
		}
	}
}
