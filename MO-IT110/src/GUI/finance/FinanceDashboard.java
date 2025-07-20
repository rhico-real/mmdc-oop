package GUI.finance;

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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import DAO.EmployeeDAO;
import DAO.UserDAO;
import GUI.LoginPage;

@SuppressWarnings("serial")
public class FinanceDashboard extends JFrame {
    
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, clearButton, refreshButton, addEmployeeButton, logoutButton;
    private JLabel statusLabel;
    
    public FinanceDashboard() {
        initComponents();
        loadEmployeeData();
    }
    
    private void initComponents() {
    	setTitle("MotorPH Payroll System | Finance Dashboard");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(1366, 768);
    	setLocationRelativeTo(null);
    	
    	// custom colors
    	String navyBlue = "#153969";

    	// Main panel
    	JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    	mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

    	// Header panel (Navbar)
    	JPanel headerPanel = new JPanel(new GridBagLayout());
    	headerPanel.setBackground(Color.decode(navyBlue));
    	headerPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
    	
    	// title label
    	JLabel titleLabel = new JLabel("HR Department - Employee Management");
    	titleLabel.setFont(FontLoader.poppinsBold25f);
    	titleLabel.setForeground(Color.WHITE);
    	GridBagConstraints titleLabelGBC = new GridBagConstraints(); 
    	titleLabelGBC.gridx = 0;
    	titleLabelGBC.gridy = 0;
    	titleLabelGBC.fill = GridBagConstraints.BOTH;
    	titleLabelGBC.insets = new Insets (0,-100,0,390);
        headerPanel.add(titleLabel,titleLabelGBC);
    	
    	// search button
    	ImageIcon searchButtonImage = new ImageIcon("resources/images/search-button-small.png");
    	searchButton = new JButton(searchButtonImage);
    	searchButton.setBorder(null);
        searchButton.setContentAreaFilled(false); 
        searchButton.setFocusPainted(false);
        GridBagConstraints searchButtonGBC = new GridBagConstraints(); 
        searchButtonGBC.gridx = 2;
        searchButtonGBC.gridy = 0;
        searchButtonGBC.anchor = GridBagConstraints.EAST;
        searchButtonGBC.insets = new Insets (0,0,5,0);
        headerPanel.add(searchButton,searchButtonGBC);
        
        // search label
        JLabel searchLabel = new JLabel ("Search");
        searchLabel.setFont(FontLoader.poppinsRegular14f);
        searchLabel.setForeground(Color.GRAY);
        GridBagConstraints searchLabelGBC = new GridBagConstraints(); 
        searchLabelGBC.gridx = 2;
        searchLabelGBC.gridy = 0;
        searchLabelGBC.anchor = GridBagConstraints.WEST;
        searchLabelGBC.fill = GridBagConstraints.BOTH;
        searchLabelGBC.insets = new Insets (0,10,0,0);
        headerPanel.add(searchLabel,searchLabelGBC);
        
    	// search field
        searchField = new JTextField(20);
        searchField.setFont(FontLoader.poppinsRegular14f);
        searchField.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints searchFieldGBC = new GridBagConstraints();
        searchFieldGBC.gridx = 2;
        searchFieldGBC.gridy = 0;
        searchLabelGBC.fill = GridBagConstraints.BOTH;
        searchFieldGBC.insets = new Insets (0,0,0,0);
        headerPanel.add(searchField, searchFieldGBC);
    	
        // clear button
    	clearButton = new JButton("Clear");
    	clearButton.setBackground(Color.decode("#718bab"));
    	clearButton.setForeground(Color.WHITE);
    	clearButton.setFont(FontLoader.poppinsRegular14f);
    	GridBagConstraints clearButtonGBC = new GridBagConstraints(); 
    	clearButtonGBC.gridx = 3;
    	clearButtonGBC.gridy = 0;
    	clearButtonGBC.fill = GridBagConstraints.BOTH;
    	clearButtonGBC.insets = new Insets (7,5,7,-90);
    	headerPanel.add(clearButton,clearButtonGBC);
  
    	// Table setup
    	String[] columnNames = {
    	    "Employee ID", "Last Name", "First Name", "Position",
    	    "Status", "Basic Salary", "View", "Edit", "Delete"
    	};

    	tableModel = new DefaultTableModel(columnNames, 0) {
    	    @Override
    	    public boolean isCellEditable(int row, int column) {
    	        return column >= 6;
    	    }

    	    @Override
    	    public Class<?> getColumnClass(int columnIndex) {
    	        return columnIndex >= 6 ? JButton.class : Object.class;
    	    }
    	};

    	// JTable with zebra striping
    	employeeTable = new JTable(tableModel) {
    	    @Override
    	    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    	        Component c = super.prepareRenderer(renderer, row, column);
    	        if (!isRowSelected(row)) {
    	            Color bg = (row % 2 == 0) ? Color.WHITE : new Color(240, 240, 240);
    	            c.setBackground(bg);
    	        } else {
    	            c.setBackground(getSelectionBackground());
    	        }
    	        return c;
    	    }
    	};

