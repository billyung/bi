#!/bin/bash
#
#drop and recreate the DW and then recreate and reload PPI tables
#
#example: full_reload mifos_ppi_dw ~/pentaho-data-integration ~/reportingWorkspace/bi

if [ $# -lt 2 ]
then
    echo "Usage: `basename $0` <DB_NAME> <PDI_HOME>"
    exit 1
fi

PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`

DB_NAME=$1
PDI_HOME=$2
MYSQL_ARGS="-u root"
echo "drop database $DB_NAME" | mysql $MYSQL_ARGS
echo "create database $DB_NAME" | mysql $MYSQL_ARGS
mysql $MYSQL_ARGS $DB_NAME < $PRGDIR/load_mifos_datawarehouse.sql
mysql $MYSQL_ARGS $DB_NAME < $PRGDIR/load_ppi_poverty_lines.sql
$PDI_HOME/kitchen.sh /file:`readlink -f $PRGDIR/CreateDataWarehouse.kjb`