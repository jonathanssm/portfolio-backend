# 🔽 Multi-stage build para imagem mínima
FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw && \
    ./mvnw -B clean package -DskipTests

# 🎯 Imagem final minimalista
FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="Your Team <team@example.com>"

# 📦 Instala apenas o necessário para healthcheck
RUN apk add --no-cache curl && \
    addgroup -S spring && adduser -S spring -G spring

WORKDIR /app
USER spring

# 📦 Copia o JAR do stage builder
COPY --from=builder /app/target/portfolio-backend-0.0.1-SNAPSHOT.jar app.jar

# 🔧 Otimizações JVM para containers
ENV JAVA_OPTS="-XX:InitialRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0 -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xms128m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENV JAVA_TOOL_OPTIONS="-XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

# 🩺 Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 🚀 Entrypoint otimizado
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