    	employeeTable.setFont(FontLoader.poppinsRegular14f);
    	employeeTable.setRowHeight(35);
    	employeeTable.setShowGrid(true);
    	employeeTable.setGridColor(Color.LIGHT_GRAY);
    	employeeTable.setFillsViewportHeight(true);

    	// Column widths
    	employeeTable.getColumnModel().getColumn(0).setPreferredWidth(80);
    	employeeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
    	employeeTable.getColumnModel().getColumn(2).setPreferredWidth(120);
    	employeeTable.getColumnModel().getColumn(3).setPreferredWidth(150);
    	employeeTable.getColumnModel().getColumn(4).setPreferredWidth(80);
    	employeeTable.getColumnModel().getColumn(5).setPreferredWidth(100);
    	employeeTable.getColumnModel().getColumn(6).setPreferredWidth(35);
    	employeeTable.getColumnModel().getColumn(7).setPreferredWidth(35);
    	employeeTable.getColumnModel().getColumn(8).setPreferredWidth(35);

    	// Button renderers/editors
    	employeeTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("View"));
    	employeeTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JButton("View"), "view"));

    	employeeTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer("Edit"));
    	employeeTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JButton("Edit"), "edit"));

    	employeeTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer("Delete"));
    	employeeTable.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JButton("Delete"), "delete"));

    	// Header font
    	JTableHeader header = employeeTable.getTableHeader();
    	header.setFont(FontLoader.poppinsSemiBold18f);

    	// Scroll pane
    	JScrollPane scrollPane = new JScrollPane(employeeTable);

    	// Button panel
    	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	addEmployeeButton = new JButton("Add New Employee");
    	refreshButton = new JButton("Refresh Data");

    	addEmployeeButton.setFont(FontLoader.poppinsRegular14f);
    	refreshButton.setFont(FontLoader.poppinsRegular14f);

    	buttonPanel.add(addEmployeeButton);
    	buttonPanel.add(refreshButton);

    	// Status panel
    	JPanel statusPanel = new JPanel(new BorderLayout());
    	statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

    	statusLabel = new JLabel("Ready");
    	statusLabel.setFont(FontLoader.poppinsRegular14f);
    	
    	logoutButton = new JButton("Logout");
    	logoutButton.setFont(FontLoader.poppinsRegular14f);
    	logoutButton.setBackground(Color.RED);
    	logoutButton.setForeground(Color.WHITE);
    	logoutButton.setFocusPainted(false);
    	logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    	logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	logoutButton.setOpaque(true);

    	statusPanel.add(statusLabel, BorderLayout.WEST);
    	statusPanel.add(logoutButton, BorderLayout.EAST);

    	// Add panels to layout
    	mainPanel.add(headerPanel, BorderLayout.NORTH);
    	mainPanel.add(scrollPane, BorderLayout.CENTER);
    	mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    	mainPanel.add(statusPanel, BorderLayout.PAGE_END);

    	add(mainPanel);

    	// Event handlers
    	addEmployeeButton.addActionListener(e -> openAddEmployee());
    	refreshButton.addActionListener(e -> loadEmployeeData());
    	searchButton.addActionListener(e -> searchEmployees());
    	clearButton.addActionListener(e -> {
    	    searchField.setText("");
    	    loadEmployeeData();
    	});
    	logoutButton.addActionListener(e -> logout());
    	searchField.addActionListener(e -> searchEmployees());
    }
    
    private void loadEmployeeData() {
        try {
            // Clear the table
            tableModel.setRowCount(0);
            
            // Get all employees
            List<EmployeeInformation> employees = EmployeeDAO.getAllEmployees();
            
            // Add data to the table
            for (EmployeeInformation employee : employees) {
                Compensation comp = EmployeeDAO.getEmployeeCompensation(employee.getEmployeeNumber());
                
                tableModel.addRow(new Object[] {
                    employee.getEmployeeNumber(),
                    employee.getLastName(),
                    employee.getFirstName(),
                    employee.getPosition(),
                    employee.getStatus(),
                    comp != null ? String.format("%.2f", comp.getBasicSalary()) : "N/A",
                    "View",
                    "Edit",
                    "Delete"
                });
            }
            
            statusLabel.setText("Loaded " + employees.size() + " employees");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading employee data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error loading data");
        }
    }
    
    private void searchEmployees() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadEmployeeData();
            return;
        }
        
        try {
            // Clear the table
            tableModel.setRowCount(0);
            
            // Search employees
            List<EmployeeInformation> employees = EmployeeDAO.searchEmployeesByName(searchTerm);
            
            // Add data to the table
            for (EmployeeInformation employee : employees) {
                Compensation comp = EmployeeDAO.getEmployeeCompensation(employee.getEmployeeNumber());
                
                tableModel.addRow(new Object[] {
                    employee.getEmployeeNumber(),
                    employee.getLastName(),
                    employee.getFirstName(),
                    employee.getPosition(),
                    employee.getStatus(),
                    comp != null ? String.format("%.2f", comp.getBasicSalary()) : "N/A",
                    "View",
                    "Edit",
                    "Delete"
                });
            }
            
            statusLabel.setText("Found " + employees.size() + " employees matching '" + searchTerm + "'");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error searching employees: " + e.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error searching data");
        }
    }
    
    private void viewEmployee(String employeeNumber) {
        try {
            GovernmentIdentification govId = EmployeeDAO.getEmployeeGovId(employeeNumber);
            Compensation comp = EmployeeDAO.getEmployeeCompensation(employeeNumber);
            
            if (govId != null && comp != null) {
                FinanceViewEmployeeDetailsPage viewPage = new FinanceViewEmployeeDetailsPage(govId, comp);
                viewPage.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not load employee details.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading employee details: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editEmployee(String employeeNumber) {
        try {
            GovernmentIdentification govId = EmployeeDAO.getEmployeeGovId(employeeNumber);
            Compensation comp = EmployeeDAO.getEmployeeCompensation(employeeNumber);
            
            if (govId != null && comp != null) {
                dispose();
                FinanceUpdateEmployeeDetailsPage updatePage = new FinanceUpdateEmployeeDetailsPage(govId, comp);
                updatePage.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not load employee details for editing.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading employee details: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteEmployee(String employeeNumber) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete employee #" + employeeNumber + "?\nThis action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (EmployeeDAO.deleteEmployee(employeeNumber)) {
                    JOptionPane.showMessageDialog(this,
                        "Employee deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadEmployeeData();
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
    }
    
    private void openAddEmployee() {
        dispose();
        new FinanceAddEmployeePage().setVisible(true);
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new LoginPage().setVisible(true);
        }
    }
    
    // Button renderer for the action columns
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
            setFocusPainted(false);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(UIManager.getColor("Button.background"));
                setForeground(UIManager.getColor("Button.foreground"));
            }
            return this;
        }
    }
    
    // Helper method for date formatting
    private String formatDateForDisplay(String sqlDate) {
        if (sqlDate == null || sqlDate.trim().isEmpty()) {
            return "";
        }
        
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
            java.util.Date date = inputFormat.parse(sqlDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return sqlDate; // Return original if parsing fails
        }
    }
    
    // Button editor for the action columns
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String action;
        private boolean isPushed;
        private int currentRow;
        
        public ButtonEditor(JButton button, String action) {
            super(new JCheckBox());
            this.button = button;
            this.action = action;
            
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Get the employee number from the first column
                String employeeNumber = employeeTable.getValueAt(currentRow, 0).toString();
                
                // Perform the appropriate action
                switch (action) {
                    case "view":
                        viewEmployee(employeeNumber);
                        break;
                    case "edit":
                        editEmployee(employeeNumber);
                        break;
                    case "delete":
                        deleteEmployee(employeeNumber);
                        break;
                }
            }
            isPushed = false;
            return button.getText();
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    // custom font
    public class FontLoader {

        // Public static font variable (accessible from anywhere)
        public static final Font poppinsRegular14f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 14f);
        public static final Font poppinsRegular20f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 20f);
        public static final Font poppinsSemiBold18f = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 18f);
        public static final Font poppinsBold25f = loadCustomFont("resources/fonts/Poppins-Bold.ttf", 25f);

        // Font loading utility
        private static Font loadCustomFont(String fontPath, float size) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(size);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                return font;
            } catch (FontFormatException | IOException e) {
                System.err.println("Error loading font: " + e.getMessage());
                return new Font("SansSerif", Font.PLAIN, (int) size); // fallback font
            }
        }
    }
}