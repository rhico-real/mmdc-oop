package GUI.hr;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class HRDashboard extends JFrame {
    
    public HRDashboard() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("FINANCE Dashboard - MotorPH Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(77, 77, 105));
        titlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("HR Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(30, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to HR Management System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentPanel.add(welcomeLabel, gbc);
        
        // Search Employee button
        JButton searchEmployeeBtn = createStyledButton("Search Employee & Create Payslip");
        searchEmployeeBtn.addActionListener(e -> openSearchEmployee());
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(searchEmployeeBtn, gbc);
        
        // View All Employees button
        JButton viewAllEmployeesBtn = createStyledButton("View All Employees & Payslips");
        viewAllEmployeesBtn.addActionListener(e -> openViewAllEmployees());
        gbc.gridx = 1; gbc.gridy = 1;
        contentPanel.add(viewAllEmployeesBtn, gbc);
        
        // Logout button
        JButton logoutBtn = createStyledButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.addActionListener(e -> logout());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(logoutBtn, gbc);
        
        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(40, 167, 69));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(250, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (text.equals("Logout")) {
                    button.setBackground(new Color(220, 53, 69));
                } else {
                    button.setBackground(new Color(40, 167, 69));
                }
            }
        });
        
        return button;
    }
    
    private void openSearchEmployee() {
        dispose();
        new HRSearchEmployeePage().setVisible(true);
    }
    
    private void openViewAllEmployees() {
        dispose();
        new HRViewAllEmployeesPage().setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new GUI.LoginPage().setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HRDashboard().setVisible(true);
        });
    }
}
