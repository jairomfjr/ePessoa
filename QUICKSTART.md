# 🚀 Quick Start - ePessoa

Guia rápido para começar a usar o sistema ePessoa em 5 minutos!

## ⚡ Início Rápido

### Opção 1: Docker Compose (Recomendado)

```bash
# 1. Clonar o repositório
git clone https://github.com/seu-usuario/epessoa.git
cd epessoa

# 2. Iniciar com Docker Compose
docker-compose up -d

# 3. Aguardar inicialização (30-40 segundos)
docker-compose logs -f app

# 4. Acessar a aplicação
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

### Opção 2: Desenvolvimento Local

```bash
# 1. Pré-requisitos
# - Java 23
# - Maven 3.8+

# 2. Clonar e compilar
git clone https://github.com/seu-usuario/epessoa.git
cd epessoa
mvn clean install

# 3. Executar (usa H2 em memória)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 4. Acessar
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
```

## 🔐 Credenciais Padrão

### Usuário Admin
```
Username: admin
Password: admin123
```

### Usuário Demo
```
Username: demo
Password: demo123
```

⚠️ **IMPORTANTE**: Altere estas senhas em produção!

## 📝 Primeiro Teste

### 1. Fazer Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "username": "admin",
  "name": "Administrador",
  "role": "ADMIN"
}
```

### 2. Salvar o Token

```bash
# Linux/Mac
export TOKEN="seu-token-aqui"

# Windows (PowerShell)
$env:TOKEN="seu-token-aqui"
```

### 3. Criar uma Pessoa

```bash
curl -X POST http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "João da Silva",
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
  }'
```

### 4. Listar Pessoas

```bash
curl -X GET http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer $TOKEN"
```

## 🌐 Swagger UI

A maneira mais fácil de testar a API é usando o Swagger UI:

1. Acesse: http://localhost:8080/swagger-ui.html
2. Clique em "Authorize" 🔒
3. Cole seu token no formato: `Bearer {seu-token}`
4. Clique em "Authorize" e depois "Close"
5. Agora você pode testar todos os endpoints!

## 📊 Endpoints Principais

### Autenticação
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Login
- `POST /api/auth/refresh` - Renovar token
- `GET /oauth2/authorization/govbr` - Login via Gov.br

### Pessoas (requer autenticação)
- `POST /api/pessoas` - Criar pessoa
- `GET /api/pessoas` - Listar todas
- `GET /api/pessoas/{id}` - Buscar por ID
- `GET /api/pessoas/cpf/{cpf}` - Buscar por CPF
- `GET /api/pessoas/buscar?nome={nome}` - Buscar por nome
- `PUT /api/pessoas/{id}` - Atualizar
- `DELETE /api/pessoas/{id}` - Deletar (soft delete)

## 🗄️ Acessar o Banco de Dados

### H2 (Desenvolvimento)

Se estiver usando o perfil `dev`:

1. Acesse: http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:epessoa_dev`
3. Username: `sa`
4. Password: (deixe em branco)
5. Clique em "Connect"

### PostgreSQL (Docker)

```bash
# Conectar ao PostgreSQL
docker exec -it epessoa-postgres psql -U epessoa_user -d epessoa

# Comandos SQL úteis
\dt                          # Listar tabelas
\d pessoas                   # Descrever tabela pessoas
SELECT * FROM usuarios;      # Ver usuários
SELECT * FROM pessoas;       # Ver pessoas
```

## 🔍 Verificar Status

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Ver Logs

```bash
# Docker Compose
docker-compose logs -f app

# Maven (desenvolvimento)
# Os logs aparecem no console
```

## 🛑 Parar e Limpar

### Docker Compose

```bash
# Parar
docker-compose stop

# Parar e remover containers
docker-compose down

# Parar, remover e limpar dados
docker-compose down -v
```

### Maven

```
# Ctrl+C no terminal para parar
```

## 📚 Próximos Passos

1. **Explorar a API**
   - Use o Swagger UI para testar todos os endpoints
   - Leia a documentação em `API_EXAMPLES.md`

2. **Configurar Gov.br OAuth2**
   - Siga as instruções em `SETUP.md`
   - Configure as credenciais no `.env`

3. **Deploy em Produção**
   - Leia o guia completo em `DEPLOYMENT.md`
   - Configure variáveis de ambiente
   - Use PostgreSQL em produção

4. **Desenvolvimento**
   - Leia `ARCHITECTURE.md` para entender a estrutura
   - Veja `COMMANDS.md` para comandos úteis
   - Execute os testes: `mvn test`

## 🐛 Problemas Comuns

### Porta 8080 já está em uso

```bash
# Linux/Mac - encontrar processo
lsof -i :8080

# Windows - encontrar processo
netstat -ano | findstr :8080

# Ou altere a porta no application.properties
server.port=8081
```

### Erro de conexão com banco de dados

1. Verifique se o PostgreSQL está rodando:
   ```bash
   docker ps
   ```

2. Veja os logs do container:
   ```bash
   docker-compose logs postgres
   ```

3. Use H2 para desenvolvimento:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

### Token JWT inválido

1. Verifique se o token está no formato correto:
   ```
   Authorization: Bearer {token}
   ```

2. Verifique se o token não expirou (24 horas por padrão)

3. Faça login novamente para obter um novo token

### Docker build falha

1. Certifique-se de ter compilado o projeto:
   ```bash
   mvn clean package -DskipTests
   ```

2. Verifique se o JAR foi gerado:
   ```bash
   ls -la target/epessoa-1.0.0.jar
   ```

## 📞 Ajuda

- **Documentação completa**: Ver arquivos `*.md` na raiz do projeto
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Issues**: https://github.com/seu-usuario/epessoa/issues

## ✅ Checklist de Configuração

- [ ] Java 23 instalado
- [ ] Maven 3.8+ instalado (ou Docker)
- [ ] Projeto clonado e compilado
- [ ] Aplicação iniciada
- [ ] Login realizado com sucesso
- [ ] Primeira pessoa cadastrada
- [ ] Swagger UI acessível
- [ ] Banco de dados acessível

## 🎉 Pronto!

Você agora tem o sistema ePessoa rodando localmente!

Para aprender mais:
- 📖 `README.md` - Visão geral do projeto
- 🏗️ `ARCHITECTURE.md` - Arquitetura do sistema
- ⚙️ `SETUP.md` - Configuração detalhada
- 🚀 `DEPLOYMENT.md` - Deploy em produção
- 💻 `COMMANDS.md` - Comandos úteis
- 📝 `API_EXAMPLES.md` - Exemplos de uso da API

---

**Desenvolvido com ❤️ usando Java 23 e Spring Boot**

