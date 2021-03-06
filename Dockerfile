FROM openjdk:8

USER root

COPY target/*.jar /opt/app/
COPY src/main/resources/devops/azure/*.json /opt/app/devops-info/azure/
 	 
WORKDIR /opt/app

EXPOSE 8099
ENTRYPOINT ["java", "-jar", "devops-workitem-sync-0.0.1-SNAPSHOT.jar"]
