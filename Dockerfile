FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR com o nome fixo que o workflow envia
COPY portfolio-backend-0.0.1-SNAPSHOT.jar app.jar

# Instala curl para healthcheck (mais leve)
RUN apk add --no-cache curl

EXPOSE 8080

# Configuração de memória JVM
ENV JAVA_OPTS="-Xms128m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
