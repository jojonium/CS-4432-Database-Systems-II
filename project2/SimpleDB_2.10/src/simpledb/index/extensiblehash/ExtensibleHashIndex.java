package simpledb.index.extensiblehash;

import java.util.ArrayList;
import java.util.HashMap;

import simpledb.index.Index;
import simpledb.query.Constant;
import simpledb.query.TableScan;
import simpledb.record.RID;
import simpledb.record.Schema;
import simpledb.record.TableInfo;
import simpledb.tx.Transaction;

/** CS4432-Project2
 * 
 * @author Joseph Petitti
 *
 * new class to represent an extensible hash index
 * a lot of this is the same as HashIndex
 * 
 * buckets are implemented as files containing index records
 */
public class ExtensibleHashIndex implements Index {
	private static int NUM_BUCKETS = 2;          // # buckets per directory
	private static int MAX_BUCKET_CAPACITY = 5; // maximum number of records per bucket
	private int globalDepth = 1;         // global directory depth
	private static String GLOBAL_TABLE = "globalTable";
	private String idxname;
	private Schema sch; // local schema
	private Schema globalSchema; // schema for the whole directory
	private Transaction tx;
	private Constant searchkey = null;
	private TableScan ts = null;
	private int localDepth;
	private String bucketFilename;
	
	/** CS4432-Project2
	 * basic constructor
	 * @param idxname
	 * @param sch
	 * @param tx
	 */
	public ExtensibleHashIndex(String idxname, Schema sch, Transaction tx) {
		this.idxname = idxname;
		this.sch = sch;
		this.tx = tx;
		this.globalSchema.addIntField("bits");
		this.globalSchema.addStringField("filename", 32);
		this.globalSchema.addIntField("localdepth");
		TableScan tempTableScan = new TableScan(new TableInfo(GLOBAL_TABLE, globalSchema), tx);
		// make the buckets
		for (int i = 0; i < NUM_BUCKETS; ++i) {
			tempTableScan.insert();
			tempTableScan.setInt("bits", i);
			tempTableScan.setString("filename", idxname + i);
			tempTableScan.setInt("localdepth", 1);
			tempTableScan.insert();
		}
	}
	
	/** CS4432-Project2
	 * implements {@link simpledb.index.Index#beforeFirst(Constant)}
	 * Positions the index before the first record
	 * having the specified search key.
	 * @param searchkey the search key value.
	 */
	public void beforeFirst(Constant searchkey) {
		close();
		this.searchkey = searchkey;
		int bucket = searchkey.hashCode() % (int)Math.pow(2, globalDepth);
		// make global table
		TableInfo globalTableInfo = new TableInfo(GLOBAL_TABLE, globalSchema);
		// make global table scan
		TableScan globalTableScan = new TableScan(globalTableInfo, tx);
		// find bit string and get the file name and local depth
		while (globalTableScan.next()) {
			if (globalTableScan.getInt("bits") == bucket) {
				this.localDepth = globalTableScan.getInt("localdepth");
				this.bucketFilename = globalTableScan.getString("filename");
				break;
			}
		}
		TableInfo ti = new TableInfo(bucketFilename, sch);
		ts = new TableScan(ti, tx);
	}
	
	/** CS4432-Project2
	 * implements {@link simpledb.index.Index#next()}
	 * Moves the index to the next record having the
	 * search key specified in the beforeFirst method. 
	 * Returns false if there are no more such index records.
	 * @return false if no other index records have the search key.
	 */
	public boolean next() {
		while (ts.next()) {
			if (ts.getVal("dataval").equals(searchkey))
				return true;
		}
		return false;
	}
	
	/** CS4432-Project2
	 * implements {@link simpledb.index.Index#getDataRid()}
	 * Returns the dataRID value stored in the current index record. 
	 * @return the dataRID stored in the current index record.
	 */
	public RID getDataRid() {
		int blknum = ts.getInt("block");
		int id = ts.getInt("id");
		return new RID(blknum, id);
	}
	
