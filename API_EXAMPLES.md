# Exemplos de Uso da API - ePessoa

## 📚 Índice

1. [Autenticação](#autenticação)
2. [Gerenciamento de Pessoas](#gerenciamento-de-pessoas)
3. [Exemplos com cURL](#exemplos-com-curl)
4. [Exemplos com JavaScript](#exemplos-com-javascript)
5. [Postman Collection](#postman-collection)

## 🔐 Autenticação

### 1. Registro de Novo Usuário

**Endpoint**: `POST /api/auth/register`

```json
{
  "username": "joao.silva",
  "password": "senha123456",
  "name": "João Silva",
  "email": "joao.silva@email.com"
}
```

**Resposta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "username": "joao.silva",
  "name": "João Silva",
  "role": "USER"
}
```

### 2. Login

**Endpoint**: `POST /api/auth/login`

```json
{
  "username": "joao.silva",
  "password": "senha123456"
}
```

**Resposta**: (mesmo formato do registro)

### 3. Renovar Token

**Endpoint**: `POST /api/auth/refresh`

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 4. Login via Gov.br

**Endpoint**: `GET /oauth2/authorization/govbr`

Redireciona para a página de login do Gov.br. Após autenticação, retorna para a aplicação com o token.

## 👥 Gerenciamento de Pessoas

### 1. Criar Nova Pessoa

**Endpoint**: `POST /api/pessoas`

**Headers**:
```
Authorization: Bearer {seu-token-jwt}
Content-Type: application/json
```

**Body**:
```json
{
  "nomeCompleto": "Maria Santos Silva",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-15",
  "sexo": "FEMININO",
  "estadoCivil": "CASADO",
  "cep": "01310100",
  "rua": "Avenida Paulista",
  "numero": "1578",
  "complemento": "Apto 101",
  "bairro": "Bela Vista",
  "cidade": "São Paulo",
  "estado": "SP",
  "email": "maria.santos@email.com",
  "telefoneFixo": "1133334444",
  "telefoneCelular": "11987654321",
  "rendaMensal": 8500.50,
  "scoreCredito": 750,
  "profissao": "Engenheira de Software",
  "banco": "Banco do Brasil",
  "numeroConta": "12345-6",
  "tipoConta": "CORRENTE"
}
```

**Resposta**: `201 Created`
```json
{
  "id": 1,
  "nomeCompleto": "Maria Santos Silva",
  "cpf": "12345678901",
  "dataNascimento": "1990-05-15",
  "sexo": "FEMININO",
  "estadoCivil": "CASADO",
  "cep": "01310100",
  "rua": "Avenida Paulista",
  "numero": "1578",
  "complemento": "Apto 101",
  "bairro": "Bela Vista",
  "cidade": "São Paulo",
  "estado": "SP",
  "email": "maria.santos@email.com",
  "telefoneFixo": "1133334444",
  "telefoneCelular": "11987654321",
  "rendaMensal": 8500.50,
  "scoreCredito": 750,
  "profissao": "Engenheira de Software",
  "banco": "Banco do Brasil",
  "numeroConta": "12345-6",
  "tipoConta": "CORRENTE",
  "ativo": true,
  "createdAt": "2025-10-04T10:30:00",
  "updatedAt": "2025-10-04T10:30:00"
}
```

### 2. Listar Todas as Pessoas

**Endpoint**: `GET /api/pessoas`

**Headers**:
```
Authorization: Bearer {seu-token-jwt}
```

**Resposta**: `200 OK`
```json
[
  {
    "id": 1,
    "nomeCompleto": "Maria Santos Silva",
    ...
  },
  {
    "id": 2,
    "nomeCompleto": "João Pedro Costa",
    ...
  }
]
```

### 3. Buscar Pessoa por ID

**Endpoint**: `GET /api/pessoas/{id}`

**Exemplo**: `GET /api/pessoas/1`

**Headers**:
```
Authorization: Bearer {seu-token-jwt}
```

### 4. Buscar Pessoa por CPF

**Endpoint**: `GET /api/pessoas/cpf/{cpf}`

**Exemplo**: `GET /api/pessoas/cpf/12345678901`

### 5. Buscar Pessoas por Nome

**Endpoint**: `GET /api/pessoas/buscar?nome={nome}`

**Exemplo**: `GET /api/pessoas/buscar?nome=Maria`

### 6. Listar Apenas Pessoas Ativas

**Endpoint**: `GET /api/pessoas/ativas`

### 7. Atualizar Pessoa

**Endpoint**: `PUT /api/pessoas/{id}`

**Body**: (mesmo formato de criação)

### 8. Deletar Pessoa (Soft Delete)

**Endpoint**: `DELETE /api/pessoas/{id}`

**Resposta**: `204 No Content`

### 9. Deletar Permanentemente (Apenas ADMIN)

**Endpoint**: `DELETE /api/pessoas/{id}/hard`

## 🖥️ Exemplos com cURL

### Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "teste",
    "password": "senha123",
    "name": "Usuário Teste",
    "email": "teste@email.com"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "teste",
    "password": "senha123"
  }'
```

### Criar Pessoa
```bash
curl -X POST http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "Carlos Eduardo",
    "cpf": "98765432100",
    "dataNascimento": "1985-03-20",
    "sexo": "MASCULINO",
    "estadoCivil": "SOLTEIRO",
    "cep": "20040020",
    "rua": "Avenida Rio Branco",
    "numero": "156",
    "bairro": "Centro",
    "cidade": "Rio de Janeiro",
    "estado": "RJ",
    "email": "carlos@email.com",
    "telefoneCelular": "21987654321",
    "rendaMensal": 5000,
    "scoreCredito": 650
  }'
```

### Listar Pessoas
```bash
curl -X GET http://localhost:8080/api/pessoas \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## 💻 Exemplos com JavaScript

### Fetch API

```javascript
// Login
async function login() {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      username: 'teste',
      password: 'senha123'
    })
  });
  
  const data = await response.json();
  localStorage.setItem('token', data.token);
  return data;
}

