FROM supercorp/supergateway

RUN apk add --no-cache openjdk21

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app
COPY server/build/libs/*.jar /mind-mcp.jar
EXPOSE 8080

