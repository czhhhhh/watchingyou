#!/bin/bash

yesterday=`date --date='1 days ago' +%Y-%m-%d`
echo ${yesterday}

export JAVA_HOME=/opt/model/jdk1.8

/opt/model/sqoop2/bin/sqoop2-shell /opt/data/project/job_hdfs2mysql.sqoop