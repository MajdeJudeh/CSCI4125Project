import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class KnowledgeSkills{
  private Connection connection;
  private Scanner input = new Scanner(System.in);



  public KnowledgeSkills(Connection conn){
    connection = conn;
  }//end of constructor with connection as parameter

  public void insertRow(){
    PreparedStatement pStmt = null;
    String insertStatement = "INSERT INTO knowledge_skills(ks_code, skill_title, " +
                              "description, skill_level) values (?, ?, ?, ?)";
    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the skill code.");
      pStmt.setInt(1, input.nextInt());

      System.out.println("Enter the skill title.");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the skill description.");
      pStmt.setString(3, input.nextLine());

      System.out.println("Enter the skill level.");
      pStmt.setInt(4, input.nextInt());

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
    String deleteStatement = "DELETE FROM knowledge_skills WHERE ks_code = ?";
    try{
      pStmt = connection.prepareStatement(deleteStatement);

      System.out.println("Enter the skill code you wish to delete.");
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
}//end of KnowledgeSkills
