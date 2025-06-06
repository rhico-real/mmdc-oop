package GUI.finance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JLabel titleLabel = new JLabel("HR Department - Employee Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        // Table setup
        String[] columnNames = {
            "Employee ID", "Last Name", "First Name", "Position", 
            "Status", "Basic Salary", "View", "Edit", "Delete"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only make action buttons columns editable
                return column >= 6;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex >= 6 ? JButton.class : Object.class;
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(35);
        employeeTable.setShowGrid(true);
        employeeTable.setGridColor(Color.LIGHT_GRAY);
        employeeTable.setFillsViewportHeight(true);
        
        // Column widths
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Last Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(120); // First Name
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Position
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Status
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Salary
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(70);  // View
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(70);  // Edit
        employeeTable.getColumnModel().getColumn(8).setPreferredWidth(70);  // Delete
        
        // Button renderers and editors
        employeeTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("View"));
        employeeTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JButton("View"), "view"));
        
        employeeTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer("Edit"));
        employeeTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JButton("Edit"), "edit"));
        
        employeeTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer("Delete"));
        employeeTable.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JButton("Delete"), "delete"));
        
        // Make header bold
        JTableHeader header = employeeTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addEmployeeButton = new JButton("Add New Employee");
        refreshButton = new JButton("Refresh Data");
        
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(refreshButton);
        
        // Status panel at the bottom
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        statusLabel = new JLabel("Ready");
        logoutButton = new JButton("Logout");
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(logoutButton, BorderLayout.EAST);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
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
        
        // Search field enter key
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
}
