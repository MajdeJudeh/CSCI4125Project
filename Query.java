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
          System.out.println("First Name\tLast Name");
          while (rs.next()){
            String firstname = rs.getString("FIRST_NAME");
            String lastname = rs.getString("LAST_NAME");
            System.out.println(firstname + "\t\t" + lastname);
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

            System.out.println("First Name\tLast Name\tSalary");
            while (rs.next()){
              String firstname = rs.getString("FIRST_NAME");
              String lastname = rs.getString("LAST_NAME");
              int salary = rs.getInt("SALARY");
              System.out.println(firstname + "\t\t" + lastname + "\t\t" + salary);
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
          System.out.println("Company ID\tLabor Cost");
          while (rs.next()){
            int compid = rs.getInt("COMP_ID");
            String laborCost = rs.getString("Labor_Cost");
            System.out.println(compid + "\t\t" + laborCost);
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

          System.out.println("ID \tSkill");
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

          System.out.println("Skill");
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

          System.out.println("Skill \tSkill Code");
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

          System.out.println("Skill \tSkill Code");
          while(rs.next()){
            String requiredSkill = rs.getString("required_skill");
            String ksCode = rs.getString("ks_code");
            System.out.println(requiredSkill + "\t" + ksCode);
          }//end of while
        }//end of Query 7
        else if (userOption == 8){
          System.out.println("List a person’s missing knowledge/skills for a specific job in a readable format.");
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

          System.out.println("Skill Lacked \tSkill Code");
          while(rs.next()){
            String skillLacked = rs.getString("skill_lacked");
            String ksCode = rs.getString("ks_code");
            System.out.println(skillLacked + "\t\t" + ksCode);
          }//end of while
        }//end of Query 8
        else if (userOption == 9){
          System.out.println("9");
          statstr = "SELECT c_code, course_title " +
          "FROM course Crs " +
          "WHERE EXISTS(SELECT ks_code " +
              "FROM req_skill " +
              "WHERE job_code = ? " +
              "MINUS " +
              "SELECT ks_code " +
              "FROM spec_rel " +
              "WHERE per_id = ?) AND " +
          "NOT EXISTS(SELECT ks_code " +
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
          int jobCode = input.nextInt();

          pStmt.setInt(1, jobCode);
          pStmt.setInt(3, jobCode);
          System.out.println("Enter the person's ID number");
          int person = input.nextInt();
          pStmt.setInt(2, person);
          pStmt.setInt(4, person);
          rs = pStmt.executeQuery();

          System.out.println("Code \t Title");
          while(rs.next()){
          String cCode = rs.getString("c_code");
          String courseTitle = rs.getString("course_title");
          System.out.println(cCode + "\t" + courseTitle);
        }//end of while
      }//end of Query 9
        else if (userOption == 10){
          System.out.println("Suppose the skill gap of a worker and the requirement of a desired job can be covered by one course. Find the " +
          "\"quickest\" solution for this worker. Show the course, section information and the completion date");
          statstr = "WITH course_list (c_code, complete_date) AS " +
                        "(SELECT c_code, MIN(complete_date) " +
                        "FROM  section Crs " +
                        "WHERE NOT EXISTS(SELECT ks_code " +
                            "FROM req_skill Rs " +
                            "WHERE job_code = ? AND NOT EXISTS( " +
                                "SELECT ks_code " +
                                "FROM course_skills Csk " +
                                "WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code) " +
                            "MINUS " +
                            "SELECT ks_code " +
                            "FROM spec_rel " +
                            "WHERE per_id = ?) " +
                        "GROUP BY c_code), " +
                    "min_complete (min_cmplt)AS " +
                        "(SELECT MIN(complete_date) " +
                        "FROM course_list) " +
                    "SELECT distinct Crs.c_code, sec_no, Crs.complete_date " +
                        "FROM course_list Crs INNER JOIN section Sec ON Crs.c_code = Sec.c_code, min_complete " +
                        "WHERE Crs.complete_date = min_cmplt";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          pStmt.setInt(1, input.nextInt());
          System.out.println("Enter the person's ID number");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          System.out.println("Code \t Section \t Date");
          while(rs.next()){
            String cCode = rs.getString("c_code");
            String section = rs.getString("sec_no");
            java.util.Date completeDate = rs.getDate("complete_date");
            System.out.println(cCode + "\t" + section + "\t" + completeDate);
          }
        }

        else if (userOption == 11){
          System.out.println("Find the cheapest course to make up one’s skill gap by showing the course to take and the cost (of the section price).");
          statstr = "WITH course_list (c_code, price) AS " +
                        "(SELECT c_code, MIN(price) " +
                        "FROM  section Crs " +
                        "WHERE NOT EXISTS(SELECT ks_code " +
                            "FROM req_skill Rs " +
                            "WHERE job_code = ? AND NOT EXISTS( " +
                                "SELECT ks_code " +
                                "FROM course_skills Csk " +
                                "WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code) " +
                            "MINUS " +
                            "SELECT ks_code " +
                            "FROM spec_rel " +
                            "WHERE per_id = ?) " +
                        "GROUP BY c_code), " +
                    "min_price (m_price)AS " +
                        "(SELECT MIN(price) " +
                        "FROM course_list Crs) " +
                    "SELECT distinct Crs.c_code, sec_no, Crs.price " +
                        "FROM course_list Crs INNER JOIN section Sec ON Crs.c_code = Sec.c_code, min_price " +
                        "WHERE Crs.price = m_price";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          pStmt.setInt(1, input.nextInt());
          System.out.println("Enter the person's ID number");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          System.out.println("Code \tSection \tPrice");
          while(rs.next()){
            String cCode = rs.getString("c_code");
            String section = rs.getString("sec_no");
            int price = rs.getInt("price");
            System.out.println(cCode + "\t" + section + "\t\t" + price);
          }
        }

        else if (userOption == 12){
          System.out.println("12 If query #9 returns nothing, then find the course sets that their combination covers all the missing knowledge/ \n" +
           "skills for a person to pursue a specific job. The considered course sets will not include more than three courses. \n" +
           "If multiple course sets are found, list the course sets (with their course IDs) in the order of the ascending order of \n" +
           "the course sets’ total costs.");
          statstr = "WITH missing_skills " +
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
          System.out.println("Enter the person's ID number");
          int perID = input.nextInt();
          pStmt.setInt(2, perID);
          System.out.println("Enter job code");
          int jobCode = input.nextInt();
          pStmt.setInt(1, jobCode);
          pStmt.setInt(3, jobCode);
          rs = pStmt.executeQuery();
          System.out.println("Course1 \tCourse2 \tCourse 3 \tPrice");
          while(rs.next()){
            String cCode1 = rs.getString("c_code_1");
            String cCode2 = rs.getString("c_code_2");
            String cCode3 = rs.getString("c_code_3");
            int price = rs.getInt("price");

            System.out.println(cCode1 + "\t\t" + cCode2 + "\t\t" + cCode3 + "\t\t" + price);
          }
        }
        else if (userOption == 13){
          System.out.println("List all the job categories that a person is qualified for.");
          statstr = "SELECT SOC AS job_category, category_title " +
          "FROM job_category JCs " +
          "WHERE NOT EXISTS(SELECT ks_code " +
              "FROM core_skill " +
              "WHERE soc = JCs.soc " +
              "MINUS " +
              "SELECT ks_code " +
              "FROM spec_rel " +
              "WHERE per_id = ?)";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String jobCategory = rs.getString("job_category");
            String categoryTitle = rs.getString("category_title");
            System.out.println(jobCategory + "\t" + categoryTitle);
          }

        }
        else if (userOption == 14){
          System.out.println("Find the job with the highest pay rate for a person according to his/her skill qualification.");
          statstr = "WITH qualified AS " +
                      "(SELECT job_code, pay_rate " +
                      "FROM jobs Jo " +
                      "WHERE NOT EXISTS(SELECT ks_code " +
                          "FROM req_skill " +
                          "WHERE job_code = Jo.job_code " +
                          "MINUS " +
                          "SELECT ks_code " +
                          "FROM spec_rel " +
                          "WHERE per_id = ?)), " +
                    "max_pay (m_pay) AS " +
                      "(SELECT MAX(pay_rate) " +
                      "FROM qualified) " +
                  "SELECT job_code " +
                  "FROM qualified NATURAL JOIN max_pay " +
                  "WHERE pay_rate = m_pay";



          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter the person's ID number");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          System.out.println("Job Code");
          while(rs.next()){
            String jobCode = rs.getString("job_code");
            System.out.println(jobCode);
          }
        }
        else if (userOption == 15){
          System.out.println("List all the names along with the emails of the persons who are qualified for a job.  ");
          statstr = "SELECT first_name, last_name, email " +
          "FROM person Prsn " +
          "WHERE NOT EXISTS(SELECT ks_code " +
              "FROM req_skill Ks " +
              "WHERE job_code = ? " +
              "MINUS " +
              "SELECT ks_code " +
              "FROM spec_rel " +
              "WHERE per_id = Prsn.per_id)";

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

          statstr = "SELECT first_name, last_name, email " +
          "FROM person Ps " +
          "WHERE EXISTS(WITH req_cont (cnt) " +
              "AS(SELECT COUNT(ks_code) " +
                    "FROM req_skill " +
                    "WHERE job_code = ?), " +
              "missing_skill (per_id, ks_code) " +
              "AS(SELECT DISTINCT per_id, ks_code " +
                    "FROM person, req_skill " +
                    "WHERE job_code = ? " +
                    "MINUS " +
                    "SELECT per_id, ks_code " +
                    "FROM spec_rel) " +
              "SELECT per_id " +
              "FROM missing_skill " +
              "WHERE per_id = Ps.per_id " +
              "GROUP BY per_id " +
              "HAVING COUNT(ks_code) = (SELECT cnt - 1 " +
                                        "FROM req_cont))";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code");
          int jobCode = input.nextInt();
          pStmt.setInt(1, jobCode);
          pStmt.setInt(2, jobCode);
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
          statstr = "WITH req_cont (cnt) " +
                      "AS(SELECT COUNT(ks_code) " +
                          "FROM req_skill " +
                          "WHERE job_code = ?), " +
                    "missing_skill (per_id, ks_code) " +
                      "AS(SELECT DISTINCT per_id, ks_code " +
                          "FROM person, req_skill " +
                          "WHERE job_code = ? " +
                          "MINUS " +
                          "SELECT per_id, ks_code " +
                          "FROM spec_rel), " +
                    "missing_one_list " +
                      "AS( SELECT per_id " +
                          "FROM missing_skill " +
                          "GROUP BY per_id " +
                          "HAVING COUNT(ks_code) = (SELECT cnt - 1 " +
                                                  "FROM req_cont)) " +
                    "SELECT ks_code, count(distinct Ms.per_id) Pid_count " +
                    "FROM missing_skill Ms INNER JOIN missing_one_list Mol ON Ms.per_id = Mol.per_id " +
                    "GROUP BY ks_code " +
                    "ORDER BY Pid_count ASC";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter a job code.");
          int jobCode = input.nextInt();
          pStmt.setInt(1, jobCode);
          pStmt.setInt(2, jobCode);
          pStmt.setInt(3, jobCode);
          rs = pStmt.executeQuery();

          System.out.println("KS Code \t Person Count");
          while(rs.next()){
            int ksCode = rs.getInt("ks_code");
            int pIDCount = rs.getInt("Pid_count");
            System.out.println(ksCode + "\t\t" + pIDCount);
          }
        }
        else if (userOption == 18){
          System.out.println("Suppose there is a new job that has nobody qualified. List " +
                              "the persons who miss the least number of skills and " +
                              "report the 'least number'");

          statstr = "WITH req_cont (cnt) " +
              "AS(SELECT COUNT(ks_code) " +
                "FROM req_skill  " +
                "WHERE job_code = ?),  " +
            "missing_skill (per_id, ks_code) " +
              "AS(SELECT DISTINCT per_id, ks_code " +
               "FROM person, req_skill " +
                "WHERE job_code = ? " +
                "MINUS " +
                "SELECT per_id, ks_code " +
                "FROM spec_rel), " +
            "least_missing (missing_ct) " +
              "AS(SELECT MIN(COUNT(ks_code)) " +
                "FROM missing_skill " +
                "GROUP BY per_id), " +
          "missing_ct_list " +
            "AS( SELECT per_id, missing_ct " +
              "FROM missing_skill,least_missing " +
              "GROUP BY missing_ct, per_id " +
              "HAVING COUNT(ks_code) = missing_ct ) " +
              "SELECT first_name, last_name, email, missing_ct AS Missing_skill_count " +
              "FROM person NATURAL JOIN missing_ct_list";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter job code.");
          int jobId = input.nextInt();
          pStmt.setInt(1, jobId);
          pStmt.setInt(2, jobId);
          rs = pStmt.executeQuery();

          System.out.println("\nFirst Name \tLast Name \tEmail \t\t\tMissing Skill Count\n");
          while(rs.next()){
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            String missingSkillCount = rs.getString("Missing_skill_count");

            System.out.printf("%-15.15s %-15.15s %-30.30s %-15.15s%n",  firstName, lastName, email , missingSkillCount);
          }
        }
        else if (userOption == 19){
          System.out.println("For a specified job category and a given small number k, make a 'missing-k' " +
                              "list that lists the people’s IDs andthe number of missing skills  for the " +
                              "people who miss only up to k skills in the ascending order of missing skills.");

          statstr = "WITH missing_skill (per_id, ks_code) " +
              "AS(SELECT DISTINCT per_id, ks_code " +
                "FROM person, core_skill " +
                "WHERE SOC = ? " +
                "MINUS " +
                "SELECT per_id, ks_code " +
                "FROM spec_rel) " +
            "SELECT per_id, COUNT(ks_code) AS Missing_skill_Ct " +
            "FROM missing_skill Ms " +
            "WHERE EXISTS( " +
                "SELECT per_id " +
                "FROM missing_skill " +
                "WHERE per_id = Ms.per_id " +
                "GROUP BY per_id " +
                "HAVING COUNT(ks_code) <= ?) " +
            "GROUP BY per_id " +
            "ORDER BY Missing_skill_Ct ASC";

          pStmt = connection.prepareStatement(statstr);

          System.out.println("Enter a SOC code.");
          pStmt.setInt(1, input.nextInt());

          System.out.println("Enter a k for missing-k");
          pStmt.setInt(2, input.nextInt());

          rs = pStmt.executeQuery();

          System.out.println("PerID\tCount");
          while(rs.next()){
            int perId = rs.getInt("per_id");
            int count = rs.getInt("Missing_skill_Ct");
            System.out.println(perId + "\t" + count);
          }
        }
        else if (userOption == 20){
          System.out.println("Given a job category code and its corresponding missing-k list specified " +
                              "in Question 19. Find every skill that is needed by at least one person in the " +
                              "given missing-k list. List each skillID and the number of people who need it " +
                              "in the descending order of the people counts");

          statstr = "WITH missing_skill (per_id, ks_code) " +
              "AS(SELECT DISTINCT per_id, ks_code " +
                "FROM person, core_skill " +
                "WHERE SOC = ? " +
                "MINUS " +
                "SELECT per_id, ks_code " +
                "FROM spec_rel), " +
              "missing_k_list " +
              "AS(SELECT per_id, ks_code " +
                "FROM missing_skill Ms " +
                "WHERE EXISTS( " +
                    "SELECT per_id " +
                    "FROM missing_skill " +
                    "WHERE per_id = Ms.per_id " +
                    "GROUP BY per_id " +
                    "HAVING COUNT(ks_code) <= ?)) " +
                "SELECT ks_code, COUNT(per_id) AS Person_count " +
                "FROM missing_k_list " +
                "GROUP BY ks_code " +
                "ORDER BY Person_count DESC";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter a SOC code.");
          int soc = input.nextInt();
          pStmt.setInt(1, soc);
          System.out.println("Enter a k for missing-k");
          pStmt.setInt(2, input.nextInt());
          rs = pStmt.executeQuery();

          System.out.println("PerID\tCount");
          while(rs.next()){
            int ksCode = rs.getInt("ks_code");
            int pIDCount = rs.getInt("Person_count");
            System.out.println(ksCode + "\t" + pIDCount);
          }
        }
        else if (userOption == 21){
          System.out.println("In a local or national crisis, we need to find all the people who once " +
                              "held a job of the special job category identifier.");

          statstr = "SELECT DISTINCT per_id " +
              "FROM experience NATURAL JOIN jc_rel " +
              "WHERE SOC = ?";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter a SOC code.");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            int perId = rs.getInt("per_id");
            System.out.println(perId);
          }
        }
        else if (userOption == 22){
          System.out.println("Find all the unemployed people who once held a job of the " +
                              "given job identifier.");

          statstr = "SELECT per_id " +
              "FROM experience " +
              "WHERE job_code = ? AND end_date IS NOT NULL";

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter a job code.");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            int perId = rs.getInt("per_id");
            System.out.println(perId);
          }
        }
        else if (userOption == 23){
          System.out.println("Find out the biggest employer in terms of number of employees or \n" +
          "the total amount of salaries and wages paid to employees.");

          statstr = "WITH Salary_tmp " +
            "AS(SELECT comp_id, salary " +
                "FROM jobs NATURAL JOIN experience " +
                "WHERE end_date IS NULL AND pay_type = 'Salary' " +
                "UNION " +
                "SELECT comp_id, salary * 1920 " +
                "FROM jobs NATURAL JOIN experience " +
                "WHERE end_date IS NULL AND pay_type = 'Hourly') " +
            "SELECT comp_id " +
            "FROM Salary_tmp " +
            "GROUP BY comp_id " +
            "HAVING SUM(salary) = (SELECT MAX(SUM(salary)) " +
                                  "FROM Salary_tmp " +
                                  "GROUP BY comp_id)";

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){
            String compID = rs.getString("comp_id");
            System.out.println(compID);
          }
        }
        else if (userOption == 24){
          System.out.println("Find out the job distribution among business sectors; find out the biggest sector \n" +
          "in terms of number of employees or the total amount of salaries and wages paid to employees.");

          statstr = "WITH Salary_tmp " +
            "AS(SELECT SOC, salary " +
                "FROM jobs NATURAL JOIN jc_rel NATURAL JOIN experience " +
                "WHERE end_date IS NULL AND pay_type = 'Salary' " +
                "UNION " +
                "SELECT SOC, salary * 1920 " +
                "FROM jobs NATURAL JOIN jc_rel NATURAL JOIN experience " +
                "WHERE end_date IS NULL AND pay_type = 'Hourly') " +
            "SELECT SOC " +
            "FROM Salary_tmp " +
            "GROUP BY SOC " +
            "HAVING SUM(salary) = (SELECT MAX(SUM(salary)) " +
                                  "FROM Salary_tmp " +
                                  "GROUP BY SOC)";

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){
            String soc = rs.getString("SOC");
            System.out.println(soc);
          }
        }
        else if (userOption == 25){
          System.out.println("Find out the ratio between the people whose earnings increase and \n" +
          "those whose earning decrease; find the average rate of earning improvement \n" +
          "for the workers in a specific business sector.");

          statstr = "WITH all_job_info " +
            "AS( SELECT SOC, salary, start_date, end_date, pay_type, per_id, Jo.job_code " +
                "FROM (jobs Jo INNER JOIN jc_rel Jc  ON Jo.job_code = Jc.job_code) " +
                    "INNER JOIN experience Exp1 ON Jo.job_code = Exp1.job_code " +
                "WHERE SOC = ?), " +
            "ending_salary (SOC, end_sal, per_id) " +
            "AS(SELECT SOC, salary, per_id " +
                "FROM all_job_info " +
                "WHERE end_date IS NULL AND pay_type = 'Salary' " +
                "UNION " +
                "SELECT SOC, salary * 1920, per_id " +
                "FROM all_job_info " +
                "WHERE end_date IS NULL AND pay_type = 'Hourly'), " +
            "starting_salary (SOC, start_sal, per_id) " +
            "AS(SELECT SOC, salary, per_id " +
                "FROM all_job_info aji " +
                "WHERE end_date IS NOT NULL AND pay_type = 'Salary' AND start_date = (SELECT MIN(start_date) " +
                        "FROM experience Exp " +
                        "WHERE Exp.per_id = aji.per_id AND Exp.job_code = aji.job_code AND end_date IS NOT NULL) " +
                "UNION " +
                "SELECT SOC, salary * 1920, per_id " +
                "FROM all_job_info aji " +
                "WHERE end_date IS NOT NULL AND pay_type = 'Hourly'  AND start_date = (SELECT MIN(start_date) " +
                        "FROM experience Exp " +
                        "WHERE Exp.per_id = aji.per_id AND Exp.job_code = aji.job_code AND end_date IS NOT NULL)) " +
                "SELECT Ss.SOC, AVG(end_sal - start_sal ) AS Average_Salary_Increase " +
                "FROM starting_salary Ss INNER JOIN ending_salary Es ON Ss. per_id = Es.per_id " +
                "GROUP BY Ss.SOC";
                ;

          pStmt = connection.prepareStatement(statstr);
          System.out.println("Enter an soc to evaluate.");
          pStmt.setInt(1, input.nextInt());
          rs = pStmt.executeQuery();

          while(rs.next()){
            String soc = rs.getString("SOC");
            String avgSalIncrease = rs.getString("Average_Salary_Increase");
            System.out.println(soc + "\t" + avgSalIncrease);
          }
        }
        else if (userOption == 26){
          System.out.println("Find the leaf-node job categories that have the most openings due to\n" +
          "lack of qualified workers. If there are many opening jobs of a job category \n" +
          "but at the same time there are many qualified jobless people. Then training \n" +
          "cannot help fill up this type of job. What we want to find is such a job \n" +
          "category that has the largest difference between vacancies (the unfilled \n" +
          "jobs of this category) and the number of jobless people who are qualified \n" +
          "for the jobs of his category.");

          statstr = "WITH open_jobs AS " +
                      "(SELECT  job_code " +
                      "FROM jobs " +
                      "MINUS " +
                      "SELECT  job_code " +
                      "FROM experience " +
                      "WHERE end_date IS NULL), " +
                    "qualified AS " +
                      "(SELECT job_code " +
                      "FROM jobs Jo " +
                      "WHERE NOT EXISTS(SELECT ks_code " +
                            "FROM req_skill " +
                            "WHERE job_code = Jo.job_code " +
                            "MINUS " +
                            "SELECT ks_code " +
                            "FROM spec_rel)), " +
                    "qualification_difference AS " +
                      "(SELECT job_code, SOC " +
                      "FROM (SELECT job_code FROM open_jobs " +
                            "MINUS " +
                            "SELECT job_code FROM qualified) NATURAL JOIN jc_rel) " +
                    "SELECT SOC " +
                    "FROM qualification_difference " +
                    "GROUP BY SOC " +
                    "HAVING COUNT(job_code) = (SELECT MAX(COUNT(job_code)) " +
                                              "FROM qualification_difference " +
                                              "GROUP BY SOC)";

          pStmt = connection.prepareStatement(statstr);
          rs = pStmt.executeQuery();

          while(rs.next()){
            String soc = rs.getString("SOC");
            System.out.println(soc);
          }
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
