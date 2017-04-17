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
WHERE Ex.end_date IS NULL ;
--1 corrected to find current workers

-- 2. List a company's staff BY salary in descending ORDER.
SELECT first_name, last_name, salary
FROM (jobs NATURAL JOIN experience Ex) INNER JOIN person Ps ON Ex.per_id = Ps.per_id
WHERE comp_id = 002 AND end_date IS NULL                                          --- variables: comp_id
ORDER BY salary DESC;
--2 corrected to find current workers

--3. List companies' labor cost (total salaries and wage rates BY 1920 hours) in descending ORDER.
SELECT DISTINCT comp_id, company_name, SUM (salary) AS CompanySalary
FROM company NATURAL JOIN jobs NATURAL JOIN experience
WHERE end_date IS NULL
GROUP BY comp_id, company_name
ORDER BY CompanySalary DESC;
--3 added jobs to link company and experience

--4. Find all the jobs a person is currently holding and worked --in the past.
SELECT job_code, start_date, end_date, salary, emp_mode, pay_rate, pay_type, company_name, city, state_abbr
FROM experience NATURAL JOIN jobs NATURAL JOIN company
WHERE per_id = 0007;                                                               --- variables: per_id
--4 table inforces that start_date canNOT be null

--5. List a person's knowledge/skills in a readable format.
SELECT first_name, last_name, skill_title AS skill
FROM knowledge_skills NATURAL JOIN spec_rel NATURAL JOIN person
WHERE per_id = 0001;                                                               --- variables: per_id
--5 changed ORDER to accomodate linking


--6. List the skill gap of a worker between his/her jobs(s) and --his/her skills.
SELECT first_name, last_name, job_title, skill_title AS skill_lacked
FROM (SELECT DISTINCT per_id, ks_code
      FROM  req_skill NATURAL JOIN experience
      MINUS
      SELECT DISTINCT per_id, ks_code
      FROM spec_rel NATURAL JOIN experience) NATURAL JOIN person NATURAL JOIN experience Ex INNER JOIN jobs Jo ON Ex.job_code = Jo.job_code NATURAL JOIN knowledge_skills
WHERE per_id = 0006;                                                               --- variables: per_id
--6 I don't understand this. no changes made

--7. List the required knowledge/skills of a job/ a job category --in a readable format. (two queries)
SELECT job_title, skill_title AS requireed_skill
FROM  jobs NATURAL JOIN req_skill NATURAL JOIN knowledge_skills
ORDER BY job_code ASC;

SELECT Jc.category_title AS job_category, skill_title AS required_skill
FROM (job_category Jc NATURAL JOIN core_skill Cs) INNER JOIN knowledge_skills Kn ON Cs.ks_code = Kn.ks_code
ORDER BY soc ASC;
--7 compatability fixed, and readability improved

--8 List a person's missing knowledge/skills for a specific job in a readable format.
SELECT first_name, last_name, skill_title AS skill_lacked, Job_title, company_name
FROM (SELECT per_id, first_name, last_name, job_code, skill_title, job_title, company_name
      FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
      INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
      MINUS
      SELECT per_id, first_name, last_name, job_code, skill_title, job_title, company_name
      FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
      INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
WHERE per_id = 0003 AND job_code = 0002;                                        ---variables:  per_id , job_code
--8 creates a pool of all relevant info for all people, associated with all jobs, then subtracts skills that people do have,
-- resulting in all relevant info for skills people lack for
--jobs. Then, filters down to the specified person and job,
-- resulting in all relevant info for skills the specified
--person lacks, for the specified job.

--9 List the courses(course id and title) that each alone
--teaches all the missing knowledge/skills for a person to
--pursue a specific job.
SELECT Fn AS c_code, Bn AS course_title
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
          WHERE per_id = 3 AND job_code = 3)                                    ---variables:  per_id , job_code
    WHERE NOT EXISTS( SELECT *
      FROM course_skills C2
      WHERE C2.c_code = Fn AND C2.ks_code = Ks));
