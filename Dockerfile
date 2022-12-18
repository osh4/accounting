FROM openjdk:18
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY target/*.jar accounting.jar
EXPOSE 8080
CMD ["java", "-jar", "accounting.jar"]
