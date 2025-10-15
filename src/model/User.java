//author @ian

package model;

/**
 * Represents a user of the library system.
 * A user can either be a STUDENT or an ADMIN.
 */
public class User {

    private String role;       // "student" or "admin"
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;

    /**
     * Full constructor — used when role is specified.
     */
    public User(String role, String firstName, String lastName, String email, String mobile, String password) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    /**
     * Overloaded constructor — defaults role to "student".
     * Useful when creating regular student accounts.
     */
    public User(String firstName, String lastName, String email, String mobile, String password) {
        this("student", firstName, lastName, email, mobile, password);
    }

    // ─────────────── Getters ───────────────
    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    // ─────────────── Setters (optional, can be removed if immutable) ───────────────
    public void setRole(String role) {
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ─────────────── Utility Methods ───────────────

    /**
     * Converts the user object into a CSV format.
     * Example: "student,Juan,Dela Cruz,juan@gmail.com,09123456789,password123"
     */
    public String toCsv() {
        return String.join(",", role, firstName, lastName, email, mobile, password);
    }

    /**
     * Nice readable format for debugging or display (not for saving).
     */
    @Override
    public String toString() {
        return "User{" +
                "role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    /**
     * Checks equality by email (useful if you want to prevent duplicates).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return email != null && email.equalsIgnoreCase(other.email);
    }

    @Override
    public int hashCode() {
        return email != null ? email.toLowerCase().hashCode() : 0;
    }
}
