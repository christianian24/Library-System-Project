package model;

public class User {
    private String role; // student or admin
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;

    public User(String role, String firstName, String lastName, String email, String mobile, String password) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    // Overload constructor (for your current call signature)
    public User(String firstName, String lastName, String email, String mobile, String password) {
        this("student", firstName, lastName, email, mobile, password);
    }

    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getPassword() { return password; }

    // Convert to CSV format
    public String toCsv() {
        return String.join(",", role, firstName, lastName, email, mobile, password);
    }
}
