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
    System.out.println("Enter \"1\" to run queries");
    System.out.println("Enter \"2\" to modify tables.");
    menu(input.nextInt());
  }

  public void menu(int menuOption){
    if(menuOption == 1) queryRunner();
    else if(menuOption == 2) dataChangeRunner();
  }

  public void queryRunner(){
      int userOption;
      Query query = new Query(connection);
    do{
      System.out.println("Enter \"0\" to exit, otherwise enter one of the numbers of the queries to run them.");
      userOption = input.nextInt();
      if(userOption > 0 && userOption <= 28){
          query.runQuery(userOption);
      }
    }while(userOption != 0);//end of do/while
  }//end of queryRunner


}//end of TUI
