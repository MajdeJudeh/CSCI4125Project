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
    System.out.println("Select 3: to find all jobs you qualify for.");
    System.out.println("Select 0: to exit.");
    option = input.nextInt();
    displayJobs(option);
  }while (option != 0);



  }

  public void displayJobs(int userOption){

    try{
      if (userOption == 1){
        showJobCategories();
        listJobs();
      }
      else if(userOption ==2){
        showQualifiedCategories();
        listJobs();
      }
      else if(userOption == 3){
        displayQualifiedJobs();
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

  public void listJobs() throws SQLException{
    System.out.println("Select a job category code to search for jobs in.");
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

  public void showQualifiedCategories() throws SQLException{


    String listQualifiedCats = "SELECT SOC AS job_category, category_title " +
    "FROM job_category JCs " +
    "WHERE NOT EXISTS(SELECT ks_code " +
        "FROM core_skill " +
        "WHERE soc = JCs.soc " +
        "MINUS " +
        "SELECT ks_code " +
        "FROM spec_rel " +
        "WHERE per_id = ?)";

    pStmt = connection.prepareStatement(listQualifiedCats);
    System.out.println("Enter your ID number");
    pStmt.setInt(1, input.nextInt());
    rs = pStmt.executeQuery();

    System.out.println("Here are the job categories you qualify for.\n\n");
    System.out.println("Category ID \tCategory Title\n");
    while(rs.next()){
      String jobCategory = rs.getString("job_category");
      String categoryTitle = rs.getString("category_title");
      System.out.println(jobCategory + "\t\t" + categoryTitle);
    }

  }

  public void displayQualifiedJobs() throws SQLException{
    String listJobs = "SELECT job_code, job_title, emp_mode, pay_rate, pay_type, company_name, city, state_abbr, href " +
                      "FROM jobs Jo NATURAL JOIN company " +
                      "WHERE NOT EXISTS(" +
                      "SELECT ks_code " +
                      "FROM req_skill Rs " +
                      "WHERE Rs.job_code = Jo.job_code " +
                      "MINUS " +
                      "SELECT ks_code " +
                      "FROM spec_rel " +
                      "WHERE per_id = ?)";

    pStmt = connection.prepareStatement(listJobs);
    System.out.println("Enter your person ID");
    pStmt.setInt(1, input.nextInt());
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
  }//end of displayQualifiedJobs
}//end of JobSearch class
