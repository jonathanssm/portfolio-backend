# Portfolio Backend

Backend em Java + Spring Boot para gerenciamento de portfolio profissional com Cloudflare Tunnel para produção.

## 🎯 Funcionalidades

- **Autenticação JWT** - Sistema de login seguro com refresh tokens
- **Gestão de Experiências** - CRUD de experiências profissionais com paginação
- **Gestão de Usuários** - Sistema completo de usuários com perfis e roles
- **API REST** - Endpoints documentados com OpenAPI/Swagger 3
- **Mensageria Assíncrona** - Eventos via Apache Kafka
- **Segurança Avançada** - Spring Security 6.x com rate limiting
- **Métricas** - Coleta de métricas de performance e uso
- **Validação** - Validação robusta com Bean Validation
- **Auditoria** - Logs estruturados e auditoria de entidades

## 🚀 Tecnologias

- **Java 21** + **Spring Boot 3.5.5**
- **Spring Security 6.x** + **JWT**
- **PostgreSQL 15** + **Apache Kafka**
- **MapStruct** + **Lombok**
- **Cloudflare Tunnel** (SSL/TLS automático)
- **Docker** + **Maven**

## 🏗️ Arquitetura

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Cliente   │───▶│ Cloudflare  │───▶│   Backend   │
│   (HTTPS)   │    │   Tunnel    │    │ (Spring)    │
└─────────────┘    │ (SSL/TLS)   │    └─────────────┘
                   └─────────────┘           │
                                            ▼
                                    ┌─────────────┐
                                    │ PostgreSQL  │
                                    │ (VM Externa)│
                                    └─────────────┘
                                              │
                                              ▼
                                    ┌─────────────┐
                                    │    Kafka    │
                                    │ (VM Externa)│
                                    └─────────────┘
