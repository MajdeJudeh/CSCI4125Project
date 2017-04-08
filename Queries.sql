/*
 * all variables are denoted by a comment starting with "---variables:" at the end of the line
 */
 
-- 1. List a company's workers by names.
SELECT First_name, Last_name--, first_name, last_name
FROM (jobs Natural Join experience Ex)INNER JOIN person Ps on Ex.per_id = Ps.per_id
WHERE Ex.end_date is null ;
--1 corrected to find current workers

-- 2. List a company's staff by salary in descending order.
SELECT first_name, last_name, salary
FROM (jobs NATURAL JOIN experience Ex) INNER JOIN person Ps on Ex.per_id = Ps.per_id
WHERE comp_id = 002 and end_date is null                                          --- variables: comp_id
ORDER BY salary DESC;
--2 corrected to find current workers

--3. List companies' labor cost (total salaries and wage rates by 1920 hours) in descending order.
SELECT distinct comp_id, company_name, SUM (salary) AS CompanySalary
FROM company NATURAL JOIN jobs NATURAL JOIN experience
WHERE end_date is NULL
GROUP BY comp_id, company_name
ORDER BY CompanySalary DESC;
--3 added jobs to link company and experience

--4. Find all the jobs a person is currently holding and worked in the past.
SELECT job_code, start_date, end_date, salary, emp_mode, pay_rate, pay_type, company_name, city, state_abbr
FROM experience Natural Join jobs natural join company
WHERE per_id = 0007;                                                               --- variables: per_id
--4 table inforces that start_date cannot be null

--5. List a person's knowledge/skills in a readable format. 
SELECT first_name, last_name, skill_title as skill
FROM knowledge_skills NATURAL JOIN spec_rel NATURAL JOIN person
WHERE per_id = 0001;                                                               --- variables: per_id
--5 changed order to accomodate linking


--6. List the skill gap of a worker between his/her jobs(s) and his/her skills.
select first_name, last_name, job_title, skill_title as skill_lacked
from (SELECT distinct per_id, ks_code 
      FROM  req_skill natural join experience
      MINUS
      SELECT distinct per_id, ks_code 
      FROM spec_rel natural join experience) natural join person natural join experience Ex inner join jobs Jo on Ex.job_code = Jo.job_code  natural join knowledge_skills
WHERE per_id = 0006;                                                               --- variables: per_id
--6 I don't understand this. no changes made    
    
--7. List the required knowledge/skills of a job/ a job category in a readable format. (two queries)
SELECT job_title, skill_title as requireed_skill
FROM  jobs  NATURAL JOIN req_skill NATURAL JOIN knowledge_skills
ORDER BY job_code ASC;

SELECT Jc.category_title as job_category, skill_title as required_skill
FROM (job_category Jc NATURAL JOIN core_skill Cs) INNER JOIN knowledge_skills Kn ON Cs.ks_code = Kn.ks_code
ORDER BY soc ASC;
--7 compatability fixed, and readability improved

--8 List a person's missing knowledge/skills for a specific job in a readable format.
SELECT first_name, last_name, skill_title as skill_lacked, Job_title, company_name 
FROM (SELECT per_id, first_name, last_name, job_code, skill_title, job_title, company_name
      FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
      INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
      MINUS
      SELECT per_id, first_name, last_name, job_code, skill_title, job_title, company_name
      FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
      INNER JOIN job_category Jc ON Jc.soc = Jr.soc))
WHERE per_id = 0003 and job_code = 0002;                                        ---variables:  per_id , job_code
--8 creates a pool of all relevant info for all people, associated with all jobs, then subtracts skills that people do have,
-- resulting in all relevant info for skills people lack for jobs. Then, filters down to the specified person and job, 
-- resulting in all relevant info for skills the specified person lacks, for the specified job.
      
--9 List the courses(course id and title) that each alone teaches all the missing knowledge/skills for a person to pursue a specific job. 
select Fn as c_code, Bn as course_title
from (select c_code as Fn, course_title as Bn from course)
where not exists( select *
    from (SELECT ks_code as Ks
          FROM (SELECT per_id, ks_code, job_code
                FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                MINUS
                SELECT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) 
          WHERE per_id = 3 and job_code = 3)                                    ---variables:  per_id , job_code 
    where not exists( select *
      from course_skills C2
      where C2.c_code = Fn and C2.ks_code = Ks)); 
--9 END COMMENT

