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

@SuppressWarnings("serial")
public class LoginPage extends JFrame {
	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JLabel usernameLabel = new JLabel("Username");
	private JLabel passwordLabel = new JLabel("Password");
	private JButton loginButton = new JButton("Login");
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;

	public LoginPage() {

		// Set up the JFrame
		setTitle("MotorPH Portal");
		setSize(400, 200); setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);		

		// Create a JPanel with EmptyBorder for padding
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Adjust the padding values as needed
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] {80, 220, 20};
		gbl_mainPanel.rowHeights = new int[] {36, 36, 36};
		mainPanel.setLayout(gbl_mainPanel);
		setContentPane(mainPanel);
		
		// Set preferred size for JTextField and JLabel
		Dimension labelSize = new Dimension(75, 30);
		Dimension fieldSize = new Dimension(200, 30);

		// Increase font size of JLabels
		Font myFont = new Font("Tahoma", Font.PLAIN, 14);
		usernameLabel.setFont(myFont); usernameField.setFont(myFont);
		passwordLabel.setFont(myFont); passwordField.setFont(myFont);
		loginButton.setFont(myFont);

		// Set preferred size for labels and fields
		usernameLabel.setPreferredSize(labelSize); usernameField.setPreferredSize(fieldSize);
		passwordLabel.setPreferredSize(labelSize); passwordField.setPreferredSize(fieldSize);

		// Add components to the JPanel with GridBagLayout
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.gridx = 0;
		gbc_usernameLabel.gridy = 0;
		mainPanel.add(usernameLabel, gbc_usernameLabel);		

		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.gridx = 0;
		gbc_passwordLabel.gridy = 1;
		mainPanel.add(passwordLabel, gbc_passwordLabel);

		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameField.gridx = 1;
		gbc_usernameField.gridy = 0;
		mainPanel.add(usernameField, gbc_usernameField);
		
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 1;
		mainPanel.add(passwordField, gbc_passwordField);		
										
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.fill = GridBagConstraints.BOTH;
		gbc_loginButton.gridx = 1;
		gbc_loginButton.gridy = 2;		
		mainPanel.add(loginButton, gbc_loginButton);			

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
				// If user is HR, go to HR dashboard
				new HRDashboard().setVisible(true);
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
