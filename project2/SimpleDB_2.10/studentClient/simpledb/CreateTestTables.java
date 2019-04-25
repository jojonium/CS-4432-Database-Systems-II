import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import simpledb.query.Plan;
import simpledb.query.Scan;
import simpledb.remote.SimpleDriver;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;
public class CreateTestTables {
 final static int maxSize=1000;
 /**
  * @param args
  */
 
	/*
	 * Must have initialized the server already
	 */
	public static Scan query(String qry) {
		try {
			// analogous to the connection
			Transaction tx = new Transaction();
			Plan p = SimpleDB.planner().createQueryPlan(qry, tx);
			Scan s = p.open();

			//s.close();
			tx.commit();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer executeUpdate(String qry) {
		try {
			Transaction tx = new Transaction();
			int p = SimpleDB.planner().executeUpdate(qry, tx);

			tx.commit();
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	
 
 public static void main(String[] args) {
	 
  SimpleDB.init("cs4432DB");
  
  
  
  String qry="Create table test1" +
  "( a1 int," +
  "  a2 int"+
  ")";
  Random rand=null;
   executeUpdate("Create table test1" +
     "( a1 int," +
     "  a2 int"+
   ")");
   executeUpdate("Create table test2" +
     "( a1 int," +
     "  a2 int"+
   ")");
   executeUpdate("Create table test3" +
     "( a1 int," +
     "  a2 int"+
   ")");
   executeUpdate("Create table test4" +
     "( a1 int," +
     "  a2 int"+
   ")");
   executeUpdate("Create table test5" +
     "( a1 int," +
     "  a2 int"+
   ")");

   executeUpdate("create sh index idx1 on test1 (a1)");
   executeUpdate("create eh index idx2 on test2 (a1)");
   executeUpdate("create bt index idx3 on test3 (a1)");
   
   
   
   for(int i=1;i<6;i++)
   {
    if(i!=5)
    {
     rand=new Random(1);// ensure every table gets the same data
     System.out.println("Starting new table test"+i);
     for(int j=0;j<maxSize;j++)
     {
      if (j % 50 == 0) { System.out.println(j); }
      executeUpdate("insert into test"+i+" (a1,a2) values("+rand.nextInt(1000)+","+rand.nextInt(1000)+ ")");
     }
    }
    else//case where i=5
    {
     for(int j=0;j<maxSize/2;j++)// insert 10000 records into test5
     {
      executeUpdate("insert into test"+i+" (a1,a2) values("+j+","+j+ ")");
     }
    }
   }
   
   
 }
}
