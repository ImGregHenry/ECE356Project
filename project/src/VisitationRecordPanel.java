import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;


public class VisitationRecordPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_VisitationRecord;
	private JTable table_AppointmentRecord;
	private Object[] row ;
	private JCalendar calendar_fromAppt;
	private JCalendar calendar_toAppt;
	private JLabel lbl_toDate;
	private String userId;
	private String type;
	private String fromDate;
	private String toDate;
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	boolean populating = false;
	private JComboBox<CustomComboBoxItem> comboBox_ScheduleDoctor;
	CustomComboBoxItem selectedDoctor = new CustomComboBoxItem("-1","");
	User login;
	private JLabel lblCorrespondingVisitationRecords;
	/**
	 * Create the panel.
	 */
	public VisitationRecordPanel(User loginUser) 
	{

		login = loginUser;
		type = login.accessLevel.toString();
		if (login.accessLevel == Login.LoginAccessLevel.PATIENT)
		{
			userId = login.PatientID;
		}
		else if (login.accessLevel == Login.LoginAccessLevel.STAFF)
		{
			userId = login.StaffID;
		}
		else if (login.accessLevel == Login.LoginAccessLevel.ADMIN)
		{
			userId = login.DoctorID;
		}
		
		Initialize();
		populating = true;
		PopulateDoctorDropDown();
		
		loginUser.accessLevel.toString();
		loadAppointmentHistory("","","-1",userId,type);	
		populating = false;
		//loadPatientVisitationHistory(patientID);
		
	}
	
	public void Initialize(){
		setBounds(100, 100, 1296, 663);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		//textField.setBounds(arg0, arg1, arg2, arg3);
		String col[];
		if (login.accessLevel != Login.LoginAccessLevel.PATIENT)
			col = new String[] { "Visitation Date", "Doctor", "Patient", "Diagnosis", "Procedure Name", "Procedure Fee","Prescription","Entered Date","Doctor's Comments" };
		else 
			col = new String[] { "Visitation Date", "Doctor", "Patient", "Diagnosis", "Procedure Name", "Procedure Fee", "Prescription","Entered Date" };
		String appointmentCol[] = new String[] {"Appointment Date", "Appointment Length", "Doctor", "Patient", "DoctorId"};
		DefaultTableModel model = new DefaultTableModel(col, 0);
		DefaultTableModel appModel = new DefaultTableModel(appointmentCol, 0);
		setLayout(null);
		
		JScrollPane scrollPaneAppointment = new JScrollPane();
		scrollPaneAppointment.setBounds(37, 189, 680, 330);
		this.add(scrollPaneAppointment);
		
		comboBox_ScheduleDoctor = new JComboBox<CustomComboBoxItem>();
		comboBox_ScheduleDoctor.setBounds(453, 75, 173, 36);
		comboBox_ScheduleDoctor.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent arg0) {
	        	if (!populating)
	        	{
		        	selectedDoctor = ((CustomComboBoxItem)comboBox_ScheduleDoctor.getSelectedItem());
		        	loadAppointmentHistory(fromDate,toDate, selectedDoctor.getID(),userId,type);
	        	}
	        }
	    });
		
		this.add(comboBox_ScheduleDoctor);
		
		
		table_AppointmentRecord = new JTable(appModel)
		{
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		
		table_AppointmentRecord.getColumnModel().getColumn(0).setPreferredWidth(145);
		table_AppointmentRecord.getColumnModel().getColumn(1).setPreferredWidth(130);
		table_AppointmentRecord.getColumnModel().getColumn(2).setPreferredWidth(200);
		table_AppointmentRecord.getColumnModel().getColumn(3).setPreferredWidth(200);
		table_AppointmentRecord.getColumnModel().getColumn(4).setPreferredWidth(0);
		table_AppointmentRecord.getColumnModel().getColumn(4).setMinWidth(0);
		table_AppointmentRecord.getColumnModel().getColumn(4).setMaxWidth(0);
		table_AppointmentRecord.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_AppointmentRecord.setBounds(100, 100, 100, 100);
		
		table_AppointmentRecord.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPaneAppointment.setViewportView(table_AppointmentRecord);
		
		
		calendar_fromAppt = new JCalendar();
		calendar_fromAppt.setBounds(37, 75, 198, 103);
		add(calendar_fromAppt);

		calendar_toAppt = new JCalendar();
		calendar_toAppt.setBounds(245, 75, 198, 103);
		add(calendar_toAppt);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(800, 189, 900, 330);
		this.add(scrollPane);

		table_VisitationRecord = new JTable(model) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
			
		};
		String startDateString = "2014-01-01";
		String endDateString = "2014-12-30";
		
		try{
			Date startdate = sdfDate.parse(startDateString);
			Date endDate = sdfDate.parse(endDateString);
			calendar_fromAppt.setDate(startdate);
			calendar_toAppt.setDate(endDate);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		calendar_fromAppt.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				
				fromDate = sdfDate.format(calendar_fromAppt.getDate());
				fromDate += " 00:00:00";
				System.out.println(fromDate);
				if (!populating)
				loadAppointmentHistory(fromDate,toDate, selectedDoctor.getID(),userId,type);
				
			}
		});
		calendar_toAppt.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				
				toDate = sdfDate.format(calendar_toAppt.getDate());
				toDate += " 23:59:59";
				System.out.println(toDate);
				if (!populating)
				loadAppointmentHistory(fromDate,toDate, selectedDoctor.getID(),userId,type);
			}
		});
		
		table_AppointmentRecord.addMouseListener(new java.awt.event.MouseAdapter(){
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				int row = table_AppointmentRecord.rowAtPoint(evt.getPoint());
				String appDate =  table_AppointmentRecord.getModel().getValueAt(row, 0).toString();
				String doctor = table_AppointmentRecord.getModel().getValueAt(row, 4).toString();
				
				System.out.println(appDate + " "+ doctor);
				
				if(!populating)
				loadPatientVisitationHistory((String)appDate,Integer.parseInt(doctor));
				
			}
		});
		table_VisitationRecord.getColumnModel().getColumn(0).setPreferredWidth(130);
		table_VisitationRecord.getColumnModel().getColumn(1).setPreferredWidth(88);
		table_VisitationRecord.getColumnModel().getColumn(2).setPreferredWidth(88);
		table_VisitationRecord.getColumnModel().getColumn(3).setPreferredWidth(143);
		table_VisitationRecord.getColumnModel().getColumn(4).setPreferredWidth(100);
		table_VisitationRecord.getColumnModel().getColumn(5).setPreferredWidth(100);
		table_VisitationRecord.getColumnModel().getColumn(6).setPreferredWidth(130);
		table_VisitationRecord.getColumnModel().getColumn(7).setPreferredWidth(130);
		if (login.accessLevel != Login.LoginAccessLevel.PATIENT)
		table_VisitationRecord.getColumnModel().getColumn(8).setPreferredWidth(130);
		table_VisitationRecord.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_VisitationRecord.setBounds(300, 100, 100, 100);
		
		table_VisitationRecord.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table_VisitationRecord);
		
		JLabel lbl_fromDate = new JLabel("From");
		lbl_fromDate.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_fromDate.setBounds(37, 50, 198, 14);
		add(lbl_fromDate);
		
		lbl_toDate = new JLabel("To");
		lbl_toDate.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_toDate.setBounds(245, 50, 198, 14);
		add(lbl_toDate);
		
		JLabel lblDoctor = new JLabel("Doctor");
		lblDoctor.setBounds(494, 44, 68, 20);
		add(lblDoctor);
		
		lblCorrespondingVisitationRecords = new JLabel("Corresponding Visitation Records");
		lblCorrespondingVisitationRecords.setHorizontalAlignment(SwingConstants.CENTER);
		lblCorrespondingVisitationRecords.setBounds(800, 128, 900, 50);
		add(lblCorrespondingVisitationRecords);
		
		
	}
	private void loadAppointmentHistory(String from, String to, String doctorId,String userId, String type )
	{
		((DefaultTableModel) table_AppointmentRecord.getModel()).setRowCount(0);
		ResultSet rs = dbQuery.Visitation_getAppointments(from, to, doctorId,userId,type);
		
		
		Object[] appRow = new Object[5];
		
		try{
			while(rs.next())
			{

				appRow[0] = rs.getObject("AppointmentDate");
				appRow[1] = rs.getObject("AppointmentLength");
				appRow[2] = rs.getObject("Doctor");
				appRow[3] = rs.getObject("Patient");
				appRow[4] = rs.getObject("doctorID");
				((DefaultTableModel) table_AppointmentRecord.getModel()).insertRow(rs.getRow()-1, appRow);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	private void PopulateDoctorDropDown()
	{
		try {
			ResultSet rs;
			if (login.accessLevel == Login.LoginAccessLevel.ADMIN)
				rs = dbQuery.Staff_GetAllDoctorInfo();
			else
				rs = dbQuery.ViewVisitation_fillDoctorTable(userId, login.accessLevel.toString());	
			
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
	
	private void loadPatientVisitationHistory(String appointmentDate, int doctorId)
	{
		((DefaultTableModel) table_VisitationRecord.getModel()).setRowCount(0);
		//ResultSet rs = dbQuery.Patient_GetPatientVisitationRecord(patientID);
		ResultSet rs = dbQuery.Visitation_getVisitations(appointmentDate, doctorId);
		int columns;
		if (login.accessLevel!=Login.LoginAccessLevel.PATIENT)
			columns = 9;
		else
			columns = 8;
		
		row = new Object[columns];
		
		try {
			while(rs.next())
			{  
			    row[0] = rs.getObject("AppointmentDate");
			    row[1] = rs.getObject("Doctor");
			    row[2] = rs.getObject("Patient");
			    row[3] = rs.getObject("VisitReason");
			    row[4] = rs.getObject("ProcedureName");
			    row[5] = rs.getObject("ProcedureFee");
			    row[6] = rs.getObject("Prescription"); 
			    row[7] = rs.getObject("EnteredDate");
			    if (login.accessLevel != Login.LoginAccessLevel.PATIENT)
			    	row[8] = rs.getObject("DoctorComment");
			    
			    ((DefaultTableModel) table_VisitationRecord.getModel()).insertRow(rs.getRow()-1, row);
			}
			
			System.out.println("Completed loading visitation history!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erorr loading visitation history!");
			e.printStackTrace();
		}
	}
}
