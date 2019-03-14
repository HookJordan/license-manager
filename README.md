# License Manager
This project is for COMP-482 Assignment 3. It is the implementation of a Product-Customer License Management System for Malwabytes Anti-Malware.

## Runtime Requirements
* JAVA JRE 1.8

## Compile Requirements
* JAVA SDK 1.8
* Gradle (Wrapper included in the project zip)

## Gradle Libraries
* org.json:json:20171018
* com.google.code.gson:gson:2.8.5

### Additional Notes
Project was built using intellij ide for java. If you wish to view the source / compile through intelli please follow these steps:
1. Import the project
  * Select the build.gradle file in the project zip to import as a gradle project
  * Click "import changes" on the gradle window that appears to download and compile the required libraries
2. Locate 'Driver.java' in the 'src/main/java/'
  * This is the main entry point of the application
3. Right click on 'Driver.java' and select 'Run Driver.main()'

## Databases
**All database are available in the /db folder in the project zip**

### User Database
* The user database is currently restricted to the following logins:
  * U: hookjo P: AAAbbb###123 
    * Customer Manager Permissions
    * Product Manager Permissions
  * U: doejo P: AAAbbb###123
    * Customer Manager Permissions
  * U: doeja P: AAAbbb###123
    * Product Manager Permissions

The user.db should be stored in the directory in which you are running from the jar file from. You can add additional user logins or modify existing logins by editing the users.db file. The entries are stored in a json array. Please note in a full implementation of this project, the login system shoud be linked to the companies ldap or active directory for authentication and authorization.

### Customer Database
* The customer database will come with a few existing records.
* Additional records can be added through the UI or manually editing the customer database file (json array as well).
* The customers.db file should be located in the jar files directory similar to the users.db file.

### Product Database
* The product database will come with a few existing records
Additional records can be added through the UI or manually editing the product database file (json array as well).
* The products.db file should be located in the jar files directory similar to the users.db file.
