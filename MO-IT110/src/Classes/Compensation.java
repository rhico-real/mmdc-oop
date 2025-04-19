package Classes;

import com.google.gson.annotations.SerializedName;
import java.io.IOException;

public class Compensation extends EmployeeInformation {
	
	@SuppressWarnings("unused")
	private String compensationMonth;
	
	@SerializedName("basic_salary") 			private double basicSalary;
	@SerializedName("rice_subsidy") 			private double riceSubsidy;
	@SerializedName("phone_allowance") 			private double phoneAllowance;
	@SerializedName("clothing_allowance")		private double clothingAllowance;
	@SerializedName("gross_semi-monthly_rate") 	private double grossSemiMonthlyRate;
	
	private double netSalary;
	

	public Compensation(String userId, String password) throws IOException {
		super(userId, password);
	}
	
	public Compensation(String employeeNumber) { super(employeeNumber); }

	public void setBasicSalary			(double value) { this.basicSalary		 	= value; }
	public void setRiceSubsidy			(double value) { this.riceSubsidy 			= value; }
	public void setPhoneAllowance		(double value) { this.phoneAllowance 		= value; }
	public void setClothingAllowance	(double value) { this.clothingAllowance 	= value; }
	public void setGrossSemiMonthlyRate	(double value) { this.grossSemiMonthlyRate 	= value; }
	public void setNetSalary 			(double value) { this.netSalary 			= value; }
	
	public double getBasicSalary			() { return basicSalary; 			}
	public double getRiceSubsidy			() { return riceSubsidy; 			}
	public double getPhoneAllowance			() { return phoneAllowance; 		}
	public double getClothingAllowance		() { return clothingAllowance; 		}
	public double getGrossSemiMonthlyRate	() { return grossSemiMonthlyRate; 	}
	public double getNetSalary				() { return netSalary; 				}
	
	public double calculateGrossSalary(double hourlyRate, double hoursRendered) {
		return hourlyRate * hoursRendered;
	}
}