	/** CS4432-Project2
	 * implements simple
	 * Inserts an index record having the specified
	 * dataval and dataRID values.
	 * @param dataval the dataval in the new index record.
	 * @param datarid the dataRID in the new index record.
	 */
	public void insert(Constant dataval, RID datarid) {
		// TODO check if bucket is full, and split if necessary
		
		// find the length of the table
		int size = 0;
		while (ts.next()) ++size;
		
		while (size >= MAX_BUCKET_CAPACITY) {
			TableInfo tempTI = new TableInfo(GLOBAL_TABLE, globalSchema);
			TableScan tempTS = new TableScan(tempTI, tx);
			
			if (localDepth == globalDepth) {
				// read the global file, delete all the records and re-insert with longer bit string
				globalDepth++;
				ArrayList<Bucket> tempAL = new ArrayList<Bucket>();
				while (tempTS.next()) {
					tempAL.add(new Bucket(tempTS.getInt("bits") + (int)Math.pow(2, globalDepth - 1), tempTS.getString("filename"), tempTS.getInt("localdepth")));
				}
				for (Bucket b : tempAL) {
					tempTS.insert();
					tempTS.setInt("bits",  b.getBits());
					tempTS.setString("filename", b.getFilename());
					tempTS.setInt("localdepth", b.getDepth());
				}
			}
			
			localDepth++;
			
			// split the bucket
			String bucket1Name = bucketFilename;
			TableScan ts1 = new TableScan(new TableInfo(bucket1Name, sch), tx);
			int bucket2Bits = (searchkey.hashCode() % (int) Math.pow(2, localDepth - 1)) + (int)Math.pow(2, localDepth - 1);
			String bucket2Name = idxname + bucket2Bits;
			TableScan ts2 = new TableScan(new TableInfo(bucket2Name, sch), tx);
			while (ts1.next()) {
				if (ts1.getVal("dataval").hashCode() % (int)Math.pow(2,  localDepth) == bucket2Bits) {
					// move it from bucket1 to bucket2
					ts2.insert();
					ts2.setInt("block", ts1.getInt("block"));
					ts2.setInt("id",  ts1.getInt("id"));
					ts2.setVal("dataval", ts1.getVal("dataval"));
					ts1.delete();
				}
			}
			
			// change filenames in global table
			tempTS.beforeFirst();
			int temp = (int)Math.pow(2, localDepth - 1);
			while (tempTS.next()) {
				int bucket = tempTS.getInt("bits");
				if (bucket % temp == searchkey.hashCode() % temp) {
					if (bucket / temp >= 1) {
						tempTS.setString("filename", bucket2Name);
					} else {
						tempTS.setString("filename", bucket1Name);
					}
				}
			}
			
			// set ts to new bucket
			beforeFirst(dataval);
			
			// get new length
			size = 0;
			while (ts.next()) size++;
		}
		
		ts.insert();
		ts.setInt("block", datarid.blockNumber());
		ts.setInt("id", datarid.id());
		ts.setVal("dataval", dataval);
	}
	
	/** CS4432-Project2
	 * implements {@link simpledb.index.Index#delete(Constant, RID)}
	 * Deletes the index record having the specified
	 * dataval and dataRID values.
	 * @param dataval the dataval of the deleted index record
	 * @param datarid the dataRID of the deleted index record
	 */
	public void delete(Constant dataval, RID datarid) {
		beforeFirst(dataval);
		while(next()) {
			if (getDataRid().equals(datarid)) {
				ts.delete();
				return;
			}
		}
	}
	
	/** CS4432-Project2
	 * implements {@link simpledb.index.Index#close()}
	 * Closes the index.
	 */
	public void close() {
		if (ts != null)
			ts.close();
	}
	
	/** CS4432-Project2
	 * Returns the cost of searching an index file having the
	 * specified number of blocks.
	 * The method assumes that all buckets are about the
	 * same size, and so the cost is simply the size of
	 * the bucket.
	 * @param numblocks the number of blocks of index records
	 * @param rpb the number of records per block (not used here)
	 * @return the cost of traversing the index
	 */
	public static int searchCost(int numblocks, int rpb){
		return numblocks / NUM_BUCKETS;
	}
}





