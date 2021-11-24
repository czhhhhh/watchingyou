#!/bin/bash

$FLUME_HOME/bin/flume-ng agent --conf $FLUME_HOME/conf -f $FLUME_HOME/conf/flume-taildir-memory-hdfs.conf -Dflume.root.logger=INFO.console -n a1

