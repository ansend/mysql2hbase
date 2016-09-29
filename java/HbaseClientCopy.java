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

public class HbaseClientCopy {

   final static byte[] SRV_COL_FAM = "bs2_copy".getBytes();
   final static int NUM_FIELDS = 16;

   HTable hTable;

   long count;

   Configuration  cfg = null;


   public HbaseClientCopy() 
   {
      /*  cfg  = HBaseConfiguration.create();
        cfg.set("hbase.zookeeper.quorum", "183.36.121.237,183.36.121.245,183.36.121.247");

        hTable = new HTable(cfg, "test:bs2_copy");

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
                        hTable = new HTable(cfg, "test:bs2_copy");
                        hTable.setAutoFlush(false);
                        hTable.setWriteBufferSize(10485760);
                }catch(IOException e){
                        e.printStackTrace();
                        throw e;
                }

      System.out.println("connectted to Hbase !!!\n");
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
 
 public void put2bscopy(String[] fields)
      throws IOException, InterruptedException {



    long timestamp = Long.parseLong(fields[7]);

    long l_zoneid = Long.parseLong(fields[1]);

    String rowkey = String.format("%s%010d%s", fields[0], l_zoneid, fields[2]);


	System.out.println("rowkey :"+rowkey);
    Put put = new Put(rowkey.getBytes());

 

    put.setWriteToWAL(false);
    put.add(SRV_COL_FAM,
        HColumnEnum.SRV_COL_V_CONTENT_KEY.getColumnName(), timestamp, fields[0].getBytes());


    put.add(SRV_COL_FAM,
            HColumnEnum.SRV_COL_ZONE_ID.getColumnName(), timestamp, fields[1].getBytes());


    put.add(SRV_COL_FAM,
            HColumnEnum.SRV_COL_V_ZFILENAME_DIGEST.getColumnName(), timestamp, fields[2].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnum.SRV_COL_ZONE_FILE.getColumnName(), timestamp, fields[3].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnum.SRV_COL_UPDATE_TIME.getColumnName(), timestamp, fields[4].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnum.SRV_COL_STATUS.getColumnName(), timestamp, fields[5].getBytes());

    put.add(SRV_COL_FAM,
            HColumnEnum.SRV_COL_N_SRC.getColumnName(), timestamp, fields[6].getBytes());
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


        HbaseClientCopy client = new HbaseClientCopy();

		String[] fds = {"000002a3d74d97569488cc5d3e8827ae2c2f06ee", "32432505", "1c884e2857b28f4cb82d512a7b667f795a0fb06a0", "yysnapshot/b96843fd5f0887e44c86d80e7f700be3ba6adef4","2016-04-12 00:35:35", "1","0", "5999424337091180043"};

	  try{
        client.put2bscopy(fds);
         
      }catch(IOException e){

         e.printStackTrace();
      }catch(InterruptedException e){

         e.printStackTrace();
      }	
        

		System.out.println("go out");

    }

	*/

}
