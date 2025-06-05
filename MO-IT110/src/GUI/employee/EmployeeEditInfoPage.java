package GUI.employee;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
public class EmployeeEditInfoPage extends JFrame {

	// Variables declaration - do not modify
	private static javax.swing.JTextField addressField;
	private javax.swing.JLabel addressLabel;
	private static javax.swing.JTextField birthdayField;
	private javax.swing.JLabel birthdayLabel;
	private javax.swing.JTextField employeeNumberField;
	private javax.swing.JLabel employeeNumberLabel;
	private static javax.swing.JTextField firstNameField;
	private javax.swing.JLabel firstNameLabel;
	private javax.swing.JButton goBackToDashboardButton;
	private javax.swing.JButton confirmButton;
	private javax.swing.JPanel jPanel3;
	private static javax.swing.JTextField lastNameField;
	private javax.swing.JLabel lastNameLabel;
	private static javax.swing.JTextField pagibigField;
	private javax.swing.JLabel pagibigLabel;
	private static javax.swing.JTextField philhealthField;
	private javax.swing.JLabel philhealthLabel;
	private static javax.swing.JTextField phoneNumberField;
	private javax.swing.JLabel phoneNumberLabel;
	private static javax.swing.JTextField sssField;
	private javax.swing.JLabel sssLabel;
	private static javax.swing.JTextField tinField;
	private javax.swing.JLabel tinLabel;
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	// End of variables declaration

