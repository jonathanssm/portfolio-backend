# Imagem base leve com JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Recebe o nome do JAR como build-arg
ARG JAR_FILE=portfolio-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Instala wget para healthcheck
RUN apk add --no-cache wget

EXPOSE 8080
ENV JAVA_OPTS="-Xms128m -Xmx512m"

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Aplica JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
