package GUI.hr;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;
import java.util.List;

import Classes.*;
import DAO.EmployeeDAO;
import UtilityClasses.SalaryCalculator;

@SuppressWarnings("serial")
public class HRSearchEmployeePage extends JFrame {
    private JLabel financeLogoLabel;
	private JTextField searchField;
    private JButton searchButton;
    private JButton backButton;
    private JPanel resultsPanel;
    private JScrollPane scrollPane;
    
    public HRSearchEmployeePage() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Search Employee - HR Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#f5f5f5"));
        setSize(1366, 788);
        setResizable(false);
        
        // custom color
        String navyBlue = "#153969";
        String lightGray = "#f5f5f5";
        String lightRed ="#ff5757";
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode(lightGray));
        
        // nav bar panel
        JPanel navBarPanel = new JPanel(new BorderLayout());
        navBarPanel.setBackground(Color.decode(navyBlue));
        navBarPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        mainPanel.add(navBarPanel, BorderLayout.NORTH);
        
        // back button
        ImageIcon backButtonImage = new ImageIcon("resources/images/back-button-navbar.png");
        backButton = new JButton(backButtonImage);
        backButton.setFocusPainted(false);
        backButton.setBorder(null);
        backButton.setContentAreaFilled(false);   
        backButton.addActionListener(e -> goBackToDashboard());
        navBarPanel.add(backButton, BorderLayout.WEST);
        
        // finance logo
        ImageIcon financeLogo = new ImageIcon("resources/images/Finance-Logo.png");
        financeLogoLabel = new JLabel(financeLogo);
        navBarPanel.add(financeLogoLabel, BorderLayout.EAST);
        
        // content panel
        JPanel contentPanel = new JPanel (new BorderLayout());
        contentPanel.setBackground(Color.decode(lightGray));
        mainPanel.add(contentPanel, BorderLayout.CENTER); // add to content panel at the top
        
        // title panel
        JPanel titlePanel = new JPanel (new GridBagLayout());
        titlePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPanel.add(titlePanel, BorderLayout.NORTH);

        // title label
        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'><b>Search Employee<br>and Create Payslip</b></html>");
        titleLabel.setFont(FontLoader.poppinsBold45f);
        titleLabel.setForeground(Color.decode(navyBlue));
        GridBagConstraints titleLabelGBC = new GridBagConstraints();        
        titleLabelGBC.gridx = 0;
        titleLabelGBC.gridy = 0;
        titleLabelGBC.anchor = GridBagConstraints.CENTER;
        titleLabelGBC.insets = new Insets (50,0,0,0);
        titlePanel.add(titleLabel, titleLabelGBC);
        
        // Search panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        contentPanel.add(searchPanel,BorderLayout.CENTER); // add to content panel at the center
        
        // search label
        JLabel searchLabel = new JLabel("Employee Number or Name");
        searchLabel.setForeground(Color.GRAY);
        searchLabel.setFont(FontLoader.poppinsRegular20f);
        GridBagConstraints searchLabelGBC = new GridBagConstraints();        
        searchLabelGBC.gridx = 0;
        searchLabelGBC.gridy = 0;
        searchLabelGBC.fill = GridBagConstraints.BOTH;
        searchLabelGBC.insets = new Insets (-70,15,10,0);
        searchPanel.add(searchLabel,searchLabelGBC);
        
        // search field
        searchField = new JTextField(20);
        searchField.setFont(FontLoader.poppinsRegular20f);
        searchField.addActionListener(e -> searchEmployees());
        
        Border outerBorder = new LineBorder(Color.GRAY, 2, true); // outer border
		Border innerPadding = new EmptyBorder(5, 9, 5, 10); // inner border
		searchField.setBorder(new CompoundBorder(outerBorder, innerPadding));
        
        GridBagConstraints searchFieldGBC = new GridBagConstraints();        
        searchFieldGBC.gridx = 0;
        searchFieldGBC.gridy = 0;
        searchFieldGBC.fill = GridBagConstraints.BOTH;
        searchFieldGBC.insets = new Insets (-70,0,10,0);
        searchPanel.add(searchField,searchFieldGBC);
        
        // search button
        ImageIcon searchButtonImage = new ImageIcon("resources/images/search-button.png");
        searchButton = new JButton(searchButtonImage);
        searchButton.setBorder(null);
        searchButton.setContentAreaFilled(false); 
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> searchEmployees());
        GridBagConstraints searchButtonGBC = new GridBagConstraints();        
        searchButtonGBC.gridx = 1;
        searchButtonGBC.gridy = 0;
        searchButtonGBC.fill = GridBagConstraints.BOTH;
        searchButtonGBC.insets = new Insets (-70,10,10,0);
        searchPanel.add(searchButton,searchButtonGBC);
        
        // Results panel
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(10, 200, 20, 200));
        
        contentPanel.add(scrollPane, BorderLayout.SOUTH);
        
        // Add components to main panel
     
        
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
        instructionLabel.setFont(FontLoader.poppinsRegular12f);
        
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
            noResultsLabel.setFont(FontLoader.poppinsRegular12f);
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
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        
        JButton createPayslipBtn = new JButton("Create Payslip");
        createPayslipBtn.setFont(FontLoader.poppinsRegular12f);
        createPayslipBtn.setBackground(Color.decode("#718bab"));
        createPayslipBtn.setForeground(Color.WHITE);
        createPayslipBtn.setFocusPainted(false);
        createPayslipBtn.addActionListener(e -> createPayslip(employee));
        GridBagConstraints createPayslipBtnGBC = new GridBagConstraints();        
        createPayslipBtnGBC.gridx = 0;
        createPayslipBtnGBC.gridy = 0;
        createPayslipBtnGBC.fill = GridBagConstraints.BOTH;
        createPayslipBtnGBC.insets = new Insets (5,5,5,10);
        buttonPanel.add(createPayslipBtn,createPayslipBtnGBC);
        
        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.setFont(FontLoader.poppinsRegular12f);
        viewDetailsBtn.setBackground(Color.decode("#718bab"));
        viewDetailsBtn.setForeground(Color.WHITE);
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.addActionListener(e -> viewEmployeeDetails(employee));
        GridBagConstraints viewDetailsBtnGBC = new GridBagConstraints();        
        viewDetailsBtnGBC.gridx = 0;
        viewDetailsBtnGBC.gridy = 1;
        viewDetailsBtnGBC.fill = GridBagConstraints.BOTH;
        viewDetailsBtnGBC.insets = new Insets (5,5,5,10);
        buttonPanel.add(viewDetailsBtn,viewDetailsBtnGBC);
        
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void addInfoLabel(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(FontLoader.poppinsRegular12f);
        panel.add(lblLabel, gbc);
        
        gbc.gridx = 1;
        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        lblValue.setFont(FontLoader.poppinsRegular12f);
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
    
 // custom font
    public class FontLoader {

        // Public static font variable (accessible from anywhere)
        public static final Font poppinsRegular12f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 12f);
        public static final Font poppinsRegular20f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 20f);
        public static final Font poppinsSemiBold20f = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 20f);
        public static final Font poppinsBold45f = loadCustomFont("resources/fonts/Poppins-Bold.ttf", 55f);

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
    
    private void goBackToDashboard() {
        dispose();
        new HRDashboard().setVisible(true);
    }
}
