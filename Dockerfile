FROM eclipse-temurin:17-jre
WORKDIR /retailer-rewards
COPY target/retailer-rewards-0.0.1-SNAPSHOT.jar retailer-rewards-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","retailer-rewards-0.0.1-SNAPSHOT.jar"]
