rm -rf dashboard
git clone -b develop http://root:Admin123%21%40%23@192.168.3.100:88/midas/dashboard.git
cp ./dashboard/pipeline.sh ./pipeline.sh
chmod 777 pipeline.sh
sh pipeline.sh