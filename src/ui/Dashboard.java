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
import util.RoundedPanel;
import util.ScrollStyleUtil;

import data.TransactionDataManager;
import model.Transaction;
import util.ReturnButtonRenderer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.GridLayout;


import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;

/**
 *
 * @Cabilen
 */
public class Dashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dashboard.class.getName());

    public Dashboard() {
        initComponents();
        FrameUtil.setupFrame(this);
        
        setupActionButtons();
        
        //========== Button styles ==========
        btnBorderless.styleBorderlessButton(btnMembers);
        btnBorderless.styleBorderlessButton(btnTransactions);
        btnBorderless.styleBorderlessButton(btnReports);
        btnBorderless.styleBorderlessButton(btnBooks);
        CircleEdgebtn.styleForwardButton(btnLogout);
        
        // ========== MEMBERS TABLE SETUP ==========
        TableStyleUtil.styleModernTable(MemberTable, jScrollPane1);
        ScrollStyleUtil.styleModernScroll(jScrollPane1);
        TableStyleUtil.centerColumn(MemberTable, 0); // Member ID
        TableStyleUtil.centerColumn(MemberTable, 5); // Actions
        TableStyleUtil.setColumnWidths(MemberTable, 100, 150, 250, 150, 150, 120);
        MemberTable.getTableHeader().setResizingAllowed(false);
        
        // ========== BOOKS TABLE SETUP ==========
        TableStyleUtil.styleModernTable(BooksTable, jScrollPane2);
        ScrollStyleUtil.styleModernScroll(jScrollPane2);
        TableStyleUtil.centerColumn(BooksTable, 0); // Title
        TableStyleUtil.centerColumn(BooksTable, 7); // Actions
        TableStyleUtil.setColumnWidths(BooksTable, 150, 120, 120, 130, 120, 100, 100, 100);
        BooksTable.getTableHeader().setResizingAllowed(false);
        
        TransactionTable1.setRowHeight(50);
        TableStyleUtil.styleModernTable(TransactionTable1, jScrollPane3, new int[]{6});
        ScrollStyleUtil.styleModernScroll(jScrollPane3);
        TableStyleUtil.setColumnWidths(TransactionTable1, 180, 150, 120, 120, 120, 100, 120);
        TransactionTable1.getTableHeader().setResizingAllowed(false);
        
        //========== Load data + setup action buttons ==========
        loadMembersToTable();
        setupActionButtons();
        loadBooksToTable();
        setupBooksActionButtons();
        loadTransactionsToTable();
        setupTransactionActionButtons();
        setupReportsPanel();
        
        //========== listeners ==========
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
        
        btnTransaction.addActionListener(evt -> issueBook());
        TransactionSearchBar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTransaction();
            }
        });
    }

    //========== Load Members ==========
    private void loadMembersToTable() {
        DefaultTableModel model = (DefaultTableModel) MemberTable.getModel();
        model.setRowCount(0);

        List<Member> members = MemberDataManager.loadMembers();
        for (Member m : members) {
            model.addRow(new Object[]{m.getId(), m.getName(), m.getEmail(), m.getPhone(), m.getMemberSince(), ""});
        }
    }

    //========== Add New Member ==========
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

    //========== Search Filter ==========
    private void searchMember() {
        String query = MemberSearchBar.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) MemberTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        MemberTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    //========== Action Buttons Setup ==========
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

    //========== Edit Member ==========
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
        panel.setBackground(new java.awt.Color(230, 216, 195));
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
        JTextField totalCopiesField = new JTextField();

        // Create ComboBox for genres
        String[] genres = {
            "Fiction",
            "Non-Fiction",
            "Science",
            "History",
            "Biography",
            "Fantasy",
            "Mystery",
            "Romance",
            "Thriller",
            "Self-Help",
            "Technology",
            "Business",
            "Philosophy",
            "Poetry",
            "Other"
        };
        JComboBox<String> categoryComboBox = new JComboBox<>(genres);
        categoryComboBox.setBackground(new java.awt.Color(245, 239, 231));

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1, 5, 5));
        panel.setBackground(new java.awt.Color(230, 216, 195));

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Category/Genre:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("Total Copies:"));
        panel.add(totalCopiesField);

        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Add New Book", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate input
                if (titleField.getText().trim().isEmpty() || 
                    authorField.getText().trim().isEmpty() || 
                    isbnField.getText().trim().isEmpty() ||
                    totalCopiesField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ All fields are required!");
                    return;
                }

                int copies = Integer.parseInt(totalCopiesField.getText().trim());
                if (copies <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ Total copies must be greater than 0!");
                    return;
                }

                Book newBook = new Book(
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    isbnField.getText().trim(),
                    categoryComboBox.getSelectedItem().toString(),
                    copies
                );

                boolean success = BookDataManager.addBook(newBook);
                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Book added successfully!");
                    loadBooksToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Failed to add book. ISBN may already exist.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid number for Total Copies!");
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
    // Replace your existing editBook() method with this improved version

