//author @ian
package data;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import model.User;

public class UserDataManager {
    private static final String FILE_PATH = "data/users.txt";
    
    // ─────────────── Add User ───────────────
    public static boolean addUser(User user) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            
            // Check duplicates
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                
                String[] parts = line.split(",");
                if (parts.length >= 6) { // ✅ Changed from 5 to 6 (all fields)
                    String existingEmail = parts[3].trim();    // ✅ Added trim()
                    String existingMobile = parts[4].trim();   // ✅ Added trim()
                    
                    // ✅ Added trim() to user input as well
                    if (existingEmail.equalsIgnoreCase(user.getEmail().trim()) ||
                        existingMobile.equals(user.getMobile().trim())) {
                        System.out.println("❌ Duplicate found:");
                        System.out.println("   Existing: " + existingEmail + " / " + existingMobile);
                        System.out.println("   New: " + user.getEmail() + " / " + user.getMobile());
                        return false; // already exists
                    }
                }
            }
            
            // Append user
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(user.toCsv());
                bw.newLine();
            }
            
            System.out.println("✅ User added successfully: " + user.getEmail());
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ─────────────── Authenticate (Admin or Student) ───────────────
    public static User authenticate(String username, String password) {
        username = username.trim();
        password = password.trim();
        
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return null;
            
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String role = parts[0].trim();
                    String firstName = parts[1].trim();
                    String pass = parts[5].trim();
                    
                    if (firstName.equalsIgnoreCase(username) && pass.equals(password)) {
                        return new User(
                            role, 
                            parts[1].trim(), 
                            parts[2].trim(), 
                            parts[3].trim(), 
                            parts[4].trim(), 
                            parts[5].trim()
                        );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ─────────────── Optional: For initial admin creation ───────────────
    public static void ensureDefaultAdmin() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            User admin = new User("admin", "System", "Admin", "admin@archiva.com", "0000000000", "admin123");
            addUser(admin);
            System.out.println("✅ Default admin account created: admin@archiva.com / admin123");
        }
    }
    
    // ─────────────── Debug Helper: Print all users ───────────────
    public static void printAllUsers() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                System.out.println("❌ No users file found");
                return;
            }
            
            List<String> lines = Files.readAllLines(file.toPath());
            System.out.println("📋 All users in database:");
            for (int i = 0; i < lines.size(); i++) {
                System.out.println("  [" + i + "] " + lines.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}