insert all 
  into company (comp_id,website,company_name) values(0001,'http://www.utsi.us','Ultimate Technical Solutions')
	into company (comp_id,website,company_name) values(0002,'http://www.gecareers.com', 'General Electric')
	into company (comp_id,website,company_name) values(0003,'http://www.acifed.com/','ACI Federal')
	into company (comp_id,website,company_name) values(0004,'http://www.ibm.com','IBM')
	into company (comp_id,website,company_name) values(0005,'http://www.catapultconsultants.com/careers/','Catapult Consultants, LLC')
	into company (comp_id,website,company_name) values(0006,'http://www.aventuretec.com/careers/Careers.aspx','Aventure Technologies, LLC')
  select * FROM dual;
  
insert all 
  into company_contact (comp_id,phone_number) values(0001,5214864)
  into company_contact (comp_id,phone_number) values(0002,9846235)
  into company_contact (comp_id,phone_number) values(0003,7842605)
  into company_contact (comp_id,phone_number) values(0004,1802350)
  into company_contact (comp_id,phone_number) values(0005,0005148)
  into company_contact (comp_id,phone_number) values(0006,1860248)
  select * FROM dual;
  
insert all 
  into address (zip_code,street_number,street_name,building,suite_number,state_abbr,city,comp_id) values(70121,631,'Road Street','the tall one',305,'LA','New Orleans',0001)
  into address (zip_code,street_number,street_name,building,suite_number,state_abbr,city,comp_id) values(70511,125,'Drive Way','Building name',11,'CA','Sacramento',0002)
  into address (zip_code,street_number,street_name,building,suite_number,state_abbr,city,comp_id) values(89521,3665,'Crab Street','Big Shed',52,'NY','New York',0003)
  into address (zip_code,street_number,street_name,building,suite_number,state_abbr,city,comp_id) values(62104,9856,'Pizza Place','Boat on the lawn',1,'GA','Atlanta',0004)
  into address (zip_code,street_number,street_name,building,suite_number,state_abbr,city,comp_id) values(98623,102,'Outside Parkway','Some Tower',36,'AK','Townsville',0005)
  into address (zip_code,street_number,street_name,building,suite_number,state_abbr,city,comp_id) values(78512,77,'Kangaroo Court','Random Stadium',7452,'IA','Gotham',0006)
  select * FROM dual;
  
insert all  
  into naics (code, sector, subsector, industry_group, naics_industry, national_industry) values(532168,53,2,1,6,8)
  into naics (code, sector, subsector, industry_group, naics_industry, national_industry) values(846034,84,6,0,3,4)
  into naics (code, sector, subsector, industry_group, naics_industry, national_industry) values(845269,84,5,2,6,9)
  into naics (code, sector, subsector, industry_group, naics_industry, national_industry) values(120486,12,0,4,8,6)
  into naics (code, sector, subsector, industry_group, naics_industry, national_industry) values(896245,89,6,2,4,5)
  into naics (code, sector, subsector, industry_group, naics_industry, national_industry) values(120445,12,0,4,4,5)
  select * FROM dual;
  
insert all
  into cspec_rel(comp_id,code) values(0001,532168)
  into cspec_rel(comp_id,code) values(0002,846034)
  into cspec_rel(comp_id,code) values(0003,845269)
  into cspec_rel(comp_id,code) values(0004,120486)
  into cspec_rel(comp_id,code) values(0005,896245)
  into cspec_rel(comp_id,code) values(0006,120445)
  select * FROM dual;

