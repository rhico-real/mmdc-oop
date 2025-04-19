package Classes;

import UtilityClasses.JsonFileHandler;
import java.io.IOException;
import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class User {

	@SerializedName("employeeNum")
	private String 	employeeNumber;
	private String 	userId;
	private String 	password;
	private Boolean isVerified 	= false;
	private Boolean loginStatus = false;
	private Boolean isAdmin 	= false;
	private Date 	dateRegistered;

	public User(String userId, String password) throws IOException {
		this.userId 	= userId;
		this.password 	= password;
		if (!userId.equals("") && !password.equals("")) {
			authenticateLogin();
		}
	}

	public User(String employeeNumber) {		setEmployeeNumber(employeeNumber);	}

	public String 	getEmployeeNumber() { return employeeNumber;	}
	public String 	getUserId		 () { return userId;			}
	public String 	getPassword		 () { return password;			}
	public Boolean 	getIsVerified	 () { return isVerified;		}
	public Boolean 	getLoginStatus	 () { return loginStatus;		}
	public Boolean 	getIsAdmin		 () { return isAdmin;			}
	public Date 	getDateRegistered() { return dateRegistered;	}

	public void setEmployeeNumber(String  employeeNum	) {	this.employeeNumber = employeeNum; 	  }
	public void setUserId		 (String  userId		) {	this.userId 		= userId;		  }
	public void setPassword		 (String  password		) {	this.password 		= password;		  }
	public void setIsVerified	 (Boolean isVerified	) {	this.isVerified 	= isVerified;	  }
	public void setLoginStatus	 (Boolean loginStatus	) {	this.loginStatus 	= loginStatus;	  }
	public void setIsAdmin		 (Boolean value			) {	this.isAdmin 		= value;		  }
	public void setDateRegistered(Date    dateRegistered) {	this.dateRegistered = dateRegistered; }

	public void authenticateLogin() throws IOException {
		if (!userId.equals("admin")) {
			// Set the employee number if the user is not an admin
			setEmployeeNumber(JsonFileHandler.nameIterator(JsonFileHandler.getLoginCredentialsJSON(), "username",
					userId, "employeeNum"));

			// Check the login status
			setLoginStatus(JsonFileHandler.compareLoginCredentials(JsonFileHandler.getLoginCredentialsJSON(),
					"username", userId, "password", password));
			return;
		}

		// Check if user is an admin
		setLoginStatus(authenticateAdminLogin(userId, password));
		return;
	}

	private Boolean authenticateAdminLogin(String userId, String password) {
		if (userId.equals("admin") && password.equals("123")) {
			setIsAdmin(true);
			return true;
		}
		return false;
	}
}
