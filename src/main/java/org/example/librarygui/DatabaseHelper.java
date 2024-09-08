package org.example.librarygui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:library.db";

    //получение объекта подключения к БД
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase() {
        //в круглых скобках указан ресурс который должен автоматически завершить выполнение после блока try-catch(аналог finally)
        try(Connection conn = connect()) {
            if(conn != null) {
                System.out.println("База данных успешно создана");
            }
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void createBooksTable() {
        String sql = "create table if not exists books(" +
                "id integer primary key," +
                "title text not null," +
                "author text not null," +
                "isbn text not null unique," +
                "isbn text not null" +
                ")";
        try(Connection conn = connect(); PreparedStatement pstmnt = conn.prepareStatement(sql)) {
            pstmnt.execute();
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
