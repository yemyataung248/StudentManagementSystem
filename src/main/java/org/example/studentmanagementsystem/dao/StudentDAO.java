package org.example.studentmanagementsystem.dao;


import org.example.studentmanagementsystem.database.DatabaseConnection;
import org.example.studentmanagementsystem.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StudentDAO {

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (name,email,age,major) VALUES(?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getMajor());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Student> getAllStudents(){
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC";

        try(Connection connection = DatabaseConnection.getConnection();
        Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)
    ){
    while (resultSet.next()){
        Student student = new Student(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getInt("age"),
                resultSet.getString("major")
        );
        students.add(student);
    }
}catch(SQLException e){
    e.printStackTrace();
        }
        System.out.println(students.size());
        return students;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, age = ?, major = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getMajor());
            pstmt.setInt(5, student.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public Student getStudentById(int id){
            String sql = "SELECT * FROM students WHERE id = ?";

            try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql);
            ){
               pstmt.setInt(1,id);
               ResultSet resultSet = pstmt.executeQuery();
               if(resultSet.next()){
                    return new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getInt("age"),
                            resultSet.getString("major")
                    );
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        public List<Student> searchStudents(String keyword){
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ? OR email LIKE ? OR major LIKE ? OR ORDER BY id DESC";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
        ){
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1,searchPattern);
            pstmt.setString(2,searchPattern);
            pstmt.setString(3,searchPattern);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()){
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getInt("age"),
                        resultSet.getString("major")
                );
                students.add(student);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return students;
        }
}
