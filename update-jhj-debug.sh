cd /data/src/jhj-parent/
git reset --hard HEAD
git pull

sed -i "s#^debug=.*#debug=true#g"  /data/src/jhj-parent/jhj-app/src/main/resources/config.properties
mvn clean package
/data/tomcat/bin/shutdown.sh
sleep 1
rm -rf /data/tomcat/webapps/jhj-app
\cp -rf /data/src/jhj-parent/jhj-app/target/jhj-app.war /data/tomcat/webapps/

rm -rf /data/tomcat/webapps/jhj-oa
\cp -rf /data/src/jhj-parent/jhj-oa/target/jhj-oa.war /data/tomcat/webapps/


/data/tomcat/bin/startup.sh

