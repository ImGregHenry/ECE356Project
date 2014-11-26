import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;







import com.toedter.calendar.JCalendar;

import javax.swing.JLabel;


public class FinanceTab extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User User;
	private JComboBox<CustomComboBoxItem> comboBox_Patient;
	private JComboBox<CustomComboBoxItem> comboBox_Doctor;
	private JComboBox<CustomComboBoxItem> comboBox_Procedure;
	
	private JComboBox<FinanceTimeSpans> comboBox_TimeSpan;
	private JCalendar calendar_Appt;
	private JTable table_FinanceData;
	private JTable table_FinanceDetails;
	private JButton btn_Go;

	public enum FinanceTimeSpans
	{
		ONEMONTH,
		TWOMONTHS,
		THREEMONTHS,
		SIXMONTH,
		ONEDAY,
		ONEWEEK,
		TWOWEEK,
		ONEYEAR
	}
	private String tableFinanceDataColumns[] = new String[] { "Date", "Length", "Doctor Name", "Patient Name", "Prescription", "Procedure Name", "Procedure Fee" };
	private String tableFinanceDetailRows[] = new String[] { "Doctor Name", "Patient Name", "Procedure Name", "Number of Visits", "Fee Per Visit", "Total Visitation Income", "Total Procedure Income", "Total Income" };
	private String tableFinanceDetailColumns[] = new String[] { "", "" };
	private int FINANCE_MAIN_TABLE_COLUMN_COUNT = 7;
	private int FINANCE_DETAILS_TABLE_COLUMN_COUNT = 2;
	private JLabel lblNewLabel;
	
	public FinanceTab(User _user){
		
		this.User = _user;
		Initialize();
		
		PopulatePatientDropDown();
		PopulateDoctorDropDown();
		PopulateTimeSpanDropDown();
		PopulateProcedureDropDown();
	}
	
	private void Initialize(){
		
		setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(379, 6, 4, 22);
		add(textArea);
		
		btn_Go = new JButton("Go");
		btn_Go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ParseDataToPopulateTable();
			}
		});
		btn_Go.setBounds(500, 306, 89, 23);
		this.add(btn_Go);
		
		comboBox_Patient = new JComboBox<CustomComboBoxItem>();
		comboBox_Patient.setBounds(10, 175, 173, 36);
		this.add(comboBox_Patient);
		
		comboBox_Doctor = new JComboBox<CustomComboBoxItem>();
		comboBox_Doctor.setBounds(10, 100, 173, 36);
		this.add(comboBox_Doctor);
		
		comboBox_Procedure = new JComboBox<CustomComboBoxItem>();
		comboBox_Procedure.setBounds(10, 250, 173, 36);
		this.add(comboBox_Procedure);
		
		calendar_Appt = new JCalendar();
		calendar_Appt.setBounds(300, 100, 198, 153);
		this.add(calendar_Appt);
		
		DefaultTableModel model = new DefaultTableModel(tableFinanceDataColumns, 0);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 400, 1160, 402);
		this.add(scrollPane);
		
		table_FinanceData = new JTable(model) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table_FinanceData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_FinanceData.getColumnModel().getColumn(0).setPreferredWidth(130);
		table_FinanceData.getColumnModel().getColumn(1).setPreferredWidth(95);
		table_FinanceData.getColumnModel().getColumn(2).setPreferredWidth(200);
		table_FinanceData.getColumnModel().getColumn(3).setPreferredWidth(200);
		table_FinanceData.getColumnModel().getColumn(4).setPreferredWidth(200);
		table_FinanceData.getColumnModel().getColumn(5).setPreferredWidth(200);
		table_FinanceData.getColumnModel().getColumn(6).setPreferredWidth(100);

		table_FinanceData.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_FinanceData.setBounds(782, 495, -382, -100);
		table_FinanceData.setFont(new Font("Calibri", Font.PLAIN, 20));
		table_FinanceData.setRowHeight(35);
		table_FinanceData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table_FinanceData);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(700, 50, 555, 302);
		this.add(scrollPane2);
		
		DefaultTableModel model2 = new DefaultTableModel(tableFinanceDetailColumns, 0);
		table_FinanceDetails = new JTable(model2) 
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table_FinanceDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_FinanceDetails.getColumnModel().getColumn(0).setPreferredWidth(350);
		table_FinanceDetails.getColumnModel().getColumn(1).setPreferredWidth(200);
		table_FinanceDetails.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_FinanceDetails.setBounds(782, 495, -382, -100);
		table_FinanceDetails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_FinanceDetails.setFont(new Font("Calibri", Font.BOLD, 26));
		table_FinanceDetails.setRowHeight(35);
		scrollPane2.setViewportView(table_FinanceDetails);
		
		JLabel lblPatient = new JLabel("Patient");
		lblPatient.setBounds(10, 150, 56, 16);
		add(lblPatient);

		JLabel lblProcedure = new JLabel("Procedure");
		lblProcedure.setBounds(10, 225, 100, 16);
		add(lblProcedure);

		JLabel lblDoctor = new JLabel("Doctor");
		lblDoctor.setBounds(10, 75, 56, 16);
		add(lblDoctor);
		
		
		comboBox_TimeSpan = new JComboBox<FinanceTimeSpans>();
		comboBox_TimeSpan.setBounds(300, 306, 173, 36);
		add(comboBox_TimeSpan);
		
		JLabel lblTimeSpan = new JLabel("Time Span");
		lblTimeSpan.setBounds(300, 277, 113, 16);
		add(lblTimeSpan);
		
		lblNewLabel = new JLabel("Financial Information");
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 26));
		lblNewLabel.setBounds(50, 28, 378, 36);
		add(lblNewLabel);
	}
	

	private void PopulateTimeSpanDropDown()
	{
		 for (FinanceTimeSpans d : FinanceTimeSpans.values()) {
			comboBox_TimeSpan.addItem(d);
		 }
	}
	
	private void PopulatePatientDropDown()
	{
		try {
			ResultSet rs = dbQuery.Staff_GetAllPatientInfo();
			comboBox_Patient.addItem(new CustomComboBoxItem("ALL", "ALL"));
			while(rs.next())
			{  
				String patientName = rs.getObject("FirstName") + " " + rs.getObject("LastName");
				comboBox_Patient.addItem(new CustomComboBoxItem(rs.getObject("PatientID").toString(), patientName));
			}
			
			System.out.println("Patient Appointment Scheduler List Loaded.");
			
		} catch (SQLException e) {
			System.out.println("ERROR: loading patient appointment filter list.");
			e.printStackTrace();
		}
	}
	
	private Date GetDateAfterTimeSpan(Date date, FinanceTimeSpans timeSpan)
	{
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
		if(timeSpan == FinanceTimeSpans.ONEDAY)
			cal.add(Calendar.DATE, 1); 
		else if(timeSpan == FinanceTimeSpans.ONEMONTH)
			cal.add(Calendar.MONTH, 1);
		else if(timeSpan == FinanceTimeSpans.TWOMONTHS)
			cal.add(Calendar.MONTH, 2);
		else if(timeSpan == FinanceTimeSpans.THREEMONTHS)
			cal.add(Calendar.MONTH, 3);
		else if(timeSpan == FinanceTimeSpans.SIXMONTH)
			cal.add(Calendar.MONTH, 6);
		else if(timeSpan == FinanceTimeSpans.ONEYEAR)
			cal.add(Calendar.YEAR, 1);
		else if(timeSpan == FinanceTimeSpans.ONEWEEK)
			cal.add(Calendar.DATE, 7);
		else if(timeSpan == FinanceTimeSpans.TWOWEEK)
			cal.add(Calendar.DATE, 14);
		
        return cal.getTime();
	}
	
	private void PopulateDoctorDropDown()
	{
		try {
			
			ResultSet rs = dbQuery.Staff_GetAllDoctorInfo();
		
			comboBox_Doctor.addItem(new CustomComboBoxItem("ALL" , "ALL"));
			
			while(rs.next())
			{  
				String doctorName = rs.getObject("FirstName") + " " + rs.getObject("LastName"); 
				comboBox_Doctor.addItem(new CustomComboBoxItem(rs.getObject("DoctorID").toString(), doctorName));
			}
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erorr loading doctor finance filter list.");
			e.printStackTrace();
		}
	}
	
	private void PopulateProcedureDropDown()
	{
		try {
			
			ResultSet rs = dbQuery.Finance_GetAllProcedures();
		
			comboBox_Procedure.addItem(new CustomComboBoxItem("ALL" , "ALL"));
			
			while(rs.next())
			{  
				comboBox_Procedure.addItem(new CustomComboBoxItem(rs.getObject("ProcedureName").toString(), rs.getObject("ProcedureName").toString()));
			}
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erorr loading doctor finance filter list.");
			e.printStackTrace();
		}
	}
	
	
	private void PopulateFinanceTables(String docID, String patID, String procName, Date startDate, Date endDate)
	{
		ResetFinanceTables();
		
		
		ResultSet rs = dbQuery.Finance_GetDoctorFinanceInfo(docID, patID, procName, startDate, endDate);
		

		String docName = ((CustomComboBoxItem)comboBox_Doctor.getSelectedItem()).getName();
		String patName = "";
		int numVisits = 0;
		int feePerVisit = 100;
		long totalVisitationIncome = 0;
		long totalProcedureIncome = 0;
		long totalIncome = 0;
		//System.out.println("Querying doctor appointment schedule for doctorID: " + selectedDoctor.getID() + "!");
		
		Object[] row = new Object[FINANCE_MAIN_TABLE_COLUMN_COUNT];
		try {
			while(rs.next())
			{  
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd YYYY");
				
				//"Appointment Date", "Appointment Length", "Patient Name","Prescription", "Procedure Name", "Procedure Fee" };
			    
			    row[0] = dateFormat.format(rs.getObject("AppointmentDate"));
			    row[1] = rs.getObject("AppointmentLength");		
			    row[2] = rs.getObject("DoctorFirstName") + " " + rs.getObject("DoctorLastName");
			    row[3] = rs.getObject("PatientFirstName") + " " + rs.getObject("PatientLastName") ;	
			    row[4] = rs.getObject("Prescription");	
			    row[5] = rs.getObject("ProcedureName");	
			    row[6] = rs.getObject("ProcedureFee");	
			    ((DefaultTableModel) table_FinanceData.getModel()).insertRow(rs.getRow()-1, row);
			    
			    docName = rs.getObject("DoctorFirstName") + " " + rs.getObject("DoctorLastName") ;	
			    patName = rs.getObject("PatientFirstName") + " " + rs.getObject("PatientLastName");
			    numVisits++;
			    totalProcedureIncome += rs.getInt("ProcedureFee");
			}
			totalVisitationIncome = numVisits * feePerVisit;
			totalIncome = totalVisitationIncome + totalProcedureIncome;
			
		    int rowCount = 0;
			for (int x = 0; x < tableFinanceDetailRows.length; x++) 
		    {
				Object[] row2 = new Object[FINANCE_DETAILS_TABLE_COLUMN_COUNT];
				row2[0] = tableFinanceDetailRows[x];
				
				if(docID == "ALL") docName = "ALL";
				if(patID == "ALL") patName = "ALL";
				if(procName == "ALL") procName = "ALL";
				
				if(tableFinanceDetailRows[x] == "Doctor Name")
				{
					row2[1] = " " + docName;
				}
				else if(tableFinanceDetailRows[x] == "Procedure Name")
				{
					row2[1] = " " + procName;
				}
				else if(tableFinanceDetailRows[x] == "Patient Name")
				{
					row2[1] = " " + patName;
				}
				else if(tableFinanceDetailRows[x] == "Number of Visits")
					row2[1] = " " + numVisits;
				else if(tableFinanceDetailRows[x] == "Fee Per Visit")
					row2[1] = " $" + 100;
				else if(tableFinanceDetailRows[x] == "Total Visitation Income")
					row2[1] = " $" + totalVisitationIncome;
				else if(tableFinanceDetailRows[x] == "Total Procedure Income")
					row2[1] = " $" + totalProcedureIncome;
				else if(tableFinanceDetailRows[x] == "Total Income")
					row2[1] = " $" + totalIncome;
				
		    	((DefaultTableModel) table_FinanceDetails.getModel()).insertRow(rowCount++, row2);
			
		    }
			
			//System.out.println("Successfully loaded doctor appointment table!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: loading Finance Doctor table!");
			e.printStackTrace();
		}
	}
	
	private void ResetFinanceTables()
	{
		if(table_FinanceData != null)
		{
			while(table_FinanceData.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_FinanceData.getModel()).removeRow(0);
			}	
		}
		
		if(table_FinanceDetails != null)
		{
			while(table_FinanceDetails.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_FinanceDetails.getModel()).removeRow(0);
			}	
		}	
	}
	
	private void ParseDataToPopulateTable()
	{

		CustomComboBoxItem selectedDoctor = (CustomComboBoxItem)comboBox_Doctor.getSelectedItem();
		CustomComboBoxItem selectedPatient = (CustomComboBoxItem)comboBox_Patient.getSelectedItem();
		CustomComboBoxItem selectedProcedure = (CustomComboBoxItem)comboBox_Procedure.getSelectedItem();

		SimpleDateFormat sdfFullDateTime = new SimpleDateFormat("MMM:dd:yyyy HH:mm:ss");
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMM:dd:yyyy");
		
		Date startDate;
		Date endDate;
		
		String startDateString = sdfDate.format(calendar_Appt.getDate());
		String startTimeString = "00:00:00";
			
		// Calculate start date of appt by merging the date and time
		String combinedDateTimeString = startDateString + " " + startTimeString; 
		try {
			startDate = sdfFullDateTime.parse(combinedDateTimeString);
		
			FinanceTimeSpans span = (FinanceTimeSpans)comboBox_TimeSpan.getSelectedItem();
			
			endDate = GetDateAfterTimeSpan(startDate, span);
			
			
			PopulateFinanceTables(selectedDoctor.getID(), selectedPatient.getID(), selectedProcedure.getID(), startDate, endDate); 
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
