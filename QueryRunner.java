import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class QueryRunner{
  private static Scanner input = new Scanner(System.in);

  public static void main(String[] args){
    if(args.length != 2){
      System.out.println("Please supply a username in password with username as arg0 and password as arg1");
    }
    else{
      System.out.println("Attempting to connect to database.");
      try (Connection connection = getDbConnection(args[0], args[1])){
        System.out.println("You have logged into the database.");
        runQueries(connection);
      }
      catch (SQLException e) {
        System.out.println("Could not connect");
        e.printStackTrace();
      }
    }

    }//end of main method
    private static Connection getDbConnection(String username, String password) throws SQLException{
      DriverManager.registerDriver(new OracleDriver());
      String url = "jdbc:oracle:thin:@dbsvcs.cs.uno.edu:1521:orcl";

      Connection connection = DriverManager.getConnection(url, username, password);
      return connection;
    }//end method getDbConnection

    private static void runQueries(Connection connection){//passing the question in
      TUI queryTUI = new TUI(connection);
      queryTUI.menuRunner();
    }
}//end class
