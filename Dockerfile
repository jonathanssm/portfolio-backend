# Imagem base leve com JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Diretório de trabalho
WORKDIR /app

# Recebe o nome do JAR como build-arg
ARG JAR_FILE=portfolio-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Instala wget para healthcheck
RUN apk add --no-cache wget

# Exposição da porta do Spring Boot
EXPOSE 8080

# Configuração de memória JVM
ENV JAVA_OPTS="-Xms128m -Xmx512m"

# Healthcheck seguro
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# ENTRYPOINT otimizado, garantindo sinais corretos para shutdown
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
