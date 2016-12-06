#! /bin/bash

#input: network interface name, eg. eth0 or eth1

function get_ip_addr() {
  eth=$1
  MASTERIP=$(ifconfig ${eth} | grep 'inet addr' | sed 's/^.*addr://g' | sed 's/  Bcast.*$//g')
  echo "$MASTERIP"

}
:<<EOF

eth0=`get_ip_addr eth0`

echo ${eth0}

eth1=`get_ip_addr eth1`

echo ${eth1}

EOF
