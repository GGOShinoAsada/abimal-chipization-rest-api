для развертывания 
1. перебилдить проект
gradle clean
gradle build -x test

2. сбилдить образ webapi

docker build -t webapi .

3. запустить docker-compose

docker-compose up

PS для соединения образа стоит указать имя образа контейнера в строке подключения (в данном случае замена localhost на postgres) 