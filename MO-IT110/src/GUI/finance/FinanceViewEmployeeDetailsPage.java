package GUI.finance;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

import Classes.Compensation;
import Classes.GovernmentIdentification;

@SuppressWarnings("serial")
public class FinanceViewEmployeeDetailsPage extends JFrame {

    private GovernmentIdentification employeeGI;
    private Compensation employeeComp;

    public FinanceViewEmployeeDetailsPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
        this.employeeGI = employeeGI;
        this.employeeComp = employeeComp;
        initComponents();
    }

    private void initComponents() {
        setTitle("MotorPH Payroll System | Finance - View Employee Details");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        JLabel headerLabel = new JLabel("Employee Details - " + employeeGI.getFirstName() + " " + employeeGI.getLastName(), JLabel.CENTER);
        headerLabel.setFont(headerLabel.getFont().deriveFont(16.0f));
        headerPanel.add(headerLabel);

        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(10, 4, 10, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add employee information
        addDetailRow(detailsPanel, "Employee Number:", employeeGI.getEmployeeNumber(), "Phone Number:", employeeGI.getPhoneNumber());
        addDetailRow(detailsPanel, "First Name:", employeeGI.getFirstName(), "SSS Number:", employeeGI.getSSSNumber());
        addDetailRow(detailsPanel, "Last Name:", employeeGI.getLastName(), "PhilHealth Number:", employeeGI.getPhilHealthNumber());
        addDetailRow(detailsPanel, "Birthday:", employeeGI.getBirthday(), "TIN Number:", employeeGI.getTinNumber());
        addDetailRow(detailsPanel, "Address:", employeeGI.getAddress(), "Pag-ibig Number:", employeeGI.getPagibigNumber());
        addDetailRow(detailsPanel, "Position:", employeeGI.getPosition(), "Status:", employeeGI.getStatus());
        addDetailRow(detailsPanel, "Supervisor:", employeeGI.getSupervisor(), "Basic Salary:", String.format("%.2f", employeeComp.getBasicSalary()));
        addDetailRow(detailsPanel, "Rice Subsidy:", String.format("%.2f", employeeComp.getRiceSubsidy()), "Phone Allowance:", String.format("%.2f", employeeComp.getPhoneAllowance()));
        addDetailRow(detailsPanel, "Clothing Allowance:", String.format("%.2f", employeeComp.getClothingAllowance()), "Gross Semi-Monthly:", String.format("%.2f", employeeComp.getGrossSemiMonthlyRate()));
        addDetailRow(detailsPanel, "Hourly Rate:", String.format("%.2f", employeeComp.getHourlyRate()), "", "");

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton goBackButton = new JButton("Go Back to Employee List");
        JButton editButton = new JButton("Edit Employee");

        goBackButton.addActionListener(e -> {
            dispose();
            new FinanceEmployeeListPage().setVisible(true);
        });

        editButton.addActionListener(e -> {
            dispose();
            new FinanceUpdateEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
        });

        buttonPanel.add(goBackButton);
        buttonPanel.add(editButton);

        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void addDetailRow(JPanel panel, String label1, String value1, String label2, String value2) {
        panel.add(new JLabel(label1));
        panel.add(new JLabel(value1 != null ? value1 : ""));
        panel.add(new JLabel(label2));
        panel.add(new JLabel(value2 != null ? value2 : ""));
    }
}
