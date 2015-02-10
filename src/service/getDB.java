package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class getDB {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/scrumapsio";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getDB(){
        Connection dbConnection = null;

        try {

            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

        try {

            dbConnection = DriverManager.getConnection(
                    DB_CONNECTION, DB_USER, DB_PASSWORD);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;
    }
}
