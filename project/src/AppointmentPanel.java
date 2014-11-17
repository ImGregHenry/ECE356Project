
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JSpinner;
import java.awt.Font;
import javax.swing.JButton;

import com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter;
import com.toedter.calendar.JCalendar;
import javax.swing.ListSelectionModel;



public class AppointmentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the panel.
	 */
	


	private JTable table_Appointments;
	private JComboBox<CustomComboBoxItem> comboBox_ScheduleDoctor;
	private JComboBox<CustomComboBoxItem> comboBox_ApptTableDoctorSelect;
	private JComboBox<CustomComboBoxItem> comboBox_SchedulePatient;
	private JButton btnCancelAppointment; 
	private JLabel lbl_BookApptMessage;
	private JCalendar calendar_Appt;
	private boolean isDoctorComboBoxLoaded = false;
	private String staffID;


	/**
	 * Create the application.
	 */
	public AppointmentPanel(String _staffID) {
		this.staffID = _staffID;
		initialize();

		
		dbQuery.StartDBConnection();
		
		PopulatePatientDropDown();
		PopulateDoctorDropDown();
		isDoctorComboBoxLoaded = true;	//NOTE: keep this flag set after PopulateDoctorDropDown() method, IMPORTANT!
		PopulateAppointmentTable();
		
		//TODO: only load doctors that staff can schedule appointments for.
		System.out.println("APPOINTMENT PANEL LOADED.  StaffID: " + this.staffID);
	}
	
	private void PopulatePatientDropDown()
	{
		try {
			ResultSet rs = dbQuery.Staff_GetAllPatientInfo();
			
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
			
			ResultSet rs = dbQuery.Staff_GetAllDoctorInfo();
			
			comboBox_ApptTableDoctorSelect.addItem(new CustomComboBoxItem("ALL", "ALL"));
			
			while(rs.next())
			{  
				String doctorName = rs.getObject("FirstName") + " " + rs.getObject("LastName"); 
				comboBox_ScheduleDoctor.addItem(new CustomComboBoxItem(rs.getObject("DoctorID").toString(), doctorName));
				comboBox_ApptTableDoctorSelect.addItem(new CustomComboBoxItem(rs.getObject("DoctorID").toString(), doctorName));
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
	
	private void ResetAppointmentTable()
	{
		if(table_Appointments != null)
		{
			while(table_Appointments.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_Appointments.getModel()).removeRow(0);
			}	
		}
	}
	
	private void PopulateAppointmentTable()
	{
		ResetAppointmentTable();
		
		CustomComboBoxItem selectedDoctor = (CustomComboBoxItem)comboBox_ApptTableDoctorSelect.getSelectedItem();
		ResultSet rs = dbQuery.Staff_GetFutureAppointmentInformation(selectedDoctor.getID());
		
		
		System.out.println("Querying doctor appointment schedule for doctorID: " + selectedDoctor.getID() + "!");
		
		Object[] row = new Object[TABLE_COLUMN_COUNT];
		try {
			while(rs.next())
			{  
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd YYYY");
				SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
				
			    row[0] = dateFormat.format(rs.getObject("AppointmentDate"));		// Date
			    row[1] = timeFormat.format(rs.getObject("AppointmentDate")); 	// Time
			    row[2] = rs.getObject("AppointmentLength");		// Length
			    row[3] = rs.getObject("DoctorName");	// Doctor Name
			    row[4] = rs.getObject("PatientName");	// Patient Name
			    row[5] = rs.getObject("StaffName");	// Staff Scheduler
			    row[6] = rs.getObject("AppointmentID");	// Doctor ID
			    row[7] = rs.getObject("CanDelete");	// Doctor ID
			    ((DefaultTableModel) table_Appointments.getModel()).insertRow(rs.getRow()-1, row);
			}
			
			System.out.println("Successfully loaded doctor appointment table!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: loading doctor appointment table!");
			e.printStackTrace();
		}
	}
	
	
	private int TABLE_APPOINTMENT_ID_COLUMN_INDEX = 6;
	private int TABLE_CAN_DELETE_COLUMN_INDEX = 7;
	private String tableColumns[] = new String[] { "Date", "Time", "Length", "Doctor Name", "Patient Name", "Staff Scheduler", "AppointmentID", "CanDelete" };
	private int TABLE_COLUMN_COUNT = 8;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		this.setFont(new Font("Calibri", Font.PLAIN, 24));
		this.setBounds(50, 100, 1066, 748);
		
		this.setLayout(null);
		
		
		DefaultTableModel model = new DefaultTableModel(tableColumns, 0);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 72, 900, 402);
		this.add(scrollPane);
		
		table_Appointments = new JTable(model) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table_Appointments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_Appointments.getColumnModel().getColumn(0).setPreferredWidth(110);
		table_Appointments.getColumnModel().getColumn(1).setPreferredWidth(88);
		table_Appointments.getColumnModel().getColumn(2).setPreferredWidth(80);
		table_Appointments.getColumnModel().getColumn(3).setPreferredWidth(200);
		table_Appointments.getColumnModel().getColumn(4).setPreferredWidth(200);
		table_Appointments.getColumnModel().getColumn(5).setPreferredWidth(200);
		
//		table_Appointments.getColumnModel().getColumn(6).setPreferredWidth(0);
//		table_Appointments.getColumnModel().getColumn(6).setMaxWidth(0);
//		table_Appointments.getColumnModel().getColumn(6).setMinWidth(0);
		table_Appointments.removeColumn(table_Appointments.getColumnModel().getColumn(TABLE_CAN_DELETE_COLUMN_INDEX));
		table_Appointments.removeColumn(table_Appointments.getColumnModel().getColumn(TABLE_APPOINTMENT_ID_COLUMN_INDEX));
		
		table_Appointments.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				// Prevent this event from firing twice
				// Disable the cancel button if this appointment can't be cancelled
				if (!e.getValueIsAdjusting() 
						&& table_Appointments.getSelectedRow() != -1) {
					if(table_Appointments.getModel().getValueAt(table_Appointments.getSelectedRow(), TABLE_CAN_DELETE_COLUMN_INDEX).toString().equals("true"))
						btnCancelAppointment.setEnabled(true);
					else
						btnCancelAppointment.setEnabled(false);
				}
			}
		});
		table_Appointments.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_Appointments.setBounds(782, 495, -382, -100);
		table_Appointments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//frame.getContentPane().add(table_VisitationRecord);
		scrollPane.setViewportView(table_Appointments);
		
		calendar_Appt = new JCalendar();
		calendar_Appt.setBounds(244, 543, 198, 153);
		this.add(calendar_Appt);
		
		lbl_BookApptMessage = new JLabel("");
		lbl_BookApptMessage.setFont(new Font("Calibri", Font.BOLD, 15));
		lbl_BookApptMessage.setBounds(752, 666, 304, 30);
		this.add(lbl_BookApptMessage);
		
		JLabel lblPatient = new JLabel("Patient");
		lblPatient.setFont(new Font("Calibri", Font.BOLD, 20));
		lblPatient.setBounds(10, 617, 119, 20);
		this.add(lblPatient);
		
		comboBox_SchedulePatient = new JComboBox<CustomComboBoxItem>();
		comboBox_SchedulePatient.setBounds(10, 644, 173, 36);
		this.add(comboBox_SchedulePatient);
		
		
		JLabel lblDoctorsSchedule = new JLabel("Doctor's Schedule:");
		lblDoctorsSchedule.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDoctorsSchedule.setBounds(116, 23, 133, 14);
		this.add(lblDoctorsSchedule);
		
		comboBox_ApptTableDoctorSelect = new JComboBox<CustomComboBoxItem>();
		comboBox_ApptTableDoctorSelect.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent arg0) {
	            // Prevent this event from firing until the combo box is fully populated (each item added fires this event)
	        	if(arg0.getStateChange() == ItemEvent.SELECTED
	            		&& isDoctorComboBoxLoaded)
	            {
		        	PopulateAppointmentTable();
	            }
	        }
	    });
		comboBox_ApptTableDoctorSelect.setBounds(280, 20, 133, 20);
		this.add(comboBox_ApptTableDoctorSelect);
		
		
		final JSpinner spinner_Time = new JSpinner( new SpinnerDateModel() );
		spinner_Time.setFont(new Font("Calibri", Font.PLAIN, 24));
		JSpinner.DateEditor de_spinner_Time = new JSpinner.DateEditor(spinner_Time, "HH:mm");
		spinner_Time.setBounds(487, 619, 92, 36);
		spinner_Time.setEditor(de_spinner_Time);
		spinner_Time.setValue(new Date()); // will only show the current time
		this.add(spinner_Time);
		
		JLabel lblHhmm = new JLabel("HH:mm");
		lblHhmm.setBounds(507, 666, 70, 14);
		this.add(lblHhmm);
		
		JLabel lblNewLabel = new JLabel("Time");
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 20));
		lblNewLabel.setBounds(487, 588, 49, 20);
		this.add(lblNewLabel);
		
		final JLabel lblscheduleNewAppointment = new JLabel("--Schedule New Appointment --");
		lblscheduleNewAppointment.setFont(new Font("Calibri", Font.BOLD, 24));
		lblscheduleNewAppointment.setBounds(305, 496, 331, 36);
		this.add(lblscheduleNewAppointment);
		
		JLabel lblLength = new JLabel("Length");
		lblLength.setFont(new Font("Calibri", Font.BOLD, 20));
		lblLength.setBounds(648, 588, 70, 20);
		this.add(lblLength);
		
		final JSpinner spinner_Length = new JSpinner( new SpinnerNumberModel(30, 0, 600, 5) );
		JSpinner.NumberEditor de_spinner_Length = new JSpinner.NumberEditor(spinner_Length);
		spinner_Length.setFont(new Font("Calibri", Font.PLAIN, 24));
		spinner_Length.setBounds(648, 619, 92, 36);
		spinner_Length.setEditor(de_spinner_Length);
		this.add(spinner_Length);
		

		final JLabel lblCancelApptMsg = new JLabel("");
		lblCancelApptMsg.setFont(new Font("Calibri", Font.BOLD, 14));
		lblCancelApptMsg.setBounds(688, 23, 180, 14);
		add(lblCancelApptMsg);
		
		JLabel lblmins = new JLabel("(mins)");
		lblmins.setBounds(671, 666, 70, 14);
		this.add(lblmins);
		
		JButton btnNewButton = new JButton("Book Appointment");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SimpleDateFormat sdfFullDateTime = new SimpleDateFormat("MMM:dd:yyyy HH:mm:ss");
				
				SimpleDateFormat sdfDate = new SimpleDateFormat("MMM:dd:yyyy");
				SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
				Date startDate;
				Date endDate;
				
				CustomComboBoxItem selectedPatient = (CustomComboBoxItem)comboBox_SchedulePatient.getSelectedItem();
				CustomComboBoxItem selectedDoctor = (CustomComboBoxItem)comboBox_ScheduleDoctor.getSelectedItem();
				System.out.println("SelectedPatientID: " + selectedPatient.getID() + " Patient Name: " + selectedPatient.getName());
				System.out.println("SelectedDoctorID: " + selectedDoctor.getID() + " Doctor Name: " + selectedDoctor.getName());
				
				String startDateString = sdfDate.format(calendar_Appt.getDate());
				String startTimeString = sdfTime.format(spinner_Time.getValue()) + ":00";
				int apptLength = (int) spinner_Length.getValue();
					
				try {
					// Calculate start date of appt by merging the date and time
					String combinedDateTimeString = startDateString + " " + startTimeString; 
					startDate = sdfFullDateTime.parse(combinedDateTimeString);
					
					System.out.println("CombinedDateTime: " + sdfFullDateTime.format(startDate));
					
					// Calculate end date of appt (based on length of appt)
					final long ONE_MINUTE_IN_MS=60000; 

					long time = startDate.getTime();
					endDate = new Date(time + (apptLength * ONE_MINUTE_IN_MS));
					//System.out.println("EndDateTime: " + sdfFullDateTime.format(endDate));
					
					
					// Test if appt is valid (in database)
					boolean isApptAvailable = dbQuery.Staff_IsAppointmentAvailabile(selectedDoctor.getID(), selectedPatient.getID(), startDate, endDate);
					
					if(isApptAvailable)
					{
						System.out.println("Appointment is available!  Attempting to schedule.");
						
						
						dbQuery.Staff_ScheduleDoctorAppointment(CreateTimeStringForApptLength(apptLength), staffID, selectedPatient.getID(), selectedDoctor.getID(), dbQuery.dbDateFormat.format(startDate));
						
						// Reload the appointment table
						PopulateAppointmentTable();
						
						lbl_BookApptMessage.setText("Successfully booked appointment.");
					}
					else
					{
						System.out.println("Failed to book appointment.  Try another time.");
						lbl_BookApptMessage.setText("Failed to book appointment.  Invalid time.");
					}
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnNewButton.setFont(new Font("Calibri", Font.BOLD, 16));
		btnNewButton.setBounds(761, 619, 189, 36);
		this.add(btnNewButton);
		
		comboBox_ScheduleDoctor = new JComboBox<CustomComboBoxItem>();
		comboBox_ScheduleDoctor.setBounds(10, 543, 173, 36);
		this.add(comboBox_ScheduleDoctor);
		
		JLabel lblDoctor = new JLabel("Doctor");
		lblDoctor.setFont(new Font("Calibri", Font.BOLD, 20));
		lblDoctor.setBounds(10, 516, 119, 20);
		this.add(lblDoctor);
		
		btnCancelAppointment = new JButton("Cancel Appointment");
		btnCancelAppointment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("CANDELETE: " + table_Appointments.getModel().getValueAt(table_Appointments.getSelectedRow(), TABLE_CAN_DELETE_COLUMN_INDEX).toString().equals("true"));
				//TODO: test whether can-delete column works.
				if(table_Appointments.getSelectedRow() != -1
						&& table_Appointments.getModel().getValueAt(table_Appointments.getSelectedRow(), TABLE_CAN_DELETE_COLUMN_INDEX).toString().equals("true"))
				{
					Object appointmentID = table_Appointments.getModel().getValueAt(table_Appointments.getSelectedRow(), TABLE_APPOINTMENT_ID_COLUMN_INDEX);
	
					System.out.println("DELETING appoint for PatientID: " + appointmentID + " Row: " + table_Appointments.getSelectedRow() + " Col: " + TABLE_APPOINTMENT_ID_COLUMN_INDEX);
					dbQuery.Staff_DeleteScheduledAppointment(appointmentID.toString());
					//TODO: handle deleting appointments when they have a visitation record (don't delete)
					
					lblscheduleNewAppointment.setText("Successfully deleted appointment.");
					
					PopulateAppointmentTable();
				}
				else
				{
					lblscheduleNewAppointment.setText("Error: Unable to delete this appointment.");
					//TODO: error message, cannot delete because visitation record exists for this appointment
				}
			}
		});
		btnCancelAppointment.setBounds(505, 19, 173, 23);
		this.add(btnCancelAppointment);
		
	}
	
	
	
	private String CreateTimeStringForApptLength(int mins)
	{
		String s = "";
		Date d = new Date("04/04/2014 00:00:00");
		d = new Date(d.getTime() + (mins * 60000));
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		s = sdf.format(d);
		
		return s;
	}
}