```

## 🏛️ Arquitetura do Código

```
src/main/java/com/jonathanssm/portfoliobackend/
├── config/          # Configurações (Security, Kafka, OpenAPI)
├── constants/       # Constantes do sistema
├── controller/      # Controllers REST (Auth, Experience, Admin, Metrics)
├── dto/            # DTOs e Mappers (MapStruct)
├── messaging/      # Produtores e Consumidores Kafka
├── model/          # Entidades JPA (User, Experience, Profile, Role)
├── repository/     # Repositórios JPA
├── service/        # Serviços de negócio
└── util/           # Utilitários (JWT, Request Helper, JPA Utils)
```

## 🎯 Princípios SOLID Aplicados

- **SRP**: Cada classe tem uma única responsabilidade
- **OCP**: Aberto para extensão, fechado para modificação
- **LSP**: Substituição de implementações sem quebrar funcionalidade
- **ISP**: Interfaces específicas e coesas
- **DIP**: Dependência de abstrações, não implementações

## 🛠️ Desenvolvimento Local

### Pré-requisitos
- Java 21+
- Docker e Docker Compose

### Executando o projeto

1. **Clone e execute infraestrutura**
   ```bash
   git clone <repository-url>
   cd portfolio-backend
   docker-compose up -d
   ```

2. **Configure variáveis de ambiente**
   ```bash
   # Windows (PowerShell)
   $env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/postgres"
   $env:SPRING_DATASOURCE_USERNAME="postgres"
   $env:SPRING_DATASOURCE_PASSWORD="IRz{aBLPe{@Yk,2v=@YP"
   $env:SPRING_KAFKA_BOOTSTRAP_SERVERS="localhost:29092"

   # Linux/Mac
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/postgres
   export SPRING_DATASOURCE_USERNAME=postgres
   export SPRING_DATASOURCE_PASSWORD=IRz{aBLPe{@Yk,2v=@YP
   export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:29092
   ```

3. **Execute a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acesse a API**
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **Health Check**: http://localhost:8080/actuator/health

## 🚀 Deploy em Produção

### Deploy Automatizado (GitHub Actions)

Deploy automático via GitHub Actions ao fazer push para `main`:

1. **Compila** o projeto com Maven
2. **Deploy** na VM Linux via SSH
3. **Configura** Cloudflare Tunnel com SSL/TLS
4. **Gerencia** containers com limites de memória

### Configuração Necessária

**GitHub Secrets (obrigatórios):**
```
BACKEND_VM_IP = 163.176.182.139
BACKEND_VM_USER = ubuntu
BACKEND_VM_SSH_KEY = [chave SSH]
SPRING_DATASOURCE_URL = [URL do banco]
SPRING_DATASOURCE_USERNAME = [usuário do banco]
SPRING_DATASOURCE_PASSWORD = [senha do banco]
SPRING_KAFKA_BOOTSTRAP_SERVERS = [servidores Kafka]
CLOUDFLARE_TUNNEL_ID = [ID do tunnel]
CLOUDFLARE_DOMAIN = api.jonathanssm.com
CLOUDFLARE_CREDENTIALS_JSON = [credenciais do tunnel]
```

**Cloudflare DNS:**
```
Type: CNAME
Name: api
Target: [tunnel-id].cfargotunnel.com
Proxy: ✅ (nuvem laranja ativada)
```

## 📚 Documentação da API

- **Swagger UI**: https://api.jonathanssm.com/swagger-ui.html
- **Health Check**: https://api.jonathanssm.com/actuator/health

### Endpoints Principais

#### 🔐 Autenticação (`/auth`)
- `POST /auth/login` - Login com username/password
- `POST /auth/register` - Registro de novo usuário
- `POST /auth/validate` - Validar token JWT
- `POST /auth/refresh` - Renovar token de acesso
- `POST /auth/logout` - Logout seguro

#### 💼 Experiências (`/experiences`)
- `GET /experiences` - Listar todas as experiências (público)
- `GET /experiences/paginated` - Listar com paginação (público)
- `GET /experiences/{id}` - Buscar experiência por ID (público)
- `POST /experiences` - Criar nova experiência (ADMIN)
- `PUT /experiences/{id}` - Atualizar experiência (ADMIN)
- `DELETE /experiences/{id}` - Deletar experiência (ADMIN)

#### 👤 Administração (`/admin`)
- `POST /admin/create-admin` - Criar usuário admin (desenvolvimento)

#### 📊 Métricas (`/metrics`)
- `GET /metrics` - Obter métricas da aplicação (ADMIN)
- `POST /metrics/reset` - Resetar métricas (ADMIN)

## 🔧 Configuração

### Variáveis de Ambiente

| Variável | Descrição | Padrão                                     |
|----------|-----------|--------------------------------------------|
| `SPRING_PROFILES_ACTIVE` | Perfil ativo | `production`                               |
| `SPRING_DATASOURCE_URL` | URL do banco | `jdbc:postgresql://postgres:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `postgres`                                 |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `IRz{aBLPe{@Yk,2v=@YP`                     |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Servidores Kafka | `localhost:29092`                           |

### Portas

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| Backend | 8080 | Aplicação Spring (interno) |
| PostgreSQL | 5432 | Banco de dados |
| Kafka | 9092, 29092 | Mensageria |
| Kafdrop | 19000 | Interface Kafka |

## 🔒 Segurança

### Recursos de Segurança Implementados

- **JWT Tokens**: Autenticação stateless com access e refresh tokens
- **Rate Limiting**: Proteção contra ataques de força bruta (5 tentativas, 15min lockout)
- **CORS**: Configuração segura para requisições cross-origin
- **Headers de Segurança**: HSTS, X-Frame-Options, Content-Type-Options
- **Validação de Entrada**: Bean Validation em todos os endpoints
- **Auditoria**: Logs estruturados para monitoramento de segurança
- **Blacklist de Tokens**: Invalidação segura de tokens JWT

### Roles e Permissões

- **USER_BASIC**: Usuário padrão (leitura de experiências)
- **ADMIN**: Administrador (CRUD completo)

## 📊 Monitoramento

### Métricas Coletadas

- **Autenticação**: Tentativas de login, sucessos, falhas
- **Experiências**: Criações, buscas, atualizações
- **Kafka**: Eventos publicados
- **Performance**: Taxa de sucesso de login

### Logs Estruturados

- **MDC**: Contexto de usuário e IP em todos os logs
- **Níveis**: DEBUG, INFO, WARN, ERROR
- **Formato**: JSON estruturado para análise

## 🐛 Troubleshooting

### Problemas Comuns

1. **Cloudflare Tunnel não conecta**
   ```bash
   docker logs cloudflare-tunnel
   ```

2. **Backend não inicia**
   ```bash
   docker-compose logs portfolio-backend
   ```

3. **Verificar conectividade**
   ```bash
   curl http://portfolio-backend:8080/actuator/health
   ```

4. **Verificar métricas**
   ```bash
   curl -H "Authorization: Bearer <token>" http://localhost:8080/metrics
   ```

5. **Erro JWT: WeakKeyException**
   ```
   The signing key's size is 328 bits which is not secure enough for the HS512 algorithm
   ```
   **Solução**: Atualizar a chave JWT para pelo menos 512 bits:
   ```bash
   # Gerar nova chave segura
   openssl rand -base64 64
   # Atualizar JWT_SECRET no .env ou application.yml
   ```

6. **Erro PostgreSQL: Cannot commit when autoCommit is enabled**
   ```
   org.postgresql.util.PSQLException: Cannot commit when autoCommit is enabled
   ```
   **Solução**: Configuração já implementada com `hikari.auto-commit: false`

## 🚀 Melhorias Implementadas

### Otimizações de Código
- ✅ **Remoção de código não utilizado**: ~200 linhas removidas
- ✅ **Aplicação dos princípios SOLID**: Código mais coeso e manutenível
- ✅ **Correção de deprecações**: Spring Security 6.x atualizado
- ✅ **Constantes para literais**: Resolução de warnings SonarQube
- ✅ **Validação robusta**: Bean Validation em todos os endpoints

### Arquitetura SOLID
- ✅ **SRP**: Services especializados (AuthTokenService, CookieService, UserAuthenticationService)
- ✅ **OCP**: Constantes centralizadas (TransactionConstants) para fácil extensão
- ✅ **LSP**: Implementações corretas de interfaces Spring Security
- ✅ **ISP**: Services com responsabilidades específicas e coesas
- ✅ **DIP**: Injeção de dependências via construtor

### Performance
- ✅ **Paginação**: Endpoints de experiências com paginação
- ✅ **Cache de tokens**: Blacklist eficiente para invalidação
- ✅ **Rate limiting**: Proteção contra abuso
- ✅ **Logs estruturados**: Melhor observabilidade
- ✅ **Transações otimizadas**: Timeouts configuráveis

### Segurança
- ✅ **JWT robusto**: Access e refresh tokens
- ✅ **Rate limiting**: Proteção contra força bruta (5 tentativas, 15min lockout)
- ✅ **Headers de segurança**: HSTS, X-Frame-Options, Content-Type-Options
- ✅ **Validação de entrada**: Bean Validation
- ✅ **Auditoria**: Logs de segurança estruturados
- ✅ **Transações seguras**: Configuração adequada para PostgreSQL

## 🏗️ Arquitetura de Services

### Services Principais
- **`AuthService`**: Orquestração do processo de autenticação
- **`AuthTokenService`**: Geração e validação de tokens JWT
- **`CookieService`**: Gerenciamento de cookies seguros
- **`UserAuthenticationService`**: Autenticação de usuários
- **`ExperienceService`**: CRUD de experiências profissionais
- **`MetricsService`**: Coleta de métricas de performance
- **`RateLimitingService`**: Controle de taxa de requisições

### Constantes Centralizadas
- **`TransactionConstants`**: Timeouts de transação
- **`SecurityConstants`**: Configurações de segurança
- **`ErrorConstants`**: Mensagens de erro padronizadas
- **`DefaultConstants`**: Valores padrão do sistema
- **`HttpConstants`**: Constantes HTTP
- **`KafkaConstants`**: Configurações Kafka
- **`ValidationConstants`**: Regras de validação

## 🔧 Configuração de Transações

### Configuração PostgreSQL
```yaml
spring:
  datasource:
    hikari:
      auto-commit: false
  jpa:
    properties:
      hibernate:
        connection.provider_disables_autocommit: true
```

### Timeouts Configuráveis
- **AUTH_TIMEOUT**: 30 segundos para operações de autenticação
- **Transações read-only**: Otimizadas para consultas
- **Gerenciamento automático**: Spring Boot auto-configuração

## 📊 Qualidade de Código

### Métricas de Melhoria
- **Linhas removidas**: ~200 linhas de código não utilizado
- **Métodos removidos**: 15+ métodos não utilizados
- **Warnings SonarQube**: 100% resolvidos
- **Deprecações**: Spring Security 6.x atualizado
- **Duplicação de código**: Eliminada com constantes

### Padrões Implementados
- **Builder Pattern**: DTOs com MapStruct
- **Factory Pattern**: Criação de tokens JWT
- **Strategy Pattern**: Diferentes tipos de validação
- **Observer Pattern**: Eventos Kafka
- **Template Method**: Processamento de transações

### Testes e Validação
- **Bean Validation**: Validação automática de entrada
- **Exception Handling**: Tratamento centralizado de erros
- **Logging Estruturado**: MDC para contexto de usuário
- **Métricas**: Coleta automática de performance
