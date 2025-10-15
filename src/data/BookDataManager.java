//author @ian

package data;

import model.Book;
import java.io.*;
import java.util.*;

public class BookDataManager {
    private static final String FILE_PATH = "data/books.csv";

    static {
        // Create data directory if it doesn't exist
        new File("data").mkdirs();
    }

    // ─────────────── Load Books ───────────────
    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length >= 7) {
                    try {
                        Book book = new Book(
                            parts[0].trim(),           // title
                            parts[1].trim(),           // author
                            parts[2].trim(),           // isbn
                            parts[3].trim(),           // category
                            Integer.parseInt(parts[4].trim()),  // totalCopies
                            Integer.parseInt(parts[5].trim()),  // availableCopies
                            parts[6].trim()            // status
                        );
                        books.add(book);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing book data: " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Books file not found. Starting with empty list.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    // ─────────────── Save Books ───────────────
    private static void saveBooks(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Book book : books) {
                writer.println(String.format("%s,%s,%s,%s,%d,%d,%s",
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getCategory(),
                    book.getTotalCopies(),
                    book.getAvailableCopies(),
                    book.getStatus()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ─────────────── Add Book ───────────────
    public static boolean addBook(Book book) {
        List<Book> books = loadBooks();
        
        // Check for duplicate ISBN
        for (Book b : books) {
            if (b.getIsbn().equals(book.getIsbn())) {
                return false;
            }
        }
        
        books.add(book);
        saveBooks(books);
        return true;
    }

    // ─────────────── Update Book ───────────────
    public static boolean updateBook(Book updatedBook) {
        List<Book> books = loadBooks();
        
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(updatedBook.getIsbn())) {
                books.set(i, updatedBook);
                saveBooks(books);
                return true;
            }
        }
        
        return false;
    }

    // ─────────────── Delete Book ───────────────
    public static boolean deleteBook(String title) {
        List<Book> books = loadBooks();
        
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(title)) {
                books.remove(i);
                saveBooks(books);
                return true;
            }
        }
        
        return false;
    }

    // ─────────────── Find Book by Title ───────────────
    public static Book findBookByTitle(String title) {
        List<Book> books = loadBooks();
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    // ─────────────── Get Total Books ───────────────
    public static int getTotalBooks() {
        return loadBooks().size();
    }
}