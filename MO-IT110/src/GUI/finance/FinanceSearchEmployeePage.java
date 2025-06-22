package GUI.finance;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import Classes.EmployeeInformation;
import DAO.EmployeeDAO;

@SuppressWarnings("serial")
public class FinanceSearchEmployeePage extends JFrame {

    private JTextField searchField;
    private JButton searchButton, goBackButton;
    private JList<String> resultsList;
    private DefaultListModel<String> listModel;

    public FinanceSearchEmployeePage() {
        initComponents();
    }

    private void initComponents() {
        setTitle("MotorPH Payroll System | Finance - Search Employee");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1366,768);
        setLocationRelativeTo(null);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        goBackButton = new JButton("Go Back to Dashboard");

        searchPanel.add(new JLabel("Search Employee:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Results panel
        listModel = new DefaultListModel<>();
        resultsList = new JList<>(listModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultsList);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(goBackButton);

        // Layout
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FinanceDashboard().setVisible(true);
            }
        });

        resultsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewSelectedEmployee();
                }
            }
        });
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<EmployeeInformation> employees = EmployeeDAO.searchEmployeesByName(searchTerm);
            listModel.clear();

            if (employees.isEmpty()) {
                listModel.addElement("No employees found matching '" + searchTerm + "'");
            } else {
                for (EmployeeInformation emp : employees) {
                    String displayText = emp.getEmployeeNumber() + " - " + emp.getLastName() + ", " + emp.getFirstName();
                    if (emp.getPosition() != null) {
                        displayText += " (" + emp.getPosition() + ")";
                    }
                    listModel.addElement(displayText);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewSelectedEmployee() {
        String selectedValue = resultsList.getSelectedValue();
        if (selectedValue == null || selectedValue.startsWith("No employees")) {
            return;
        }

        // Extract employee number from the selected value
        String employeeNumber = selectedValue.split(" - ")[0];
        
        try {
            Classes.GovernmentIdentification govId = DAO.EmployeeDAO.getEmployeeGovId(employeeNumber);
            Classes.Compensation comp = DAO.EmployeeDAO.getEmployeeCompensation(employeeNumber);
            
            if (govId != null && comp != null) {
                dispose();
                new FinanceViewEmployeeDetailsPage(govId, comp).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Could not load employee details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employee details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
