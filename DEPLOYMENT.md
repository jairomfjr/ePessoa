# Guia de Deploy - ePessoa

## 🚀 Deploy em Produção

Este guia apresenta diferentes opções para fazer deploy da aplicação ePessoa em produção.

## 📋 Preparação

### 1. Build da Aplicação

```bash
# Build com Maven (pula os testes para build mais rápido)
mvn clean package -DskipTests

# O JAR será gerado em: target/epessoa-1.0.0.jar
```

### 2. Variáveis de Ambiente Necessárias

```bash
# Database
DB_URL=jdbc:postgresql://seu-host:5432/epessoa
DB_USERNAME=seu-usuario
DB_PASSWORD=sua-senha

# JWT
JWT_SECRET=sua-chave-secreta-muito-longa-e-segura-com-no-minimo-64-caracteres

# Gov.br OAuth2
GOVBR_CLIENT_ID=seu-client-id-producao
GOVBR_CLIENT_SECRET=seu-client-secret-producao

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

## 🐳 Deploy com Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:23-jre-alpine
LABEL maintainer="ePessoa Team"

WORKDIR /app

# Copiar o JAR
COPY target/epessoa-1.0.0.jar app.jar

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Executar aplicação
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-jar", \
    "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: epessoa-postgres
    environment:
      POSTGRES_DB: epessoa
      POSTGRES_USER: epessoa_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    networks:
      - epessoa-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U epessoa_user"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build: .
    container_name: epessoa-app
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/epessoa
      DB_USERNAME: epessoa_user
      DB_PASSWORD: ${DB_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      GOVBR_CLIENT_ID: ${GOVBR_CLIENT_ID}
      GOVBR_CLIENT_SECRET: ${GOVBR_CLIENT_SECRET}
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "8080:8080"
    networks:
      - epessoa-network
    restart: unless-stopped

  nginx:
    image: nginx:alpine
    container_name: epessoa-nginx
    depends_on:
      - app
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - ./ssl:/etc/nginx/ssl:ro
    networks:
      - epessoa-network
    restart: unless-stopped

volumes:
  postgres_data:

networks:
  epessoa-network:
    driver: bridge
```

### Comandos Docker

```bash
# Build da imagem
docker build -t epessoa:1.0.0 .

# Executar com Docker Compose
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Parar
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

## ☁️ Deploy em Cloud

### 1. Heroku

```bash
# Login
heroku login

# Criar app
heroku create epessoa-app

# Adicionar PostgreSQL
heroku addons:create heroku-postgresql:hobby-dev

# Configurar variáveis de ambiente
heroku config:set JWT_SECRET=sua-chave-secreta
heroku config:set GOVBR_CLIENT_ID=seu-client-id
heroku config:set GOVBR_CLIENT_SECRET=seu-client-secret

# Deploy
git push heroku main

# Ver logs
heroku logs --tail
```

**Procfile**:
```
web: java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/epessoa-1.0.0.jar
```

### 2. AWS Elastic Beanstalk

```bash
# Instalar EB CLI
pip install awsebcli

# Inicializar
eb init -p corretto-23 epessoa-app

# Criar ambiente
eb create epessoa-prod

# Deploy
eb deploy

# Abrir no navegador
eb open
```

**.ebextensions/options.config**:
```yaml
option_settings:
  aws:elasticbeanstalk:application:environment:
    SPRING_PROFILES_ACTIVE: prod
    JWT_SECRET: sua-chave-secreta
    GOVBR_CLIENT_ID: seu-client-id
    GOVBR_CLIENT_SECRET: seu-client-secret
```

### 3. Google Cloud Platform (Cloud Run)

```bash
# Build e push para Container Registry
gcloud builds submit --tag gcr.io/seu-projeto/epessoa

# Deploy
gcloud run deploy epessoa \
  --image gcr.io/seu-projeto/epessoa \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars JWT_SECRET=sua-chave,GOVBR_CLIENT_ID=seu-id
```

### 4. Microsoft Azure (App Service)

```bash
# Login
az login

# Criar resource group
az group create --name epessoa-rg --location eastus

# Criar App Service plan
az appservice plan create --name epessoa-plan --resource-group epessoa-rg --sku B1 --is-linux

# Criar Web App
az webapp create --resource-group epessoa-rg --plan epessoa-plan --name epessoa-app --runtime "JAVA:23-java23"

# Deploy
az webapp deployment source config-zip --resource-group epessoa-rg --name epessoa-app --src target/epessoa-1.0.0.jar
```

## 🔒 Nginx como Reverse Proxy

### nginx.conf

```nginx
events {
    worker_connections 1024;
}

