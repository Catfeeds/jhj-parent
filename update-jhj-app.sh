cd /data/src/jhj-parent2.1/
svn up
mvn clean package
/data/tomcat/bin/shutdown.sh
sleep 1
rm -rf /data/tomcat/webapps/jhj-app
\cp -rf /data/src/jhj-parent2.1/jhj-app/target/jhj-app.war /data/tomcat/webapps/
/data/tomcat/bin/startup.sh

