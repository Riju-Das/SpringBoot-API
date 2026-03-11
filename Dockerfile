FROM eclipse-temurin:latest
ADD target/content-calender.jar content-calender.jar
ENTRYPOINT ["java", "-jar", "content-calender.jar"]