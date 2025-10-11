/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;
import util.FrameUtil;
import java.awt.Color;
import util.AlertUtil;
import util.UserDataManager;
/**
 *
 * @author User
 */
public class CreateAccount extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CreateAccount.class.getName());

    /**
     * Creates new form CreateAccount
     */
    public CreateAccount() {
        initComponents();
        FrameUtil.setupFrame(this);

        // Remove focus border on the button
        CreateButton.setFocusPainted(false);
        
        // Add padding inside text fields
        javax.swing.border.Border padding = javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10);
        javax.swing.border.Border line = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204));

        txtFirstname.setBorder(javax.swing.BorderFactory.createCompoundBorder(line, padding));
        Lastname.setBorder(javax.swing.BorderFactory.createCompoundBorder(line, padding));
        Number.setBorder(javax.swing.BorderFactory.createCompoundBorder(line, padding));
        Password.setBorder(javax.swing.BorderFactory.createCompoundBorder(line, padding));
        Repassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(line, padding));
        Email.setBorder(javax.swing.BorderFactory.createCompoundBorder(line, padding));
        
        
        Number.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                String text = Number.getText();

                // Block non-digit characters
                if (!Character.isDigit(c) && c != '\b' && c != '\u007F') {
                    e.consume();
                    util.AlertUtil.showRoundedToastTopRight(
                        CreateAccount.this,
                        "Please enter numbers only.",
                        new Color(241, 196, 15) // 🔴 Red for warning
                    );
                    return;
                }

                // Limit to 11 digits
                if (text.length() >= 11 && c != '\b' && c != '\u007F') {
                    e.consume();
                    util.AlertUtil.showRoundedToastTopRight(
                        CreateAccount.this,
                        "Mobile number must be 11 digits only.",
                        new Color(241, 196, 15) // 🟡 Yellow for limit warning
                    );
                }
            }
        });




        
        // --- FIRST NAME PLACEHOLDER ---
        txtFirstname.setForeground(Color.GRAY);
        txtFirstname.setText("First name");
        txtFirstname.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtFirstname.getText().equals("First name")) {
                    txtFirstname.setText("");
                    txtFirstname.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtFirstname.getText().isEmpty()) {
                    txtFirstname.setText("First name");
                    txtFirstname.setForeground(Color.GRAY);
                }
            }
        });

        // --- LAST NAME PLACEHOLDER ---
        Lastname.setForeground(Color.GRAY);
        Lastname.setText("Last name");
        Lastname.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (Lastname.getText().equals("Last name")) {
                    Lastname.setText("");
                    Lastname.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (Lastname.getText().isEmpty()) {
                    Lastname.setText("Last name");
                    Lastname.setForeground(Color.GRAY);
                }
            }
        });
        
        // -- EMAIL PLACEHOLDER --
        Email.setForeground(Color.GRAY);
        Email.setText("Email");
        Email.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (Email.getText().equals("Email")) {
                    Email.setText("");
                    Email.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (Email.getText().isEmpty()) {
                    Email.setText("Email");
                    Email.setForeground(Color.GRAY);
                }
            }
        });
        
        
        // --- MOBILE PLACEHOLDER ---
        Number.setForeground(Color.GRAY);
        Number.setText("Mobile number");
        Number.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (Number.getText().equals("Mobile number")) {
                    Number.setText("");
                    Number.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (Number.getText().isEmpty()) {
                    Number.setText("Mobile number");
                    Number.setForeground(Color.GRAY);
                }
            }
        });

        // --- PASSWORD PLACEHOLDER ---
        Password.setText("Enter Password");
        Password.setForeground(Color.GRAY);
        Password.setEchoChar((char) 0); // Show placeholder text

        Password.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (String.valueOf(Password.getPassword()).equals("Enter Password")) {
                    Password.setText("");
                    Password.setForeground(Color.BLACK);
                    Password.setEchoChar('•'); // Hide as user types
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (String.valueOf(Password.getPassword()).isEmpty()) {
                    Password.setText("Enter Password");
                    Password.setForeground(Color.GRAY);
                    Password.setEchoChar((char) 0); // Show placeholder again
                }
            }
        });
        
        // --- CONFIRM PASSWORD PLACEHOLDER ---
        Repassword.setText("Confirm Password");
        Repassword.setForeground(Color.GRAY);
        Repassword.setEchoChar((char) 0); // Show placeholder text

        Repassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (String.valueOf(Repassword.getPassword()).equals("Confirm Password")) {
                    Repassword.setText("");
                    Repassword.setForeground(Color.BLACK);
                    Repassword.setEchoChar('•'); // Hide as user types
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (String.valueOf(Repassword.getPassword()).isEmpty()) {
                    Repassword.setText("Confirm Password");
                    Repassword.setForeground(Color.GRAY);
                    Repassword.setEchoChar((char) 0); // Show placeholder again
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new util.RoundedPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtFirstname = new javax.swing.JTextField();
        Lastname = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        Number = new javax.swing.JTextField();
        CreateButton = new javax.swing.JButton();
        Email = new javax.swing.JTextField();
        Icon = new javax.swing.JLabel();
        Repassword = new javax.swing.JPasswordField();
        BacktoLogin = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(230, 216, 195));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/bagong logo.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(200, 143));
        jLabel1.setMinimumSize(new java.awt.Dimension(200, 143));
        jLabel1.setPreferredSize(new java.awt.Dimension(200, 143));

        jPanel2.setBackground(new java.awt.Color(234, 228, 213));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setText("Create a new account");

        txtFirstname.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtFirstname.setText("First name");
        txtFirstname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtFirstname.setSelectionColor(new java.awt.Color(194, 166, 140));
        txtFirstname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFirstnameActionPerformed(evt);
            }
        });

        Lastname.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Lastname.setText("Last name");
        Lastname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        Lastname.setSelectionColor(new java.awt.Color(194, 166, 140));

        Password.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        Password.setSelectionColor(new java.awt.Color(194, 166, 140));

        Number.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Number.setText("Mobile number");
        Number.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        Number.setSelectionColor(new java.awt.Color(194, 166, 140));
        Number.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NumberActionPerformed(evt);
            }
        });

        CreateButton.setBackground(new java.awt.Color(204, 204, 204));
        CreateButton.setText("Create an Account");
        CreateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateButtonActionPerformed(evt);
            }
        });

        Email.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        Email.setText("Email");
        Email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailActionPerformed(evt);
            }
        });

        Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/book.png"))); // NOI18N

        BacktoLogin.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        BacktoLogin.setText("Already have an account?");
        BacktoLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BacktoLogin.setContentAreaFilled(false);
        BacktoLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BacktoLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BacktoLoginActionPerformed(evt);
            }
        });

        jLabel3.setText("\"Please register to create your library account.\"");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(Repassword, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(50, 50, 50)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Number, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(Icon)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(CreateButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(txtFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(Lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel3)))
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BacktoLogin)
                .addGap(129, 129, 129))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Icon))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Number, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(Repassword, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(CreateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BacktoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ronald\\Documents\\NetBeansProjects\\Library-System-Project\\src\\resources\\553574027_1727697471531176_6493368102668136107_n (1).png")); // NOI18N
        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(288, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(159, 159, 159)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(138, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(301, 301, 301))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NumberActionPerformed

    private void txtFirstnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFirstnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFirstnameActionPerformed

    private void EmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailActionPerformed

    private void CreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateButtonActionPerformed
    String first = txtFirstname.getText().trim();
    String last = Lastname.getText().trim();
    String email = Email.getText().trim();
    String mobile = Number.getText().trim();
    String pass = new String(Password.getPassword());
    String repass = new String(Repassword.getPassword());

    // 🔸 Validation checks
    if (first.isEmpty() || last.isEmpty() || email.isEmpty() || mobile.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
        util.AlertUtil.showRoundedToastTopRight(this, "All fields are required!", new java.awt.Color(241, 196, 15));
        return;
    }

    if (!pass.equals(repass)) {
        util.AlertUtil.showRoundedToastTopRight(this, "Passwords do not match!", new java.awt.Color(231, 76, 60));
        return;
    }

    if (!email.contains("@") || !email.contains(".")) {
        util.AlertUtil.showRoundedToastTopRight(this, "Invalid email format!", new java.awt.Color(241, 196, 15));
        return;
    }

    if (mobile.length() != 11 || !mobile.matches("\\d+")) {
        util.AlertUtil.showRoundedToastTopRight(this, "Mobile number must be 11 digits.", new java.awt.Color(241, 196, 15));
        return;
    }

    // 🔸 Create a new User object
    model.User user = new model.User("student", first, last, email, mobile, pass);

    // 🔸 Save user to file (users.txt)
    boolean success = util.UserDataManager.addUser(user);

    if (success) {
        util.AlertUtil.showRoundedToastTopRight(this, "Account created successfully!", new java.awt.Color(46, 204, 113));

        // Reset fields
        txtFirstname.setText("First name");
        Lastname.setText("Last name");
        Email.setText("Email");
        Number.setText("Mobile number");
        Password.setText("");
        Repassword.setText("");

    } else {
        util.AlertUtil.showRoundedToastTopRight(this, "Account already exists!", new java.awt.Color(231, 76, 60));
    }

    }//GEN-LAST:event_CreateButtonActionPerformed

    private void BacktoLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BacktoLoginActionPerformed

        // this runs when the button is clicked
        Main createFrame = new Main();
        createFrame.setVisible(true);
        this.dispose(); // closes the current frame (optional)
    }//GEN-LAST:event_BacktoLoginActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CreateAccount().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton BacktoLogin;
    private javax.swing.JButton CreateButton;
    private javax.swing.JTextField Email;
    private javax.swing.JLabel Icon;
    private javax.swing.JTextField Lastname;
    private javax.swing.JTextField Number;
    private javax.swing.JPasswordField Password;
    private javax.swing.JPasswordField Repassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtFirstname;
    // End of variables declaration//GEN-END:variables
}
