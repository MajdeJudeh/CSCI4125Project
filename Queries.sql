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
WHERE Ex.end_date IS NULL AND comp_id = ?;                                      ---variables: comp_id(test on 2)
--1 END COMMENT

-- 2. List a company's staff BY salary in descending ORDER.
SELECT first_name, last_name, salary
FROM (jobs NATURAL JOIN experience Ex) INNER JOIN person Ps ON Ex.per_id = Ps.per_id
WHERE comp_id = ? AND end_date IS NULL AND pay_type = 'Salary'                  ---variables: comp_id(test on 2)
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
FROM Salarys_view
GROUP BY comp_id 
ORDER BY Labor_Cost DESC;
--3 END COMMENT

--4. Find all the jobs a person is currently holding and worked --in the past.
SELECT job_code, start_date, end_date, salary, emp_mode, pay_rate, pay_type, company_name, city, state_abbr
FROM experience NATURAL JOIN jobs NATURAL JOIN company
WHERE per_id = ?;                                                               ---variables: per_id(test on 7)
--4 END COMMENT

--5. List a person's knowledge/skills in a readable format.
SELECT per_id, skill_title AS skill
FROM knowledge_skills NATURAL JOIN spec_rel
WHERE per_id = ?;                                                               ---variables: per_id(test on 1)
--5 END COMMENT


--6. List the skill gap of a worker between his/her jobs(s) and --his/her skills.
SELECT DISTINCT ks_code
FROM  req_skill NATURAL JOIN experience
WHERE per_id = ? AND end_date IS NULL                                           ---variables: per_id(test on 6)
MINUS
SELECT ks_code
FROM spec_rel
WHERE per_id = ?;                                                               ---variables: per_id(test on 6)
--6 END COMMENT

--7. List the required knowledge/skills of a job/ a job category --in a readable format. (two queries)
SELECT skill_title AS requireed_skill, ks_code
FROM  req_skill NATURAL JOIN knowledge_skills
WHERE job_code = ?;                                                             ---variables: job_code(test on 3)

SELECT skill_title AS required_skill, ks_code
FROM core_skill Cs NATURAL JOIN knowledge_skills
WHERE soc = ?;                                                                  ---variables: soc(test on 3)
--7 END COMMENT

--8 List a person's missing knowledge/skills for a specific job in a readable format.
SELECT skill_title AS skill_lacked, ks_code
FROM(SELECT ks_code
    FROM req_skill
    WHERE job_code = ?                                                          ---variables: job_code(test on 2)
    MINUS
    SELECT ks_code
    FROM spec_rel
    WHERE per_id = ?) NATURAL JOIN knowledge_skills;                             ---variables:  per_id(test on 3) 
--8 END COMMENT

--9 List the courses(course id and title) that each alone
--teaches all the missing knowledge/skills for a person to
--pursue a specific job.

SELECT c_code, course_title
FROM course Crs
WHERE NOT EXISTS(SELECT ks_code
    FROM req_skill Rs
    WHERE job_code = ? AND NOT EXISTS(                                          ---variables:  job_code(test on 3) 
        SELECT ks_code
        FROM course_skills Csk
        WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code)
    MINUS
    SELECT ks_code
    FROM spec_rel
    WHERE per_id = ?);                                                          ---variables:  per_id(test on 3) 
--9 END COMMENT

--10 Suppose the skill gap of a worker and the requirement of a --desired job can be covered BY ONe course. Find the
--   ?quickest? solution for this worker. Show the course,
--section information and the completion date
WITH course_list (c_code, Min_cmplt) AS
    (SELECT c_code, MIN(complete_date)
    FROM  section
    GROUP BY c_code)
SELECT Crs.c_code, sec_no, complete_date
    FROM course_list Crs INNER JOIN section Sec ON Crs.c_code = Sec.c_code
    WHERE complete_date = Min_cmplt AND NOT EXISTS(SELECT ks_code
        FROM req_skill Rs
        WHERE job_code = ? AND NOT EXISTS(                                      ---variables:  job_code(test on 3) 
            SELECT ks_code
            FROM course_skills Csk
            WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code)
        MINUS
        SELECT ks_code
        FROM spec_rel
        WHERE per_id = ?);                                                      ---variables:  per_id(test on 3)
