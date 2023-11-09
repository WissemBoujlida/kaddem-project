FROM openjdk:11
EXPOSE 9089
RUN wget --user=admin --password=nexus http://192.168.1.13:8081/repository/maven-releases/tn/esprit/KaddemProject/1.0/KaddemProject-1.0.jar
ENTRYPOINT ["java","-jar","/KaddemProject-1.0.jar"]
