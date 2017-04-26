import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class JobCategory{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public JobCategory(Connection conn){
    connection = conn;
  }//end of constructor

  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO job_category(soc, category_title, description, " +
    "pay_range_high, pay_range_low, parent_cate) values (?, ?, ?, ?, ?, ?)";

    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the SOC code.");
      pStmt.setInt(1, input.nextInt());
      input.nextLine();// Discard '\n'


      System.out.println("Enter the job category title");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the description");
      pStmt.setString(3, input.nextLine());

      System.out.println("Enter the high pay range");
      pStmt.setInt(4, input.nextInt());

      System.out.println("Enter the low pay range");
      pStmt.setInt(5, input.nextInt());

      System.out.println("Enter the parent category");
      pStmt.setInt(6, input.nextInt());

      pStmt.executeUpdate();
    }catch(SQLException e){
      e.getMessage();
    }
  }//end of insertRow

  public void deleteRow(){
    PreparedStatement pStmt = null;
    String deleteStatement = "DELETE FROM job_category WHERE soc = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the SOC code you wish to delete");
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
}//end of JobCategory
