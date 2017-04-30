-- Sean Naquin
-- Majde Judeh

--queries file

/*
 * all variables are deNOTed BY a comment starting with
 * "---variables:" at the end of the line
 */

-- 1. List a company's workers BY names.
SELECT First_name, Last_name
FROM (jobs NATURAL JOIN experience Ex)INNER JOIN person Ps ON Ex.per_id = Ps.per_id
WHERE Ex.end_date IS NULL AND comp_id = ?;                                      ---variables: comp_id(test on 2/4)
--1 END COMMENT

-- 2. List a company's staff BY salary in descending ORDER.
SELECT first_name, last_name, salary
FROM (jobs NATURAL JOIN experience Ex) INNER JOIN person Ps ON Ex.per_id = Ps.per_id
WHERE comp_id = ? AND end_date IS NULL AND pay_type = 'Salary'                  ---variables: comp_id(test on 1/2)
ORDER BY salary DESC;
--2 END COMMENT

--3. List companies' labor cost (total salaries and wage rates BY 1920 hours) in descending ORDER.
WITH Salary_tmp
AS(SELECT comp_id, salary
    FROM jobs NATURAL JOIN experience
    WHERE end_date IS NULL AND pay_type = 'Salary'
    UNION
    SELECT comp_id, salary * 1920
    FROM jobs NATURAL JOIN experience
    WHERE end_date IS NULL AND pay_type = 'Hourly')
SELECT comp_id,  SUM(salary) AS Labor_Cost
FROM Salary_tmp
GROUP BY comp_id
ORDER BY Labor_Cost DESC;
--3 END COMMENT

--4. Find all the jobs a person is currently holding and worked --in the past.
SELECT job_code, start_date, end_date, salary, emp_mode, pay_rate, pay_type, company_name, city, state_abbr
FROM experience NATURAL JOIN jobs NATURAL JOIN company
WHERE per_id = ?;                                                               ---variables: per_id(test on 6/7)
--4 END COMMENT

--5. List a person's knowledge/skills in a readable format.
SELECT per_id, skill_title AS skill
FROM knowledge_skills NATURAL JOIN spec_rel
WHERE per_id = ?;                                                               ---variables: per_id(test on 1/3)
--5 END COMMENT


--6. List the skill gap of a worker between his/her jobs(s) and --his/her skills.
SELECT DISTINCT ks_code
FROM  req_skill NATURAL JOIN experience 
WHERE per_id = ? AND end_date IS NULL                                           ---variables: per_id(test on 3/6)
MINUS
SELECT ks_code
FROM spec_rel
WHERE per_id = ?;                                                               ---variables: per_id(test on 3/6)
--6 END COMMENT

--7. List the required knowledge/skills of a job/ a job category --in a readable format. (two queries)
SELECT skill_title AS required_skill, ks_code
FROM  req_skill NATURAL JOIN knowledge_skills
WHERE job_code = ?;                                                             ---variables: job_code(test on 3/11)

SELECT skill_title AS required_skill, ks_code
FROM core_skill NATURAL JOIN knowledge_skills
WHERE soc = ?;                                                                  ---variables: soc(test on 1/3)
--7 END COMMENT

--8 List a person's missing knowledge/skills for a specific job in a readable format.
SELECT skill_title AS skill_lacked, ks_code
FROM(SELECT ks_code
    FROM req_skill
    WHERE job_code = ?                                                          ---variables: job_code(test on 2/11)
    MINUS
    SELECT ks_code
    FROM spec_rel
    WHERE per_id = ?) NATURAL JOIN knowledge_skills;                            ---variables:  per_id(test on 3/4)
--8 END COMMENT

--9 List the courses(course id and title) that each alone
--teaches all the missing knowledge/skills for a person to
--pursue a specific job.
SELECT c_code, course_title
FROM course Crs
WHERE EXISTS(SELECT ks_code --ensures that the person is missing skills for the job
    FROM req_skill
    WHERE job_code = ?                                                          ---variables:  job_code(test on 3/10)
    MINUS
    SELECT ks_code
    FROM spec_rel
    WHERE per_id = 7) AND
NOT EXISTS(SELECT ks_code
    FROM req_skill Rs
    WHERE job_code = ? AND NOT EXISTS(                                          ---variables:  job_code(test on 3/10)
        SELECT ks_code
        FROM course_skills Csk
        WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code)
    MINUS
    SELECT ks_code
    FROM spec_rel
    WHERE per_id = ?);                                                          ---variables:  per_id(test on 3/7) 
--9 END COMMENT

