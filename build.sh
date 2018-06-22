#!/bin/sh
echo "============begin package=========="

rm -rf target
mvn clean package

rm -rf output
mkdir -p output/webapps

mv target/*.war output/webapps/privilege.war

echo "=============deploy end============="
