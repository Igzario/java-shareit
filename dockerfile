from amazoncorretto:11-alpine-jdk 
copy target/shareit-0.0.1-SNAPSHOT.jar /app.jar 
cmd ["java","-jar","/app.jar"]
