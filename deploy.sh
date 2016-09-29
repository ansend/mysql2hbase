#! /bin/bash


declare -a array

#array[0]="183.36.121.237"
#array[1]="183.36.121.45"

i=0;

for line in $(cat ./mysqlips.dat)
do
     echo "server:${line}"
	 array[$i]=${line}
	 i=`expr $i + 1`
	 #echo "read strings ... `expr $i + 1`"
done

for svr in ${array[*]} 
do
    echo $svr
  # ssh -p32200 $svr "rm -r /tmp/bs2_import"
  # scp -rp ../bs2_import $svr:/tmp
  scp -rp ../bs2_import/*.sh $svr:/tmp/bs2_import
done
