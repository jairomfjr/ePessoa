# Arquitetura do Sistema - ePessoa

## 📐 Visão Geral

O sistema ePessoa foi desenvolvido seguindo os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**, organizado em camadas bem definidas para garantir manutenibilidade, testabilidade e escalabilidade.

## 🏗️ Estrutura de Camadas

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│    (Controllers / REST Endpoints)       │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Application Layer               │
│     (Services / Business Logic)         │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Domain Layer                   │
│    (Entities / Domain Models)           │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      Infrastructure Layer               │
│  (Repositories / Database / Security)   │
└─────────────────────────────────────────┘
```

## 📦 Estrutura de Pacotes

```
br.gov.epessoa/
│
├── config/                      # Configurações gerais
│   ├── SecurityConfig.java      # Configuração de segurança
│   ├── OpenApiConfig.java       # Configuração Swagger
│   ├── CorsConfig.java          # Configuração CORS
│   └── DataLoader.java          # Inicialização de dados
│
├── controller/                  # Camada de apresentação (REST)
│   ├── AuthController.java      # Endpoints de autenticação
│   └── PessoaController.java    # Endpoints de pessoas
│
├── dto/                         # Data Transfer Objects
│   ├── PessoaRequestDTO.java
│   ├── PessoaResponseDTO.java
│   ├── LoginRequestDTO.java
│   ├── RegisterRequestDTO.java
│   ├── AuthResponseDTO.java
│   └── RefreshTokenRequestDTO.java
│
├── model/                       # Entidades de domínio
│   ├── Usuario.java             # Entidade de usuário
│   └── Pessoa.java              # Entidade principal
│
├── repository/                  # Camada de persistência
│   ├── UsuarioRepository.java
│   └── PessoaRepository.java
│
├── service/                     # Lógica de negócio
│   ├── AuthService.java
│   ├── UsuarioService.java
│   └── PessoaService.java
│
├── security/                    # Componentes de segurança
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationEntryPoint.java
│   └── OAuth2AuthenticationSuccessHandler.java
│
├── exception/                   # Tratamento de exceções
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
└── EpessoaApplication.java     # Classe principal
```

## 🔄 Fluxo de Requisição

### 1. Autenticação JWT

```
Cliente → AuthController → AuthService → UsuarioService
                                 ↓
                          JwtTokenProvider
                                 ↓
                          Response com Token
```

### 2. Requisição Autenticada

```
Cliente → JwtAuthenticationFilter → Validação Token
              ↓                           ↓
    SecurityContext ← UserDetails ← JwtTokenProvider
              ↓
    PessoaController → PessoaService → PessoaRepository
              ↓              ↓                ↓
         DTO Response ← Entity ← Database
```

### 3. OAuth2 Gov.br

```
Cliente → /oauth2/authorization/govbr
              ↓
    Redirect para Gov.br
              ↓
    Login no Gov.br
              ↓
    Callback com código
              ↓
    OAuth2AuthenticationSuccessHandler
              ↓
    UsuarioService (criar/atualizar)
              ↓
    JwtTokenProvider (gerar token)
              ↓
    Redirect com token
```

## 🗄️ Modelo de Dados

### Diagrama ER

```
┌─────────────────────┐
│      USUARIOS       │
├─────────────────────┤
│ id (PK)             │
│ username            │
│ password            │
│ name                │
│ email               │
│ role                │
│ govbr_sub           │
│ enabled             │
│ created_at          │
│ updated_at          │
└─────────┬───────────┘
          │
          │ 1:N
          │
┌─────────▼───────────┐
│      PESSOAS        │
├─────────────────────┤
│ id (PK)             │
│ usuario_id (FK)     │
│                     │
│ -- Dados Pessoais   │
│ nome_completo       │
│ cpf                 │
│ data_nascimento     │
│ sexo                │
│ estado_civil        │
│                     │
│ -- Endereço         │
│ cep                 │
│ rua                 │
│ numero              │
│ complemento         │
│ bairro              │
│ cidade              │
│ estado              │
│                     │
│ -- Contatos         │
│ email               │
│ telefone_fixo       │
│ telefone_celular    │
│                     │
│ -- Financeiro       │
│ renda_mensal        │
│ score_credito       │
│ profissao           │
│ banco               │
│ numero_conta        │
│ tipo_conta          │
│                     │
│ -- Auditoria        │
│ ativo               │
│ created_at          │
│ updated_at          │
└─────────────────────┘
```

## 🔒 Segurança

### Camadas de Segurança

1. **Spring Security**: Configuração base de segurança
2. **JWT**: Autenticação stateless
3. **OAuth2**: Integração com Gov.br
4. **BCrypt**: Hash de senhas
5. **CORS**: Controle de origens
6. **Rate Limiting**: (via Nginx em produção)

### Fluxo de Autenticação

```
┌─────────────────────────────────────────────────┐
│                                                 │
│  1. Cliente faz login                           │
│     POST /api/auth/login                        │
│                                                 │
│  2. AuthenticationManager valida credenciais    │
│                                                 │
│  3. JwtTokenProvider gera token                 │
│                                                 │
│  4. Cliente recebe token                        │
│                                                 │
│  5. Cliente usa token em requisições            │
│     Header: Authorization: Bearer {token}       │
│                                                 │
│  6. JwtAuthenticationFilter valida token        │
│                                                 │
│  7. Requisição processada se válido             │
│                                                 │
└─────────────────────────────────────────────────┘
```

## 🧪 Estratégia de Testes

### Pirâmide de Testes

```
         /\
        /  \  E2E Tests (poucos)
       /────\
      /      \  Integration Tests (médio)
     /────────\
    /          \  Unit Tests (muitos)
   /────────────\
