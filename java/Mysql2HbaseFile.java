import java.sql.*;

public class Mysql2HbaseFile {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://10.26.7.99:6301/myshard";

   //  Database credentials
   //static final String USER = "db_myshard_rw";
   //static final String PASS = "8Qf5p9lJOx,B";
 
   static long count;
   static HbaseClientFile client = null;

   static{

   client = new HbaseClientFile();
   }

   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;

   if (args.length == 0) {  
            System.out.println("no arguments provided");  
            return;  
    }  
    System.out.println("调用main方法时指定的参数包括：");  
    for (int i = 0; i < args.length; i++) {  
            System.out.println("参数" + (i + 1) + "的值为：" + args[i]);  
    }  

   String port = args[0];
   String tblname = args[1];
   String PASS = args[2];

   String USER = "db_monitor";
   System.out.println("mysql port  :" +  args[0]);

   System.out.println("mysql table name  :" +  args[1]);
 
   String db_url = "jdbc:mysql://127.0.0.1:" + port + "/myshard";

   System.out.println("mysql connect string:" +  db_url);

   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      // conn = DriverManager.getConnection(DB_URL,USER,PASS);

      conn = DriverManager.getConnection(db_url,USER,PASS);
      
      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;
      //sql = "SELECT *  FROM  bs2_file_4 limit 1000";
	  //sql = "select v_content_key, zone_id, v_zfilename_digest, zone_file, update_time, status, n_src, __version from bs2_copy_4 limit 10";

      sql = "select v_filename_digest, upload_id, bucket, file_name, upload_zone_id, update_time, size, status, mime, v_content_key, file_type, n_is_caculate_size, __version from " + tblname + " limit 20";

      System.out.println("mysql query string: " +  sql);

      ResultSet rs = stmt.executeQuery(sql);

	  System.out.println("no exception during JDBC query start to put hbase ");

      //STEP 5: Extract data from result set
      count = 0;
	  String fields[] = new String[13];
      while(rs.next()){
         //Retrieve by column name
	
		 for ( int i=0; i< 13; i++){

			 fields[i]=rs.getString(i+1);
			 //System.out.print("tmp :" + i + fields[i]  + "\n");
		 
		 }

		 client.put2bsfile(fields);

		 if(count % 10000 == 0) {
			         System.out.println("count : " + count);
	     }

	     count++  ;

         //String v_filename_digest = rs.getString("v_content_key");
         //long  upload_id = rs.getLong("zone_id");
         //System.out.print("v_filename_digest " + v_filename_digest + "\n");
         //System.out.print(", upload_id: " + upload_id + "\n");
      }

	  client.teardown();
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
   System.exit(0);
}//end main
}//end mysql2hbase

