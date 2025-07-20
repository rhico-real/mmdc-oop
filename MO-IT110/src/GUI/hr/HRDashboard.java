package GUI.hr;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public class HRDashboard extends JFrame {
    
    public HRDashboard() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Human Resource Dashboard - MotorPH Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setResizable(false);
        
        String navyBlue = "#153969";
        String lightGray = "#f5f5f5";
        String lightRed ="#ff5757";
        
        Font poppinsRegular14f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 14f);
        Font poppinsRegular24f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 24f);
        Font poppinsBold40f = loadCustomFont("resources/fonts/Poppins-Bold.ttf", 45f);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#f5f5f5"));
        
        // Nav Bar Panel
        JPanel navBarPanel = new JPanel(new GridBagLayout());
        navBarPanel.setBackground(Color.decode("#153969"));
        navBarPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        GridBagConstraints navBarPanelGBC = new GridBagConstraints();
        navBarPanelGBC.gridx = 0;
        navBarPanelGBC.gridy = 0;
        navBarPanelGBC.fill = GridBagConstraints.BOTH;
        mainPanel.add(navBarPanel, navBarPanelGBC);
        
        // motorph logo
        ImageIcon motorphlogoAdmin = new ImageIcon("resources/images/motorph-logo-white.png");
        JLabel motorPHLogo = new JLabel(motorphlogoAdmin);
        GridBagConstraints motorPHLogoGBC = new GridBagConstraints();
        motorPHLogoGBC.gridx = 0;
        motorPHLogoGBC.gridy = 0;
        motorPHLogoGBC.insets = new Insets(-30, -100, 0, 950);
        navBarPanel.add(motorPHLogo, motorPHLogoGBC);
        
        // logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.decode(lightGray));
        logoutButton.setForeground(Color.decode(navyBlue));
        logoutButton.setPreferredSize(new Dimension(85,35));
        logoutButton.setFont(poppinsRegular14f);
        logoutButton.addActionListener(e -> logout());
        GridBagConstraints logoutButtonGBC = new GridBagConstraints();
        logoutButtonGBC.gridx = 1; 
        logoutButtonGBC.gridy = 0; 
        logoutButtonGBC.anchor = GridBagConstraints.CENTER;
        navBarPanel.add(logoutButton, logoutButtonGBC);
        
        // finance logo
        ImageIcon financeLogo = new ImageIcon("resources/images/Finance-Logo.png");
        JLabel financeLogoLabel = new JLabel(financeLogo);
        GridBagConstraints financeLogoLabelGBC = new GridBagConstraints();
        financeLogoLabelGBC.gridx = 2;
        financeLogoLabelGBC.gridy = 0;
        financeLogoLabelGBC.insets = new Insets(0, -15, 0, -75);
        navBarPanel.add(financeLogoLabel, financeLogoLabelGBC);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints contentPanelGBC = new GridBagConstraints();
        contentPanelGBC.gridx = 0;
        contentPanelGBC.gridy = 1;
        contentPanelGBC.weightx = 1;
        contentPanelGBC.weighty = 0.9;
        contentPanelGBC.fill = GridBagConstraints.BOTH;
        mainPanel.add(contentPanel, contentPanelGBC);
        
        // left content panel
        JPanel leftContentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints leftContentPanelGBC = new GridBagConstraints();
        leftContentPanelGBC.gridx = 0;
        leftContentPanelGBC.gridy = 0;
        leftContentPanelGBC.weightx = 0.5;
        leftContentPanelGBC.weighty = 1;
        leftContentPanelGBC.fill = GridBagConstraints.BOTH;
        contentPanel.add(leftContentPanel, leftContentPanelGBC);
        
        // right content panel
        JPanel rightContentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rightContentPanelGBC = new GridBagConstraints();
        rightContentPanelGBC.gridx = 1;
        rightContentPanelGBC.gridy = 0;
        rightContentPanelGBC.weightx = 0.5;
        rightContentPanelGBC.weighty = 1;
        rightContentPanelGBC.fill = GridBagConstraints.BOTH;
        contentPanel.add(rightContentPanel, rightContentPanelGBC);
        
        // Title
        JLabel titleLabel = new JLabel("<html><b>Human Resource<br>Management<br>System<br><br>Welcome!</b></html>");
        titleLabel.setFont(poppinsBold40f);
        titleLabel.setForeground(Color.decode(navyBlue));
        GridBagConstraints titleLabelGBC = new GridBagConstraints();
        titleLabelGBC.gridx = 0;
        titleLabelGBC.gridy = 0;
        titleLabelGBC.insets = new Insets (-50,15,0,0);
        titleLabelGBC.anchor = GridBagConstraints.WEST;
        leftContentPanel.add(titleLabel,titleLabelGBC);
        
        // search employee button
        JButton searchEmployeeButton = new JButton("Search Employee and Create Payslip");
        searchEmployeeButton.addActionListener(e -> openSearchEmployee());
        searchEmployeeButton.setPreferredSize(new Dimension(500,120));
        searchEmployeeButton.setBorder(new LineBorder(Color.GRAY,2, true));
        searchEmployeeButton.setFont(poppinsRegular24f);
        searchEmployeeButton.setBackground(Color.WHITE);
        GridBagConstraints searchEmployeeButtonGBC = new GridBagConstraints();
        searchEmployeeButtonGBC.gridx = 0; 
        searchEmployeeButtonGBC.gridy = 0; 
        searchEmployeeButtonGBC.insets = new Insets (0,0,15,0);
        searchEmployeeButtonGBC.fill = GridBagConstraints.HORIZONTAL;
        rightContentPanel.add(searchEmployeeButton, searchEmployeeButtonGBC);
        
        // view all employees and payslips button
        JButton viewAllEmployeesButtonn = new JButton("View All Employees and Payslips");
        viewAllEmployeesButtonn.addActionListener(e -> openViewAllEmployees());
        viewAllEmployeesButtonn.setPreferredSize(new Dimension(500,120));
        viewAllEmployeesButtonn.setBorder(new LineBorder(Color.GRAY,2, true));
        viewAllEmployeesButtonn.setFont(poppinsRegular24f);
        viewAllEmployeesButtonn.setBackground(Color.WHITE);
        GridBagConstraints viewAllEmployeesButtonGBC = new GridBagConstraints();
        viewAllEmployeesButtonGBC.gridx = 0; 
        viewAllEmployeesButtonGBC.gridy = 1; 
        viewAllEmployeesButtonGBC.insets = new Insets (15,0,0,0);
        viewAllEmployeesButtonGBC.fill = GridBagConstraints.HORIZONTAL;
        rightContentPanel.add(viewAllEmployeesButtonn, viewAllEmployeesButtonGBC);
        
        // assemble jframe
        add(mainPanel);
		pack();
		setSize(1366,788);
		setVisible(true);
		setLocationRelativeTo(null);
    }
    
    // custom font
    private static Font loadCustomFont(String fontPath, float size) {
	    try {
	        Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(size);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(font);
	        return font;
	    } catch (FontFormatException | IOException e) {
	        System.err.println("Error loading font: " + e.getMessage());
	        return null;
	    }
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
