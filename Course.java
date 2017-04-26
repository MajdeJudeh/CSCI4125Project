import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class Course{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public Course(){}//end of default constructor

  public Course(Connection conn){
    connection = conn;
  }//end of
  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO course(c_code, course_title, course_level, " +
                              "description, credits, active) values (?, ?, ?, ?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the course's ID number");
      pStmt.setInt(1, input.nextInt());
      input.nextLine();// Discard '\n'

      System.out.println("Enter the course title");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the course level");
      pStmt.setInt(3, input.nextInt());
      input.nextLine();// Discard '\n'

      System.out.println("Enter the course description.");
      pStmt.setString(4, input.nextLine());

      System.out.println("Enter the credit value of the course.");
      pStmt.setInt(5, input.nextInt());

      System.out.println("Enter the status of the course. 1=active \t 0=inactive.");
      pStmt.setInt(6, input.nextInt());

      pStmt.executeUpdate();
    } catch (SQLException e){
      System.out.println(e.getMessage());
    } finally {
      try{
        if(pStmt != null) pStmt.close();
      }catch (SQLException e){
        System.out.println(e.getMessage());
      }
    }
  }//end of insertRow
  public void deleteRow(){
    PreparedStatement pStmt = null;
    String deleteStatement = "DELETE FROM course WHERE c_code = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the course's ID number you wish to delete");
      pStmt.setInt(1, input.nextInt());

      pStmt.executeUpdate();
    } catch (SQLException e){
      System.out.println(e.getMessage());
    } finally {
      try{
        if(pStmt != null) pStmt.close();
      }catch (SQLException e){
        System.out.println(e.getMessage());
      }
    }
  }//ebd of deleteRow
}//end of Query class
