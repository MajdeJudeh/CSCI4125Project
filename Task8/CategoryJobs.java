import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class CategoryJobs{
  private Connection connection;
  private Scanner input = new Scanner(System.in);
  private PreparedStatement pStmt = null;
  ResultSet rs = null;
  public CategoryJobs(Connection conn){
    connection = conn;
  }//end of acceptEmployee constructor

  public void jobCategories(){
    System.out.println("This program is used to find all jobs in a job category. Please select a category ID from below.\n\n");

    try{

      listCategories(); //displays all of the companies for the user.

      listJobs();

    } catch (SQLException e){
      System.out.println(e.getMessage());
    } finally {
      try{
        if(pStmt != null) pStmt.close();
      }catch (SQLException e){
        System.out.println(e.getMessage());
      }
    }
  }//end of jobCategories

  public void listCategories() throws SQLException{
    String statStr = "SELECT soc, category_title FROM job_category";
    pStmt = connection.prepareStatement(statStr);
    rs = pStmt.executeQuery();

    System.out.println("Category ID \tCategory Title\n");
    while(rs.next()){
      String soc = rs.getString("SOC");
      String catTitle= rs.getString("category_title");
      System.out.println(soc + "\t\t" + catTitle);
    }
  }

  public void listJobs() throws SQLException{
    System.out.println("Select a job category code from above to list  jobs.");
    int option = input.nextInt();

    String listJobs = "SELECT job_code, job_title, emp_mode, pay_rate, pay_type, company_name, city, state_abbr, href " +
                      "FROM jobs NATURAL JOIN company NATURAL JOIN Jc_rel " +
                      "WHERE SOC = ?";

    pStmt = connection.prepareStatement(listJobs);
    pStmt.setInt(1, option);
    rs = pStmt.executeQuery();

    System.out.printf("%-10s %-20s %-10s %-10s %-10s %-20s %-15s %-8s %-50s%n%n", "Job Code",
                        "Job Title", "Job Type", "Pay Rate", "Pay Type", "Company", "City", "State", "Website");
    while(rs.next()){
      String jobCode = rs.getString("job_code");
      String jobTitle = rs.getString("job_title");
      String empMode = rs.getString("emp_mode");
      String payRate = rs.getString("pay_rate");
      String payType = rs.getString("pay_type");
      String company = rs.getString("company_name");
      String city = rs.getString("city");
      String state = rs.getString("state_abbr");
      String href = rs.getString("href");

      System.out.printf("%-10.10s %-20.20s %-10.10s %-10.10s %-10.10s %-20.20s %-15.15s %-8.8s %-50.50s%n", jobCode , jobTitle , empMode , payRate , payType , company, city, state, href);
    }
  }

}//end of PeopleSearch class
