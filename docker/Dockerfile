FROM supercorp/supergateway

# Install OpenJDK 21 on Alpine
RUN apk add --no-cache openjdk21

# Set JAVA_HOME (optional, but good practice)
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"
