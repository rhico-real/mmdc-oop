package GUI.employee;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.ComboPopup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Classes.Compensation;
import Classes.GovernmentIdentification;
import GUI.LoginPage;
//import GUI.admin.CalculatorPage.RoundedPanel;
import UtilityClasses.JsonFileHandler;
import UtilityClasses.SalaryCalculator;
import DAO.AttendanceDAO;
import java.time.format.DateTimeFormatter;
import java.time.YearMonth;

@SuppressWarnings("serial")
public class EmployeeDashboard extends JFrame {

	// Variables declaration - do not modify
	
	private RoundedPanel grossSalaryPanel = new RoundedPanel(20);
	private RoundedPanel allowancesPanel = new RoundedPanel(20);
	private RoundedPanel addressPanel = new RoundedPanel(20);
	private RoundedPanel netSalaryPanel = new RoundedPanel(20);
	private RoundedPanel myInformationPanel = new RoundedPanel(20);
	private RoundedPanel employmentPanel = new RoundedPanel(20);
	private RoundedPanel governmentIDPanel = new RoundedPanel(20);
	
	private javax.swing.JPanel mainPanel;
	private javax.swing.JPanel menuBar ;
	private javax.swing.JPanel contentPanel; 
	private javax.swing.JPanel titlePanel;
	private javax.swing.JLabel titleLabel;
	private javax.swing.JLabel profileName;
	private javax.swing.JLabel profilePic;
	private javax.swing.JPanel informationPanel;
	private javax.swing.JLabel myInformationLabel;
	
	private javax.swing.JLabel address;
	private javax.swing.JLabel addressValue;
	private javax.swing.JLabel allowancesLabel;
	private javax.swing.JButton submitLeaveRequestButton;
	private javax.swing.JButton submitOvertimeButton;
	private javax.swing.JButton submitPayslipButton;
	private javax.swing.JButton editInfoButton;
	private javax.swing.JLabel birthday;
	private javax.swing.JLabel birthdayValue;
	private javax.swing.JLabel clothingAllowanceLabel;
	private javax.swing.JLabel clothingAllowanceValue;
	private javax.swing.JButton computeButton;
	private javax.swing.JButton logoutButton;
	private javax.swing.JLabel firstName;
	private javax.swing.JLabel firstNameValue;
	private javax.swing.JLabel grossSalaryComputationLabel;
	private javax.swing.JLabel grossSalaryLabel;
	private javax.swing.JLabel grossSalaryLabel1;
	private javax.swing.JLabel grossSalaryValue;
	private javax.swing.JLabel grossSalaryValue1;
	private javax.swing.JLabel hourlyRate;
	private javax.swing.JLabel hourlyRateLabel;
	private javax.swing.JLabel hourlyRateValue;
	private javax.swing.JLabel hourlyRateLabel1;
	private javax.swing.JLabel hoursRenderedLabel;
	private javax.swing.JLabel hoursRenderedValue;
	private javax.swing.JLabel immediateSupervisor;
	private javax.swing.JLabel immediateSupervisorValue;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel lastName;
	private javax.swing.JLabel lastNameValue;
	private javax.swing.JComboBox<String> monthDropdown;
	private javax.swing.JComboBox<String> yearDropdown;
	private javax.swing.JLabel netSalaryComputationLabel;
	private javax.swing.JLabel netSalaryLabel;
	private javax.swing.JLabel netSalaryValue;
	private javax.swing.JLabel pagibigDeductionsLabel;
	private javax.swing.JLabel pagibigDeductionsValue;
	private javax.swing.JLabel pagibigNumber;
	private javax.swing.JLabel pagibigNumberValue;
	private javax.swing.JLabel philhealthDeductionsLabel;
	private javax.swing.JLabel philhealthDeductionsValue;
	private javax.swing.JLabel philhealthNumber;
	private javax.swing.JLabel philhealthNumberValue;
	private javax.swing.JLabel phoneAllowanceLabel;
	private javax.swing.JLabel phoneAllowanceValue;
	private javax.swing.JLabel phoneNumber;
	private javax.swing.JLabel phoneNumberValue;
	private javax.swing.JLabel position;
	private javax.swing.JLabel positionValue;
	private javax.swing.JLabel riceSubsidyLabel;
	private javax.swing.JLabel riceSubsidyValue;
	private javax.swing.JLabel salaryAfterTaxLabel;
	private javax.swing.JLabel salaryAfterTaxValue;
	private javax.swing.JLabel sssDeductionsLabel;
	private javax.swing.JLabel sssDeductionsValue;
	private javax.swing.JLabel overtimeLabel;
	private javax.swing.JLabel overtimeValue;
	private javax.swing.JLabel sssNumber;
	private javax.swing.JLabel sssNumberValue;
	private javax.swing.JLabel status;
	private javax.swing.JLabel statusValue;
	private javax.swing.JLabel taxableSalaryLabel;
	private javax.swing.JLabel taxableSalaryValue;
	private javax.swing.JLabel tinNumber;
	private javax.swing.JLabel tinNumberValue;
	private javax.swing.JLabel totalAllowanceLabel;
	private javax.swing.JLabel totalAllowanceValue;
	private javax.swing.JLabel totalAllowancesLabel1;
	private javax.swing.JLabel totalAllowancesValue1;
	private javax.swing.JLabel totalDeductionsLabel;
	private javax.swing.JLabel totalDeductionsValue;
	private javax.swing.JLabel withHoldingTaxLabel;
	private javax.swing.JLabel withHoldingTaxValue;
	private javax.swing.JLabel welcomeLabel;
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	private Double totalAllowance;
	private String selectedMonth = LocalDate.now().getMonth().toString();
	private int selectedYear = LocalDate.now().getYear();
	private AtomicInteger hoursRenderedNum = new AtomicInteger(0);
	private AtomicInteger absentsNum = new AtomicInteger(0);
	private AtomicInteger latesNum = new AtomicInteger(0);
	private AtomicInteger presentsNum = new AtomicInteger(0);
	private DecimalFormat numberFormat = new DecimalFormat("#.00");
	private double overtimeHours;
	// End of variables declaration

