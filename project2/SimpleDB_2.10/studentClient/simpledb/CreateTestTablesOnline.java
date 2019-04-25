/******************************************************************/
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import simpledb.remote.SimpleDriver;
public class CreateTestTablesOnline {
 final static int maxSize=20000;
 /**
  * @param args
  */
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  Connection conn=null;
  Driver d = new SimpleDriver();
  String host = "localhost"; //you may change it if your SimpleDB server is running on a different machine
  String url = "jdbc:simpledb://" + host;
  String qry="Create table test1" +
  "( a1 int," +
  "  a2 int"+
  ")";
  Random rand=null;
  Statement s=null;
  try {
   conn = d.connect(url, null);
   s=conn.createStatement();

   s.executeUpdate("Create table test2" +
     "( a1 int," +
     "  a2 int"+
   ")");

   s.executeUpdate("create sh index idx1 on test1 (a1)");
   s.executeUpdate("create eh index idx2 on test2 (a1)");
   s.executeUpdate("create bt index idx3 on test3 (a1)");
   rand=new Random(1);
   for(int i=0;i<10;i++) {
	   s.executeUpdate("insert into test2 (a1,a2) values("+rand.nextInt(1000)+","+rand.nextInt(1000)+ ")");
   }
   
   
   
   conn.close();

  } catch (SQLException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }finally
  {
   try {
    conn.close();
   } catch (SQLException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }
 }
}
