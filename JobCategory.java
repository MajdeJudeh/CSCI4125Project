import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class JobCategory{
  private Connection connection;
  private Scanner input = new Scanner(System.in);

  public JobCategory(Conenction conn){
    connection = conn;
  }//end of constructor

  public void insertRow(){
    PreparedStatment pStmt = null;
    String insertStatement = "INSERT INTO job_category(soc, category_title, description, " +
    "pay_range_high, pay_range_low, parent_cate) values (?, ?, ?, ?, ?, ?)";

    try{
      pStmt = connection.prepareStatement(insertStatement);

      System.out.println("Enter the SOC code.");
      pStmt.setInt(1, input.nextInt());

      System.out.println("Enter the job category title");
      pStmt.setString(2, input.nextLine());

      System.out.println("Enter the description");
      pStmt.setString(3, input.nextLine());
      System.out.println("Enter the high pay range");
      System.out.println("Enter the low pay range");
      System.out.println("Enter the parent category");
    }catch(SQLException e){
      e.getMessage();
    }
  }//end of insertRow
}//end of JobCategory
