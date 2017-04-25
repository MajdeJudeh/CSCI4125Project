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
    String insertStatement = "INSERT INTO jobs(job_code, emp_mode, pay_rate, pay_type, comp_id, " +
                              "href, job_title, city, state_abbr, dateStr) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the job code");
      pStmt.setInt(1, input.nextInt());

      System.out.println("Enter the type of employment full-time/part-time/internship/etc");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the pay rate for the job.");
      pStmt.setInt(3, input.nextInt());

      System.out.println("Enter the pay type for the job.");
      pStmt.setString(4, input.nextLine());

      System.out.println("Enter the company id of the company offering the job.");
      pStmt.setInt(5, input.nextInt());

      System.out.println("Enter the website link for the job.");
      pStmt.setString(5, input.nextLine());

      System.out.println("Enter the job title of the job.");
      pStmt.setString(6, input.nextLine());

      System.out.println("Enter the city for the job.");
      pStmt.setString(7, input.nextLine());

      System.out.println("Enter the state abbreviation for the job");
      pStmt.setString(8, input.nextLine());

      System.out.println("Enter the day of the month the job was created in \"dd\" format");
      int day = input.nextInt();
      System.out.println("Enter the month the job was created in \"mm\" format.");
      int month = input.nextInt();
      System.out.println("Enter the year the job was created in \"yyyy\" format.");
      int year = input.nextInt();

      pStmt.setDate(10, new java.sql.Date(new java.util.Date().getTime()));
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
    String deleteStatement = "DELETE FROM jobs WHERE job_code = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the job's ID number you wish to delete");
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