--9 END COMMENT

--10 Suppose the skill gap of a worker and the requirement of a --desired job can be covered BY ONe course. Find the
--   ?quickest? solution for this worker. Show the course,
--section information and the completion date

SELECT Fn AS c_code, Bn AS course_title
FROM (SELECT cors.c_code AS Fn, course_title AS Bn, complete_date
    FROM course cors INNER JOIN section ON section.c_code = cors.c_code) INNER JOIN (SELECT c_code AS secnc, MIN(complete_date) AS min_cmplt
       FROM section
       GROUP BY c_code) ON Fn = secnc AND complete_date = min_cmplt
WHERE NOT EXISTS( SELECT *
    FROM (SELECT ks_code AS Ks
          FROM (SELECT per_id, ks_code, job_code
                FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                MINUS
                SELECT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
          WHERE per_id = 3 AND job_code = 3)                                    ---variables:  per_id , job_code
    WHERE NOT EXISTS( SELECT *
    FROM course_skills C2
    WHERE C2.c_code = Fn AND C2.ks_code = Ks));
--10 END COMMENT

--11 Find the cheapest course to make up ONe?s skill gap BY
--showing the course to take and the cost (of the section
--price).
SELECT DISTINCT Fn AS c_code, Bn AS course_title, Sec.price
FROM (SELECT c_code AS Fn, course_title AS Bn FROM course) INNER JOIN section Sec ON Fn = Sec.c_code
WHERE NOT EXISTS( SELECT *
    FROM (SELECT ks_code AS Ks
          FROM (SELECT per_id, ks_code, job_code
                FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                MINUS
                SELECT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo NATURAL JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
          WHERE per_id = 5 AND job_code = 3)                                    ---variables:  per_id , job_code
    WHERE NOT EXISTS( SELECT *
      FROM course_skills C2
      WHERE C2.c_code = Fn AND C2.ks_code = Ks));
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
    WHERE per_id = 0002 AND job_code = 0003) ON Ks = Cs.ks_code                 ---variables:  per_id , job_code
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
                WHERE per_id = 2 AND job_code = 3)                              ---variables:  per_id , job_code
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
        WHERE per_id = 0002 AND job_code = 0003) ON Ks = Cs.ks_code             ---variables:  per_id , job_code
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
                    WHERE per_id = 2 AND job_code = 3)                          ---variables:  per_id , job_code
                WHERE NOT EXISTS( SELECT *
                  FROM course_skills C2
                  WHERE C2.c_code = Fn AND C2.ks_code = Ks)))
    GROUP BY per_id
    HAVING COUNT(c_code) <= 3);
--12 It works, but 21 SELECT statements... there's probably a
--more efficient way to do this
--my own implementation, date complete: 4/7/2017, BONUS POINTS

--13 List all the job categories that a person is qualified for.
SELECT first_name, last_name, email
FROM job_category JCs
WHERE NOT EXISTS( SELECT SOC AS SOCs
    FROM (SELECT DISTINCT per_id, ks_code, SOC
        FROM person, core_skill
        MINUS
        SELECT DISTINCT per_id, ks_code, SOC
        FROM (person NATURAL JOIN spec_rel Sr), job_category)
    WHERE per_id = 1 AND NOT EXISTS(SELECT *
      FROM core_skill Cs INNER JOIN job_category J2 ON Cs.SOC = J2.SOC
      WHERE JCs.SOC = J2.SOC AND Cs.SOC = SOCs));                                                           ---variables: per_id
--13 END COMMENT, implemented my own solution 

--14 Find the job with the highest pay rate for a person
--according to his/her skill qualification.
--skipped for now

