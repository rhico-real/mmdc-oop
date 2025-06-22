package GUI.admin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.text.DecimalFormat;
import Classes.Compensation;
import GUI.admin.DashboardPage.RoundedPanel;
import UtilityClasses.SalaryCalculator;

@SuppressWarnings("serial")
public class CalculatorPage extends JFrame {

	// Hourly Rate
	private double hourlyRate;
	private JLabel hourlyRateLabel = new JLabel("Hourly Rate: ");
	private JLabel hourlyRateValue = new JLabel();

	// Hours Rendered
	private double hoursRendered;
	private JLabel hoursRenderedLabel = new JLabel("Hours Rendered");
	private JTextField hoursRenderedField = new JTextField(4);

	// Gross Salary
	private double grossSalary;
	private JLabel grossSalaryLabel = new JLabel("Gross Salary ");
	private JLabel grossSalaryValue = new JLabel("");

	// Rice Subsidy
	private double riceSubsidy;
	private JLabel riceSubsidyLabel = new JLabel("Rice Subsidy ");
	private JLabel riceSubsidyValue = new JLabel("");

	// Phone Allowances
	private double phoneAllowance;
	private JLabel phoneAllowanceLabel = new JLabel("Phone Allowance ");
	private JLabel phoneAllowanceValue = new JLabel("");

	// Clothing Allowances
	private double clothingAllowance;
	private JLabel clothingAllowanceLabel = new JLabel("Clothing Allowance ");
	private JLabel clothingAllowanceValue = new JLabel("");

	// Total Allowance
	private double totalAllowance;
	private JLabel totalAllowanceLabel = new JLabel("Total ");
	private JLabel totalAllowanceValue = new JLabel("");

	// SSS Deduction
	private double sssDeductions;
	private JLabel sssDeductionsLabel = new JLabel("SSS ");
	private JLabel sssDeductionsValue = new JLabel("");

	// PhilHealth Deduction
	private double philhealthDeductions;
	private JLabel philhealthDeductionsLabel = new JLabel("PhilHealth ");
	private JLabel philhealthDeductionsValue = new JLabel("");

	// Pag-ibig Deduction
	private double pagibigDeductions;
	private JLabel pagibigDeductionsLabel = new JLabel("PagIbig ");
	private JLabel pagibigDeductionsValue = new JLabel("");

	// Total Deductions
	private double totalDeductions;
	private JLabel totalDeductionsLabel = new JLabel("Total Deductions ");
	private JLabel totalDeductionsValue = new JLabel("");

	// Taxable Salary
	private double taxableSalary;
	private JLabel taxableSalaryLabel = new JLabel("Taxable Salary ");
	private JLabel taxableSalaryValue = new JLabel("");

	// Withholding Tax
	private double withHoldingTax;
	private JLabel withHoldingTaxLabel = new JLabel("Withholding Tax ");
	private JLabel withHoldingTaxValue = new JLabel("");

	// Salary After Tax
	private double salaryAfterTax;
	private JLabel salaryAfterTaxLabel = new JLabel("Salary After Tax ");
	private JLabel salaryAfterTaxValue = new JLabel("");

	// Net Salary
	private double netSalary;
	private JLabel netSalaryTitle = new JLabel ("Calculated Net");
	private JLabel netSalaryLabel = new JLabel("Net Salary ");
	private JLabel netSalaryValue = new JLabel("");

	// Buttons
	private JButton calculateSalaryButton = new JButton("=");
	
	// Mandated Deductions Subtitle
	private JLabel mandatedSubtitle = new JLabel("Deductions");
	private JLabel taxesSubtitle = new JLabel("Taxes");
	private JLabel emptyTextLabel1 = new JLabel("");
	private JLabel emptyTextValue1 = new JLabel("");
	private JLabel emptyTextLabel2 = new JLabel(" ");
	private JLabel emptyTextValue2= new JLabel(" ");
	private JLabel emptyTextLabel3 = new JLabel("  ");
	private JLabel emptyTextValue3= new JLabel("  ");
	private JLabel emptyTextLabel4 = new JLabel("  ");
	private JLabel emptyTextValue4 = new JLabel("  ");


	// Panels
	private JPanel mainPanel;
	private JPanel menubarPanel;
	private JPanel dashboardPanel;
	private JPanel netSalaryPanel;
	private JPanel titleNetSalPanel;
	private JPanel boxesPanel;
	private JPanel grossSalaryPanel;
	private JPanel calculatorPanel;
	private JPanel resultPanel;
	private JPanel netSalaryValuePanel;

