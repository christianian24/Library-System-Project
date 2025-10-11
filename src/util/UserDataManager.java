package util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import model.User;

public class UserDataManager {
    private static final String FILE_PATH = "data/users.txt";

    // Save a new user (returns false if duplicate email or number)
    public static boolean addUser(User user) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();

            // Check for duplicates
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String existingEmail = parts[3];
                    String existingMobile = parts[4];
                    if (existingEmail.equalsIgnoreCase(user.getEmail()) || existingMobile.equals(user.getMobile())) {
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

    // Authenticate student (used for login)
    public static boolean authenticateStudent(String email, String password) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return false;

            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String role = parts[0];
                    String e = parts[3];
                    String p = parts[5];
                    if (role.equalsIgnoreCase("student") && e.equalsIgnoreCase(email) && p.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
