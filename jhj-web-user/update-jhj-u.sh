cd /data/src/jhj-parent/jhj-web-user
git reset --hard HEAD
git pull
cd /data/src/jhj-parent/jhj-web-user

npm install grunt grunt-cli --save-dev
npm install grunt-legacy-util grunt-legacy-log  grunt-contrib-cssmin grunt-contrib-connect grunt-contrib-concat grunt-contrib-less grunt-contrib-watch grunt-contrib-uglify load-grunt-tasks grunt-css asset-cache-control --save-dev
npm install
rm -rf /data/src/jhj-parent/jhj-web-user/html

grunt
\cp  index1.html index.html
rm -rf /data/src/jhj-parent/jhj-web-user/index1.html
\cp -rf * /data/tomcat/webapps/u 

