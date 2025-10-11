package util;

import java.io.*;
import java.util.*;
import model.User;

public class UserDataManager {

    private static final String FILE_PATH = "Users.txt";

    // Add a new user to the file
    public static boolean addUser(User user) {
        if (findUser(user.getEmail(), user.getPassword()) != null) {
            return false; // already exists
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.getFullName() + "," + user.getEmail() + "," + user.getPassword());
            writer.newLine();
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find user by email and password
    public static User findUser(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    String fName = data[0];
                    String lName = data[1];
                    String mail = data[2];
                    String mobile = data[3];
                    String pass = data[4];

                    if (mail.equals(email) && pass.equals(password)) {
                        return new User(fName, lName, mail, mobile, pass);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // not found
    }
}