	// Headers
	private JLabel grossSalaryComputationLabel;
	private JLabel netSalaryComputationLabel;
	private JLabel allowancesLabel;

	// Duplicated Labels
	private JLabel grossSalaryLabel1;
	private JLabel grossSalaryValue1;
	private JLabel totalAllowancesLabel1;
	private JLabel totalAllowanceValue1;
	
	private RoundedPanel mandatedDedPanel = new RoundedPanel(30);
	private RoundedPanel taxesDedPanel = new RoundedPanel(30);
	private RoundedPanel allowanceDedPanel = new RoundedPanel(30);
	private RoundedPanel netSalResultPanel = new RoundedPanel(30);

	/**
	 * Creates new form CalculatorPage
	 */
	public CalculatorPage(Compensation employeeComp) {
		initComponents(employeeComp);
		// Set important data on render
		setDataOnRender(employeeComp);
	}
	
	// CUSTOM FONT ASSIGNMENT
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
	
	// BORDER THICKNESS OF ROUNDED PANEL
	static class RoundedPanel extends JPanel {
	    private int cornerRadius;
	    private Color borderColor = Color.BLACK; // Default border color
	    private int borderThickness = 2;         // Border thickness

	    public RoundedPanel(int radius) {
	        this.cornerRadius = radius;
	        setOpaque(false); // Make background transparent
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
	    
	    // ROUNDED PANEL LAYOUT
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);

	        // Enable anti-aliasing for smoother curves
	        Graphics2D g2 = (Graphics2D) g.create();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        int width = getWidth();
	        int height = getHeight();

	        // Draw filled rounded rectangle (background)
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

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	//	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents(Compensation employeeComp) {

