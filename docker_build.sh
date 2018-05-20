cp ./prod_cfg/application.properties ./src/main/resources/application.properties

mvn clean install
sudo docker build . -t eqd-slots
