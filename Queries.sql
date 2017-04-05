-- 1. List a company's workers by names.
SELECT First_name, Last_name--, first_name, last_name
FROM (jobs Natural Join experience Ex)INNER JOIN person Ps on Ex.per_id = Ps.per_id
WHERE Ex.end_date is null ;
--1 corrected to find current workers

-- 2. List a company's staff by salary in descending order.
SELECT first_name, last_name, salary
FROM (jobs NATURAL JOIN experience Ex) INNER JOIN person Ps on Ex.per_id = Ps.per_id
WHERE comp_id = 002 and end_date is null
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
WHERE per_id = 0007;
--4 table inforces that start_date cannot be null

--5. List a person's knowledge/skills in a readable format. 
SELECT first_name, last_name, skill_title as skill
FROM knowledge_skills NATURAL JOIN spec_rel NATURAL JOIN person
WHERE per_id = 0001;
--5 changed order to accomodate linking

--6. List the skill gap of a worker between his/her jobs(s) and his/her skills.
select first_name, last_name, job_title, skill_title as skill_lacked
from (SELECT per_id, first_name, last_name, skill_title, job_title, company_name
      FROM person Natural Join Experience natural join jobs natural join company natural join req_skill natural join knowledge_skills Ks NATURAL JOIN req_skill NATURAL JOIN jobs Jo Natural JOIN company)
      MINUS
      SELECT per_id, first_name, last_name, job_code, skill_title, job_title, company_name
      FROM (person Pe Inner join spec_rel Sr  on  Pe.per_id = Sr.per_id )NATURAL JOIN knowledge_skills Ks natural join experience natural join jobs Jo Natural JOIN company)     
WHERE per_id = 0002;


WITH person_skills (per_id, first_name, last_name, ks_code, skill_title) AS
      ( SELECT per_id, first_name, last_name, ks_code, skill_title
        FROM spec_rel NATURAL JOIN knowledge_skills NATURAL JOIN person
        WHERE per_id = 0001)
  ( SELECT per_id, first_name, last_name, job_code, ks_code, skill_title
    FROM jc_rel NATURAL JOIN jobs NATURAL JOIN job_category 
                NATURAL JOIN req_skill NATURAL JOIN experience
                NATURAL JOIN person NATURAL JOIN spec_rel
                NATURAL JOIN knowledge_skills
    WHERE per_id = 0001) 
  MINUS
  ( SELECT person_skills.per_id, person_skills.first_name, person_skills.last_name, experience.job_code, person_skills.ks_code, person_skills.skill_title
    FROM experience JOIN person_skills ON person_skills.per_id=experience.per_id);
--6 I don't understand this. no changes made    
    
--7. List the required knowledge/skills of a job/ a job category in a readable format. (two queries)
SELECT job_title, skill_title as requireed_skill
FROM  jobs  NATURAL JOIN req_skill NATURAL JOIN knowledge_skills
ORDER BY job_code ASC;

SELECT Jc.title as job_category, skill_title as required_skill
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
WHERE per_id = 0003 and job_code = 0002;
--8 creates a pool of all relevant info for all people, associated with all jobs, then subtracts skills that people do have,
-- resulting in all relevant info for skills people lack for jobs. Then, filters down to the specified person and job, 
-- resulting in all relevant info for skills the specified person lacks, for the specified job.
      

                
