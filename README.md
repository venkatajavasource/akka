# akka
Akka Framework
/************************************************************************************/
/** This the read me file that helps to set-up and run the jar file to process 	 ****/
/** the files for given directory and print the file name with no.of word in that. **/
/************************************************************************************/

Tools and technologies Uses
1) JDK 1.8
2) Akka Framework 2.5.11
3) Junit 4.4
4) Logback 1.0.7

How to run: 
Steps to fallow to run,
1) Create folder with name "akka" in C directory. The folder should be like C:\akka\
2) Copy "fileActorSystem.jar" and "fileActorBootStrap.bat" to the folder C:\akka\
3) Go to C:\akka in command prompt and run fileActorBootStrap.bat
   Provide the directory path and press Enter.
4) The application log file akka.log will be generated in C:\akka\akka.log folder.
5) Verify all the files from the provided directory are processed, then Press Enter to exit the jar file.

How to Set-up Dev environment:
1) Copy fileActorSystem.7z file to C:\akka\ or any of your preferred directory.
2) Un-zip to the folder C\akka\
3) Open the command prompt and run > mvn package to package the project as jar with all dependencies.
C:\akka\fileActorSystem> mvn package
4) As we know the jar file will be created in maven target folder with name 
fileActorSystem-0.0.1-SNAPSHOT-jar-with-dependencies.jar