insert all
  into jobs(job_code, emp_mode,pay_rate, pay_type, comp_id, job_title, href, city, state_abbr, dateStr) values (0001, 'Full Time', 60000, 'Salary',0001 , 'Help Desk Technician','https://www.indeed.com/company/Ultimate-Technical-Solutions,-Inc./jobs/Help-Desk-Technician-7e6c820a31c1167d?fccid=564040c6a12a690f', 'New Orleans', 'LA', DATE '2017-02-05')
  into jobs(job_code, emp_mode,pay_rate, pay_type, comp_id, job_title, href, city, state_abbr, dateStr) values (0002, 'Full Time', 40000, 'Salary',0001 , 'Coffee Guy','https://www.indeed.com/company/Ultimate-Technical-Solutions,-Inc./jobs/Executive-Coffee-Maker-7e6c820j9838rw2d?fccid=564040c6a12a690f', 'New Orleans', 'LA', DATE '2016-03-05')
  into jobs(job_code, emp_mode,pay_rate, pay_type, comp_id, job_title, href, city, state_abbr, dateStr) values (0003, 'Internship', 45000, 'Salary',0002 , 'Back-end Developer','https://www.indeed.com/company/GE/jobs/back-end-developer-7e6c820a31c1167d?fccid=564040c6a12a690f', 'Sacramento', 'CA', DATE '2016-02-07')
  into jobs(job_code, emp_mode,pay_rate, pay_type, comp_id, job_title, href, city, state_abbr, dateStr) values (0004, 'Part Time', 60000, 'Hourly',0003 , 'Software Making Guy','https://www.indeed.com/company/ACI-Federal/jobs/Software-Making-Guy-7e6c820a31c1167d?fccid=564040c6a12a690f', 'New York', 'NY', DATE '2017-02-05')
  into jobs(job_code, emp_mode,pay_rate, pay_type, comp_id, job_title, href, city, state_abbr, dateStr) values (0005, 'Full Time', 65000, 'Salary',0004 , 'Software Engineer','https://www.indeed.com/company/Ultimate-Technical-Solutions,-Inc./jobs/Software-Engineer-7e6c820a31c1167d?fccid=564040c6a12a690f', 'Atlanta', 'GA', DATE '2017-04-01')
  into jobs(job_code, emp_mode,pay_rate, pay_type, comp_id, job_title, href, city, state_abbr, dateStr) values (0006, 'Full Time', 72000, 'Salary',0005 , 'Software Developer','https://www.indeed.com/company/Ultimate-Technical-Solutions,-Inc./jobs/Software-Developer-7e6c820a31c1167d?fccid=564040c6a12a690f', 'Townsville', 'AK', DATE '2016-12-29')
  into jobs(job_code, emp_mode,pay_rate, pay_type, comp_id, job_title, href, city, state_abbr, dateStr) values (0007, 'Full Time', 120000, 'Salary',0006 , 'QA Tester','https://www.indeed.com/company/Ultimate-Technical-Solutions,-Inc./jobs/QA-tester-7e6c820a31c1167d?fccid=564040c6a12a690f', 'Gotham', 'IA', DATE '2016-08-02')
  select * FROM dual;
  
insert all
  into  knowledge_skills(ks_code, skill_title,  description,  skill_level) values (0001,'Java','can use the object oriented language, Java', 1)
  into  knowledge_skills(ks_code, skill_title,  description,  skill_level) values (0002,'SQL','can use the database language, SQL', 3)
  into  knowledge_skills(ks_code, skill_title,  description,  skill_level) values (0003,'C++','can use the programming language, C++', 3)
  into  knowledge_skills(ks_code, skill_title,  description,  skill_level) values (0004,'MVC','Familliar with Model View Controller', 2)
  into  knowledge_skills(ks_code, skill_title,  description,  skill_level) values (0005,'QA Testing','testing prgorams', 2)
  into  knowledge_skills(ks_code, skill_title,  description,  skill_level) values (0006,'Java(coffee)','making good coffee', 2)
  into  knowledge_skills(ks_code, skill_title,  description,  skill_level) values (0007,'Java(coffee)','making better coffee', 3)
  select * FROM dual;
  
insert all
  into req_skill(ks_code, job_code) values (0001,0001)
  into req_skill(ks_code, job_code) values (0006,0002)
  into req_skill(ks_code, job_code) values (0002,0003)
  into req_skill(ks_code, job_code) values (0003,0003)
  into req_skill(ks_code, job_code) values (0001,0004)
  into req_skill(ks_code, job_code) values (0001,0005)
  into req_skill(ks_code, job_code) values (0005,0006)
  into req_skill(ks_code, job_code) values (0003,0007)
  into req_skill(ks_code, job_code) values (0003,0006)
  into req_skill(ks_code, job_code) values (0001,0007)
  into req_skill(ks_code, job_code) values (0004,0007)
  into req_skill(ks_code, job_code) values (0005,0003)
  select * FROM dual;
  