--10 END COMMENT

--11 Find the cheapest course to make up ONe?s skill gap BY
--showing the course to take and the cost (of the section
--price).
WITH course_list (c_code, Min_price) AS
    (SELECT c_code, MIN(price)
    FROM  section
    GROUP BY c_code)
SELECT DISTINCT Crs.c_code, sec_no, price
    FROM course_list Crs INNER JOIN section Sec ON Crs.c_code = Sec.c_code
    WHERE price = Min_price AND NOT EXISTS(SELECT ks_code
        FROM req_skill Rs
        WHERE job_code = ? AND NOT EXISTS(                                      ---variables:  job_code(test on 3) 
            SELECT ks_code
            FROM course_skills Csk
            WHERE Crs.c_code = Csk.c_code AND Csk.ks_code = Rs.ks_code)
        MINUS
        SELECT ks_code
        FROM spec_rel
        WHERE per_id = ?);                                                      ---variables:  per_id(test on 5)
--11 END COMMENT

--12 If query #9 returns NOThing, then find the course sets that
--their combination covers all the missing knowledge/
-- skills for a person to pursue a specific job. The considered
--course sets will NOT include more than three courses.
-- If multiple course sets are found, list the course sets (with
--their course IDs) in the ORDER of the ascending ORDER of
-- the course sets? total costs.
SELECT c_code, course_title
FROM course NATURAL JOIN course_skills Cs INNER JOIN (SELECT ks_code Ks, per_id
    FROM (SELECT per_id, ks_code, job_code
    FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
    INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
    MINUS
    SELECT per_id, ks_code, job_code
    FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
    INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
    WHERE per_id = ? AND job_code = ?) ON Ks = Cs.ks_code                 ---variables:  per_id(test on 2) , job_code(test on 3)
WHERE NOT EXISTS(SELECT Fn AS c_code, Bn AS course_title
    FROM (SELECT c_code AS Fn, course_title AS Bn FROM course)
        WHERE NOT EXISTS( SELECT *
            FROM (SELECT ks_code AS Ks
                FROM (SELECT per_id, ks_code, job_code
                    FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                    INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                    MINUS
                    SELECT per_id, ks_code, job_code
                    FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                    INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
                WHERE per_id = ? AND job_code = ?)                              ---variables:  per_id(test on 2) , job_code(test on 3)
            WHERE NOT EXISTS( SELECT *
              FROM course_skills C2
              WHERE C2.c_code = Fn AND C2.ks_code = Ks))) AND EXISTS(SELECT per_id
    FROM course NATURAL JOIN course_skills Cs INNER JOIN (SELECT ks_code Ks, per_id
        FROM (SELECT per_id, ks_code, job_code
        FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
        INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
        MINUS
        SELECT per_id, ks_code, job_code
        FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
        INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
        WHERE per_id = ? AND job_code = ?) ON Ks = Cs.ks_code             ---variables:  per_id(test on 2) , job_code(test on 3)
    WHERE NOT EXISTS(SELECT Fn AS c_code, Bn AS course_title
        FROM (SELECT c_code AS Fn, course_title AS Bn FROM course)
            WHERE NOT EXISTS( SELECT *
                FROM (SELECT ks_code AS Ks
                    FROM (SELECT per_id, ks_code, job_code
                        FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                        INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                        MINUS
                        SELECT per_id, ks_code, job_code
                        FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                        INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
                    WHERE per_id = ? AND job_code = ?)                          ---variables:  per_id(test on 2) , job_code(test on 3)
                WHERE NOT EXISTS( SELECT *
                  FROM course_skills C2
                  WHERE C2.c_code = Fn AND C2.ks_code = Ks)))
    GROUP BY per_id
    HAVING COUNT(c_code) <= 3);
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
    WHERE per_id = ?)                                                           ---variables:  per_id(test on 6)                     
--13 END COMMENT, implemented my own solution 



--14 Find the job with the highest pay rate for a person
--according to his/her skill qualification.
SELECT job_code
FROM jobs Jo
WHERE pay_rate = (SELECT MAX(pay_rate)
    FROM jobs Jo
    WHERE NOT EXISTS( SELECT *
        FROM (SELECT Pid
              FROM (SELECT DISTINCT per_id AS Pid, ks_code, job_code
                    FROM person, req_skill Ks
                    MINUS
                    SELECT DISTINCT per_id, ks_code, job_code
                    FROM (person NATURAL JOIN spec_rel Sr), jobs)
              WHERE Pid = ?)                                                    ---variables: per_id(test on 3)
        WHERE NOT EXISTS( SELECT *
          FROM (person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id)
              INNER JOIN req_skill Rs ON Rs.ks_code = Sr.ks_code
          WHERE Jo.job_code = Rs.job_code AND P2.per_id = Pid))) 
AND NOT EXISTS( SELECT *
    FROM (SELECT Pid
          FROM (SELECT DISTINCT per_id AS Pid, ks_code, job_code
                FROM person, req_skill Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE Pid = ?)                                                        ---variables: per_id(test on 3)
    WHERE NOT EXISTS( SELECT *
      FROM (person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id)
          INNER JOIN req_skill Rs ON Rs.ks_code = Sr.ks_code
      WHERE Jo.job_code = Rs.job_code AND P2.per_id = Pid));
--14 END COMMENT, custom solution


--15 List all the names along with the emails of the persons who
--are qualified for a job.
SELECT first_name, last_name, email
FROM person Prsn
WHERE NOT EXISTS(SELECT ks_code AS Ks
         FROM req_skill Ks
         WHERE job_code = ?                                                     ---variables: job_code(test on 1)
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
          WHERE job_code = ?),                                                  ---variables: job_code(test on 3)
      missing_skill (per_id, ks_code) 
      AS(SELECT DISTINCT per_id, ks_code
          FROM person, req_skill
          WHERE job_code = ?                                                    ---variables: job_code(test on 3)
          MINUS
          SELECT per_id, ks_code
          FROM spec_rel)
      SELECT per_id
      FROM missing_skill
      WHERE per_id = Ps.per_id
      GROUP BY per_id
      HAVING COUNT(ks_code) = (SELECT cnt - 1
                              FROM req_cont)) ;
--16 END COMMENT

--17. List the skillID AND the number of people in the missing-
--one list for a given job code in the ascending ORDER of the
--people counts.
SELECT COUNT(Pid)as PidC, ks_code
FROM ((SELECT DISTINCT Pid, ks_code
    FROM req_skill, (SELECT DISTINCT Prsn.per_id AS Pid
      FROM person Prsn
      WHERE EXISTS (SELECT Ps, COUNT(Ks)
          FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
                FROM (SELECT DISTINCT per_id, ks_code, job_code
                      FROM person, req_skill  Ks
                      MINUS
                      SELECT DISTINCT per_id, ks_code, job_code
                      FROM (person NATURAL JOIN spec_rel Sr), jobs)
                WHERE job_code = ?)                                             ---variables: job_code(test on 4)
          WHERE NOT EXISTS( SELECT *
              FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
              WHERE P2.per_id = Prsn.per_id AND Sr.ks_code = Ks)
          GROUP BY Ps
          HAVING COUNT(Ks) = 1))
        WHERE job_code = ?)                                                     ---variables: job_code(test on 4)
    MINUS

    (SELECT DISTINCT Pid, ks_code
    FROM(SELECT DISTINCT Prsn.per_id AS Pid
      FROM person Prsn
      WHERE EXISTS (SELECT Ps, COUNT(Ks)
          FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
                FROM (SELECT DISTINCT per_id, ks_code, job_code
                      FROM person, req_skill  Ks
                      MINUS
                      SELECT DISTINCT per_id, ks_code, job_code
                      FROM (person NATURAL JOIN spec_rel Sr), jobs)
                WHERE job_code = ?)                                             ---variables: job_code(test on 4)
          WHERE NOT EXISTS( SELECT *
              FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
              WHERE P2.per_id = Prsn.per_id AND Sr.ks_code = Ks)
          GROUP BY Ps
          HAVING COUNT(Ks) = 1)) INNER JOIN spec_rel ON Pid = spec_rel.per_id))

GROUP BY ks_code
ORDER BY PidC asc;
--17 END COMMENT

--18. Suppose there is a new job that has nobody qualified. List
--the persons who miss the least number of skills and
--report the ?least number?.

SELECT first_name, last_name, email, MsC AS Missing_skill_count
FROM person Prsn INNER JOIN (SELECT Ps, COUNT(Ks) Msc
    FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = ?)                                                   ---variables: job_code(test on 8)
    WHERE NOT EXISTS( SELECT *
        FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
        WHERE P2.per_id = Ps AND Sr.ks_code = Ks)
    GROUP BY Ps
    HAVING COUNT(Ks) = (SELECT DISTINCT MIN(COUNT(ks_code))
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = ?                                                    ---variables: job_code(test on 8)
          GROUP BY per_id)) ON Prsn.per_id = Ps
