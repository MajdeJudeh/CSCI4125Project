import java.sql.*;
import java.math.*;
import oracle.jdbc.*;
import java.util.*;

public class Query{
  private Connection connection;
  private Scanner input = new Scanner(System.in);
  public Query(){}
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
          "WHERE Ex.end_date IS NULL AND comp_id = ?";
          pStmt = connection.prepareStatement(statstr);

          System.out.println("Enter a company id.");
          pStmt.setInt(1, input.nextInt());

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
            "FROM (jobs NATURAL JOIN experience Ex) INNER JOIN person Ps ON Ex.per_id = Ps.per_id " +
            "WHERE comp_id = ? AND end_date IS NULL AND pay_type = 'Salary' " +
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
          statstr = "WITH Salary_tmp " +
          "AS(SELECT comp_id, salary " +
              "FROM jobs NATURAL JOIN experience " +
              "WHERE end_date IS NULL AND pay_type = 'Salary' " +
              "UNION " +
              "SELECT comp_id, salary * 1920 " +
              "FROM jobs NATURAL JOIN experience " +
              "WHERE end_date IS NULL AND pay_type = 'Hourly') " +
          "SELECT comp_id,  SUM(salary) AS Labor_Cost " +
          "FROM Salary_tmp " +
          "GROUP BY comp_id " +
          "ORDER BY Labor_Cost DESC";

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while (rs.next()){
            int compid = rs.getInt("COMP_ID");
            String laborCost = rs.getString("Labor_Cost");
            System.out.println(compid + "\t" + laborCost);
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
          statstr = "SELECT per_id, skill_title AS skill " +
          "FROM knowledge_skills NATURAL JOIN spec_rel " +
          "WHERE per_id = ?";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String perID= rs.getString("per_id");
            String skill = rs.getString("skill");

            System.out.println(perID + "\t" + skill);
          }//end of while
        }//end of query 5
        else if (userOption == 6){
          System.out.println("6. List the skill gap of a worker between his/her jobs(s) and his/her skills.");
          statstr = "SELECT DISTINCT ks_code " +
            "FROM  req_skill NATURAL JOIN experience " +
            "WHERE per_id = ? AND end_date IS NULL  " +
            "MINUS " +
            "SELECT ks_code " +
            "FROM spec_rel " +
            "WHERE per_id = ?";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          int perId = input.nextInt();
          pStmt.setInt(1, perId);
          pStmt.setInt(2, perId);
          rs = pStmt.executeQuery();

          while(rs.next()){
            String ksCode = rs.getString("ks_code");
            System.out.println(ksCode);
          }//end of while
        }//end of query 6
        else if (userOption == 7){
          System.out.println("List the required knowledge/skills of a job/ a job category in a readable format. (two queries)");

          System.out.println("First one");
          statstr = "SELECT skill_title AS required_skill, ks_code " +
          "FROM  req_skill NATURAL JOIN knowledge_skills " +
          "WHERE job_code = ?";

          pStmt = connection.prepareStatement(statstr);

          System.out.println("Enter the job code");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String requiredSkill = rs.getString("required_skill");
            String ksCode = rs.getString("ks_code");
            System.out.println(requiredSkill + "\t" + ksCode);
          }//end of while

          System.out.println("Second one");

          statstr = "SELECT skill_title AS required_skill, ks_code " +
          "FROM core_skill NATURAL JOIN knowledge_skills " +
          "WHERE soc = ?";

          pStmt = connection.prepareStatement(statstr);

          System.out.println("Enter the SOC code");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String requiredSkill = rs.getString("required_skill");
            String ksCode = rs.getString("ks_code");
            System.out.println(requiredSkill + "\t" + ksCode);
          }//end of while
        }//end of Query 7
        else if (userOption == 8){
          System.out.println("8");
          statstr = "SELECT skill_title AS skill_lacked, ks_code " +
          "FROM(SELECT ks_code " +
                "FROM req_skill " +
                "WHERE job_code = ? " +
                "MINUS " +
                "SELECT ks_code " +
                "FROM spec_rel " +
                "WHERE per_id = ?) NATURAL JOIN knowledge_skills";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          pStmt.setInt(1, input.nextInt());
          System.out.println("Enter the person's ID number");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String skillLacked = rs.getString("skill_lacked");
            String ksCode = rs.getString("ks_code");
            System.out.println(skillLacked + "\t" + ksCode);
          }//end of while
        }//end of Query 8
        else if (userOption == 9){
          System.out.println("9");
          statstr = "SELECT c_code, course_title " +
          "FROM course Crs " +
          "WHERE NOT EXISTS(SELECT ks_code " +
              "FROM req_skill Rs " +
              "WHERE job_code = ? AND NOT EXISTS( " +
                  "SELECT ks_code " +
                  "FROM course_skills Csk " +
                  "WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code) " +
              "MINUS " +
              "SELECT ks_code " +
              "FROM spec_rel " +
              "WHERE per_id = ?)";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          pStmt.setInt(1, input.nextInt());
          System.out.println("Enter the person's ID number");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
          String cCode = rs.getString("c_code");
          String courseTitle = rs.getString("course_title");
          System.out.println(cCode + "\t" + courseTitle);
        }//end of while
      }//end of Query 9
        else if (userOption == 10){
          System.out.println("Suppose the skill gap of a worker and the requirement of a desired job can be covered by one course. Find the " +
          "\"quickest\" solution for this worker. Show the course, section information and the completion date");
          statstr = "WITH course_list (c_code, Min_cmplt) AS " +
                        "(SELECT c_code, MIN(complete_date) FROM  section GROUP BY c_code) " +
                    "SELECT Crs.c_code, sec_no, complete_date " +
                    "FROM course_list Crs INNER JOIN section Sec ON Crs.c_code = Sec.c_code " +
                    "WHERE complete_date = Min_cmplt AND NOT EXISTS(SELECT ks_code " +
                        "FROM req_skill Rs " +
                        "WHERE job_code = ? AND NOT EXISTS( " +
                            "SELECT ks_code " +
                            "FROM course_skills Csk " +
                            "WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code) " +
                        "MINUS " +
                        "SELECT ks_code " +
                        "FROM spec_rel " +
                        "WHERE per_id = ?)";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          pStmt.setInt(1, input.nextInt());
          System.out.println("Enter the person's ID number");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String cCode = rs.getString("c_code");
            String section = rs.getString("sec_no");
            java.util.Date completeDate = rs.getDate("complete_date");
            System.out.println(cCode + "\t" + section + "\t" + courseTitle);
          }
        }

        else if (userOption == 11){
          System.out.println("Find the cheapest course to make up one’s skill gap by showing the course to take and the cost (of the section price).");
          statstr = "select distinct Fn as c_code, Bn as course_title, Sec.price as section_price " +
          "from (select c_code as Fn, course_title as Bn from course) inner join section Sec on Fn = Sec.c_code " +
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
            int secPrice = rs.getInt("section_price");
            System.out.println(cCode + "\t" + courseTitle + "\t" + secPrice);
          }
        }

        else if (userOption == 12){
          System.out.println("12 If query #9 returns nothing, then find the course sets that their combination covers all the missing knowledge/ \n" +
           "skills for a person to pursue a specific job. The considered course sets will not include more than three courses. \n" +
           "If multiple course sets are found, list the course sets (with their course IDs) in the order of the ascending order of \n" +
           "the course sets’ total costs.");
          statstr = "select c_code, course_title " +
          "from course natural join course_skills Cs inner join (SELECT ks_code Ks, per_id " +
              "FROM (SELECT per_id, ks_code, job_code " +
              "FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
              "INNER JOIN job_category Jc ON Jc.soc = Jr.soc) " +
              "MINUS " +
              "SELECT per_id, ks_code, job_code " +
              "FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
              "INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) " +
              "WHERE per_id = ? and job_code = ?) on Ks = Cs.ks_code " +
          "where not exists(select Fn as c_code, Bn as course_title " +
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
                        "where C2.c_code = Fn and C2.ks_code = Ks))) and exists(select per_id " +
              "from course natural join course_skills Cs inner join (SELECT ks_code Ks, per_id " +
                  "FROM (SELECT per_id, ks_code, job_code " +
                  "FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
                  "INNER JOIN job_category Jc ON Jc.soc = Jr.soc) " +
                  "MINUS " +
                  "SELECT per_id, ks_code, job_code " +
                  "FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr) " +
                  "INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) " +
                  "WHERE per_id = ? and job_code = ?) on Ks = Cs.ks_code " +
              "where not exists(select Fn as c_code, Bn as course_title " +
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
                            "where C2.c_code = Fn and C2.ks_code = Ks))) " +
              "group by per_id " +
              "having count(c_code) <= 3)";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          int perID = input.nextInt();
          pStmt.setInt(1, perID);
          pStmt.setInt(3, perID);
          pStmt.setInt(5, perID);
          pStmt.setInt(7, perID);
          System.out.println("Enter job code");
          int jobCode = input.nextInt();
          pStmt.setInt(2, jobCode);
          pStmt.setInt(4, jobCode);
          pStmt.setInt(6, jobCode);
          pStmt.setInt(8, jobCode);
          rs = pStmt.executeQuery();

          while(rs.next()){
            String cCode = rs.getString("c_code");
            String courseTitle = rs.getString("course_title");
            System.out.println(cCode + "\t" + courseTitle);
          }
        }
        else if (userOption == 13){
          System.out.println("Skipped for now");
        }
        else if (userOption == 14){
          System.out.println("Skipped for now");
        }
        else if (userOption == 15){
          System.out.println("List all the names along with the emails of the persons who are qualified for a job.  ");
          statstr = "select first_name, last_name, email " +
          "from person Prsn " +
          "where not exists( select * " +
              "from (SELECT ks_code as Ks " +
                    "FROM (SELECT distinct per_id, ks_code, job_code " +
                          "FROM person, req_skill Ks " +
                          "MINUS " +
                          "SELECT distinct per_id, ks_code, job_code " +
                          "FROM (person NATURAL JOIN spec_rel Sr), jobs) " +
                    "WHERE job_code = ?) " +
              "where not exists( select * " +
                "from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id " +
                "where P2.per_id = Prsn.per_id and Sr.ks_code = Ks))";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            System.out.println(firstName + "\t" + lastName + "\t" + email);
          }
        }
        else if (userOption == 16){
          System.out.println("When a company cannot find any qualified person for a job, a secondary solution is to find a person who is almost \n" +
          "qualified to the job. Make a \"missing-one\" list that lists people who miss only one skill for a specified job.");

          statstr = "select first_name, last_name, email " +
          "from person Prsn " +
          "where exists (select Ps, count(Ks) " +
              "from (SELECT distinct per_id Ps, ks_code as Ks " +
                    "FROM (SELECT distinct per_id, ks_code, job_code " +
                          "FROM person, req_skill  Ks " +
                          "MINUS " +
                          "SELECT distinct per_id, ks_code, job_code " +
                          "FROM (person NATURAL JOIN spec_rel Sr), jobs) " +
                    "WHERE job_code = ?) " +
              "where not exists( select * " +
                  "from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id " +
                  "where P2.per_id = Prsn.per_id and Sr.ks_code = Ks) " +
              "group by Ps " +
              "having count(Ks) = 1)";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            System.out.println(firstName + "\t" + lastName + "\t" + email);
          }
        }
        else if (userOption == 17){
          System.out.println("List the skillID and the number of people in the missing-one list for a given job code in the ascending order of the people counts.");
          statstr = "select count(Pid)as PidC, ks_code " +
          "from ((select distinct Pid, ks_code " +
              "from req_skill, (select distinct Prsn.per_id as Pid " +
                "from person Prsn " +
                "where exists (select Ps, count(Ks) " +
                    "from (SELECT distinct per_id Ps, ks_code as Ks " +
                          "FROM (SELECT distinct per_id, ks_code, job_code " +
                                "FROM person, req_skill  Ks " +
                                "MINUS " +
                                "SELECT distinct per_id, ks_code, job_code " +
                                "FROM (person NATURAL JOIN spec_rel Sr), jobs) " +
                          "WHERE job_code = ?) " +
                    "where not exists( select * " +
                        "from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id " +
                        "where P2.per_id = Prsn.per_id and Sr.ks_code = Ks) " +
                    "group by Ps " +
                    "having count(Ks) = 1)) " +
                  "where job_code = ?) " +
              "minus " +
              "(select distinct Pid, ks_code " +
              "from(select distinct Prsn.per_id as Pid " +
                "from person Prsn " +
                "where exists (select Ps, count(Ks) " +
                    "from (SELECT distinct per_id Ps, ks_code as Ks " +
                          "FROM (SELECT distinct per_id, ks_code, job_code " +
                                "FROM person, req_skill  Ks " +
                                "MINUS " +
                                "SELECT distinct per_id, ks_code, job_code " +
                                "FROM (person NATURAL JOIN spec_rel Sr), jobs) " +
                          "WHERE job_code = ?) " +
                    "where not exists( select * " +
                        "from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id " +
                        "where P2.per_id = Prsn.per_id and Sr.ks_code = Ks) " +
                    "group by Ps " +
                    "having count(Ks) = 1)) inner join spec_rel on Pid = spec_rel.per_id)) " +
          "group by ks_code " +
          "order by PidC asc";


          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter a job code.");
          int jobCode = input.nextInt();
          pStmt.setInt(1, jobCode);
          pStmt.setInt(2, jobCode);
          pStmt.setInt(3, jobCode);
          rs = pStmt.executeQuery();

          while(rs.next()){
            int pIDCount = rs.getInt("PidC");
            int ksCode = rs.getInt("ks_code");
            System.out.println(pIDCount + "\t" + ksCode);
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
