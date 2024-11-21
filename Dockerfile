FROM openjdk:17
RUN groupadd deployuser && useradd -m -g deployuser -d /home/deployuser -s /bin/bash deployuser
USER deployuser:deployuser
ENV SERVICE_HOME=/usr/accounting
WORKDIR $SERVICE_HOME
ARG JAR_FILE
COPY ${JAR_FILE} accounting.jar
EXPOSE 8443
ENTRYPOINT ["sh", "-c", "java -jar ${SERVICE_HOME}/accounting.jar"]