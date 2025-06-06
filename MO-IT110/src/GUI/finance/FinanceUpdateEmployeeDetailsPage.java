package GUI.finance;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.*;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import DAO.EmployeeDAO;
import DAO.UserDAO;
import UtilityClasses.DataValidators;

@SuppressWarnings("serial")
public class FinanceUpdateEmployeeDetailsPage extends JFrame {

    private GovernmentIdentification employeeGI;
    private Compensation employeeComp;
    
    private JTextField employeeNumberField, firstNameField, lastNameField, birthdayField, addressField;
    private JTextField phoneNumberField, sssField, philhealthField, tinField, pagibigField;
    private JTextField statusField, positionField, immediateSupervisorField;
    private JTextField basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField;
    private JTextField grossSemiMonthlyRateField, hourlyRateField;
    
    public FinanceUpdateEmployeeDetailsPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
        this.employeeGI = employeeGI;
        this.employeeComp = employeeComp;
        initComponents();
    }

    private void initComponents() {
        setTitle("MotorPH Payroll System | Finance - Update Employee Details");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Initialize fields with existing data
        employeeNumberField = new JTextField(employeeGI.getEmployeeNumber());
        employeeNumberField.setEditable(false); // Employee number cannot be changed
        
        firstNameField = new JTextField(employeeGI.getFirstName());
        lastNameField = new JTextField(employeeGI.getLastName());
        birthdayField = new JTextField(employeeGI.getBirthday());
        addressField = new JTextField(employeeGI.getAddress());
        phoneNumberField = new JTextField(employeeGI.getPhoneNumber());
        sssField = new JTextField(employeeGI.getSSSNumber());
        philhealthField = new JTextField(employeeGI.getPhilHealthNumber());
        tinField = new JTextField(employeeGI.getTinNumber());
        pagibigField = new JTextField(employeeGI.getPagibigNumber());
        statusField = new JTextField(employeeGI.getStatus());
        positionField = new JTextField(employeeGI.getPosition());
        immediateSupervisorField = new JTextField(employeeGI.getSupervisor());
        
        basicSalaryField = new JTextField(String.format("%.2f", employeeComp.getBasicSalary()));
        riceSubsidyField = new JTextField(String.format("%.2f", employeeComp.getRiceSubsidy()));
        phoneAllowanceField = new JTextField(String.format("%.2f", employeeComp.getPhoneAllowance()));
        clothingAllowanceField = new JTextField(String.format("%.2f", employeeComp.getClothingAllowance()));
        grossSemiMonthlyRateField = new JTextField(String.format("%.2f", employeeComp.getGrossSemiMonthlyRate()));
        hourlyRateField = new JTextField(String.format("%.2f", employeeComp.getHourlyRate()));

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder("Update Employee Information"));
        JLabel headerLabel = new JLabel("Edit Employee Details - " + employeeGI.getFirstName() + " " + employeeGI.getLastName(), JLabel.CENTER);
        headerLabel.setFont(headerLabel.getFont().deriveFont(16.0f));
        headerPanel.add(headerLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(10, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components to panel
        formPanel.add(new JLabel("Employee Number:"));
        formPanel.add(employeeNumberField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneNumberField);

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("SSS Number:"));
        formPanel.add(sssField);

        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("PhilHealth Number:"));
        formPanel.add(philhealthField);

        formPanel.add(new JLabel("Birthday:"));
        formPanel.add(birthdayField);
        formPanel.add(new JLabel("TIN Number:"));
        formPanel.add(tinField);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Pag-ibig Number:"));
        formPanel.add(pagibigField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(positionField);

        formPanel.add(new JLabel("Supervisor:"));
        formPanel.add(immediateSupervisorField);
        formPanel.add(new JLabel("Basic Salary:"));
        formPanel.add(basicSalaryField);

        formPanel.add(new JLabel("Rice Subsidy:"));
        formPanel.add(riceSubsidyField);
        formPanel.add(new JLabel("Phone Allowance:"));
        formPanel.add(phoneAllowanceField);

        formPanel.add(new JLabel("Clothing Allowance:"));
        formPanel.add(clothingAllowanceField);
        formPanel.add(new JLabel("Gross Semi-Monthly:"));
        formPanel.add(grossSemiMonthlyRateField);

        formPanel.add(new JLabel("Hourly Rate:"));
        formPanel.add(hourlyRateField);
        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Changes");

        cancelButton.addActionListener(e -> {
            dispose();
            new FinanceViewEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
        });

        saveButton.addActionListener(e -> saveChanges());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add tooltips
        birthdayField.setToolTipText("Format: MM/dd/yyyy (e.g., 05/25/1990)");
        sssField.setToolTipText("Format: xx-xxxxxxx-x");
        philhealthField.setToolTipText("Max Length: 12");
        tinField.setToolTipText("Format: xxx-xxx-xxx-xxx");
        pagibigField.setToolTipText("Max Length: 12");
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void saveChanges() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (!validateAndUpdateEmployee(errorMessage)) {
            JOptionPane.showMessageDialog(this, errorMessage.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, "Employee information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new FinanceEmployeeListPage().setVisible(true);
    }
    
    private boolean validateAndUpdateEmployee(StringBuilder errorMessage) {
        JTextField[] fields = {
            employeeNumberField, lastNameField, firstNameField, birthdayField, addressField,
            phoneNumberField, sssField, philhealthField, tinField, pagibigField, statusField, positionField,
            immediateSupervisorField, basicSalaryField, riceSubsidyField, phoneAllowanceField,
            clothingAllowanceField, grossSemiMonthlyRateField, hourlyRateField
        };
        
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
            errorMessage.append("Please enter a valid date for birthday in format MM/dd/yyyy.");
            return false;
        }
        
        // Create updated employee objects
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
        
        // Update in database
        boolean updated = EmployeeDAO.updateEmployee(employee, govId, compensation);
        
        if (updated) {
            // Update username in the users table
            String username = (firstNameField.getText() + "." + lastNameField.getText()).toLowerCase();
            UserDAO.updateUsername(employeeNumberField.getText(), username);
        }
        
        if (!updated) {
            errorMessage.append("Failed to update employee in database.");
            return false;
        }
        
        return true;
    }
}
