package GUI.hr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.List;

import Classes.*;
import DAO.EmployeeDAO;

@SuppressWarnings("serial")
public class HRViewAllEmployeesPage extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchBtn;
    private JButton refreshBtn;
    private JButton backBtn;
    
    public HRViewAllEmployeesPage() {
        initComponents();
        loadEmployees();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("All Employees & Payslips - HR Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setResizable(true);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(77, 77, 105));
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        titlePanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("All Employees & Payslips");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        
        backBtn = new JButton("Back to Dashboard");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setBackground(new Color(108, 117, 125));
        backBtn.setForeground(Color.BLACK);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> goBackToDashboard());
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(backBtn, BorderLayout.EAST);
        
        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel searchLabel = new JLabel("Filter by Name or Employee #:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(200, 25));
        
        searchBtn = new JButton("Filter");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setBackground(new Color(40, 167, 69));
        searchBtn.setForeground(Color.BLACK);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> filterEmployees());
        
        refreshBtn = new JButton("Show All");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(108, 117, 125));
        refreshBtn.setForeground(Color.BLACK);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadEmployees());
        
        // Add enter key listener to search field
        searchField.addActionListener(e -> filterEmployees());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        
        // Table setup
        String[] columnNames = {
            "Employee #", "Name", "Position", "Status", 
            "Basic Salary", "Phone", "View Details", "Create Payslip"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 6; // Only buttons are editable
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        employeeTable.setRowHeight(35);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set column widths
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Employee #
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Position
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Basic Salary
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Phone
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(100); // View Details
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(120); // Create Payslip
        
        // Custom header renderer
        JTableHeader header = employeeTable.getTableHeader();
        header.setDefaultRenderer(new BoldHeaderRenderer());
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Button renderers and editors
        employeeTable.getColumn("View Details").setCellRenderer(new ButtonRenderer("View Details"));
        employeeTable.getColumn("View Details").setCellEditor(new ButtonEditor("View Details"));
        
        employeeTable.getColumn("Create Payslip").setCellRenderer(new ButtonRenderer("Create Payslip"));
        employeeTable.getColumn("Create Payslip").setCellEditor(new ButtonEditor("Create Payslip"));
        
        // Table scroll pane
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadEmployees() {
        tableModel.setRowCount(0);
        List<EmployeeInformation> employees = EmployeeDAO.getAllEmployees();
        
        for (EmployeeInformation employee : employees) {
            Compensation compensation = new Compensation(employee.getEmployeeNumber());
            
            Object[] rowData = {
                employee.getEmployeeNumber(),
                employee.getFirstName() + " " + employee.getLastName(),
                employee.getPosition(),
                employee.getStatus(),
                String.format("₱%.2f", compensation.getBasicSalary()),
                employee.getPhoneNumber(),
                "View Details",
                "Create Payslip"
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void filterEmployees() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        
        if (searchTerm.isEmpty()) {
            loadEmployees();
            return;
        }
        
        tableModel.setRowCount(0);
        List<EmployeeInformation> employees = EmployeeDAO.getAllEmployees();
        
        for (EmployeeInformation employee : employees) {
            String fullName = (employee.getFirstName() + " " + employee.getLastName()).toLowerCase();
            String empNum = employee.getEmployeeNumber().toLowerCase();
            
            if (fullName.contains(searchTerm) || empNum.contains(searchTerm)) {
                Compensation compensation = new Compensation(employee.getEmployeeNumber());
                
                Object[] rowData = {
                    employee.getEmployeeNumber(),
                    employee.getFirstName() + " " + employee.getLastName(),
                    employee.getPosition(),
                    employee.getStatus(),
                    String.format("₱%.2f", compensation.getBasicSalary()),
                    employee.getPhoneNumber(),
                    "View Details",
                    "Create Payslip"
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void viewEmployeeDetails(int row) {
        String employeeNumber = (String) tableModel.getValueAt(row, 0);
        EmployeeInformation employee = EmployeeDAO.getEmployeeByNumber(employeeNumber);
        
        if (employee != null) {
            showEmployeeDetailsDialog(employee);
        }
    }
    
    private void createPayslip(int row) {
        String employeeNumber = (String) tableModel.getValueAt(row, 0);
        EmployeeInformation employee = EmployeeDAO.getEmployeeByNumber(employeeNumber);
        
        if (employee != null) {
            try {
                GovernmentIdentification govId = new GovernmentIdentification(employee.getEmployeeNumber());
                Compensation compensation = new Compensation(employee.getEmployeeNumber());
                
                dispose();
                new HRCreatePayslipPage(
                        employee.getEmployeeNumber(),
                        employee.getFirstName() + " " + employee.getLastName(),
                        employee.getAddress(),
                        employee.getSupervisor(),
                        govId.getSSSNumber(),
                        String.valueOf(govId.getPhilHealthNumber()),
                        employee.getPhoneNumber(),
                        employee.getPosition(),
                        govId.getTinNumber(),
                        String.valueOf(govId.getPagibigNumber()),
                        compensation
                    ).setVisible(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error creating payslip: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void showEmployeeDetailsDialog(EmployeeInformation employee) {
        JDialog detailDialog = new JDialog(this, "Employee Details", true);
        detailDialog.setSize(600, 500);
        detailDialog.setLocationRelativeTo(this);
        
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 15);
        
        // Get additional employee data
        GovernmentIdentification govId = new GovernmentIdentification(employee.getEmployeeNumber());
        Compensation compensation = new Compensation(employee.getEmployeeNumber());
        
        // Add all employee details
        addDetailRow(detailPanel, "Employee Number:", employee.getEmployeeNumber(), gbc, 0);
        addDetailRow(detailPanel, "First Name:", employee.getFirstName(), gbc, 1);
        addDetailRow(detailPanel, "Last Name:", employee.getLastName(), gbc, 2);
        addDetailRow(detailPanel, "Birthday:", employee.getBirthday(), gbc, 3);
        addDetailRow(detailPanel, "Address:", employee.getAddress(), gbc, 4);
        addDetailRow(detailPanel, "Phone Number:", employee.getPhoneNumber(), gbc, 5);
        addDetailRow(detailPanel, "Position:", employee.getPosition(), gbc, 6);
        addDetailRow(detailPanel, "Status:", employee.getStatus(), gbc, 7);
        addDetailRow(detailPanel, "Supervisor:", employee.getSupervisor(), gbc, 8);
        addDetailRow(detailPanel, "Basic Salary:", String.format("₱%.2f", compensation.getBasicSalary()), gbc, 9);
        addDetailRow(detailPanel, "SSS:", govId.getSSSNumber(), gbc, 10);
        addDetailRow(detailPanel, "PhilHealth:", String.valueOf(govId.getPhilHealthNumber()), gbc, 11);
        addDetailRow(detailPanel, "TIN:", govId.getTinNumber(), gbc, 12);
        addDetailRow(detailPanel, "Pag-IBIG:", String.valueOf(govId.getPagibigNumber()), gbc, 13);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> detailDialog.dispose());
        gbc.gridx = 0; gbc.gridy = 14; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        detailPanel.add(closeBtn, gbc);
        
        JScrollPane dialogScrollPane = new JScrollPane(detailPanel);
        detailDialog.add(dialogScrollPane);
        detailDialog.setVisible(true);
    }
    
    private void addDetailRow(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblLabel, gbc);
        
        gbc.gridx = 1;
        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lblValue, gbc);
    }
    
    private void goBackToDashboard() {
        dispose();
        new HRDashboard().setVisible(true);
    }
    
    // Header renderer
    class BoldHeaderRenderer extends DefaultTableCellRenderer {
        public BoldHeaderRenderer() {
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
            setBackground(new Color(240, 240, 240));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return c;
        }
    }
    
    // Button renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        private String buttonText;
        
        public ButtonRenderer(String text) {
            this.buttonText = text;
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 10));
            setFocusPainted(false);
            
            if (text.equals("Create Payslip")) {
                setBackground(new Color(0, 123, 255));
            } else {
                setBackground(new Color(108, 117, 125));
            }
            setForeground(Color.BLACK);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(buttonText);
            return this;
        }
    }
    
    // Button editor
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;
        
        public ButtonEditor(String text) {
            super(new JCheckBox());
            this.label = text;
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Segoe UI", Font.BOLD, 10));
            button.setFocusPainted(false);
            
            if (text.equals("Create Payslip")) {
                button.setBackground(new Color(0, 123, 255));
            } else {
                button.setBackground(new Color(108, 117, 125));
            }
            button.setForeground(Color.BLACK);
            
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
            this.selectedRow = row;
            button.setText(label);
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if (label.equals("View Details")) {
                    SwingUtilities.invokeLater(() -> viewEmployeeDetails(selectedRow));
                } else if (label.equals("Create Payslip")) {
                    SwingUtilities.invokeLater(() -> createPayslip(selectedRow));
                }
            }
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
