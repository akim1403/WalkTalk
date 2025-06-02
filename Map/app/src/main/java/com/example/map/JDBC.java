package com.example.map;

import java.io.IOException;
import java.sql.*;

public class JDBC {
    public void display() throws IOException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/walkTalk";
        String username = "root";
        String password = "12345678";
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt;
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("Select * from data");
        while (rs.next()) {
            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
        }
        stmt.close();
        conn.close();
    }
}
