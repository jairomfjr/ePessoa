# Guia de Configuração - ePessoa

## 📋 Pré-requisitos

### Software Necessário

1. **Java Development Kit (JDK) 23**
   - Download: https://jdk.java.net/23/
   - Verifique a instalação: `java -version`

2. **Maven 3.8+**
   - Download: https://maven.apache.org/download.cgi
   - Verifique a instalação: `mvn -version`

3. **PostgreSQL 12+** (ou use H2 para desenvolvimento)
   - Download: https://www.postgresql.org/download/

4. **Git**
   - Download: https://git-scm.com/downloads

### IDEs Recomendadas

- IntelliJ IDEA (Community ou Ultimate)
- Eclipse com Spring Tools
- VS Code com extensões Java e Spring Boot

## 🚀 Passos de Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/epessoa.git
cd epessoa
```

### 2. Configurar o Banco de Dados

#### Opção A: PostgreSQL (Recomendado para Produção)

1. Criar o banco de dados:
```sql
CREATE DATABASE epessoa;
CREATE USER epessoa_user WITH PASSWORD 'sua_senha_segura';
GRANT ALL PRIVILEGES ON DATABASE epessoa TO epessoa_user;
```

2. Atualizar `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/epessoa
spring.datasource.username=epessoa_user
spring.datasource.password=sua_senha_segura
```

#### Opção B: H2 Database (Para Desenvolvimento)

1. Comentar as configurações do PostgreSQL no `application.properties`
2. Adicionar:
```properties
spring.datasource.url=jdbc:h2:mem:epessoa
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### 3. Configurar Variáveis de Ambiente

#### Windows (PowerShell)
```powershell
$env:JWT_SECRET="sua-chave-secreta-muito-longa-e-segura-aqui"
$env:GOVBR_CLIENT_ID="seu-client-id-do-govbr"
$env:GOVBR_CLIENT_SECRET="seu-client-secret-do-govbr"
```

#### Linux/Mac (Bash)
```bash
export JWT_SECRET="sua-chave-secreta-muito-longa-e-segura-aqui"
export GOVBR_CLIENT_ID="seu-client-id-do-govbr"
export GOVBR_CLIENT_SECRET="seu-client-secret-do-govbr"
```

### 4. Compilar o Projeto

```bash
mvn clean install
```

### 5. Executar a Aplicação

```bash
mvn spring-boot:run
```

Ou execute diretamente o JAR:
```bash
java -jar target/epessoa-1.0.0.jar
```

### 6. Verificar a Instalação

Acesse:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console** (se usar H2): http://localhost:8080/h2-console

## 🔐 Configuração do Gov.br OAuth2

### 1. Registrar Aplicação no Gov.br

1. Acesse: https://sso.staging.acesso.gov.br/
2. Crie uma nova aplicação
3. Configure os Redirect URIs:
   - `http://localhost:8080/login/oauth2/code/govbr`
   - `http://localhost:8080/api/auth/oauth2/callback`

### 2. Obter Credenciais

Após criar a aplicação, você receberá:
- **Client ID**
- **Client Secret**

### 3. Configurar na Aplicação

Atualize o `application.properties`:
```properties
spring.security.oauth2.client.registration.govbr.client-id=${GOVBR_CLIENT_ID}
spring.security.oauth2.client.registration.govbr.client-secret=${GOVBR_CLIENT_SECRET}
```

### 4. Ambiente de Produção

Para produção, altere as URLs para o ambiente real do Gov.br:
```properties
spring.security.oauth2.client.provider.govbr.authorization-uri=https://sso.acesso.gov.br/authorize
spring.security.oauth2.client.provider.govbr.token-uri=https://sso.acesso.gov.br/token
spring.security.oauth2.client.provider.govbr.user-info-uri=https://sso.acesso.gov.br/userinfo
```

## 📝 Credenciais Padrão

A aplicação cria dois usuários automaticamente:

### Admin
- **Username**: admin
- **Password**: admin123
- **Role**: ADMIN

### Demo
- **Username**: demo
- **Password**: demo123
- **Role**: USER

⚠️ **IMPORTANTE**: Altere essas senhas em produção!

## 🧪 Executar Testes

```bash
# Todos os testes
mvn test

# Com relatório de cobertura
mvn test jacoco:report

# Apenas testes unitários
mvn test -Dtest=*Test

# Apenas testes de integração
mvn test -Dtest=*IT
```

## 📦 Build para Produção

```bash
# Criar JAR executável
mvn clean package -DskipTests

# JAR estará em: target/epessoa-1.0.0.jar
```

## 🐳 Docker (Opcional)

### Criar Dockerfile

```dockerfile
FROM eclipse-temurin:23-jre
WORKDIR /app
COPY target/epessoa-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build e Run

```bash
docker build -t epessoa:1.0.0 .
docker run -p 8080:8080 -e JWT_SECRET=sua-chave epessoa:1.0.0
```

## 🔧 Troubleshooting

### Problema: Porta 8080 já está em uso

Altere a porta no `application.properties`:
```properties
server.port=8081
```

### Problema: Erro de conexão com o banco

Verifique:
1. PostgreSQL está rodando
2. Credenciais estão corretas
3. Banco de dados foi criado

### Problema: Token JWT inválido

Verifique:
1. JWT_SECRET tem pelo menos 64 caracteres
2. Formato do token: `Bearer {token}`
3. Token não está expirado

## 📞 Suporte

Para problemas ou dúvidas:
- Email: suporte@epessoa.gov.br
- Issues: https://github.com/seu-usuario/epessoa/issues

## 📚 Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Gov.br OAuth2](https://manual-roteiro-integracao-login-unico.servicos.gov.br/)

