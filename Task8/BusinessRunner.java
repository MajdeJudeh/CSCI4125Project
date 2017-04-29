import java.util.*;
import java.sql.*;
import java.math.*;
import oracle.jdbc.*;

public class BusinessRunner{

  public static void main(String[] args){

      System.out.println("Attempting to connect to database.");
      try (Connection connection = getDbConnection("mbjudeh3" , "gccnbF7d")){
        System.out.println("You have logged into the database.");
        runQueries(connection);
      }
      catch (SQLException e) {
        System.out.println("Could not connect");
        e.printStackTrace();
      }


  }//end of main method

  private static Connection getDbConnection(String username, String password) throws SQLException{
    DriverManager.registerDriver(new OracleDriver());
    String url = "jdbc:oracle:thin:@dbsvcs.cs.uno.edu:1521:orcl";

    Connection connection = DriverManager.getConnection(url, username, password);
    return connection;
  }//end method getDbConnection

  private static void runQueries(Connection connection){//passing the question in

    BusinessTUI businessTUI = new BusinessTUI(connection);
    businessTUI.menuRunner();
  }
}//end class
