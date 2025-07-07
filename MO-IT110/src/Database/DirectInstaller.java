package Database;

/**
 * This class provides a direct way to install database objects
 * without relying on the SQL files.
 */
public class DirectInstaller {
    
    public static void main(String[] args) {
        System.out.println("Starting direct database installation...");
        
        // Execute the direct installer
        boolean success = DirectDatabaseInstaller.installDatabaseObjects();
        
        if (success) {
            System.out.println("Database objects installed successfully!");
        } else {
            System.err.println("Failed to install database objects.");
        }
    }
}
