# ePessoa - Sistema de Cadastro de Pessoas

Sistema backend completo desenvolvido em Java 23 com Spring Boot para cadastro de pessoas, incluindo autenticação JWT e integração com Gov.br.

## 📋 Funcionalidades

- ✅ CRUD completo de Pessoas
- ✅ Autenticação JWT (Login, Registro, Refresh Token)
- ✅ Autenticação OAuth2 via Gov.br
- ✅ Validação de dados (CPF, e-mail, etc)
- ✅ Tratamento global de exceções
- ✅ Documentação interativa com Swagger/OpenAPI
- ✅ Testes unitários e de integração

## 🛠️ Tecnologias Utilizadas

- **Java 23**
- **Spring Boot 3.3.4**
- **Spring Security** (JWT + OAuth2)
- **Spring Data JPA**
- **PostgreSQL** (produção)
- **H2 Database** (testes)
- **Hibernate**
- **Lombok**
- **MapStruct**
- **Swagger/OpenAPI 3**
- **JUnit 5**
- **Mockito**

## 📦 Estrutura do Projeto

```
ePessoa/
├── src/
│   ├── main/
│   │   ├── java/br/gov/epessoa/
│   │   │   ├── config/          # Configurações (Security, Swagger, etc)
│   │   │   ├── controller/      # Controllers REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositories JPA
│   │   │   ├── service/         # Lógica de negócio
│   │   │   ├── security/        # JWT, filters, configurações
│   │   │   └── exception/       # Exceções customizadas
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Testes unitários e de integração
├── pom.xml
└── README.md
```

## 🚀 Como Executar

### Pré-requisitos

- Java 23 ou superior
- Maven 3.8+
- PostgreSQL 12+ (ou usar H2 em memória)

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE epessoa;
```

2. Configure as credenciais em `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/epessoa
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Configuração Gov.br OAuth2

1. Registre sua aplicação no portal Gov.br
2. Configure as variáveis de ambiente ou no `application.properties`:
```properties
spring.security.oauth2.client.registration.govbr.client-id=seu-client-id
spring.security.oauth2.client.registration.govbr.client-secret=seu-client-secret
```

### Executando a Aplicação

```bash
# Compilar o projeto
mvn clean install

# Executar
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🔐 Autenticação

### JWT

1. **Registrar novo usuário**:
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "usuario@email.com",
  "password": "senha123",
  "name": "Nome Completo"
}
```

2. **Login**:
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario@email.com",
  "password": "senha123"
}
```

3. **Usar o token** nas requisições:
```http
GET /api/pessoas
Authorization: Bearer {seu-token-jwt}
```

### Gov.br OAuth2

Acesse: `http://localhost:8080/oauth2/authorization/govbr`

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar com cobertura
mvn test jacoco:report
```

## 📊 Endpoints Principais

### Autenticação
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Login com JWT
- `POST /api/auth/refresh` - Renovar token
- `GET /oauth2/authorization/govbr` - Login via Gov.br

### Pessoas (requer autenticação)
- `GET /api/pessoas` - Listar todas as pessoas
- `GET /api/pessoas/{id}` - Buscar pessoa por ID
- `GET /api/pessoas/cpf/{cpf}` - Buscar por CPF
- `POST /api/pessoas` - Criar nova pessoa
- `PUT /api/pessoas/{id}` - Atualizar pessoa
- `DELETE /api/pessoas/{id}` - Remover pessoa

## 🔧 Variáveis de Ambiente

```bash
# Banco de Dados
DB_URL=jdbc:postgresql://localhost:5432/epessoa
DB_USERNAME=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=sua-chave-secreta-aqui
JWT_EXPIRATION=86400000

# Gov.br
GOVBR_CLIENT_ID=seu-client-id
GOVBR_CLIENT_SECRET=seu-client-secret
```

## 📝 Licença

Este projeto é de código aberto e está disponível sob a licença MIT.

## 👥 Autor

Desenvolvido para demonstração de arquitetura backend moderna com Java 23 e Spring Boot.

