/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String mobile; // optional

    public User(String fullName, String email, String password, String role, String mobile) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.mobile = mobile;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getMobile() { return mobile; }
}
