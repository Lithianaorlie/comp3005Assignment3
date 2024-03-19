package com.comp3005;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    
    // JDBC & Database credentials
    static final String url = "jdbc:postgresql://localhost:5432/Assignment 3";
    static final String user = "postgres";
    static final String password = "12345";
    static Connection conn;
    //The Constructor
    public Main() {
        try {
            
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            //Creating a new instance of main
            new Main();
            Scanner scanner = new Scanner(System.in);
            int choice = -1;
            //Menu choices
            //User needs to choose which operation they want to carry out and enter an input
            //Or just exit but pressing 0
            while (choice != 0) {

                System.out.println("Choose an operation:");
                System.out.println("1. Display All Students");
                System.out.println("2. Add Student");
                System.out.println("3. Update Student Email");
                System.out.println("4. Delete Student");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                //After choosing an operation, User enters input
                switch (choice) {
                    // Case 1 for the getAllStudents() function
                    case 1:
                        getAllStudents();
                        break;
                    // Case 2 for the addStudent(first_name, last_name, email, enrollment_date) function
                    case 2:
                        System.out.print("Enter first name: ");
                        String firstName = scanner.next();
                        System.out.print("Enter last name: ");
                        String lastName = scanner.next();
                        System.out.print("Enter email: ");
                        String email = scanner.next();
                        System.out.print("Enter enrollment date (YYYY-MM-DD): ");
                        String enrollmentDate = scanner.next();
                        addStudent(firstName, lastName, email, enrollmentDate);
                        break;
                    // Case 3 for the updateStudentEmail (student_id, new_email) function
                    case 3:
                        System.out.print("Enter student ID to update email: ");
                        int studentIdToUpdate = scanner.nextInt();
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.next();
                        updateStudentEmail(studentIdToUpdate, newEmail);
                        break;
                    // Case 4 for the deleteStudent(student_id) function
                    case 4:
                        System.out.print("Enter student ID to delete: ");
                        int studentIdToDelete = scanner.nextInt();
                        deleteStudent(studentIdToDelete);
                        break;
                    //Case 0 for exiting program
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Function implementation of getAllStudents()

    public static void getAllStudents() {
        try {
            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT * FROM students");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.println("Student ID: " + resultSet.getInt("student_id") + ", First name: " + resultSet.getString("first_name") + ", Last name: " + resultSet.getString("last_name") + ", Email: " + resultSet.getString("email") + ", Enrollment Date: " + resultSet.getString("enrollment_date"));
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Function implementation of addStudent(first_name, last_name, email, enrollment_date)
    public static void addStudent(String first_name, String last_name,String email, String enrollment_date){
        try {
            // Prepare SQL INSERT statement
            String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            // Set parameter values
            statement.setString(1, first_name);
            statement.setString(2, last_name);
            statement.setString(3, email);
            Date date = Date.valueOf(enrollment_date);
            statement.setDate(4,date);
            //getAllStudents();
            // Execute the INSERT statement
            statement.executeUpdate();
            // Close the statement
            statement.close();
            System.out.println("Student added successfully!");
            getAllStudents();
        } catch (SQLException e) {
            System.out.println("Error occurred while adding student: " + e.getMessage());
        }

    }
    //Function implementation of updateStudentEmail(student_id, new_email)

    public static void updateStudentEmail(Integer student_id, String new_email){
        try{
            String sql = "UPDATE students SET email = ? WHERE student_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, new_email);
            statement.setInt(2, student_id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated>0){
                System.out.println("The student's email has been successfully updated.");
                getAllStudents();
            } else {
                System.out.println("Email update failed! ");
            }
            statement.close();

        } catch (SQLException e){
            System.out.println("Error occurred while updating student email: " + e.getMessage());

        }


    }
    //Function implementation of deleteStudent(student_id)

    public static void deleteStudent(Integer student_id){
        try {
            // Prepare SQL DELETE statement
            String sql = "DELETE FROM students WHERE student_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            // Set parameter value
            statement.setInt(1, student_id);
            // Execute the DELETE statement
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student with ID " + student_id + " deleted successfully.");
                getAllStudents();
            } else {
                System.out.println("No student found with ID " + student_id);
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting student: " + e.getMessage());
        }
    
    }
}
