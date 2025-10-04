# Comandos Úteis - ePessoa

## 🔧 Maven

### Build e Compilação

```bash
# Compilar o projeto
mvn compile

# Compilar e executar testes
mvn test

# Compilar, testar e gerar JAR
mvn package

# Limpar, compilar e instalar (pula testes)
mvn clean install -DskipTests

# Compilar com perfil específico
mvn clean package -Pprod

# Ver árvore de dependências
mvn dependency:tree

# Atualizar dependências
mvn versions:display-dependency-updates

# Verificar plugins desatualizados
mvn versions:display-plugin-updates
```

### Execução

```bash
# Executar aplicação
mvn spring-boot:run

# Executar com perfil dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Executar com debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Executar JAR diretamente
java -jar target/epessoa-1.0.0.jar

# Executar JAR com perfil
java -jar -Dspring.profiles.active=prod target/epessoa-1.0.0.jar

# Executar com memória específica
java -Xms512m -Xmx1024m -jar target/epessoa-1.0.0.jar
```

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar teste específico
mvn test -Dtest=PessoaServiceTest

# Executar testes de uma classe
mvn test -Dtest=PessoaServiceTest#shouldCreatePessoaSuccessfully

# Executar com cobertura (JaCoCo)
mvn clean test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html

# Executar apenas testes unitários
mvn test -Dtest=*Test

# Executar apenas testes de integração
mvn test -Dtest=*IT

# Executar testes em paralelo
mvn test -T 4
```

## 🐳 Docker

### Build e Execução

```bash
# Build da imagem
docker build -t epessoa:1.0.0 .

# Build sem cache
docker build --no-cache -t epessoa:1.0.0 .

# Executar container
docker run -p 8080:8080 epessoa:1.0.0

# Executar com variáveis de ambiente
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=sua-chave \
  epessoa:1.0.0

# Executar em background
docker run -d -p 8080:8080 --name epessoa-app epessoa:1.0.0

# Ver logs
docker logs epessoa-app

# Ver logs em tempo real
docker logs -f epessoa-app

# Parar container
docker stop epessoa-app

# Remover container
docker rm epessoa-app

# Acessar shell do container
docker exec -it epessoa-app sh
```

### Docker Compose

```bash
# Iniciar todos os serviços
docker-compose up

# Iniciar em background
docker-compose up -d

# Ver logs
docker-compose logs

# Ver logs de um serviço específico
docker-compose logs app

# Logs em tempo real
docker-compose logs -f

# Parar serviços
docker-compose stop

# Parar e remover containers
docker-compose down

# Parar, remover e limpar volumes
docker-compose down -v

# Rebuild de imagens
docker-compose build

# Rebuild sem cache
docker-compose build --no-cache

# Executar comando em serviço
docker-compose exec app sh

# Ver status dos serviços
docker-compose ps

# Reiniciar serviço específico
docker-compose restart app
```

## 🗄️ PostgreSQL

### Comandos Básicos

```bash
# Conectar ao PostgreSQL
psql -h localhost -U postgres -d epessoa

# Conectar via Docker
docker exec -it epessoa-postgres psql -U epessoa_user -d epessoa
```

### SQL Úteis

```sql
-- Listar todas as tabelas
\dt

-- Descrever estrutura de tabela
\d pessoas

-- Ver usuários
SELECT * FROM usuarios;

-- Ver pessoas ativas
SELECT * FROM pessoas WHERE ativo = true;

-- Buscar pessoa por CPF
SELECT * FROM pessoas WHERE cpf = '12345678901';

-- Contar total de pessoas
SELECT COUNT(*) FROM pessoas;

-- Pessoas criadas hoje
SELECT * FROM pessoas WHERE DATE(created_at) = CURRENT_DATE;

-- Top 10 maiores rendas
SELECT nome_completo, renda_mensal 
FROM pessoas 
ORDER BY renda_mensal DESC 
LIMIT 10;

-- Pessoas por estado
SELECT estado, COUNT(*) 
FROM pessoas 
GROUP BY estado 
ORDER BY COUNT(*) DESC;

-- Limpar todas as pessoas (cuidado!)
TRUNCATE TABLE pessoas CASCADE;

-- Resetar sequência de ID
ALTER SEQUENCE pessoas_id_seq RESTART WITH 1;
```

### Backup e Restore

```bash
# Fazer backup
pg_dump -U postgres -d epessoa > backup.sql

# Fazer backup comprimido
pg_dump -U postgres -d epessoa | gzip > backup.sql.gz

# Restore
psql -U postgres -d epessoa < backup.sql

# Restore de backup comprimido
gunzip < backup.sql.gz | psql -U postgres -d epessoa

# Backup via Docker
docker exec epessoa-postgres pg_dump -U epessoa_user epessoa > backup.sql

# Restore via Docker
docker exec -i epessoa-postgres psql -U epessoa_user epessoa < backup.sql
```

## 🔍 cURL - Testes de API

### Autenticação

```bash
# Registrar novo usuário
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "teste",
    "password": "senha123",
    "name": "Usuario Teste",
    "email": "teste@email.com"
  }' | jq

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }' | jq

# Salvar token em variável
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r '.token')

echo $TOKEN

# Refresh token
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d "{\"refreshToken\": \"$REFRESH_TOKEN\"}" | jq
```

### CRUD de Pessoas

```bash
# Criar pessoa
curl -X POST http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "João Silva",
    "cpf": "12345678901",
    "dataNascimento": "1990-01-15",
    "sexo": "MASCULINO",
    "estadoCivil": "SOLTEIRO",
    "cep": "01310100",
    "rua": "Avenida Paulista",
    "numero": "1578",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "email": "joao@email.com",
    "telefoneCelular": "11987654321",
    "rendaMensal": 5000,
    "scoreCredito": 750
  }' | jq

