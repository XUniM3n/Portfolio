FROM java

COPY target/*.jar /usr/app/
COPY Images/* /opt/serverimage/
RUN for f in /usr/app/*.jar; do mv "$f" /usr/app/app.jar; done
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
