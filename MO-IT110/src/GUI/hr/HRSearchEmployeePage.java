package GUI.hr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.List;

import Classes.*;
import DAO.EmployeeDAO;
import UtilityClasses.SalaryCalculator;

@SuppressWarnings("serial")
public class HRSearchEmployeePage extends JFrame {
    private JTextField searchField;
    private JButton searchBtn;
    private JButton backBtn;
    private JPanel resultsPanel;
    private JScrollPane scrollPane;
    
    public HRSearchEmployeePage() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Search Employee - HR Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 788);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(77, 77, 105));
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        titlePanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Search Employee & Create Payslip");
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
        searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel searchLabel = new JLabel("Search by Employee Number or Name:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(250, 30));
        
        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtn.setBackground(new Color(40, 167, 69));
        searchBtn.setForeground(Color.BLACK);
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> searchEmployees());
        
        // Add enter key listener to search field
        searchField.addActionListener(e -> searchEmployees());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        // Results panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Show initial instructions
        showInstructions();
    }
    
    private void showInstructions() {
        resultsPanel.removeAll();
        
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center;'>" +
            "<h3>Search Instructions:</h3>" +
            "<p>• Enter employee number (e.g., 10001) or name (e.g., John Doe)</p>" +
            "<p>• Click Search or press Enter to find employees</p>" +
            "<p>• Click 'Create Payslip' button next to employee to generate payslip</p>" +
            "</div></html>");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        instructionPanel.add(instructionLabel);
        resultsPanel.add(instructionPanel);
        
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    private void searchEmployees() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        resultsPanel.removeAll();
        
        List<EmployeeInformation> employees;
        
        // Check if search term is numeric (employee number)
        if (searchTerm.matches("\\d+")) {
            EmployeeInformation employee = EmployeeDAO.getEmployeeByNumber(searchTerm);
            employees = employee != null ? List.of(employee) : List.of();
        } else {
            // Search by name
            employees = EmployeeDAO.searchEmployeesByName(searchTerm);
        }
        
        if (employees.isEmpty()) {
            JPanel noResultsPanel = new JPanel();
            noResultsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel noResultsLabel = new JLabel("No employees found matching: " + searchTerm);
            noResultsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            noResultsPanel.add(noResultsLabel);
            resultsPanel.add(noResultsPanel);
        } else {
            for (EmployeeInformation employee : employees) {
                JPanel employeePanel = createEmployeePanel(employee);
                resultsPanel.add(employeePanel);
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    private JPanel createEmployeePanel(EmployeeInformation employee) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);
        
        // Employee info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 2, 15);
        
        // Employee details
        addInfoLabel(infoPanel, "Employee #:", employee.getEmployeeNumber(), gbc, 0);
        addInfoLabel(infoPanel, "Name:", employee.getFirstName() + " " + employee.getLastName(), gbc, 1);
        addInfoLabel(infoPanel, "Position:", employee.getPosition(), gbc, 2);
        addInfoLabel(infoPanel, "Status:", employee.getStatus(), gbc, 3);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton createPayslipBtn = new JButton("Create Payslip");
        createPayslipBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        createPayslipBtn.setBackground(new Color(0, 123, 255));
        createPayslipBtn.setForeground(Color.BLACK);
        createPayslipBtn.setFocusPainted(false);
        createPayslipBtn.addActionListener(e -> createPayslip(employee));
        
        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewDetailsBtn.setBackground(new Color(108, 117, 125));
        viewDetailsBtn.setForeground(Color.BLACK);
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.addActionListener(e -> viewEmployeeDetails(employee));
        
        buttonPanel.add(createPayslipBtn);
        buttonPanel.add(viewDetailsBtn);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void addInfoLabel(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblLabel, gbc);
        
        gbc.gridx = 1;
        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lblValue, gbc);
    }
    
    private void createPayslip(EmployeeInformation employee) {
        try {
            // Get additional employee data
            GovernmentIdentification govId = new GovernmentIdentification(employee.getEmployeeNumber());
            Compensation compensation = new Compensation(employee.getEmployeeNumber());
            
            // Open payslip page
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
    
    private void viewEmployeeDetails(EmployeeInformation employee) {
        // Create a dialog to show employee details
        JDialog detailDialog = new JDialog(this, "Employee Details", true);
        detailDialog.setSize(500, 400);
        detailDialog.setLocationRelativeTo(this);
        
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 15);
        
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
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> detailDialog.dispose());
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        detailPanel.add(closeBtn, gbc);
        
        detailDialog.add(detailPanel);
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
}
