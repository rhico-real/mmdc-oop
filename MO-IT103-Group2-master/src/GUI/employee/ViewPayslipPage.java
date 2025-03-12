package GUI.employee;

import java.util.ArrayList;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;

public class ViewPayslipPage extends javax.swing.JFrame {
    

    public ViewPayslipPage() {
        initComponents();
        populateComboBox();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

    	titlePane = new javax.swing.JPanel();
        contentPane = new javax.swing.JPanel();
        
        lblEmployeeNo = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblImmediateSupervisor = new javax.swing.JLabel();
        lblSSSNo = new javax.swing.JLabel();
        lblPhilHealthNo = new javax.swing.JLabel();
        lblPAYSLIP = new javax.swing.JLabel();
        lblEARNINGS = new javax.swing.JLabel();
        lblMonthlyRate = new javax.swing.JLabel();
        lblDailyRate = new javax.swing.JLabel();
        lblDaysWorked = new javax.swing.JLabel();
        lblOvertime = new javax.swing.JLabel();
        lblGrossIncome_1 = new javax.swing.JLabel();
        lblBENEFITS = new javax.swing.JLabel();
        lblRiceSubsidy = new javax.swing.JLabel();
        lblPhoneAllowance = new javax.swing.JLabel();
        lblClothingAllowance = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblDEDUCTIONS = new javax.swing.JLabel();
        lblSocialSecuritySystem = new javax.swing.JLabel();
        lblPhilHealth = new javax.swing.JLabel();
        lblPagIbig = new javax.swing.JLabel();
        lblWithholdTax = new javax.swing.JLabel();
        lblTotalDeduction = new javax.swing.JLabel();
        lblSUMMARY = new javax.swing.JLabel();
        lblGrossIncome_2 = new javax.swing.JLabel();
        lblBenefits = new javax.swing.JLabel();
        lblTotalDeductions = new javax.swing.JLabel();
        lblTakeHomePay = new javax.swing.JLabel();
        lblPhoneNumber = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblPosition = new javax.swing.JLabel();
        lblTinNo = new javax.swing.JLabel();
        lblPagIbigNo = new javax.swing.JLabel();
        lblSelectMonth = new javax.swing.JLabel();
        lblTotalHoursWorked = new javax.swing.JLabel();
        
        txtEmployeeNo = new javax.swing.JTextField();
        txtEmployeeName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtSSSNum = new javax.swing.JTextField();
        txtPhilHealthNum = new javax.swing.JTextField();
        txtMonthlyRate = new javax.swing.JTextField();
        txtDailyRate = new javax.swing.JTextField();
        txtDaysWorked = new javax.swing.JTextField();
        txtOvertime = new javax.swing.JTextField();
        txtGrossIncome = new javax.swing.JTextField();
        txtRiceAllowance = new javax.swing.JTextField();
        txtPhoneAllowance = new javax.swing.JTextField();
        txtClothingAllowance = new javax.swing.JTextField();
        txtAllowanceTotal = new javax.swing.JTextField();
        txtSSS = new javax.swing.JTextField();
        txtPhilHealth = new javax.swing.JTextField();
        txtPagIbig = new javax.swing.JTextField();
        txtWithHoldingTax = new javax.swing.JTextField();
        txtTotalDeductions = new javax.swing.JTextField();
        txtGrossIncomeSummary = new javax.swing.JTextField();
        txtBenefits = new javax.swing.JTextField();
        txtTotalDeductionsSummary = new javax.swing.JTextField();
        txtNetPay = new javax.swing.JTextField();
        txtPhoneNum = new javax.swing.JTextField();
        txtStatus = new javax.swing.JTextField();
        txtPosition = new javax.swing.JTextField();
        txtTinNo = new javax.swing.JTextField();
        txtPagibigNum = new javax.swing.JTextField();
        txtSupervisor = new javax.swing.JTextField();
        txtTotalHoursWorked = new javax.swing.JTextField();
        
        cboMonth = new javax.swing.JComboBox<>();
        
        btnGenerate = new javax.swing.JButton();
        btnGenerate.setFont(new Font("Tahoma", Font.BOLD, 12));
        
        btnUpdate = new javax.swing.JButton();
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(77, 77, 105));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));

        lblEmployeeNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblEmployeeNo.setText("Employee No. :");

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblName.setText("Name :");

        lblAddress.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblAddress.setText("Address :");
        
        lblImmediateSupervisor.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblImmediateSupervisor.setText("Immmediate Supervisor :");

        lblSSSNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblSSSNo.setText("SSS No. :");

        lblPhilHealthNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblPhilHealthNo.setText("PhilHealth No. :");       

       

        lblPAYSLIP.setFont(new java.awt.Font("Segoe UI", 3, 14)); 
        lblPAYSLIP.setText("PAYSLIP");

        lblEARNINGS.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblEARNINGS.setText("EARNINGS");

        lblMonthlyRate.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblMonthlyRate.setText("Monthly Rate :");

        lblDailyRate.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblDailyRate.setText("Daily Rate :");

        lblDaysWorked.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblDaysWorked.setText("Days Worked :");

        lblOvertime.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblOvertime.setText("Overtime :");

        lblGrossIncome_1.setFont(new java.awt.Font("Segoe UI", 3, 12)); 
        lblGrossIncome_1.setText("Gross Income :");

   
        lblBENEFITS.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblBENEFITS.setText("BENEFITS");

        lblRiceSubsidy.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblRiceSubsidy.setText("Rice Subsidy :");

        lblPhoneAllowance.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblPhoneAllowance.setText("Phone Allowance :");

        lblClothingAllowance.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblClothingAllowance.setText("Clothing Allowance :");

        lblTotal.setFont(new java.awt.Font("Segoe UI", 3, 12)); 
        lblTotal.setText("TOTAL :");       

        lblDEDUCTIONS.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblDEDUCTIONS.setText("DEDUCTIONS");

        lblSocialSecuritySystem.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblSocialSecuritySystem.setText("Social Security System :");

        lblPhilHealth.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblPhilHealth.setText("PhilHealth :");

        lblPagIbig.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblPagIbig.setText("Pag-Ibig :");

        lblWithholdTax.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblWithholdTax.setText("Withholding Tax :");

        lblTotalDeduction.setFont(new java.awt.Font("Segoe UI", 3, 12)); 
        lblTotalDeduction.setText("TOTAL DEDUCTION :");
   

        lblSUMMARY.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblSUMMARY.setText("SUMMARY");

        lblGrossIncome_2.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblGrossIncome_2.setText("Gross Income :");

        lblBenefits.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblBenefits.setText("Benefits :");

        lblTotalDeductions.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblTotalDeductions.setText("Total Deductions :");

        lblTakeHomePay.setFont(new java.awt.Font("Segoe UI", 3, 12)); 
        lblTakeHomePay.setText("TAKE HOME PAY :");

        lblPhoneNumber.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblPhoneNumber.setText("Phone Number :");     

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblStatus.setText("Status :");      

        lblPosition.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblPosition.setText("Position :");        

        lblTinNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblTinNo.setText("TIN No. :");

        lblPagIbigNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblPagIbigNo.setText("Pag-Ibig No. :");

        
        lblSelectMonth.setFont(new java.awt.Font("Segoe UI", 3, 12)); 
        lblSelectMonth.setText("Select Month :");

        lblTotalHoursWorked.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblTotalHoursWorked.setText("Total Hours Worked:");
        
        cboMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	// TODO: Upon selecting ComboBox from Select month
            }
        });
        
        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // TODO: upon clicking Generate button            	
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	// TODO: Upon clicking Update button
            }
        });
  

        javax.swing.GroupLayout gl_contentPane = new javax.swing.GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
        	gl_contentPane.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addGap(0, 408, Short.MAX_VALUE)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        				.addComponent(lblSUMMARY, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblDEDUCTIONS, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblBENEFITS, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblEARNINGS, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
        			.addGap(366))
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGap(47)
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
        									.addComponent(lblRiceSubsidy, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblPAYSLIP, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblOvertime, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblDaysWorked, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblDailyRate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblMonthlyRate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblGrossIncome_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
        								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
        									.addComponent(lblPhoneAllowance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblClothingAllowance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        								.addComponent(lblTotal, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
        								.addComponent(lblPagIbig, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
        								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
        									.addComponent(lblWithholdTax, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblPhilHealth, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
        									.addComponent(lblTotalDeduction, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblSocialSecuritySystem, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
        									.addComponent(lblBenefits, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(lblGrossIncome_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
        								.addComponent(lblTotalDeductions, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
        								.addComponent(lblTotalHoursWorked))
        							.addGap(238))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblTakeHomePay, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
        							.addGap(26)))
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        						.addComponent(txtMonthlyRate)
        						.addComponent(txtDailyRate)
        						.addComponent(txtDaysWorked)
        						.addComponent(txtOvertime)
        						.addComponent(txtGrossIncome)
        						.addComponent(txtRiceAllowance)
        						.addComponent(txtPhoneAllowance)
        						.addComponent(txtClothingAllowance)
        						.addComponent(txtAllowanceTotal)
        						.addComponent(txtSSS)
        						.addComponent(txtPhilHealth)
        						.addComponent(txtPagIbig)
        						.addComponent(txtWithHoldingTax)
        						.addComponent(txtTotalDeductions)
        						.addComponent(txtGrossIncomeSummary)
        						.addComponent(txtBenefits)
        						.addComponent(txtTotalDeductionsSummary)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(txtNetPay, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
        							.addGap(0, 284, Short.MAX_VALUE))
        						.addComponent(txtTotalHoursWorked)))
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGap(50)
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblAddress)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblPhoneNumber)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtPhoneNum))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblEmployeeNo)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtEmployeeNo))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblName)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtEmployeeName, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
        						.addComponent(btnGenerate))
        					.addGap(27)
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblPagIbigNo)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtPagibigNum, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblPhilHealthNo)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtPhilHealthNum, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblSSSNo, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(txtSSSNum, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addComponent(lblTinNo, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(txtTinNo, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
        					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(lblImmediateSupervisor, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
        						.addComponent(lblStatus, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(lblPosition, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(lblSelectMonth, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(cboMonth, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(txtSupervisor, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
        						.addComponent(txtPosition)
        						.addComponent(txtStatus))))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap(775, Short.MAX_VALUE)
        			.addComponent(btnUpdate)
        			.addContainerGap())
        );
        gl_contentPane.setVerticalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addGap(25)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        					.addComponent(lblEmployeeNo)
        					.addComponent(txtEmployeeNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addComponent(txtStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addComponent(lblSSSNo)
        					.addComponent(txtSSSNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addComponent(lblStatus))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblName)
        				.addComponent(txtEmployeeName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblPosition)
        				.addComponent(lblPhilHealthNo)
        				.addComponent(txtPhilHealthNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(txtPosition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblAddress)
        				.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblTinNo)
        				.addComponent(txtTinNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblImmediateSupervisor)
        				.addComponent(txtSupervisor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblPhoneNumber)
        				.addComponent(txtPhoneNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblPagIbigNo)
        				.addComponent(txtPagibigNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(cboMonth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblSelectMonth))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btnGenerate)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblPAYSLIP)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        								.addGroup(gl_contentPane.createSequentialGroup()
        									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        										.addGroup(gl_contentPane.createSequentialGroup()
        											.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        												.addGroup(gl_contentPane.createSequentialGroup()
        													.addComponent(lblEARNINGS)
        													.addPreferredGap(ComponentPlacement.UNRELATED)
        													.addComponent(lblMonthlyRate))
        												.addComponent(txtMonthlyRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        											.addPreferredGap(ComponentPlacement.RELATED)
        											.addComponent(lblDailyRate))
        										.addComponent(txtDailyRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(lblDaysWorked))
        								.addComponent(txtDaysWorked, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(lblOvertime))
        						.addComponent(txtOvertime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblTotalHoursWorked))
        				.addComponent(txtTotalHoursWorked, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(16)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblGrossIncome_1)
        				.addComponent(txtGrossIncome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(lblBENEFITS)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        								.addComponent(lblRiceSubsidy)
        								.addComponent(txtRiceAllowance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(lblPhoneAllowance))
        						.addComponent(txtPhoneAllowance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblClothingAllowance))
        				.addComponent(txtClothingAllowance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblTotal)
        				.addComponent(txtAllowanceTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(lblDEDUCTIONS)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        								.addGroup(gl_contentPane.createSequentialGroup()
        									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        										.addGroup(gl_contentPane.createSequentialGroup()
        											.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        												.addComponent(lblSocialSecuritySystem)
        												.addComponent(txtSSS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        											.addPreferredGap(ComponentPlacement.RELATED)
        											.addComponent(lblPhilHealth))
        										.addComponent(txtPhilHealth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        									.addPreferredGap(ComponentPlacement.RELATED)
        									.addComponent(lblPagIbig))
        								.addComponent(txtPagIbig, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(lblWithholdTax))
        						.addComponent(txtWithHoldingTax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(lblTotalDeduction))
        				.addComponent(txtTotalDeductions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(lblSUMMARY)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        								.addComponent(lblGrossIncome_2)
        								.addComponent(txtGrossIncomeSummary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(lblBenefits))
        						.addComponent(txtBenefits, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblTotalDeductions))
        				.addComponent(txtTotalDeductionsSummary, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
        				.addComponent(txtNetPay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblTakeHomePay))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btnUpdate)
        			.addContainerGap(32, Short.MAX_VALUE))
        );
        contentPane.setLayout(gl_contentPane);

        titlePane.setBackground(new java.awt.Color(77, 77, 105));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/motorcycleWhite.png"))); 
        lblTitle.setText("MotorPH");

        javax.swing.GroupLayout gl_titlePane = new javax.swing.GroupLayout(titlePane);
        titlePane.setLayout(gl_titlePane);
        gl_titlePane.setHorizontalGroup(
            gl_titlePane.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_titlePane.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_titlePane.setVerticalGroup(
            gl_titlePane.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gl_titlePane.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titlePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(contentPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(titlePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        pack();
    }

    
    private void populateComboBox() {
        ArrayList<String> month = new ArrayList<>();
        month.add("January");
        month.add("Febuary");
        month.add("March");
        month.add("April");
        month.add("May");
        month.add("June");
        month.add("July");
        month.add("August");
        month.add("September");
        month.add("October");
        month.add("November");
        month.add("December");

        for (String months : month) {
            cboMonth.addItem(months);
        }

    }

    public static void main(String args[]) {

    	try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewPayslipPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewPayslipPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewPayslipPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewPayslipPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewPayslipPage().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JComboBox<String> cboMonth;
    private javax.swing.JLabel lblEmployeeNo;
    private javax.swing.JLabel lblDailyRate;
    private javax.swing.JLabel lblDaysWorked;
    private javax.swing.JLabel lblOvertime;
    private javax.swing.JLabel lblGrossIncome_1;
    private javax.swing.JLabel lblBENEFITS;
    private javax.swing.JLabel lblRiceSubsidy;
    private javax.swing.JLabel lblPhoneAllowance;
    private javax.swing.JLabel lblClothingAllowance;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblDEDUCTIONS;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblSocialSecuritySystem;
    private javax.swing.JLabel lblPhilHealth;
    private javax.swing.JLabel lblPagIbig;
    private javax.swing.JLabel lblWithholdTax;
    private javax.swing.JLabel lblTotalDeduction;
    private javax.swing.JLabel lblSUMMARY;
    private javax.swing.JLabel lblGrossIncome_2;
    private javax.swing.JLabel lblBenefits;
    private javax.swing.JLabel lblTotalDeductions;
    private javax.swing.JLabel lblTakeHomePay;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblPhoneNumber;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JLabel lblTinNo;
    private javax.swing.JLabel lblPagIbigNo;
    private javax.swing.JLabel lblSelectMonth;
    private javax.swing.JLabel lblTotalHoursWorked;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblImmediateSupervisor;
    private javax.swing.JLabel lblSSSNo;
    private javax.swing.JLabel lblPhilHealthNo;
    private javax.swing.JLabel lblPAYSLIP;
    private javax.swing.JLabel lblEARNINGS;
    private javax.swing.JLabel lblMonthlyRate;
    private javax.swing.JPanel titlePane;
    private javax.swing.JPanel contentPane;
    private javax.swing.JTextField txtOvertime;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAllowanceTotal;
    private javax.swing.JTextField txtBenefits;
    private javax.swing.JTextField txtClothingAllowance;
    private javax.swing.JTextField txtDailyRate;
    private javax.swing.JTextField txtDaysWorked;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtEmployeeNo;
    private javax.swing.JTextField txtGrossIncome;
    private javax.swing.JTextField txtGrossIncomeSummary;
    private javax.swing.JTextField txtMonthlyRate;
    private javax.swing.JTextField txtNetPay;
    private javax.swing.JTextField txtPagIbig;
    private javax.swing.JTextField txtPagibigNum;
    private javax.swing.JTextField txtPhilHealth;
    private javax.swing.JTextField txtPhilHealthNum;
    private javax.swing.JTextField txtPhoneAllowance;
    private javax.swing.JTextField txtPhoneNum;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtRiceAllowance;
    private javax.swing.JTextField txtSSS;
    private javax.swing.JTextField txtSSSNum;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtSupervisor;
    private javax.swing.JTextField txtTinNo;
    private javax.swing.JTextField txtTotalDeductions;
    private javax.swing.JTextField txtTotalDeductionsSummary;
    private javax.swing.JTextField txtTotalHoursWorked;
    private javax.swing.JTextField txtWithHoldingTax;
}
