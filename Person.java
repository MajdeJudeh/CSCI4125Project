import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class Person{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public Person(Connection conn){
    connection = conn;
  }//end of person constructor
  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO person(per_id, first_name, last_name, street_name, street_number, apt_number, " +
                              "city, state_abbr, zip_code, email, gender) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the person's ID number");
      pStmt.setInt(1, input.nextInt());
      input.nextLine();// Discard '\n'

      System.out.println("Enter the person's first name");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the person's last name");
      pStmt.setString(3, input.nextLine());

      System.out.println("Enter the person's street name");
      pStmt.setString(4, input.nextLine());

      System.out.println("Enter the person's street number.");
      pStmt.setInt(5, input.nextInt());
      input.nextLine();// Discard '\n'

      System.out.println("Enter the person's apt number. Hit enter if not applicable");
      try {
        String aptNumber = input.nextLine();
        System.out.println(aptNumber);
        if(	!(aptNumber.isEmpty()) ){
          pStmt.setInt(6, Integer.parseInt(aptNumber));
        }
        else
          pStmt.setNull(6, java.sql.Types.INTEGER);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }

      System.out.println("Enter the person's city");
      pStmt.setString(7, input.nextLine());

      System.out.println("Enter the person's state abbreviation");
      pStmt.setString(8, input.nextLine());

      System.out.println("Enter the person's zip code.");
      pStmt.setInt(9, input.nextInt());
      input.nextLine();// Discard '\n'

      System.out.println("Enter the person's email address.");
      pStmt.setString(10, input.nextLine());

      System.out.println("Enter the person's gender.");
      pStmt.setString(11, input.nextLine());

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
    String deleteStatement = "DELETE FROM person WHERE per_id = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the person's ID number you wish to delete");
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
