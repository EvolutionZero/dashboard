// maven构建

mvn clean package -Dmaven.test.skip=true

// 构建命令

docker build -t  dashboard .

// 启动命令

docker run --name my_dashboard --shm-size="500M" --privileged=true  -p 9091:8081 -d dashboard

// 进入容器
docker exec -it xxxx /bin/bash

// 删除容器
docker rm -f xxx 

// 容器拷贝文件

docker cp  13941f798a09:/opt/selenium/AShot_BBC_Entire_1650514428435.png    D:/AShot_BBC_Entire_1650514428435.png

// ps -ef 查看java程序的PID再进行启动

ps -ef 

cd /opt/selenium/arthas && java -jar arthas-boot.jar 1