	public EmployeeEditInfoPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;
		initComponents();
	}

	private void initComponents() {

		goBackToDashboardButton = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		birthdayField = new javax.swing.JTextField();
		firstNameField = new javax.swing.JTextField();
		confirmButton = new javax.swing.JButton();
		addressLabel = new javax.swing.JLabel();
		pagibigField = new javax.swing.JTextField();
		employeeNumberLabel = new javax.swing.JLabel();
		phoneNumberField = new javax.swing.JTextField();
		philhealthField = new javax.swing.JTextField();
		sssLabel = new javax.swing.JLabel();
		lastNameField = new javax.swing.JTextField();
		birthdayLabel = new javax.swing.JLabel();
		firstNameLabel = new javax.swing.JLabel();
		pagibigLabel = new javax.swing.JLabel();
		phoneNumberLabel = new javax.swing.JLabel();
		employeeNumberField = new javax.swing.JTextField();
		addressField = new javax.swing.JTextField();
		philhealthLabel = new javax.swing.JLabel();
		lastNameLabel = new javax.swing.JLabel();
		tinField = new javax.swing.JTextField();
		tinLabel = new javax.swing.JLabel();
		sssField = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("MotorPH Payroll System | Edit Personal Information");

		goBackToDashboardButton.setText("Go Back to Dashboard");
		goBackToDashboardButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goBackToDashboardButtonActionPerformed(evt);
			}
		});

		// Change the value of each corresponding field with current employee data
		addressField.setText(employeeGI.getAddress());
		birthdayField.setText(employeeGI.getBirthday());
		employeeNumberField.setText(employeeGI.getEmployeeNumber());
		firstNameField.setText(employeeGI.getFirstName());
		lastNameField.setText(employeeGI.getLastName());
		pagibigField.setText(employeeGI.getPagibigNumber());
		philhealthField.setText(employeeGI.getPhilHealthNumber());
		phoneNumberField.setText(employeeGI.getPhoneNumber());
		sssField.setText(employeeGI.getSSSNumber());
		tinField.setText(employeeGI.getTinNumber());

		// Set preferred size for all the fields
		addressField.setPreferredSize(new Dimension(164, 22));

		// Set the employeeNumber to uneditable
		employeeNumberField.setEditable(false);
		employeeNumberField.setEnabled(false);

		jPanel3.setBackground(new java.awt.Color(204, 204, 204));
		jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

		tinLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		tinLabel.setText("TIN Number");

		confirmButton.setText("Save Changes");
		confirmButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				confirmButtonActionPerformed(evt);
			}
		});

		addressLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		addressLabel.setText("Address");

		employeeNumberLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		employeeNumberLabel.setText("Employee Number");

		sssLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		sssLabel.setText("SSS Number");

		birthdayLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		birthdayLabel.setText("Birthday");

		firstNameLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		firstNameLabel.setText("First Name");

		pagibigLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		pagibigLabel.setText("Pag-ibig Number");

		phoneNumberLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		phoneNumberLabel.setText("Phone Number");

		philhealthLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		philhealthLabel.setText("PhilHealth Number");

		lastNameLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		lastNameLabel.setText("Last Name");

		/*************************/
		/* Custom tooltip events */
		/*************************/

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

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addGap(36, 36, 36).addGroup(jPanel3Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(firstNameField)
						.addComponent(employeeNumberField).addComponent(birthdayField).addComponent(addressField)
						.addGroup(jPanel3Layout.createSequentialGroup()
								.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(addressLabel).addComponent(birthdayLabel)
										.addComponent(lastNameLabel).addComponent(firstNameLabel)
										.addComponent(employeeNumberLabel))
								.addGap(0, 49, Short.MAX_VALUE))
						.addComponent(lastNameField)).addGap(44, 44, 44)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(phoneNumberLabel).addComponent(sssLabel).addComponent(sssField)
								.addComponent(philhealthLabel).addComponent(philhealthField).addComponent(tinLabel)
								.addComponent(tinField).addComponent(pagibigLabel).addComponent(pagibigField)
								.addComponent(phoneNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 163,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(44, 44, 44)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 163,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(36, 36, 36)));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(employeeNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(phoneNumberLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(employeeNumberField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(phoneNumberField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(firstNameLabel).addComponent(sssLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(sssField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lastNameLabel).addComponent(philhealthLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(philhealthField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(birthdayLabel).addComponent(tinLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(birthdayField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(tinField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(addressLabel).addComponent(pagibigLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(pagibigField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(confirmButton))
						.addContainerGap(19, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(28, 28, 28)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(goBackToDashboardButton).addComponent(jPanel3,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(29, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(46, 46, 46).addComponent(goBackToDashboardButton)
						.addGap(18, 18, 18)
						.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(43, Short.MAX_VALUE)));

		pack();

		// Make the window appear in the middle
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

		// Show success message
		JOptionPane.showMessageDialog(this, "Your information has been updated successfully!", "Success", 
				JOptionPane.INFORMATION_MESSAGE);

		// Go back to the employee dashboard
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeeEditInfoPage Window
				dispose();

				// Create updated objects with new data
				GovernmentIdentification updatedGI = new GovernmentIdentification(employeeNumToUpdate);
				updatedGI.setFirstName(firstNameField.getText());
				updatedGI.setLastName(lastNameField.getText());
				updatedGI.setBirthday(birthdayField.getText());
				updatedGI.setAddress(addressField.getText());
				updatedGI.setPhoneNumber(phoneNumberField.getText());
				updatedGI.setSSSNumber(sssField.getText());
				updatedGI.setPhilHealthNumber(philhealthField.getText());
				updatedGI.setTinNumber(tinField.getText());
				updatedGI.setPagibigNumber(pagibigField.getText());
				
				// Copy other fields that weren't edited
				updatedGI.setStatus(employeeGI.getStatus());
				updatedGI.setPosition(employeeGI.getPosition());
				updatedGI.setSupervisor(employeeGI.getSupervisor());

				// Return to dashboard with updated information
				new EmployeeDashboard(updatedGI, employeeComp).setVisible(true);
			}
		});
	}

	private boolean updateEmployeeInDatabase(String employeeNumToUpdate, StringBuilder errorMessage) {
		// Maintain arrays to validate user input - only personal/contact info fields
		JTextField[] stringOnlyFields = { lastNameField, firstNameField };

		JTextField[] fields = { lastNameField, firstNameField, birthdayField, addressField, phoneNumberField, sssField,
				philhealthField, tinField, pagibigField };

		// Check if all fields are filled out
		if (Arrays.stream(fields).anyMatch(field -> field.getText().trim().isEmpty())) {
			errorMessage.setLength(0);
			errorMessage.append("Please fill in all the fields.");
			return false;
		}

		// Check string-only fields
		if (Arrays.stream(stringOnlyFields)
				.anyMatch(stringField -> !DataValidators.isPureString(stringField.getText()))) {
			errorMessage.setLength(0);
			errorMessage.append("Please enter valid characters only for name fields.");
			return false;
		}

		// Validate date format
		// if (!DataValidators.isValidDate(birthdayField.getText())) {
		// 	errorMessage.setLength(0);
		// 	errorMessage.append("Please enter a valid date for birthday.");
		// 	return false;
		// }

		// Validate other fields format
		if (!DataValidators.isSSSFormattedCorrectly(sssField.getText())
				|| !DataValidators.isPhoneNumberFormattedCorrectly(phoneNumberField.getText())
				|| !DataValidators.isTINFormattedCorrectly(tinField.getText())
				|| !DataValidators.isProperLength(pagibigField.getText())
				|| !DataValidators.isProperLength(philhealthField.getText())) {
			errorMessage.setLength(0);
			errorMessage.append("Please follow proper formatting for government numbers and phone number.");
			return false;
		}

		// Create employee objects with the updated information
		EmployeeInformation employee = new EmployeeInformation(employeeNumToUpdate);
		GovernmentIdentification govId = new GovernmentIdentification(employeeNumToUpdate);
		Compensation compensation = new Compensation(employeeNumToUpdate);

		// Set only the personal information that employees can edit
		employee.setLastName(lastNameField.getText());
		employee.setFirstName(firstNameField.getText());
		employee.setBirthday(birthdayField.getText());
		employee.setAddress(addressField.getText());
		employee.setPhoneNumber(phoneNumberField.getText());
		
		// Keep existing values for fields employees shouldn't change
		employee.setStatus(employeeGI.getStatus());
		employee.setPosition(employeeGI.getPosition());
		employee.setSupervisor(employeeGI.getSupervisor());
		employee.setHourlyRate(employeeComp.getHourlyRate());

		// Set government ID information
		govId.setSSSNumber(sssField.getText());
		govId.setPhilHealthNumber(philhealthField.getText());
		govId.setTinNumber(tinField.getText());
		govId.setPagibigNumber(pagibigField.getText());

		// Keep existing compensation values
		compensation.setBasicSalary(employeeComp.getBasicSalary());
		compensation.setRiceSubsidy(employeeComp.getRiceSubsidy());
		compensation.setPhoneAllowance(employeeComp.getPhoneAllowance());
		compensation.setClothingAllowance(employeeComp.getClothingAllowance());
		compensation.setGrossSemiMonthlyRate(employeeComp.getGrossSemiMonthlyRate());
		compensation.setHourlyRate(employeeComp.getHourlyRate());

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

	private void goBackToDashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Go back to the employee dashboard
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeeEditInfoPage Window
				dispose();

				new EmployeeDashboard(employeeGI, employeeComp).setVisible(true);
			}
		});
	}
}