	public EmployeeDashboard(GovernmentIdentification employeeGI, Compensation employeeComp) {

		// Put the class objects onto a higher scope
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;

		// Set total allowance early on to avoid errors
		this.totalAllowance = employeeComp.getRiceSubsidy() + employeeComp.getPhoneAllowance()
				+ employeeComp.getClothingAllowance();

		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// @SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		
		mainPanel = new javax.swing.JPanel(new BorderLayout());
		menuBar = new javax.swing.JPanel(new GridBagLayout());
		contentPanel = new javax.swing.JPanel(new BorderLayout());
		titlePanel = new javax.swing.JPanel(new GridBagLayout());
		titleLabel = new javax.swing.JLabel();
		profileName = new javax.swing.JLabel();
		profilePic = new javax.swing.JLabel();
		informationPanel = new javax.swing.JPanel(new GridBagLayout());
		myInformationLabel = new javax.swing.JLabel();

		jScrollPane1 = new javax.swing.JScrollPane();
		address = new javax.swing.JLabel();
		addressValue = new javax.swing.JLabel();
		hoursRenderedLabel = new javax.swing.JLabel();
		grossSalaryComputationLabel = new javax.swing.JLabel();
		hourlyRateLabel = new javax.swing.JLabel();
		grossSalaryLabel = new javax.swing.JLabel();
		hourlyRateLabel1 = new javax.swing.JLabel();
		grossSalaryValue = new javax.swing.JLabel();
		hoursRenderedValue = new javax.swing.JLabel();
		netSalaryComputationLabel = new javax.swing.JLabel();
		sssDeductionsLabel = new javax.swing.JLabel();
		sssDeductionsValue = new javax.swing.JLabel();
		overtimeLabel = new javax.swing.JLabel();
		overtimeValue = new javax.swing.JLabel();
		philhealthDeductionsLabel = new javax.swing.JLabel();
		philhealthDeductionsValue = new javax.swing.JLabel();
		totalDeductionsLabel = new javax.swing.JLabel();
		totalDeductionsValue = new javax.swing.JLabel();
		pagibigDeductionsLabel = new javax.swing.JLabel();
		pagibigDeductionsValue = new javax.swing.JLabel();
		grossSalaryLabel1 = new javax.swing.JLabel();
		grossSalaryValue1 = new javax.swing.JLabel();
		taxableSalaryLabel = new javax.swing.JLabel();
		taxableSalaryValue = new javax.swing.JLabel();
		withHoldingTaxLabel = new javax.swing.JLabel();
		withHoldingTaxValue = new javax.swing.JLabel();
		salaryAfterTaxLabel = new javax.swing.JLabel();
		salaryAfterTaxValue = new javax.swing.JLabel();
		totalAllowancesLabel1 = new javax.swing.JLabel();
		totalAllowancesValue1 = new javax.swing.JLabel();
		netSalaryLabel = new javax.swing.JLabel();
		netSalaryValue = new javax.swing.JLabel();
		riceSubsidyLabel = new javax.swing.JLabel();
		riceSubsidyValue = new javax.swing.JLabel();
		phoneAllowanceLabel = new javax.swing.JLabel();
		phoneAllowanceValue = new javax.swing.JLabel();
		totalAllowanceLabel = new javax.swing.JLabel();
		totalAllowanceValue = new javax.swing.JLabel();
		allowancesLabel = new javax.swing.JLabel();
		clothingAllowanceLabel = new javax.swing.JLabel();
		clothingAllowanceValue = new javax.swing.JLabel();
		firstName = new javax.swing.JLabel();
		firstNameValue = new javax.swing.JLabel();
		lastName = new javax.swing.JLabel();
		lastNameValue = new javax.swing.JLabel();
		birthday = new javax.swing.JLabel();
		birthdayValue = new javax.swing.JLabel();
		phoneNumber = new javax.swing.JLabel();
		phoneNumberValue = new javax.swing.JLabel();
		status = new javax.swing.JLabel();
		statusValue = new javax.swing.JLabel();
		position = new javax.swing.JLabel();
		positionValue = new javax.swing.JLabel();
		immediateSupervisor = new javax.swing.JLabel();
		immediateSupervisorValue = new javax.swing.JLabel();
		hourlyRate = new javax.swing.JLabel();
		hourlyRateValue = new javax.swing.JLabel();
		sssNumber = new javax.swing.JLabel();
		sssNumberValue = new javax.swing.JLabel();
		philhealthNumber = new javax.swing.JLabel();
		philhealthNumberValue = new javax.swing.JLabel();
		pagibigNumber = new javax.swing.JLabel();
		pagibigNumberValue = new javax.swing.JLabel();
		tinNumber = new javax.swing.JLabel();
		tinNumberValue = new javax.swing.JLabel();
		welcomeLabel = new javax.swing.JLabel();
		monthDropdown = new javax.swing.JComboBox<>();
		yearDropdown = new javax.swing.JComboBox<>();
		computeButton = new javax.swing.JButton();
		submitLeaveRequestButton = new javax.swing.JButton();
		submitOvertimeButton = new javax.swing.JButton();
		submitPayslipButton = new javax.swing.JButton();
		editInfoButton = new javax.swing.JButton();
		logoutButton = new javax.swing.JButton();

		setTitle("MotorPH Payroll System | Full Details of " + employeeGI.getLastName());
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(1366,768);
		setResizable(false);
		
		// custom colors
		String navyBlue = "#153969";
		String lightGray = "#f5f5f5";
		String lightRed ="#ff5757";
		
		//mainPanel
		add(mainPanel);
		
		// menubar panel
		menuBar.setBackground(Color.white);
		menuBar.setBorder(new EmptyBorder(0, 60, 0, 60));
		mainPanel.add(menuBar, BorderLayout.WEST);
		
		// motorph logo
		ImageIcon companyLogo = new ImageIcon("resources/images/motorph-logo-navyblue.png");
		JLabel companyLogoLabel = new JLabel(companyLogo);
		GridBagConstraints companyLogoLabelGBC = new GridBagConstraints();
		companyLogoLabelGBC.gridx = 0;
		companyLogoLabelGBC.gridy = 0;
		companyLogoLabelGBC.anchor = GridBagConstraints.WEST;
		companyLogoLabelGBC.insets = new Insets (-350,-65,-120,0);
		menuBar.add(companyLogoLabel, companyLogoLabelGBC);
		
		// compute button
		computeButton.setFont(FontLoader.poppinsRegular14f);
		computeButton.setForeground(Color.WHITE);
		computeButton.setBackground(Color.decode("#FF9B45"));
		computeButton.setPreferredSize(new Dimension(85,30));
		computeButton.setBorder(new EmptyBorder(0,-4,0,-4));
		computeButton.setBorder(null);
		computeButton.setFocusPainted(false);
		//computeButton.setContentAreaFilled(false); 
		
		GridBagConstraints computeButtonGBC = new GridBagConstraints();
		computeButtonGBC.gridx = 0;
		computeButtonGBC.gridy = 1;
		computeButtonGBC.anchor= GridBagConstraints.WEST;
		computeButtonGBC.insets = new Insets (0,0,5,0);
		menuBar.add(computeButton, computeButtonGBC);
		
		// month dropdown
		monthDropdown.setBackground(Color.decode(lightGray));
		monthDropdown.setFont(FontLoader.poppinsRegular20f);
		monthDropdown.setPreferredSize(new Dimension(150,40));
		monthDropdown.setBorder(null);
		monthDropdown.setFocusable(false);
		monthDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
		monthDropdown.setUI(new ModernComboBoxUI());
		
		GridBagConstraints monthDropdownGBC = new GridBagConstraints();
		monthDropdownGBC.gridx = 0;
		monthDropdownGBC.gridy = 2;
		monthDropdownGBC.anchor = GridBagConstraints.WEST;
		monthDropdownGBC.insets = new Insets (0,0,0,0);
		menuBar.add(monthDropdown, monthDropdownGBC);
		
		// year dropdown
		yearDropdown.setBackground(Color.decode(lightGray));
		yearDropdown.setFont(FontLoader.poppinsRegular20f);
		yearDropdown.setPreferredSize(new Dimension(100,40));
		yearDropdown.setBorder(null);
		yearDropdown.setFocusable(false);
		yearDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
		yearDropdown.setUI(new ModernComboBoxUI());
		
		GridBagConstraints yearDropdownGBC = new GridBagConstraints();
		yearDropdownGBC.gridx = 0;
		yearDropdownGBC.insets = new Insets (0,0,0,0);
		yearDropdownGBC.anchor = GridBagConstraints.EAST;
		menuBar.add(yearDropdown, yearDropdownGBC);
		
		//submit leave button
		ImageIcon leaveRequestIcon = new ImageIcon("resources/images/employee/leave-request-button.png");
		JButton submitLeaveRequestButton = new JButton(leaveRequestIcon);
		submitLeaveRequestButton.setFocusPainted(false);
		submitLeaveRequestButton.setBorder(null);
		submitLeaveRequestButton.setContentAreaFilled(false); 
		
		GridBagConstraints submitLeaveRequestButtonGBC = new GridBagConstraints();
		submitLeaveRequestButtonGBC.gridx = 0;
		submitLeaveRequestButtonGBC.gridy = 5;
		submitLeaveRequestButtonGBC.anchor = GridBagConstraints.WEST;
		submitLeaveRequestButtonGBC.insets = new Insets (70,-15,0,0);
		menuBar.add(submitLeaveRequestButton, submitLeaveRequestButtonGBC);
		
		// overtime button
		ImageIcon submitOvertimeIcon = new ImageIcon("resources/images/employee/overtime-button.png");
		JButton submitOvertimeButton = new JButton(submitOvertimeIcon);
		submitOvertimeButton.setSize(new Dimension(120,25));
		submitOvertimeButton.setFocusPainted(false);
		submitOvertimeButton.setBorder(null);
		submitOvertimeButton.setContentAreaFilled(false); 
		
		GridBagConstraints submitOvertimeButtonGBC = new GridBagConstraints();
		submitOvertimeButtonGBC.gridx = 0;
		submitOvertimeButtonGBC.gridy = 6;
		submitOvertimeButtonGBC.anchor = GridBagConstraints.WEST;
		submitOvertimeButtonGBC.insets = new Insets (15,-15,0,0);
		menuBar.add(submitOvertimeButton, submitOvertimeButtonGBC);
		
		// payslip button
		ImageIcon payslipIcon = new ImageIcon("resources/images/employee/payslip-button.png");
		JButton submitPayslipButton = new JButton(payslipIcon);
		submitPayslipButton.setFocusPainted(false);
		submitPayslipButton.setBorder(null);
		submitPayslipButton.setContentAreaFilled(false); 
		
		GridBagConstraints submitPayslipButtonGBC = new GridBagConstraints();
		submitPayslipButtonGBC.gridx = 0;
		submitPayslipButtonGBC.gridy = 7;
		submitPayslipButtonGBC.anchor = GridBagConstraints.WEST;
		submitPayslipButtonGBC.insets = new Insets (15,-15,0,0);
		menuBar.add(submitPayslipButton, submitPayslipButtonGBC);
		
		// update information
		ImageIcon informationIcon = new ImageIcon("resources/images/employee/my-information-button.png");
		JButton editInfoButton = new JButton(informationIcon);
		editInfoButton.setFocusPainted(false);
		editInfoButton.setBorder(null);
		editInfoButton.setContentAreaFilled(false); 
		
		GridBagConstraints editInfoButtonGBC = new GridBagConstraints();
		editInfoButtonGBC.gridx = 0;
		editInfoButtonGBC.gridy = 8;
		editInfoButtonGBC.anchor = GridBagConstraints.WEST;
		editInfoButtonGBC.insets = new Insets (15,-15,0,0);
		menuBar.add(editInfoButton, editInfoButtonGBC);
		
		// logout button
		ImageIcon logoutIcon = new ImageIcon("resources/images/employee/employee-logout-button.png");
		JButton logoutButton = new JButton(logoutIcon);
		logoutButton.setFocusPainted(false);
		logoutButton.setBorder(null);
		logoutButton.setContentAreaFilled(false); 
		GridBagConstraints logoutButtonGBC = new GridBagConstraints();
		logoutButtonGBC.gridx = 0;
		logoutButtonGBC.gridy = 9;
		logoutButtonGBC.anchor = GridBagConstraints.WEST;
		logoutButtonGBC.insets = new Insets (-40,-30,-200,0);
		menuBar.add(logoutButton, logoutButtonGBC);
		
		//-----------------------------------------------------------------------------
		
		// content panel
		contentPanel.setBackground(Color.decode(lightGray));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		// titlePanel
		titlePanel.setBorder(new EmptyBorder(5, 20, -5, 20));
		titlePanel.setBackground(Color.decode(lightGray));
		contentPanel.add(titlePanel, BorderLayout.NORTH);
		
		//title label
		titleLabel.setText("My Dashboard");
		titleLabel.setFont(FontLoader.poppinsBold40f);
		titleLabel.setForeground(Color.decode(navyBlue));
		GridBagConstraints titleLabelGBC = new GridBagConstraints();
		titleLabelGBC.gridx = 0;
		titleLabelGBC.gridy = 0;
		titleLabelGBC.insets = new Insets (10,0,0,450);
		titlePanel.add(titleLabel, titleLabelGBC);
		
		// profilename
		profileName.setFont(FontLoader.poppinsRegularUserFont);
		profileName.setForeground(Color.decode(navyBlue));
		profileName.setText(employeeGI.getFirstName() + " " + employeeGI.getLastName());
		GridBagConstraints profileNameGBC = new GridBagConstraints();
		profileNameGBC.gridx = 1;
		profileNameGBC.gridy = 0;
		profileNameGBC.insets = new Insets (0,0,0,0);
		titlePanel.add(profileName, profileNameGBC);
		
		// profile picture
		ImageIcon profilePicImage = new ImageIcon ("resources/images/profile-pic-user.png");
		profilePic = new JLabel(profilePicImage);
		GridBagConstraints profilePicImageGBC = new GridBagConstraints();
		profilePicImageGBC.gridx = 2;
		profilePicImageGBC.gridy = 0;
		profilePicImageGBC.insets = new Insets (0,0,0,0);
		titlePanel.add(profilePic, profilePicImageGBC);
		
		//-----------------------------------------------------------------
		
		// informationPanel
		informationPanel.setBackground(Color.decode(lightGray));
		contentPanel.add(informationPanel, BorderLayout.CENTER);
		
		// gross salary
		grossSalaryPanel.setBackground(Color.WHITE);
		GridBagConstraints grossSalaryPanelGBC = new GridBagConstraints();
		grossSalaryPanelGBC.gridx = 0;
		grossSalaryPanelGBC.gridy = 0;
		grossSalaryPanelGBC.gridwidth = 1;
		grossSalaryPanelGBC.gridheight = 1;
		grossSalaryPanelGBC.insets = new Insets (5,5,5,5);
		informationPanel.add(grossSalaryPanel, grossSalaryPanelGBC);
		
		// allowances 
		allowancesPanel.setBackground(Color.WHITE);
		GridBagConstraints allowancesPanelGBC = new GridBagConstraints();
		allowancesPanelGBC.gridx = 0;
		allowancesPanelGBC.gridy = 1;
		allowancesPanelGBC.gridwidth = 1;
		allowancesPanelGBC.gridheight = 1;
		allowancesPanelGBC.insets = new Insets (0,5,5,5);
		informationPanel.add(allowancesPanel, allowancesPanelGBC);
		
		// address
		addressPanel.setBackground(Color.WHITE);
		GridBagConstraints addressPanelGBC = new GridBagConstraints();
		addressPanelGBC.gridx = 0;
		addressPanelGBC.gridy = 2;
		addressPanelGBC.gridwidth= 2;
		addressPanelGBC.gridheight= 1;
		addressPanelGBC.insets = new Insets (0,5,5,5);
		informationPanel.add(addressPanel, addressPanelGBC);
		
		// net salary
		netSalaryPanel.setBackground(Color.WHITE);
		GridBagConstraints netSalaryPanelGBC = new GridBagConstraints();
		netSalaryPanelGBC.gridx = 1;
		netSalaryPanelGBC.gridy = 0;
		netSalaryPanelGBC.gridwidth= 1;
		netSalaryPanelGBC.gridheight= 2;
		netSalaryPanelGBC.insets = new Insets (5,5,5,5);
		informationPanel.add(netSalaryPanel, netSalaryPanelGBC);
		
		// my information
		myInformationPanel.setBackground(Color.WHITE);
		GridBagConstraints myInformationPanelGBC = new GridBagConstraints();
		myInformationPanelGBC.gridx = 2;
		myInformationPanelGBC.gridy = 0;
		myInformationPanelGBC.gridwidth= 1;
		myInformationPanelGBC.gridheight= 1;
		myInformationPanelGBC.insets = new Insets (5,5,5,5);
		informationPanel.add(myInformationPanel, myInformationPanelGBC);
		
		// employment
		employmentPanel.setBackground(Color.WHITE);
		GridBagConstraints employmentPanelGBC = new GridBagConstraints();
		employmentPanelGBC.gridx = 2;
		employmentPanelGBC.gridy = 1;
		employmentPanelGBC.gridwidth= 1;
		employmentPanelGBC.gridheight= 1;
		employmentPanelGBC.insets = new Insets (0,5,5,5);
		informationPanel.add(employmentPanel, employmentPanelGBC);
		
		// government id's
		governmentIDPanel.setBackground(Color.WHITE);
		GridBagConstraints governmentIDPanelGBC = new GridBagConstraints();
		governmentIDPanelGBC.gridx = 2;
		governmentIDPanelGBC.gridy = 2;
		governmentIDPanelGBC.gridwidth= 1;
		governmentIDPanelGBC.gridheight= 1;
		governmentIDPanelGBC.insets = new Insets  (0,5,5,5);
		informationPanel.add(governmentIDPanel, governmentIDPanelGBC);
		
		// gross salary layout
		
		grossSalaryComputationLabel.setFont(FontLoader.poppinsSemiBold24f); // NOI18N
		grossSalaryComputationLabel.setText("Gross Salary");
		hoursRenderedLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		hoursRenderedLabel.setText("Hours Rendered");
		hourlyRateLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		hourlyRateLabel.setText("Hourly Rate");
		grossSalaryLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		grossSalaryLabel.setText("Gross Salary");
		hourlyRateLabel1.setFont(FontLoader.poppinsRegular14f); // NOI18N
		hourlyRateLabel1.setText(Double.toString(employeeComp.getHourlyRate()));
		grossSalaryValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		grossSalaryValue.setText(" ");
		hoursRenderedValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		hoursRenderedValue.setText(" ");
		
		GroupLayout grossSalaryPanelGL = new GroupLayout(grossSalaryPanel);
		grossSalaryPanel.setLayout(grossSalaryPanelGL);

		// Define horizontal group (2 columns)
		grossSalaryPanelGL.setHorizontalGroup(
				grossSalaryPanelGL.createSequentialGroup()
				.addGap(30)
		        .addGroup(grossSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(grossSalaryComputationLabel)
		            .addComponent(hoursRenderedLabel)
		            .addComponent(hourlyRateLabel)
		            .addComponent(grossSalaryLabel)
		        )
		        .addGap(50)
		        .addGroup(grossSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addGap(30)  
		            .addComponent(hoursRenderedValue)
		            .addComponent(hourlyRateLabel1)
		            .addComponent(grossSalaryValue)
		        )
		        .addGap(30)
		);

		// Define vertical group (4 rows)
		grossSalaryPanelGL.setVerticalGroup(
				grossSalaryPanelGL.createSequentialGroup()
				.addGap(15)
		        .addGroup(grossSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(grossSalaryComputationLabel)
		            .addGap(0)  // No value in column 2 for this row
		        )
		        .addGap(20)
		        .addGroup(grossSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(hoursRenderedLabel)
		            .addComponent(hoursRenderedValue)
		        )
		        .addGroup(grossSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(hourlyRateLabel)
		            .addComponent(hourlyRateLabel1)
		        )
		        .addGap(30)
		        .addGroup(grossSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(grossSalaryLabel)
		            .addComponent(grossSalaryValue)
		        )
		        .addGap(30) 
		);
		
		// -------------------------------------------------------------------------
		
		// allowances layout
		allowancesLabel.setFont(FontLoader.poppinsSemiBold24f); // NOI18N
		allowancesLabel.setText("Allowances");
		riceSubsidyLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		riceSubsidyLabel.setText("Rice Subsidy");
		riceSubsidyValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		riceSubsidyValue.setText(Double.toString(employeeComp.getRiceSubsidy()));
		phoneAllowanceLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		phoneAllowanceLabel.setText("Phone Allowance");
		phoneAllowanceValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		phoneAllowanceValue.setText(Double.toString(employeeComp.getPhoneAllowance()));
		totalAllowanceLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		totalAllowanceLabel.setText("Total Allowances");
		totalAllowanceValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		totalAllowanceValue.setText(numberFormat.format(totalAllowance));
		clothingAllowanceLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		clothingAllowanceLabel.setText("Clothing Allowance");
		clothingAllowanceValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		clothingAllowanceValue.setText(Double.toString(employeeComp.getClothingAllowance()));
		
		GroupLayout allowancesPanelGL = new GroupLayout(allowancesPanel);
		allowancesPanel.setLayout(allowancesPanelGL);

		// Define horizontal group (2 columns)
		allowancesPanelGL.setHorizontalGroup(
				allowancesPanelGL.createSequentialGroup()
				.addGap(30)
		        .addGroup(allowancesPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(allowancesLabel)
		            .addComponent(riceSubsidyLabel)
		            .addComponent(phoneAllowanceLabel)
		            .addComponent(clothingAllowanceLabel)
		            .addComponent(totalAllowanceLabel)
		        )
		        .addGap(50)
		        .addGroup(allowancesPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
	        		.addGap(30) 
		            .addComponent(riceSubsidyValue)
		            .addComponent(phoneAllowanceValue)
		            .addComponent(clothingAllowanceValue)
		            .addComponent(totalAllowanceValue)
		        )
		        .addGap(30)
		);

		// Define vertical group (4 rows)
		allowancesPanelGL.setVerticalGroup(
				allowancesPanelGL.createSequentialGroup()
				.addGap(15)
		        .addGroup(allowancesPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(allowancesLabel)
		            .addGap(0)  // No value in column 2 for this row
		        )
		        .addGap(20)
		        .addGroup(allowancesPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(riceSubsidyLabel)
		            .addComponent(riceSubsidyValue)
		        )
		        .addGroup(allowancesPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
	        		.addComponent(phoneAllowanceLabel)
	        		.addComponent(phoneAllowanceValue)
		        )
		        .addGroup(allowancesPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(clothingAllowanceLabel)
		            .addComponent(clothingAllowanceValue)
		        )
		        .addGap(30)
		        .addGroup(allowancesPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(totalAllowanceLabel)
		            .addComponent(totalAllowanceValue)
		        )
		        .addGap(15)
		);
		
		// -------------------------------------------------------------------------
		
		// address layout
		address.setFont(FontLoader.poppinsSemiBold24f); // NOI18N
		address.setText("Address");
		addressValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		addressValue.setText(employeeGI.getAddress());

		GroupLayout addressPanelGL = new GroupLayout(addressPanel);
		addressPanel.setLayout(addressPanelGL);

		// Define horizontal group (2 columns)
		addressPanelGL.setHorizontalGroup(
				addressPanelGL.createSequentialGroup()
				.addGap(30)
		        .addGroup(addressPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(address)
		            .addComponent(addressValue)
		        )
		        .addGap(120)
		);

		// Define vertical group (4 rows)
		addressPanelGL.setVerticalGroup(
				addressPanelGL.createSequentialGroup()
				.addGap(15)
		        .addGroup(addressPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(address)
		        )
		        .addGap(25)
		        .addGroup(addressPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(addressValue)
		        )
		        .addGap(70)
		);

	// -------------------------------------------------------------------------
		
		// net salary layout
		netSalaryComputationLabel.setFont(FontLoader.poppinsSemiBold24f); // NOI18N
		netSalaryComputationLabel.setText("Net Salary");
		overtimeLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		overtimeLabel.setText("Overtime");
		overtimeValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		overtimeValue.setText(" ");
		sssDeductionsLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		sssDeductionsLabel.setText("SSS Deduction");
		sssDeductionsValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		sssDeductionsValue.setText(" ");
		philhealthDeductionsLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		philhealthDeductionsLabel.setText("PhilHealth Deduction");
		philhealthDeductionsValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		philhealthDeductionsValue.setText(" ");
		totalDeductionsLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		totalDeductionsLabel.setText("Total Deductions");
		totalDeductionsValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		totalDeductionsValue.setText(" ");
		pagibigDeductionsLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		pagibigDeductionsLabel.setText("Pag-ibig Deduction");
		pagibigDeductionsValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		pagibigDeductionsValue.setText(" ");
		taxableSalaryLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		taxableSalaryLabel.setText("Taxable Salary");
		taxableSalaryValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		taxableSalaryValue.setText(" ");
		withHoldingTaxLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		withHoldingTaxLabel.setText("Withholding Tax");
		withHoldingTaxValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		withHoldingTaxValue.setText(" ");
		salaryAfterTaxLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		salaryAfterTaxLabel.setText("Salary After Tax");
		salaryAfterTaxValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		salaryAfterTaxValue.setText(" ");
		totalAllowancesLabel1.setFont(FontLoader.poppinsRegular14f); // NOI18N
		totalAllowancesLabel1.setText("Total Allowances");
		totalAllowancesValue1.setFont(FontLoader.poppinsRegular14f); // NOI18N
		totalAllowancesValue1.setText(numberFormat.format(totalAllowance));
		grossSalaryLabel1.setFont(FontLoader.poppinsRegular14f); // NOI18N
		grossSalaryLabel1.setText("Gross Salary");
		grossSalaryValue1.setFont(FontLoader.poppinsRegular14f); // NOI18N
		grossSalaryValue1.setText(" ");
		netSalaryLabel.setFont(FontLoader.poppinsRegular14f); // NOI18N
		netSalaryLabel.setText("Net Salary");
		netSalaryValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		netSalaryValue.setText(" ");
		
		GroupLayout netSalaryPanelGL = new GroupLayout(netSalaryPanel);
		netSalaryPanel.setLayout(netSalaryPanelGL);

		// Define horizontal group (2 columns)
		netSalaryPanelGL.setHorizontalGroup(
				netSalaryPanelGL.createSequentialGroup()
				.addGap(30)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(netSalaryComputationLabel)
		            .addComponent(overtimeLabel)
		            .addComponent(sssDeductionsLabel)
		            .addComponent(philhealthDeductionsLabel)
		            .addComponent(pagibigDeductionsLabel)
		            .addComponent(totalDeductionsLabel)
		            .addComponent(taxableSalaryLabel)
		            .addComponent(withHoldingTaxLabel)
		            .addComponent(salaryAfterTaxLabel)
		            .addComponent(totalAllowancesLabel1)
		            .addComponent(grossSalaryLabel1)
		            .addComponent(netSalaryLabel)
		        )
		        .addGap(50)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addGap(30)  
		            .addComponent(overtimeValue)
		            .addComponent(sssDeductionsValue)
		            .addComponent(philhealthDeductionsValue)
		            .addComponent(pagibigDeductionsValue)
		            .addComponent(totalDeductionsValue)
		            .addComponent(taxableSalaryValue)
		            .addComponent(withHoldingTaxValue)
		            .addComponent(salaryAfterTaxValue)
		            .addComponent(totalAllowancesValue1)
		            .addComponent(grossSalaryValue1)
		            .addComponent(netSalaryValue)
		        )
		        .addGap(30)
		);

		// Define vertical group (4 rows)
		netSalaryPanelGL.setVerticalGroup(
				netSalaryPanelGL.createSequentialGroup()
				.addGap(15)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(netSalaryComputationLabel)
		            .addGap(0)  // No value in column 2 for this row
		        )
		        .addGap(20)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(overtimeLabel)
		            .addComponent(overtimeValue)
		        )
		        .addGap(20)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(sssDeductionsLabel)
		            .addComponent(sssDeductionsValue)
		        )
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(philhealthDeductionsLabel)
		            .addComponent(philhealthDeductionsValue)
		        )
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(pagibigDeductionsLabel)
			            .addComponent(pagibigDeductionsValue)
			        )
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(totalDeductionsLabel)
			            .addComponent(totalDeductionsValue)
			        )
		        .addGap(20)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(taxableSalaryLabel)
			            .addComponent(taxableSalaryValue)
			        )
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(withHoldingTaxLabel)
			            .addComponent(withHoldingTaxValue)
			        )
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(salaryAfterTaxLabel)
			            .addComponent(salaryAfterTaxValue)
			        )
		        .addGap(20)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(totalAllowancesLabel1)
		        		.addComponent(totalAllowancesValue1)
		        		)
		        .addGap(20)
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(grossSalaryLabel1)
			            .addComponent(grossSalaryValue1)
			        )
		        .addGroup(netSalaryPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(netSalaryLabel)
			            .addComponent(netSalaryValue)
			        )
		        .addGap(15)
		);

		// -------------------------------------------------------------------------

		// my information layout
		myInformationLabel.setText("My Information");
		myInformationLabel.setFont(FontLoader.poppinsSemiBold24f);
		firstName.setFont(FontLoader.poppinsRegular14f); // NOI18N
		firstName.setText("First Name");
		firstNameValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		firstNameValue.setText(employeeGI.getFirstName());
		lastName.setFont(FontLoader.poppinsRegular14f); // NOI18N
		lastName.setText("Last Name");
		lastNameValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		lastNameValue.setText(employeeGI.getLastName());
		birthday.setFont(FontLoader.poppinsRegular14f); // NOI18N
		birthday.setText("Birthday");
		birthdayValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		birthdayValue.setText(employeeGI.getBirthday());
		phoneNumber.setFont(FontLoader.poppinsRegular14f);// NOI18N
		phoneNumber.setText("Phone Number");
		phoneNumberValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		phoneNumberValue.setText(employeeGI.getPhoneNumber());
		
		GroupLayout myInformationPanelGL = new GroupLayout(myInformationPanel);
		myInformationPanel.setLayout(myInformationPanelGL);

		// Define horizontal group (2 columns)
		myInformationPanelGL.setHorizontalGroup(
				myInformationPanelGL.createSequentialGroup()
				.addGap(30)
		        .addGroup(myInformationPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(myInformationLabel)
		            .addComponent(firstName)
		            .addComponent(lastName)
		            .addComponent(birthday)
		            .addComponent(phoneNumber)
		        )
		        .addGap(30)
		        .addGroup(myInformationPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addGap(30)
		            .addComponent(firstNameValue)
		            .addComponent(lastNameValue)
		            .addComponent(birthdayValue)
		            .addComponent(phoneNumberValue)
		        )
		        .addGap(30)
		);

		// Define vertical group (4 rows)
		myInformationPanelGL.setVerticalGroup(
				myInformationPanelGL.createSequentialGroup()
				.addGap(15)
		        .addGroup(myInformationPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(myInformationLabel)
		            .addGap(0)
		        )
		        .addGap(30)
		        .addGroup(myInformationPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(firstName)
		        		.addComponent(firstNameValue)
        		)
		        .addGroup(myInformationPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(lastName)
		        		.addComponent(lastNameValue)
        		)
		        .addGroup(myInformationPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(birthday)
		        		.addComponent(birthdayValue)
        		)
		        .addGroup(myInformationPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(phoneNumber)
		        		.addComponent(phoneNumberValue)
        		)
		        .addGap(30)
		 
		);
		

		// -------------------------------------------------------------------------
		
		// employment layout
		JLabel employmentLabel = new JLabel ("Employment");
		employmentLabel.setFont(FontLoader.poppinsSemiBold24f);
		status.setFont(FontLoader.poppinsRegular14f); // NOI18N
		status.setText("Status");
		statusValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		statusValue.setText(employeeGI.getStatus());
		position.setFont(FontLoader.poppinsRegular14f); // NOI18N
		position.setText("Position");
		positionValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		positionValue.setText(employeeGI.getPosition());
		immediateSupervisor.setFont(FontLoader.poppinsRegular14f); // NOI18N
		immediateSupervisor.setText("Immediate Supervisor");
		immediateSupervisorValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		immediateSupervisorValue.setText(employeeGI.getSupervisor());
		hourlyRate.setFont(FontLoader.poppinsRegular14f);// NOI18N
		hourlyRate.setText("Hourly Rate");
		hourlyRateValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		hourlyRateValue.setText(Double.toString(employeeComp.getHourlyRate()));
		
		GroupLayout employmentPanelGL = new GroupLayout(employmentPanel);
		employmentPanel.setLayout(employmentPanelGL);

		// Define horizontal group (2 columns)
		employmentPanelGL.setHorizontalGroup(
				employmentPanelGL.createSequentialGroup()
				.addGap(30)
		        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(employmentLabel)
		            .addComponent(status)
		            .addComponent(position)
		            .addComponent(immediateSupervisor)
		            .addComponent(hourlyRate)
		        )
		        .addGap(50)
		        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		        		.addGap(30)
		        		.addComponent(statusValue)
		        		.addComponent(positionValue)
		        		.addComponent(immediateSupervisorValue)
		        		.addComponent(hourlyRateValue)
        		)
		        .addGap(35)
		);

		// Define vertical group (4 rows)
		employmentPanelGL.setVerticalGroup(
				employmentPanelGL.createSequentialGroup()
				.addGap(15)
		        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(employmentLabel)
		            .addGap(0)
		        )
		        .addGap(40)
		        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(status)
		            .addComponent(statusValue)
		        )
		        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(position)
		            .addComponent(positionValue)
		        )
		        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
	        		.addComponent(immediateSupervisor)
		            .addComponent(immediateSupervisorValue)
		        )
		        .addGroup(employmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
	        		.addComponent(hourlyRate)
		            .addComponent(hourlyRateValue)
		        )
		        .addGap(30)
		);

		// -------------------------------------------------------------------------
		
		// deduction layout
		JLabel governmentPanelLabel = new JLabel ("Government ID's");
		governmentPanelLabel.setFont(FontLoader.poppinsSemiBold24f);
		sssNumber.setFont(FontLoader.poppinsRegular14f); // NOI18N
		sssNumber.setText("SSS Number");
		sssNumberValue.setFont(FontLoader.poppinsRegular14f);// NOI18N
		sssNumberValue.setText(employeeGI.getSSSNumber());
		philhealthNumber.setFont(FontLoader.poppinsRegular14f); // NOI18N
		philhealthNumber.setText("PhilHealth Number");
		philhealthNumberValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		philhealthNumberValue.setText(employeeGI.getPhilHealthNumber());
		pagibigNumber.setFont(FontLoader.poppinsRegular14f); // NOI18N
		pagibigNumber.setText("Pag-ibig Number");
		pagibigNumberValue.setFont(FontLoader.poppinsRegular14f);// NOI18N
		pagibigNumberValue.setText(employeeGI.getPagibigNumber());
		tinNumber.setFont(FontLoader.poppinsRegular14f); // NOI18N
		tinNumber.setText("TIN Number");
		tinNumberValue.setFont(FontLoader.poppinsRegular14f); // NOI18N
		tinNumberValue.setText(employeeGI.getTinNumber());
		
		GroupLayout governmentPanelGL = new GroupLayout(governmentIDPanel);
		governmentIDPanel.setLayout(governmentPanelGL);

		// Define horizontal group (2 columns)
		governmentPanelGL.setHorizontalGroup(
				governmentPanelGL.createSequentialGroup()
				.addGap(30)
		        .addGroup(governmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(governmentPanelLabel)
		            .addComponent(sssNumber)
		            .addComponent(philhealthNumber)
		            .addComponent(pagibigNumber)
		            .addComponent(hourlyRate)
		        )
		        .addGroup(governmentPanelGL.createParallelGroup(GroupLayout.Alignment.LEADING)
		        		.addGap(30)
		        		.addComponent(sssNumberValue)
		        		.addComponent(philhealthNumberValue)
		        		.addComponent(pagibigNumberValue)
		        		.addComponent(hourlyRateValue)
        		)
		        .addGap(20)
		);

		// Define vertical group (4 rows)
		governmentPanelGL.setVerticalGroup(
				governmentPanelGL.createSequentialGroup()
				.addGap(15)
		        .addGroup((governmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(governmentPanelLabel)
		        		.addGap(0))
		        )
		        .addGap(20)
		        .addGroup(governmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(sssNumber)
		            .addComponent(sssNumberValue)
		        )
		        .addGroup(governmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
	        		.addComponent(philhealthNumber)
	        		.addComponent(philhealthNumberValue)
        		)
		        .addGroup(governmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
	        		.addComponent(pagibigNumber)
	        		.addComponent(pagibigNumberValue)
        		)
		        .addGroup(governmentPanelGL.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(hourlyRate)
		        		.addComponent(hourlyRateValue)
        		)
		        .addGap(15)
		);

		// -------------------------------------------------------------------------

		monthDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March",
				"April", "May", "June", "July", "August", "September", "October", "November", "December" }));

		monthDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monthDropdownActionPerformed(e);
			}
		});

		// Initialize year dropdown with years from 2000 to present
		int currentYear = LocalDate.now().getYear();
		String[] years = new String[currentYear - 2000 + 1];
		for (int i = 0; i < years.length; i++) {
			years[i] = String.valueOf(2000 + i);
		}
		yearDropdown.setModel(new javax.swing.DefaultComboBoxModel<>(years));
		yearDropdown.setSelectedItem(String.valueOf(currentYear)); // Set current year as default

		yearDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yearDropdownActionPerformed(e);
			}
		});

		computeButton.setText("Compute");
		computeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					computeButtonActionPerformed(evt);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		});

		submitLeaveRequestButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backToEmployeeListButtonActionPerformed(evt);
			}
		});
		
		submitOvertimeButton.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent evt) {
		        submitOvertimeButtonActionPerformed(evt);
		    }
		});
		
		submitPayslipButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				viewPayslipButtonActionPerformed(evt);
			}
		});
		
		editInfoButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editInfoButtonActionPerformed(evt);
			}
		});
		

		welcomeLabel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
		welcomeLabel.setText("Welcome, " + employeeGI.getLastName() + ".");

		logoutButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logoutButtonActionPerformed(evt);
			}
		});

		pack();

		// Must be called after setting pack
		setSize(1366,768);
		setLocationRelativeTo(null);
	}
	
	// </editor-fold>
	
	private void submitOvertimeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		OvertimeDialog dialog = new OvertimeDialog(this);
	    dialog.setVisible(true); // Show the dialog

	    // Get the overtime hours from the dialog
	    overtimeHours = dialog.getOvertimeHours();
	    overtimeValue.setText(Double.toString(overtimeHours));
	    
	    // Optionally, you can display the entered overtime hours or process it further
	    System.out.println("Overtime submitted: " + overtimeHours + " hours");
	}

	// @SuppressWarnings("serial")
	public class OvertimeDialog extends JDialog {
	    private JTextField overtimeField;
	    private JButton submitButton;
	    private double overtimeHours;

	    public OvertimeDialog(JFrame parent) {
	        super(parent, "Submit Overtime", true);
	        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

	        overtimeField = new JTextField(10);
	        submitButton = new JButton("Submit");

	        add(new JLabel("Overtime in Hours:"));
	        add(overtimeField);
	        add(submitButton);

	        submitButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                    overtimeHours = Double.parseDouble(overtimeField.getText());
	                    dispose(); // Close the dialog
	                } catch (NumberFormatException ex) {
	                    JOptionPane.showMessageDialog(OvertimeDialog.this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        });

	        pack();
	        setLocationRelativeTo(parent); // Center the dialog
	    }

	    public double getOvertimeHours() {
	        return overtimeHours;
	    }
	}
	
	private void computeButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {

		// Reset values for hours rendered
		resetSummaryValues();

		// Compute for the hoursRendered using database instead of JSON
		calculateDaysWorkedFromDatabase(employeeGI.getEmployeeNumber(), selectedMonth);

		// Compute for the Gross Salary
		Double grossSalary = computeGrossSalary();

		grossSalaryValue.setText(numberFormat.format(computeGrossSalary()));
		grossSalaryValue1.setText(numberFormat.format(computeGrossSalary()));

		// Compute for deductions
		Double sssDeduction = SalaryCalculator.getSSS(grossSalary);
		Double philhealthDeduction = SalaryCalculator.getPhilHealth(grossSalary);
		Double pagibigDeduction = SalaryCalculator.getPagibig(grossSalary);
		Double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;

		sssDeductionsValue.setText(numberFormat.format(sssDeduction));
		philhealthDeductionsValue.setText(numberFormat.format(philhealthDeduction));
		pagibigDeductionsValue.setText(numberFormat.format(pagibigDeduction));
		totalDeductionsValue.setText(numberFormat.format(totalDeductions));

		// Compute for Taxable Salary
		Double taxableSalary = grossSalary - totalDeductions;

		taxableSalaryValue.setText(numberFormat.format(taxableSalary));

		// Compute for Withholding Tax
		Double withholdingTax = SalaryCalculator.getWithholding(taxableSalary);

		withHoldingTaxValue.setText(numberFormat.format(withholdingTax));

		// Compute for Salary After Tax
		Double salaryAfterTax = taxableSalary - withholdingTax;

		salaryAfterTaxValue.setText(numberFormat.format(salaryAfterTax));

		// Compute for Net Salary
		
		Double totalOvertimeCost = grossSalary * overtimeHours;
		
		Double netSalary = salaryAfterTax + totalAllowance + totalOvertimeCost;

		netSalaryValue.setText(numberFormat.format(netSalary));
		
		overtimeValue.setText(Double.toString(overtimeHours));
	}
	
	private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				new LoginPage().setVisible(true);
			}
		});
	}

	private void backToEmployeeListButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Go back to the employee list page
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				new LeaveRequestPage(employeeGI, employeeComp).setVisible(true);
			}
		});
	}
	
	private void viewPayslipButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Calculate days worked from database before showing payslip
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Calculate days worked for the selected month from database
				int daysWorked = calculateDaysWorkedFromDatabase(employeeGI.getEmployeeNumber(), selectedMonth);
				
				// Ensure we have computed all values before showing payslip
				if (grossSalaryValue.getText().trim().isEmpty() || grossSalaryValue.getText().equals(" ")) {
					// If gross salary is not computed yet, compute it first
					try {
						computeButtonActionPerformed(null);
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(EmployeeDashboard.this, 
							"Error calculating salary. Please try clicking Compute first.", 
							"Calculation Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				// Remove the EmployeesPage Window
				dispose();

				 new ViewPayslipPage(
			        employeeGI.getEmployeeNumber(),
			        employeeGI.getFirstName() + " " + employeeGI.getLastName(),
			        employeeGI.getAddress(),
			        employeeGI.getSupervisor(),
			        employeeGI.getSSSNumber(),
			        employeeGI.getPhilHealthNumber(),
			        employeeGI.getPhoneNumber(),
			        employeeGI.getPosition(),
			        employeeGI.getTinNumber(),
			        employeeGI.getPagibigNumber(),
			        numberFormat.format(employeeComp.getHourlyRate()),
			        String.valueOf(daysWorked), // Days worked from database calculation
			        Double.toString(overtimeHours), // Overtime
			        numberFormat.format(employeeComp.getRiceSubsidy()),
			        numberFormat.format(employeeComp.getPhoneAllowance()),
			        numberFormat.format(employeeComp.getClothingAllowance()),
			        sssDeductionsValue.getText(),
			        philhealthDeductionsValue.getText(),
			        pagibigDeductionsValue.getText(),
			        withHoldingTaxValue.getText(),
			        totalDeductionsValue.getText(),
			        grossSalaryValue.getText(),
			        netSalaryValue.getText()
			    ).setVisible(true);
			}
		});
	}

	private void monthDropdownActionPerformed(java.awt.event.ActionEvent evt) {

		// Reset values for hours rendered
		resetSummaryValues();

		// Get the selected item
		selectedMonth = ((String) monthDropdown.getSelectedItem()).toUpperCase();
	}

	private void yearDropdownActionPerformed(java.awt.event.ActionEvent evt) {

		// Reset values for hours rendered
		resetSummaryValues();

		// Get the selected item
		selectedYear = Integer.parseInt((String) yearDropdown.getSelectedItem());
	}

	@SuppressWarnings("unused")
	public void loadAttendanceRecordsFromJsonFile(String filePath) throws IOException {

		// Load the JSON file as a JsonArray
		JsonArray jsonArray = JsonFileHandler.getAttendanceJSON(filePath);

		// Loop through each element in the array and create an AttendanceData object for each one
		for (JsonElement element : jsonArray) {

			JsonObject attendanceJson = element.getAsJsonObject();
			String employeeNum = attendanceJson.get("employeeNum").getAsString();
			LocalDateTime month = SalaryCalculator.getTimeInOrOut(attendanceJson, "time_in");

			if (employeeNum.equals(employeeGI.getEmployeeNumber()) && 
				month.getMonth().equals(Month.valueOf(selectedMonth))) {

				String date = attendanceJson.get("date").getAsString();
				String timeIn = attendanceJson.get("time_in").getAsString();
				String timeOut = attendanceJson.get("time_out").getAsString();
				String hoursRendered = SalaryCalculator.getAttendance(attendanceJson, Month.valueOf(selectedMonth),
						presentsNum, latesNum, absentsNum, hoursRenderedNum);
				String present = isPresent(hoursRendered);
			}
		}

		// Set the hours rendered to the label
		hoursRenderedValue.setText(hoursRenderedNum.toString());
	}

	public Double computeGrossSalary() {
		return hoursRenderedNum.get() * employeeComp.getHourlyRate();
	}

	public String isPresent(String hoursRendered) {
		if (!hoursRendered.equals("0"))
			return "Present";
		return "Absent";
	}

	public void resetSummaryValues() {
		this.absentsNum = new AtomicInteger(0);
		this.hoursRenderedNum = new AtomicInteger(0);
		this.latesNum = new AtomicInteger(0);
		this.presentsNum = new AtomicInteger(0);
	}

	/**
	 * Calculate days worked from PostgreSQL attendance table for the logged-in employee
	 * @param employeeNumber Employee number
	 * @param selectedMonth Selected month (e.g., "JANUARY", "FEBRUARY")
	 * @return Number of days worked in the selected month
	 */
	public int calculateDaysWorkedFromDatabase(String employeeNumber, String selectedMonth) {
		try {
			// Get the selected year and the selected month
			Month month = Month.valueOf(selectedMonth);
			
			// Calculate the first and last day of the selected month
			YearMonth yearMonth = YearMonth.of(selectedYear, month);
			LocalDate firstDay = yearMonth.atDay(1);
			LocalDate lastDay = yearMonth.atEndOfMonth();
			
			// Format dates for database query (database uses yyyy-MM-dd format)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String startDate = firstDay.format(formatter);
			String endDate = lastDay.format(formatter);
			
			// Get attendance records for the employee within the date range
			var attendanceRecords = AttendanceDAO.getAttendanceByDateRange(startDate, endDate);
			
			// Reset summary values
			resetSummaryValues();
			
			// Count days worked and calculate hours for the specific employee
			int daysWorked = 0;
			
			for (AttendanceDAO.AttendanceRecord record : attendanceRecords) {
				// Check if this record belongs to the logged-in employee
				if (String.valueOf(record.getEmployeeNum()).equals(employeeNumber)) {
					try {
						// Parse time_in and time_out
						String timeIn = record.getTimeIn();
						String timeOut = record.getTimeOut();
						
						// Skip if either time is null or empty
						if (timeIn == null || timeOut == null || 
							timeIn.trim().isEmpty() || timeOut.trim().isEmpty()) {
							absentsNum.incrementAndGet();
							continue;
						}
						
						// Parse the time strings to calculate hours worked
						DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						// Handle database date format (yyyy-MM-dd)
						LocalDateTime timeInDateTime = LocalDate.parse(record.getDate(), formatter)
								.atTime(java.time.LocalTime.parse(timeIn, timeFormatter));
						LocalDateTime timeOutDateTime = LocalDate.parse(record.getDate(), formatter)
								.atTime(java.time.LocalTime.parse(timeOut, timeFormatter));
						
						// Calculate duration in minutes
						long minutes = java.time.Duration.between(timeInDateTime, timeOutDateTime).toMinutes();
						long hours = java.time.Duration.between(timeInDateTime, timeOutDateTime).toHours();
						
						// Determine if employee was present, late, or absent
						if (minutes > 0) {
							daysWorked++;
							presentsNum.incrementAndGet();
							
							// Check if late (less than 8.5 hours but more than 8 hours)
							if (minutes < 530 && minutes > 0) {
								latesNum.incrementAndGet();
							}
							
							// Add to total hours rendered
							if (minutes > 529 && minutes < 540) {
								hoursRenderedNum.addAndGet((int) hours + 1);
							} else {
								hoursRenderedNum.addAndGet((int) hours);
							}
						} else {
							absentsNum.incrementAndGet();
						}
						
					} catch (Exception e) {
						System.err.println("Error parsing attendance record for date " + record.getDate() + ": " + e.getMessage());
						absentsNum.incrementAndGet();
					}
				}
			}
			
			// Update the UI with calculated values
			hoursRenderedValue.setText(hoursRenderedNum.toString());
			
			System.out.println("Days worked in " + selectedMonth + ": " + daysWorked);
			System.out.println("Total hours rendered: " + hoursRenderedNum.get());
			System.out.println("Present days: " + presentsNum.get());
			System.out.println("Late days: " + latesNum.get());
			System.out.println("Absent days: " + absentsNum.get());
			
			return daysWorked;
			
		} catch (Exception e) {
			System.err.println("Error calculating days worked from database: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Get days worked for the currently logged-in employee for a specific month
	 * This method can be called from the payslip view
	 * @param month Month name (e.g., "JANUARY", "FEBRUARY")
	 * @return Number of days worked
	 */
	public int getDaysWorkedForEmployee(String month) {
		return calculateDaysWorkedFromDatabase(employeeGI.getEmployeeNumber(), month);
	}

	/**
	 * Static utility method to calculate days worked for any employee for any month
	 * Can be used by other classes without requiring an EmployeeDashboard instance
	 * @param employeeNumber Employee number
	 * @param selectedMonth Selected month (e.g., "JANUARY", "FEBRUARY")
	 * @param currentYear Year to calculate for (use current year if unsure)
	 * @return Number of days worked in the selected month
	 */
	public static int calculateDaysWorkedForEmployee(String employeeNumber, String selectedMonth, int currentYear) {
		try {
			// Get the selected month
			Month month = Month.valueOf(selectedMonth.toUpperCase());
			
			// Calculate the first and last day of the selected month
			YearMonth yearMonth = YearMonth.of(currentYear, month);
			LocalDate firstDay = yearMonth.atDay(1);
			LocalDate lastDay = yearMonth.atEndOfMonth();
			
			// Format dates for database query (database uses yyyy-MM-dd format)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String startDate = firstDay.format(formatter);
			String endDate = lastDay.format(formatter);
			
			// Get attendance records for the employee within the date range
			var attendanceRecords = AttendanceDAO.getAttendanceByDateRange(startDate, endDate);
			
			// Count days worked for the specific employee
			int daysWorked = 0;
			
			for (AttendanceDAO.AttendanceRecord record : attendanceRecords) {
				// Check if this record belongs to the specified employee
				if (String.valueOf(record.getEmployeeNum()).equals(employeeNumber)) {
					try {
						// Parse time_in and time_out
						String timeIn = record.getTimeIn();
						String timeOut = record.getTimeOut();
						
						// Skip if either time is null or empty
						if (timeIn == null || timeOut == null || 
							timeIn.trim().isEmpty() || timeOut.trim().isEmpty()) {
							continue;
						}
						
						// Parse the time strings to calculate hours worked
						DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						// Handle database date format (yyyy-MM-dd)
						LocalDateTime timeInDateTime = LocalDate.parse(record.getDate(), formatter)
								.atTime(java.time.LocalTime.parse(timeIn, timeFormatter));
						LocalDateTime timeOutDateTime = LocalDate.parse(record.getDate(), formatter)
								.atTime(java.time.LocalTime.parse(timeOut, timeFormatter));
						
						// Calculate duration in minutes
						long minutes = java.time.Duration.between(timeInDateTime, timeOutDateTime).toMinutes();
						
						// If employee worked (minutes > 0), count as a day worked
						if (minutes > 0) {
							daysWorked++;
						}
						
					} catch (Exception e) {
						System.err.println("Error parsing attendance record for date " + record.getDate() + ": " + e.getMessage());
					}
				}
			}
			
			System.out.println("Employee " + employeeNumber + " worked " + daysWorked + " days in " + selectedMonth + " " + currentYear);
			return daysWorked;
			
		} catch (Exception e) {
			System.err.println("Error calculating days worked for employee " + employeeNumber + ": " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Static utility method to calculate days worked for any employee for any month (uses current year)
	 * @param employeeNumber Employee number
	 * @param selectedMonth Selected month (e.g., "JANUARY", "FEBRUARY")
	 * @return Number of days worked in the selected month for current year
	 */
	public static int calculateDaysWorkedForEmployee(String employeeNumber, String selectedMonth) {
		return calculateDaysWorkedForEmployee(employeeNumber, selectedMonth, LocalDate.now().getYear());
	}
	
	private void editInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// Go to the employee edit information page
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeeDashboard Window
				dispose();

				new EmployeeEditInfoPage(employeeGI, employeeComp).setVisible(true);
			}
		});
	}
	
	public class FontLoader {

        // Public static font variable (accessible from anywhere)
        public static final Font poppinsRegular14f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 14f); // regular text size
        public static final Font poppinsRegular20f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 20f); // regular text size
        public static final Font poppinsRegular18f= loadCustomFont("resources/fonts/Poppins-Regular.ttf", 16f); // regular text size
        public static final Font poppinsRegularUserFont = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 20f); // profile name
        public static final Font poppinsSemiBold24f = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 24f); // panel title
        public static final Font poppinsBold40f = loadCustomFont("resources/fonts/Poppins-Bold.ttf", 40f); // title 

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
	
	// rounded panel
	static class RoundedPanel extends JPanel {
	    private int cornerRadius;
	    private Color borderColor = Color.WHITE; // Default border color
	    private int borderThickness = 2;         // Border thickness

	    public RoundedPanel(int radius) {
	        this.cornerRadius = radius;
	        setOpaque(false); // Keep this false to allow rounded shape
	        setBackground(Color.WHITE); // Explicitly set background to white
	    }

	    // Setter for border color
	    public void setBorderColor(Color color) {
	        this.borderColor = color;
	        repaint();
	    }

	    // Setter for border thickness
	    public void setBorderThickness(int thickness) {
	        this.borderThickness = thickness;
	        repaint();
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D) g.create();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        int width = getWidth();
	        int height = getHeight();

	        // Draw filled rounded rectangle with the background color
	        g2.setColor(getBackground());
	        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

	        // Draw border
	        g2.setColor(borderColor);
	        g2.setStroke(new BasicStroke(borderThickness));
	        g2.drawRoundRect(
	            borderThickness / 2,
	            borderThickness / 2,
	            width - borderThickness,
	            height - borderThickness,
	            cornerRadius,
	            cornerRadius
	        );

	        g2.dispose();
	    }
	}
	
    // custom dropdown user interface
    static class ModernComboBoxUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            return new ArrowButton();
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox);

            JScrollPane scrollPane = (JScrollPane) popup.getComponents()[0];
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();

            // modern scroll bar
            scrollBar.setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    thumbColor = new Color(180, 180, 180);
                    trackColor = new Color(240, 240, 240);
                }

                @Override
                protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }

                private JButton createZeroButton() {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(0, 0));
                    button.setMinimumSize(new Dimension(0, 0));
                    button.setMaximumSize(new Dimension(0, 0));
                    return button;
                }
            });

            return popup;
        }
    }

    // custom arrow button
    static class ArrowButton extends JButton {
        public ArrowButton() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            // down ward arrow button for dropdown
            int size = 8;
            int x = (w - size) / 2;
            int y = (h - size) / 2;

            Polygon arrow = new Polygon();
            arrow.addPoint(x, y);
            arrow.addPoint(x + size, y);
            arrow.addPoint(x + size / 2, y + size);

            g2.setColor(new Color(100, 100, 100));
            g2.fill(arrow);
            g2.dispose();
        }
    }
}
