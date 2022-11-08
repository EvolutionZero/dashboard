FROM selenium/standalone-chrome:98.0-chromedriver-98.0-20220208
RUN mkdir /opt/selenium/opt
RUN mkdir /opt/selenium/opt/dashboard
RUN mkdir /opt/selenium/opt/dashboard/temp
RUN mkdir /opt/selenium/opt/dashboard/logs
RUN mkdir /opt/selenium/opt/dashboard/report
RUN mkdir /opt/selenium/opt/dashboard/report/html
RUN mkdir /opt/selenium/opt/dashboard/report/pdf
RUN mkdir /opt/selenium/opt/dashboard/report/png
RUN mkdir /opt/selenium/opt/dashboard/script
ADD ./target/classes/velocity/echarts.zero.js /opt/selenium/opt/dashboard/script/echarts.zero.js
ADD ./target/classes/301158.html /opt/selenium/opt/dashboard/script/301158.html
ADD ./target/classes/velocity/jquery /opt/selenium/opt/dashboard/script/jquery
ADD ./target/dashboard*.jar /opt/selenium/opt/dashboard/dashboard.jar
COPY ./arthas-bin /opt/selenium/opt/arthas
ENTRYPOINT ["java", "-Dlog.home=/opt/selenium/opt/dashboard/logs", "-Dlog.appender.ref=STDOUT" , "-Duser.timezone=GMT+08", "-Dfile.encoding=UTF-8","-jar","/opt/selenium/opt/dashboard/dashboard.jar"]