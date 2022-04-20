// maven构建

mvn clean package -Dmaven.test.skip=true

// 构建命令

docker build -t  dashboard .

// 启动命令

docker run --name my_dashboard --shm-size="500M"  -p 8888:8080 -d dashboard

// 进入容器
docker exec -it xxxx /bin/bash

// 删除容器
docker rm -f xxx 