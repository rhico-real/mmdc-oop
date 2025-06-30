package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import Classes.User;
import GUI.admin.DashboardPage;
import GUI.employee.EmployeeDashboard;
import GUI.hr.HRDashboard;
import GUI.finance.FinanceDashboard;
import java.io.IOException;
import java.io.File;

@SuppressWarnings("serial")
public class LoginPage extends JFrame {
	private JLabel titleLabel = new JLabel("MotorPH Payroll");
	private JLabel descriptionLabel = new JLabel("Log in your account");
	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JLabel usernameLabel = new JLabel("    Username");
	private JLabel passwordLabel = new JLabel("    Password");
	private JButton loginButton = new JButton("Login");
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;

	// import poppins font
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

	public LoginPage() {

		// Set up the JFrame
		setTitle("MotorPH Portal");
		setSize(1366, 768); setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// custom font
		Font poppinsHeader = loadCustomFont("resources/fonts/Poppins-Bold.ttf", 60f);
		Font poppinsSubHeader = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf",16f);
		Font poppinsText = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 14f);
		
		// Setup Primary JPanel
		JPanel container = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Left subpanel 
		JPanel leftpanel = new JPanel();
        leftpanel.setBackground(Color.decode("#f5f5f5")); //#27374D
        // Left subpanel padding
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(leftpanel, gbc);
        
        // Right subpanel
        JPanel rightpanel = new JPanel(new GridBagLayout());
        rightpanel.setBackground(Color.decode("#f5f5f5"));
        // Right subpanel padding
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(rightpanel, gbc);

        add(container);
        setVisible(true);
        // Login Page Background Image 
        ImageIcon icon = new ImageIcon("resources/images/mph-bg-real.png");
        JLabel imageLabel = new JLabel(icon);
        leftpanel.add(imageLabel);
		
		// Set preferred size for JTextField and JLabel
        titleLabel.setPreferredSize(new Dimension (300,700)); 
        descriptionLabel.setPreferredSize(new Dimension (300,200));
        
		Dimension fieldSize = new Dimension(300,120);
		usernameLabel.setPreferredSize(fieldSize); 
		usernameField.setPreferredSize(fieldSize);
		passwordLabel.setPreferredSize(fieldSize); 
		passwordField.setPreferredSize(fieldSize);

		// Adjust font size of JLabels
		titleLabel.setFont(poppinsHeader); 
		descriptionLabel.setFont(poppinsSubHeader);
		usernameLabel.setFont(poppinsText); 
		usernameField.setFont(poppinsText);
		passwordLabel.setFont(poppinsText); 
		passwordField.setFont(poppinsText);
		loginButton.setFont(poppinsText); 
		
		// Set font color for labels
		titleLabel.setForeground(Color.decode("#153969"));
		usernameLabel.setForeground(Color.GRAY);  
		passwordLabel.setForeground(Color.GRAY);  


		// round text field border
		Border outerBorder = new LineBorder(Color.GRAY, 2, true); // outer border
		Border innerPadding = new EmptyBorder(5, 9, 5, 10); // inner border
		usernameField.setBorder(new CompoundBorder(outerBorder, innerPadding));
		passwordField.setBorder(new CompoundBorder(outerBorder, innerPadding));
		
		loginButton.setBorder(new LineBorder(Color.decode("#153969"),8, true));
		
		GridBagConstraints gbc_titleLabel = new GridBagConstraints();
		gbc_titleLabel.gridx = 0;
		gbc_titleLabel.gridy = 0;
		gbc_titleLabel.anchor = GridBagConstraints.WEST;
		gbc_titleLabel.insets = new Insets(0, -80, 40, 0);
		rightpanel.add(titleLabel, gbc_titleLabel);
		
		GridBagConstraints gbc_descriptionLabel = new GridBagConstraints();
		gbc_descriptionLabel.gridx = 0;
		gbc_descriptionLabel.gridy = 1;
		gbc_descriptionLabel.anchor = GridBagConstraints.WEST;
		gbc_descriptionLabel.insets = new Insets(0, -80, 15, 0);
		rightpanel.add(descriptionLabel, gbc_descriptionLabel);

		// Add components to the JPanel with GridBagLayout
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.gridx = 0;
		gbc_usernameLabel.gridy = 2;
		gbc_usernameLabel.anchor = GridBagConstraints.WEST;
		gbc_usernameLabel.insets = new Insets(0, -80, 0, 150);
		rightpanel.add(usernameLabel, gbc_usernameLabel);		

		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 3;
		gbc_passwordLabel.anchor = GridBagConstraints.WEST;
		gbc_passwordLabel.insets = new Insets(10, -80, 50, 150); // padding 
		rightpanel.add(passwordLabel, gbc_passwordLabel);

		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameField.gridx = 0;
		gbc_usernameField.gridy = 2;
		gbc_usernameField.insets = new Insets(0, -80, 0, 150);
		rightpanel.add(usernameField, gbc_usernameField);
		
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 0;
		gbc_passwordField.gridy = 3;
		gbc_passwordField.insets = new Insets(10,-80, 50, 150);
		rightpanel.add(passwordField, gbc_passwordField);		
										
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.fill = GridBagConstraints.BOTH;
		gbc_loginButton.gridx = 0;
		gbc_loginButton.gridy = 4;		
		gbc_loginButton.insets = new Insets(0, -80, 0, 150);
		loginButton.setBackground(Color.decode("#153969")); 
		loginButton.setForeground(Color.WHITE); 
		rightpanel.add(loginButton, gbc_loginButton);			

		// Add ActionListener to the login button
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkLoginCredentials();
			}
		});

		// Attach the ActionListener for each field available
		JTextField[] fields = { usernameField, passwordField };
		for (JTextField field : fields) {
			field.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					checkLoginCredentials();
				}
			});
		}
	}
	

	public void checkLoginCredentials() {
		
		// Create a User object with the login credentials
		User userInfo = new User(usernameField.getText(), new String(passwordField.getPassword()));

		// Show dialog if the user credentials provided are incorrect.
		if (userInfo.getLoginStatus().equals(false)) {
			JOptionPane.showMessageDialog(new JFrame(""), "User credentials incorrect.", "Login Failed",
					JOptionPane.ERROR_MESSAGE);

			return;
		}
		
		dispose(); // Close the last page
		
		java.awt.EventQueue.invokeLater(new Runnable() {
		
			public void run() {
			
				if (userInfo.getIsAdmin()) {
				// Proceed to the next page once logged in
				// Create and display the form
				new DashboardPage().setVisible(true); 
				} 
				else if (userInfo.getIsHR()) {
				// If user is HR, go to Finance dashboard for CRUD operations
				new GUI.finance.FinanceDashboard().setVisible(true);
				}
		else if (userInfo.getIsFinance()) {
			// If user is Finance, go to HR dashboard for payslip creation
			new GUI.hr.HRDashboard().setVisible(true);
		}
			else {
				// Call constructor
				employeeGI = new GovernmentIdentification(userInfo.getEmployeeNumber());
				employeeComp = new Compensation(userInfo.getEmployeeNumber());

				// Set all the data for the logged in employee
				EmployeeInformation.setEmployeeInformationObject(userInfo.getEmployeeNumber(), employeeGI,
				   employeeComp);

				// If user is an employee, go to employee dashboard page
				new EmployeeDashboard(employeeGI, employeeComp).setVisible(true);
			} // end of else-block
			} // end of run()
		}); // end of .invokeLater()
	} // end of checkLoginCredentials()

}
