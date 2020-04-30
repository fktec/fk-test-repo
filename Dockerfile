FROM openjdk:8

USER root

COPY target/*.jar /home/Documents/
COPY src/main/resources/devops/azure/*.json /home/Documents/devops/
 	 
WORKDIR /home/Documents

EXPOSE 8099
ENTRYPOINT ["java", "-jar", "devops-workitem-sync-0.0.1-SNAPSHOT.jar"]
