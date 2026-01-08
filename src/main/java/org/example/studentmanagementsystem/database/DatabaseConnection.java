package org.example.studentmanagementsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class DatabaseConnection {
    private static final String URL ="jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "student_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Paingnyi634752!";

    private static Connection connection = null;

    public static Connection getConnection(){
        try{
            if(connection == null || connection.isClosed()){
                Connection tempConnection = DriverManager.getConnection(URL,USER,PASSWORD);
                Statement statement = tempConnection.createStatement();
                statement.execute("CREATE DATABASE IF NOT EXISTS "+ DB_NAME);
                statement.close();
                tempConnection.close();

                connection = DriverManager.getConnection(URL+DB_NAME,USER,PASSWORD);
                createTables();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    private static void createTables(){
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS students(
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                age INT,
                major VARCHAR(100),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(createTableSQL);
            System.out.println("Database and tables created successfully!");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void closeConnection(){
        try{
            if(connection !=null && !connection.isClosed()){
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
