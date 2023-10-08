#!/bin/sh
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

usage="Usage: platform-daemon.sh (start|stop|status)"

# if no args specified, show usage
if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi

startStop=$1
shift
command=$1
shift

echo "Begin $startStop $command......"

BIN_DIR=$(dirname $0)
DATA_OPEN_API_HOME=${DATA_OPEN_API_HOME:-$(cd $BIN_DIR/..; pwd)}

export STOP_TIMEOUT=5

pid=$DATA_OPEN_API_HOME/pid
cd $DATA_OPEN_API_HOME/

export DATA_OPEN_API_LOG_DIR=$DATA_OPEN_API_HOME/logs

log=$DATA_OPEN_API_LOG_DIR/open-data-platform.out


if [ ! -d "$DATA_OPEN_API_LOG_DIR" ]; then
  mkdir $DATA_OPEN_API_LOG_DIR
fi

case $startStop in
  (start)
    echo starting $command, logging to $DATA_OPEN_API_HOME
    nohup /bin/sh "$DATA_OPEN_API_HOME/bin/start.sh" > $log 2>&1 &
    echo $! > $pid
    ;;

  (stop)
      if [ -f $pid ]; then
        TARGET_PID=`cat $pid`
        if kill -0 $TARGET_PID > /dev/null 2>&1; then
          echo stopping $command
          pkill -P $TARGET_PID
          sleep $STOP_TIMEOUT
          if kill -0 $TARGET_PID > /dev/null 2>&1; then
            echo "$command did not stop gracefully after $STOP_TIMEOUT seconds: killing with kill -9"
            pkill -P -9 $TARGET_PID
          fi
        else
          echo no $command to stop
        fi
        rm -f $pid
      else
        echo no $command to stop
      fi
      ;;

  (status)
    # more details about the status can be added later
    serverCount=`ps -ef | grep "$DATA_OPEN_API_HOME" | grep "$CLASS" | grep -v "grep" | wc -l`
    state="STOP"
    #  font color - red
    state="[ \033[1;31m $state \033[0m ]"
    if [[ $serverCount -gt 0 ]];then
      state="RUNNING"
      # font color - green
      state="[ \033[1;32m $state \033[0m ]"
    fi
    echo -e "$command  $state"
    ;;

  (*)
    echo $usage
    exit 1
    ;;

esac

echo "End $startStop $command."
