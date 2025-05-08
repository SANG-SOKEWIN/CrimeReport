# CrimeReport

Crime Report Management System
How to Run the Project
Requirements

Java Runtime Environment (JRE) 8 or higher

Setup and Execution

1. Save the provided Java code as CrimeReport.java
2. Open a terminal/command prompt
3. Navigate to the directory containing the file
4. Run the program

First-time Use

On first run, the system will automatically create necessary data files:

userLogin.txt - Stores user credentials
userDetails.txt - Stores user personal information
policeLogin.txt - Stores police officer credentials
crimeReports.txt - Stores all crime report data

User Access

Register as a new user (option 3 from main menu)
Login with your National ID and password (option 1)
Use the system to report crimes and track case status

Police Access

1. When attempting police login for the first time (option 2 from main menu), enter any random ID and password
2. This will create the policeLogin.txt file automatically (the login will fail but the file will be crea
3. Manually add police credentials to the policeLogin.txt file
4. Format: [Police ID]:[Password] (one entry per line)
5. Example entry in policeLogin.txt: P1001:police123
6. You can now login as police with these credentials

Project Overview
This Crime Report Management System allows citizens to report crimes and track cases while enabling police officers to manage and investigate reported incidents through a simple console interface.