insert all
  into pref_skill(ks_code, job_code) values (0004,0001)
  into pref_skill(ks_code, job_code) values (0006,0001)
  into pref_skill(ks_code, job_code) values (0004,0003)
  into pref_skill(ks_code, job_code) values (0005,0003)
  into pref_skill(ks_code, job_code) values (0007,0002)
  into pref_skill(ks_code, job_code) values (0003,0005)
  into pref_skill(ks_code, job_code) values (0005,0004)
  into pref_skill(ks_code, job_code) values (0001,0006)
  into pref_skill(ks_code, job_code) values (0002,0007)
  select * FROM dual;
  
insert all
  into job_category(soc, title, description, pay_range_high, pay_range_low, parent_cate) values (1,'Software Development','programs programs', 70000, 50000, null)
  into job_category(soc, title, description, pay_range_high, pay_range_low, parent_cate) values (2,'Software Engineer','programs programs with math', 90000, 65000, 1)
  into job_category(soc, title, description, pay_range_high, pay_range_low, parent_cate) values (3,'Database Managment','manages databases', 90000, 65000, 1)
  into job_category(soc, title, description, pay_range_high, pay_range_low, parent_cate) values (4,'Coffee Guy','makes cups of java for importent people', 90000, 15000, null)
  into job_category(soc, title, description, pay_range_high, pay_range_low, parent_cate) values (5,'Computer Technician','fixes computers', 70000, 30000, null)
  select * FROM dual;

insert all
  into jc_rel(soc, job_code) values(5,1)
  into jc_rel(soc, job_code) values(4,2)
  into jc_rel(soc, job_code) values(3,3)
  into jc_rel(soc, job_code) values(1,4)
  into jc_rel(soc, job_code) values(2,5)
  into jc_rel(soc, job_code) values(1,6)
  into jc_rel(soc, job_code) values(2,7)
  select * FROM dual;
  
insert all
  into core_skill(ks_code, soc) values(1,1)
  into core_skill(ks_code, soc) values(3,1)
  into core_skill(ks_code, soc) values(1,2)
  into core_skill(ks_code, soc) values(3,2)
  into core_skill(ks_code, soc) values(4,2)
  into core_skill(ks_code, soc) values(5,2)
  into core_skill(ks_code, soc) values(2,3)
  into core_skill(ks_code, soc) values(6,4)
  into core_skill(ks_code, soc) values(7,4)
  into core_skill(ks_code, soc) values(3,5)
  select * FROM dual;
  
insert all
  into s_tier(ks_code, skill_tier) values(1,1)
  into s_tier(ks_code, skill_tier) values(2,3)
  into s_tier(ks_code, skill_tier) values(3,2)
  into s_tier(ks_code, skill_tier) values(4,2)
  into s_tier(ks_code, skill_tier) values(5,2)
  into s_tier(ks_code, skill_tier) values(6,1)
  into s_tier(ks_code, skill_tier) values(7,3)
  select * FROM dual;
  
insert all
  into ks_cluster(ks_code,ks_cluster) values(1,1)
  into ks_cluster(ks_code,ks_cluster) values(2,1)
  into ks_cluster(ks_code,ks_cluster) values(3,1)
  into ks_cluster(ks_code,ks_cluster) values(4,2)
  into ks_cluster(ks_code,ks_cluster) values(5,2)
  into ks_cluster(ks_code,ks_cluster) values(6,3)
  into ks_cluster(ks_code,ks_cluster) values(7,3)
  select * FROM dual;
  
insert all
  into course(c_code, title, course_level, description, credits) values (1,'Intro to Java Programming',2100,'learn to use the java programming language',3)
  into course(c_code, title, course_level, description, credits) values (2,'Intro to C++ Programming',2120,'learn to use the C++ programming language',3)
  into course(c_code, title, course_level, description, credits) values (3,'Intro to SQL Programming',4567,'learn to use the SQL programming language',3)
  into course(c_code, title, course_level, description, credits) values (4,'Data Structures',3100,'learn to use data structures and MVC',3)
  into course(c_code, title, course_level, description, credits) values (5,'Intro to Computers',1000,'learn how to find the power button',3)
  select * FROM dual;
  
