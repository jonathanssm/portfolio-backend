FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# ✅ Copia o JAR com nome correto
COPY portfolio-backend-0.0.1-SNAPSHOT.jar app.jar

# ✅ Instala curl mínimo
RUN apk add --no-cache curl

EXPOSE 8080

# ✅ Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# ✅ Entrypoint simples
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
