import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class NAICS{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public NAICS(Connection conn){
    connection = conn;
  }//end of NAICS constructor

  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO naics(code, sector, subsector, industry_group, " +
                              "naics_industry, national_industry) values (?, ?, ?, ?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the NAICS code");
      pStmt.setInt(1, input.nextInt());

      System.out.println("Enter the sector number");
      pStmt.setInt(2, input.nextInt());

      System.out.println("Enter the subsector number");
      pStmt.setInt(3, input.nextInt());

      System.out.println("Enter the industry group number");
      pStmt.setInt(4, input.nextInt());

      System.out.println("Enter the NAICS industry number");
      pStmt.setInt(5, input.nextInt());

      System.out.println("Enter the national industry number");
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
    String deleteStatement = "DELETE FROM naics WHERE code = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the NAICS code you wish to delete");
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
}//end of naics class
