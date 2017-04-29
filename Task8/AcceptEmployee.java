import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class AcceptEmployee{
  private Connection connection;
  private Scanner input = new Scanner(System.in);
  private PreparedStatement pStmt = null;
  ResultSet rs = null;
  public AcceptEmployee(Connection conn){
    connection = conn;
  }//end of acceptEmployee constructor

  public void selectCompany(){
    System.out.println("This program is used to hire an employee. Please select input your company id from the following.\n\n");

    try{

      displayCompanies(); //displays all of the companies for the user.
      System.out.println("Please input your company id from above");

      int companyID = input.nextInt();
      input.nextLine();

      displayJobs(companyID);

      System.out.println("\nSelect the job you wish to hire an employee for.");
      int jobID = input.nextInt();
      hirePerson(jobID);

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

  public void displayCompanies() throws SQLException{
    String listCompanies = "SELECT comp_id, company_name FROM company";
    pStmt = connection.prepareStatement(listCompanies);
    rs = pStmt.executeQuery();

    System.out.println("Comp ID \tCompany Name\n");
    while(rs.next()){
      String compID = rs.getString("comp_id");
      String companyName = rs.getString("company_name");

      System.out.println(compID + "\t\t" + companyName);
    }
  }

  public void displayJobs(int companyID) throws SQLException{
    System.out.println("Here are the current jobs your company has posted.\n\n");

    String listJobs = "SELECT job_code, job_title FROM Jobs WHERE comp_id = ?";
    pStmt = connection.prepareStatement(listJobs);
    pStmt.setInt(1, companyID);
    rs = pStmt.executeQuery();

    System.out.println("Job Code \tJob Title\n");
    while(rs.next()){
      String jobCode = rs.getString("job_code");
      String jobTitle = rs.getString("job_title");

      System.out.println(jobCode + "\t\t" + jobTitle);

    }
  }

  public void hirePerson(int jobCode) throws SQLException{
    System.out.println("Here is a list of all the people in the database.\n\n");

    String listPeople = "SELECT per_id, first_name, last_name FROM Person";
    pStmt = connection.prepareStatement(listPeople);
    rs = pStmt.executeQuery();

    System.out.println("ID \tFirst Name \tLast Name\n");
    while(rs.next()){
      String per_id = rs.getString("per_id");
      String firstName = rs.getString("first_name");
      String lastName = rs.getString("last_name");
      System.out.println(per_id + "\t" + firstName + "\t\t" + lastName);
    }
    System.out.println("\nSelect the person's person ID that you wish to hire.");
    int perID = input.nextInt();
    System.out.println("Enter the salary you wish to pay the person.");
    int salary = input.nextInt();

    String hirePerson = "INSERT INTO experience(per_id,job_code,start_date,end_date,salary) values(?, ?, ?, ?, ?)";

    pStmt = connection.prepareStatement(hirePerson);
    pStmt.setInt(1, perID);
    pStmt.setInt(2, jobCode);
    pStmt.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
    pStmt.setNull(4, java.sql.Types.DATE);
    pStmt.setInt(5, salary);
    pStmt.executeUpdate();

    System.out.println("The employee with perID = " + perID +" has successfully been inputed into the database.");
  }
}//end of Query class
