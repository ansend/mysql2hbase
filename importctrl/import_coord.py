#!/usr/bin/env python
import logging
import os.path
import os
import time
import subprocess
import threading

FORMAT = '%(asctime)s-%(thread)d-%(levelname)s-%(message)s'
logging.basicConfig(format=FORMAT, level=logging.DEBUG)


def get_num_of_hbase_store_file():
    r_cmd = '''hadoop/bin/hdfs dfs -count /hbase/data/default/bs2_copy|grep "default/bs2_copy"|awk '{print $2}' '''
    cmd = ['ssh', '-p32200', 'hadoop@10.26.7.223', r_cmd]
    logging.debug("start to exec:%s", cmd)
    num_store_file = subprocess.check_output(cmd)
    logging.info("store file %s", num_store_file)
    return int(num_store_file)

def import_coordinate():
    while True:
        count = 0
        try:
           num_store_file = get_num_of_hbase_store_file()
           if num_store_file > 140000:
               if not os.path.isfile('pause'):
                   logging.info("create pause file. store file %d", num_store_file) 
                   open('pause', 'w').close()
           elif num_store_file < 100000:
               if os.path.isfile('pause'):
                   logging.info("remove pause file. store file %d", num_store_file)
                   os.remove('pause')

        except:
           logging.exception("Unexpect exception!")
        time.sleep(10)

def trigger_major_compact():
    r_cmd = '''echo major_compact \\'bs2_copy\\' >cmd.tmp '''
    cmd = ['ssh', '-p32200', 'hadoop@10.26.7.223', r_cmd]
    logging.debug("start to exec:%s", cmd)
    subprocess.call(cmd)
    r_cmd = '''echo exit>>cmd.tmp '''
    cmd = ['ssh', '-p32200', 'hadoop@10.26.7.223', r_cmd]
    logging.debug("start to exec:%s", cmd)
    subprocess.call(cmd)
    r_cmd = '''hbase/bin/hbase shell cmd.tmp '''
    cmd = ['ssh', '-p32200', 'hadoop@10.26.7.223', r_cmd]
    logging.debug("start to exec:%s", cmd)
    subprocess.call(cmd)


def major_compactor(interval):
    while True:
        try:
            if os.path.isfile('pause'):
                trigger_major_compact()
        except:
            logging.exception("Unexpect exception!")
        time.sleep(interval)

if __name__ == "__main__":
    thr = threading.Thread(target=major_compactor, args=(1800,))
    thr.daemon = True
    thr.start()

    import_coordinate()