		Font poppinsTitleBoldFont = loadCustomFont("resources/fonts/Poppins-Bold.ttf", 35f);
		Font poppinsTitleSemiBold = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 28f);
		Font poppinsRegular28f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 28f);
		Font poppinsRegular24f = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 24f);
		Font poppinsSemiBold24f = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 24f);
		Font poppinsCalculatorButton = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 35);
		Font poppinsSubTitleSemiBold = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 18f);
		Font poppinsRegularFont = loadCustomFont("resources/fonts/Poppins-Regular.ttf", 18f);
		Font poppinsBoldFont = loadCustomFont("resources/fonts/Poppins-SemiBold.ttf", 18f);
		

		grossSalaryComputationLabel = new javax.swing.JLabel();
		netSalaryComputationLabel = new javax.swing.JLabel();
		grossSalaryLabel1 = new javax.swing.JLabel();
		grossSalaryValue1 = new javax.swing.JLabel();
		totalAllowancesLabel1 = new javax.swing.JLabel();
		totalAllowanceValue1 = new javax.swing.JLabel();
		allowancesLabel = new javax.swing.JLabel();
		
		setTitle("MotorPH Payroll System | Salary Calculator");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(Color.decode("#f5f5f5"));
		add(mainPanel);

		// Menubar Panel
		menubarPanel = new JPanel(new GridBagLayout());
		menubarPanel.setBackground(Color.decode("#153969"));
		GridBagConstraints menubarGBC = new GridBagConstraints();
		menubarGBC.gridx = 0;
		menubarGBC.gridy = 0;
		menubarGBC.weightx = 1;
		menubarGBC.weighty = 0;
		menubarGBC.fill = GridBagConstraints.BOTH;
		mainPanel.add(menubarPanel, menubarGBC);

		// Dashboard Panel
		dashboardPanel = new JPanel(new GridBagLayout());
		GridBagConstraints dashboardGBC = new GridBagConstraints();
		dashboardGBC.gridx = 0;
		dashboardGBC.gridy = 1;
		dashboardGBC.weightx = 1;
		dashboardGBC.weighty = 1;
		dashboardGBC.fill = GridBagConstraints.BOTH;
		mainPanel.add(dashboardPanel, dashboardGBC);

		// Net Salary Panel
		netSalaryPanel = new JPanel(new GridBagLayout());
		netSalaryPanel.setBackground(Color.BLUE);
		GridBagConstraints netSalaryGBC = new GridBagConstraints();
		netSalaryGBC.gridx = 0;
		netSalaryGBC.gridy = 0;
		netSalaryGBC.weightx = 0.7;
		netSalaryGBC.weighty = 1;
		netSalaryGBC.fill = GridBagConstraints.BOTH;
		dashboardPanel.add(netSalaryPanel, netSalaryGBC);

		// Title Net Salary Panel
		titleNetSalPanel = new JPanel(new GridBagLayout());
		GridBagConstraints titleNetSalGBC = new GridBagConstraints();
		titleNetSalGBC.gridx = 0;
		titleNetSalGBC.gridy = 0;
		titleNetSalGBC.weightx = 1;
		titleNetSalGBC.weighty = 0.05;
		titleNetSalGBC.fill = GridBagConstraints.BOTH;
		netSalaryPanel.add(titleNetSalPanel, titleNetSalGBC);

		// Boxes Panel
		boxesPanel = new JPanel(new GridBagLayout());
		GridBagConstraints boxesGBC = new GridBagConstraints();
		boxesGBC.gridx = 0;
		boxesGBC.gridy = 1;
		boxesGBC.weightx = 1;
		boxesGBC.weighty = 0.95;
		boxesGBC.fill = GridBagConstraints.BOTH;
		netSalaryPanel.add(boxesPanel, boxesGBC);

		// Mandated Deduction Panel
		mandatedDedPanel.setBackground(Color.WHITE);
		GridBagConstraints mandatedGBC = new GridBagConstraints();
		mandatedGBC.gridx = 0;
		mandatedGBC.gridy = 0;
		mandatedGBC.weightx = 0.5;
		mandatedGBC.weighty = 0.5;
		mandatedGBC.fill = GridBagConstraints.BOTH;
		mandatedGBC.insets = new Insets (15,30,15,15);
		boxesPanel.add(mandatedDedPanel, mandatedGBC);

		// Taxes Deduction Panel
		taxesDedPanel.setBackground(Color.WHITE);
		GridBagConstraints taxesGBC = new GridBagConstraints();
		taxesGBC.gridx = 1;
		taxesGBC.gridy = 0;
		taxesGBC.weightx = 0.5;
		taxesGBC.weighty = 0.5;
		taxesGBC.fill = GridBagConstraints.BOTH;
		taxesGBC.insets = new Insets (15,15,15,15);
		boxesPanel.add(taxesDedPanel, taxesGBC);

		// Allowance Deduction Panel
		allowanceDedPanel.setBackground(Color.WHITE);
		GridBagConstraints allowanceGBC = new GridBagConstraints();
		allowanceGBC.gridx = 0;
		allowanceGBC.gridy = 1;
		allowanceGBC.weightx = 0.5;
		allowanceGBC.weighty = 0.5;
		allowanceGBC.fill = GridBagConstraints.BOTH;
		allowanceGBC.insets = new Insets (15,30,30,15);
		boxesPanel.add(allowanceDedPanel, allowanceGBC);

		// Net Salary Result Panel
		netSalResultPanel.setBackground(Color.WHITE);
		GridBagConstraints netResultGBC = new GridBagConstraints();
		netResultGBC.gridx = 1;
		netResultGBC.gridy = 1;
		netResultGBC.weightx = 0.5;
		netResultGBC.weighty = 0.5;
		netResultGBC.fill = GridBagConstraints.BOTH;
		netResultGBC.insets = new Insets (15,15,30,15);
		boxesPanel.add(netSalResultPanel, netResultGBC);

		// Gross Salary Panel
		grossSalaryPanel = new JPanel(new GridBagLayout());
		grossSalaryPanel.setBackground(Color.decode("#dbdbdb"));
		GridBagConstraints grossGBC = new GridBagConstraints();
		grossGBC.gridx = 1;
		grossGBC.gridy = 0;
		grossGBC.weightx = 0.3;
		grossGBC.weighty = 1;
		grossGBC.fill = GridBagConstraints.BOTH;
		grossGBC.insets = new Insets (15,15,15,15);
		dashboardPanel.add(grossSalaryPanel, grossGBC);

		// Calculator Panel
		calculatorPanel = new JPanel(new GridBagLayout());
		calculatorPanel.setBackground(Color.WHITE);
		GridBagConstraints calcGBC = new GridBagConstraints();
		calcGBC.gridx = 0;
		calcGBC.gridy = 0;
		calcGBC.weightx = 1;
		calcGBC.weighty = 0.7;
		calcGBC.fill = GridBagConstraints.BOTH;
		calcGBC.insets = new Insets (75,75,75,75);
		grossSalaryPanel.add(calculatorPanel, calcGBC);

		// 🔸 Result Panel
		resultPanel = new JPanel(new GridBagLayout());
		resultPanel.setBackground(Color.WHITE);
		GridBagConstraints resultGBC = new GridBagConstraints();
		resultGBC.gridx = 0;
		resultGBC.gridy = 1;
		resultGBC.weightx = 1;
		resultGBC.weighty = 0.3;
		resultGBC.fill = GridBagConstraints.BOTH;
		resultGBC.insets = new Insets (0,75,75,75);
		grossSalaryPanel.add(resultPanel, resultGBC);

		// MENUBAR LAYOUT
		
		// add the logos
		ImageIcon motorphlogoAdmin = new ImageIcon("resources/images/MotorPH-Logo.png");
        JLabel motorPHLogo = new JLabel(motorphlogoAdmin);
        ImageIcon admindisplayLogo = new ImageIcon("resources/images/Admin-Logo.png");
        JLabel adminLogo = new JLabel(admindisplayLogo);
        
        GridBagConstraints gbc_motorPHLogo = new GridBagConstraints();
        gbc_motorPHLogo.gridx = 0;
        gbc_motorPHLogo.gridy = 0;
        gbc_motorPHLogo.insets = new Insets(0, 0, 0, 460);
		menubarPanel.add(motorPHLogo, gbc_motorPHLogo);
		
		GridBagConstraints gbc_adminLogo = new GridBagConstraints();
        gbc_adminLogo.gridx = 2;
        gbc_adminLogo.gridy = 0;
        gbc_adminLogo.insets = new Insets(0, 460, 0, 0);
		menubarPanel.add(adminLogo, gbc_adminLogo);
				
		netSalaryComputationLabel.setText("Net Salary Computation");		
		netSalaryComputationLabel.setFont(poppinsTitleBoldFont); // NOI18N
		GridBagConstraints gbc_netSalaryComputationLabel = new GridBagConstraints();
		gbc_netSalaryComputationLabel.gridx = 0;
		gbc_netSalaryComputationLabel.gridy = 0;
		gbc_netSalaryComputationLabel.insets = new Insets(15, -250, 0, 0);
		titleNetSalPanel.add(netSalaryComputationLabel, gbc_netSalaryComputationLabel);
		
		
		

		// DEDUCTION BOX PANEL
		mandatedSubtitle.setFont(poppinsTitleSemiBold);
		sssDeductionsLabel.setFont(poppinsRegularFont); // NOI18N
		sssDeductionsValue.setFont(poppinsRegularFont); // NOI18N
		philhealthDeductionsLabel.setFont(poppinsRegularFont); // NOI18N
		philhealthDeductionsValue.setFont(poppinsRegularFont); // NOI18N
		pagibigDeductionsLabel.setFont(poppinsRegularFont); // NOI18N
		pagibigDeductionsValue.setFont(poppinsRegularFont); // NOI18N
		totalDeductionsLabel.setFont(poppinsBoldFont); // NOI18N
		totalDeductionsValue.setFont(poppinsRegularFont); // NOI18N
	
		GroupLayout gl_mandatedDedPanel = new GroupLayout(mandatedDedPanel);
		mandatedDedPanel.setLayout(gl_mandatedDedPanel);


		gl_mandatedDedPanel.setHorizontalGroup(
			gl_mandatedDedPanel.createSequentialGroup()
		        .addGap(30) // Left padding
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(mandatedSubtitle)
		            .addComponent(emptyTextLabel1)
		            .addComponent(sssDeductionsLabel)
		            .addComponent(philhealthDeductionsLabel)
		            .addComponent(pagibigDeductionsLabel)
		            .addComponent(emptyTextLabel1)
		            .addComponent(totalDeductionsLabel)
		        )
		        .addGap(40)
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(emptyTextValue1)
		            .addComponent(emptyTextValue1)
		            .addComponent(sssDeductionsValue)
		            .addComponent(philhealthDeductionsValue)
		            .addComponent(pagibigDeductionsValue)
		            .addComponent(emptyTextValue1)
		            .addComponent(totalDeductionsValue)
		        )
		);

		gl_mandatedDedPanel.setVerticalGroup(
			gl_mandatedDedPanel.createSequentialGroup()
		        .addGap(15) // Top padding
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(mandatedSubtitle)
		            .addGap(0)
		        )
		        .addGap(25)
		        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(sssDeductionsLabel)
		            .addComponent(sssDeductionsValue)
		        )
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(philhealthDeductionsLabel)
		            .addComponent(philhealthDeductionsValue)
		        )
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(pagibigDeductionsLabel)
		            .addComponent(pagibigDeductionsValue)
		        )
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
	        		.addComponent(emptyTextLabel1)
		            .addComponent(emptyTextValue1)
		        )
		        .addGap(25)
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addGroup(gl_mandatedDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(totalDeductionsLabel)
		            .addComponent(totalDeductionsValue)
		        )
		        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		
		// TAXES BOX PANEL
		// set font size and color
		taxesSubtitle.setFont(poppinsTitleSemiBold); // NOI18N
		taxableSalaryLabel.setFont(poppinsRegularFont); // NOI18N
		taxableSalaryValue.setFont(poppinsRegularFont); // NOI18N
		withHoldingTaxLabel.setFont(poppinsRegularFont); // NOI18N
		withHoldingTaxValue.setFont(poppinsRegularFont); // NOI18N
		salaryAfterTaxLabel.setFont(poppinsBoldFont); // NOI18N
		salaryAfterTaxValue.setFont(poppinsRegularFont); // NOI18N
		
		
		GroupLayout gl_taxesDedPanel = new GroupLayout(taxesDedPanel);
		taxesDedPanel.setLayout(gl_taxesDedPanel);

		// Define horizontal group (2 columns)
		gl_taxesDedPanel.setHorizontalGroup(
				gl_taxesDedPanel.createSequentialGroup()
				.addGap(30)
		        .addGroup(gl_taxesDedPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(taxesSubtitle)
		            .addComponent(taxableSalaryLabel)
		            .addComponent(withHoldingTaxLabel)
		            .addComponent(salaryAfterTaxLabel)
		        )
		        .addGap(50)
		        .addGroup(gl_taxesDedPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addGap(0)  // For the row with taxesSubtitle, nothing in column 2
		            .addComponent(taxableSalaryValue)
		            .addComponent(withHoldingTaxValue)
		            .addComponent(salaryAfterTaxValue)
		        )
		);

		// Define vertical group (4 rows)
		gl_taxesDedPanel.setVerticalGroup(
				gl_taxesDedPanel.createSequentialGroup()
				.addGap(15)
		        .addGroup(gl_taxesDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(taxesSubtitle)
		            .addGap(0)  // No value in column 2 for this row
		        )
		        .addGap(25)
		        .addGroup(gl_taxesDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(taxableSalaryLabel)
		            .addComponent(taxableSalaryValue)
		        )
		        .addGroup(gl_taxesDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(withHoldingTaxLabel)
		            .addComponent(withHoldingTaxValue)
		        )
		        .addGap(50)
		        .addGroup(gl_taxesDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(salaryAfterTaxLabel)
		            .addComponent(salaryAfterTaxValue)
		        )
		);
		
		// ALLOWANCE BOX PANEL
		// set font size and color
		allowancesLabel.setFont(poppinsTitleSemiBold); // NOI18N
		allowancesLabel.setText("Allowance");
		riceSubsidyLabel.setFont(poppinsRegularFont); // NOI18N
		riceSubsidyValue.setFont(poppinsRegularFont); // NOI18N
		phoneAllowanceLabel.setFont(poppinsRegularFont); // NOI18N
		phoneAllowanceValue.setFont(poppinsRegularFont); // NOI18N
		clothingAllowanceLabel.setFont(poppinsRegularFont); // NOI18N
		clothingAllowanceValue.setFont(poppinsRegularFont); // NOI18N
		totalAllowanceLabel.setFont(poppinsBoldFont); // NOI18N
		totalAllowanceValue.setFont(poppinsRegularFont); // NOI18N
		
		GroupLayout gl_allowanceDedPanel = new GroupLayout(allowanceDedPanel);
		allowanceDedPanel.setLayout(gl_allowanceDedPanel);
		
		
		gl_allowanceDedPanel.setHorizontalGroup(
				gl_allowanceDedPanel.createSequentialGroup()
			        .addGap(30) // Left padding
			        .addGroup(gl_allowanceDedPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addComponent(allowancesLabel)
			            .addComponent(riceSubsidyLabel)
			            .addComponent(phoneAllowanceLabel)
			            .addComponent(clothingAllowanceLabel)
			            .addComponent(totalAllowanceLabel)
			        )
			        .addGap(30)
			        .addGroup(gl_allowanceDedPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
			            .addGap(0)
			            .addComponent(riceSubsidyValue)
			            .addComponent(phoneAllowanceValue)
			            .addComponent(clothingAllowanceValue)
			            .addComponent(totalAllowanceValue)
			        )
			);

			gl_allowanceDedPanel.setVerticalGroup(
					gl_allowanceDedPanel.createSequentialGroup()
			        .addGap(15) // Top padding
			        .addGroup(gl_allowanceDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(allowancesLabel)
			            .addGap(0)
			        )
			        .addGap(25)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addGroup(gl_allowanceDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(riceSubsidyLabel)
			            .addComponent(riceSubsidyValue)
			        )
			        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
			        .addGroup(gl_allowanceDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(phoneAllowanceLabel)
			            .addComponent(phoneAllowanceValue)
			        )
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addGroup(gl_allowanceDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(clothingAllowanceLabel)
			            .addComponent(clothingAllowanceValue)
			        )
			        .addGap(25)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addGroup(gl_allowanceDedPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(totalAllowanceLabel)
			            .addComponent(totalAllowanceValue)
			        )
			        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
		
			
			// NET SALARY COMPUTATION RESULT BOX PANEL
			// set font size and color
			netSalaryTitle.setFont(poppinsTitleSemiBold);
			netSalaryLabel.setFont(poppinsBoldFont); // NOI18N
			netSalaryValue.setFont(poppinsRegularFont); // NOI18N
			
			GroupLayout gl_netSalResultPanel = new GroupLayout(netSalResultPanel);
			netSalResultPanel.setLayout(gl_netSalResultPanel);


			gl_netSalResultPanel.setHorizontalGroup(
					gl_netSalResultPanel.createSequentialGroup()
					.addGap(30)
			        .addGroup(gl_netSalResultPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
		        		.addComponent(netSalaryTitle)
	        			.addComponent(netSalaryLabel)
			        )
			        .addGroup(gl_netSalResultPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
			        		.addGap(0)
		        			.addComponent(netSalaryValue)
			        )
			);

			// Define vertical group (4 rows)
			gl_netSalResultPanel.setVerticalGroup(
					gl_netSalResultPanel.createSequentialGroup()
					.addGap(15)
			        .addGroup(gl_netSalResultPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
		        		.addComponent(netSalaryTitle)
		        		.addGap(0)
			        )
			        .addGap(25)
			        .addGroup(gl_netSalResultPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
				            .addComponent(netSalaryLabel)
			        		.addComponent(netSalaryValue)
				    )
			);
			
			// GROSS SALARY CALCULATOR PANEL
			// set border size
			Border hoursRenderedFieldBorder = new LineBorder(Color.GRAY, 2, true); // outer border
			Border innerPadding = new EmptyBorder(5, 9, 5, 10); // inner border
			hoursRenderedField.setBorder(new CompoundBorder(hoursRenderedFieldBorder, innerPadding));
			calculatorPanel.setBorder(new LineBorder(Color.BLACK,3, true));
			calculateSalaryButton.setBorder(new LineBorder(Color.decode("#ca7842"),2, true));
			hoursRenderedField.setPreferredSize(new Dimension(50, 10));
			calculateSalaryButton.setPreferredSize(new Dimension(100, 40));
			
			// set font size and color
			grossSalaryComputationLabel.setFont(poppinsTitleSemiBold);
			grossSalaryComputationLabel.setText("Gross Salary Calculator");
			hourlyRateLabel.setFont(poppinsRegularFont);
			hourlyRateValue.setFont(poppinsRegularFont);
			hoursRenderedLabel.setForeground(Color.GRAY);
			hoursRenderedLabel.setFont(poppinsRegularFont);
			hoursRenderedField.setFont(poppinsRegular28f);
			calculateSalaryButton.setFont(poppinsCalculatorButton);
			calculateSalaryButton.setBackground(Color.decode("#ffbf78"));
			
			GridBagConstraints grossSalaryComputationLabelGBC = new GridBagConstraints();
			grossSalaryComputationLabelGBC.gridx = 0;
			grossSalaryComputationLabelGBC.gridy = 0;
			grossSalaryComputationLabelGBC.weightx = 1;
			grossSalaryComputationLabelGBC.weighty = 0.0;
			grossSalaryComputationLabelGBC.gridwidth = GridBagConstraints.REMAINDER;
			grossSalaryComputationLabelGBC.fill = GridBagConstraints.CENTER;
			grossSalaryComputationLabelGBC.insets = new Insets (20,0,0,0);
			calculatorPanel.add(grossSalaryComputationLabel, grossSalaryComputationLabelGBC);
			
			GridBagConstraints hourlyRateLabelGBC = new GridBagConstraints();
			hourlyRateLabelGBC.gridx = 0;
			hourlyRateLabelGBC.gridy = 1;
			hourlyRateLabelGBC.weightx = 1;
			hourlyRateLabelGBC.weighty = 0.3;
			hourlyRateLabelGBC.gridwidth = 1;
			hourlyRateLabelGBC.fill = GridBagConstraints.BOTH;
			hourlyRateLabelGBC.insets = new Insets (0,20,-20,0);
			calculatorPanel.add(hourlyRateLabel, hourlyRateLabelGBC);
			
			GridBagConstraints hourlyRateValueGBC = new GridBagConstraints();
			hourlyRateValueGBC.gridx = 1;
			hourlyRateValueGBC.gridy = 1;
			hourlyRateValueGBC.weightx = 1;
			hourlyRateValueGBC.weighty = 0.3;
			hourlyRateValueGBC.gridwidth = 1;
			hourlyRateValueGBC.fill = GridBagConstraints.BOTH;
			hourlyRateValueGBC.insets = new Insets (0,-80,-20,0);
			calculatorPanel.add(hourlyRateValue, hourlyRateValueGBC);
			
			GridBagConstraints hoursRenderedLabelGBC = new GridBagConstraints();
			hoursRenderedLabelGBC.gridx = 0;
			hoursRenderedLabelGBC.gridy = 2;
			hoursRenderedLabelGBC.weightx = 0.7;
			hoursRenderedLabelGBC.weighty = 0.3;
			hoursRenderedLabelGBC.gridwidth = 1;
			hoursRenderedLabelGBC.fill = GridBagConstraints.BOTH;
			hoursRenderedLabelGBC.insets = new Insets (0,30,20,5);
			calculatorPanel.add(hoursRenderedLabel, hoursRenderedLabelGBC);
			
			GridBagConstraints hoursRenderedFieldGBC = new GridBagConstraints();
			hoursRenderedFieldGBC.gridx = 0;
			hoursRenderedFieldGBC.gridy = 2;
			hoursRenderedFieldGBC.weightx = 0.7;
			hoursRenderedFieldGBC.weighty = 0.3;
			hoursRenderedFieldGBC.gridwidth = 1;
			hoursRenderedFieldGBC.fill = GridBagConstraints.BOTH;
			hoursRenderedFieldGBC.insets = new Insets (0,20,20,5);
			calculatorPanel.add(hoursRenderedField, hoursRenderedFieldGBC);
			
			GridBagConstraints calculateSalaryButtonGBC = new GridBagConstraints();
			calculateSalaryButtonGBC.gridx = 1;
			calculateSalaryButtonGBC.gridy = 2;
			calculateSalaryButtonGBC.weightx = 0.3;
			calculateSalaryButtonGBC.weighty = 0.7;
			calculateSalaryButtonGBC.gridwidth = 1;
			calculateSalaryButtonGBC.fill = GridBagConstraints.BOTH;
			calculateSalaryButtonGBC.insets = new Insets (0,0,20,20);
			calculatorPanel.add(calculateSalaryButton, calculateSalaryButtonGBC);
			
			hoursRenderedField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					hoursRenderedFieldActionPerformed(evt, employeeComp);
				}
			});

			calculateSalaryButton.setText("=");
			calculateSalaryButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					calculateSalaryButtonActionPerformed(evt, employeeComp);
				}
			});
			
			// GROSS SALARY CALCULATOR RESULT PANEL
			
			resultPanel.setBorder(new LineBorder(Color.BLACK,3, true));
			grossSalaryLabel.setFont(poppinsSemiBold24f); // NOI18N
			grossSalaryValue.setFont(poppinsRegular24f); // NOI18N
		
			GridBagConstraints grossSalaryLabelGBC = new GridBagConstraints();
			grossSalaryLabelGBC.gridx = 0;
			grossSalaryLabelGBC.gridy = 0;
			grossSalaryLabelGBC.weightx = 0.5;
			grossSalaryLabelGBC.weighty = 1;
			grossSalaryLabelGBC.fill = GridBagConstraints.BOTH;
			grossSalaryLabelGBC.insets = new Insets (0,50,0,-50);
			resultPanel.add(grossSalaryLabel, grossSalaryLabelGBC);
			
			GridBagConstraints ggrossSalaryValueGBC = new GridBagConstraints();
			ggrossSalaryValueGBC.gridx = 1;
			ggrossSalaryValueGBC.gridy = 0;
			ggrossSalaryValueGBC.weightx = 0.5;
			ggrossSalaryValueGBC.weighty = 1;
			ggrossSalaryValueGBC.fill = GridBagConstraints.BOTH;
			ggrossSalaryValueGBC.insets = new Insets (0,0,0,0);
			resultPanel.add(grossSalaryValue, ggrossSalaryValueGBC);

		add(mainPanel);
		pack();
		setSize(1366,788);
		setVisible(true);
		setLocationRelativeTo(null);
		
	}

	private void hoursRenderedFieldActionPerformed(java.awt.event.ActionEvent evt, Compensation employeeComp) {
		// Exit if the value is not of double type
		if (!isNumeric(hoursRenderedField.getText())) {
			JOptionPane.showMessageDialog(new JFrame(""), "Please provide a valid input.", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		setDataOnEvent(employeeComp);
	}

	private void calculateSalaryButtonActionPerformed(java.awt.event.ActionEvent evt, Compensation employeeComp) {

		// Exit if the value is not of double type
		if (!isNumeric(hoursRenderedField.getText())) {
			JOptionPane.showMessageDialog(new JFrame(""), "Please provide a valid input.", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		setDataOnEvent(employeeComp);
	}

	public void setDataOnEvent(Compensation employeeComp) {
		// Format the data to only be two decimal places
		DecimalFormat numberFormat = new DecimalFormat("#.00");

		// Get hours rendered
		hoursRendered = Double.parseDouble(hoursRenderedField.getText());

		// Set Gross Salary
		grossSalary = Double
				.parseDouble(numberFormat.format(employeeComp.calculateGrossSalary(hourlyRate, hoursRendered)));
		grossSalaryValue.setText(String.valueOf(grossSalary));
		grossSalaryValue1.setText(String.valueOf(grossSalary)); // Duplicate label

		// Set SSS Deductions
		sssDeductions = Double.parseDouble(numberFormat.format(SalaryCalculator.getSSS(grossSalary)));
		sssDeductionsValue.setText(String.valueOf(sssDeductions));

		// Set PhilHealth Deductions
		philhealthDeductions = Double.parseDouble(numberFormat.format(SalaryCalculator.getPhilHealth(grossSalary)));
		philhealthDeductionsValue.setText(String.valueOf(philhealthDeductions));

		// Set Pag-ibig Deductions
		pagibigDeductions = Double.parseDouble(numberFormat.format(SalaryCalculator.getPagibig(grossSalary)));
		pagibigDeductionsValue.setText(String.valueOf(pagibigDeductions));

		// Set Total Deductions
		totalDeductions = pagibigDeductions + philhealthDeductions + sssDeductions;
		totalDeductionsValue.setText(String.valueOf(totalDeductions));

		// Set Taxable Salary
		taxableSalary = Double.parseDouble(numberFormat.format(grossSalary - totalDeductions));
		taxableSalaryValue.setText(String.valueOf(taxableSalary));

		// Set Withholding Tax
		withHoldingTax = Double.parseDouble(numberFormat.format(SalaryCalculator.getWithholding(taxableSalary)));
		withHoldingTaxValue.setText(String.valueOf(withHoldingTax));

		// Set Salary After Tax
		salaryAfterTax = Double.parseDouble(numberFormat.format(taxableSalary - withHoldingTax));
		salaryAfterTaxValue.setText(String.valueOf(salaryAfterTax));

		// Set Net Salary
		netSalary = Double.parseDouble(numberFormat.format(salaryAfterTax + totalAllowance));
		netSalaryValue.setText(String.valueOf(netSalary));

	}

	public void setDataOnRender(Compensation employeeComp) {
		// Set hourly rate
		hourlyRate = employeeComp.getHourlyRate();
		hourlyRateValue.setText(Double.toString(hourlyRate));

		// Set rice subsidy
		riceSubsidy = employeeComp.getRiceSubsidy();
		riceSubsidyValue.setText(Double.toString(riceSubsidy));

		// Set phone allowance
		phoneAllowance = employeeComp.getPhoneAllowance();
		phoneAllowanceValue.setText(Double.toString(phoneAllowance));

		// Set clothing allowance
		clothingAllowance = employeeComp.getClothingAllowance();
		clothingAllowanceValue.setText(Double.toString(clothingAllowance));

		totalAllowance = phoneAllowance + riceSubsidy + clothingAllowance;
		totalAllowanceValue.setText(Double.toString(totalAllowance));
		totalAllowanceValue1.setText(Double.toString(totalAllowance)); // Duplicate label
	}

	private static boolean isNumeric(String str) {
		try {
			// Attempt to parse the input as a number
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
