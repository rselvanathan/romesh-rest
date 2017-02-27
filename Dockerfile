FROM java:openjdk-8
ADD target/universal/romesh-rest-1.0.zip /
RUN unzip -q /romesh-rest-1.0.zip && sh -c 'chmod +x /romesh-rest-1.0/bin/romesh-rest'
EXPOSE 9000
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "./romesh-rest-1.0/bin/romesh-rest"]