WHERE EXISTS (SELECT Ps, COUNT(Ks)
    FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = ?)                                                   ---variables: job_code(test on 8)
    WHERE NOT EXISTS( SELECT *
        FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
        WHERE P2.per_id = Prsn.per_id AND Sr.ks_code = Ks)
    GROUP BY Ps
    HAVING COUNT(Ks) = (SELECT DISTINCT MIN(COUNT(ks_code))
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = ?                                                    ---variables: job_code(test on 8)
          GROUP BY per_id));
--18 END COMMENT

--19. For a specified job category and a given small number k, make a “missing-k”
--list that lists the people’s IDs andthe number of missing skills  for the
--people who miss only up to k skills in the ascending order of missing skills.
WITH missing_skill (per_id, ks_code) 
  AS(SELECT DISTINCT per_id, ks_code
      FROM person, core_skill
      WHERE SOC = ?                                                             ---variables: SOC(test on 1)
      MINUS
      SELECT per_id, ks_code
      FROM spec_rel),
  req_cont (cnt) 
  AS(SELECT MIN(COUNT(ks_code))
      FROM core_skill NATURAL JOIN missing_skill
      WHERE SOC = ?                                                             ---variables: SOC(test on 1)
      GROUP BY per_id)
SELECT per_id, COUNT(ks_code) AS Missing_skill_Ct
FROM missing_skill Ms
WHERE EXISTS(
      SELECT per_id
      FROM missing_skill
      WHERE per_id = Ms.per_id
      GROUP BY per_id
      HAVING COUNT(ks_code) <= ?)                                               ---variables: k(test on 2)
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
      WHERE SOC = ?                                                             ---variables: SOC(test on 1)
      MINUS
      SELECT per_id, ks_code
      FROM spec_rel),
  req_cont (cnt) 
  AS(SELECT MIN(COUNT(ks_code))
      FROM core_skill NATURAL JOIN missing_skill
      WHERE SOC = ?                                                             ---variables: SOC(test on 1)
      GROUP BY per_id)
