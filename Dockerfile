FROM selenium/standalone-chrome:98.0-chromedriver-98.0-20220208
VOLUME /tmp
ADD ./target/dashboard*.jar dashboard.jar
COPY ./arthas-bin /opt/selenium/arthas
ENTRYPOINT ["java", "-Dlog.home=/opt/selenium/", "-Dlog.appender.ref=STDOUT" , "-Duser.timezone=GMT+08", "-Dfile.encoding=UTF-8","-jar","/dashboard.jar"]