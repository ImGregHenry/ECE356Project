import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AssignPatientToDoctor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String staffID;
	private JComboBox<CustomComboBoxItem> comboBox_Patient;
	private JComboBox<CustomComboBoxItem> comboBox_Doctor;
	private JLabel lbl_AssignPatientToDocMessage;
	private JLabel lbl_DeletePatientToDocAssignment;
	private JTable table_PatientToDoctorAssignments;
	
	private int TABLE_PATIENTID_COLUMN_INDEX = 2;
	private int TABLE_DOCTORID_COLUMN_INDEX = 3;
	
	private String tableColumns[] = new String[] { "Patient Name", "Doctor Name", "PatientID", "AssignedToDoctorID" };
	
	
	/**
	 * Create the panel.
	 */
	public AssignPatientToDoctor(String _staffID) {
		this.staffID = _staffID;
		
		Initialize();
		PopulatePatientDropDown();
		PopulateDoctorDropDown();
		PopulatePatientDoctorAssignmentTable();
	}

	private void ResetPatientDoctorAssignmentTable()
	{
		if(table_PatientToDoctorAssignments != null)
		{
			while(table_PatientToDoctorAssignments.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_PatientToDoctorAssignments.getModel()).removeRow(0);
			}	
		}
	}
	
	private void PopulatePatientDoctorAssignmentTable()
	{
		ResultSet rs = dbQuery.Staff_GetPatientDoctorAssignments();
		
		ResetPatientDoctorAssignmentTable();
		
		
		
		System.out.println("Querying for Patient to Doctor Assignments.");
		
		Object[] row = new Object[tableColumns.length];
		try {
			while(rs.next())
			{  
				row[0] = rs.getObject("PatientName");		
			    row[1] = rs.getObject("DoctorName"); 	
			    row[2] = rs.getObject("PatientID");
			    row[3] = rs.getObject("AssignedToDoctorID");	
			    ((DefaultTableModel) table_PatientToDoctorAssignments.getModel()).insertRow(rs.getRow()-1, row);
			}
			
			System.out.println("Successfully loaded patient to doctor assignment table!");
		} catch (SQLException e) {
			System.out.println("ERROR: loading patient to doctor assignment table!");
			e.printStackTrace();
		}
	}
	
	private void Initialize()
	{
		setLayout(null);
		comboBox_Patient = new JComboBox<CustomComboBoxItem>();
		comboBox_Patient.setBounds(125, 80, 156, 20);
		add(comboBox_Patient);
		
		JLabel lbl_Patient = new JLabel("Patient:");
		lbl_Patient.setBounds(69, 83, 46, 14);
		lbl_Patient.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lbl_Patient);
		
		JLabel lbl_Doctor = new JLabel("Doctor:");
		lbl_Doctor.setBounds(69, 129, 46, 14);
		lbl_Doctor.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lbl_Doctor);
		
		comboBox_Doctor = new JComboBox<CustomComboBoxItem>();
		comboBox_Doctor.setBounds(125, 126, 156, 20);
		add(comboBox_Doctor);
		
		JButton btn_AssignPatientToDoctor = new JButton("Assign Patient to Doctor");
		btn_AssignPatientToDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomComboBoxItem selectedPatient = (CustomComboBoxItem)comboBox_Patient.getSelectedItem();
				CustomComboBoxItem selectedDoctor = (CustomComboBoxItem)comboBox_Doctor.getSelectedItem();
				
				if(dbQuery.Staff_CanAssignPatientToDoctor(selectedPatient.getID(), selectedDoctor.getID()))
				{
					dbQuery.Staff_AssignPatientToDoctor(selectedPatient.getID(), selectedDoctor.getID());
					lbl_AssignPatientToDocMessage.setText("Successfully assigned patient to doctor.");
					lbl_DeletePatientToDocAssignment.setText("");
					PopulatePatientDoctorAssignmentTable();
				}
				else
				{
					lbl_AssignPatientToDocMessage.setText("Failed to assign patient to doctor.");
					lbl_DeletePatientToDocAssignment.setText("");
				}
				
			}
		});
		btn_AssignPatientToDoctor.setBounds(305, 104, 243, 23);
		add(btn_AssignPatientToDoctor);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 243, 603, 402);
		add(scrollPane);
		
			
			DefaultTableModel model = new DefaultTableModel(tableColumns, 0);
		
			table_PatientToDoctorAssignments = new JTable(model) 
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
	
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table_PatientToDoctorAssignments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table_PatientToDoctorAssignments.getColumnModel().getColumn(0).setPreferredWidth(300);
			table_PatientToDoctorAssignments.getColumnModel().getColumn(1).setPreferredWidth(300);
		
			table_PatientToDoctorAssignments.removeColumn(table_PatientToDoctorAssignments.getColumnModel().getColumn(TABLE_DOCTORID_COLUMN_INDEX));
			table_PatientToDoctorAssignments.removeColumn(table_PatientToDoctorAssignments.getColumnModel().getColumn(TABLE_PATIENTID_COLUMN_INDEX));
			
			
		
		table_PatientToDoctorAssignments.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Prevent this event from firing twice
				// Disable the cancel button if this appointment can't be cancelled