// Criar Pessoa
async function criarPessoa(pessoaData) {
  const token = localStorage.getItem('token');
  
  const response = await fetch('http://localhost:8080/api/pessoas', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(pessoaData)
  });
  
  return await response.json();
}

// Listar Pessoas
async function listarPessoas() {
  const token = localStorage.getItem('token');
  
  const response = await fetch('http://localhost:8080/api/pessoas', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  return await response.json();
}

// Atualizar Pessoa
async function atualizarPessoa(id, pessoaData) {
  const token = localStorage.getItem('token');
  
  const response = await fetch(`http://localhost:8080/api/pessoas/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(pessoaData)
  });
  
  return await response.json();
}

// Deletar Pessoa
async function deletarPessoa(id) {
  const token = localStorage.getItem('token');
  
  await fetch(`http://localhost:8080/api/pessoas/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}
```

### Axios

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// Interceptor para adicionar token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Login
const login = async (username, password) => {
  const response = await api.post('/auth/login', { username, password });
  localStorage.setItem('token', response.data.token);
  return response.data;
};

// Criar Pessoa
const criarPessoa = async (pessoaData) => {
  const response = await api.post('/pessoas', pessoaData);
  return response.data;
};

// Listar Pessoas
const listarPessoas = async () => {
  const response = await api.get('/pessoas');
  return response.data;
};
```

## 📮 Postman Collection

Importe este JSON no Postman para testar a API:

```json
{
  "info": {
    "name": "ePessoa API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080/api"
    },
    {
      "key": "token",
      "value": ""
    }
  ],
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "url": "{{baseUrl}}/auth/register",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"teste\",\n  \"password\": \"senha123\",\n  \"name\": \"Teste\",\n  \"email\": \"teste@email.com\"\n}"
            }
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "{{baseUrl}}/auth/login",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"teste\",\n  \"password\": \"senha123\"\n}"
            }
          }
        }
      ]
    }
  ]
}
```

## 🎯 Enums Disponíveis

### Sexo
- `MASCULINO`
- `FEMININO`
- `OUTRO`
- `NAO_INFORMADO`

### Estado Civil
- `SOLTEIRO`
- `CASADO`
- `DIVORCIADO`
- `VIUVO`
- `UNIAO_ESTAVEL`

### Tipo de Conta
- `CORRENTE`
- `POUPANCA`
- `SALARIO`
- `CONJUNTA`

## ❗ Códigos de Status HTTP

- `200 OK`: Requisição bem-sucedida
- `201 Created`: Recurso criado com sucesso
- `204 No Content`: Requisição bem-sucedida sem conteúdo de retorno
- `400 Bad Request`: Dados inválidos
- `401 Unauthorized`: Não autenticado ou token inválido
- `403 Forbidden`: Sem permissão
- `404 Not Found`: Recurso não encontrado
- `500 Internal Server Error`: Erro interno do servidor

