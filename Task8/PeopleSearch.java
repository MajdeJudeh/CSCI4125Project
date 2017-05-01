import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class PeopleSearch{
  private Connection connection;
  private Scanner input = new Scanner(System.in);
  private PreparedStatement pStmt = null;
  ResultSet rs = null;
  public PeopleSearch(Connection conn){
    connection = conn;
  }//end of acceptEmployee constructor

  public void selectPerson(){
    System.out.println("This program is used to find potential employees. Please select input your company id from the following.\n\n");

    try{

      displayCompanies(); //displays all of the companies for the user.
      System.out.println("Please input your company id from above");

      int companyID = input.nextInt();
      input.nextLine();

      displayJobs(companyID);

      System.out.println("Enter a job code from above.");
      int jobID = input.nextInt();
      listPersons(jobID);

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

  public void listPersons(int jobCode) throws SQLException{
    System.out.println("Here is a list of all the people in the database who are missing 3 skills or less.\n\n");

    listPersons();

    System.out.println("\nSelect the person's person ID that you wish to train.");
    int perID = input.nextInt();

    courseSet(perID, jobCode);

  }

  public void listPersons() throws SQLException{
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

  }

  public void courseSet(int perID, int jobCode) throws SQLException{

    String statstr = "WITH missing_skills " +
          "AS (SELECT ks_code " +
              "FROM req_skill " +
              "WHERE job_code = ? " +
              "MINUS " +
              "SELECT ks_code " +
              "FROM spec_rel " +
              "WHERE per_id = ?), " +
        "single_course_list " +
          "AS( SELECT c_code " +
              "FROM course Crs " +
              "WHERE EXISTS( SELECT ks_code " +
                          "FROM missing_skills) " +
              "AND EXISTS( SELECT ks_code " +
                          "FROM course_skills NATURAL JOIN req_skill " +
                          "WHERE c_code = Crs.c_code AND job_code = ? " +
                          "MINUS " +
                          "SELECT ks_code " +
                          "FROM missing_skills)), " +
        "course_sets_2 (id, c_code_1, c_code_2,c_code_3,price) " +
          "AS( SELECT ROWNUM, c1.c_code, c2.c_code, null,s1.price + s2.price " +
              "FROM course_skills c1 INNER JOIN section s1 ON c1.c_code = s1.c_code, " +
              "course_skills c2 INNER JOIN section s2 ON c2.c_code = s2.c_code " +
              "WHERE c1.c_code != c2.c_code AND NOT EXISTS(SELECT c_code " +
                          "FROM single_course_list) " +
              "AND NOT EXISTS( SELECT ks_code " +
                          "FROM missing_skills " +
                          "MINUS " +
                          "SELECT ks_code " +
                          "FROM course_skills " +
                          "WHERE c_code = c1.c_code OR c_code = c2.c_code)), " +
        "course_sets_3 (id, c_code_1, c_code_2, c_code_3, price) " +
          "AS( SELECT ROWNUM, c1.c_code, c2.c_code, c3.c_code, s1.price+s2.price+s3.price " +
              "FROM course_skills c1 INNER JOIN section s1 ON c1.c_code = s1.c_code, " +
              "course_skills c2  INNER JOIN section s2 ON c2.c_code = s2.c_code, " +
              "course_skills c3  INNER JOIN section s3 ON c3.c_code = s3.c_code " +
              "WHERE c1.c_code != c2.c_code AND c1.c_code != c3.c_code " +
              "AND c3.c_code != c2.c_code AND NOT EXISTS(SELECT c_code " +
                          "FROM single_course_list) " +
              "AND NOT EXISTS(SELECT c_code_1 " +
                          "FROM course_sets_2) " +
              "AND NOT EXISTS( SELECT ks_code " +
                          "FROM missing_skills " +
                          "MINUS " +
                          "SELECT ks_code " +
                          "FROM course_skills " +
                          "WHERE c_code = c1.c_code OR c_code = c2.c_code " +
                          "OR c_code = c3.c_code))," +
        "combined_list " +
          "AS(SELECT c_code_1, c_code_2, c_code_3,price " +
              "FROM course_sets_3 Cs3 " +
              "WHERE id  = (SELECT MIN(id) " +
                          "FROM course_sets_3 " +
                          "WHERE (Cs3.c_code_1 = c_code_2 OR Cs3.c_code_1 = c_code_3 " +
                          "OR Cs3.c_code_2 = c_code_3 OR Cs3.c_code_2 = c_code_1 " +
                          "OR Cs3.c_code_3 = c_code_2 OR Cs3.c_code_3 = c_code_1) " +
                          "OR (Cs3.c_code_1 = c_code_1 AND Cs3.c_code_2 = c_code_2 AND Cs3.c_code_3 = c_code_3) " +
                          "AND Cs3.price = price) " +
              "UNION " +
              "SELECT c_code_1, c_code_2, c_code_3,price " +
              "FROM course_sets_2 Cs2 " +
              "WHERE id  = (SELECT MIN(id) " +
                          "FROM course_sets_2 " +
                          "WHERE (Cs2.c_code_1 = c_code_2 OR Cs2.c_code_2 = c_code_1) " +
                          "OR (Cs2.c_code_1 = c_code_1 AND Cs2.c_code_2 = c_code_2) " +
                          "AND Cs2.price = price)) " +
        "SELECT c_code_1,c_code_2,c_code_3, price " +
        "FROM combined_list " +
        "ORDER BY price ASC";


    pStmt = connection.prepareStatement(statstr);
    pStmt.setInt(2, perID);
    pStmt.setInt(1, jobCode);
    pStmt.setInt(3, jobCode);
    rs = pStmt.executeQuery();
    System.out.println("Here is a list of courses you can use to train this person by price.");
    System.out.println("Course1 \tCourse2 \tCourse 3 \tPrice");
    while(rs.next()){
      String cCode1 = rs.getString("c_code_1");
      String cCode2 = rs.getString("c_code_2");
      String cCode3 = rs.getString("c_code_3");
      int price = rs.getInt("price");

      System.out.println(cCode1 + "\t\t" + cCode2 + "\t\t" + cCode3 + "\t\t" + price);
    }
  }
}//end of PeopleSearch class
