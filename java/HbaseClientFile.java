import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseClientFile {

   final static byte[] SRV_COL_FAM = "bs2_file".getBytes();
   final static int NUM_FIELDS = 16;

   HTable hTable;

   long count;

   Configuration  cfg = null;


   public HbaseClientFile() 
   {
      /*  cfg  = HBaseConfiguration.create();
        cfg.set("hbase.zookeeper.quorum", "183.36.121.237,183.36.121.245,183.36.121.247");

        hTable = new HTable(cfg, "test:bs2_file");

        hTable.setAutoFlush(false);

        hTable.setWriteBufferSize(10485760);
        count = 0;

      */
      try{
         setup();
	  }catch(IOException e){

         e.printStackTrace();
	  }catch(InterruptedException e){

         e.printStackTrace(); 
	  }
      

	}

    protected void setup() throws IOException,
      InterruptedException {
                //Configuration HBASE_CONFIG = new Configuration();
                //Configuration cfg = HBaseConfiguration.create(HBASE_CONFIG);
				
	    	    cfg  = HBaseConfiguration.create();		
	       	    cfg.set("hbase.zookeeper.quorum", "183.36.121.237,183.36.121.245,183.36.121.247");
                try{
                        hTable = new HTable(cfg, "test:bs2_file");
                        hTable.setAutoFlush(false);
                        hTable.setWriteBufferSize(10485760);
                }catch(IOException e){
                        e.printStackTrace();
                        throw e;
                }

      System.out.println("connectted to Hbase \n");
   }
 
   public void teardown()
	   throws IOException{

       try{ 
             hTable.flushCommits();
          
          }catch (IOException e) {
          
             e.printStackTrace();
          } 


   }

     /** {@inheritDoc} */
 
    public void put2bsfile(String[] fields)
      throws IOException, InterruptedException {



    long timestamp = Long.parseLong(fields[12]);

    long upload_id = Long.parseLong(fields[1]);

    String rowkey = String.format("%s%010d", fields[0], upload_id);


	System.out.println("rowkey :"+rowkey);
    Put put = new Put(rowkey.getBytes());

 

    put.setWriteToWAL(false);
	
    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_V_FILENAME_DIGEST.getColumnName(), timestamp, fields[0].getBytes());


    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_UPLOAD_ID.getColumnName(), timestamp, fields[1].getBytes());


    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_BUCKET.getColumnName(), timestamp, fields[2].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_FILE_NAME.getColumnName(), timestamp, fields[3].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_UPLOAD_ZONE_ID.getColumnName(), timestamp, fields[4].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_UPDATE_TIME.getColumnName(), timestamp, fields[5].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_SIZE.getColumnName(), timestamp, fields[6].getBytes());
			
    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_STATUS.getColumnName(), timestamp, fields[7].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_MINE.getColumnName(), timestamp, fields[8].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_V_CONTENT_KEY.getColumnName(), timestamp, fields[9].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_FILE_TYPE.getColumnName(), timestamp, fields[10].getBytes());
			 
    put.add(SRV_COL_FAM,
            HColumnEnumFile.SRV_COL_N_IS_CACULATE_SIZE.getColumnName(), timestamp, fields[11].getBytes());


	
    try{
		//System.out.println("start put to hbase !");
        hTable.put(put);
		//System.out.println("end put to hbase !");
    }catch(IOException e){
        e.printStackTrace();
        throw e;
    }

  }
/*

    public static void main(String[] args) throws Exception {


        HbaseClientFile client = new HbaseClientFile();

		String[] fds = { "000002a3d74d97569488cc5d3e8827ae2c2f062a", "423620343308415317", "yysnapshot", "yysnapshot/b96843fd5f0887e44c86d80e7f700be3ba6adef4", "32432505", "2016-04-1200:35:35", "36791", "0", "image/jpeg", "1b96843fd5f0887e44c86d80e7f700be3ba6adef4", "1", "0", "5981767826106005004"};

	  try{
        client.put2bsfile(fds);
         
      }catch(IOException e){

         e.printStackTrace();
      }catch(InterruptedException e){

         e.printStackTrace();
      }	
        

		System.out.println("go out");

    }

*/

}
