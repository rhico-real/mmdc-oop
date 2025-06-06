package GUI.hr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.text.DecimalFormat;
import java.util.Map;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import Classes.Compensation;
import DAO.AttendanceDAO;
import UtilityClasses.SalaryCalculator;

// JasperReports imports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.util.ArrayList;
import java.util.List;
import java.awt.Desktop;
import java.io.InputStream;

@SuppressWarnings("serial")
public class HRCreatePayslipPage extends JFrame {
    
    private String employeeNumber;
    private String employeeName;
    private String address;
    private String supervisor;
    private String sss;
    private String philhealth;
    private String phoneNumber;
    private String position;
    private String tin;
    private String pagibig;
    private Compensation compensation;
    
    // Form components
    private JTextField monthField;
    private JTextField yearField;
    private JTextField daysWorkedField;
    private JTextField overtimeHoursField;
    private JButton calculateBtn;
    private JButton generateReportBtn;
    private JButton backBtn;
    private JPanel payslipPanel;
    private Map<String, Object> currentSalaryData;
    private int currentMonth;
    private int currentYear;
    private int currentDaysWorked;
    private double currentOvertimeHours;
    
    public HRCreatePayslipPage(String employeeNumber, String employeeName, String address, 
                               String supervisor, String sss, String philhealth, String phoneNumber,
                               String position, String tin, String pagibig, Compensation compensation) {
        this.employeeNumber = employeeNumber;
        this.employeeName = employeeName;
        this.address = address;
        this.supervisor = supervisor;
        this.sss = sss;
        this.philhealth = philhealth;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.tin = tin;
        this.pagibig = pagibig;
        this.compensation = compensation;
        
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Create Payslip - HR Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);
        setResizable(true);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(77, 77, 105));
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        titlePanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Create Payslip for " + employeeName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLACK);
        
        backBtn = new JButton("Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setBackground(new Color(108, 117, 125));
        backBtn.setForeground(Color.BLACK);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> goBack());
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(backBtn, BorderLayout.EAST);
        
        // Input panel
        JPanel inputPanel = createInputPanel();
        
        // Payslip display panel
        payslipPanel = new JPanel();
        payslipPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        payslipPanel.setLayout(new BoxLayout(payslipPanel, BoxLayout.Y_AXIS));
        
        // Scroll pane for payslip
        JScrollPane scrollPane = new JScrollPane(payslipPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, scrollPane);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.4);
        
        // Add components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Show initial instructions
        showInstructions();
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        inputPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Employee info (read-only)
        JLabel empInfoLabel = new JLabel("Employee Information:");
        empInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(empInfoLabel, gbc);
        
        gbc.gridwidth = 1;
        addReadOnlyField(inputPanel, "Employee #:", employeeNumber, gbc, 1);
        addReadOnlyField(inputPanel, "Name:", employeeName, gbc, 2);
        addReadOnlyField(inputPanel, "Position:", position, gbc, 3);
        
        // Separator
        JSeparator separator = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 20, 10);
        inputPanel.add(separator, gbc);
        
        // Payroll period inputs
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel payrollLabel = new JLabel("Payroll Period:");
        payrollLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        inputPanel.add(payrollLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Month and Year inputs
        addInputField(inputPanel, "Month (1-12):", monthField = new JTextField(10), gbc, 6);
        addInputField(inputPanel, "Year:", yearField = new JTextField(10), gbc, 7);
        addInputField(inputPanel, "Days Worked:", daysWorkedField = new JTextField(10), gbc, 8);
        addInputField(inputPanel, "Overtime Hours:", overtimeHoursField = new JTextField(10), gbc, 9);
        
        // Set default values
        overtimeHoursField.setText("0");
        
        // Auto-calculate button
        JButton autoCalcBtn = new JButton("Auto-Calculate from Attendance");
        autoCalcBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        autoCalcBtn.setBackground(new Color(255, 193, 7));
        autoCalcBtn.setForeground(Color.BLACK);
        autoCalcBtn.setFocusPainted(false);
        autoCalcBtn.addActionListener(e -> autoCalculateAttendance());
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(autoCalcBtn, gbc);
        
        // Calculate payslip button
        calculateBtn = new JButton("Generate Payslip");
        calculateBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        calculateBtn.setBackground(new Color(40, 167, 69));
        calculateBtn.setForeground(Color.BLACK);
        calculateBtn.setFocusPainted(false);
        calculateBtn.addActionListener(e -> generatePayslip());
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2;
        inputPanel.add(calculateBtn, gbc);
        
        // Generate Report button
        generateReportBtn = new JButton("Generate Payslip Report (PDF)");
        generateReportBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        generateReportBtn.setBackground(new Color(220, 53, 69));
        generateReportBtn.setForeground(Color.BLACK);
        generateReportBtn.setFocusPainted(false);
        generateReportBtn.setEnabled(false); // Initially disabled
        generateReportBtn.addActionListener(e -> generateJasperReport());
        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 2;
        inputPanel.add(generateReportBtn, gbc);
        
        return inputPanel;
    }
    
    private void addReadOnlyField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblLabel, gbc);
        
        gbc.gridx = 1;
        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lblValue, gbc);
    }
    
    private void addInputField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblLabel, gbc);
        
        gbc.gridx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(field, gbc);
    }
    
    private void showInstructions() {
        payslipPanel.removeAll();
        
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center; padding: 20px;'>" +
            "<h3>Payslip Generation Instructions</h3>" +
            "<p>1. Fill in the payroll period (month and year)</p>" +
            "<p>2. Enter days worked and overtime hours manually, or</p>" +
            "<p>3. Click 'Auto-Calculate from Attendance' to get data from attendance records</p>" +
            "<p>4. Click 'Generate Payslip' to create the payslip</p>" +
            "</div></html>");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        payslipPanel.add(instructionLabel);
        payslipPanel.revalidate();
        payslipPanel.repaint();
    }
    
    private void autoCalculateAttendance() {
        try {
            String monthStr = monthField.getText().trim();
            String yearStr = yearField.getText().trim();
            
            if (monthStr.isEmpty() || yearStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter month and year first.", 
                    "Input Required", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);
            
            if (month < 1 || month > 12) {
                JOptionPane.showMessageDialog(this, 
                    "Month must be between 1 and 12.", 
                    "Invalid Month", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get attendance data from database
            Map<String, Object> attendanceData = AttendanceDAO.calculateMonthlyAttendance(employeeNumber, month, year);
            
            if (attendanceData != null) {
                int daysWorked = (Integer) attendanceData.get("daysWorked");
                double overtimeHours = (Double) attendanceData.get("overtimeHours");
                
                daysWorkedField.setText(String.valueOf(daysWorked));
                overtimeHoursField.setText(String.valueOf(overtimeHours));
                
                JOptionPane.showMessageDialog(this, 
                    "Attendance data loaded successfully!\nDays Worked: " + daysWorked + 
                    "\nOvertime Hours: " + overtimeHours, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No attendance data found for the specified period.", 
                    "No Data", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for month and year.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error calculating attendance: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generatePayslip() {
        try {
            // Validate inputs
            String monthStr = monthField.getText().trim();
            String yearStr = yearField.getText().trim();
            String daysWorkedStr = daysWorkedField.getText().trim();
            String overtimeHoursStr = overtimeHoursField.getText().trim();
            
            if (monthStr.isEmpty() || yearStr.isEmpty() || daysWorkedStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all required fields.", 
                    "Input Required", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);
            int daysWorked = Integer.parseInt(daysWorkedStr);
            double overtimeHours = Double.parseDouble(overtimeHoursStr.isEmpty() ? "0" : overtimeHoursStr);
            
            // Validate ranges
            if (month < 1 || month > 12) {
                JOptionPane.showMessageDialog(this, "Month must be between 1 and 12.", "Invalid Month", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (daysWorked < 0 || daysWorked > 31) {
                JOptionPane.showMessageDialog(this, "Days worked must be between 0 and 31.", "Invalid Days", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (overtimeHours < 0) {
                JOptionPane.showMessageDialog(this, "Overtime hours cannot be negative.", "Invalid Overtime", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Calculate salary using SalaryCalculator
            Map<String, Object> salaryData = SalaryCalculator.calculateSalary(
                employeeNumber, daysWorked, overtimeHours, month, year
            );
            
            // Store current data for report generation
            this.currentSalaryData = salaryData;
            this.currentMonth = month;
            this.currentYear = year;
            this.currentDaysWorked = daysWorked;
            this.currentOvertimeHours = overtimeHours;
            
            // Enable the report generation button
            generateReportBtn.setEnabled(true);
            
            // Display payslip
            displayPayslip(salaryData, month, year, daysWorked, overtimeHours);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers.", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error generating payslip: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void displayPayslip(Map<String, Object> salaryData, int month, int year, int daysWorked, double overtimeHours) {
        payslipPanel.removeAll();
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String[] monthNames = {"", "January", "February", "March", "April", "May", "June",
                              "July", "August", "September", "October", "November", "December"};
        
        // Payslip header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel companyLabel = new JLabel("MotorPH Payslip");
        companyLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        companyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel periodLabel = new JLabel("Pay Period: " + monthNames[month] + " " + year);
        periodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        periodLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(companyLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(periodLabel);
        
        // Employee information
        JPanel empInfoPanel = new JPanel(new GridBagLayout());
        empInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(15, 15, 15, 15)
        ));
        empInfoPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        addPayslipField(empInfoPanel, "Employee Number:", employeeNumber, gbc, 0);
        addPayslipField(empInfoPanel, "Employee Name:", employeeName, gbc, 1);
        addPayslipField(empInfoPanel, "Position:", position, gbc, 2);
        addPayslipField(empInfoPanel, "Address:", address, gbc, 3);
        addPayslipField(empInfoPanel, "Phone Number:", phoneNumber, gbc, 4);
        addPayslipField(empInfoPanel, "Immediate Supervisor:", supervisor, gbc, 5);
        
        // Salary details
        JPanel salaryPanel = new JPanel(new GridBagLayout());
        salaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(15, 15, 15, 15)
        ));
        salaryPanel.setBackground(Color.WHITE);
        
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(3, 10, 3, 10);
        
        // Work details
        addPayslipField(salaryPanel, "Days Worked:", String.valueOf(daysWorked), gbc, 0);
        addPayslipField(salaryPanel, "Overtime Hours:", String.valueOf(overtimeHours), gbc, 1);
        
        // Earnings
        JLabel earningsLabel = new JLabel("EARNINGS:");
        earningsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        salaryPanel.add(earningsLabel, gbc);
        gbc.gridwidth = 1;
        
        addPayslipField(salaryPanel, "Basic Salary:", "₱" + df.format((Double) salaryData.get("basicSalary")), gbc, 3);
        addPayslipField(salaryPanel, "Rice Subsidy:", "₱" + df.format(compensation.getRiceSubsidy()), gbc, 4);
        addPayslipField(salaryPanel, "Phone Allowance:", "₱" + df.format(compensation.getPhoneAllowance()), gbc, 5);
        addPayslipField(salaryPanel, "Clothing Allowance:", "₱" + df.format(compensation.getClothingAllowance()), gbc, 6);
        addPayslipField(salaryPanel, "Overtime Pay:", "₱" + df.format((Double) salaryData.get("overtimePay")), gbc, 7);
        addPayslipField(salaryPanel, "Gross Pay:", "₱" + df.format((Double) salaryData.get("grossPay")), gbc, 8);
        
        // Deductions
        JLabel deductionsLabel = new JLabel("DEDUCTIONS:");
        deductionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        salaryPanel.add(deductionsLabel, gbc);
        gbc.gridwidth = 1;
        
        addPayslipField(salaryPanel, "SSS Contribution:", "₱" + df.format((Double) salaryData.get("sssDeduction")), gbc, 10);
        addPayslipField(salaryPanel, "PhilHealth Contribution:", "₱" + df.format((Double) salaryData.get("philhealthDeduction")), gbc, 11);
        addPayslipField(salaryPanel, "Pag-IBIG Contribution:", "₱" + df.format((Double) salaryData.get("pagibigDeduction")), gbc, 12);
        addPayslipField(salaryPanel, "Withholding Tax:", "₱" + df.format((Double) salaryData.get("withholdingTax")), gbc, 13);
        addPayslipField(salaryPanel, "Total Deductions:", "₱" + df.format((Double) salaryData.get("totalDeductions")), gbc, 14);
        
        // Net pay
        JSeparator separator = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 15; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        salaryPanel.add(separator, gbc);
        
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JLabel netPayLabel = new JLabel("NET PAY:");
        netPayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 16;
        salaryPanel.add(netPayLabel, gbc);
        
        JLabel netPayValue = new JLabel("₱" + df.format((Double) salaryData.get("netPay")));
        netPayValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        netPayValue.setForeground(new Color(40, 167, 69));
        gbc.gridx = 1; gbc.gridy = 16;
        salaryPanel.add(netPayValue, gbc);
        
        // Government IDs
        JPanel govPanel = new JPanel(new GridBagLayout());
        govPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(15, 15, 15, 15)
        ));
        govPanel.setBackground(Color.WHITE);
        
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JLabel govLabel = new JLabel("Government IDs:");
        govLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        govPanel.add(govLabel, gbc);
        gbc.gridwidth = 1;
        
        addPayslipField(govPanel, "SSS Number:", sss, gbc, 1);
        addPayslipField(govPanel, "PhilHealth Number:", philhealth, gbc, 2);
        addPayslipField(govPanel, "TIN:", tin, gbc, 3);
        addPayslipField(govPanel, "Pag-IBIG Number:", pagibig, gbc, 4);
        
        // Add all panels to payslip
        payslipPanel.add(headerPanel);
        payslipPanel.add(Box.createVerticalStrut(15));
        payslipPanel.add(empInfoPanel);
        payslipPanel.add(Box.createVerticalStrut(15));
        payslipPanel.add(salaryPanel);
        payslipPanel.add(Box.createVerticalStrut(15));
        payslipPanel.add(govPanel);
        
        payslipPanel.revalidate();
        payslipPanel.repaint();
    }
    
    private void addPayslipField(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        panel.add(lblLabel, gbc);
        
        gbc.gridx = 1;
        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(lblValue, gbc);
    }
    
    private void generateJasperReport() {
        if (currentSalaryData == null) {
            JOptionPane.showMessageDialog(this, 
                "Please generate a payslip first before creating a report.", 
                "No Payslip Data", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // First check if JasperReports classes are available
            try {
                Class.forName("net.sf.jasperreports.engine.JasperCompileManager");
            } catch (ClassNotFoundException e) {
                throw new Exception("JasperReports libraries not found in classpath. Please add the following JAR files to your libs folder and update the build path:\n" +
                    "- jasperreports-6.20.6.jar\n" +
                    "- commons-collections4-4.4.jar\n" +
                    "- commons-logging-1.2.jar\n" +
                    "- itext-2.1.7.jar\n" +
                    "And ensure they are added to the Java Build Path in Eclipse.");
            }
            
            // Create reports directory if it doesn't exist
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
                System.out.println("Created reports directory: " + reportsDir.getAbsolutePath());
            }
            
            // Generate filename with timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String fileName = "Payslip_" + employeeNumber + "_" + timestamp + ".pdf";
            String outputPath = "reports/" + fileName;
            
            // Check for template files
            String jrxmlPath = "reports/PayslipReport.jrxml";
            String jasperPath = "reports/PayslipReport.jasper";
            
            File jrxmlFile = new File(jrxmlPath);
            File jasperFile = new File(jasperPath);
            
            System.out.println("Looking for templates:");
            System.out.println("JRXML file: " + jrxmlFile.getAbsolutePath() + " (exists: " + jrxmlFile.exists() + ")");
            System.out.println("Jasper file: " + jasperFile.getAbsolutePath() + " (exists: " + jasperFile.exists() + ")");
            
            // Check if compiled report exists, if not, compile from .jrxml
            if (!jasperFile.exists()) {
                if (jrxmlFile.exists()) {
                    System.out.println("Compiling JRXML template...");
                    // Compile the report
                    JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
                    System.out.println("Report compiled successfully!");
                } else {
                    throw new Exception("Report template not found.\n\n" +
                        "Expected location: " + jrxmlFile.getAbsolutePath() + "\n\n" +
                        "Please ensure PayslipReport.jrxml exists in the reports folder.\n" +
                        "The template file has been created for you at this location.");
                }
            }
            
            // Prepare parameters
            Map<String, Object> parameters = prepareReportParameters();
            System.out.println("Prepared " + parameters.size() + " parameters for the report");
            
            // Create data source (empty list since we're using parameters)
            List<Map<String, Object>> dataList = new ArrayList<>();
            dataList.add(new HashMap<>()); // Add empty map for single record
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
            
            // Fill report
            System.out.println("Filling report with data...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parameters, dataSource);
            
            // Export to PDF
            System.out.println("Exporting to PDF: " + outputPath);
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            
            // Show success message
            int result = JOptionPane.showConfirmDialog(this,
                "Payslip report generated successfully!\n" +
                "File saved as: " + fileName + "\n" +
                "Location: " + new File(outputPath).getAbsolutePath() + "\n\n" +
                "Would you like to open the report?",
                "Report Generated",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                // Open the generated PDF
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(new File(outputPath));
                    } else {
                        // Alternative: show in JasperViewer
                        JasperViewer.viewReport(jasperPrint, false);
                    }
                } catch (Exception openException) {
                    JOptionPane.showMessageDialog(this,
                        "Report generated successfully but could not open automatically.\n" +
                        "Please manually open: " + new File(outputPath).getAbsolutePath(),
                        "Report Generated",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            String errorMessage;
            if (e.getMessage().contains("JasperReports libraries not found")) {
                errorMessage = e.getMessage();
            } else if (e.getMessage().contains("Report template not found")) {
                errorMessage = e.getMessage();
            } else {
                errorMessage = "Error generating report: " + e.getMessage() + "\n\n" +
                    "Common solutions:\n" +
                    "1. Ensure JasperReports libraries are in the classpath\n" +
                    "2. Check that PayslipReport.jrxml exists in the reports folder\n" +
                    "3. Verify file permissions for the reports directory";
            }
            
            JOptionPane.showMessageDialog(this,
                errorMessage,
                "Report Generation Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private Map<String, Object> prepareReportParameters() {
        Map<String, Object> parameters = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String[] monthNames = {"", "January", "February", "March", "April", "May", "June",
                              "July", "August", "September", "October", "November", "December"};
        
        // Employee information
        parameters.put("employeeNumber", employeeNumber);
        parameters.put("employeeName", employeeName);
        parameters.put("position", position);
        parameters.put("address", address);
        parameters.put("phoneNumber", phoneNumber);
        parameters.put("supervisor", supervisor);
        
        // Government IDs
        parameters.put("sssNumber", sss);
        parameters.put("philhealthNumber", philhealth);
        parameters.put("tinNumber", tin);
        parameters.put("pagibigNumber", pagibig);
        
        // Pay period
        parameters.put("payPeriod", monthNames[currentMonth] + " " + currentYear);
        parameters.put("month", currentMonth);
        parameters.put("year", currentYear);
        parameters.put("daysWorked", currentDaysWorked);
        parameters.put("overtimeHours", currentOvertimeHours);
        
        // Earnings
        parameters.put("basicSalary", df.format((Double) currentSalaryData.get("basicSalary")));
        parameters.put("riceSubsidy", df.format(compensation.getRiceSubsidy()));
        parameters.put("phoneAllowance", df.format(compensation.getPhoneAllowance()));
        parameters.put("clothingAllowance", df.format(compensation.getClothingAllowance()));
        parameters.put("overtimePay", df.format((Double) currentSalaryData.get("overtimePay")));
        parameters.put("grossPay", df.format((Double) currentSalaryData.get("grossPay")));
        
        // Deductions
        parameters.put("sssDeduction", df.format((Double) currentSalaryData.get("sssDeduction")));
        parameters.put("philhealthDeduction", df.format((Double) currentSalaryData.get("philhealthDeduction")));
        parameters.put("pagibigDeduction", df.format((Double) currentSalaryData.get("pagibigDeduction")));
        parameters.put("withholdingTax", df.format((Double) currentSalaryData.get("withholdingTax")));
        parameters.put("totalDeductions", df.format((Double) currentSalaryData.get("totalDeductions")));
        
        // Net pay
        parameters.put("netPay", df.format((Double) currentSalaryData.get("netPay")));
        
        // Report generation info
        parameters.put("generatedDate", new SimpleDateFormat("MMMM dd, yyyy").format(new Date()));
        parameters.put("generatedTime", new SimpleDateFormat("hh:mm a").format(new Date()));
        
        return parameters;
    }
    
    private void goBack() {
        dispose();
        new HRSearchEmployeePage().setVisible(true);
    }
}