--10 Suppose the skill gap of a worker and the requirement of a desired job can be covered by one course. Find the
--   “quickest” solution for this worker. Show the course, section information and the completion date

select Fn as c_code, Bn as course_title
from (select cors.c_code as Fn, course_title as Bn, complete_date 
    from course cors inner join section on section.c_code = cors.c_code) inner join (select c_code as secnc, min(complete_date) as min_cmplt
       from section
       group by c_code) on Fn = secnc and complete_date = min_cmplt
where not exists( select *
    from (SELECT ks_code as Ks
          FROM (SELECT per_id, ks_code, job_code
                FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                MINUS
                SELECT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) 
          WHERE per_id = 3 and job_code = 3)                                    ---variables:  per_id , job_code 
    where not exists( select *
    from course_skills C2
    where C2.c_code = Fn and C2.ks_code = Ks)); 
--10 END COMMENT

--11 Find the cheapest course to make up one’s skill gap by showing the course to take and the cost (of the section price).
select distinct Fn as c_code, Bn as course_title, Sec.price
from (select c_code as Fn, course_title as Bn from course) inner join section Sec on Fn = Sec.c_code
where not exists( select *
    from (SELECT ks_code as Ks
          FROM (SELECT per_id, ks_code, job_code
                FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                MINUS
                SELECT per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) 
          WHERE per_id = 5 and job_code = 3)                                    ---variables:  per_id , job_code 
    where not exists( select *
      from course_skills C2
      where C2.c_code = Fn and C2.ks_code = Ks));
--11 END COMMENT

--12 If query #9 returns nothing, then find the course sets that their combination covers all the missing knowledge/
-- skills for a person to pursue a specific job. The considered course sets will not include more than three courses.
-- If multiple course sets are found, list the course sets (with their course IDs) in the order of the ascending order of
-- the course sets’ total costs.
select c_code, course_title
from course natural join course_skills Cs inner join (SELECT ks_code Ks, per_id
    FROM (SELECT per_id, ks_code, job_code
    FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
    INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
    MINUS
    SELECT per_id, ks_code, job_code
    FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
    INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) 
    WHERE per_id = 0002 and job_code = 0003) on Ks = Cs.ks_code                 ---variables:  per_id , job_code 
