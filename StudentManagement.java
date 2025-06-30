package com.maahi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class StudentManagement {
    static final String DB_URL = "jdbc:mysql://localhost:3306/StudentDB";
    static final String USER = "root";
    static final String PASS = "Mahesh@2003";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n---- Student Database Management (JDBC) ----");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student by ID");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> displayAll();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> searchStudentById();
                case 6 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 6);
    }

    public static void addStudent() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.print("Enter student name: ");
            String name = sc.nextLine();
            System.out.print("Enter age: ");
            int age = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter branch: ");
            String branch = sc.nextLine();

            String sql = "INSERT INTO students (name, age, branch) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, branch);
            ps.executeUpdate();
            System.out.println(" Student added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayAll() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d, Branch: %s\n",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getInt("age"), rs.getString("branch"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStudent() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.print("Enter student ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new name: ");
            String name = sc.nextLine();
            System.out.print("Enter new age: ");
            int age = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter new branch: ");
            String branch = sc.nextLine();

            String sql = "UPDATE students SET name=?, age=?, branch=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, branch);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println(" Student record updated.");
            else System.out.println(" Student ID not found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.print("Enter student ID to delete: ");
            int id = sc.nextInt();
            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println(" Student deleted.");
            else System.out.println(" Student ID not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchStudentById() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.print("Enter student ID to search: ");
            int id = sc.nextInt();
            String sql = "SELECT * FROM students WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d, Branch: %s\n",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getInt("age"), rs.getString("branch"));
            } else {
                System.out.println(" Student not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
