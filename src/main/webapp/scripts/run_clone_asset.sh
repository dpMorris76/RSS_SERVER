#! /bin/bash
####################################################################
#   PROCEDURE:   run_mad_email_reader.sh
#     PURPOSE:   Script to run program to read in emails 
#					for mad ticket creation 
#      FORMAT:   run_mad_email_reader.sh 
#
#  CREATED BY:   Ted Cabato
#  MODIFIED BY:  Mark Morris
####################################################################

CLASSPATH=$CLASSPATH:WEB-INF/
CLASSPATH=$CLASSPATH:WEB-INF/classes/
CLASSPATH=$CLASSPATH:WEB-INF/lib/*

echo CLASSPATH = $CLASSPATH

java -Xmx512M -classpath $CLASSPATH com.centurylink.rss.business.service.RunScheduler >> scripts/logs/RSSRunScheduler.dlog 2>&1