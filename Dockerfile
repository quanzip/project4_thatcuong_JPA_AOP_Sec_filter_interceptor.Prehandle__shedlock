from openjdk:8-jdk-alpine

copy target/project-0.0.1-SNAPSHOT.jar project-0.0.1-SNAPSHOT.jar

entrypoint ["java", "-jar", "/project-0.0.1-SNAPSHOT.jar"]