http {
    upstream epessoa_backend {
        server app:8080;
    }

    # Rate limiting
    limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;

    server {
        listen 80;
        server_name epessoa.gov.br www.epessoa.gov.br;

        # Redirect to HTTPS
        return 301 https://$host$request_uri;
    }

    server {
        listen 443 ssl http2;
        server_name epessoa.gov.br www.epessoa.gov.br;

        # SSL certificates
        ssl_certificate /etc/nginx/ssl/cert.pem;
        ssl_certificate_key /etc/nginx/ssl/key.pem;
        ssl_protocols TLSv1.2 TLSv1.3;
        ssl_ciphers HIGH:!aNULL:!MD5;

        # Security headers
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-Content-Type-Options "nosniff" always;
        add_header X-XSS-Protection "1; mode=block" always;
        add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;

        # Compression
        gzip on;
        gzip_types text/plain text/css application/json application/javascript text/xml application/xml text/javascript;

        # Client body size
        client_max_body_size 10M;

        location / {
            limit_req zone=api_limit burst=20 nodelay;
            
            proxy_pass http://epessoa_backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            # Timeouts
            proxy_connect_timeout 60s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
        }

        # Health check endpoint (bypass rate limiting)
        location /actuator/health {
            proxy_pass http://epessoa_backend;
            access_log off;
        }
    }
}
```

## 📊 Monitoramento

### 1. Spring Boot Actuator

Já está configurado. Endpoints disponíveis:
- `/actuator/health` - Health check
- `/actuator/info` - Informações da aplicação
- `/actuator/metrics` - Métricas

### 2. Prometheus + Grafana

Adicione ao `pom.xml`:
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Adicione ao `application.properties`:
```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.metrics.export.prometheus.enabled=true
```

### 3. Logs

Configure um agregador de logs como:
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Splunk
- CloudWatch (AWS)
- Stackdriver (GCP)

## 🔐 SSL/TLS

### Let's Encrypt (Certbot)

```bash
# Instalar Certbot
sudo apt-get install certbot python3-certbot-nginx

# Obter certificado
sudo certbot --nginx -d epessoa.gov.br -d www.epessoa.gov.br

# Renovação automática
sudo crontab -e
# Adicionar: 0 12 * * * /usr/bin/certbot renew --quiet
```

## 🗄️ Backup do Banco de Dados

### Script de Backup PostgreSQL

```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="/backups"
DB_NAME="epessoa"
DB_USER="epessoa_user"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/epessoa_backup_$TIMESTAMP.sql"

# Criar backup
pg_dump -U $DB_USER -d $DB_NAME -F c -f $BACKUP_FILE

# Comprimir
gzip $BACKUP_FILE

# Remover backups com mais de 7 dias
find $BACKUP_DIR -name "*.gz" -mtime +7 -delete

echo "Backup concluído: ${BACKUP_FILE}.gz"
```

### Cron Job para Backup Automático

```bash
# Executar diariamente às 2h da manhã
0 2 * * * /path/to/backup.sh >> /var/log/epessoa-backup.log 2>&1
```

## 📈 Escalabilidade

### Load Balancer

Configure múltiplas instâncias da aplicação atrás de um load balancer:

- **AWS**: Application Load Balancer (ALB)
- **GCP**: Cloud Load Balancing
- **Azure**: Azure Load Balancer
- **Self-hosted**: Nginx, HAProxy

### Cache

Considere adicionar Redis para cache:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## ✅ Checklist de Deploy

- [ ] Alterar senhas padrão (admin/demo)
- [ ] Configurar JWT_SECRET com chave forte
- [ ] Configurar banco de dados PostgreSQL em produção
- [ ] Configurar credenciais Gov.br de produção
- [ ] Habilitar HTTPS/SSL
- [ ] Configurar backup automático do banco
- [ ] Configurar monitoramento e alertas
- [ ] Configurar logs agregados
- [ ] Implementar rate limiting
- [ ] Revisar permissões de segurança
- [ ] Testar todos os endpoints
- [ ] Documentar URLs de produção
- [ ] Configurar domínio customizado
- [ ] Testar processo de rollback
- [ ] Configurar CI/CD

## 🆘 Troubleshooting

### Aplicação não inicia

1. Verificar logs: `docker logs epessoa-app`
2. Verificar conexão com banco de dados
3. Validar variáveis de ambiente

### Erro de memória

Aumentar heap size:
```bash
java -Xms512m -Xmx2048m -jar app.jar
```

### Performance lenta

1. Aumentar pool de conexões do banco
2. Adicionar índices no banco de dados
3. Implementar cache
4. Escalar horizontalmente

## 📞 Suporte

Para questões de deploy em produção:
- Email: devops@epessoa.gov.br
- Documentação: https://docs.epessoa.gov.br

