FROM openjdk:11
ADD build/libs/server-side-template-*.jar /server-side-template.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/server-side-template.jar"]