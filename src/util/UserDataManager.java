//@ian

package util;

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
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String existingEmail = parts[3];
                    String existingMobile = parts[4];
                    if (existingEmail.equalsIgnoreCase(user.getEmail()) ||
                        existingMobile.equals(user.getMobile())) {
                        return false; // already exists
                    }
                }
            }

            // Append user
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(user.toCsv());
                bw.newLine();
            }

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
                String role = parts[0];
                String firstName = parts[1].trim(); // username
                String pass = parts[5].trim();

                if (firstName.equalsIgnoreCase(username) && pass.equals(password)) {
                    return new User(role, parts[1], parts[2], parts[3], parts[4], parts[5]);
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
}
