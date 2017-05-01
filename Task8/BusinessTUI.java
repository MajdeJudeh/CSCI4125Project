import java.util.Scanner;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class BusinessTUI{
  private Scanner input = new Scanner(System.in);
  private Connection connection;

  public BusinessTUI(Connection conn){
    connection = conn;
  }
  public void menuRunner(){
    System.out.println("Enter \"0\" to exit");
    System.out.println("Enter \"1\" to insert an employee into your company.");
    System.out.println("Enter \"2\" to search for jobs.");
    System.out.println("Enter \"3\" to search for potential employees.");
    System.out.println("Enter \"4\" to find the jobs in each category.");
    menu(input.nextInt());
  }

  public void menu(int menuOption){
    if (menuOption != 0){
           if(menuOption == 1) acceptEmployee();
      else if(menuOption == 2) searchJobs();
      else if(menuOption == 3) findPerson();
      else if(menuOption == 4) careerPlanning();
      System.out.println("\nTask completed, hit enter to continue");
      input.nextLine();
      input.nextLine();
      menuRunner();

    }
  }

  public void acceptEmployee(){
    AcceptEmployee insertEmployee = new AcceptEmployee(connection);
    insertEmployee.selectCompany();
  }//end of acceptEmployee

  public void searchJobs(){
    JobSearch jobSearch = new JobSearch(connection);
    jobSearch.searchOptions();
  }

  public void findPerson(){
    PeopleSearch findPeople = new PeopleSearch(connection);
    findPeople.selectPerson();
  }

  public void careerPlanning(){
    CategoryJobs catJobs = new CategoryJobs(connection);
    catJobs.jobCategories();
  }
}//end of TUI
