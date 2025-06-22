package GUI.finance;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import DAO.EmployeeDAO;
import UtilityClasses.CustomTooltip;
import UtilityClasses.DataValidators;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class FinanceAddEmployeePage extends JFrame {

    private JTextField employeeNumberField, firstNameField, lastNameField, birthdayField, addressField;
    private JTextField phoneNumberField, sssField, philhealthField, tinField, pagibigField;
    private JTextField statusField, positionField, immediateSupervisorField;
    private JTextField basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField;
    private JTextField grossSemiMonthlyRateField, hourlyRateField;
    private JButton goBackButton, confirmButton;

    public FinanceAddEmployeePage() {
        initComponents();
    }

    private void initComponents() {
        setTitle("MotorPH Payroll System | Finance - Add Employee");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1366,768);
        setResizable(false);
        
        // Initialize fields
        String employeeNum = generateNextEmployeeNumber();
        employeeNumberField = new JTextField(employeeNum);
        employeeNumberField.setEditable(false);
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        birthdayField = new JTextField();
        addressField = new JTextField();
        phoneNumberField = new JTextField();
        sssField = new JTextField();
        philhealthField = new JTextField();
        tinField = new JTextField();
        pagibigField = new JTextField();
        statusField = new JTextField();
        positionField = new JTextField();
        immediateSupervisorField = new JTextField();
        basicSalaryField = new JTextField();
        riceSubsidyField = new JTextField();
        phoneAllowanceField = new JTextField();
        clothingAllowanceField = new JTextField();
        grossSemiMonthlyRateField = new JTextField();
        hourlyRateField = new JTextField();

        // Create main panel
        JPanel mainPanel = new JPanel(new GridLayout(10, 4, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components to panel
        mainPanel.add(new JLabel("Employee Number:"));
        mainPanel.add(employeeNumberField);
        mainPanel.add(new JLabel("Phone Number:"));
        mainPanel.add(phoneNumberField);

        mainPanel.add(new JLabel("First Name:"));
        mainPanel.add(firstNameField);
        mainPanel.add(new JLabel("SSS Number:"));
        mainPanel.add(sssField);

        mainPanel.add(new JLabel("Last Name:"));
        mainPanel.add(lastNameField);
        mainPanel.add(new JLabel("PhilHealth Number:"));
        mainPanel.add(philhealthField);

        mainPanel.add(new JLabel("Birthday:"));
        mainPanel.add(birthdayField);
        mainPanel.add(new JLabel("TIN Number:"));
        mainPanel.add(tinField);

        mainPanel.add(new JLabel("Address:"));
        mainPanel.add(addressField);
        mainPanel.add(new JLabel("Pag-ibig Number:"));
        mainPanel.add(pagibigField);

        mainPanel.add(new JLabel("Status:"));
        mainPanel.add(statusField);
        mainPanel.add(new JLabel("Position:"));
        mainPanel.add(positionField);

        mainPanel.add(new JLabel("Supervisor:"));
        mainPanel.add(immediateSupervisorField);
        mainPanel.add(new JLabel("Basic Salary:"));
        mainPanel.add(basicSalaryField);

        mainPanel.add(new JLabel("Rice Subsidy:"));
        mainPanel.add(riceSubsidyField);
        mainPanel.add(new JLabel("Phone Allowance:"));
        mainPanel.add(phoneAllowanceField);

        mainPanel.add(new JLabel("Clothing Allowance:"));
        mainPanel.add(clothingAllowanceField);
        mainPanel.add(new JLabel("Gross Semi-Monthly:"));
        mainPanel.add(grossSemiMonthlyRateField);

        mainPanel.add(new JLabel("Hourly Rate:"));
        mainPanel.add(hourlyRateField);
        mainPanel.add(new JLabel(""));
        mainPanel.add(new JLabel(""));

        // Buttons
        goBackButton = new JButton("Go Back to Employee List");
        confirmButton = new JButton("Confirm");

        goBackButton.addActionListener(e -> {
            dispose();
            new FinanceEmployeeListPage().setVisible(true);
        });

        confirmButton.addActionListener(e -> confirmButtonActionPerformed());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(goBackButton);
        buttonPanel.add(confirmButton);

        add(mainPanel, "Center");
        add(buttonPanel, "South");

        addTooltips();
        pack();
        setLocationRelativeTo(null);
    }

    private String generateNextEmployeeNumber() {
        try {
            List<EmployeeInformation> employees = EmployeeDAO.getAllEmployees();
            int maxEmployeeNum = 10000;
            for (EmployeeInformation emp : employees) {
                try {
                    int currentEmpNum = Integer.parseInt(emp.getEmployeeNumber());
                    if (currentEmpNum > maxEmployeeNum) {
                        maxEmployeeNum = currentEmpNum;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid numbers
                }
            }
            return String.valueOf(maxEmployeeNum + 1);
        } catch (Exception e) {
            return "10001";
        }
    }

    private void addTooltips() {
        phoneNumberField.setToolTipText("Format: xxx-xxx-xxx");
        sssField.setToolTipText("Format: xx-xxxxxxx-x");
        philhealthField.setToolTipText("Max Length: 12");
        tinField.setToolTipText("Format: xxx-xxx-xxx-xxx");
        pagibigField.setToolTipText("Max Length: 12");
        birthdayField.setToolTipText("Format: MM/dd/yyyy (e.g., 05/25/1990)");
    }

    private void confirmButtonActionPerformed() {
        StringBuilder errorMessage = new StringBuilder();

        if (!validateAndCreateEmployee(errorMessage)) {
            JOptionPane.showMessageDialog(this, errorMessage.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Employee created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new FinanceEmployeeListPage().setVisible(true);
    }

    private boolean validateAndCreateEmployee(StringBuilder errorMessage) {
        JTextField[] fields = {employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
                phoneNumberField, sssField, philhealthField, tinField, pagibigField, statusField, positionField,
                immediateSupervisorField, basicSalaryField, riceSubsidyField, phoneAllowanceField,
                clothingAllowanceField, grossSemiMonthlyRateField, hourlyRateField};

        JTextField[] stringOnlyFields = {lastNameField, firstNameField, positionField, immediateSupervisorField, statusField};
        JTextField[] numericFields = {basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, grossSemiMonthlyRateField, hourlyRateField};

        // Validation
        if (Arrays.stream(fields).anyMatch(field -> field.getText().trim().isEmpty())) {
            errorMessage.append("Please fill in all fields.");
            return false;
        }

        if (Arrays.stream(numericFields).anyMatch(field -> !DataValidators.isNumeric(field.getText()))) {
            errorMessage.append("Please enter valid numeric values for salary fields.");
            return false;
        }

        if (Arrays.stream(stringOnlyFields).anyMatch(field -> !DataValidators.isPureString(field.getText()))) {
            errorMessage.append("Please enter valid characters only for name fields.");
            return false;
        }

        if (!DataValidators.isValidDate(birthdayField.getText())) {
            errorMessage.append("Please enter a valid date for birthday.");
            return false;
        }

        // Create employee objects
        EmployeeInformation employee = new EmployeeInformation(employeeNumberField.getText());
        employee.setFirstName(firstNameField.getText());
        employee.setLastName(lastNameField.getText());
        
        // Convert date format from MM/dd/yyyy to yyyy-MM-dd for SQL
        try {
            String birthdayText = birthdayField.getText();
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = inputFormat.parse(birthdayText);
            String formattedDate = outputFormat.format(date);
            employee.setBirthday(formattedDate);
        } catch (Exception e) {
            errorMessage.append("Invalid date format. Please use MM/dd/yyyy format.");
            return false;
        }
        
        employee.setAddress(addressField.getText());
        employee.setPhoneNumber(phoneNumberField.getText());
        employee.setPosition(positionField.getText());
        employee.setStatus(statusField.getText());
        employee.setSupervisor(immediateSupervisorField.getText());
        employee.setHourlyRate(Double.parseDouble(hourlyRateField.getText()));
        

        GovernmentIdentification govId = new GovernmentIdentification(employeeNumberField.getText());
        govId.setSSSNumber(sssField.getText());
        govId.setPhilHealthNumber(philhealthField.getText());
        govId.setTinNumber(tinField.getText());
        govId.setPagibigNumber(pagibigField.getText());

        Compensation compensation = new Compensation(employeeNumberField.getText());
        compensation.setBasicSalary(Double.parseDouble(basicSalaryField.getText()));
        compensation.setRiceSubsidy(Double.parseDouble(riceSubsidyField.getText()));
        compensation.setPhoneAllowance(Double.parseDouble(phoneAllowanceField.getText()));
        compensation.setClothingAllowance(Double.parseDouble(clothingAllowanceField.getText()));
        compensation.setGrossSemiMonthlyRate(Double.parseDouble(grossSemiMonthlyRateField.getText()));
        compensation.setHourlyRate(Double.parseDouble(hourlyRateField.getText()));

        // Generate credentials
        String username = (firstNameField.getText() + "." + lastNameField.getText()).toLowerCase();
        String password = "password" + employeeNumberField.getText();

        // Create in database
        boolean success = EmployeeDAO.createEmployee(employee, govId, compensation, username, password, positionField.getText(), "General");

        if (!success) {
            errorMessage.append("Failed to create employee in database.");
            return false;
        }

        return true;
    }
}
