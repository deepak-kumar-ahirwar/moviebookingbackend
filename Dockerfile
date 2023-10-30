FROM openjdk:17
EXPOSE 8080
ADD target/moviebookingapp-0.0.1-SNAPSHOT.jar moviebookingapp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["sh","-c","java -jar /moviebookingapp-0.0.1-SNAPSHOT.jar"]