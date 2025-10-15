package data;

import model.Transaction;
import java.io.*;
import java.util.*;

public class TransactionDataManager {
    private static final String FILE_PATH = "data/transactions.csv";

    static {
        new File("data").mkdirs();
    }

    // Load Transactions
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length >= 8) {
                    try {
                        Transaction transaction = new Transaction(
                            parts[0].trim(),  // bookTitle
                            parts[1].trim(),  // bookIsbn
                            parts[2].trim(),  // memberName
                            parts[3].trim(),  // memberId
                            parts[4].trim(),  // issueDate
                            parts[5].trim(),  // dueDate
                            parts[6].trim(),  // returnDate
                            parts[7].trim()   // status
                        );
                        transactions.add(transaction);
                    } catch (Exception e) {
                        System.err.println("Error parsing transaction data: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Transactions file not found. Starting with empty list.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return transactions;
    }

    // Save Transactions
    private static void saveTransactions(List<Transaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Transaction t : transactions) {
                writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                    t.getBookTitle(),
                    t.getBookIsbn(),
                    t.getMemberName(),
                    t.getMemberId(),
                    t.getIssueDate(),
                    t.getDueDate(),
                    t.getReturnDate(),
                    t.getStatus()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add Transaction
    public static boolean addTransaction(Transaction transaction) {
        List<Transaction> transactions = loadTransactions();
        transactions.add(transaction);
        saveTransactions(transactions);
        return true;
    }

    // Update Transaction (for returns)
    public static boolean updateTransaction(String bookIsbn, String memberId, String returnDate) {
        List<Transaction> transactions = loadTransactions();
        
        for (Transaction t : transactions) {
            if (t.getBookIsbn().equals(bookIsbn) && 
                t.getMemberId().equals(memberId) && 
                t.getStatus().equals("Active")) {
                t.setReturnDate(returnDate);
                t.setStatus("Returned");
                saveTransactions(transactions);
                return true;
            }
        }
        
        return false;
    }

    // Get Active Transactions Count
    public static int getActiveTransactionsCount() {
        List<Transaction> transactions = loadTransactions();
        int count = 0;
        for (Transaction t : transactions) {
            if (t.getStatus().equals("Active")) {
                count++;
            }
        }
        return count;
    }
}