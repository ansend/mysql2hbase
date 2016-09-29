#! /bin/bash

sudo -s cp /etc/hosts /etc/hosts.bakup


 sudo -s cat << EOF >> /etc/hosts
#temp change for bs2 table import
183.36.121.237 hadoop-svr1
183.36.121.245 hadoop-svr2
183.36.121.247 hadoop-svr3
183.36.121.30 hadoop-svr4
183.36.121.31 hadoop-svr5
183.36.121.43 hadoop-svr6

10.26.7.223  hbase-svr1.bs2vod.hbasetest.com
10.26.7.231  hbase-svr2.bs2vod.hbasetest.com
10.26.7.233  hbase-svr3.bs2vod.hbasetest.com
10.26.1.30  hbase-svr4.bs2vod.hbasetest.com
10.26.1.31  hbase-svr5.bs2vod.hbasetest.com
10.26.1.43  hbase-svr6.bs2vod.hbasetest.com
EOF