--10 Suppose the skill gap of a worker and the requirement of a --desired job can be covered BY ONe course. Find the
--   ?quickest? solution for this worker. Show the course,
--section information and the completion date
WITH course_list (c_code, complete_date) AS
    (SELECT c_code, MIN(complete_date)
    FROM  section Crs
    WHERE NOT EXISTS(SELECT ks_code
        FROM req_skill Rs
        WHERE job_code = ? AND NOT EXISTS(                                      ---variables:  job_code(test on 3/4) 
            SELECT ks_code
            FROM course_skills Csk
            WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code)
        MINUS
        SELECT ks_code
        FROM spec_rel
        WHERE per_id = ?)                                                       ---variables:  per_id(test on 3/2) 
    GROUP BY c_code),
min_complete (min_cmplt)AS
    (SELECT MIN(complete_date)
    FROM course_list)                                                      
SELECT distinct Crs.c_code, sec_no, Crs.complete_date
    FROM course_list Crs INNER JOIN section Sec ON Crs.c_code = Sec.c_code, min_complete
    WHERE Crs.complete_date = min_cmplt;
--10 END COMMENT

--11 Find the cheapest course to make up ONe?s skill gap BY
--showing the course to take and the cost (of the section
--price).
WITH course_list (c_code, price) AS
    (SELECT c_code, MIN(price)
    FROM  section Crs
    WHERE NOT EXISTS(SELECT ks_code
        FROM req_skill Rs
        WHERE job_code = ? AND NOT EXISTS(                                      ---variables:  job_code(test on 3/4) 
            SELECT ks_code
            FROM course_skills Csk
            WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code)
        MINUS
        SELECT ks_code
        FROM spec_rel
        WHERE per_id = ?)                                                       ---variables:  per_id(test on 3/2) 
    GROUP BY c_code),
min_price (m_price)AS
    (SELECT MIN(price)
    FROM course_list Crs)                                                      
SELECT distinct Crs.c_code, sec_no, Crs.price
    FROM course_list Crs INNER JOIN section Sec ON Crs.c_code = Sec.c_code, min_price
    WHERE Crs.price = m_price;                                                  
--11 END COMMENT

--12 If query #9 returns NOThing, then find the course sets that
--their combination covers all the missing knowledge/
-- skills for a person to pursue a specific job. The considered
--course sets will NOT include more than three courses.
-- If multiple course sets are found, list the course sets (with
--their course IDs) in the ORDER of the ascending ORDER of
-- the course sets? total costs.

WITH missing_skills
  AS( SELECT ks_code
      FROM req_skill
      WHERE job_code = ?                                                        ---variables:  job_code(test on 10/11)                              
      MINUS
      SELECT ks_code
      FROM spec_rel
      WHERE per_id = ?),                                                        ---variables:  per_id(test on 1/3) 
single_course_list
  AS( SELECT c_code
      FROM course Crs
      WHERE EXISTS( SELECT ks_code
                  FROM missing_skills)
      AND EXISTS( SELECT ks_code
                  FROM course_skills NATURAL JOIN req_skill
                  WHERE c_code = Crs.c_code AND job_code = ?                    ---variables:  job_code(test on 10/11)   
                  MINUS
                  SELECT ks_code
                  FROM missing_skills)),
course_sets_2 (id, c_code_1, c_code_2,c_code_3,price)
  AS( SELECT ROWNUM, c1.c_code, c2.c_code, null,s1.price + s2.price 
      FROM course_skills c1 INNER JOIN section s1 ON c1.c_code = s1.c_code,
      course_skills c2 INNER JOIN section s2 ON c2.c_code = s2.c_code
      WHERE c1.c_code != c2.c_code AND NOT EXISTS(SELECT c_code
                  FROM single_course_list)
      AND NOT EXISTS( SELECT ks_code
                  FROM missing_skills
                  MINUS
                  SELECT ks_code
                  FROM course_skills
                  WHERE c_code = c1.c_code OR c_code = c2.c_code)),
course_sets_3 (id, c_code_1, c_code_2, c_code_3, price)
  AS( SELECT ROWNUM, c1.c_code, c2.c_code, c3.c_code, s1.price+s2.price+s3.price
      FROM course_skills c1 INNER JOIN section s1 ON c1.c_code = s1.c_code, 
      course_skills c2  INNER JOIN section s2 ON c2.c_code = s2.c_code, 
      course_skills c3  INNER JOIN section s3 ON c3.c_code = s3.c_code
      WHERE c1.c_code != c2.c_code AND c1.c_code != c3.c_code 
      AND c3.c_code != c2.c_code AND NOT EXISTS(SELECT c_code
                  FROM single_course_list)
      AND NOT EXISTS(SELECT c_code_1
                  FROM course_sets_2)
      AND NOT EXISTS( SELECT ks_code
                  FROM missing_skills
                  MINUS
                  SELECT ks_code
                  FROM course_skills
                  WHERE c_code = c1.c_code OR c_code = c2.c_code 
                  OR c_code = c3.c_code)),
