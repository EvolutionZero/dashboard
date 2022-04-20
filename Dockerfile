FROM selenium/standalone-chrome:98.0-chromedriver-98.0-20220208
VOLUME /tmp
ADD ./target/dashborad*.jar dashborad.jar
#RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#    && echo "Asia/Shanghai" > /etc/timezone

COPY 98.0.4758.102-google-chrome-stable_current_x86_64.rpm /opt/98.0.4758.102-google-chrome-stable_current_x86_64.rpm
COPY chromedriver /opt/chromedriver
ENTRYPOINT ["java","-Dmidas.threshold=4", "-Dspring.datasource.hikari.maximum-pool-size=24", "-Dfile.encoding=UTF-8","-jar","/dashborad.jar"]