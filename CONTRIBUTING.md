# 🤝 Guia de Contribuição - ePessoa

Obrigado por considerar contribuir com o projeto ePessoa! Este documento fornece diretrizes para contribuições.

## 📋 Código de Conduta

### Nossos Compromissos

- Ser respeitoso e inclusivo
- Aceitar críticas construtivas
- Focar no que é melhor para a comunidade
- Demonstrar empatia com outros membros

## 🚀 Como Contribuir

### 1. Reportar Bugs

Se você encontrou um bug, por favor:

1. Verifique se o bug já foi reportado nas [Issues](https://github.com/seu-usuario/epessoa/issues)
2. Se não encontrou, abra uma nova issue com:
   - **Título claro e descritivo**
   - **Passos para reproduzir**
   - **Comportamento esperado vs atual**
   - **Screenshots** (se aplicável)
   - **Versão do Java, Maven, OS**
   - **Logs relevantes**

#### Template de Bug Report

```markdown
### Descrição do Bug
[Descrição clara do que aconteceu]

### Passos para Reproduzir
1. Vá para '...'
2. Clique em '...'
3. Execute '...'
4. Veja o erro

### Comportamento Esperado
[O que deveria acontecer]

### Comportamento Atual
[O que realmente acontece]

### Screenshots
[Se aplicável]

### Ambiente
- Java: [versão]
- Maven: [versão]
- OS: [Windows/Linux/Mac]
- Banco: [PostgreSQL/H2]

### Logs
```
[Cole os logs aqui]
```
```

### 2. Sugerir Melhorias

Para sugerir uma nova funcionalidade:

1. Abra uma issue com o label `enhancement`
2. Descreva claramente:
   - **Problema que resolve**
   - **Solução proposta**
   - **Alternativas consideradas**
   - **Impacto no sistema existente**

### 3. Contribuir com Código

#### Passo a Passo

1. **Fork o repositório**
   ```bash
   # Clique em "Fork" no GitHub
   ```

2. **Clone seu fork**
   ```bash
   git clone https://github.com/seu-usuario/epessoa.git
   cd epessoa
   ```

3. **Configure o upstream**
   ```bash
   git remote add upstream https://github.com/original-usuario/epessoa.git
   ```

4. **Crie uma branch**
   ```bash
   git checkout -b feature/minha-funcionalidade
   # ou
   git checkout -b fix/corrigir-bug
   ```

5. **Faça suas alterações**
   - Escreva código limpo e comentado
   - Siga os padrões do projeto
   - Adicione testes

6. **Commit suas mudanças**
   ```bash
   git add .
   git commit -m "feat: adicionar nova funcionalidade"
   ```

7. **Push para seu fork**
   ```bash
   git push origin feature/minha-funcionalidade
   ```

8. **Abra um Pull Request**
   - Vá para o repositório original no GitHub
   - Clique em "New Pull Request"
   - Selecione sua branch
   - Preencha o template

## 📝 Padrões de Código

### Convenção de Commits

Seguimos o [Conventional Commits](https://www.conventionalcommits.org/):

```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

#### Tipos

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Apenas documentação
- `style`: Formatação, ponto e vírgula, etc
- `refactor`: Refatoração de código
- `test`: Adicionar ou corrigir testes
- `chore`: Tarefas de manutenção

#### Exemplos

```bash
feat(auth): adicionar login via Gov.br
fix(pessoa): corrigir validação de CPF
docs(readme): atualizar instruções de setup
test(service): adicionar testes para PessoaService
refactor(controller): simplificar lógica de autenticação
```

### Estilo de Código Java

1. **Naming Conventions**
   ```java
   // Classes: PascalCase
   public class PessoaService {}
   
   // Métodos/Variáveis: camelCase
   public void createPessoa() {}
   private String nomeCompleto;
   
   // Constantes: UPPER_SNAKE_CASE
   private static final int MAX_ATTEMPTS = 3;
   
   // Packages: lowercase
   package br.gov.epessoa.service;
   ```

2. **Formatação**
   - Indentação: 4 espaços
   - Limite de linha: 120 caracteres
   - Chaves: mesmo estilo do projeto
   - Imports organizados e sem wildcards desnecessários

3. **Comentários**
   ```java
   /**
    * Cria uma nova pessoa no sistema.
    *
    * @param request dados da pessoa
    * @return pessoa criada
    * @throws BusinessException se CPF já existir
    */
   public PessoaResponseDTO createPessoa(PessoaRequestDTO request) {
       // Implementação
   }
   ```

4. **DTOs e Entidades**
   - Use `@Builder` para construção de objetos
   - Use `@Data` do Lombok para getters/setters
   - Valide com Bean Validation

5. **Services**
   - Métodos públicos devem ter `@Transactional`
   - Use `@Slf4j` para logs
   - Injete dependências via construtor
   - Trate exceções adequadamente

6. **Controllers**
   - Use anotações Swagger (`@Operation`, `@ApiResponse`)
   - Valide input com `@Valid`
   - Retorne DTOs, não entidades

### Testes

1. **Nomenclatura**
   ```java
   @Test
   @DisplayName("Deve criar pessoa com sucesso")
   void shouldCreatePessoaSuccessfully() {
       // Arrange
       // Act
       // Assert
   }
   ```

2. **Estrutura AAA**
   ```java
   @Test
   void testMethod() {
       // Arrange (Given)
       var input = createTestData();
       
       // Act (When)
       var result = service.method(input);
       
       // Assert (Then)
       assertNotNull(result);
       assertEquals(expected, result);
   }
   ```

3. **Cobertura**
   - Mínimo 60% de cobertura
   - Testes unitários para Services
   - Testes de integração para Controllers
   - Teste casos de sucesso e falha

## 🔍 Code Review

### O que procuramos

- ✅ Código limpo e legível
- ✅ Testes adequados
- ✅ Documentação atualizada
- ✅ Segue os padrões do projeto
- ✅ Não quebra funcionalidades existentes
- ✅ Commits bem descritos
- ✅ PR com descrição clara

### Checklist do PR

- [ ] Código compila sem erros
- [ ] Testes passam (`mvn test`)
- [ ] Cobertura mantida ou melhorada
- [ ] Documentação atualizada
- [ ] CHANGELOG atualizado (se aplicável)
- [ ] Sem conflitos com main
- [ ] Testado localmente
- [ ] Logs apropriados

## 📚 Documentação

### Quando Atualizar

- Novos endpoints: atualizar `API_EXAMPLES.md`
- Novas configurações: atualizar `SETUP.md`
- Mudanças na arquitetura: atualizar `ARCHITECTURE.md`
- Novos comandos: atualizar `COMMANDS.md`
- Processo de deploy: atualizar `DEPLOYMENT.md`

### Estilo de Documentação

- Use Markdown
- Seja claro e conciso
- Adicione exemplos práticos
- Use emojis para melhor visualização
- Mantenha a formatação consistente

## 🐛 Debug e Troubleshooting

### Logs

```java
// Use níveis apropriados
log.debug("Detalhes de debug: {}", details);
log.info("Operação concluída com sucesso");
log.warn("Situação inesperada: {}", situation);
log.error("Erro ao processar: {}", error, exception);
```

### Exceções

```java
// Lance exceções específicas
throw new BusinessException("CPF já cadastrado");
throw new ResourceNotFoundException("Pessoa não encontrada");

// Não capture exceções genéricas
// ❌ Ruim
try {
} catch (Exception e) {}

// ✅ Bom
try {
} catch (SpecificException e) {
    log.error("Erro específico", e);
    throw new BusinessException("Mensagem amigável", e);
}
```

## 🔒 Segurança

### Checklist de Segurança

- [ ] Não commitar senhas ou secrets
- [ ] Validar todos os inputs
- [ ] Usar prepared statements (JPA faz isso)
- [ ] Sanitizar dados de saída
- [ ] Não logar dados sensíveis
- [ ] Usar HTTPS em produção
- [ ] Implementar rate limiting

### Dados Sensíveis

```java
// ❌ Não faça isso
log.info("Senha do usuário: {}", password);
log.info("Token: {}", jwtToken);

// ✅ Faça isso
log.info("Usuário autenticado: {}", username);
log.debug("Token gerado para usuário: {}", username);
```

## 🎯 Prioridades

### Alta Prioridade
- Bugs críticos
- Problemas de segurança
- Performance issues
- Bugs que afetam usuários

### Média Prioridade
- Novas funcionalidades
- Melhorias de UX
- Refatorações
- Otimizações

### Baixa Prioridade
- Documentação
- Testes adicionais
- Code cleanup
- Atualizações de dependências

## 💬 Comunicação

### Onde Discutir

- **Issues**: Bugs e features específicas
- **Discussions**: Ideias gerais e dúvidas
- **Pull Requests**: Code review e implementações
- **Wiki**: Documentação detalhada

### Linguagem

- Seja respeitoso e profissional
- Use português ou inglês
- Seja claro e objetivo
- Forneça contexto suficiente

## 🏆 Reconhecimento

Todos os contribuidores serão:
- Adicionados ao CONTRIBUTORS.md
- Mencionados no CHANGELOG
- Creditados nos releases

## 📞 Ajuda

Precisa de ajuda?

- 📧 Email: contribuicoes@epessoa.gov.br
- 💬 Discussions: https://github.com/seu-usuario/epessoa/discussions
- 📖 Wiki: https://github.com/seu-usuario/epessoa/wiki

## 📜 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a mesma licença do projeto (MIT).

---

**Obrigado por contribuir! 🎉**

