package GUI.admin;

import javax.swing.table.*;

import Classes.Compensation;
import Classes.GovernmentIdentification;
import Classes.LeaveRequest;
import DAO.LeaveRequestDAO;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class LeaveRequestListPage extends JFrame {

	private JScrollPane jScrollPane1;
	private JButton goBackButton;
	private JTable jTable1;
	private int numberOfColumns = 9;
	@SuppressWarnings("unused") private JButton addEmployeeButton;
	@SuppressWarnings("unused") private JButton deleteEmployeeButton;
	private int selectedRow;
	@SuppressWarnings("unused") private String employeeNum;

	// Instantiate two of the user's important information
	GovernmentIdentification employeeGI;
	Compensation employeeComp;
	LeaveRequest leaveRequest;

	public LeaveRequestListPage(GovernmentIdentification employeeGI, Compensation employeeComp) throws ParseException {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;
		initComponents();
		loadEmployeeData();
	}

	private void initComponents() {

		// Set JFrame
		setTitle("MotorPH Payroll System | Leave Requests");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		// Instantiate Table
		jTable1 = new JTable();

		addEmployeeButton = new JButton();
		deleteEmployeeButton = new JButton();

		// Instantiate Button Component
		goBackButton = new JButton();
		goBackButton.setText("Go Back to Dashboard");
		goBackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goBackButtonActionPerformed(evt);
			}
		});

		// Create an empty default table model
		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Employee Number",
				"Last Name", "First Name", "Start Date", "End Date", "Status", "Leave Type", "", "" }) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Return the appropriate class for the last column (column with buttons)
				return (columnIndex == getColumnCount() - 1) || (columnIndex == getColumnCount() - 2) ? JButton.class
						: Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// Allow editing only for the last column
				return column == getColumnCount() - 1 || column == getColumnCount() - 2;
			}
		};

		// Modify Table Row Height
		jTable1 = new JTable(model);
		jTable1.setRowHeight(30);

		// Modify the width of the first column
		TableColumn firstColumn = jTable1.getColumnModel().getColumn(0);
		firstColumn.setMinWidth(0);
		firstColumn.setMaxWidth(0);

		// Modify the width of the second column
		TableColumn secondColumn = jTable1.getColumnModel().getColumn(1);
		secondColumn.setPreferredWidth(90); // Set your preferred width here

		// Modify the width of the last column
		TableColumn lastColumn = jTable1.getColumnModel().getColumn(numberOfColumns);
		lastColumn.setPreferredWidth(50); // Set your preferred width here

		// Modify the width of the last column
		TableColumn deleteColumn = jTable1.getColumnModel().getColumn(numberOfColumns - 1);
		deleteColumn.setPreferredWidth(100); // Set your preferred width here

		// Set a custom renderer and editor for the Edit Column
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new ButtonRenderer("Delete"));
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 1)
				.setCellEditor(new ButtonEditor(1, "Delete", "DeleteDialogPane"));

		// Set a custom renderer and editor for the View Employee column
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 2)
				.setCellRenderer(new ButtonRenderer("View Request"));
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 2)
				.setCellEditor(new ButtonEditor(1, "View Request", "LeaveRequestDetailsPage"));

		// Set custom renderer for the header cells to make them bold
		JTableHeader header = jTable1.getTableHeader();
		header.setDefaultRenderer(new BoldHeaderRenderer(header.getDefaultRenderer()));

		jScrollPane1 = new JScrollPane(jTable1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup().addComponent(goBackButton).addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap(13, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(13, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(goBackButton))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		pack();

		// Make the window appear in the middle
		setLocationRelativeTo(null);
	}

	private void loadEmployeeData() throws ParseException {
		try {
			// Get all leave requests from the database
			List<LeaveRequest> leaveRequests = LeaveRequestDAO.getAllLeaveRequests();
			
			// Get the table model
			DefaultTableModel model = (DefaultTableModel) ((JTable) jScrollPane1.getViewport().getView()).getModel();
			
			// Add data to the table
			for (LeaveRequest request : leaveRequests) {
				// Format dates
				String formattedStartDate;
				String formattedEndDate;
				
				try {
					// Try to parse and format the dates
					LocalDate startDate = LocalDate.parse(request.getStartDate().substring(0, 10));
					LocalDate endDate = LocalDate.parse(request.getEndDate().substring(0, 10));
					formattedStartDate = startDate.toString();
					formattedEndDate = endDate.toString();
				} catch (Exception e) {
					// Use the dates as they are if parsing fails
					formattedStartDate = request.getStartDate();
					formattedEndDate = request.getEndDate();
				}
				
				// Add the data to the table model
				model.addRow(new Object[] { 
					request.getId(), 
					request.getEmployeeNum(),
					request.getLastName(), 
					request.getFirstName(), 
					formattedStartDate,
					formattedEndDate, 
					request.isApproved(), 
					request.getLeaveType(), 
					"View", 
					"View" 
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, 
				"Error loading leave request data: " + e.getMessage(), 
				"Database Error", 
				JOptionPane.ERROR_MESSAGE);
		}
	}

	// Click event of Go Back to Dashboard Button
	private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				// Go back to the dashboard page
				new DashboardPage().setVisible(true);
			}
		});
	}

	private void deleteLeaveEntry(String id) {
		try {
			// Delete the leave request from the database
			if (LeaveRequestDAO.deleteLeaveRequest(id)) {
				JOptionPane.showMessageDialog(this, 
					"Leave request deleted successfully.", 
					"Success", 
					JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, 
					"Failed to delete leave request.", 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, 
				"Error deleting leave request: " + e.getMessage(), 
				"Database Error", 
				JOptionPane.ERROR_MESSAGE);
		}
	}

	// Custom on-render look for the button column
	private class ButtonRenderer extends JButton implements TableCellRenderer {
		private String buttonLabel;

		public ButtonRenderer(String buttonLabel) {
			this.buttonLabel = buttonLabel;
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText(buttonLabel);
			return this;
		}
	}

	// Custom click-event look for the button column
	private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
		private JButton button;
		private int targetColumn;
		private String buttonLabel;

		public ButtonEditor(int targetColumn, String buttonLabel, String page) {
			this.targetColumn = targetColumn;
			this.buttonLabel = buttonLabel;
			button = new JButton(this.buttonLabel);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					java.awt.EventQueue.invokeLater(new Runnable() {
						public void run() {
							if (!page.equals("DeleteDialogPane"))
								dispose();

							// Check what page to go to
							switch (page) {
							case "FullEmployeeDetailsPage":
								// Go to the employees information page
								new FullEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
								break;
							case "UpdateEmployeeDetailsPage":
								// Go to the employees information page
								new UpdateEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
								break;
							case "LeaveRequestDetailsPage":
								// Go to the employees information page
								new LeaveRequestDetailsPage(employeeGI, employeeComp, leaveRequest).setVisible(true);
								break;
							case "DeleteDialogPane":
								// Display a confirmation dialog
								int result = JOptionPane.showConfirmDialog(null, "Do you want to proceed?",
										"Confirmation", JOptionPane.YES_NO_OPTION);

								// Check the user's choice
								if (result == JOptionPane.YES_OPTION) {
									try {
										performDeleteOperation(targetColumn);
										dispose();
										navigateToLeaveRequestListPage();
									} catch (Exception e) {
										e.printStackTrace();
										JOptionPane.showMessageDialog(null, 
											"Error: " + e.getMessage(), 
											"Error", 
											JOptionPane.ERROR_MESSAGE);
									}
								}
								break;
							default:
								new FullEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
								break;
							}

						}
					});
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

			// Call constructor
			employeeGI = new GovernmentIdentification(jTable1.getValueAt(row, targetColumn).toString());
			employeeComp = new Compensation(jTable1.getValueAt(row, targetColumn).toString());
			leaveRequest = new LeaveRequest(jTable1.getValueAt(row, targetColumn).toString());

			selectedRow = row;

			// Set all the important information to be passed
			LeaveRequest.setLeaveRequestInformationObject(jTable1.getValueAt(row, targetColumn - 1).toString(),
					leaveRequest);

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return "View";
		}
	}

	// Make the column headers bold
	private static class BoldHeaderRenderer implements TableCellRenderer {

		private final TableCellRenderer defaultRenderer;

		public BoldHeaderRenderer(TableCellRenderer defaultRenderer) {
			this.defaultRenderer = defaultRenderer;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			if (c instanceof JLabel) {
				JLabel label = (JLabel) c;
				Font font = label.getFont();
				label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
			}

			return c;
		}
	}

	private void performDeleteOperation(int targetColumn) {
		deleteLeaveEntry(jTable1.getValueAt(selectedRow, targetColumn - 1).toString());
	}

	private void navigateToLeaveRequestListPage() throws ParseException {
		new LeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
	}
}
