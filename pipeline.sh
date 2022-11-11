cd dashboard
mvn clean package -Dmaven.test.skip=true
docker rm -f $(docker ps --filter "name=dashboard" -q -a)
docker images | grep "dashboard" | awk '{print $1":"$2}' | xargs docker rmi
docker build -t dashboard .
docker run --name my_dashboard-1  --shm-size="500M" --privileged=true --restart=always  -p 8877:8081 -d dashboard
