FROM eclipse-temurin:23-jre-alpine

LABEL maintainer="ePessoa Team <suporte@epessoa.gov.br>"
LABEL description="Sistema de Cadastro de Pessoas - Backend API"
LABEL version="1.0.0"

# Criar diretório de trabalho
WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar o JAR da aplicação
COPY target/epessoa-1.0.0.jar app.jar

# Alterar propriedade do arquivo
RUN chown spring:spring app.jar

# Usar usuário não-root
USER spring:spring

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Variáveis de ambiente padrão
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Comando de execução
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

