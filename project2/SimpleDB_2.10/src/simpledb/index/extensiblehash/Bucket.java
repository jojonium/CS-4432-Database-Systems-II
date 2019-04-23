package simpledb.index.extensiblehash;

/** CS4432-Project2
 * 
 * @author Joseph Petitti
 *
 * new class to represent extensible hash index buckets
 */
public class Bucket {
	private int bits;
	private String filename;
	private int depth;
	
	/** CS4432-Project2
	 * basic constructor
	 */
	public Bucket(int bits, String filename, int depth) {
		this.bits = bits;
		this.filename = filename;
		this.depth = depth;
	}
	
	/** CS4432-Project2
	 * bits getter
	 * @return bits
	 */
	public int getBits() {
		return bits;
	}
	
	/** CS4432-Project2
	 * bits setter
	 * @param bits new bits
	 * @return new bits
	 */
	public int setBits(int bits) {
		this.bits = bits;
		return this.bits;
	}
	
	/** CS4432-Project2
	 * filename getter
	 * @return filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/** CS4432-Project2
	 * filename setter
	 * @param filename new filename
	 * @return new filename
	 */
	public String setFilename(String filename) {
		this.filename = filename;
		return this.filename;
	}
	
	/** CS4432-Project2
	 * depth getter
	 * @return depth
	 */
	public int getDepth() {
		return depth;
	}
	
	/** CS4432-Project2
	 * depth setter
	 * @param depth new depth
	 * @return new depth
	 */
	public int setDepth(int depth) {
		this.depth = depth;
		return this.depth;
	}
}
