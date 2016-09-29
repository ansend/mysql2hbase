/**
 * HBase table columns for the 'bs2_copy' column family
 */
public enum HColumnEnum {
  SRV_COL_V_CONTENT_KEY ("v_content_key".getBytes()),
  SRV_COL_ZONE_ID ("zone_id".getBytes()),
  SRV_COL_V_ZFILENAME_DIGEST ("v_zfilename_digest".getBytes()),
  SRV_COL_ZONE_FILE ("zone_file".getBytes()),
  SRV_COL_UPDATE_TIME ("update_time".getBytes()),
  SRV_COL_STATUS ("status".getBytes()),
  SRV_COL_N_SRC("n_src".getBytes());
  
 
  private final byte[] columnName;
  
  HColumnEnum (byte[] column) {
    this.columnName = column;
  }

  public byte[] getColumnName() {
    return this.columnName;
  }
}
