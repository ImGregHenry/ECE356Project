import java.awt.BorderLayout;
import java.awt.Color;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;


public class VisitationRecordPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_VisitationRecord;
	
	/**
	 * Create the panel.
	 */
	public VisitationRecordPanel(int patientID) 
	{
		Initialize();
		loadPatientVisitationHistory(patientID);
	}
	
	public void Initialize(){
		setBounds(100, 100, 742, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		String col[] = new String[] { "Visitation Date", "Length of Visit", "Doctor", "Procedure Name", "Procedure Fee" };
		DefaultTableModel model = new DefaultTableModel(col, 0);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(386, 72, 743, 427);
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
		table_VisitationRecord.getColumnModel().getColumn(0).setPreferredWidth(145);
		table_VisitationRecord.getColumnModel().getColumn(1).setPreferredWidth(88);
		table_VisitationRecord.getColumnModel().getColumn(2).setPreferredWidth(176);
		table_VisitationRecord.getColumnModel().getColumn(3).setPreferredWidth(143);
		table_VisitationRecord.getColumnModel().getColumn(4).setPreferredWidth(87);
		table_VisitationRecord.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_VisitationRecord.setBounds(782, 495, -382, -100);
		table_VisitationRecord.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table_VisitationRecord);
		
	}
	
	private void loadPatientVisitationHistory(int patientID)
	{
		ResultSet rs = dbQuery.Patient_GetPatientVisitationRecord(patientID);
		
		int columns = 5;
		
		Object[] row = new Object[columns];
		try {
			while(rs.next())
			{  
			    row[0] = rs.getObject("AppointmentDate");
			    row[1] = rs.getObject("AppointmentLength");
			    row[2] = rs.getObject("DoctorName");
			    row[3] = rs.getObject("ProcedureName");
			    row[4] = rs.getObject("ProcedureFee");
			    
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