//				if (!e.getValueIsAdjusting() 
//						&& table_Appointments.getSelectedRow() != -1) {
//					if(table_Appointments.getModel().getValueAt(table_Appointments.getSelectedRow(), TABLE_CAN_DELETE_COLUMN_INDEX).toString().equals("true"))
//						btnCancelAppointment.setEnabled(true);
//					else
//						btnCancelAppointment.setEnabled(false);
//				}
			}
		});
		table_PatientToDoctorAssignments.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_PatientToDoctorAssignments.setBounds(782, 495, -382, -100);
		table_PatientToDoctorAssignments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_PatientToDoctorAssignments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table_PatientToDoctorAssignments);
		
		lbl_AssignPatientToDocMessage = new JLabel("");
		lbl_AssignPatientToDocMessage.setFont(new Font("Calibri", Font.BOLD, 15));
		lbl_AssignPatientToDocMessage.setBounds(315, 134, 304, 30);
		add(lbl_AssignPatientToDocMessage);
		
		JButton btnDeleteSelectedPatient = new JButton("Delete Selected Patient Assignment");
		btnDeleteSelectedPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String patID = table_PatientToDoctorAssignments.getModel().getValueAt(table_PatientToDoctorAssignments.getSelectedRow(), TABLE_PATIENTID_COLUMN_INDEX).toString();
				String docID = table_PatientToDoctorAssignments.getModel().getValueAt(table_PatientToDoctorAssignments.getSelectedRow(), TABLE_DOCTORID_COLUMN_INDEX).toString();
						
				dbQuery.Staff_DeletePatientToDoctorAssignment(patID, docID);
				
				lbl_DeletePatientToDocAssignment.setText("Successfully deleted assignment of patient to doctor.");
				lbl_AssignPatientToDocMessage.setText("");
				System.out.println("Successfully delete assignment of patientID '" + patID + "' to doctorID '" + docID + "'.");
				
				PopulatePatientDoctorAssignmentTable();
			}
		});
		btnDeleteSelectedPatient.setBounds(162, 207, 271, 23);
		add(btnDeleteSelectedPatient);
		
		lbl_DeletePatientToDocAssignment = new JLabel("");
		lbl_DeletePatientToDocAssignment.setFont(new Font("Calibri", Font.BOLD, 15));
		lbl_DeletePatientToDocAssignment.setBounds(443, 207, 304, 30);
		add(lbl_DeletePatientToDocAssignment);
	}
	
	private void PopulatePatientDropDown()
	{
		try {
			ResultSet rs = dbQuery.Staff_GetAllPatientInfo();
			
			while(rs.next())
			{  
				String patientName = rs.getObject("FirstName") + " " + rs.getObject("LastName");
				comboBox_Patient.addItem(new CustomComboBoxItem(rs.getObject("PatientID").toString(), patientName));
			}
			
			System.out.println("Patient Appointment Drop Down List Loaded.");
			
		} catch (SQLException e) {
			System.out.println("ERROR: loading patient appointment filter list.");
			e.printStackTrace();
		}
	}
	

	private void PopulateDoctorDropDown()
	{
		try {
			
			ResultSet rs = dbQuery.Staff_GetAllDoctorInfo();
			
			while(rs.next())
			{  
				String doctorName = rs.getObject("FirstName") + " " + rs.getObject("LastName"); 
				comboBox_Doctor.addItem(new CustomComboBoxItem(rs.getObject("DoctorID").toString(), doctorName));
			}
			
			System.out.println("Doctor Appointment Filter List Loaded.");
			System.out.println("Doctor Appointment Scheduler List Loaded.");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erorr loading doctor appointment filter list.");
			e.printStackTrace();
		}
	}
}
