package simpledb.parse;

/**
 * The parser for the <i>create index</i> statement.
 * @author Edward Sciore
 */
public class CreateIndexData {
   // CS4432-Project2 added idxtype field
   private String idxtype, idxname, tblname, fldname;
   
   /** CS4432-Project2
    * Added idxtype to constructor
    */
   /**
    * Saves the table and field names of the specified index.
    */
   public CreateIndexData(String idxtype, String idxname, String tblname, String fldname) {
      this.idxtype = idxtype;
      this.idxname = idxname;
      this.tblname = tblname;
      this.fldname = fldname;
   }
   
   /** CS4432-Project2
    * New getter method
    * @return the index type
    */
   public String indexType() {
      return idxtype;
   }
   
   /**
    * Returns the name of the index.
    * @return the name of the index
    */
   public String indexName() {
      return idxname;
   }
   
   /**
    * Returns the name of the indexed table.
    * @return the name of the indexed table
    */
   public String tableName() {
      return tblname;
   }
   
   /**
    * Returns the name of the indexed field.
    * @return the name of the indexed field
    */
   public String fieldName() {
      return fldname;
   }
}

