cd /data/src/jhj-parent/
git pull
mvn clean package
/data/tomcat/bin/shutdown.sh
sleep 1
rm -rf /data/tomcat/webapps/jhj-oa
\cp -rf /data/src/jhj-parent/jhj-oa/target/jhj-oa.war /data/tomcat/webapps/
/data/tomcat/bin/startup.sh