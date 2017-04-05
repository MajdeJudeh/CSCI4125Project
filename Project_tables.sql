CREATE TABLE company(
    comp_id INTEGER,
    website VARCHAR(100),
    company_name VARCHAR (50),
    PRIMARY KEY( comp_id)
    );
CREATE TABLE company_contact(
  comp_id INTEGER,
  phone_number INTEGER,
  PRIMARY KEY (phone_number),
  FOREIGN KEY(comp_id) REFERENCES company(comp_id)
  );
CREATE TABLE address(
  zip_code INTEGER,
  street_number INTEGER,
  street_name VARCHAR(20),
  building VARCHAR(20),
  suite_number INTEGER,
  state_abbr CHAR(2),
  city VARCHAR(15),
  comp_id INTEGER,
  PRIMARY KEY( zip_code, street_number, street_name, building, suite_number, state_abbr, city, comp_id),
  FOREIGN KEY (comp_id) REFERENCES company(comp_id)
  );
CREATE TABLE naics(
  code INTEGER,
  sector INTEGER,
  subsector INTEGER,
  industry_group INTEGER,
  naics_industry INTEGER,
  national_industry INTEGER,
  PRIMARY KEY( code)
  );
CREATE TABLE cspec_rel(
  comp_id INTEGER,
  code INTEGER,
  FOREIGN KEY (comp_id) REFERENCES company(comp_id),
  FOREIGN KEY (code) REFERENCES naics(code),
  PRIMARY KEY (comp_id, code)
  );
CREATE TABLE parent_child(
  code INTEGER,
  parent_code INTEGER,
  PRIMARY KEY (code, parent_code),
  FOREIGN KEY (code) REFERENCES naics(code),
  FOREIGN KEY (parent_code) REFERENCES naics(code)
  );
CREATE TABLE jobs(
  job_code INTEGER,
  emp_mode VARCHAR(10),
  pay_rate INTEGER,
  pay_type VARCHAR(10),
  comp_id INTEGER,
  href VARCHAR(200),
  job_title VARCHAR(30),
  city VARCHAR(30),
  state_abbr CHAR(2),
  dateStr DATE,
  PRIMARY KEY (job_code),
  FOREIGN KEY (comp_id) REFERENCES company(comp_id)
  );
CREATE TABLE knowledge_skills(
  ks_code INTEGER,
  skill_title VARCHAR (15),
  description VARCHAR (100),
  skill_level INTEGER,
  PRIMARY KEY (ks_code)
  );
CREATE TABLE req_skill(
  ks_code INTEGER,
  job_code INTEGER,
  FOREIGN KEY (ks_code) REFERENCES knowledge_skills(ks_code),
  FOREIGN KEY (job_code) REFERENCES jobs(job_code)
  );
CREATE TABLE pref_skill(
  ks_code INTEGER,
  job_code INTEGER,
  FOREIGN KEY (ks_code) REFERENCES knowledge_skills(ks_code),
  FOREIGN KEY (job_code) REFERENCES jobs(job_code)
  );
CREATE TABLE job_category(
  soc INTEGER,
  title VARCHAR (20),
  description VARCHAR(100),
  pay_range_high INTEGER,
  pay_range_low INTEGER,
  parent_cate INTEGER,
  PRIMARY KEY (soc),
  FOREIGN KEY (parent_cate) REFERENCES job_category(soc)
  );
CREATE TABLE jc_rel(
  soc INTEGER,
  job_code INTEGER,
  FOREIGN KEY (soc) REFERENCES job_category(soc),
  FOREIGN KEY (job_code) REFERENCES jobs(job_code)
  );
CREATE TABLE core_skill(
  ks_code INTEGER,
  soc INTEGER,
  FOREIGN KEY (ks_code) REFERENCES knowledge_skills(ks_code),
  FOREIGN KEY (soc) REFERENCES job_category(soc)
  );
CREATE TABLE s_tier(
  ks_code INTEGER,
  skill_tier INTEGER,
  FOREIGN KEY (ks_code) REFERENCES knowledge_skills(ks_code)
  );
CREATE TABLE ks_cluster(
  ks_code INTEGER,
  ks_cluster INTEGER,
  FOREIGN KEY (ks_code) REFERENCES knowledge_skills(ks_code)
  );
CREATE TABLE course(
  c_code INTEGER, 
  title VARCHAR(30),
  course_level INTEGER,
  description VARCHAR(100),
  credits INTEGER,
  PRIMARY KEY (c_code)
  );
  
CREATE TABLE course_skills(
  ks_code INTEGER,
  c_code INTEGER,
  FOREIGN KEY (ks_code) REFERENCES knowledge_skills(ks_code),
  FOREIGN KEY (c_code) REFERENCES course(c_code)
  );
CREATE TABLE prereq(
  prereq INTEGER,
  c_code INTEGER,
  FOREIGN KEY (prereq) REFERENCES course(c_code),
  FOREIGN KEY (c_code) REFERENCES course(c_code)
  );
CREATE TABLE section(
  sec_code INTEGER,
  c_code INTEGER,
  sec_no INTEGER,
  semester CHAR (3),
  sec_year INTEGER, 
  complete_date DATE,
  offered_by VARCHAR (20),
  sec_format VARCHAR (10),
  status CHAR (4),
  price INTEGER,
  PRIMARY KEY (sec_code),
  FOREIGN KEY (c_code) REFERENCES course(c_code)
  );
CREATE TABLE person(
  per_id INTEGER,
  first_name VARCHAR (20),
  last_name VARCHAR (20),
  street_name VARCHAR (30),
  street_number INTEGER,
  apt_number INTEGER,
  city VARCHAR (20),
  state_abbr CHAR(2),
  zip_code INTEGER,
  email VARCHAR (60),
  GENDER VARCHAR (10),
  PRIMARY KEY (per_id)
  );
CREATE TABLE takes(
  sec_code INTEGER,
  per_id INTEGER,
  grade CHAR (1),
  PRIMARY KEY (per_id, sec_code,grade),
  FOREIGN KEY (sec_code) REFERENCES section(sec_code),
  FOREIGN KEY (per_id) REFERENCES person(per_id)
  );

CREATE TABLE person_phone(
  per_id INTEGER,
  phone_number INTEGER,
  FOREIGN KEY (per_id) REFERENCES person(per_id)
  );
CREATE TABLE spec_rel(
  per_id INTEGER,
  ks_code INTEGER,
  FOREIGN KEY (per_id) REFERENCES person(per_id),
  FOREIGN KEY (ks_code) REFERENCES knowledge_skills(ks_code)
  );
CREATE TABLE experience(
  per_id INTEGER,
  job_code INTEGER,
  start_date DATE NOT NULL,
  end_date DATE,
  salary INTEGER,
  FOREIGN KEY (per_id) REFERENCES person(per_id),
  FOREIGN KEY (job_code) REFERENCES jobs(job_code)
  );
  
  
  
