/**
 * HBase table columns for the 'bs2_file' column family
 */

public enum HColumnEnumFile {
  SRV_COL_V_FILENAME_DIGEST ("v_filename_digest".getBytes()),
  SRV_COL_UPLOAD_ID ("upload_id".getBytes()),
  SRV_COL_BUCKET ("bucket".getBytes()),
  SRV_COL_FILE_NAME ("file_name".getBytes()),
  SRV_COL_UPLOAD_ZONE_ID ("upload_zone_id".getBytes()),
  SRV_COL_UPDATE_TIME ("update_time".getBytes()),
  SRV_COL_SIZE ("size".getBytes()),
  SRV_COL_STATUS ("status".getBytes()),
  SRV_COL_MINE ("mime".getBytes()),
  SRV_COL_V_CONTENT_KEY ("v_content_key".getBytes()),
  SRV_COL_FILE_TYPE  ("file_type".getBytes()),
  SRV_COL_N_IS_CACULATE_SIZE  ("n_is_caculate_size".getBytes()),
  SRV_COL___VERSION ("__version".getBytes());

  private final byte[] columnName;

  HColumnEnumFile (byte[] column) {
    this.columnName = column;
  }

  public byte[] getColumnName() {
    return this.columnName;
  }
}
