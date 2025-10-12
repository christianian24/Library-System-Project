//@ian

package ui;

import util.FrameUtil;
import util.btnBorderless;
import util.CircleEdgebtn;
import util.TableStyleUtil;
import util.MemberDataManager;
import model.Member;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author User
 */
public class Dashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dashboard.class.getName());

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        FrameUtil.setupFrame(this);

        // Button styles
        btnBorderless.styleBorderlessButton(btnMembers);
        btnBorderless.styleBorderlessButton(btnTransactions);
        btnBorderless.styleBorderlessButton(btnReports);
        btnBorderless.styleBorderlessButton(btnBooks);
        CircleEdgebtn.styleForwardButton(btnLogout);
        
        util.TableStyleUtil.styleModernTable(MemberTable, jScrollPane1);
        
        // 🪄 Apply modern style
        util.TableStyleUtil.styleModernTable(MemberTable, jScrollPane1);

        // Optional alignment
        util.TableStyleUtil.centerColumn(MemberTable, 0); // Member ID
        util.TableStyleUtil.centerColumn(MemberTable, 5); // Actions

        // 🧭 Set custom column widths
        util.TableStyleUtil.setColumnWidths(MemberTable, 100, 150, 250, 150, 150, 120);
        MemberTable.getTableHeader().setResizingAllowed(false);
        
        
        
        
        // Load data + events
        loadMembersToTable();

        btnAddMember.addActionListener(evt -> addMember());
        SearchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchMember();
            }
        });
        
        
    }

    // ─────────────── Load Members ───────────────
    private void loadMembersToTable() {
        DefaultTableModel model = (DefaultTableModel) MemberTable.getModel();
        model.setRowCount(0);

        List<Member> members = MemberDataManager.loadMembers();
        for (Member m : members) {
            model.addRow(new Object[]{m.getId(), m.getName(), m.getEmail(), m.getPhone(), m.getMemberSince(), ""});
        }
    }

    // ─────────────── Add New Member ───────────────
    private void addMember() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1));
        panel.add(new JLabel("Member ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Member", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            Member newMember = new Member(
                    idField.getText(),
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    date
            );

            boolean success = MemberDataManager.addMember(newMember);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Member added successfully!");
                loadMembersToTable();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Duplicate email or phone number found.");
            }
        }
    }

    // ─────────────── Search Filter ───────────────
    private void searchMember() {
        String query = SearchBar.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) MemberTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        MemberTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        ButtonPanels = new javax.swing.JPanel();
        btnMembers = new javax.swing.JButton();
        btnBooks = new javax.swing.JButton();
        btnTransactions = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TabbedPane = new javax.swing.JTabbedPane();
        MemberPanel = new javax.swing.JPanel();
        btnAddMember = new javax.swing.JButton();
        SearchBar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        MemberTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BooksPanel = new javax.swing.JPanel();
        TransactionPanel = new javax.swing.JPanel();
        ReportsPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ButtonPanels.setBackground(new java.awt.Color(230, 216, 195));

        btnMembers.setBackground(new java.awt.Color(211, 196, 174));
        btnMembers.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        btnMembers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/group_35dp_000000_FILL0_wght400_GRAD0_opsz40.png"))); // NOI18N
        btnMembers.setText("Members");
        btnMembers.setToolTipText("");
        btnMembers.setBorder(null);
        buttonGroup1.add(btnMembers);
        btnMembers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMembers.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnMembers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembersActionPerformed(evt);
            }
        });

        btnBooks.setBackground(new java.awt.Color(211, 196, 174));
        btnBooks.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        btnBooks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/book_2_35dp_000000_FILL0_wght400_GRAD0_opsz40.png"))); // NOI18N
        btnBooks.setText("Books");
        btnBooks.setBorder(null);
        buttonGroup1.add(btnBooks);
        btnBooks.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBooks.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnBooks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBooksActionPerformed(evt);
            }
        });

        btnTransactions.setBackground(new java.awt.Color(211, 196, 174));
        btnTransactions.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        btnTransactions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/checkbook_35dp_000000_FILL0_wght400_GRAD0_opsz40.png"))); // NOI18N
        btnTransactions.setText("Transactions");
        btnTransactions.setBorder(null);
        buttonGroup1.add(btnTransactions);
        btnTransactions.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTransactions.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnTransactions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionsActionPerformed(evt);
            }
        });

        btnReports.setBackground(new java.awt.Color(211, 196, 174));
        btnReports.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        btnReports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/assignment_35dp_000000_FILL0_wght400_GRAD0_opsz40.png"))); // NOI18N
        btnReports.setText("Reports");
        btnReports.setBorder(null);
        buttonGroup1.add(btnReports);
        btnReports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReports.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportsActionPerformed(evt);
            }
        });

        btnLogout.setBackground(new java.awt.Color(211, 196, 174));
        btnLogout.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        btnLogout.setText("Log out");
        buttonGroup1.add(btnLogout);
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/logonewsize.png"))); // NOI18N

        javax.swing.GroupLayout ButtonPanelsLayout = new javax.swing.GroupLayout(ButtonPanels);
        ButtonPanels.setLayout(ButtonPanelsLayout);
        ButtonPanelsLayout.setHorizontalGroup(
            ButtonPanelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ButtonPanelsLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(ButtonPanelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(14, 14, 14))
            .addGroup(ButtonPanelsLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(ButtonPanelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnReports, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTransactions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMembers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBooks, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ButtonPanelsLayout.setVerticalGroup(
            ButtonPanelsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ButtonPanelsLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel5)
                .addGap(33, 33, 33)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(btnMembers, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBooks, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTransactions, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReports, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );

        jPanel1.add(ButtonPanels, new org.netbeans.lib.awtextra.AbsoluteConstraints(-8, 60, 230, 840));

        jPanel9.setBackground(new java.awt.Color(182, 176, 159));

        jLabel1.setFont(new java.awt.Font("Monotype Corsiva", 1, 36)); // NOI18N
        jLabel1.setText("ARCHIVA LIBRARY SYSTEM");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1100, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1600, 60));

        MemberPanel.setBackground(new java.awt.Color(245, 239, 231));

        btnAddMember.setBackground(new java.awt.Color(222, 207, 187));
        btnAddMember.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnAddMember.setText("Add Member Button");
        btnAddMember.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddMember.setFocusable(false);

        SearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SearchBar.setText("Search Bar");
        SearchBar.setFocusable(false);

        MemberTable.setBackground(new java.awt.Color(245, 239, 231));
        MemberTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            }, // **Change this line to an empty array: new Object [][] {}**
            new String [] {
                "Member ID", "Name", "Email", "Phone", "Member Since", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        MemberTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(MemberTable);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setText("Member Management");

        jLabel3.setText("Register and manage library members");

        javax.swing.GroupLayout MemberPanelLayout = new javax.swing.GroupLayout(MemberPanel);
        MemberPanel.setLayout(MemberPanelLayout);
        MemberPanelLayout.setHorizontalGroup(
            MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberPanelLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addGroup(MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addGroup(MemberPanelLayout.createSequentialGroup()
                            .addComponent(SearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(btnAddMember)))
                    .addComponent(jLabel2))
                .addContainerGap(123, Short.MAX_VALUE))
        );
        MemberPanelLayout.setVerticalGroup(
            MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MemberPanelLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddMember, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        TabbedPane.addTab("Members", MemberPanel);

        javax.swing.GroupLayout BooksPanelLayout = new javax.swing.GroupLayout(BooksPanel);
        BooksPanel.setLayout(BooksPanelLayout);
        BooksPanelLayout.setHorizontalGroup(
            BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
        );
        BooksPanelLayout.setVerticalGroup(
            BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 845, Short.MAX_VALUE)
        );

        TabbedPane.addTab("Books", BooksPanel);

        javax.swing.GroupLayout TransactionPanelLayout = new javax.swing.GroupLayout(TransactionPanel);
        TransactionPanel.setLayout(TransactionPanelLayout);
        TransactionPanelLayout.setHorizontalGroup(
            TransactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
        );
        TransactionPanelLayout.setVerticalGroup(
            TransactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 845, Short.MAX_VALUE)
        );

        TabbedPane.addTab("Transaction", TransactionPanel);

        javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
        ReportsPanel.setLayout(ReportsPanelLayout);
        ReportsPanelLayout.setHorizontalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
        );
        ReportsPanelLayout.setVerticalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 845, Short.MAX_VALUE)
        );

        TabbedPane.addTab("Reports", ReportsPanel);

        jPanel1.add(TabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 1380, 880));

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

    private void btnBooksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBooksActionPerformed
        TabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_btnBooksActionPerformed

    private void btnTransactionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionsActionPerformed
        TabbedPane.setSelectedIndex(2);
    }//GEN-LAST:event_btnTransactionsActionPerformed

    private void btnMembersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembersActionPerformed
        TabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_btnMembersActionPerformed

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        TabbedPane.setSelectedIndex(3);
    }//GEN-LAST:event_btnReportsActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        Main createFrame = new Main();
        createFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Dashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BooksPanel;
    private javax.swing.JPanel ButtonPanels;
    private javax.swing.JPanel MemberPanel;
    private javax.swing.JTable MemberTable;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JTextField SearchBar;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JPanel TransactionPanel;
    private javax.swing.JButton btnAddMember;
    private javax.swing.JButton btnBooks;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMembers;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnTransactions;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
