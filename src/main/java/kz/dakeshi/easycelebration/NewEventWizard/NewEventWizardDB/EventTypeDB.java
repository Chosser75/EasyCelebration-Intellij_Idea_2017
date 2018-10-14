package kz.dakeshi.easycelebration.NewEventWizard.NewEventWizardDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EventTypeDB
{

  public ArrayList db() throws ClassNotFoundException
  {

    Class.forName("org.sqlite.JDBC");

    ResultSet rs = null;
    Connection connection = null;
    ArrayList<String> list=new ArrayList<String>();

    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:src\\Clb.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.

      rs = statement.executeQuery("select event_type from EventTypeSpr");

      while(rs.next())
      {list.add(rs.getString("event_type"));}
    }
    catch(SQLException e)
    {
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
    return list;

  }
}