where not exists(select Fn as c_code, Bn as course_title
    from (select c_code as Fn, course_title as Bn from course)
        where not exists( select *
            from (SELECT ks_code as Ks
                FROM (SELECT per_id, ks_code, job_code
                    FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                    INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                    MINUS
                    SELECT per_id, ks_code, job_code
                    FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                    INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) 
                WHERE per_id = 2 and job_code = 3)                              ---variables:  per_id , job_code 
            where not exists( select *
              from course_skills C2
              where C2.c_code = Fn and C2.ks_code = Ks))) and exists(select per_id
    from course natural join course_skills Cs inner join (SELECT ks_code Ks, per_id
        FROM (SELECT per_id, ks_code, job_code
        FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
        INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
        MINUS
        SELECT per_id, ks_code, job_code
        FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
        INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) 
        WHERE per_id = 0002 and job_code = 0003) on Ks = Cs.ks_code             ---variables:  per_id , job_code
    where not exists(select Fn as c_code, Bn as course_title
        from (select c_code as Fn, course_title as Bn from course)
            where not exists( select *
                from (SELECT ks_code as Ks
                    FROM (SELECT per_id, ks_code, job_code
                        FROM person, ((knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                        INNER JOIN job_category Jc ON Jc.soc = Jr.soc)
                        MINUS
                        SELECT per_id, ks_code, job_code
                        FROM (person NATURAL JOIN spec_rel NATURAL JOIN knowledge_skills Ks), ((jobs Jo Natural JOIN company NATURAL JOIN jc_rel Jr)
                        INNER JOIN job_category Jc ON Jc.soc = Jr.soc)) 
                    WHERE per_id = 2 and job_code = 3)                          ---variables:  per_id , job_code
                where not exists( select *
                  from course_skills C2
                  where C2.c_code = Fn and C2.ks_code = Ks)))
    group by per_id
    having count(c_code) <= 3);
--12 It works, but 21 select statements... there's probably a more efficient way to do this
--my own implementation, date complete: 4/7/2017, BONUS POINTS ^^

--13 List all the job categories that a person is qualified for.
--skipped for now

--14 Find the job with the highest pay rate for a person according to his/her skill qualification.
--skipped for now

--15 List all the names along with the emails of the persons who are qualified for a job.     
select first_name, last_name, email
from person Prsn
where not exists( select *
    from (SELECT ks_code as Ks
          FROM (SELECT distinct per_id, ks_code, job_code
                FROM person, req_skill Ks
                MINUS
                SELECT distinct per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 2)                                                   ---variables: job_code 
    where not exists( select *
      from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id
      where P2.per_id = Prsn.per_id and Sr.ks_code = Ks)); 
--15 END COMMENT

--16. When a company cannot find any qualified person for a job, a secondary solution is to find a person who is almost
--qualified to the job. Make a “missing-one” list that lists people who miss only one skill for a specified job.
select first_name, last_name, email
from person Prsn
where exists (select Ps, count(Ks)
    from (SELECT distinct per_id Ps, ks_code as Ks
          FROM (SELECT distinct per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT distinct per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 2)                                                   ---variables: job_code 
    where not exists( select *
        from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id
        where P2.per_id = Prsn.per_id and Sr.ks_code = Ks)
    group by Ps
    having count(Ks) = 1);
--16 END COMMENT

--17. List the skillID and the number of people in the missing-one list for a given job code in the ascending order of the
--people counts.

select count(Pid)as PidC, ks_code
from ((select distinct Pid, ks_code
    from req_skill, (select distinct Prsn.per_id as Pid
      from person Prsn
      where exists (select Ps, count(Ks)
          from (SELECT distinct per_id Ps, ks_code as Ks
                FROM (SELECT distinct per_id, ks_code, job_code
                      FROM person, req_skill  Ks
                      MINUS
                      SELECT distinct per_id, ks_code, job_code
                      FROM (person NATURAL JOIN spec_rel Sr), jobs)
                WHERE job_code = 4)                                             ---variables: job_code 
          where not exists( select *
              from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id
              where P2.per_id = Prsn.per_id and Sr.ks_code = Ks)
          group by Ps
          having count(Ks) = 1))
        where job_code = 4)                                                     ---variables: job_code 
    minus

    (select distinct Pid, ks_code 
    from(select distinct Prsn.per_id as Pid
      from person Prsn
      where exists (select Ps, count(Ks)
          from (SELECT distinct per_id Ps, ks_code as Ks
                FROM (SELECT distinct per_id, ks_code, job_code
                      FROM person, req_skill  Ks
                      MINUS
                      SELECT distinct per_id, ks_code, job_code
                      FROM (person NATURAL JOIN spec_rel Sr), jobs)
                WHERE job_code = 4)                                             ---variables: job_code 
          where not exists( select *
              from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id
              where P2.per_id = Prsn.per_id and Sr.ks_code = Ks)
          group by Ps
          having count(Ks) = 1)) inner join spec_rel on Pid = spec_rel.per_id))
           
group by ks_code
order by PidC asc; 
--17 END COMMENT

--18. Suppose there is a new job that has nobody qualified. List the persons who miss the least number of skills and
--report the “least number”. 

select first_name, last_name, email, MsC as Missing_skill_count
from person Prsn inner join (select Ps, count(Ks) Msc
    from (SELECT distinct per_id Ps, ks_code as Ks
          FROM (SELECT distinct per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT distinct per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 8)                                                   ---variables: job_code 
    where not exists( select *
        from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id
        where P2.per_id = Ps and Sr.ks_code = Ks)
    group by Ps
    having count(Ks) = (SELECT distinct min(count(ks_code))
          FROM (SELECT distinct per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT distinct per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 8                                                    ---variables: job_code 
          group by per_id)) on Prsn.per_id = Ps
where exists (select Ps, count(Ks)
    from (SELECT distinct per_id Ps, ks_code as Ks
          FROM (SELECT distinct per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT distinct per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 8)                                                   ---variables: job_code 
    where not exists( select *
        from person P2 inner join spec_rel Sr on P2.per_id = Sr.per_id
        where P2.per_id = Prsn.per_id and Sr.ks_code = Ks)
    group by Ps
    having count(Ks) = (SELECT distinct min(count(ks_code))
          FROM (SELECT distinct per_id, ks_code, job_code
                FROM person, req_skill  Ks
                MINUS
                SELECT distinct per_id, ks_code, job_code
                FROM (person NATURAL JOIN spec_rel Sr), jobs)
          WHERE job_code = 8                                                    ---variables: job_code 
          group by per_id)); 
--18 END COMMENT
