import java.sql.*;
import java.math.*;
import oracle.jdbc.*;
import java.util.*;

public class Query{
  private Connection connection;
  private Scanner input = new Scanner(System.in);
  public Query(){

  }
  public Query(Connection conn){
    connection = conn;
  }
  public void runQuery(int userOption){
      PreparedStatement pStmt = null;
      ResultSet rs = null;
      String statstr = null;
      try {
        if (userOption == 1){
          System.out.println("List a company's workers by names.");
          statstr = "SELECT First_name, Last_name " +
          "FROM (jobs Natural Join experience Ex)INNER JOIN person Ps on Ex.per_id = Ps.per_id " +
          "WHERE Ex.end_date is null";
          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while (rs.next()){
            String firstname = rs.getString("FIRST_NAME");
            String lastname = rs.getString("LAST_NAME");
            System.out.println(firstname + "\t" + lastname);
          }//end of while
        }//end of query 1
        else if (userOption == 2){
            System.out.println("List a company's staff by salary in descending order.");
            statstr = "SELECT first_name, last_name, salary " +
            "FROM (jobs NATURAL JOIN experience Ex) INNER JOIN person Ps on Ex.per_id = Ps.per_id " +
            "WHERE comp_id = ? and end_date is null " +
            "ORDER BY salary DESC";
            pStmt = connection.prepareStatement(statstr);
            System.out.println("Enter the desired company number.");
            pStmt.setInt(1, input.nextInt());
            rs = pStmt.executeQuery();

            while (rs.next()){
              String firstname = rs.getString("FIRST_NAME");
              String lastname = rs.getString("LAST_NAME");
              int salary = rs.getInt("SALARY");
              System.out.println(firstname + "\t" + lastname + "\t" + salary);
            }//end of while
        }//end of query 2
        else if (userOption == 3){
          System.out.println("List companies' labor cost (total salaries and wage rates by 1920 hours) in descending order.");
          statstr = "SELECT distinct comp_id, company_name, SUM (salary) AS CompanySalary " +
          "FROM company NATURAL JOIN jobs NATURAL JOIN experience " +
          "WHERE end_date is NULL " +
          "GROUP BY comp_id, company_name " +
          "ORDER BY CompanySalary DESC";

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while (rs.next()){
            int compid = rs.getInt("COMP_ID");
            String compName = rs.getString("COMPANY_NAME");
            int salary = rs.getInt("COMPANYSALARY");
            System.out.println(compid + "\t" + compName + "\t" + salary);
          }//end of while
        }//end of query 3
        else if (userOption == 4){
          System.out.println(" Find all the jobs a person is currently holding and worked in the past.");
          statstr = "SELECT job_code, start_date, end_date, salary, emp_mode, pay_rate, pay_type, company_name, city, state_abbr " +
          "FROM experience Natural Join jobs natural join company " +
          "WHERE per_id = ?";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            int jobCode = rs.getInt("job_code");
            java.util.Date sDate = rs.getDate("start_date");
            java.util.Date eDate = rs.getDate("end_date");
            int salary = rs.getInt("salary");
            String empmode = rs.getString("emp_mode");
            int payRate = rs.getInt("pay_rate");
            String payType = rs.getString("pay_type");
            String compName = rs.getString("company_name");
            String city = rs.getString("city");
            String state = rs.getString("state_abbr");

            System.out.println(jobCode + "\t" + sDate + "\t" + eDate + "\t" + salary + "\t" + empmode + "\t" +
                                payRate + "\t" + payType + "\t" + compName + "\t" + city + "\t" + state);
          }//end of while
        }//end of query 4
        else if (userOption == 5){
          System.out.println("List a person's knowledge/skills in a readable format. ");
          statstr = "SELECT first_name, last_name, skill_title as skill " +
          "FROM knowledge_skills NATURAL JOIN spec_rel NATURAL JOIN person " +
          "WHERE per_id = ?";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String skill = rs.getString("skill");

            System.out.println(firstName + "\t" + lastName + "\t" + skill);
          }//end of while
        }//end of query 5
        else if (userOption == 6){
          System.out.println("6. List the skill gap of a worker between his/her jobs(s) and his/her skills.");
          statstr = "select first_name, last_name, job_title, skill_title as skill_lacked " +
          "from (SELECT distinct per_id, ks_code " +
                "FROM  req_skill natural join experience " +
                "MINUS " +
                "SELECT distinct per_id, ks_code " +
                "FROM spec_rel natural join experience) natural join person natural join experience Ex inner join jobs Jo on Ex.job_code = Jo.job_code  natural join knowledge_skills " +
          "WHERE per_id = ?";
          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String jobTitle = rs.getString("job_title");
            String skillLacked = rs.getString("skill_lacked");
            System.out.println(firstName + "\t" + lastName + "\t" + jobTitle + "\t" + skillLacked);
          }//end of while
        }//end of query 6
        else if (userOption == 7){
          System.out.println("List the required knowledge/skills of a job/ a job category in a readable format. (two queries)");

          System.out.println("First one");
          statstr = "SELECT job_title, skill_title as requireed_skill " +
          "FROM  jobs  NATURAL JOIN req_skill NATURAL JOIN knowledge_skills " +
          "ORDER BY job_code ASC";

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){
            String jobTitle = rs.getString("job_title");
            String requiredSkill = rs.getString("requireed_skill");
            System.out.println(jobTitle + "\t" + requiredSkill);
          }//end of while

          System.out.println("Second one");

          statstr = "SELECT Jc.category_title as job_category, skill_title as required_skill " +
          "FROM (job_category Jc NATURAL JOIN core_skill Cs) INNER JOIN knowledge_skills Kn ON Cs.ks_code = Kn.ks_code " +
          "ORDER BY soc ASC";

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){
            String jobCategory = rs.getString("job_category");
            String reqSkill = rs.getString("required_skill");
            System.out.println(jobCategory + "\t" + reqSkill);
          }//end of while
        }//end of Query 7
        else if (userOption == 8){
          System.out.println("8");
          statstr = "SELECT first_name, last_name, skill_title as skill_lacked, Job_title, company_name " +
          "FROM (SELECT per_id, first_name, last_name, job_code, skill_title, job_title, company_name " +
                "FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
                "INNER JOIN job_category Jc ON Jc.soc = Jr.soc) " +
                "MINUS " +
                "SELECT per_id, first_name, last_name, job_code, skill_title, job_title, company_name " +
                "FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
                "INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) " +
          "WHERE per_id = ? and job_code = ?";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          System.out.println("Enter job code");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String skillLacked = rs.getString("skill_lacked");
            String jobTitle = rs.getString("job_title");
            String compName = rs.getString("company_name");

            System.out.println(firstName + "\t" + lastName + "\t" + skillLacked + "\t" + jobTitle + "\t" + compName);
          }//end of while
        }//end of Query 8
        else if (userOption == 9){
          System.out.println("9");
          statstr = "select Fn as c_code, Bn as course_title " +
          "from (select c_code as Fn, course_title as Bn from course) " +
          "where not exists( select * " +
              "from (SELECT ks_code as Ks " +
                    "FROM (SELECT per_id, ks_code, job_code " +
                          "FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
                          "INNER JOIN job_category Jc ON Jc.soc = Jr.soc) " +
                          "MINUS " +
                          "SELECT per_id, ks_code, job_code " +
                          "FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
                          "INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) " +
                    "WHERE per_id = ? and job_code = ?) " +
              "where not exists( select * " +
                "from course_skills C2 " +
                "where C2.c_code = Fn and C2.ks_code = Ks))";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          System.out.println("Enter job code");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
          String cCode = rs.getString("c_code");
          String courseTitle = rs.getString("course_title");
          System.out.println(cCode + "\t" + courseTitle);
        }//end of while
      }//end of Query 9
        else if (userOption == 10){
          System.out.println("10");

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){

          }
        }
        else if (userOption == 11){
          System.out.println("11");

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){

          }
        }
        else if (userOption == 12){
          System.out.println("12");

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){

          }
        }
        else if (userOption == 13){
          System.out.println("13");
        }
        else if (userOption == 14){
          System.out.println("14");
        }
        else if (userOption == 15){
          System.out.println("15");

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){

          }
        }
        else if (userOption == 16){
          System.out.println("16");

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){

          }
        }
        else if (userOption == 17){
          System.out.println("17");

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){

          }
        }
        else if (userOption == 18){
          System.out.println("18");

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){

          }
        }
        else if (userOption == 19){
          System.out.println("19");
        }
        else if (userOption == 20){
          System.out.println("20");
        }
      //end of try
    } catch (SQLException e){
      System.out.println(e.getMessage());
    } finally {
      try{

        if (rs != null) rs.close();
        if (pStmt != null) pStmt.close();

      }catch (SQLException e){
        System.out.println(e.getMessage());
      }
    }

  }

}
