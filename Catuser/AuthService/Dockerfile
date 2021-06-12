FROM java

COPY target/*.jar /usr/app/
RUN for f in /usr/app/*.jar; do mv "$f" /usr/app/app.jar; done
EXPOSE 9999
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
