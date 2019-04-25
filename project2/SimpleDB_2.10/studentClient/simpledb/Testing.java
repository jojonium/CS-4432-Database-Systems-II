import java.sql.*;

import simpledb.query.Plan;
import simpledb.query.Scan;
import simpledb.remote.SimpleDriver;
import simpledb.server.SimpleDB;
import simpledb.tx.Transaction;


/*
 * CS4432-Project1
 * This is the testing file that contains queries for task 2.6.
 * This file can be run and operates fully offline (without a connection to the server)
 */
public class Testing {
	
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
		try {

			// analogous to the connection
			Transaction tx = new Transaction();

			// analogous to the statement
			String qry = "select a1, a2" + "from test2"; // " + "where MajorId = DId";
			Plan p = SimpleDB.planner().createQueryPlan(qry, tx);

			// analogous to the result set
			Scan s = p.open();

			System.out.println("a1, a2");
			while (s.next()) {
				String sname = s.getString("a1"); // SimpleDB stores field names
				String dname = s.getString("a2"); // in lower case
				System.out.println(sname + "\t" + dname);
			}
			s.close();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}