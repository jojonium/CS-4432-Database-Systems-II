/******************************************************************/
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import simpledb.remote.SimpleDriver;
public class CreateTestTablesOnline2 {
 final static int maxSize=1000000;
 
 public static long timeIt(String qry, Statement s, boolean output) throws SQLException {
	 final long start = System.currentTimeMillis();
	 ResultSet rs = s.executeQuery(qry);
	 final long elapsed = System.currentTimeMillis() - start;

	 // Step 3: loop through the result set
	 System.out.println("a1\ta2");
     if (output) while (rs.next()) {
    	 int a1 = rs.getInt("a1");
    	 int a2 = rs.getInt("a2");
    	 System.out.println(a1 + "\t" + a2);
	 }
	 rs.close();
	   
	 return elapsed;
 }
 
 public static long update(String qry, Statement s) throws SQLException {
	 final long start = System.currentTimeMillis();
	 s.executeUpdate(qry);
	 final long elapsed = System.currentTimeMillis() - start;	   
	 return elapsed;
 }
 
 public static void selectionConstantPredicate(String tableName, Statement s) throws SQLException {
	 String query = "select a1, a2 from " + tableName + " where a1 = 891";
	 long time = timeIt(query, s, false);
	 System.out.println("Query: [" + query + "]: Execution time: " + time + "ms\n");
 }
 
 public static void selectionJoin(String table1, String table2, Statement s) throws SQLException {
	 String query = "select a1, a2 from " + table1 + ", " + table2 + 
			 " where a1 = " + table2 + ".a1";
	 System.out.println(query);
	 long time = timeIt(query, s, false);
	 System.out.println("Query: [" + query + "]: Execution time: " + time + "ms\n");
 }
 
 
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
   if (false) {
   s.executeUpdate("Create table test1" +
     "( a1 int," +
     "  a2 int"+
   ")");
   s.executeUpdate("Create table test2" +
     "( a1 int," +
     "  a2 int"+
   ")");
   s.executeUpdate("Create table test3" +
     "( a1 int," +
     "  a2 int"+
   ")");
   s.executeUpdate("Create table test4" +
     "( a1 int," +
     "  a2 int"+
   ")");
   s.executeUpdate("Create table test5" +
     "( a1 int," +
     "  a2 int"+
   ")");

   s.executeUpdate("create sh index idx1 on test1 (a1)");
   s.executeUpdate("create eh index idx2 on test2 (a1)");
   s.executeUpdate("create bt index idx3 on test3 (a1)");
   // No index on test4
   
   if (false) {
	   
   for(int i=1;i<6;i++)
   {
    if(i!=5)
    {
     rand=new Random(1);// ensure every table gets the same data
     
     System.out.println("Started batch insertion on test"+i);
     
     for(int j=0;j<maxSize;j++)
     {
      s.executeUpdate("insert into test"+i+" (a1,a2) values("+rand.nextInt(1000)+","+rand.nextInt(1000)+ ")");
     }
    }
    else//case where i=5
    {
     for(int j=0;j<maxSize/2;j++)// insert 10000 records into test5
     {
      s.executeUpdate("insert into test"+i+" (a1,a2) values("+j+","+j+ ")");
     }
    }
   }
   
   }
   
   
   }
   
   System.out.println("================================");
   System.out.println("Selection on constant predicate:");
   System.out.println("================================");
   
   
   selectionConstantPredicate("test1", s);
   selectionConstantPredicate("test2", s);
   selectionConstantPredicate("test3", s);
   selectionConstantPredicate("test4", s);
   
   System.out.println("================================");
   System.out.println("Join on a1 = a1 between all tables and the table with no index:");
   System.out.println("================================");
   
   selectionJoin("test1", "test5", s);
   selectionJoin("test2", "test5", s);
   selectionJoin("test3", "test5", s);
   selectionJoin("test4", "test5", s);
   
   
   
   
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