// ========== EDIT BOOK METHOD WITH GENRE DROPDOWN ==========
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
        isbnField.setEditable(false); // ISBN shouldn't be changed
        isbnField.setBackground(new java.awt.Color(220, 220, 220));

        JTextField totalCopiesField = new JTextField(totalCopies);
        JTextField availableCopiesField = new JTextField(availableCopies);
        JTextField statusField = new JTextField(status);
        statusField.setEditable(false); // Status is auto-calculated
        statusField.setBackground(new java.awt.Color(220, 220, 220));

        // Create ComboBox for genres with current category selected
        String[] genres = {
            "Fiction",
            "Non-Fiction",
            "Science",
            "History",
            "Biography",
            "Fantasy",
            "Mystery",
            "Romance",
            "Thriller",
            "Self-Help",
            "Technology",
            "Business",
            "Philosophy",
            "Poetry",
            "Other"
        };
        JComboBox<String> categoryComboBox = new JComboBox<>(genres);
        categoryComboBox.setBackground(new java.awt.Color(245, 239, 231));
        categoryComboBox.setSelectedItem(category); // Set current category

        JPanel panel = new JPanel(new java.awt.GridLayout(0, 1, 5, 5));
        panel.setBackground(new java.awt.Color(230, 216, 195));

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN: (Cannot be changed)"));
        panel.add(isbnField);
        panel.add(new JLabel("Category/Genre:"));
        panel.add(categoryComboBox);
        panel.add(new JLabel("Total Copies:"));
        panel.add(totalCopiesField);
        panel.add(new JLabel("Available Copies:"));
        panel.add(availableCopiesField);
        panel.add(new JLabel("Status: (Auto-calculated)"));
        panel.add(statusField);

        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Edit Book", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int total = Integer.parseInt(totalCopiesField.getText().trim());

                if (total <= 0) {
                    JOptionPane.showMessageDialog(this, "⚠️ Total copies must be greater than 0!");
                    return;
                }

                Book updatedBook = new Book(
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    isbnField.getText().trim(),
                    categoryComboBox.getSelectedItem().toString(),
                    total
                );

                if (BookDataManager.updateBook(updatedBook)) {
                    JOptionPane.showMessageDialog(this, "✅ Book updated successfully!");
                    loadBooksToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Failed to update book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid number for Total Copies!");
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


// ========== LOAD TRANSACTIONS METHOD ==========
    private void loadTransactionsToTable() {
        DefaultTableModel model = (DefaultTableModel) TransactionTable1.getModel();
        model.setRowCount(0);

        List<Transaction> transactions = TransactionDataManager.loadTransactions();
        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                t.getBookTitle() + "\n" + t.getBookIsbn(),
                t.getMemberName() + "\n" + t.getMemberId(),
                t.getIssueDate(),
                t.getDueDate(),
                t.getReturnDate(),
                t.getStatus(),
                ""  // Actions column
            });
        }
    }
    
    // ========== ISSUE BOOK METHOD ==========
    private void issueBook() {
        // Create combo boxes for book and member selection
        List<Book> books = BookDataManager.loadBooks();
        List<Member> members = MemberDataManager.loadMembers();

        // Filter available books
        JComboBox<String> bookCombo = new JComboBox<>();
        for (Book book : books) {
            if (book.getAvailableCopies() > 0) {
                bookCombo.addItem(book.getTitle() + " (" + book.getIsbn() + ")");
            }
        }

        JComboBox<String> memberCombo = new JComboBox<>();
        for (Member member : members) {
            memberCombo.addItem(member.getName() + " (" + member.getId() + ")");
        }

        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(14, 1, 90, 1));

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Select Book:"));
        panel.add(bookCombo);
        panel.add(new JLabel("Select Member:"));
        panel.add(memberCombo);
        panel.add(new JLabel("Loan Period (days):"));
        panel.add(daysSpinner);

        int result = JOptionPane.showConfirmDialog(this, panel, "Issue Book", 
                                                   JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (bookCombo.getSelectedItem() == null || memberCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select both book and member.");
                return;
            }

            // Extract ISBN and Member ID
            String bookSelection = bookCombo.getSelectedItem().toString();
            String isbn = bookSelection.substring(bookSelection.lastIndexOf("(") + 1, 
                                                 bookSelection.lastIndexOf(")"));

            String memberSelection = memberCombo.getSelectedItem().toString();
            String memberId = memberSelection.substring(memberSelection.lastIndexOf("(") + 1, 
                                                       memberSelection.lastIndexOf(")"));

            // Get book and member details
            Book selectedBook = null;
            for (Book book : books) {
                if (book.getIsbn().equals(isbn)) {
                    selectedBook = book;
                    break;
                }
            }

            Member selectedMember = null;
            for (Member member : members) {
                if (member.getId().equals(memberId)) {
                    selectedMember = member;
                    break;
                }
            }

            if (selectedBook != null && selectedMember != null) {
                // Calculate dates
                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String issueDate = today.format(formatter);

                int loanDays = (Integer) daysSpinner.getValue();
                LocalDate dueLocalDate = today.plusDays(loanDays);
                String dueDate = dueLocalDate.format(formatter);

                // Create transaction
                Transaction transaction = new Transaction(
                    selectedBook.getTitle(),
                    selectedBook.getIsbn(),
                    selectedMember.getName(),
                    selectedMember.getId(),
                    issueDate,
                    dueDate,
                    "Active"
                );

                // Update book availability
                selectedBook.setAvailableCopies(selectedBook.getAvailableCopies() - 1);
                if (selectedBook.getAvailableCopies() == 0) {
                    selectedBook.setStatus("Unavailable");
                }
                BookDataManager.updateBook(selectedBook);

                // Save transaction
                TransactionDataManager.addTransaction(transaction);

                JOptionPane.showMessageDialog(this, "✅ Book issued successfully!");
                loadTransactionsToTable();
                loadBooksToTable(); // Refresh books table
            }
        }
    }

    // ========== SEARCH TRANSACTION METHOD ==========
    private void searchTransaction() {
        String query = TransactionSearchBar1.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) TransactionTable1.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        TransactionTable1.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    // ========== SETUP TRANSACTION ACTION BUTTONS ==========
    private void setupTransactionActionButtons() {
        TableColumn actionColumn = TransactionTable1.getColumnModel().getColumn(6);

        ReturnButtonRenderer buttonRenderer = new ReturnButtonRenderer();
        actionColumn.setCellRenderer(buttonRenderer);

        actionColumn.setPreferredWidth(120);
        actionColumn.setMinWidth(100);
        actionColumn.setMaxWidth(150);

        ReturnButtonRenderer.ReturnHandler handler = new ReturnButtonRenderer.ReturnHandler() {
            @Override
            public void onReturn(int row) {
                returnBook(row);
            }
        };

        ReturnButtonRenderer.addClickListener(TransactionTable1, 6, handler);

        TransactionTable1.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
    }

    // ========== RETURN BOOK METHOD ==========
    private void returnBook(int row) {
        if (row < 0) return;

        DefaultTableModel model = (DefaultTableModel) TransactionTable1.getModel();
        String bookInfo = model.getValueAt(row, 0).toString();
        String memberInfo = model.getValueAt(row, 1).toString();

        String[] bookParts = bookInfo.split("\n");
        String isbn = bookParts.length > 1 ? bookParts[1] : "";

        String[] memberParts = memberInfo.split("\n");
        String memberId = memberParts.length > 1 ? memberParts[1] : "";

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Confirm book return?",
            "Return Book",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String returnDate = today.format(formatter);

            if (TransactionDataManager.updateTransaction(isbn, memberId, returnDate)) {
                Book book = BookDataManager.findBookByTitle(bookParts[0]);
                if (book != null) {
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                    book.setStatus("Available");
                    BookDataManager.updateBook(book);
                }

                JOptionPane.showMessageDialog(this, "✅ Book returned successfully!");

                // Just reload the table data - buttons will update automatically
                loadTransactionsToTable();
                loadBooksToTable();

                // NO need to call setupTransactionActionButtons() again!
                // The mouse listener is persistent and will continue working
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return book.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//======================================================================================================================================================

    // ========== SETUP REPORTS PANEL ==========
    // ========== SETUP REPORTS PANEL ==========
    private void setupReportsPanel() {
        // Setup stat cards with fixed sizes
        TotalBooks.setPreferredSize(new java.awt.Dimension(270, 171));
        TotalBooks.setMinimumSize(new java.awt.Dimension(270, 171));
        TotalBooks.setMaximumSize(new java.awt.Dimension(270, 171));

        TotalsMembers.setPreferredSize(new java.awt.Dimension(270, 171));
        TotalsMembers.setMinimumSize(new java.awt.Dimension(270, 171));
        TotalsMembers.setMaximumSize(new java.awt.Dimension(270, 171));

        TotalTransactions.setPreferredSize(new java.awt.Dimension(270, 171));
        TotalTransactions.setMinimumSize(new java.awt.Dimension(270, 171));
        TotalTransactions.setMaximumSize(new java.awt.Dimension(270, 171));

        OverdueBooks.setPreferredSize(new java.awt.Dimension(270, 171));
        OverdueBooks.setMinimumSize(new java.awt.Dimension(270, 171));
        OverdueBooks.setMaximumSize(new java.awt.Dimension(270, 171));

        // Setup stat cards with borders and content
        setupStatCard(TotalBooks, "📚 Total Books", 
            String.valueOf(BookDataManager.loadBooks().size()),
            "Available: " + getAvailableBooks() + " | Borrowed: " + getBorrowedBooks(),
            new java.awt.Color(59, 130, 246));

        setupStatCard(TotalsMembers, "👥 Total Members",
            String.valueOf(MemberDataManager.loadMembers().size()),
            "",
            new java.awt.Color(251, 146, 60));

        setupStatCard(TotalTransactions, "🔄 Total Transactions",
            String.valueOf(TransactionDataManager.loadTransactions().size()),
            "Active: " + getActiveTransactions() + " | Returned: " + getReturnedTransactions(),
            new java.awt.Color(34, 197, 94));

        setupStatCard(OverdueBooks, "⚠️ Overdue Books",
            String.valueOf(getOverdueBooks()),
            "",
            new java.awt.Color(239, 68, 68));

        // Setup Books by Category with fixed size
        BookandbyCategory.setPreferredSize(new java.awt.Dimension(1120, 150));
        BookandbyCategory.setMinimumSize(new java.awt.Dimension(1120, 120));
        BookandbyCategory.setMaximumSize(new java.awt.Dimension(1120, 120));
        BookandbyCategory.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 15));
        BookandbyCategory.setBackground(new java.awt.Color(230, 216, 195));
        BookandbyCategory.removeAll();

        JLabel categoryTitle = new JLabel("📚 Books by Category");
        categoryTitle.setFont(new java.awt.Font("Arial", 1, 18));
        categoryTitle.setPreferredSize(new java.awt.Dimension(1050, 30));
        BookandbyCategory.add(categoryTitle);

        String[] allCategories = {
            "Fiction",
            "Non-Fiction", 
            "Science",
            "History",
            "Biography",
            "Fantasy",
            "Mystery",
            "Romance",
            "Thriller",
            "Self-Help",
            "Technology",
            "Business",
            "Philosophy",
            "Poetry",
            "Other"
        };
        
        for (String category : allCategories) {
            int count = getCategoryCount(category);
            BookandbyCategory.add(createCategoryItem(category, count));
        }
        RecentTransactionTable.setRowHeight(40);
        TableStyleUtil.styleModernTable(RecentTransactionTable, jScrollPane4);
        ScrollStyleUtil.styleModernScroll(jScrollPane4);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1120, 230));
        jScrollPane4.setMinimumSize(new java.awt.Dimension(1120, 230));
        jScrollPane4.setMaximumSize(new java.awt.Dimension(1120, 230));
        loadRecentTransactions();
    }

    private void setupStatCard(JPanel card, String title, String value, String subtitle, java.awt.Color iconColor) {
        card.setLayout(new java.awt.BorderLayout(10, 10));
        card.setBackground(new java.awt.Color(245, 239, 231));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.removeAll();

        // Icon
        JLabel iconLabel = new JLabel(title.substring(0, 2)); // Get emoji
        iconLabel.setFont(new java.awt.Font("Segoe UI Emoji", 0, 32));
        iconLabel.setPreferredSize(new java.awt.Dimension(50, 50));

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new java.awt.Color(245, 239, 231));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new java.awt.Font("Arial", 1, 32));

        JLabel titleLabel = new JLabel(title.substring(3)); // Remove emoji
        titleLabel.setFont(new java.awt.Font("Segoe UI", 0, 12));
        titleLabel.setForeground(new java.awt.Color(100, 100, 100));

        contentPanel.add(valueLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(titleLabel);

        if (!subtitle.isEmpty()) {
            contentPanel.add(Box.createVerticalStrut(8));
            JLabel subLabel = new JLabel(subtitle);
            subLabel.setFont(new java.awt.Font("Segoe UI", 0, 11));
            contentPanel.add(subLabel);
        }

        card.add(iconLabel, java.awt.BorderLayout.WEST);
        card.add(contentPanel, java.awt.BorderLayout.CENTER);

        card.revalidate();
        card.repaint();
    }

    private JPanel createCategoryItem(String category, int count) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setBackground(new java.awt.Color(230, 216, 195));

        JLabel countLabel = new JLabel(String.valueOf(count));
        countLabel.setFont(new java.awt.Font("Arial", 1, 36));
        countLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        categoryLabel.setForeground(new java.awt.Color(100, 100, 100));
        categoryLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        item.add(countLabel);
        item.add(categoryLabel);

        return item;
    }

    private void loadRecentTransactions() {
        DefaultTableModel model = (DefaultTableModel) RecentTransactionTable.getModel();
        model.setRowCount(0);

        List<Transaction> transactions = TransactionDataManager.loadTransactions();

        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                t.getBookTitle(),
                t.getMemberName(),
                t.getIssueDate(),
                t.getDueDate(),
                t.getReturnDate(),
                t.getStatus()
            });
        }
    }

    // Add these helper methods to your Dashboard class (before the closing brace)

    // ========== HELPER METHODS FOR REPORTS ==========

    private int getAvailableBooks() {
        List<Book> books = BookDataManager.loadBooks();
        int available = 0;
        for (Book book : books) {
            available += book.getAvailableCopies();
        }
        return available;
    }

    private int getBorrowedBooks() {
        List<Book> books = BookDataManager.loadBooks();
        int borrowed = 0;
        for (Book book : books) {
            borrowed += (book.getTotalCopies() - book.getAvailableCopies());
        }
        return borrowed;
    }

    private int getActiveTransactions() {
        List<Transaction> transactions = TransactionDataManager.loadTransactions();
        int active = 0;
        for (Transaction t : transactions) {
            if ("Active".equalsIgnoreCase(t.getStatus())) {
                active++;
            }
        }
        return active;
    }

    private int getReturnedTransactions() {
        List<Transaction> transactions = TransactionDataManager.loadTransactions();
        int returned = 0;
        for (Transaction t : transactions) {
            if ("Returned".equalsIgnoreCase(t.getStatus())) {
                returned++;
            }
        }
        return returned;
    }

    private int getOverdueBooks() {
        List<Transaction> transactions = TransactionDataManager.loadTransactions();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        int overdue = 0;

        for (Transaction t : transactions) {
            if ("Active".equalsIgnoreCase(t.getStatus())) {
                try {
                    LocalDate dueDate = LocalDate.parse(t.getDueDate(), formatter);
                    if (today.isAfter(dueDate)) {
                        overdue++;
                    }
                } catch (Exception e) {
                    logger.log(java.util.logging.Level.WARNING, "Error parsing date", e);
                }
            }
        }
        return overdue;
    }

    private int getCategoryCount(String category) {
        List<Book> books = BookDataManager.loadBooks();
        int count = 0;
        for (Book book : books) {
            if (category.equalsIgnoreCase(book.getCategory())) {
                count++;
            }
        }
        return count;
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
        jPanel2 = new RoundedPanel(30);
        jScrollPane1 = new javax.swing.JScrollPane();
        MemberTable = new javax.swing.JTable();
        MemberSearchBar = new javax.swing.JTextField();
        btnAddMember = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        BooksPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new RoundedPanel(30);
        jScrollPane2 = new javax.swing.JScrollPane();
        BooksTable = new javax.swing.JTable();
        BookSearchBar = new javax.swing.JTextField();
        btnAddBooks = new javax.swing.JButton();
        TransactionPanel = new javax.swing.JPanel();
        jPanel11 = new RoundedPanel(30);
        TransactionSearchBar1 = new javax.swing.JTextField();
        btnTransaction = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TransactionTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        ReportsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        RecentTransactionTable = new javax.swing.JTable();
        BookandbyCategory = new javax.swing.JPanel();
        TotalBooks = new javax.swing.JPanel();
        TotalsMembers = new javax.swing.JPanel();
        TotalTransactions = new javax.swing.JPanel();
        OverdueBooks = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

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

        jPanel9.setBackground(new java.awt.Color(222, 217, 203));

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

        jPanel2.setBackground(new java.awt.Color(230, 216, 195));
        jPanel2.setPreferredSize(new java.awt.Dimension(1200, 700));

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

        MemberSearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        MemberSearchBar.setText("Search Bar");
        MemberSearchBar.setFocusable(false);
        MemberSearchBar.setPreferredSize(new java.awt.Dimension(79, 30));
        MemberSearchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MemberSearchBarActionPerformed(evt);
            }
        });

        btnAddMember.setBackground(new java.awt.Color(222, 207, 187));
        btnAddMember.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnAddMember.setText("Add Member");
        btnAddMember.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddMember.setFocusable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(MemberSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 966, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddMember, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MemberSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setText("Member Management");

        jLabel3.setText("Register and manage library members");

        javax.swing.GroupLayout MemberPanelLayout = new javax.swing.GroupLayout(MemberPanel);
        MemberPanel.setLayout(MemberPanelLayout);
        MemberPanelLayout.setHorizontalGroup(
            MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberPanelLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        MemberPanelLayout.setVerticalGroup(
            MemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberPanelLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Members", MemberPanel);

        BooksPanel.setBackground(new java.awt.Color(245, 239, 231));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setText("Books Management");

        jLabel8.setText("Add, view, and manage library books");

        jPanel7.setBackground(new java.awt.Color(230, 216, 195));

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

        BookSearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        BookSearchBar.setText("Search Bar");
        BookSearchBar.setFocusable(false);
        BookSearchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BookSearchBarActionPerformed(evt);
            }
        });

        btnAddBooks.setBackground(new java.awt.Color(222, 207, 187));
        btnAddBooks.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnAddBooks.setText("Add Book");
        btnAddBooks.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddBooks.setFocusable(false);
        btnAddBooks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddBooksActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(BookSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 966, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddBooks, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BookSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddBooks, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout BooksPanelLayout = new javax.swing.GroupLayout(BooksPanel);
        BooksPanel.setLayout(BooksPanelLayout);
        BooksPanelLayout.setHorizontalGroup(
            BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BooksPanelLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        BooksPanelLayout.setVerticalGroup(
            BooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BooksPanelLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        TabbedPane.addTab("Books", BooksPanel);

        TransactionPanel.setBackground(new java.awt.Color(245, 239, 231));

        jPanel11.setBackground(new java.awt.Color(230, 216, 195));

        TransactionSearchBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TransactionSearchBar1.setText("Search Bar");
        TransactionSearchBar1.setFocusable(false);
        TransactionSearchBar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TransactionSearchBar1ActionPerformed(evt);
            }
        });

        btnTransaction.setBackground(new java.awt.Color(222, 207, 187));
        btnTransaction.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnTransaction.setText("Transaction");
        btnTransaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTransaction.setFocusable(false);
        btnTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionActionPerformed(evt);
            }
        });

        TransactionTable1.setBackground(new java.awt.Color(245, 239, 231));
        TransactionTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            }, // **Change this line to an empty array: new Object [][] {}**
            new String [] {
                "Book", "Member", "Issue Date", "Due Date", "Return Date", "Status", "Actions"
            }) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, true
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            TransactionTable1.getTableHeader().setReorderingAllowed(false);
            jScrollPane3.setViewportView(TransactionTable1);

            javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
            jPanel11.setLayout(jPanel11Layout);
            jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addComponent(TransactionSearchBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 966, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(btnTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 26, Short.MAX_VALUE))
            );
            jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TransactionSearchBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                    .addGap(25, 25, 25))
            );

            jLabel9.setText("Manage borrowing and returning of books");

            jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
            jLabel10.setText("Transaction Management");

            javax.swing.GroupLayout TransactionPanelLayout = new javax.swing.GroupLayout(TransactionPanel);
            TransactionPanel.setLayout(TransactionPanelLayout);
            TransactionPanelLayout.setHorizontalGroup(
                TransactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TransactionPanelLayout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addGroup(TransactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10))
                    .addContainerGap(93, Short.MAX_VALUE))
            );
            TransactionPanelLayout.setVerticalGroup(
                TransactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(TransactionPanelLayout.createSequentialGroup()
                    .addGap(46, 46, 46)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel9)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(128, Short.MAX_VALUE))
            );

            TabbedPane.addTab("Transaction", TransactionPanel);

            ReportsPanel.setBackground(new java.awt.Color(245, 239, 231));

            RecentTransactionTable.setBackground(new java.awt.Color(245, 239, 231));
            RecentTransactionTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                }, // **Change this line to an empty array: new Object [][] {}**
                new String [] {
                    "Book", "Member", "Issue Date", "Due Date", "Return Date", "Status"
                }) {
                    Class[] types = new Class [] {
                        java.lang.String.class, java.lang.String.class, java.lang.String.class,
                        java.lang.String.class, java.lang.String.class, java.lang.String.class
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
                RecentTransactionTable.getTableHeader().setReorderingAllowed(false);
                jScrollPane4.setViewportView(RecentTransactionTable);

                BookandbyCategory.setPreferredSize(new java.awt.Dimension(0, 120));

                javax.swing.GroupLayout BookandbyCategoryLayout = new javax.swing.GroupLayout(BookandbyCategory);
                BookandbyCategory.setLayout(BookandbyCategoryLayout);
                BookandbyCategoryLayout.setHorizontalGroup(
                    BookandbyCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                );
                BookandbyCategoryLayout.setVerticalGroup(
                    BookandbyCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 220, Short.MAX_VALUE)
                );

                TotalBooks.setBackground(new java.awt.Color(204, 204, 204));

                javax.swing.GroupLayout TotalBooksLayout = new javax.swing.GroupLayout(TotalBooks);
                TotalBooks.setLayout(TotalBooksLayout);
                TotalBooksLayout.setHorizontalGroup(
                    TotalBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 270, Short.MAX_VALUE)
                );
                TotalBooksLayout.setVerticalGroup(
                    TotalBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                );

                TotalsMembers.setBackground(new java.awt.Color(204, 204, 204));

                javax.swing.GroupLayout TotalsMembersLayout = new javax.swing.GroupLayout(TotalsMembers);
                TotalsMembers.setLayout(TotalsMembersLayout);
                TotalsMembersLayout.setHorizontalGroup(
                    TotalsMembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 270, Short.MAX_VALUE)
                );
                TotalsMembersLayout.setVerticalGroup(
                    TotalsMembersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                );

                TotalTransactions.setBackground(new java.awt.Color(204, 204, 204));

                javax.swing.GroupLayout TotalTransactionsLayout = new javax.swing.GroupLayout(TotalTransactions);
                TotalTransactions.setLayout(TotalTransactionsLayout);
                TotalTransactionsLayout.setHorizontalGroup(
                    TotalTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 270, Short.MAX_VALUE)
                );
                TotalTransactionsLayout.setVerticalGroup(
                    TotalTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 130, Short.MAX_VALUE)
                );

                OverdueBooks.setBackground(new java.awt.Color(204, 204, 204));

                javax.swing.GroupLayout OverdueBooksLayout = new javax.swing.GroupLayout(OverdueBooks);
                OverdueBooks.setLayout(OverdueBooksLayout);
                OverdueBooksLayout.setHorizontalGroup(
                    OverdueBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 270, Short.MAX_VALUE)
                );
                OverdueBooksLayout.setVerticalGroup(
                    OverdueBooksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 130, Short.MAX_VALUE)
                );

                jLabel11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jLabel11.setText("Reports & Analytics");

                jLabel12.setText("Manage borrowing and returning of books");

                jLabel14.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
                jLabel14.setText("Recent Transactions");

                javax.swing.GroupLayout ReportsPanelLayout = new javax.swing.GroupLayout(ReportsPanel);
                ReportsPanel.setLayout(ReportsPanelLayout);
                ReportsPanelLayout.setHorizontalGroup(
                    ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportsPanelLayout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ReportsPanelLayout.createSequentialGroup()
                                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(ReportsPanelLayout.createSequentialGroup()
                                .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ReportsPanelLayout.createSequentialGroup()
                                        .addComponent(TotalBooks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TotalsMembers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TotalTransactions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(OverdueBooks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(BookandbyCategory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1116, Short.MAX_VALUE))
                                .addGap(2, 135, Short.MAX_VALUE))
                            .addGroup(ReportsPanelLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(0, 0, Short.MAX_VALUE))))
                );
                ReportsPanelLayout.setVerticalGroup(
                    ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportsPanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ReportsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(TotalTransactions, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TotalsMembers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TotalBooks, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(OverdueBooks, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BookandbyCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(183, Short.MAX_VALUE))
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
        TabbedPane.setSelectedComponent(ReportsPanel); // show Reports tab
        setupReportsPanel();
    }//GEN-LAST:event_btnReportsActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        Main createFrame = new Main();
        createFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void MemberSearchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MemberSearchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MemberSearchBarActionPerformed

    private void BookSearchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BookSearchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BookSearchBarActionPerformed

    private void TransactionSearchBar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TransactionSearchBar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TransactionSearchBar1ActionPerformed

    private void btnTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTransactionActionPerformed

    private void btnAddBooksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBooksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddBooksActionPerformed

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
    private javax.swing.JPanel BookandbyCategory;
    private javax.swing.JPanel BooksPanel;
    private javax.swing.JTable BooksTable;
    private javax.swing.JPanel ButtonPanels;
    private javax.swing.JPanel MemberPanel;
    private javax.swing.JTextField MemberSearchBar;
    private javax.swing.JTable MemberTable;
    private javax.swing.JPanel OverdueBooks;
    private javax.swing.JTable RecentTransactionTable;
    private javax.swing.JPanel ReportsPanel;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JPanel TotalBooks;
    private javax.swing.JPanel TotalTransactions;
    private javax.swing.JPanel TotalsMembers;
    private javax.swing.JPanel TransactionPanel;
    private javax.swing.JTextField TransactionSearchBar1;
    private javax.swing.JTable TransactionTable1;
    private javax.swing.JButton btnAddBooks;
    private javax.swing.JButton btnAddMember;
    private javax.swing.JButton btnBooks;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMembers;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnTransaction;
    private javax.swing.JButton btnTransactions;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
