package GUI.admin;

import javax.swing.table.*;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import DAO.EmployeeDAO;
import DAO.UserDAO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

@SuppressWarnings("serial")
public class EmployeeListPage extends JFrame {

	private JScrollPane jScrollPane1;
	private JButton jButton1;
	private JTable jTable1;
	private int numberOfColumns = 9;
	private JButton addEmployeeButton;
	@SuppressWarnings("unused") private JButton deleteEmployeeButton;
	private int selectedRow;
	private String employeeNum;

	// Instantiate two of the user's important information
	GovernmentIdentification employeeGI;
	Compensation employeeComp;

	public EmployeeListPage() {
		initComponents();
		loadEmployeeData();
	}

	private void initComponents() {

		// Set JFrame
		setTitle("MotorPH Payroll System | Employee List");
	    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	    setResizable(false);
	    setSize(1366, 768);
	    setLocationRelativeTo(null);

	    // 🔹 Top Navigation Bar with LEFT and RIGHT sections
	    JPanel navBar = new JPanel(new BorderLayout());
	    navBar.setBackground(new Color(45, 62, 80));
	    navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

	    // Left-aligned: Back to Dashboard
	    JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    leftNav.setOpaque(false);
	    JButton jButton1 = new JButton("← Back to Dashboard");
	    jButton1.setFocusPainted(false);
	    jButton1.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    jButton1.setForeground(Color.WHITE);
	    jButton1.setBackground(new Color(52, 152, 219));
	    jButton1.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
	    jButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
	    leftNav.add(jButton1);

	    // Right-aligned: Add Employee
	    JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
	    rightNav.setOpaque(false);
	    addEmployeeButton = new JButton("Add Employee");
	    addEmployeeButton.setFocusPainted(false);
	    addEmployeeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    addEmployeeButton.setBackground(new Color(52, 152, 219));
	    addEmployeeButton.setForeground(Color.WHITE);
	    addEmployeeButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
	    addEmployeeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    addEmployeeButton.addActionListener(evt -> addEmployeeButtonActionPerformed(evt));
	    rightNav.add(addEmployeeButton);

	    // Add to navbar
	    navBar.add(leftNav, BorderLayout.WEST);
	    navBar.add(rightNav, BorderLayout.EAST);

	    // 🔹 Table Model
	    DefaultTableModel model = new DefaultTableModel(new Object[][] {},
	        new String[] { "Employee Number", "Last Name", "First Name", "SSS No.", "PhilHealth No.", "TIN", "Pagibig No.", "", "", "" }) {
	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            return (columnIndex >= getColumnCount() - 3) ? JButton.class : Object.class;
	        }

	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return column >= getColumnCount() - 3;
	        }
	    };

	    // 🔹 Table Setup
	    jTable1 = new JTable(model);
	    jTable1.setRowHeight(30);
	    jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    jTable1.setSelectionBackground(new Color(173, 216, 230));
	    jTable1.setSelectionForeground(Color.BLACK);
	    jTable1.setGridColor(new Color(230, 230, 230));
	    jTable1.setFillsViewportHeight(true);

	    JTableHeader header = jTable1.getTableHeader();
	    header.setFont(new Font("Segoe UI", Font.BOLD, 15));
	    header.setBackground(new Color(60, 63, 65));
	    header.setForeground(Color.WHITE);
	    header.setPreferredSize(new Dimension(100, 35));
	    header.setDefaultRenderer(new BoldHeaderRenderer(header.getDefaultRenderer()));

	    // 🔹 Column Widths
	    jTable1.getColumnModel().getColumn(0).setPreferredWidth(90); // Employee Number
	    jTable1.getColumnModel().getColumn(7).setPreferredWidth(60); // Edit
	    jTable1.getColumnModel().getColumn(8).setPreferredWidth(120); // View
	    jTable1.getColumnModel().getColumn(9).setPreferredWidth(100); // Delete

	    // 🔹 Button Renderers & Editors
	    jTable1.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer("Edit"));
	    jTable1.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(0, "Edit", "UpdateEmployeeDetailsPage"));

	    jTable1.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer("View Employee"));
	    jTable1.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(0, "View Employee", "FullEmployeeDetailsPage"));

	    jTable1.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer("Delete"));
	    jTable1.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(0, "Delete", "DeleteDialogPane"));

	    // 🔹 Scroll Pane
	    jScrollPane1 = new JScrollPane(jTable1);
	    jScrollPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // 🔹 Layout
	    GroupLayout layout = new GroupLayout(getContentPane());
	    getContentPane().setLayout(layout);
	    layout.setHorizontalGroup(
	        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	            .addComponent(navBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	    );
	    layout.setVerticalGroup(
	        layout.createSequentialGroup()
	            .addComponent(navBar, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
	            .addGap(0)
	            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	    );

	    pack();
	    setSize(1366,768);
	    setLocationRelativeTo(null);
	}

	private void loadEmployeeData() {
		try {
			// Get all employees from the database
			List<EmployeeInformation> employees = EmployeeDAO.getAllEmployees();
			
			// Get the table model
			DefaultTableModel model = (DefaultTableModel) ((JTable) jScrollPane1.getViewport().getView()).getModel();
			
			// Auto increment employeeNum for record creation
			if (!employees.isEmpty()) {
				int maxEmployeeNum = 0;
				for (EmployeeInformation emp : employees) {
					int currentEmpNum = Integer.parseInt(emp.getEmployeeNumber());
					if (currentEmpNum > maxEmployeeNum) {
						maxEmployeeNum = currentEmpNum;
					}
				}
				employeeNum = String.valueOf(maxEmployeeNum + 1);
			} else {
				employeeNum = "10001"; // Start with 10001 if no employees exist
			}
			
			// Add data to the table
			for (EmployeeInformation employee : employees) {
				GovernmentIdentification govId = EmployeeDAO.getEmployeeGovId(employee.getEmployeeNumber());
				
				// Add the data to the table model
				model.addRow(new Object[] { 
					employee.getEmployeeNumber(), 
					employee.getLastName(),
					employee.getFirstName(), 
					govId.getSSSNumber(), 
					govId.getPhilHealthNumber(),
					govId.getTinNumber(), 
					govId.getPagibigNumber(), 
					"View" 
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, 
				"Error loading employee data: " + e.getMessage(), 
				"Database Error", 
				JOptionPane.ERROR_MESSAGE);
		}
	}

	// Click event of Go Back to Dashboard Button
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				// Go back to the dashboard page
				new DashboardPage().setVisible(true);
			}
		});
	}

	private void addEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Close the employee list
				dispose();

				// Refresh the Employees Page
				new AddEmployeeDetailsPage(employeeNum).setVisible(true);
			}
		});
	}

	private void deleteEmployeeButtonActionPerformed(String employeeNumToRemove) {
		try {
			// Delete the employee from the database using EmployeeDAO
			if (EmployeeDAO.deleteEmployee(employeeNumToRemove)) {
				JOptionPane.showMessageDialog(this, 
					"Employee deleted successfully.", 
					"Success", 
					JOptionPane.INFORMATION_MESSAGE);
					
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						// Remove the EmployeesPage Window
						dispose();

						// Refresh the Employees Page
						new EmployeeListPage().setVisible(true);
					}
				});
			} else {
				JOptionPane.showMessageDialog(this, 
					"Failed to delete employee.", 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, 
				"Error deleting employee: " + e.getMessage(), 
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
							case "DeleteDialogPane":
								// Display a confirmation dialog
								int result = JOptionPane.showConfirmDialog(null, "Do you want to proceed?",
										"Confirmation", JOptionPane.YES_NO_OPTION);

								// Check the user's choice
								if (result == JOptionPane.YES_OPTION) {
									deleteEmployeeButtonActionPerformed(
										jTable1.getValueAt(selectedRow, targetColumn).toString());
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

			selectedRow = row;

			// Set all the important information to be passed
			EmployeeInformation.setEmployeeInformationObject(jTable1.getValueAt(row, targetColumn).toString(),
					employeeGI, employeeComp);

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
}
