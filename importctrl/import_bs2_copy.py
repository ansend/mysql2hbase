#!/usr/bin/env python

import Queue
import time
import logging
import shlex
import subprocess
import threading
import os.path
import pymysql
import os
import copy

busi_name = 'bs2_vod'
room_no = '2'
metadb_host='58.215.180.149'
metadb_port=6301
metadb_user='rds_rw'
metadb_passwd='ewqfRFQ23K'
metadb_database='myshard_metadata'
db_name='myshard'
table_name='bs2_copy'


FORMAT = '%(asctime)s-%(thread)d-%(levelname)s-%(message)s'
logging.basicConfig(format=FORMAT, level=logging.DEBUG)

task_count = 0
task_count_lock = threading.Lock()

def do_task(t):
    r_cmd = '/tmp/bs2_import/bs2copy_import.sh %s %s' % (t['port'], t['tablename'])
    cmd = ['ssh', '-p32200', t['ipAddress'], r_cmd]
    logging.debug("start to exec:%s", cmd)
    ret = subprocess.call(cmd)
   
    r_cmd = 'cat /tmp/bs2_import/log/ret_%s_%s' % (t['port'], t['tablename'])
    cmd = ['ssh', '-p32200', t['ipAddress'], r_cmd]
    out = subprocess.check_output(cmd)
    if 0 != int(out): 
       logging.debug("ERROR!!! ret: %s  exec:%s", out, cmd )

def is_paused():
    if os.path.exists('pause') or os.path.exists('pause1'):
        return True
    return False

def import_worker(task_q):
    global task_count
    global task_count_lock
    try:
        while True:
            t = task_q.get(True)
            logging.info("Got task %s", t)
            if is_paused():
                logging.info("In paused status, sleep 10 second and recheck.")
                task_q.put(t)
                time.sleep(10)
                continue
            try:
                t1 = time.time()
                do_task(t)
                t2 = time.time()
                task_count_lock.acquire()
                task_count = task_count - 1
                task_count_lock.release()
                logging.info("Task done. cost: %f task: %s", t2 - t1, t)
            except:
                logging.exception("Fail to do task: %s. ignore and looking for next task.", t)
    except:
        logging.exception("Unexpected exception!")

def gen_task(q_list):
    global task_count
    global task_count_lock
    metadb = pymysql.connect(host=metadb_host, port=metadb_port, user=metadb_user, passwd=metadb_passwd, database=metadb_database)
    metacur = metadb.cursor()
    # get all mysql servers info
    metacur.execute('SELECT `server_name`, `property`, `value` FROM `myshard_server_con_config` WHERE `busi_name`=%s AND `machine_room_no`=%s', (busi_name, room_no))
    mysqlinfox = {}
    for row in metacur:
        server = row[0]
        property = row[1]
        value = row[2]
        if not server in mysqlinfox:
            mysqlinfox[server] = {}
        mysqlinfox[server][property] = value
    for mysqlinfo in mysqlinfox.values():
        partitiondb = pymysql.connect(host=mysqlinfo['ipAddress'], 
                                    port=int(mysqlinfo['port']), 
                                    user=mysqlinfo['user'], 
                                    passwd=mysqlinfo['password'],
                                    database=db_name,
                                    autocommit=True)
        partitioncur = partitiondb.cursor()
        # get all tables in that database
        partitioncur.execute('SHOW TABLES')
        tableshards=list()
        for row in partitioncur:
            tablename = row[0][:row[0].rfind('_')]
            shard_no = row[0][row[0].rfind('_')+1:]
            if tablename == table_name:
                p = int(shard_no) % 80
                t = copy.copy(mysqlinfo)
                t['tablename'] = row[0]
                task_count_lock.acquire() 
                task_count = task_count + 1
                task_count_lock.release()
                q_list[p].put(t)
        partitiondb.close()
    metadb.close()

def main():
    global task_count

    q_list = []
    for i in xrange(0, 80):
        task_q = Queue.Queue()
        thr = threading.Thread(target=import_worker, args=[task_q])
        thr.daemon = True
        thr.start()
        q_list.append(task_q)
  
    gen_task(q_list)

    while True:
        time.sleep(1)
        logging.info("task count:%d", task_count)

if __name__ == "__main__":
    main()