combined_list 
    AS(SELECT c_code_1, c_code_2, c_code_3,price
    FROM course_sets_3 Cs3
    WHERE id  = (SELECT MIN(id)
                  FROM course_sets_3
                  WHERE (Cs3.c_code_1 = c_code_2 OR Cs3.c_code_1 = c_code_3 
                  OR Cs3.c_code_2 = c_code_3 OR Cs3.c_code_2 = c_code_1
                  OR Cs3.c_code_3 = c_code_2 OR Cs3.c_code_3 = c_code_1)
                  OR (Cs3.c_code_1 = c_code_1 AND Cs3.c_code_2 = c_code_2 AND Cs3.c_code_3 = c_code_3)
                  AND Cs3.price = price)
    UNION 
    SELECT c_code_1, c_code_2, c_code_3,price
    FROM course_sets_2 Cs2
    WHERE id  = (SELECT MIN(id)
                  FROM course_sets_2
                  WHERE (Cs2.c_code_1 = c_code_2 OR Cs2.c_code_2 = c_code_1)
                  OR (Cs2.c_code_1 = c_code_1 AND Cs2.c_code_2 = c_code_2)
                  AND Cs2.price = price))                                
SELECT c_code_1,c_code_2,c_code_3, price
FROM combined_list
ORDER BY price ASC;
--12 It works, but 21 SELECT statements... there's probably a
--more efficient way to do this
--my own implementation, date complete: 4/7/2017, BONUS POINTS

--13 List all the job categories that a person is qualified for.
SELECT SOC AS job_category, category_title
FROM job_category JCs
WHERE NOT EXISTS(SELECT ks_code
    FROM core_skill
    WHERE soc = JCs.soc
    MINUS
    SELECT ks_code
    FROM spec_rel
    WHERE per_id = ?);                                                           ---variables:  per_id(test on 2/6)
--13 END COMMENT, implemented my own solution



--14 Find the job with the highest pay rate for a person
--according to his/her skill qualification.
WITH qualified AS
  (SELECT job_code, pay_rate
  FROM jobs Jo
  WHERE NOT EXISTS(SELECT ks_code
      FROM req_skill
      WHERE job_code = Jo.job_code
      MINUS
      SELECT ks_code
      FROM spec_rel
      WHERE per_id = ?)),                                                       ---variables:  per_id(test on 4/6)
max_pay (m_pay) AS 
  (SELECT MAX(pay_rate)
  FROM qualified)
SELECT job_code
FROM qualified NATURAL JOIN max_pay
WHERE pay_rate = m_pay;
--14 END COMMENT, custom solution


--15 List all the names along with the emails of the persons who
--are qualified for a job.
SELECT first_name, last_name, email
FROM person Prsn
WHERE NOT EXISTS(SELECT ks_code
         FROM req_skill Ks
         WHERE job_code = ?                                                     ---variables: job_code(test on 1/3)
         MINUS
         SELECT ks_code
         FROM spec_rel
         WHERE per_id = Prsn.per_id);
--15 END COMMENT

--16. When a company canNOT find any qualified person for a job,
--a secondary solution is to find a person who is almost
--qualified to the job. Make a ?missing-one? list that lists
--people who miss ONly ONe skill for a specified job.
SELECT first_name, last_name, email
FROM person Ps
WHERE EXISTS(WITH req_cont (cnt)
      AS(SELECT COUNT(ks_code)
          FROM req_skill
          WHERE job_code = ?),                                                  ---variables: job_code(test on 3/8)
      missing_skill (per_id, ks_code)
      AS(SELECT DISTINCT per_id, ks_code
          FROM person, req_skill
          WHERE job_code = ?                                                    ---variables: job_code(test on 3/8)
          MINUS
          SELECT per_id, ks_code
          FROM spec_rel)
      SELECT per_id
      FROM missing_skill
      WHERE per_id = Ps.per_id
      GROUP BY per_id
      HAVING COUNT(ks_code) = (SELECT cnt - 1
                              FROM req_cont));
--16 END COMMENT

--17. List the skillID AND the number of people in the missing-
--one list for a given job code in the ascending ORDER of the
--people counts.
WITH req_cont (cnt)
    AS(SELECT COUNT(ks_code)
      FROM req_skill
      WHERE job_code = ?),                                                      ---variables: job_code(test on 3/6)
