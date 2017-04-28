import java.util.Scanner;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class TUI{
  private Scanner input = new Scanner(System.in);
  private Connection connection;
  public TUI(){

  }
  public TUI(Connection conn){
    connection = conn;
  }
  public void menuRunner(){
    System.out.println("Enter \"0\" to exit");
    System.out.println("Enter \"1\" to run 8 - A");
    System.out.println("Enter \"2\" to run 8 - B");
    System.out.println("Enter \"3\" to run 8 - C");
    System.out.println("Enter \"4\" to run 8 - D");
    menu(input.nextInt());
  }

  public void menu(int menuOption){
         if(menuOption == 1) acceptEmployee();
    else if(menuOption == 2) jobHunt();
    else if(menuOption == 3) findPerson();
    else if(menuOption == 4) careerPlanning();
  }

  public void acceptEmployee(){

  }//end of acceptEmployee

  public void jobHunt(){

  }

  public void findPerson(){

  }

  public void careerPlanning(){

  }
}//end of TUI
