FROM amazoncorretto:11
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Duser.timezone=UTC","-jar","/app.jar"]