--15 List all the names along with the emails of the persons who
--are qualified for a job.
SELECT first_name, last_name, email
FROM person Prsn
WHERE NOT EXISTS( SELECT *
    FROM (SELECT ks_code AS Ks
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 2)                                                   ---variables: job_code
    WHERE NOT EXISTS( SELECT *
      FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
      WHERE P2.per_id = Prsn.per_id AND Sr.ks_code = Ks));
--15 END COMMENT

--16. When a company canNOT find any qualified person for a job,
--a secondary solution is to find a person who is almost
--qualified to the job. Make a ?missing-one? list that lists
--people who miss ONly ONe skill for a specified job.
SELECT first_name, last_name, email
FROM person Prsn
WHERE EXISTS (SELECT Ps, COUNT(Ks)
    FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 2)                                                   ---variables: job_code
    WHERE NOT EXISTS( SELECT *
        FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
        WHERE P2.per_id = Prsn.per_id AND Sr.ks_code = Ks)
    GROUP BY Ps
    HAVING COUNT(Ks) = 1);
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
                WHERE job_code = 4)                                             ---variables: job_code
          WHERE NOT EXISTS( SELECT *
              FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
              WHERE P2.per_id = Prsn.per_id AND Sr.ks_code = Ks)
          GROUP BY Ps
          HAVING COUNT(Ks) = 1))
        WHERE job_code = 4)                                                     ---variables: job_code
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
                WHERE job_code = 4)                                             ---variables: job_code
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
          WHERE job_code = 8)                                                   ---variables: job_code
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
          WHERE job_code = 8                                                    ---variables: job_code
          GROUP BY per_id)) ON Prsn.per_id = Ps
WHERE EXISTS (SELECT Ps, COUNT(Ks)
    FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 8)                                                   ---variables: job_code
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
          WHERE job_code = 8                                                    ---variables: job_code
          GROUP BY per_id));
--18 END COMMENT

--19. For a specified job category and a given small number k, make a “missing-k”
--list that lists the people’s IDs andthe number of missing skills  for the
--people who miss only up to k skills in the ascending order of missing skills.
SELECT first_name, last_name, email, Jo.job_code
FROM (jobs Jo INNER JOIN JC_rel Jc ON Jo.job_code = Jc.job_code), person Prsn
WHERE  JC.SOC = 1 AND EXISTS (SELECT Ps, COUNT(Ks)                              ---variables: SOC
    FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = Jo.Job_code)
    WHERE NOT EXISTS( SELECT *
        FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
        WHERE P2.per_id = Prsn.per_id AND Sr.ks_code = Ks)
    GROUP BY Ps
    HAVING COUNT(Ks) = 1)                                                      ---variables: k
ORDER BY Jo.job_code;
--19 END COMMENT

--20. Given a job category code and its corresponding missing-k list specified
--in Question 19. Find every skill that is needed by at least one person in the
--given missing-k list. List each skillID and the number of people who need it
--in the descending order of the people counts
SELECT count(distinct Prsn) AS Person_Count, M_Ks AS Missing_Skill
FROM ((jobs Jo INNER JOIN JC_rel Jc ON Jo.job_code = Jc.job_code)INNER JOIN
    (SELECT DISTINCT per_id Prsn, ks_code AS M_Ks, job_code AS Jcode
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs))
    ON Jo.job_code = Jcode)
WHERE  JC.SOC = 1 AND EXISTS (SELECT Ps, COUNT(Ks)                              ---variables: SOC
    FROM (SELECT DISTINCT per_id Ps, ks_code AS Ks
          FROM (SELECT DISTINCT per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT DISTINCT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = Jo.Job_code)
    WHERE NOT EXISTS( SELECT *
        FROM person P2 INNER JOIN spec_rel Sr ON P2.per_id = Sr.per_id
        WHERE P2.per_id = Prsn AND Sr.ks_code = Ks)
    GROUP BY Ps
    HAVING COUNT(Ks) = 1)                                                      ---variables: k
GROUP BY M_Ks
ORDER BY Person_Count DESC;
--20 END COMMENT
