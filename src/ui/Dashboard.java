//@ian
package ui;

import util.FrameUtil;
import util.btnBorderless;
import util.CircleEdgebtn;
import util.TableStyleUtil;
import util.ButtonRendererEditor;
import data.MemberDataManager;
import model.Member;

import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.TableRowSorter;
import data.BookDataManager;
import model.Book;

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
        
        setupActionButtons();
        
        // Button styles
        btnBorderless.styleBorderlessButton(btnMembers);
        btnBorderless.styleBorderlessButton(btnTransactions);
        btnBorderless.styleBorderlessButton(btnReports);
        btnBorderless.styleBorderlessButton(btnBooks);
        CircleEdgebtn.styleForwardButton(btnLogout);
        
        // ========== MEMBERS TABLE SETUP ==========
        TableStyleUtil.styleModernTable(MemberTable, jScrollPane1);
        TableStyleUtil.centerColumn(MemberTable, 0); // Member ID
        TableStyleUtil.centerColumn(MemberTable, 5); // Actions
        TableStyleUtil.setColumnWidths(MemberTable, 100, 150, 250, 150, 150, 120);
        MemberTable.getTableHeader().setResizingAllowed(false);
        
        // ========== BOOKS TABLE SETUP ==========
        TableStyleUtil.styleModernTable(BooksTable, jScrollPane2);
        TableStyleUtil.centerColumn(BooksTable, 0); // Title
        TableStyleUtil.centerColumn(BooksTable, 7); // Actions
        TableStyleUtil.setColumnWidths(BooksTable, 150, 120, 120, 130, 120, 100, 100, 100);
        BooksTable.getTableHeader().setResizingAllowed(false);
        
        // Load data + setup action buttons
        loadMembersToTable();
        setupActionButtons();
        
        // Load Book data + setup action buttons
        loadBooksToTable();
        setupBooksActionButtons();
        
        
        // listeners
        btnAddMember.addActionListener(evt -> addMember());
        MemberSearchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchMember();
            }
        });
        
        btnAddBooks.addActionListener(evt -> addBook());
        BookSearchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchBook();
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
        String query = MemberSearchBar.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) MemberTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        MemberTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    // ─────────────── Action Buttons Setup ───────────────
    private void setupActionButtons() {
        TableColumn actionColumn = MemberTable.getColumnModel().getColumn(5);

        ButtonRendererEditor.ActionHandler handler = new ButtonRendererEditor.ActionHandler() {
            @Override
            public void onEdit(int row) {
                editRow(row);
            }

            @Override
            public void onDelete(int row) {
                deleteRow(row);
            }
        };

        ButtonRendererEditor buttonEditor = new ButtonRendererEditor(handler);
        actionColumn.setCellRenderer(buttonEditor);
        actionColumn.setCellEditor(buttonEditor);

        // ✅ CRITICAL: Enable editing on single click
        MemberTable.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);

        // ✅ Stop editing when focus leaves the table
        MemberTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    // ─────────────── Edit Member ───────────────
    private void editRow(int row) {
        if (row < 0) return;
        DefaultTableModel model = (DefaultTableModel) MemberTable.getModel();

        String id = model.getValueAt(row, 0).toString();
        String name = model.getValueAt(row, 1).toString();
        String email = model.getValueAt(row, 2).toString();
        String phone = model.getValueAt(row, 3).toString();
        String address = model.getValueAt(row, 4).toString();

        JTextField nameField = new JTextField(name);
        JTextField emailField = new JTextField(email);
        JTextField phoneField = new JTextField(phone);
        JTextField addressField = new JTextField(address);

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1));
        panel.setBackground(new java.awt.Color(230, 216, 195)); // soft beige
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Edit Member", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            Member updatedMember = new Member(
                    id,
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim()
            );

            if (MemberDataManager.updateMember(updatedMember)) {
                JOptionPane.showMessageDialog(this, "Member updated successfully!");
                loadMembersToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update member.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// ========== DELETE MEMBERS ==========
    private void deleteRow(int row) {
        if (row < 0) return;
        DefaultTableModel model = (DefaultTableModel) MemberTable.getModel();
        String id = model.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete this member?", "Confirm Delete", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (MemberDataManager.deleteMember(id)) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                loadMembersToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete member.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }  
    }


//======================================================================================================================================================


    // ========== LOAD BOOKS METHOD ==========
    private void loadBooksToTable() {
        DefaultTableModel model = (DefaultTableModel) BooksTable.getModel();
        model.setRowCount(0);

        List<Book> books = BookDataManager.loadBooks();  // You'll need to create this
        for (Book b : books) {
            model.addRow(new Object[]{
                b.getTitle(), 
                b.getAuthor(), 
                b.getIsbn(), 
                b.getCategory(), 
                b.getTotalCopies(), 
                b.getAvailableCopies(), 
                b.getStatus(), 
                ""  // Actions column
            });
        }
    }

    // ========== ADD NEW BOOK METHOD ==========
    private void addBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField totalCopiesField = new JTextField();

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Total Copies:"));
        panel.add(totalCopiesField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Book newBook = new Book(
                    titleField.getText(),
                    authorField.getText(),
                    isbnField.getText(),
                    categoryField.getText(),
                    Integer.parseInt(totalCopiesField.getText())
            );

            boolean success = BookDataManager.addBook(newBook);
            if (success) {
                JOptionPane.showMessageDialog(this, "✅ Book added successfully!");
                loadBooksToTable();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Failed to add book.");
            }
        }
    }

    // ========== SEARCH BOOKS METHOD ==========
    private void searchBook() {
        String query = BookSearchBar.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) BooksTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        BooksTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    // ========== SETUP BOOKS ACTION BUTTONS ==========
    private void setupBooksActionButtons() {
        TableColumn actionColumn = BooksTable.getColumnModel().getColumn(7);

        ButtonRendererEditor.ActionHandler handler = new ButtonRendererEditor.ActionHandler() {
            @Override
            public void onEdit(int row) {
                editBook(row);
            }

            @Override
            public void onDelete(int row) {
                deleteBook(row);
            }
        };

        ButtonRendererEditor buttonEditor = new ButtonRendererEditor(handler);
        actionColumn.setCellRenderer(buttonEditor);
        actionColumn.setCellEditor(buttonEditor);

        BooksTable.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
        BooksTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    // ========== EDIT BOOK METHOD ==========
    private void editBook(int row) {
        if (row < 0) return;
        DefaultTableModel model = (DefaultTableModel) BooksTable.getModel();

        String title = model.getValueAt(row, 0).toString();
        String author = model.getValueAt(row, 1).toString();
        String isbn = model.getValueAt(row, 2).toString();
        String category = model.getValueAt(row, 3).toString();
        String totalCopies = model.getValueAt(row, 4).toString();
        String availableCopies = model.getValueAt(row, 5).toString();
        String status = model.getValueAt(row, 6).toString();

        JTextField titleField = new JTextField(title);
        JTextField authorField = new JTextField(author);
        JTextField isbnField = new JTextField(isbn);
        JTextField categoryField = new JTextField(category);
        JTextField totalCopiesField = new JTextField(totalCopies);
        JTextField availableCopiesField = new JTextField(availableCopies);
        JTextField statusField = new JTextField(status);

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Total Copies:"));
        panel.add(totalCopiesField);
        panel.add(new JLabel("Available Copies:"));
        panel.add(availableCopiesField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Book updatedBook = new Book(
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    isbnField.getText().trim(),
                    categoryField.getText().trim(),
                    Integer.parseInt(totalCopiesField.getText())
            );

            if (BookDataManager.updateBook(updatedBook)) {
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
                loadBooksToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ========== DELETE BOOK METHOD ==========
    private void deleteBook(int row) {
        if (row < 0) return;
        DefaultTableModel model = (DefaultTableModel) BooksTable.getModel();
        String title = model.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (BookDataManager.deleteBook(title)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                loadBooksToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


//======================================================================================================================================================



























  
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
        MemberSearchBar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        MemberTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BooksPanel = new javax.swing.JPanel();
        btnAddBooks = new javax.swing.JButton();
        BookSearchBar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        BooksTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        TransactionPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        ReportsPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

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

        MemberSearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        MemberSearchBar.setText("Search Bar");
        MemberSearchBar.setFocusable(false);

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
                false, false, false, false, false, true
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
                            .addComponent(MemberSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(MemberSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        TabbedPane.addTab("Members", MemberPanel);

        BooksPanel.setBackground(new java.awt.Color(245, 239, 231));

        btnAddBooks.setBackground(new java.awt.Color(222, 207, 187));
        btnAddBooks.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnAddBooks.setText("Add Member Button");
        btnAddBooks.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddBooks.setFocusable(false);

        BookSearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        BookSearchBar.setText("Search Bar");
        BookSearchBar.setFocusable(false);

        BooksTable.setBackground(new java.awt.Color(245, 239, 231));
        BooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            }, // **Change this line to an empty array: new Object [][] {}**
            new String [] {
                "Title", "Author", "ISBN", "Category", "Total Copies", "Available", "Status", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class,
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        BooksTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(BooksTable);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setText("Member Management");

        jLabel8.setText("Register and manage library members");

        javax.swing.GroupLayout BooksPanelLayout = new javax.swing.GroupLayout(BooksPanel);
        BooksPanel.setLayout(BooksPanelLayout);
        BooksPanelLayout.setHorizontalGroup(
            BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BooksPanelLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addGroup(BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addGroup(BooksPanelLayout.createSequentialGroup()
                            .addComponent(BookSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(btnAddBooks)))
                    .addComponent(jLabel7))
                .addContainerGap(123, Short.MAX_VALUE))
        );
        BooksPanelLayout.setVerticalGroup(
            BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BooksPanelLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddBooks, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BookSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );

        TabbedPane.addTab("Books", BooksPanel);

        TransactionPanel.setBackground(new java.awt.Color(245, 239, 231));

        jLabel6.setFont(new java.awt.Font("Serif", 3, 36)); // NOI18N
        jLabel6.setText("Coming Soon.");

        javax.swing.GroupLayout TransactionPanelLayout = new javax.swing.GroupLayout(TransactionPanel);
        TransactionPanel.setLayout(TransactionPanelLayout);
        TransactionPanelLayout.setHorizontalGroup(
            TransactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TransactionPanelLayout.createSequentialGroup()
                .addGap(525, 525, 525)
                .addComponent(jLabel6)
                .addContainerGap(644, Short.MAX_VALUE))
        );
        TransactionPanelLayout.setVerticalGroup(
            TransactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TransactionPanelLayout.createSequentialGroup()
                .addGap(361, 361, 361)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(416, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Transaction", TransactionPanel);

        ReportsPanel.setBackground(new java.awt.Color(245, 239, 231));

        jLabel4.setFont(new java.awt.Font("Serif", 3, 36)); // NOI18N
        jLabel4.setText("Coming Soon.");

        javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
        ReportsPanel.setLayout(ReportsPanelLayout);
        ReportsPanelLayout.setHorizontalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGap(525, 525, 525)
                .addComponent(jLabel4)
                .addContainerGap(644, Short.MAX_VALUE))
        );
        ReportsPanelLayout.setVerticalGroup(
            ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsPanelLayout.createSequentialGroup()
                .addGap(361, 361, 361)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(416, Short.MAX_VALUE))
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
    private javax.swing.JTextField BookSearchBar;
    private javax.swing.JPanel BooksPanel;
    private javax.swing.JTable BooksTable;
    private javax.swing.JPanel ButtonPanels;
    private javax.swing.JPanel MemberPanel;
    private javax.swing.JTextField MemberSearchBar;
    private javax.swing.JTable MemberTable;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JPanel TransactionPanel;
    private javax.swing.JButton btnAddBooks;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
