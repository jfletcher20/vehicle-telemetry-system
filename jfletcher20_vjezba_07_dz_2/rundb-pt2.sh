#!/bin/bash
sudo java -Dfile.encoding=UTF-8 -cp /opt/h2/bin/h2-2.2.224.jar org.h2.tools.Server -tcp -tcpAllowOthers -webAllowOthers -baseDir /opt/database -ifNotExists