SELECT ks_code, COUNT(per_id) AS Person_count
FROM missing_skill Ms
WHERE EXISTS(
      SELECT per_id
      FROM missing_skill
      WHERE per_id = Ms.per_id
      GROUP BY per_id
      HAVING COUNT(ks_code) <= ?)                                               ---variables: k(test on 2)
GROUP BY ks_code
ORDER BY Person_count DESC;
--20 END COMMENT


--21. In a local or national crisis, we need to find all the people who once 
--held a job of the special job category identifier.
SELECT DISTINCT per_id
FROM experience NATURAL JOIN jc_rel NATURAL JOIN job_category
WHERE SOC = ?;                                                                  ---variables: SOC(test on 3) 
--21 END COMMENT

--22. Find all the unemployed people who once held a job of the 
--given job identifier.
SELECT per_id
FROM experience
WHERE job_code = ? AND end_date IS NOT NULL;                                    ---variables: job_code(test on 2)
--22 END COMMENT

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
    WHERE SOC = ?),                                                             ---variables: SOC(test on 2) 
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
    SELECT  job_code
    FROM experience
    WHERE end_date IS NULL),
qualified AS
    (SELECT job_code
    FROM open_jobs Oj
    WHERE NOT EXISTS(SELECT ks_code AS Ks
             FROM req_skill Ks
             WHERE job_code = Oj.job_code
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