# Listar todas
curl -X GET http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer $TOKEN" | jq

# Buscar por ID
curl -X GET http://localhost:8080/api/pessoas/1 \
  -H "Authorization: Bearer $TOKEN" | jq

# Buscar por CPF
curl -X GET http://localhost:8080/api/pessoas/cpf/12345678901 \
  -H "Authorization: Bearer $TOKEN" | jq

# Buscar por nome
curl -X GET "http://localhost:8080/api/pessoas/buscar?nome=João" \
  -H "Authorization: Bearer $TOKEN" | jq

# Atualizar pessoa
curl -X PUT http://localhost:8080/api/pessoas/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "João Silva Santos",
    ...
  }' | jq

# Deletar (soft delete)
curl -X DELETE http://localhost:8080/api/pessoas/1 \
  -H "Authorization: Bearer $TOKEN"
```

## 📊 Monitoramento

### Actuator

```bash
# Health check
curl http://localhost:8080/actuator/health | jq

# Info da aplicação
curl http://localhost:8080/actuator/info | jq

# Métricas
curl http://localhost:8080/actuator/metrics | jq

# Métrica específica
curl http://localhost:8080/actuator/metrics/jvm.memory.used | jq

# Prometheus metrics
curl http://localhost:8080/actuator/prometheus
```

### Logs

```bash
# Ver logs em tempo real
tail -f logs/epessoa.log

# Filtrar logs de erro
grep "ERROR" logs/epessoa.log

# Contar erros por tipo
grep "ERROR" logs/epessoa.log | cut -d' ' -f5- | sort | uniq -c | sort -nr

# Logs de hoje
grep "$(date +%Y-%m-%d)" logs/epessoa.log
```

## 🔐 Segurança

### Gerar JWT Secret

```bash
# Gerar chave aleatória base64
openssl rand -base64 64

# Gerar UUID
uuidgen
```

### SSL/TLS

```bash
# Gerar keystore para HTTPS
keytool -genkeypair -alias epessoa \
  -keyalg RSA -keysize 2048 \
  -storetype PKCS12 \
  -keystore keystore.p12 \
  -validity 3650

# Ver certificados no keystore
keytool -list -v -keystore keystore.p12

# Exportar certificado
keytool -export -alias epessoa \
  -keystore keystore.p12 \
  -file epessoa.crt
```

## 🧹 Limpeza

```bash
# Limpar build Maven
mvn clean

# Limpar containers Docker parados
docker container prune

# Limpar imagens não usadas
docker image prune

# Limpar volumes não usados
docker volume prune

# Limpar tudo (cuidado!)
docker system prune -a

# Limpar logs antigos
find logs/ -name "*.log" -mtime +30 -delete
```

## 📈 Performance

### JVM Profiling

```bash
# Heap dump
jmap -dump:live,format=b,file=heap.bin <PID>

# Ver uso de memória
jstat -gc <PID> 1000

# Thread dump
jstack <PID> > thread_dump.txt

# Ver processos Java
jps -l
```

### Benchmark com Apache Bench

```bash
# 1000 requisições, 10 concorrentes
ab -n 1000 -c 10 http://localhost:8080/actuator/health

# Com autenticação
ab -n 1000 -c 10 -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/pessoas
```

### Benchmark com wrk

```bash
# 30 segundos, 12 threads, 400 conexões
wrk -t12 -c400 -d30s http://localhost:8080/actuator/health

# Com script Lua personalizado
wrk -t12 -c400 -d30s -s script.lua http://localhost:8080/api/pessoas
```

## 🔄 Git

```bash
# Status
git status

# Add e commit
git add .
git commit -m "feat: adicionar nova funcionalidade"

# Push
git push origin main

# Pull
git pull origin main

# Ver histórico
git log --oneline --graph

# Ver diferenças
git diff

# Criar branch
git checkout -b feature/nova-funcionalidade

# Voltar para main
git checkout main

# Merge
git merge feature/nova-funcionalidade

# Tags
git tag -a v1.0.0 -m "Versão 1.0.0"
git push origin v1.0.0
```

## 🌐 Acesso Rápido

```bash
# Swagger UI
open http://localhost:8080/swagger-ui.html

# H2 Console (perfil dev)
open http://localhost:8080/h2-console

# Actuator Health
open http://localhost:8080/actuator/health

# API Docs JSON
open http://localhost:8080/api-docs
```

## 💡 Dicas

### Aliases Úteis (adicionar ao .bashrc ou .zshrc)

```bash
# Maven
alias mvnci="mvn clean install"
alias mvnct="mvn clean test"
alias mvnrun="mvn spring-boot:run"

# Docker
alias dcu="docker-compose up"
alias dcd="docker-compose down"
alias dcl="docker-compose logs -f"

# ePessoa específico
alias epessoa-start="docker-compose up -d"
alias epessoa-stop="docker-compose down"
alias epessoa-logs="docker-compose logs -f app"
alias epessoa-db="docker exec -it epessoa-postgres psql -U epessoa_user -d epessoa"
```

### Scripts Úteis

```bash
# Script para restart completo
#!/bin/bash
echo "Parando aplicação..."
docker-compose down
echo "Limpando volumes..."
docker-compose down -v
echo "Rebuilding..."
docker-compose build --no-cache
echo "Iniciando..."
docker-compose up -d
echo "Aplicação reiniciada!"
```

## 📞 Ajuda

```bash
# Ajuda Maven
mvn --help

# Ajuda Docker
docker --help
docker-compose --help

# Versão Java
java -version

# Versão Maven
mvn -version

# Variáveis de ambiente
printenv | grep -i spring
```