missing_skill (per_id, ks_code)
    AS(SELECT DISTINCT per_id, ks_code
      FROM person, req_skill
      WHERE job_code = ?                                                        ---variables: job_code(test on 3/6)
      MINUS
      SELECT per_id, ks_code
      FROM spec_rel),
missing_one_list 
  AS( SELECT per_id
    FROM missing_skill
    GROUP BY per_id
    HAVING COUNT(ks_code) = (SELECT cnt - 1
                        FROM req_cont)) 
SELECT ks_code, count(distinct Ms.per_id) Pid_count
FROM missing_skill Ms INNER JOIN missing_one_list Mol ON Ms.per_id = Mol.per_id
GROUP BY ks_code
ORDER BY Pid_count ASC;
--17 END COMMENT

--18. Suppose there is a new job that has nobody qualified. List
--the persons who miss the least number of skills and
--report the ?least number?.
WITH req_cont (cnt)
    AS(SELECT COUNT(ks_code)
      FROM req_skill
      WHERE job_code = ?),                                                      ---variables: job_code(test on 6/8)
missing_skill (per_id, ks_code)
    AS(SELECT DISTINCT per_id, ks_code
      FROM person, req_skill
      WHERE job_code = ?                                                        ---variables: job_code(test on 6/8)
      MINUS
      SELECT per_id, ks_code
      FROM spec_rel),
least_missing (missing_ct)
  AS(SELECT MIN(COUNT(ks_code))
    FROM missing_skill
    GROUP BY per_id),
missing_ct_list 
  AS( SELECT per_id, missing_ct
    FROM missing_skill,least_missing
    GROUP BY missing_ct, per_id
    HAVING COUNT(ks_code) = missing_ct ) 
SELECT first_name, last_name, email, missing_ct AS Missing_skill_count
FROM person NATURAL JOIN missing_ct_list;
--18 END COMMENT

--19. For a specified job category and a given small number k, make a “missing-k"?
--list that lists the people’s IDs andthe number of missing skills  for the
--people who miss only up to k skills in the ascending order of missing skills.
WITH missing_skill (per_id, ks_code)
  AS(SELECT DISTINCT per_id, ks_code
      FROM person, core_skill
      WHERE SOC = ?                                                             ---variables: SOC(test on 1/2)
      MINUS
      SELECT per_id, ks_code
      FROM spec_rel)
SELECT per_id, COUNT(ks_code) AS Missing_skill_Ct
FROM missing_skill Ms
WHERE EXISTS(
      SELECT per_id
      FROM missing_skill
      WHERE per_id = Ms.per_id
      GROUP BY per_id
      HAVING COUNT(ks_code) <= ?)                                               ---variables: k(test on 2/4)
GROUP BY per_id
ORDER BY Missing_skill_Ct ASC;
--19 END COMMENT

--20. Given a job category code and its corresponding missing-k list specified
--in Question 19. Find every skill that is needed by at least one person in the
--given missing-k list. List each skillID and the number of people who need it
--in the descending order of the people counts
WITH missing_skill (per_id, ks_code)
  AS(SELECT DISTINCT per_id, ks_code
      FROM person, core_skill
      WHERE SOC = ?                                                             ---variables: SOC(test on 1/2)
      MINUS
      SELECT per_id, ks_code
      FROM spec_rel),
missing_k_list
  AS(SELECT per_id, ks_code
  FROM missing_skill Ms
  WHERE EXISTS(
        SELECT per_id
        FROM missing_skill
        WHERE per_id = Ms.per_id
        GROUP BY per_id
        HAVING COUNT(ks_code) <= ?))                                            ---variables: k(test on 2/4)
SELECT ks_code, COUNT(per_id) AS Person_count
FROM missing_k_list
GROUP BY ks_code
ORDER BY Person_count DESC;
--20 END COMMENT


--21. In a local or national crisis, we need to find all the people who once
--held a job of the special job category identifier.
SELECT DISTINCT per_id
FROM experience NATURAL JOIN jc_rel
WHERE SOC = ?;                                                                  ---variables: SOC(test on 2/3)
--21 END COMMENT

--22. Find all the unemployed people who once held a job of the
--given job identifier.
SELECT per_id
FROM experience Ex
WHERE job_code = ? AND NOT EXISTS (SELECT per_id                                ---variables: job_code(test on 2/3)
                        FROM experience
                        WHERE Ex.per_id = per_id AND end_date IS NULL);                                    
--22 END COMMENT, NOT EXIST ensures that the person currently holds no jobs

