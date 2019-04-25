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
	
	public static void startup() {
		// analogous to the driver
		SimpleDB.init("cs4432DB");
		

		String s = "create table STUDENT(SId int, SName varchar(10), MajorId int, GradYear int)";
		executeUpdate(s);
		System.out.println("Table STUDENT created.");

		s = "insert into STUDENT(SId, SName, MajorId, GradYear) values ";
		String[] studvals = { "(1, 'joe', 10, 2004)", "(2, 'amy', 20, 2004)", "(3, 'max', 10, 2005)",
				"(4, 'sue', 20, 2005)", "(5, 'bob', 30, 2003)", "(6, 'kim', 20, 2001)", "(7, 'art', 30, 2004)",
				"(8, 'pat', 20, 2001)", "(9, 'lee', 10, 2004)" };
		for (int i = 0; i < studvals.length; i++)
			executeUpdate(s + studvals[i]);
		System.out.println("STUDENT records inserted.");

		s = "create table DEPT(DId int, DName varchar(8))";
		executeUpdate(s);
		System.out.println("Table DEPT created.");

		s = "insert into DEPT(DId, DName) values ";
		String[] deptvals = { "(10, 'compsci')", "(20, 'math')", "(30, 'drama')" };
		for (int i = 0; i < deptvals.length; i++)
			executeUpdate(s + deptvals[i]);;
		System.out.println("DEPT records inserted.");

		s = "create table COURSE(CId int, Title varchar(20), DeptId int)";
		executeUpdate(s);;
		System.out.println("Table COURSE created.");

		s = "insert into COURSE(CId, Title, DeptId) values ";
		String[] coursevals = { "(12, 'db systems', 10)", "(22, 'compilers', 10)", "(32, 'calculus', 20)",
				"(42, 'algebra', 20)", "(52, 'acting', 30)", "(62, 'elocution', 30)" };
		for (int i = 0; i < coursevals.length; i++)
			executeUpdate(s + coursevals[i]);
		System.out.println("COURSE records inserted.");

		s = "create table SECTION(SectId int, CourseId int, Prof varchar(8), YearOffered int)";
		executeUpdate(s);
		System.out.println("Table SECTION created.");

		s = "insert into SECTION(SectId, CourseId, Prof, YearOffered) values ";
		String[] sectvals = { "(13, 12, 'turing', 2004)", "(23, 12, 'turing', 2005)", "(33, 32, 'newton', 2000)",
				"(43, 32, 'einstein', 2001)", "(53, 62, 'brando', 2001)" };
		for (int i = 0; i < sectvals.length; i++)
			executeUpdate(s + sectvals[i]);
		System.out.println("SECTION records inserted.");

		s = "create table ENROLL(EId int, StudentId int, SectionId int, Grade varchar(2))";
		executeUpdate(s);
		System.out.println("Table ENROLL created.");

		s = "insert into ENROLL(EId, StudentId, SectionId, Grade) values ";
		String[] enrollvals = { "(14, 1, 13, 'A')", "(24, 1, 43, 'C' )", "(34, 2, 43, 'B+')", "(44, 4, 33, 'B' )",
				"(54, 4, 53, 'A' )", "(64, 6, 53, 'A' )" };
		for (int i = 0; i < enrollvals.length; i++)
			executeUpdate(s + enrollvals[i]);
		System.out.println("ENROLL records inserted.");
		
		// Joe changed his major to math
		s = "update STUDENT set MajorId = 20 where SId = 1";
		executeUpdate(s);
		System.out.println("STUDENT record updated.");
		
		
		
		// Create index
		s = "create bt index staticStudentNameIndex on STUDENT ( SName )";
		executeUpdate(s);
		System.out.println("index created on student");
		
		

	}

	public static void main(String[] args) {
		try {
			startup();

			// analogous to the connection
			Transaction tx = new Transaction();

			// analogous to the statement
			String qry = "select SName, DName " + "from DEPT, STUDENT " + "where MajorId = DId";
			Plan p = SimpleDB.planner().createQueryPlan(qry, tx);

			// analogous to the result set
			Scan s = p.open();

			System.out.println("Name\tMajor");
			while (s.next()) {
				String sname = s.getString("sname"); // SimpleDB stores field names
				String dname = s.getString("dname"); // in lower case
				System.out.println(sname + "\t" + dname);
			}
			s.close();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}