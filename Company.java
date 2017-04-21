import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class Company{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public Company(){}//end of default constructor

  public Company(Connection conn){
    connection = conn;
  }//end of
  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO company(comp_id, website, company_name) values (?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the company's ID number");
      pStmt.setInt(1, input.nextInt());

      System.out.println("Enter the company website");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the company name");
      pStmt.setInt(3, input.nextInt());

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
  }
  public void deleteRow(){
    PreparedStatement pStmt = null;
    String deleteStatement = "DELETE FROM company WHERE comp_id = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the company's ID number you wish to delete");
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
  }
}//end of Query class