--23. Find out the biggest employer in terms of number of employees or
--the total amount of salaries and wages paid to employees.
WITH Salary_tmp
AS(SELECT comp_id, salary
    FROM jobs NATURAL JOIN experience
    WHERE end_date IS NULL AND pay_type = 'Salary'
    UNION
    SELECT comp_id, salary * 1920
    FROM jobs NATURAL JOIN experience
    WHERE end_date IS NULL AND pay_type = 'Hourly')
SELECT comp_id
FROM Salary_tmp
GROUP BY comp_id
HAVING SUM(salary) = (SELECT MAX(SUM(salary))
                      FROM Salary_tmp
                      GROUP BY comp_id);
--23 END COMMENT

--24. Find out the job distribution among business sectors; find out the
--biggest sector in terms of number of employees or the total amount of
--salaries and wages paid to employees.
WITH Salary_tmp
AS(SELECT SOC, salary
    FROM jobs NATURAL JOIN jc_rel NATURAL JOIN experience
    WHERE end_date IS NULL AND pay_type = 'Salary'
    UNION
    SELECT SOC, salary * 1920
    FROM jobs NATURAL JOIN jc_rel NATURAL JOIN experience
    WHERE end_date IS NULL AND pay_type = 'Hourly')
SELECT SOC
FROM Salary_tmp
GROUP BY SOC
HAVING SUM(salary) = (SELECT MAX(SUM(salary))
                      FROM Salary_tmp
                      GROUP BY SOC);
--24 END COMMENT

--25. Find out the ratio between the people whose earnings increase and
--those whose earning decrease; find the average rate of earning improvement
--for the workers in a specific business sector.
WITH all_job_info
AS( SELECT SOC, salary, start_date, end_date, pay_type, per_id, Jo.job_code
    FROM (jobs Jo INNER JOIN jc_rel Jc  ON Jo.job_code = Jc.job_code)
        INNER JOIN experience Exp1 ON Jo.job_code = Exp1.job_code
    WHERE SOC = ?),                                                             ---variables: SOC(test on 2/4)
ending_salary (SOC, end_sal, per_id)
AS(SELECT SOC, salary, per_id
    FROM all_job_info
    WHERE end_date IS NULL AND pay_type = 'Salary'
    UNION
    SELECT SOC, salary * 1920, per_id
    FROM all_job_info
    WHERE end_date IS NULL AND pay_type = 'Hourly'),
starting_salary (SOC, start_sal, per_id)
AS(SELECT SOC, salary, per_id
    FROM all_job_info aji
    WHERE end_date IS NOT NULL AND pay_type = 'Salary' AND start_date = (SELECT MIN(start_date)
              FROM experience Exp
              WHERE Exp.per_id = aji.per_id AND Exp.job_code = aji.job_code AND end_date IS NOT NULL)
    UNION
    SELECT SOC, salary * 1920, per_id
    FROM all_job_info aji
    WHERE end_date IS NOT NULL AND pay_type = 'Hourly'  AND start_date = (SELECT MIN(start_date)
              FROM experience Exp
              WHERE Exp.per_id = aji.per_id AND Exp.job_code = aji.job_code AND end_date IS NOT NULL))
SELECT Ss.SOC, AVG(end_sal - start_sal ) AS Average_Salary_Increase
FROM starting_salary Ss INNER JOIN ending_salary Es ON Ss. per_id = Es.per_id
GROUP BY Ss.SOC;
--25 END COMMENT

--26. Find the leaf-node job categories that have the most openings due to
--lack of qualified workers. If there are many opening jobs of a job category
--but at the same time there are many qualified jobless people. Then training
--cannot help fill up this type of job. What we want to find is such a job
--category that has the largest difference between vacancies (the unfilled
--jobs of this category) and the number of jobless people who are qualified
--for the jobs of his category.

WITH open_jobs AS
    (SELECT  job_code
    FROM jobs
    MINUS
    SELECT job_code
    FROM experience
    WHERE end_date IS NULL),
qualified AS
    (SELECT job_code
    FROM jobs Jo
    WHERE NOT EXISTS(SELECT ks_code
             FROM req_skill
             WHERE job_code = Jo.job_code
             MINUS
             SELECT ks_code
             FROM spec_rel)),
qualification_difference AS
    (SELECT job_code, SOC
    FROM (SELECT job_code FROM open_jobs
         MINUS
         SELECT job_code FROM qualified) NATURAL JOIN jc_rel)
SELECT SOC
FROM qualification_difference
GROUP BY SOC
HAVING COUNT(job_code) = (SELECT MAX(COUNT(job_code))
                              FROM qualification_difference
                              GROUP BY SOC);
--26 END COMMENT
