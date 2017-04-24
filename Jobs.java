import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class Jobs{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public Jobs(Connection conn){
    connection = conn;
  }//end of jobs constructor

  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO jobs(job_code, emp_mode, pay_rate, pay_type, comp_id" +
                              "href, job_title, city, state_abbr, dateStr) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the job code");
      pStmt.setInt(1, input.nextInt());

      System.out.println("Enter the type of employment full-time/part-time/internship/etc");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the course level");
      pStmt.setInt(3, input.nextInt());

      System.out.println("Enter the course description.");
      pStmt.setString(4, input.nextLine());

      System.out.println("Enter the credit value of the course.");
      pStmt.setInt(5, input.nextInt());

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
}//end of Jobs class
