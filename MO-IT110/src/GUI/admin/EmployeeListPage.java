package GUI.admin;

import javax.swing.table.*;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import DAO.EmployeeDAO;
import DAO.UserDAO;
import GUI.admin.LeaveRequestListPage.ButtonRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

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
	
	// CUSTOM FONT CLASS
	private static Font loadCustomFont(String fontPath, float size) {
	    try {
	        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(size);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(font);
	        return font;
	    } catch (FontFormatException | IOException e) {
	        System.err.println("Error loading font: " + e.getMessage());
	        return null;
	    }
	}

	private void initComponents() {

		// Set JFrame
		setTitle("MotorPH Payroll System | Employee List");
	    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	    setResizable(false);
	    setSize(1366, 768);
	    setLocationRelativeTo(null);
	    
	    // custom font
	    Font poppinsRegular16f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 16f);
	    Font poppinsSemiBold18f = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 18f);

	    // Top Navigation Bar with LEFT and RIGHT sections
	    JPanel navBar = new JPanel(new GridBagLayout());
	    navBar.setBackground(Color.decode("#153969"));
	    
	    ImageIcon motorphlogoAdmin = new ImageIcon("resources/images/MotorPH-Logo.png");
        JLabel motorPHLogo = new JLabel(motorphlogoAdmin);
        
        GridBagConstraints motorPHLogoGBC = new GridBagConstraints();
        motorPHLogoGBC.insets = new Insets(-20, 0, 0, 790);
        navBar.add(motorPHLogo, motorPHLogoGBC);
        
        // back to dashboard button
        JButton navBackButton = new JButton("Dashboard");
	    navBackButton.setFocusPainted(false);
	    navBackButton.setFont(poppinsRegular16f);
	    navBackButton.setForeground(Color.WHITE);
	    navBackButton.setBackground(Color.decode("#547792"));
	    navBackButton.setPreferredSize(new Dimension(120, 40));
	    navBackButton.setBorder(new LineBorder(Color.decode("#153969"),3, true));
	    navBackButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    navBackButton.addActionListener(evt -> jButton1ActionPerformed(evt));
	    navBar.add(navBackButton);
	    
	    GridBagConstraints navBackButtonGBC = new GridBagConstraints();
	    navBackButtonGBC.insets = new Insets(0,0,0,0);
        navBar.add(navBackButton, navBackButtonGBC);
        
	    // search button
	    JButton addEmployeeButton = new JButton("Add Employee");
	    addEmployeeButton.setFocusPainted(false);
	    addEmployeeButton.setFont(poppinsRegular16f);
	    addEmployeeButton.setForeground(Color.WHITE);
	    addEmployeeButton.setBackground(Color.decode("#547792"));
	    addEmployeeButton.setPreferredSize(new Dimension(160, 40));
	    addEmployeeButton.setBorder(new LineBorder(Color.decode("#153969"),3, true));
	    addEmployeeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    addEmployeeButton.addActionListener(evt -> addEmployeeButtonActionPerformed(evt));
	    navBar.add(addEmployeeButton);
	    
	    GridBagConstraints addEmployeeButtonGBC = new GridBagConstraints();
        addEmployeeButtonGBC.insets = new Insets(0,10,0,0);
        navBar.add(addEmployeeButton, addEmployeeButtonGBC);
	    

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
	    jTable1.setFont(poppinsRegular16f);
	    jTable1.setSelectionBackground(new Color(173, 216, 230));
	    jTable1.setSelectionForeground(Color.BLACK);
	    jTable1.setGridColor(new Color(230, 230, 230));
	    jTable1.setFillsViewportHeight(true);

	    JTableHeader header = jTable1.getTableHeader();
	    header.setFont(poppinsSemiBold18f);
	    header.setBackground(new Color(60, 63, 65));
	    header.setForeground(Color.WHITE);
	    header.setPreferredSize(new Dimension(100, 35));
	    header.setDefaultRenderer(new BoldHeaderRenderer(header.getDefaultRenderer()));

	    // 🔹 Column Widths
	    jTable1.getColumnModel().getColumn(0).setPreferredWidth(125); // Employee Number
	    jTable1.getColumnModel().getColumn(4).setPreferredWidth(120); 
	    jTable1.getColumnModel().getColumn(5).setPreferredWidth(120); 
	    jTable1.getColumnModel().getColumn(7).setPreferredWidth(20); // Edit
	    jTable1.getColumnModel().getColumn(8).setPreferredWidth(20); // View
	    jTable1.getColumnModel().getColumn(9).setPreferredWidth(20); // Delete


	    // buttons
	    jTable1.getColumnModel().getColumn(7).setCellRenderer(
		        new ButtonRenderer("Edit", Color.GRAY)
	    );
	    jTable1.getColumnModel().getColumn(7).setCellEditor(
	        new ButtonEditor(1, "Edit", "UpdateEmployeeDetailsPage")
	    );
	    jTable1.getColumnModel().getColumn(8).setCellRenderer(
		        new ButtonRenderer("View", Color.GRAY)  
	    );
	    jTable1.getColumnModel().getColumn(8).setCellEditor(
	        new ButtonEditor(1, "View Employee", "FullEmployeeDetailsPage")
	    );

	    jTable1.getColumnModel().getColumn(9).setCellRenderer(
	        new ButtonRenderer("Delete", new Color(0xBF3131))  
	    );
	    jTable1.getColumnModel().getColumn(9).setCellEditor(
	        new ButtonEditor(1, "Delete", "DeleteDialogPane")
	    );

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
	
	class ButtonRenderer extends JButton implements TableCellRenderer {
	    private String label;
	    private Color backgroundColor;

	    public ButtonRenderer(String label, Color backgroundColor) {
	        this.label = label;
	        this.backgroundColor = backgroundColor;
	        setOpaque(true);
	        setFont(new Font("SansSerif", Font.BOLD, 14));
	        setForeground(Color.WHITE);
	        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
	        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {

	        setText(label);
	        setBackground(isSelected ? new Color(173, 216, 230) : backgroundColor);
	        return this;
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
