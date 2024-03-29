insert into company (name, type) values ('Vocational School Elmshorn', 'education');
insert into company (name, type) values ('University of Hamburg', 'education');
insert into company (name, type) values ('Goodgame Studios', 'experience');
insert into company (name, type) values ('XYRALITY GmbH', 'experience');
insert into company (name, type) values ('HanseMerkur Insurance Group', 'experience');
insert into company (name, type) values ('Gameforge', 'experience');

insert into job (title) values ('Berufsfachschule (Field of studies - Foreign languages)');
insert into job (title) values ('Berufsoberschule (Field of studies - Economics)');
insert into job (title) values ('Computer Science');
insert into job (title) values ('Quality Assurance Engineer');
insert into job (title) values ('Software Engineering Trainee');
insert into job (title) values ('Software Development Engineer');
insert into job (title) values ('DevOps Engineer');
insert into job (title) values ('Software Developer');

insert into skill (text, active) values ('Java', true);
insert into skill (text, active) values ('Kotlin', true);
insert into skill (text, active) values ('Android', true);
insert into skill (text, active) values ('Git', true);
insert into skill (text, active) values ('Postgres', true);
insert into skill (text, active) values ('Docker', true);
insert into skill (text, active) values ('DevOps', true);
insert into skill (text, active) values ('Kubernetes', true);
insert into skill (text, active) values ('Ansible', true);
insert into skill (text, active) values ('IntelliJ IDEA', true);
insert into skill (text, active) values ('Android Studio', true);

insert into contact (name, email) values ('Rickie Karp', 'contact@rickiekarp.net');

insert into experience (startDate, endDate, companyid, jobid, description) values (now(), null, 1, 1, '{
  "description": "University of applied sciences entrance qualification",
  "tasks": []
}');

insert into experience (startDate, endDate, companyid, jobid, description) values (now(), null, 1, 2, '{
  "description": "General qualification for university entrance",
  "tasks": []
}');

insert into experience (startDate, endDate, companyid, jobid, description) values (now(), null, 2, 3, '{
  "description": "Attended computer science classes for one semester",
  "tasks": []
}');

insert into experience (startDate, endDate, companyid, jobid, description) values (now(), null, 3, 4, '{
  "description": "During my time at Goodgame Studios I was responsible for:",
  "tasks": [
    "Testing alpha versions of games and company-internal systems",
    "Creating and verifying bug reports",
    "Maintaining the Atlassian Jira bug database",
    "Analysing test processes and developing process improvements",
    "Supervising a team of up to 30 testers",
    "Providing feedback in regards to game design and gameplay"
  ]
}');

insert into experience (startDate, endDate, companyid, jobid, description) values (now(), null, 4, 5, '{
  "description": "At Xyrality I worked on:",
  "tasks": [
    "Developing automated tests for Android, iOS and Browser game clients",
    "Creating and maintaining Jenkins build scripts",
    "Maintaining Jenkins slave machines for continuous integration",
    "Leading the test automation project which includes evaluating/prioritizing software requirements and creating/assigning tasks to individual developers",
    "Designing and implementing a Spring/Hibernate based webapplication using Java/Kotlin and the Vaadin Framework"
  ]
}');

insert into experience (startDate, endDate, companyid, jobid, description) values (now(), null, 5, 6, '{
  "description": "My responsibilities while working for HanseMerkur:",
  "tasks": [
    "Creating Java/AngularJS Enterprise applications",
    "Developing features for a REST/JMS based microservice architecture",
    "Profiling and optimizing backend/frontend performance"
  ]
}');

insert into experience (startDate, endDate, companyid, jobid, description) values (now(), null, 6, 7, '{
  "description": "At Gameforge I have been involved in",
  "tasks": [
    "Operation and maintenance of the payment system, the company\'s own and company-wide platform for payment processing",
    "Optimization of the system in terms of performance, stability, reliability, availability and security",
    "Identify, analyze, and coordinate on issues with external payment providers and games",
    "Development of quick problem solutions (repair scripts, hotfixes, etc.) and sustainable strategies",
    "Technical configuration management of the systems with Ansible",
    "Technical release management through optimized processes/scripts and CD pipelines",
    "Co-design of the technical oepration of the payment system and the various processes",
    "Further development, definition and optimization of the monitoring and supervision of the payment system for the timely detection of problems and errors",
    "Coordination and communication in case of technical problems and emergencies (incident management)",
    "Support in optimizing data sets and ensuring data consistency",
    "24/7 on-call service to ensure system availability",
    "Internal and external technical contact for the operation of the payment system, including, among others, third-level support for billing support and developers",
    "Participation in internal projects and consulting for development and operations on system and application-related topics",
    "Development of CI/CD pipelines",
    "Development of sustainable anti-fraud measures and support of the corresponding specialist department",
    "Creation of technical documentation and reference prototypes for external developer studios for the processing of payments on mobile devices",
    "Implementation of UI and gameplay features for OGame Mobile (Unity)"
  ]
}');