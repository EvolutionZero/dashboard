rm -rf dashboard
git clone  -b develop http://root:Admin123%21%40%23@192.168.3.100:88/midas/dashboard.git
cd dashboard
mvn clean package -Dmaven.test.skip=true
docker rm -f $(docker ps --filter "name=dashboard" -q -a)
docker images | grep "dashboard" | awk '{print $1":"$2}' | xargs docker rmi
docker build -t dashboard .
docker run --name my_dashboard --cpus="0.5" -m 512m --restart=always  -p 8877:8081 -d dashboard
