#!/bin/sh
echo "============begin package=========="

rm -rf target
#开始打包
mvn clean package -DskipTests=true

cd target/

mv udata-privilege.war /home/work/udata_tomcat/webapps/privilege.war

echo "=============package end============="

