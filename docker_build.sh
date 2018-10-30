cp ../production/spin-to-win-backend.properties ./src/main/resources/application.properties

mvn clean install
sudo docker build . -t eqd-slots
