FROM supercorp/supergateway

# Install OpenJDK 21 on Alpine
RUN apk add --no-cache openjdk21

# Set JAVA_HOME (optional, but good practice)
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV MIND_USERNAME="flyingtoaster0"
ENV MIND_PASSWORD="wtO6qTRTeugPBV7PV8vEiAhgrR7f2IHA"
ENV MIND_BASE_URL="http://10.88.111.213:8086/api"
ENV MIND_TIMEZONE="America/Toronto"
ENV REDIS_HOST="redis"
