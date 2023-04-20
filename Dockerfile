FROM openjdk:8

VOLUME /tmp

EXPOSE 8080

ADD target/Jago-IT-BE-0.0.1-SNAPSHOT.jar Jago-IT-BE-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/Jago-IT-BE-0.0.1-SNAPSHOT.jar"] 