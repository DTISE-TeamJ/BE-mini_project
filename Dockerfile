FROM maven:3.9.8-sapmachine-21 AS build
WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline -B
RUN echo "done"

# Copy source files
COPY src ./src

# Build the application
RUN mvn package -DskipTests

FROM openjdk:21-slim
WORKDIR /app
LABEL maintainer="dtise-j@mail.com"
LABEL company="Mini Project - J"
COPY --from=build /app/target/*.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]