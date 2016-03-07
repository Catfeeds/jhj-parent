cd /data/src/jhj-parent2.0/
svn up
mvn clean package
/data/tomcat/bin/shutdown.sh
sleep 1
rm -rf /data/tomcat/webapps/jhj-oa
\cp -rf /data/src/jhj-parent2.0/jhj-oa2.0/target/jhj-oa.war /data/tomcat/webapps/
/data/tomcat/bin/startup.sh