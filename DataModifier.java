import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class DataModifier{
  private Connection connection;
  private Scanner input = new Scanner(System.in);
  public DataModifier(Connection conn){
    connection = conn;
  }

  public void change(int userOption){

    while(userOption != 0){

      if(userOption == 1){
        System.out.printf("Enter 1 to insert a person. \n Enter 2 to delete a person. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          Person insert = new Person(connection);
          insert.insertRow();
        }//end of inner if

        else if(userOption == 2){
          Person delete = new Person(connection);
          delete.deleteRow();
          userOption = 1;
        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 1

      else if(userOption == 2){
        System.out.printf("Enter 1 to insert a Job. \n Enter 2 to delete a Job. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          Jobs insert = new Jobs(connection);
          insert.insertRow();
          userOption = 2;
        }//end of inner if

        else if(userOption == 2){
          Jobs delete = new Jobs(connection);
          delete.deleteRow();

        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 2

      else if(userOption == 3){
        System.out.printf("Enter 1 to insert a Job Category. \n Enter 2 to delete a Job Category. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          JobCategory insert = new JobCategory(connection);
          insert.insertRow();
          userOption = 3;
        }//end of inner if

        else if(userOption == 2){
          JobCategory delete = new JobCategory(connection);
          delete.deleteRow();
          userOption = 3;
        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 3

      else if(userOption == 4){
        System.out.printf("Enter 1 to insert a Company. \n Enter 2 to delete a Company. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          Company insert = new Company(connection);
          insert.insertRow();
          userOption = 4;
        }//end of inner if

        else if(userOption == 2){
          Company delete = new Company(connection);
          delete.deleteRow();
          userOption = 4;
        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 4

      else if(userOption == 5){
        System.out.printf("Enter 1 to insert a Course. \n Enter 2 to delete a Course. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          Course insert = new Course(connection);
          insert.insertRow();
          userOption = 5;
        }//end of inner if

        else if(userOption == 2){
          Course delete = new Course(connection);
          delete.deleteRow();
          userOption = 5;
        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 5

      else if(userOption == 6){
        System.out.printf("Enter 1 to insert a Section. \n Enter 2 to delete a Section. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          Section insert = new Section(connection);
          insert.insertRow();
          userOption = 6;
        }//end of inner if

        else if(userOption == 2){
          Section delete = new Section(connection);
          delete.deleteRow();
          userOption = 6;
        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 6

      else if(userOption == 7){
        System.out.printf("Enter 1 to insert a Knowledge Skill. \n Enter 2 to delete a Knowledge Skill. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          KnowledgeSkills insert = new KnowledgeSkills(connection);
          insert.insertRow();
          userOption = 7;
        }//end of inner if

        else if(userOption == 2){
          KnowledgeSkills delete = new KnowledgeSkills(connection);
          delete.deleteRow();
          userOption = 7;
        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 7

      else if(userOption == 8){
        System.out.printf("Enter 1 to insert a NAICS. \n Enter 2 to delete a NAICS. \n Any other number will exit the program. \n");
        userOption = input.nextInt();

        if(userOption == 1){
          NAICS insert = new NAICS(connection);
          insert.insertRow();
          userOption = 8;
        }//end of inner if

        else if(userOption == 2){
          NAICS delete = new NAICS(connection);
          delete.deleteRow();
          userOption = 8;
        }//end of inner else if 2

        else userOption = 0;

      }//end of outer if for userOption = 8

    }//end of while for userOption

  }//end of change method

}//end of class
