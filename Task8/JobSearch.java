import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class JobSearch{
  private Connection connection;
  private Scanner input = new Scanner(System.in);
  private PreparedStatement pStmt = null;
  ResultSet rs = null;
  public JobSearch(Connection conn){
    connection = conn;
  }//end of acceptEmployee constructor

  public void searchOptions(){
    int option = 0;
    do {
    System.out.println("\nThis program is used to search for jobs.\n\n");
    System.out.println("Select 1: to search by job category.");
    System.out.println("Select 2: to search by jobs categories you qualify for.");
    System.out.println("Select 0: to exit.");
    option = input.nextInt();
    displayJobs(option);
  }while (option != 0);



  }

  public void displayJobs(int userOption){

    try{
      if (userOption == 1){
        showJobCategories();
        selectJobCategory();
      }
      else if(userOption ==2){
        showQualifiedCategories();
      }

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

  public void showJobCategories() throws SQLException{
    System.out.println("Here are all the job categories. Select a category\n\n");
    String jobCategories = "SELECT SOC, category_title FROM job_category";
    pStmt = connection.prepareStatement(jobCategories);
    rs = pStmt.executeQuery();

    System.out.println("Category ID \tCategory Title\n");
    while(rs.next()){
      String soc = rs.getString("SOC");
      String catTitle= rs.getString("category_title");
      System.out.println(soc + "\t\t" + catTitle);
    }
  }

  public void selectJobCategory() throws SQLException{
    System.out.println("Select a job category code to search for jobs in.");
    int option = input.nextInt();

    String listJobs = "SELECT job_code, job_title, emp_mode, pay_rate, pay_type, company_name, city, state_abbr, href " +
                      "FROM jobs NATURAL JOIN company NATURAL JOIN Jc_rel " +
                      "WHERE SOC = ?";

    pStmt = connection.prepareStatement(listJobs);
    pStmt.setInt(1, option);
    rs = pStmt.executeQuery();

    System.out.println("Job Code \tJob Title \t\tJob Type \tPay Rate \tPay Type \tCompany \t\tCity \t\tState \tWebsite");
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

      System.out.println(jobCode + "\t\t" + jobTitle + "\t" + empMode + "\t" + payRate + "\t\t" + payType + "\t\t" + company +
                          "\t\t" + city + "\t" + state + "\t" + href);
    }
  }

  public void showQualifiedCategories() throws SQLException{
    System.out.println("Here are the current jobs your company has posted.\n\n");

    String listJobs = "SELECT job_code, job_title FROM Jobs WHERE comp_id = ?";
    pStmt = connection.prepareStatement(listJobs);
    pStmt.setInt(1, 1);
    rs = pStmt.executeQuery();

    System.out.println("Job Code \tJob Title\n");
    while(rs.next()){
      String jobCode = rs.getString("job_code");
      String jobTitle = rs.getString("job_title");

      System.out.println(jobCode + "\t\t" + jobTitle);

    }
  }
}//end of Query class
