FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app
COPY ./build/libs/auth-service.jar /app/auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
