import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class Section{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public Section(Connection conn){
    connection = conn;
  }//end of
  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO section(sec_code, c_code, sec_no, semester, sec_year complete_date, " +
                              "offered_by, sec_format, status, price) values (?, ?, ?, ?, ?, DATE ?, ?, ?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the section code for this class.");
      pStmt.setInt(1, input.nextInt());

      System.out.println("Enter the course code for this class");
      pStmt.setInt(2, input.nextInt());

      System.out.println("Enter the section number for this class");
      pStmt.setInt(3, input.nextInt());

      System.out.println("Enter the semester the section was offered in the following format: FAL, SPR, SUM");
      pStmt.setString(4, input.nextLine());

      System.out.println("Enter the year the section was offered in.");
      pStmt.setInt(5, input.nextInt());

      System.out.println("Enter the day the section was completed in \"YYYY-MM-DD\" format");
      pStmt.setString(6, input.nextLine());

      System.out.println("Enter the instructor's name.");
      pStmt.setString(7, input.nextLine());

      System.out.println("Enter the section format. (lecture/lab/etc...)");
      pStmt.setString(8, input.nextLine());

      System.out.println("Enter the status of the section. (FULL/OPEN)");
      pStmt.setString(9, input.nextLine());

      System.out.println("Enter the price of the section.");
      pStmt.setInt(10, input.nextInt());

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
    String deleteStatement = "DELETE FROM section WHERE sec_code = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the section code you wish to delete");
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