insert all
  into course_skills(ks_code,c_code) values(1,1)
  into course_skills(ks_code,c_code) values(2,3)
  into course_skills(ks_code,c_code) values(3,2)
  into course_skills(ks_code,c_code) values(4,4)
  into course_skills(ks_code,c_code) values(1,4)
  into course_skills(ks_code,c_code) values(5,4)
  into course_skills(ks_code,c_code) values(5,1)
  select * FROM dual;
  
insert all
  into prereq(prereq,c_code) values(5,1)
  into prereq(prereq,c_code) values(5,3)
  into prereq(prereq,c_code) values(5,2)
  into prereq(prereq,c_code) values(5,4)
  select * FROM dual;

insert all
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (1, 1,001,'FAL',2016,DATE '2016-12-11','Toups','lecture','FULL',400)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (2, 2,001,'SPR',2016,DATE '2016-05-17','Toups','lecture','FULL',400)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (3, 4,001,'FAL',2015,DATE '2015-12-12','Summa','lecture','FULL',500)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (4, 3,001,'SPR',2016,DATE '2016-05-17','Tu','lecture','FULL',500)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (5, 2,001,'SPR',2015,DATE '2015-05-14','Toups','lecture','FULL',400)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (6, 3,001,'FAL',2016,DATE '2016-12-11','Tu','lecture','OPEN',500)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (7, 2,001,'SUM',2016,DATE '2016-07-23','Toups','lecture','OPEN',400)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (8, 5,001,'FAL',2014,DATE '2014-12-10','Staff','lecture','FULL',200)
  into section(sec_code, c_code,sec_no, semester, sec_year, complete_date, offered_by, sec_format, status, price) values (9, 1,001,'SUM',2017,DATE '2017-07-25','Toups','lecture','OPEN',400)
  select * FROM dual;

insert all
  into person(per_id, first_name, last_name, street_name, street_number, apt_number, city, state_abbr, zip_code, email, GENDER) values(1,'Samwise', 'Gamgee', 'dirt road', 12, null, 'The Shire', null,null,'SamWG@gmail.com','male')
  into person(per_id, first_name, last_name, street_name, street_number, apt_number, city, state_abbr, zip_code, email, GENDER) values(2,'Frodo', 'Baggins', 'other dirt road', 32, null, 'The Shire', null,null,'FrodoB@yahoo.com','male')
  into person(per_id, first_name, last_name, street_name, street_number, apt_number, city, state_abbr, zip_code, email, GENDER) values(3,'Amanda', 'Smith', 'Pleasant Drive', 121, 5, 'Citysburg', 'CA', 54126,'ASmith@msn.com','female')
  into person(per_id, first_name, last_name, street_name, street_number, apt_number, city, state_abbr, zip_code, email, GENDER) values(4,'Greg', 'Jackson', 'Windy Road', 861, 42, 'Springfield', 'MS',84562,'Dogs.R.awesome47@ymail.com','male')
  into person(per_id, first_name, last_name, street_name, street_number, apt_number, city, state_abbr, zip_code, email, GENDER) values(5,'Eliza', 'Burger', 'Coca Cola Parkway', 784, 1452, 'Sodatown', 'IA',78462,null,'female')
  into person(per_id, first_name, last_name, street_name, street_number, apt_number, city, state_abbr, zip_code, email, GENDER) values(6,'Frank', 'Johnson', 'Park Way', 112, null, 'New City Name', 'AL', 47815,'JeeMail@gmail.com','male')
  into person(per_id, first_name, last_name, street_name, street_number, apt_number, city, state_abbr, zip_code, email, GENDER) values(7,'Seth', 'Eman', 'Long Road',71784, null, 'Middle of Nowhere', 'FL',71154,null,'male')
  select * FROM dual;  
  
