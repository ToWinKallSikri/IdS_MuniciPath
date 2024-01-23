FROM openjdk:17-jdk-alpine
EXPOSE 1234
COPY target/Municipath-0.0.1.jar Municipath-0.0.1.jar
COPY data/municipath.mv.db data/municipath.mv.db
ENTRYPOINT ["java", "-jar", "Municipath-0.0.1.jar"]