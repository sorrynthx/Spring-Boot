package com.example.demo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Student;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();
	
    // list
	/*
	 * public List<Student> list() {
	 * 
	 * // 학생 수 5명 목록 만들기 for (long i = 1; i <= 5; i++) { Student student = new
	 * Student(); student.setId(i); student.setName("Student " + i);
	 * student.setAge(20 + (int) i); students.add(student); }
	 * 
	 * return students; }
	 */
	
	// list from DB
	public List<Student> list() {
		
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;

	    try {
	        // Connect to the database
	        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "#testdb123!");

	        // Create a statement
	        stmt = conn.createStatement();

	        // Execute a query to retrieve students
	        String sql = "SELECT id, name, age FROM students";
	        rs = stmt.executeQuery(sql);

	        // Process the result set and create Student objects
	        while (rs.next()) {
	            Student student = new Student();
	            student.setId(rs.getLong("id"));
	            student.setName(rs.getString("name"));
	            student.setAge(rs.getInt("age"));
	            students.add(student);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Close resources
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return students;
	}
	
	// save
	public void add(Student student) {
		
		// DB에 저장하는 로직 (현재는 출력만 진행)
		System.out.println("==============START=================");
		System.out.println("Student Info: " + student.getId());
		System.out.println("Student Info: " + student.getName());
		System.out.println("Student Info: " + student.getAge());
		System.out.println("============== END =================");
		
		// JDBC 이용하여 DB 저장
		Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // Connect to the database (서버정보, 유저, 비밀번호)
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "#testdb123!");
            
            // Prepare an SQL statement
            String sql = "INSERT INTO students (id, name, age) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getAge());
            
            // Execute the statement
            pstmt.executeUpdate();
            
            System.out.println("Student saved to database: " + student.getName());
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
	
}