insert all
  into takes(sec_code,  per_id,  grade) values(8,1,'C')
  into takes(sec_code,  per_id,  grade) values(3,1,'B')
  into takes(sec_code,  per_id,  grade) values(7,1,'A')
  into takes(sec_code,  per_id,  grade) values(9,1,'I')
  into takes(sec_code,  per_id,  grade) values(8,2,'B')
  into takes(sec_code,  per_id,  grade) values(7,2,'B')
  into takes(sec_code,  per_id,  grade) values(9,2,'I')
  into takes(sec_code,  per_id,  grade) values(3,2,'C')
  into takes(sec_code,  per_id,  grade) values(8,3,'A')
  into takes(sec_code,  per_id,  grade) values(2,3,'A')
  into takes(sec_code,  per_id,  grade) values(4,3,'B')
  into takes(sec_code,  per_id,  grade) values(8,4,'F')
  into takes(sec_code,  per_id,  grade) values(8,4,'D')
  into takes(sec_code,  per_id,  grade) values(8,5,'B')
  into takes(sec_code,  per_id,  grade) values(9,5,'I')
  into takes(sec_code,  per_id,  grade) values(2,5,'A')
  into takes(sec_code,  per_id,  grade) values(4,5,'A')
  into takes(sec_code,  per_id,  grade) values(8,6,'A')
  into takes(sec_code,  per_id,  grade) values(1,6,'B')
  into takes(sec_code,  per_id,  grade) values(3,6,'A')
  into takes(sec_code,  per_id,  grade) values(5,6,'C')
  into takes(sec_code,  per_id,  grade) values(6,6,'A')
  into takes(sec_code,  per_id,  grade) values(8,7,'A')
  into takes(sec_code,  per_id,  grade) values(5,7,'B')
  into takes(sec_code,  per_id,  grade) values(4,7,'C')
  into takes(sec_code,  per_id,  grade) values(3,7,'A')
  into takes(sec_code,  per_id,  grade) values(1,7,'A')
  select * FROM dual; 
  
insert all
  into person_phone(per_id, phone_number) values (1,5124962)
  into person_phone(per_id, phone_number) values (2,null)
  into person_phone(per_id, phone_number) values (3,9438715)
  into person_phone(per_id, phone_number) values (4,8495210)
  into person_phone(per_id, phone_number) values (4,8459721)
  into person_phone(per_id, phone_number) values (5,9401530)
  into person_phone(per_id, phone_number) values (6,8475012)
  into person_phone(per_id, phone_number) values (1,9412501)
  into person_phone(per_id, phone_number) values (7,8291410)
  select * FROM dual; 
  
insert all
  into spec_rel(per_id,ks_code) values(1,6)
  into spec_rel(per_id,ks_code) values(1,2)
  into spec_rel(per_id,ks_code) values(1,3)
  into spec_rel(per_id,ks_code) values(1,4)
  into spec_rel(per_id,ks_code) values(2,1)
  into spec_rel(per_id,ks_code) values(2,4)
  into spec_rel(per_id,ks_code) values(2,3)
  into spec_rel(per_id,ks_code) values(2,7)
  into spec_rel(per_id,ks_code) values(3,2)
  into spec_rel(per_id,ks_code) values(3,3)
  into spec_rel(per_id,ks_code) values(4,6)
  into spec_rel(per_id,ks_code) values(4,7)
  into spec_rel(per_id,ks_code) values(5,3)  
  into spec_rel(per_id,ks_code) values(5,2)
  into spec_rel(per_id,ks_code) values(6,1)
  into spec_rel(per_id,ks_code) values(6,4)
  into spec_rel(per_id,ks_code) values(6,3)
  into spec_rel(per_id,ks_code) values(6,2) 
  into spec_rel(per_id,ks_code) values(7,4) 
  into spec_rel(per_id,ks_code) values(7,1) 
  into spec_rel(per_id,ks_code) values(7,3) 
  into spec_rel(per_id,ks_code) values(7,2) 
  into spec_rel(per_id,ks_code) values(7,5) 
  into spec_rel(per_id,ks_code) values(7,6) 
  select * FROM dual; 
  
  
insert all
  into experience(per_id,job_code,start_date,end_date,salary) values(1,3,DATE '2016-04-01',null,39000)
  into experience(per_id,job_code,start_date,end_date,salary) values(4,2,DATE '2016-03-27',DATE '2016-04-05',3000)
  into experience(per_id,job_code,start_date,end_date,salary) values(4,2,DATE '2016-04-05',null,40000)
  into experience(per_id,job_code,start_date,end_date,salary) values(6,3,DATE '2016-04-04',null,68000) 
  into experience(per_id,job_code,start_date,end_date,salary) values(7,3,DATE '2016-04-05',DATE '2016-08-05',50000) 
  into experience(per_id,job_code,start_date,end_date,salary) values(7,7,DATE '2016-08-07',null, 120000) 
  select * FROM dual; 
