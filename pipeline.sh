cd dashboard
git branch
git log -1
mvn clean package -Dmaven.test.skip=true
rc=$?
if [[ $rc -ne '0' ]] ; then
  echo '编译失败'; exit $rc
fi
docker rm -f $(docker ps --filter "name=dashboard" -q -a)
docker images | grep "dashboard" | awk '{print $1":"$2}' | xargs docker rmi
docker build -t dashboard .
docker run --name my_dashboard-1 --privileged=true --restart=always  -p 8877:8081 -d dashboard
docker run --name my_dashboard-2 --privileged=true --restart=always  -p 8878:8081 -d dashboard
docker run --name my_dashboard-3 --privileged=true --restart=always  -p 8879:8081 -d dashboard
docker run --name my_dashboard-4 --privileged=true --restart=always  -p 8890:8081 -d dashboard
