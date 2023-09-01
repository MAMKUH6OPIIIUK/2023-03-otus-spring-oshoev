FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY /target/spring-hw-18-*.jar /library/library-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/library/library-app.jar"]