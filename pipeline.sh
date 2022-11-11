cd dashboard
mvn clean package -Dmaven.test.skip=true
docker rm -f $(docker ps --filter "name=dashboard" -q -a)
docker images | grep "dashboard" | awk '{print $1":"$2}' | xargs docker rmi
docker build -t dashboard .
docker run --name my_dashboard-1 --privileged=true --restart=always  -p 8877:8081 -d dashboard
docker run --name my_dashboard-2 --privileged=true --restart=always  -p 8878:8081 -d dashboard
