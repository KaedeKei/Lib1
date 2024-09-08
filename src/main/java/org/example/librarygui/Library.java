package org.example.librarygui;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Library {
    public Library() {
        DatabaseHelper.createNewDatabase();
        DatabaseHelper.createBooksTable();
    }

    public boolean addBook(Book book) {
        String sql = "insert into books(title, author, genre, isbn) values(?, ?, ?, ?)";
        try(Connection conn = DatabaseHelper.connect(); PreparedStatement pstmnt = conn.prepareStatement(sql)) {
            pstmnt.setString(1, book.getTitle());
            pstmnt.setString(2, book.getAuthor());
            pstmnt.setString(2, book.getGenre());
            pstmnt.setString(3, book.getIsbn());

            pstmnt.executeUpdate();
            return true;
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String sql = "update books set title=?, author=?, genre=? where isbn=?";
        try(Connection conn = DatabaseHelper.connect(); PreparedStatement pstmnt = conn.prepareStatement(sql)) {
            pstmnt.setString(1, book.getTitle());
            pstmnt.setString(2, book.getAuthor());
            pstmnt.setString(2, book.getGenre());
            pstmnt.setString(3, book.getIsbn());
            //возвращает количество затронутых строк
            pstmnt.executeUpdate();
            return true;
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean removeBook(String isbn) {
        String sql = "delete from books where isbn=?";
        try(Connection conn = DatabaseHelper.connect(); PreparedStatement pstmnt = conn.prepareStatement(sql)) {
            pstmnt.setString(1, isbn);
            //возвращает количество затронутых строк
            pstmnt.executeUpdate();
            return true;
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    //для запросов без параметров используется объект Statement
    public ArrayList<Book> listBooks() {
        ArrayList<Book> books = new ArrayList<Book>();
        String sql = "select * from books";
        try(Connection conn = DatabaseHelper.connect(); Statement stmnt = conn.createStatement(); ResultSet res = stmnt.executeQuery(sql)) {
            while(res.next()) {
                Book book = new Book(res.getString("title"), res.getString("author"), res.getString("genre"), res.getString("isbn"));
                book.setId(res.getInt("id"));
                System.out.println(book);
                books.add(book);
            }
            return books;
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public Book findBookByIsbn(String isbn) {
        String sql = "select * from books where isbn=?";
        try(Connection conn = DatabaseHelper.connect(); PreparedStatement pstmnt = conn.prepareStatement(sql)) {
            pstmnt.setString(1, isbn);
            ResultSet res = pstmnt.executeQuery();

            if(res.next()) {
                Book book = new Book(res.getString("title"), res.getString("author"), res.getString("genre"), res.getString("isbn"));
                book.setId(res.getInt("id"));
                return book;
            }
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void findBooksByAuthor(String author) {
        String sql = "select * from books where author=?";
        try(Connection conn = DatabaseHelper.connect(); PreparedStatement pstmnt = conn.prepareStatement(sql)) {
            pstmnt.setString(1, author);
            ResultSet res = pstmnt.executeQuery();

            if(res.next()) {
                Book book = new Book(res.getString("title"), res.getString("author"), res.getString("genre"), res.getString("isbn"));
                book.setId(res.getInt("id"));
                System.out.println(book);
            }
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
