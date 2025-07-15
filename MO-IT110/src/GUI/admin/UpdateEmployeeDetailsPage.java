package GUI.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import DAO.EmployeeDAO;
import DAO.UserDAO;
import UtilityClasses.CustomTooltip;
import UtilityClasses.DataValidators;

@SuppressWarnings("serial")
public class UpdateEmployeeDetailsPage extends JFrame {

	// Variables declaration - do not modify
	private static javax.swing.JTextField addressField;
	private javax.swing.JLabel addressLabel;
	private static javax.swing.JTextField basicSalaryField;
	private javax.swing.JLabel basicSalaryLabel;
	private static javax.swing.JTextField birthdayField;
	private javax.swing.JLabel birthdayLabel;
	private static javax.swing.JTextField clothingAllowanceField;
	private javax.swing.JLabel clothingAllowanceLabel;
	private javax.swing.JTextField employeeNumberField;
	private javax.swing.JLabel employeeNumberLabel;
	private static javax.swing.JTextField firstNameField;
	private javax.swing.JLabel firstNameLabel;
	private javax.swing.JButton goBackToEmployeeListButton;
	private static javax.swing.JTextField grossSemiMonthlyRateField;
	private javax.swing.JLabel grossSemiMonthlyRateLabel;
	private static javax.swing.JTextField hourlyRateField;
	private javax.swing.JLabel hourlyRateLabel;
	private static javax.swing.JTextField immediateSupervisorField;
	private javax.swing.JLabel immediateSupervisorLabel;
	private javax.swing.JButton confirmButton;
	private  javax.swing.JPanel mainPanel;
	private  javax.swing.JPanel navBar;
	private  javax.swing.JPanel leftNavBar;
	private  javax.swing.JPanel contentArea;
	private  javax.swing.JPanel contentPanel;
	private static javax.swing.JTextField lastNameField;
	private javax.swing.JLabel lastNameLabel;
	private static javax.swing.JTextField pagibigField;
	private javax.swing.JLabel pagibigLabel;
	private static javax.swing.JTextField philhealthField;
	private javax.swing.JLabel philhealthLabel;
	private static javax.swing.JTextField phoneAllowanceField;
	private javax.swing.JLabel phoneAllowanceLabel;
	private static javax.swing.JTextField phoneNumberField;
	private javax.swing.JLabel phoneNumberLabel;
	private static javax.swing.JTextField positionField;
	private javax.swing.JLabel positionLabel;
	private static javax.swing.JTextField riceSubsidyField;
	private javax.swing.JLabel riceSubsidyLabel;
	private static javax.swing.JTextField sssField;
	private javax.swing.JLabel sssLabel;
	private static javax.swing.JTextField statusField;
	private javax.swing.JLabel statusLabel;
	private static javax.swing.JTextField tinField;
	private javax.swing.JLabel tinLabel;
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	// End of variables declaration

	public UpdateEmployeeDetailsPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;
		initComponents();
	}

	private void initComponents() {
		
		// custom color
		String navyBlue = "#153969";
		String lightGray = "#f5f5f5";
		
	    RoundedTextField employeeNumberField = new RoundedTextField(20);
	    RoundedTextField firstNameField = new RoundedTextField(20);
	    RoundedTextField lastNameField = new RoundedTextField(20);
	    RoundedTextField birthdayField = new RoundedTextField(20);
	    RoundedTextField addressField = new RoundedTextField(20);
	    RoundedTextField phoneNumberField = new RoundedTextField(20);
	    RoundedTextField sssField = new RoundedTextField(20);
	    RoundedTextField philhealthField = new RoundedTextField(20);
	    RoundedTextField tinField = new RoundedTextField(20);
	    RoundedTextField pagibigField = new RoundedTextField(20);
	    RoundedTextField statusField = new RoundedTextField(20);
	    RoundedTextField positionField = new RoundedTextField(20);
	    RoundedTextField immediateSupervisorField = new RoundedTextField(20);
	    RoundedTextField basicSalaryField = new RoundedTextField(20);
	    RoundedTextField riceSubsidyField = new RoundedTextField(20);
	    RoundedTextField phoneAllowanceField = new RoundedTextField(20);
	    RoundedTextField clothingAllowanceField = new RoundedTextField(20);
	    RoundedTextField grossSemiMonthlyRateField = new RoundedTextField(20);
	    RoundedTextField hourlyRateField = new RoundedTextField(20);

		goBackToEmployeeListButton = new javax.swing.JButton();
		hourlyRateLabel = new javax.swing.JLabel();
		basicSalaryLabel = new javax.swing.JLabel();
		tinLabel = new javax.swing.JLabel();
		phoneAllowanceLabel = new javax.swing.JLabel();
		confirmButton = new javax.swing.JButton();
		positionLabel = new javax.swing.JLabel();
		addressLabel = new javax.swing.JLabel();
		immediateSupervisorLabel = new javax.swing.JLabel();
		employeeNumberLabel = new javax.swing.JLabel();
		grossSemiMonthlyRateLabel = new javax.swing.JLabel();
		sssLabel = new javax.swing.JLabel();
		statusLabel = new javax.swing.JLabel();
		riceSubsidyLabel = new javax.swing.JLabel();
		birthdayLabel = new javax.swing.JLabel();
		firstNameLabel = new javax.swing.JLabel();
		pagibigLabel = new javax.swing.JLabel();
		phoneNumberLabel = new javax.swing.JLabel();
		philhealthLabel = new javax.swing.JLabel();
		lastNameLabel = new javax.swing.JLabel();
		clothingAllowanceLabel = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("MotorPH Payroll System | Update Employee Details");

		goBackToEmployeeListButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goBackToEmployeeListButtonActionPerformed(evt);
			}
		});

		// Change the value of each corresponding field
		addressField.setText(employeeGI.getAddress());
		basicSalaryField.setText(Double.toString(employeeComp.getBasicSalary()));
		birthdayField.setText(employeeGI.getBirthday());
		clothingAllowanceField.setText(Double.toString(employeeComp.getClothingAllowance()));
		employeeNumberField.setText(employeeGI.getEmployeeNumber());
		firstNameField.setText(employeeGI.getFirstName());
		grossSemiMonthlyRateField.setText(Double.toString(employeeComp.getGrossSemiMonthlyRate()));
		hourlyRateField.setText(Double.toString(employeeComp.getHourlyRate()));
		immediateSupervisorField.setText(employeeGI.getSupervisor());
		lastNameField.setText(employeeGI.getLastName());
		pagibigField.setText(employeeGI.getPagibigNumber());
		philhealthField.setText(employeeGI.getPhilHealthNumber());
		phoneAllowanceField.setText(Double.toString(employeeComp.getPhoneAllowance()));
		phoneNumberField.setText(employeeGI.getPhoneNumber());
		positionField.setText(employeeGI.getPosition());
		riceSubsidyField.setText(Double.toString(employeeComp.getRiceSubsidy()));
		sssField.setText(employeeGI.getSSSNumber());
		statusField.setText(employeeGI.getStatus());
		tinField.setText(employeeGI.getTinNumber());

		// Set preferred size for all the fields
		addressField.setPreferredSize(new Dimension(164, 22));

		// Set the employeeNumber to uneditable
		employeeNumberField.setEditable(false);
		employeeNumberField.setEnabled(false);

		hourlyRateLabel.setFont(FontLoader.poppinsTextFont);
		hourlyRateLabel.setText("Hourly Rate");

		basicSalaryLabel.setFont(FontLoader.poppinsTextFont);
		basicSalaryLabel.setText("Basic Salary");

		tinLabel.setFont(FontLoader.poppinsTextFont);
		tinLabel.setText("TIN Number");

		phoneAllowanceLabel.setFont(FontLoader.poppinsTextFont);
		phoneAllowanceLabel.setText("Phone Allowance");

		positionLabel.setFont(FontLoader.poppinsTextFont);
		positionLabel.setText("Position");

		addressLabel.setFont(FontLoader.poppinsTextFont);
		addressLabel.setText("Address");

		immediateSupervisorLabel.setFont(FontLoader.poppinsTextFont);
		immediateSupervisorLabel.setText("Immediate Supervisor");

		employeeNumberLabel.setFont(FontLoader.poppinsTextFont);
		employeeNumberLabel.setText("Employee Number");

		grossSemiMonthlyRateLabel.setFont(FontLoader.poppinsTextFont);
		grossSemiMonthlyRateLabel.setText("Gross Semi-Monthly Rate");

		sssLabel.setFont(FontLoader.poppinsTextFont);
		sssLabel.setText("SSS Number");

		statusLabel.setFont(FontLoader.poppinsTextFont);
		statusLabel.setText("Status");

		riceSubsidyLabel.setFont(FontLoader.poppinsTextFont);
		riceSubsidyLabel.setText("Rice Subsidy");

		birthdayLabel.setFont(FontLoader.poppinsTextFont);
		birthdayLabel.setText("Birthday");

		firstNameLabel.setFont(FontLoader.poppinsTextFont);
		firstNameLabel.setText("First Name");

		pagibigLabel.setFont(FontLoader.poppinsTextFont);
		pagibigLabel.setText("Pag-ibig Number");

		phoneNumberLabel.setFont(FontLoader.poppinsTextFont);
		phoneNumberLabel.setText("Phone Number");

		philhealthLabel.setFont(FontLoader.poppinsTextFont);
		philhealthLabel.setText("PhilHealth Number");

		lastNameLabel.setFont(FontLoader.poppinsTextFont);
		lastNameLabel.setText("Last Name");

		clothingAllowanceLabel.setFont(FontLoader.poppinsTextFont);
		clothingAllowanceLabel.setText("Clothing Allowance");
		
		confirmButton.setText("Confirm");
		confirmButton.setFont(FontLoader.poppinsTextFont);
		confirmButton.setBackground(Color.decode(navyBlue));
		confirmButton.setForeground(Color.WHITE);
		confirmButton.setPreferredSize(new Dimension(100,50));
		confirmButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				confirmButtonActionPerformed(evt);
			}
		});
		
		// text field general size setting
		JTextField[] fields = {
			    employeeNumberField,
			    firstNameField,
			    lastNameField,
			    birthdayField,
			    addressField,
			    phoneNumberField,
			    sssField,
			    philhealthField,
			    tinField,
			    pagibigField,
			    statusField,
			    positionField,
			    immediateSupervisorField,
			    basicSalaryField,
			    riceSubsidyField,
			    phoneAllowanceField,
			    clothingAllowanceField,
			    grossSemiMonthlyRateField,
			    hourlyRateField
			};
			
			// for loop to make general layout for all fields
			for (JTextField field : fields) {
			    field.setPreferredSize(new Dimension(200,45));
			    field.setBorder(null);
			    field.setFont(FontLoader.poppinsTextFont);
			    field.setBorder(new EmptyBorder(0,12,0,0));
			}


		// Phone number tooltip
		String phoneNumberTooltip = "Accepted format: xxx-xxx-xxx";

		phoneNumberField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				CustomTooltip.showCustomTooltip(phoneNumberField, phoneNumberTooltip);
			}
		});

		phoneNumberField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Show the tooltip when the component gains focus
				CustomTooltip.showCustomTooltip(phoneNumberField, phoneNumberTooltip);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Hide the tooltip when the component loses focus
				CustomTooltip.hideCustomTooltip();
			}
		});

		// SSS tooltip
		String sssTooltip = "Accepted format: xx-xxxxxxx-x";

		sssField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				CustomTooltip.showCustomTooltip(sssField, sssTooltip);
			}
		});

		sssField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Show the tooltip when the component gains focus
				CustomTooltip.showCustomTooltip(sssField, sssTooltip);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Hide the tooltip when the component loses focus
				CustomTooltip.hideCustomTooltip();
			}
		});

		// PhilHealth tooltip
		String philhealthTooltip = "Max Length: 12";

		philhealthField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				CustomTooltip.showCustomTooltip(philhealthField, philhealthTooltip);
			}
		});

		philhealthField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Show the tooltip when the component gains focus
				CustomTooltip.showCustomTooltip(philhealthField, philhealthTooltip);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Hide the tooltip when the component loses focus
				CustomTooltip.hideCustomTooltip();
			}
		});

		// TIN tooltip
		String tinTooltip = "Accepted format: xxx-xxx-xxx-xxx";

		tinField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				CustomTooltip.showCustomTooltip(tinField, tinTooltip);
			}
		});

		tinField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Show the tooltip when the component gains focus
				CustomTooltip.showCustomTooltip(tinField, tinTooltip);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Hide the tooltip when the component loses focus
				CustomTooltip.hideCustomTooltip();
			}
		});

		// Pagibig tooltip
		String pagibigTooltip = "Max Length: 12";

		pagibigField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				CustomTooltip.showCustomTooltip(pagibigField, pagibigTooltip);
			}
		});

		pagibigField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Show the tooltip when the component gains focus
				CustomTooltip.showCustomTooltip(pagibigField, pagibigTooltip);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Hide the tooltip when the component loses focus
				CustomTooltip.hideCustomTooltip();
			}
		});

		// Birthday tooltip
		String birthdayTooltip = "Accepted format: MM/dd/yyyy";

		birthdayField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				CustomTooltip.showCustomTooltip(birthdayField, birthdayTooltip);
			}
		});

		birthdayField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Show the tooltip when the component gains focus
				CustomTooltip.showCustomTooltip(birthdayField, birthdayTooltip);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Hide the tooltip when the component loses focus
				CustomTooltip.hideCustomTooltip();
			}
		});
		
		// main panel
		mainPanel = new JPanel(new BorderLayout());
	
		// navigation bar panel
		navBar = new JPanel(new BorderLayout());
		navBar.setBackground(Color.decode(navyBlue));
		navBar.setBorder(new EmptyBorder(0,0,0,0));
		mainPanel.add(navBar, BorderLayout.NORTH);
		
			// left of navigation bar panel
			leftNavBar = new JPanel (new GridBagLayout());
			leftNavBar.setBackground(Color.decode(navyBlue));
			navBar.add(leftNavBar, BorderLayout.WEST);
			
				// back button
				ImageIcon backButtonIcon = new ImageIcon("resources/images/back-button-navbar.png");
		        goBackToEmployeeListButton = new JButton(backButtonIcon);
		        goBackToEmployeeListButton.setBorder(null);
		        goBackToEmployeeListButton.setFocusPainted(false);
		        goBackToEmployeeListButton.setContentAreaFilled(false); 
			    
			    GridBagConstraints navBackButtonGBC = new GridBagConstraints();
			    navBackButtonGBC.insets = new Insets(0,0,0,0);
			    leftNavBar.add(goBackToEmployeeListButton, navBackButtonGBC);
			    
			    goBackToEmployeeListButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						goBackToEmployeeListButtonActionPerformed(evt);
					}
				});
			    
		        // company logo
			    ImageIcon motorphlogoAdmin = new ImageIcon("resources/images/motorph-logo-white.png");
		        JLabel motorPHLogo = new JLabel(motorphlogoAdmin);
		        
		        GridBagConstraints motorPHLogoGBC = new GridBagConstraints();
		        motorPHLogoGBC.insets = new Insets(-45,-30,0,0);
		        leftNavBar.add(motorPHLogo, motorPHLogoGBC);
	        
	        // admin account logo
	        ImageIcon adminIcon = new ImageIcon("resources/images/Admin-Logo.png");
	        JLabel adminIconLabel = new JLabel(adminIcon);
	        adminIconLabel.setBorder(new EmptyBorder(-10,0,-10,0));
	        navBar.add(adminIconLabel, BorderLayout.EAST);
        
	    // content area panel
		contentArea = new JPanel(new BorderLayout());
		contentArea.setBackground(Color.decode(lightGray));
		contentArea.setBorder(new EmptyBorder(50,50,50,50));
		mainPanel.add(contentArea, BorderLayout.CENTER);
		
		// content panel
		contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBackground(Color.decode(lightGray));
		contentArea.add(contentPanel, BorderLayout.CENTER);
		
		// group layout 
		
		GroupLayout contentPanelGL = new GroupLayout(contentPanel);
		contentPanel.setLayout(contentPanelGL);
		
		contentPanelGL.setHorizontalGroup(
				contentPanelGL.createSequentialGroup()
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		        		.addComponent(employeeNumberLabel)
			            .addComponent(employeeNumberField)
			            .addComponent(firstNameLabel)
			            .addComponent(firstNameField)
			            .addComponent(lastNameLabel)
			            .addComponent(lastNameField)
			            .addComponent(birthdayLabel)
			            .addComponent(birthdayField)
			            .addComponent(addressLabel)
			            .addComponent(addressField)
			            	)
		        .addGap(50)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(phoneNumberLabel)
			            .addComponent(phoneNumberField)
			            .addComponent(sssLabel)
			            .addComponent(sssField)
			            .addComponent(philhealthLabel)
			            .addComponent(philhealthField)
			            .addComponent(tinLabel)
			            .addComponent(tinField)
			            .addComponent(pagibigLabel)
			            .addComponent(pagibigField)
			            	)
		        .addGap(50)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(statusLabel)
			            .addComponent(statusField)
			            .addComponent(positionLabel)
			            .addComponent(positionField)
			            .addComponent(immediateSupervisorLabel)
			            .addComponent(immediateSupervisorField)
			            .addComponent(basicSalaryLabel)
			            .addComponent(basicSalaryField)
			            .addComponent(riceSubsidyLabel)
			            .addComponent(riceSubsidyField)
			            	)
		        .addGap(50)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(phoneAllowanceLabel)
			            .addComponent(phoneAllowanceField)
			            .addComponent(clothingAllowanceLabel)
			            .addComponent(clothingAllowanceField)
			            .addComponent(grossSemiMonthlyRateLabel)
			            .addComponent(grossSemiMonthlyRateField)
			            .addComponent(hourlyRateLabel)
			            .addComponent(hourlyRateField)
			            .addGap(0)
			            .addComponent(confirmButton)
			            	)
		);
		
		contentPanelGL.setVerticalGroup(
				contentPanelGL.createSequentialGroup()
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			    		.addComponent(employeeNumberLabel)
			    		.addComponent(phoneNumberLabel)
			    		.addComponent(statusLabel)
			    		.addComponent(phoneAllowanceLabel)
			            	)
		        .addGap(5)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(employeeNumberField)
		        		.addComponent(phoneNumberField)
		        		.addComponent(statusField)
		        		.addComponent(phoneAllowanceField)
		        		)
		        .addGap(30)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(firstNameLabel)
		        		.addComponent(sssLabel)
		        		.addComponent(positionLabel)
		        		.addComponent(clothingAllowanceLabel)
		        		)
		        .addGap(5)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(firstNameField)
		        		.addComponent(sssField)
		        		.addComponent(positionField)
		        		.addComponent(clothingAllowanceField)
		        		)
		        .addGap(30)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(lastNameLabel)
		        		.addComponent(philhealthLabel)
		        		.addComponent(immediateSupervisorLabel)
		        		.addComponent(grossSemiMonthlyRateLabel)
		        		)
		        .addGap(5)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(lastNameField)
		        		.addComponent(philhealthField)
		        		.addComponent(immediateSupervisorField)
		        		.addComponent(grossSemiMonthlyRateField)
		        		)
		        .addGap(30)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(birthdayLabel)
		        		.addComponent(tinLabel)
		        		.addComponent(basicSalaryLabel)
		        		.addComponent(hourlyRateLabel)
		        		)
		        .addGap(5)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(birthdayField)
		        		.addComponent(tinField)
		        		.addComponent(basicSalaryField)
		        		.addComponent(hourlyRateField)
		        		)
		        .addGap(30)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(addressLabel)
		        		.addComponent(pagibigLabel)
		        		.addComponent(riceSubsidyLabel)
		        		.addGap(0)
		        		)
		        .addGap(5)
		        .addGroup(contentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(addressField)
		        		.addComponent(pagibigField)
		        		.addComponent(riceSubsidyField)
		        		.addComponent(confirmButton)
		        		)
		         
		);
		
		// taskbar icon
		ImageIcon taskbarImage = new ImageIcon("resources/images/motorph-taskbar-image.png");
		setIconImage(taskbarImage.getImage());
		
		// jframe window layout 
		add(mainPanel);
		pack();
		setSize(1366,768);
		setLocationRelativeTo(null);
	}// </editor-fold>

	private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Instantiate error message in case of misinput
		StringBuilder errorMessage = new StringBuilder();

		// Get the employee number to update
		String employeeNumToUpdate = employeeNumberField.getText();

		// Update the employee information in the database
		if (!updateEmployeeInDatabase(employeeNumToUpdate, errorMessage)) {
			errorDialogPane(errorMessage, "Error");
			return;
		}

		// Go back to the employee list page
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the UpdateEmployeeDetailsPage Window
				dispose();

				new EmployeeListPage().setVisible(true);
			}
		});
	}



	private boolean updateEmployeeInDatabase(String employeeNumToUpdate, StringBuilder errorMessage) {
		// Maintain arrays to validate user input
		JTextField[] stringOnlyFields = { lastNameField, firstNameField, positionField, immediateSupervisorField,
				statusField };

		JTextField[] fields = { lastNameField, firstNameField, birthdayField, addressField, phoneNumberField, sssField,
				philhealthField, tinField, pagibigField, statusField, positionField, immediateSupervisorField,
				basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField,
				grossSemiMonthlyRateField, hourlyRateField };

		JTextField[] numericFields = { pagibigField, philhealthField, basicSalaryField, riceSubsidyField,
				phoneAllowanceField, clothingAllowanceField, grossSemiMonthlyRateField, hourlyRateField };

		// Check if all fields are filled out
		if (Arrays.stream(fields).anyMatch(field -> field.getText().trim().isEmpty())) {
			errorMessage.setLength(0);
			errorMessage.append("Please fill in all the fields.");
			return false;
		}

		// Check numeric fields
		if (Arrays.stream(numericFields).anyMatch(numField -> !DataValidators.isNumeric(numField.getText()))) {
			errorMessage.setLength(0);
			errorMessage.append(
					"Please enter valid numeric values for those that require it. (e.g. Hourly Rate, Basic Salary)");
			return false;
		}

		// Check string-only fields
		if (Arrays.stream(stringOnlyFields)
				.anyMatch(stringField -> !DataValidators.isPureString(stringField.getText()))) {
			errorMessage.setLength(0);
			errorMessage.append("Please enter valid characters only.");
			return false;
		}

		// Validate date format
		if (!DataValidators.isValidDate(birthdayField.getText())) {
			errorMessage.setLength(0);
			errorMessage.append("Please enter a valid date.");
			return false;
		}

		// Validate other fields format
		if (!DataValidators.isSSSFormattedCorrectly(sssField.getText())
				|| !DataValidators.isPhoneNumberFormattedCorrectly(phoneNumberField.getText())
				|| !DataValidators.isTINFormattedCorrectly(tinField.getText())
				|| !DataValidators.isProperLength(pagibigField.getText())
				|| !DataValidators.isProperLength(philhealthField.getText())) {
			errorMessage.setLength(0);
			errorMessage.append("Please follow proper formatting.");
			return false;
		}

		// Create employee objects with the updated information
		EmployeeInformation employee = new EmployeeInformation(employeeNumToUpdate);
		GovernmentIdentification govId = new GovernmentIdentification(employeeNumToUpdate);
		Compensation compensation = new Compensation(employeeNumToUpdate);

		// Set employee information
		employee.setLastName(lastNameField.getText());
		employee.setFirstName(firstNameField.getText());
		employee.setBirthday(birthdayField.getText());
		employee.setAddress(addressField.getText());
		employee.setPhoneNumber(phoneNumberField.getText());
		employee.setStatus(statusField.getText());
		employee.setPosition(positionField.getText());
		employee.setSupervisor(immediateSupervisorField.getText());
		employee.setHourlyRate(Double.parseDouble(hourlyRateField.getText()));

		// Set government ID information
		govId.setSSSNumber(sssField.getText());
		govId.setPhilHealthNumber(philhealthField.getText());
		govId.setTinNumber(tinField.getText());
		govId.setPagibigNumber(pagibigField.getText());

		// Set compensation information
		compensation.setBasicSalary(Double.parseDouble(basicSalaryField.getText()));
		compensation.setRiceSubsidy(Double.parseDouble(riceSubsidyField.getText()));
		compensation.setPhoneAllowance(Double.parseDouble(phoneAllowanceField.getText()));
		compensation.setClothingAllowance(Double.parseDouble(clothingAllowanceField.getText()));
		compensation.setGrossSemiMonthlyRate(Double.parseDouble(grossSemiMonthlyRateField.getText()));
		compensation.setHourlyRate(Double.parseDouble(hourlyRateField.getText()));

		// Update the database
		boolean updated = EmployeeDAO.updateEmployee(employee, govId, compensation);
		
		// Update username in the users table based on first and last name
		String username = (firstNameField.getText() + "." + lastNameField.getText()).toLowerCase();
		UserDAO.updateUsername(employeeNumToUpdate, username);

		return updated;
	}

	private void errorDialogPane(StringBuilder errorMessage, String title) {
		JOptionPane.showMessageDialog(new JFrame(""), errorMessage, title, JOptionPane.ERROR_MESSAGE);
	}

	private void goBackToEmployeeListButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Go back to the employee list page
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				new EmployeeListPage().setVisible(true);
			}
		});
	}
	
	// round text fields 
	public class RoundedTextField extends JTextField {
	    private int arcSize = 15;

	    public RoundedTextField(int columns) {
	        super(columns);
	        setOpaque(false);
	        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D) g.create();

	        // Enable anti-aliasing
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        // Background
	        g2.setColor(getBackground());
	        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);

	        // Border
	        g2.setColor(Color.LIGHT_GRAY);
	        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcSize, arcSize);

	        g2.dispose();
	        super.paintComponent(g);
	    }
	}
	
	private class FontLoader {

        // Public static font variable (accessible from anywhere)
        public static final Font poppinsTextFont = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 17f);
        public static final Font poppinsConfirmButton = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 17f);

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
}
