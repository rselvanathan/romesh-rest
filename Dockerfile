FROM develar/java
ADD target/scala-2.11/romesh-rest_2.11-1.0.jar app.jar
EXPOSE 9000
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]