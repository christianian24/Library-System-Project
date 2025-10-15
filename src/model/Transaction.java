//author @ian

package model;

public class Transaction {
    private String bookTitle;
    private String bookIsbn;
    private String memberName;
    private String memberId;
    private String issueDate;
    private String dueDate;
    private String returnDate;
    private String status;

    // Constructor for new transaction (no return date yet)
    public Transaction(String bookTitle, String bookIsbn, String memberName, 
                      String memberId, String issueDate, String dueDate, String status) {
        this.bookTitle = bookTitle;
        this.bookIsbn = bookIsbn;
        this.memberName = memberName;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = "-";
        this.status = status;
    }

    // Constructor with return date
    public Transaction(String bookTitle, String bookIsbn, String memberName, 
                      String memberId, String issueDate, String dueDate, 
                      String returnDate, String status) {
        this.bookTitle = bookTitle;
        this.bookIsbn = bookIsbn;
        this.memberName = memberName;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    
    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    public String getIssueDate() { return issueDate; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
    
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}