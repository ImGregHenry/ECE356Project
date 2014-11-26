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


public class AssignStaffToDoctor extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<CustomComboBoxItem> comboBox_Staff;
	private JComboBox<CustomComboBoxItem> comboBox_Doctor;
	private JLabel lbl_AssignStaffToDocMessage;
	private JLabel lbl_DeleteStaffToDocAssignment; 
	private JLabel lbl_PageTitle;
	private JLabel lbl_WelcomeDoctor; 
	private JTable table_StaffToDoctorAssignments;
	private User User;
	
	private int TABLE_STAFFID_COLUMN_INDEX = 2;
	private int TABLE_DOCTORID_COLUMN_INDEX = 3;
	
	
	private String tableColumns[] = new String[] { "Staff Name", "Doctor Name", "StaffID", "AssignedToDoctorID" };
	
	/**
	 * Create the panel.
	 */
	public AssignStaffToDoctor(User _user) {
		this.User = _user;
		
		Initialize();
		lbl_WelcomeDoctor.setText("Welcome Doctor: " + this.User.DoctorFirstName + " " + this.User.DoctorLastName);
		
		LoadPage();
	}
	
	private void LoadPage()
	{
		PopulateStaffDropDown();
		PopulateDoctorDropDown();
		PopulateStaffDoctorAssignmentTable();
	}

	private void Initialize()
	{
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(101, 318, 603, 402);
		add(scrollPane);

		ImageIcon refreshImage = new ImageIcon(getClass().getResource("ref.png"));
		
		JButton btnNewButton = new JButton("", refreshImage);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoadPage();
				lbl_AssignStaffToDocMessage.setText("");
				lbl_DeleteStaffToDocAssignment.setText("");
			}
		});	
		btnNewButton.setBounds(10, 11, 35, 35);
		add(btnNewButton);
		
		DefaultTableModel model = new DefaultTableModel(tableColumns, 0);
	
		table_StaffToDoctorAssignments = new JTable(model);
		table_StaffToDoctorAssignments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_StaffToDoctorAssignments.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_StaffToDoctorAssignments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_StaffToDoctorAssignments.getColumnModel().getColumn(0).setPreferredWidth(300);
		table_StaffToDoctorAssignments.getColumnModel().getColumn(1).setPreferredWidth(300);
	
		table_StaffToDoctorAssignments.removeColumn(table_StaffToDoctorAssignments.getColumnModel().getColumn(TABLE_DOCTORID_COLUMN_INDEX));
		table_StaffToDoctorAssignments.removeColumn(table_StaffToDoctorAssignments.getColumnModel().getColumn(TABLE_STAFFID_COLUMN_INDEX));
		
		table_StaffToDoctorAssignments.setBounds(782, 495, -382, -100);
		scrollPane.setViewportView(table_StaffToDoctorAssignments);
		
		comboBox_Staff = new JComboBox<CustomComboBoxItem>();
		comboBox_Staff.setBounds(197, 140, 300, 20);
		add(comboBox_Staff);
		
		JLabel lbl_StaffName = new JLabel("Staff:");
		lbl_StaffName.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_StaffName.setBounds(141, 143, 46, 14);
		add(lbl_StaffName);
		
		JLabel lbl_Doctor = new JLabel("Doctor:");
		lbl_Doctor.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_Doctor.setBounds(141, 189, 46, 14);
		add(lbl_Doctor);
		
		comboBox_Doctor = new JComboBox<CustomComboBoxItem>();
		comboBox_Doctor.setEnabled(false);
		comboBox_Doctor.setBounds(197, 186, 300, 20);
		add(comboBox_Doctor);
		
		JButton btn_AssignStaffToDoctor = new JButton("Assign Staff to Doctor");
		btn_AssignStaffToDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomComboBoxItem selectedStaff = (CustomComboBoxItem)comboBox_Staff.getSelectedItem();
				CustomComboBoxItem selectedDoctor = (CustomComboBoxItem)comboBox_Doctor.getSelectedItem();
				
				if(dbQuery.Doctor_CanAssignStaffToDoctor(selectedDoctor.getID(), selectedStaff.getID()))
				{
					dbQuery.Doctor_AssignStaffToDoctor(selectedDoctor.getID(), selectedStaff.getID());
					lbl_AssignStaffToDocMessage.setText("Successfully assigned staff to doctor.");
					lbl_DeleteStaffToDocAssignment.setText("");
					
					PopulateStaffDoctorAssignmentTable();
				}
				else
				{
					lbl_AssignStaffToDocMessage.setText("Failed to staff staff to doctor.");
					lbl_DeleteStaffToDocAssignment.setText("");
				}
			}
		});
		btn_AssignStaffToDoctor.setBounds(539, 166, 243, 23);
		add(btn_AssignStaffToDoctor);
		
		JButton btnDeleteSelectedStaff = new JButton("Delete Selected Staff to Doctor Assignment");
		btnDeleteSelectedStaff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(table_StaffToDoctorAssignments.getSelectedRow() != -1){
					String staffID = table_StaffToDoctorAssignments.getModel().getValueAt(table_StaffToDoctorAssignments.getSelectedRow(), TABLE_STAFFID_COLUMN_INDEX).toString();
					String docID = table_StaffToDoctorAssignments.getModel().getValueAt(table_StaffToDoctorAssignments.getSelectedRow(), TABLE_DOCTORID_COLUMN_INDEX).toString();
					
					dbQuery.Doctor_DeleteStaffToDoctorAssignment(docID, staffID);
					lbl_DeleteStaffToDocAssignment.setText("Successfully deleted staff to doctor assignment.");
					lbl_AssignStaffToDocMessage.setText("");
					
					PopulateStaffDoctorAssignmentTable();
				}
				else
				{
					lbl_AssignStaffToDocMessage.setText("");
					lbl_DeleteStaffToDocAssignment.setText("Please select an assignment to delete.");	
				}
			}
		});
		btnDeleteSelectedStaff.setBounds(197, 267, 308, 23);
		add(btnDeleteSelectedStaff);
		
		lbl_AssignStaffToDocMessage = new JLabel("");
		lbl_AssignStaffToDocMessage.setFont(new Font("Calibri", Font.BOLD, 15));
		lbl_AssignStaffToDocMessage.setBounds(539, 198, 424, 30);
		add(lbl_AssignStaffToDocMessage);
		 
		lbl_DeleteStaffToDocAssignment = new JLabel("");
		lbl_DeleteStaffToDocAssignment.setFont(new Font("Calibri", Font.BOLD, 15));
		lbl_DeleteStaffToDocAssignment.setBounds(551, 260, 444, 30);
		add(lbl_DeleteStaffToDocAssignment);
		
		lbl_WelcomeDoctor = new JLabel("Welcome Doctor:");
		lbl_WelcomeDoctor.setFont(new Font("Calibri", Font.BOLD, 20));
		lbl_WelcomeDoctor.setBounds(73, 67, 540, 30);
		add(lbl_WelcomeDoctor);
		
		lbl_PageTitle = new JLabel("Assign Staff To Doctor");
		lbl_PageTitle.setFont(new Font("Calibri", Font.BOLD, 28));
		lbl_PageTitle.setBounds(281, 11, 372, 30);
		add(lbl_PageTitle);
	}
	
	private void ResetStaffDoctorAssignmentTable()
	{
		if(table_StaffToDoctorAssignments != null)
		{
			while(table_StaffToDoctorAssignments.getRowCount() > 0)
			{
			    ((DefaultTableModel) table_StaffToDoctorAssignments.getModel()).removeRow(0);
			}	
		}
	}
	
	private void PopulateStaffDoctorAssignmentTable()
	{
		ResetStaffDoctorAssignmentTable();
		
		ResultSet rs = dbQuery.Doctor_GetStaffToDoctorAssignments(this.User.DoctorID);
		
		//System.out.println("Querying for Staff to Doctor Assignments.");
		
		Object[] row = new Object[tableColumns.length];
		try {
			while(rs.next())
			{  
				row[0] = rs.getObject("StaffName");		
			    row[1] = rs.getObject("DoctorName");
			    row[2] = rs.getObject("StaffID");
			    row[3] = rs.getObject("AssignedToDoctorID");
			    ((DefaultTableModel) table_StaffToDoctorAssignments.getModel()).insertRow(rs.getRow()-1, row);
			}
			
			System.out.println("Successfully loaded staff to doctor assignment table!");
		} catch (SQLException e) {
			System.out.println("ERROR: loading staff to doctor assignment table!");
			e.printStackTrace();
		}
	}
	
	private void PopulateStaffDropDown()
	{
		try {
			comboBox_Staff.removeAllItems();
			
			ResultSet rs = dbQuery.Staff_GetAllStaffInfo();
			
			while(rs.next())
			{  
				String staffName = rs.getObject("FirstName") + " " + rs.getObject("LastName");
				comboBox_Staff.addItem(new CustomComboBoxItem(rs.getObject("StaffID").toString(), staffName));
			}
			
			System.out.println("Staff List Drop Down Loaded.");
			
		} catch (SQLException e) {
			System.out.println("ERROR: loading staff drop down list.");
			e.printStackTrace();
		}
	}
	

	private void PopulateDoctorDropDown()
	{
		comboBox_Doctor.removeAllItems();
		comboBox_Doctor.addItem(new CustomComboBoxItem(this.User.DoctorID, this.User.DoctorFirstName + " " + this.User.DoctorLastName));
	}
}