```

### Cobertura

- **Unit Tests**: Services, Repositories
- **Integration Tests**: Controllers, Security
- **E2E Tests**: Fluxos completos (planejado)

## 📊 Monitoramento e Observabilidade

### Métricas (Spring Actuator)

- **Health**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus` (se configurado)

### Logs

```
[timestamp] [level] [logger] - message
```

- **DEBUG**: Desenvolvimento
- **INFO**: Operações normais
- **WARN**: Situações inesperadas
- **ERROR**: Erros que requerem atenção

## 🔄 Ciclo de Vida da Aplicação

```
1. Inicialização
   ├── Spring Boot inicia
   ├── Beans são criados
   ├── DataLoader executa
   └── Servidor HTTP inicia

2. Execução
   ├── Recebe requisições
   ├── Processa através das camadas
   └── Retorna respostas

3. Shutdown
   ├── Fecha conexões
   ├── Finaliza threads
   └── Libera recursos
```

## 🚀 Deploy e Escalabilidade

### Estratégias de Escalabilidade

1. **Vertical**: Aumentar recursos da máquina
2. **Horizontal**: Múltiplas instâncias + Load Balancer
3. **Database**: Read replicas, Connection pooling
4. **Cache**: Redis para dados frequentes
5. **CDN**: Assets estáticos (se aplicável)

### Arquitetura em Produção

```
                     Internet
                        │
                        ▼
                  Load Balancer
                        │
        ┌───────────────┼───────────────┐
        ▼               ▼               ▼
    Instance 1      Instance 2      Instance 3
        │               │               │
        └───────────────┼───────────────┘
                        ▼
                   PostgreSQL
                   (Primary)
                        │
                        ▼
                   PostgreSQL
                   (Replica)
```

## 📈 Métricas de Performance

### Objetivos (SLA)

- **Uptime**: 99.9%
- **Response Time**: < 200ms (p95)
- **Throughput**: 1000 req/s
- **Error Rate**: < 0.1%

### Otimizações

1. **Database**:
   - Índices em CPF, email
   - Connection pooling
   - Query optimization

2. **Application**:
   - Lazy loading de relacionamentos
   - Batch operations
   - Cache de queries frequentes

3. **Network**:
   - GZIP compression
   - HTTP/2
   - CDN para assets

## 🔐 Segurança em Profundidade

### Checklist de Segurança

- [x] Autenticação JWT
- [x] OAuth2 Gov.br
- [x] Password hashing (BCrypt)
- [x] SQL Injection protection (JPA)
- [x] XSS protection (headers)
- [x] CSRF protection (stateless JWT)
- [x] CORS configuration
- [x] HTTPS/TLS (via proxy)
- [x] Input validation
- [x] Error handling (sem expor detalhes)
- [ ] Rate limiting (planejado via Nginx)
- [ ] API versioning (futuro)

## 📚 Princípios Arquiteturais

### SOLID

- **S**ingle Responsibility: Cada classe tem uma responsabilidade
- **O**pen/Closed: Aberto para extensão, fechado para modificação
- **L**iskov Substitution: Substituibilidade de tipos
- **I**nterface Segregation: Interfaces específicas
- **D**ependency Inversion: Dependa de abstrações

### Clean Code

- Nomes descritivos
- Funções pequenas e focadas
- Comentários apenas quando necessário
- Testes automatizados
- DRY (Don't Repeat Yourself)

## 🔮 Roadmap Futuro

### Fase 2
- [ ] Auditoria completa de operações
- [ ] Histórico de alterações
- [ ] Notificações (email/SMS)
- [ ] Relatórios e dashboards

### Fase 3
- [ ] Multi-tenancy
- [ ] API versioning
- [ ] GraphQL endpoint
- [ ] WebSockets para real-time

### Fase 4
- [ ] Microservices migration
- [ ] Event sourcing
- [ ] CQRS pattern
- [ ] Service mesh (Istio)

## 📞 Contato e Suporte

- **Documentação**: `/swagger-ui.html`
- **Email**: arquitetura@epessoa.gov.br
- **Wiki**: https://wiki.epessoa.gov.br

