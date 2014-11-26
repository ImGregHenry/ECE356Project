import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;


public class CreateVisitationRecordPanel extends JPanel {
	private JTextField txtVReason;
	private JTextField txt_ApptLength;
	private JTextArea tfDocComm;
	private JTable table_VisitationRecord;
	private JComboBox<CustomComboBoxItem> comboBox_SchedulePatient;
	private JComboBox<CustomComboBoxItem> comboBox_ScheduleDoctor;
	private JComboBox<CustomComboBoxItem> comboBox_Procedures;


	private JTextField txtApptDate;
	private JTextField txtDoctorName;
	String appDate;
	String appLength; 
	String doctor;
	String patient;
	String dComments;
	String vReason ;
	String procFee ;
	String procName;
	String userId ="";
	String entDate;
	String doctorID;
	String prescription;
	User user;
	String appID;
	private JTextField txtPatient;
	CustomComboBoxItem selectedPatient = new CustomComboBoxItem("-1","");
	CustomComboBoxItem selectedDoctor = new CustomComboBoxItem("-1","");
	private boolean populating = false;
	public CreateVisitationRecordPanel(User login)
	{
		ImageIcon refreshImage = new ImageIcon(getClass().getResource("ref.png"));
		
		JButton btn_Refresh = new JButton("", refreshImage);
		btn_Refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Refresh();
			}
		});	
		btn_Refresh.setBounds(20, 11, 35, 35);
		this.add(btn_Refresh);
		
		user = login;
		if (login.accessLevel == Login.LoginAccessLevel.DOCTOR)
		userId = login.DoctorID;
		
		comboBox_Procedures = new JComboBox<CustomComboBoxItem>();
		comboBox_Procedures.setBounds(994, 344, 269, 36);
		this.add(comboBox_Procedures);
		
		comboBox_SchedulePatient = new JComboBox<CustomComboBoxItem>();
		comboBox_SchedulePatient.setBounds(381, 38, 173, 36);
		
		comboBox_SchedulePatient.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent arg0) {
	        	if (!populating){
		            // Prevent this event from firing until the combo box is fully populated (each item added fires this event)
					selectedPatient = (CustomComboBoxItem)comboBox_SchedulePatient.getSelectedItem();
					
		
					fillTable(selectedDoctor.getID(), selectedPatient.getID());
	        	}
	        }
	    });
		this.add(comboBox_SchedulePatient);
		
		comboBox_ScheduleDoctor = new JComboBox<CustomComboBoxItem>();
		comboBox_ScheduleDoctor.setBounds(153, 38, 173, 36);
		
		comboBox_ScheduleDoctor.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent arg0) {
	        	if (!populating){
		            // Prevent this event from firing until the combo box is fully populated (each item added fires this event)
					selectedDoctor = (CustomComboBoxItem)comboBox_ScheduleDoctor.getSelectedItem();
					fillTable(selectedDoctor.getID(), selectedPatient.getID());								
	        	}
	        }
	    });
		this.add(comboBox_ScheduleDoctor);
		
		populating = true;
		PopulatePatientDropDown();
		PopulateDoctorDropDown();
		PopulateProcedureDropDown();
		populating = false;
	

		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 87, 900, 405);
		add(scrollPane);
		
		JButton btnCreateupdate = new JButton("Create/Update");
		
		btnCreateupdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomComboBoxItem procItem = ((CustomComboBoxItem) comboBox_Procedures.getSelectedItem());

				if (txtApptDate.getText().equals("") || txtDoctorName.getText().equals("") || txtPatient.getText().equals(""))
				{
					
					System.out.println(procItem.getName().substring(0,procItem.getName().indexOf(" ")));

					JOptionPane.showMessageDialog(null,"Please select a record");			
					return;
				}
				
				System.out.println(txt_ApptLength.getText().equals(appLength));
				System.out.println(txtVReason.getText().equals(vReason));
				
				System.out.println(tfDocComm.getText().equals(dComments));
				if (procItem.getID().equals("-1") || procItem.getName().substring(0,procItem.getName().indexOf(" - ")).equals(""))
				{
					JOptionPane.showMessageDialog(null,"Please select the procedure");
					return;
				}
				
				if (!dbQuery.check_visitationRecent(appID,entDate))
				{
					JOptionPane.showMessageDialog(null,"Your data seems to be outdated, Refreshing. Please resubmit after reset");
					Refresh();
					
					return;
				}
				
				if(txt_ApptLength.getText().equals(appLength) && txtVReason.getText().equals(vReason) && tfDocComm.getText().equals(dComments) &&
						procItem.getName().equals(procName))
				{
					JOptionPane.showMessageDialog(null,"Please change the record");
					return;
				}
				
				
				dbQuery.Visitation_UpdateAppointmentRecord(txt_ApptLength.getText(),appID,doctorID);
				dbQuery.Visitation_InsertVisitationRecord(appID,tfDocComm.getText(),txtVReason.getText(),
						procItem.getID(), procItem.getName().substring(0,procItem.getName().indexOf(" - ")));
			
				JOptionPane.showMessageDialog(null,"Successfully entered new visitation record.");

				Refresh();
			}
		});
		btnCreateupdate.setBounds(835, 630, 132, 23);
		add(btnCreateupdate);
		
		JLabel lblAppointmentDate = new JLabel("Appointment Date");
		lblAppointmentDate.setBounds(965, 116, 143, 23);
		add(lblAppointmentDate);
		
		JLabel lblAppointmentLength = new JLabel("Appointment Length");
		lblAppointmentLength.setBounds(965, 218, 143, 14);
		add(lblAppointmentLength);
		
		JLabel lblDoctorFilterName = new JLabel("Doctor");
		lblDoctorFilterName.setBounds(105, 49, 38, 14);
		add(lblDoctorFilterName);
		
		JLabel lblPatientFilterName = new JLabel("Patient");
		lblPatientFilterName.setBounds(336, 49, 45, 14);
		add(lblPatientFilterName);
		
		JLabel lblVisitReason = new JLabel("Diagnosis");
		lblVisitReason.setBounds(1019, 259, 143, 14);
		add(lblVisitReason);
		
		JLabel lblProcedureName = new JLabel("Procedure - Prescription Name");
		lblProcedureName.setBounds(1004, 306, 269, 25);
		add(lblProcedureName);
		
		JLabel lblDoctorsComment = new JLabel("Doctor's Comment");
		lblDoctorsComment.setBounds(850, 525, 143, 14);
		add(lblDoctorsComment);
		
		tfDocComm = new JTextArea();
		tfDocComm.setBounds(992, 521, 206, 103);
		tfDocComm.setLineWrap(true);
		add(tfDocComm);

		
		txtVReason = new JTextField();
		txtVReason.setBounds(1120, 256, 143, 20);
		add(txtVReason);
		txtVReason.setColumns(10);
		
		txt_ApptLength = new JTextField();
		txt_ApptLength.setBounds(1120, 215, 143, 20);
		add(txt_ApptLength);
		txt_ApptLength.setColumns(10);

		String col[] = new String[] { "Visitation Date", "Visitation Length", "Doctor", "Patient","Doctor's Comments", 
				"Diagnosis", "Procedure Fee", "Procedure Name","Entered Date","Doctor ID","Appointment ID","Prescription"};

		DefaultTableModel model = new DefaultTableModel(col, 0);
		
		
		table_VisitationRecord = new JTable(model) 
		{

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
		};
		scrollPane.setViewportView(table_VisitationRecord);
		
		
		table_VisitationRecord.getColumnModel().getColumn(0).setPreferredWidth(130);
		table_VisitationRecord.getColumnModel().getColumn(1).setPreferredWidth(88);
		table_VisitationRecord.getColumnModel().getColumn(2).setPreferredWidth(88);
		table_VisitationRecord.getColumnModel().getColumn(3).setPreferredWidth(143);
		table_VisitationRecord.getColumnModel().getColumn(4).setPreferredWidth(100);
		table_VisitationRecord.getColumnModel().getColumn(5).setPreferredWidth(100);
		table_VisitationRecord.getColumnModel().getColumn(6).setPreferredWidth(130);
		table_VisitationRecord.getColumnModel().getColumn(7).setPreferredWidth(130);
		table_VisitationRecord.getColumnModel().getColumn(8).setPreferredWidth(130);
		table_VisitationRecord.getColumnModel().getColumn(9).setMinWidth(0);
		table_VisitationRecord.getColumnModel().getColumn(9).setMaxWidth(0);
		table_VisitationRecord.getColumnModel().getColumn(9).setPreferredWidth(0);
		table_VisitationRecord.getColumnModel().getColumn(10).setMinWidth(0);
		table_VisitationRecord.getColumnModel().getColumn(10).setMaxWidth(0);
		table_VisitationRecord.getColumnModel().getColumn(11).setMinWidth(0);
		table_VisitationRecord.getColumnModel().getColumn(11).setMaxWidth(0);
		table_VisitationRecord.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_VisitationRecord.setBounds(300, 100, 100, 100);
		
		table_VisitationRecord.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JLabel lblDoctor = new JLabel("Doctor");
		lblDoctor.setBounds(1033, 152, 103, 23);
		add(lblDoctor);
		
		JLabel lblPatient = new JLabel("Patient");
		lblPatient.setBounds(1033, 185, 103, 17);
		add(lblPatient);
		
		txtApptDate = new JTextField();
		txtApptDate.setEnabled(false);
		txtApptDate.setBounds(1120, 117, 143, 20);
		add(txtApptDate);
		txtApptDate.setColumns(10);
		
		txtDoctorName = new JTextField();
		txtDoctorName.setEnabled(false);
		txtDoctorName.setBounds(1120, 152, 143, 20);
		add(txtDoctorName);
		txtDoctorName.setColumns(10);
		
		txtPatient = new JTextField();
		txtPatient.setEnabled(false);
		txtPatient.setBounds(1120, 184, 143, 20);
		add(txtPatient);
		txtPatient.setColumns(10);
		

		
		table_VisitationRecord.addMouseListener(new java.awt.event.MouseAdapter(){
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				int row = table_VisitationRecord.rowAtPoint(evt.getPoint());
				 appDate =  table_VisitationRecord.getModel().getValueAt(row, 0).toString();
				 appLength =  table_VisitationRecord.getModel().getValueAt(row, 1).toString();
				 doctor =  table_VisitationRecord.getModel().getValueAt(row, 2).toString();
				 patient = table_VisitationRecord.getModel().getValueAt(row, 3).toString();
				 dComments =  table_VisitationRecord.getModel().getValueAt(row, 4).toString();
				 vReason =  table_VisitationRecord.getModel().getValueAt(row, 5).toString();
				 procFee = table_VisitationRecord.getModel().getValueAt(row, 6).toString();
				 procName =  table_VisitationRecord.getModel().getValueAt(row, 7).toString();
				 entDate =  table_VisitationRecord.getModel().getValueAt(row, 8).toString();
				 appID = table_VisitationRecord.getModel().getValueAt(row, 9).toString();
				 doctorID = table_VisitationRecord.getModel().getValueAt(row, 10).toString();
				 procName += " - " + table_VisitationRecord.getModel().getValueAt(row, 11).toString();
				
				txtApptDate.setText(appDate);
				
				System.out.println("CurrentDoctor: "+doctorID+" currentAppointment: "+appID);
				txtDoctorName.setText(doctor);
				txtPatient.setText(patient);
	
				txt_ApptLength.setText(appLength);
				txtVReason.setText(vReason);
				comboBox_Procedures.getModel().setSelectedItem(new CustomComboBoxItem(procFee,procName));
				comboBox_Procedures.requestFocus();
				comboBox_Procedures.requestFocusInWindow();
				tfDocComm.setText(dComments);
				
				System.out.println(appDate + " "+ doctor);
				
							
			}
		});
		table_VisitationRecord.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		

		
		fillTable("-1","-1");
	}
	private void fillTable(String doctorId, String patientId)
	{
		((DefaultTableModel) table_VisitationRecord.getModel()).setRowCount(0);
		//ResultSet rs = dbQuery.Patient_GetPatientVisitationRecord(patientID);
		ResultSet rs = dbQuery.Visitation_getAllVisits(doctorId, patientId, userId);
		int columns = 12;
		
		Object[] row = new Object[columns];
		
		try {
			while(rs.next())
			{  
			    row[0] = rs.getObject("AppointmentDate");
			    row[1] = rs.getObject("AppointmentLength");
			    row[2] = rs.getObject("Doctor");
			    row[3] = rs.getObject("Patient");
			    row[4] = rs.getObject("DoctorComment");			    
			    row[5] = rs.getObject("VisitReason");
			    row[6] = rs.getObject("ProcedureFee");
			    row[7] = rs.getObject("ProcedureName");
			    row[8] = rs.getObject("EnteredDate");
			    row[9] = rs.getObject("AppointmentID");
			    row[10] = rs.getObject("DoctorID");
			    row[11] = rs.getObject("Prescription");
			    
			    ((DefaultTableModel) table_VisitationRecord.getModel()).insertRow(rs.getRow()-1, row);
			}
			
			System.out.println("Completed loading visitation history!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erorr loading visitation history!");
			e.printStackTrace();
		}
		
	}
	private void PopulatePatientDropDown()
	{
		try {
			ResultSet rs = dbQuery.Staff_GetAllPatientInfo();
			
			comboBox_SchedulePatient.addItem(new CustomComboBoxItem("-1", "All"));
			while(rs.next())
			{  
				String patientName = rs.getObject("FirstName") + " " + rs.getObject("LastName");
				comboBox_SchedulePatient.addItem(new CustomComboBoxItem(rs.getObject("PatientID").toString(), patientName));
			}
			
			System.out.println("Patient Appointment Scheduler List Loaded.");
			
		} catch (SQLException e) {
			System.out.println("ERROR: loading patient appointment filter list.");
			e.printStackTrace();
		}
	}
	
	private void PopulateDoctorDropDown()
	{
		try {
			ResultSet rs;

			rs = dbQuery.Staff_GetAllDoctorInfo();
			
			
			comboBox_ScheduleDoctor.addItem(new CustomComboBoxItem("-1", "All"));			
			while(rs.next())
			{  
				String doctorName = rs.getObject("FirstName") + " " + rs.getObject("LastName"); 
				comboBox_ScheduleDoctor.addItem(new CustomComboBoxItem(rs.getObject("DoctorID").toString(), doctorName));
			}
			
			System.out.println("Doctor Appointment Filter List Loaded.");
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erorr loading doctor appointment filter list.");
			e.printStackTrace();
		}
	}
	
	private void PopulateProcedureDropDown()
	{
		try
		{
			ResultSet rs = dbQuery.Visitation_getProcedures();
			comboBox_Procedures.addItem(new CustomComboBoxItem("-1",""));
			while(rs.next())
			{
				
				String procedureName = rs.getObject("ProcedureName").toString() + " - "+rs.getObject("Prescription");
				comboBox_Procedures.addItem(new CustomComboBoxItem(rs.getObject("ProcedureFee").toString(),procedureName));
			}
			System.out.println("Procedure List Loaded.");
		}
		catch (SQLException e)
		{
			System.out.println("Error loading Procedure filter list.");
		}
	}
	private void Refresh()
	{
		 appDate =  "";
		 appLength =  "";
		 doctor =  "";
		 patient = "";
		 dComments = "";
		 vReason = "";
		 procFee = "";
		 procName =  "";
		 entDate =  "";
		 appID = "";
		 doctorID = "";
		prescription = "";
		
		txtApptDate.setText("");
		
		System.out.println("CurrentDoctor: "+doctorID+" currentAppointment: "+appID);
		txtDoctorName.setText("");
		txtPatient.setText("");

		txt_ApptLength.setText("");
		txtVReason.setText("");
		comboBox_Procedures.getModel().setSelectedItem(new CustomComboBoxItem("-1",""));
		comboBox_Procedures.requestFocus();
		comboBox_Procedures.requestFocusInWindow();
		tfDocComm.setText("");
		
		System.out.println(appDate + " "+ doctor);
		
		fillTable(selectedDoctor.getID(), selectedPatient.getID());
	}
}
