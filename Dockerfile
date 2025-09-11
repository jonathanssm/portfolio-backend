# Imagem base leve com JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copiar o JAR construído
COPY portfolio-backend.jar app.jar

# Expor a porta padrão do Spring Boot
EXPOSE 8080

# Variáveis de ambiente para controlar memória do Java
ENV JAVA_OPTS="-Xms128m -Xmx512m"

# Healthcheck para monitorar se a aplicação está rodando
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Entry point que aplica as variáveis de memória
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
