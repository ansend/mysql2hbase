#! /bin/bash

if [ $# -lt 2 ];then
                echo "usage: `basename $0` port tablename"
                        exit
fi

cd /tmp/bs2_import

date=$(date +"%Y-%m-%d-%H-%M.%S")

port=$1

tblname=$2

dbpass=$(cat /etc/snmp/yyms_agent_db_scripts/db_6301.conf | grep password | sed 's/^password=//g' | sed 's/ //g')

log_file=bs2import_${port}_${tblname}_${date}

echo "mysql port:" ${port}

echo "mysql tablename:" ${tblname}

if [ -z "$dbpass" ]
   then
        echo "no password found"
                exit 1
   else
       echo "password: " "${dbpass}"
fi

 ./jdk1.7.0_79/bin/java -cp ./java:./java/lib/mysql-connector-java-5.1.39/*:./java/lib/hbase-0.98.17_lib/*  Mysql2HbaseFile ${port} ${tblname} ${dbpass} >./log/$log_file 2>&1 

echo "$?" > ./log/out_${port}_${tblname}



