package com.example.map;

import java.io.IOException;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        JDBC jdbc = new JDBC();
        jdbc.display();
    }
}
