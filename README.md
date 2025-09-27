# Portfolio Backend

Backend em Java + Spring Boot para gerenciamento de portfolio profissional com Cloudflare Tunnel para produção.

## 🎯 Funcionalidades

- **Autenticação JWT** - Sistema de login seguro
- **Gestão de Experiências** - CRUD de experiências profissionais
- **API REST** - Endpoints documentados com OpenAPI/Swagger
- **Mensageria Assíncrona** - Eventos via Apache Kafka
- **Segurança** - Spring Security com autorização baseada em roles

## 🚀 Tecnologias

- **Java 21** + **Spring Boot 3.5.5**
- **PostgreSQL 15** + **Apache Kafka**
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
   - **Swagger UI**: http://localhost:8080/api/swagger-ui.html
   - **Health Check**: http://localhost:8080/api/actuator/health

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

- **Swagger UI**: https://api.jonathanssm.com/api/swagger-ui.html
- **Health Check**: https://api.jonathanssm.com/api/actuator/health

### Endpoints Principais

#### 🔐 Autenticação (`/auth`)
- `POST /auth/login` - Login com username/password
- `POST /auth/validate` - Validar token JWT

#### 💼 Experiências (`/experiences`)
- `GET /experiences` - Listar todas as experiências
- `GET /experiences/{id}` - Buscar experiência por ID
- `POST /experiences` - Criar nova experiência (ADMIN)
- `PUT /experiences/{id}` - Atualizar experiência (ADMIN)
- `DELETE /experiences/{id}` - Deletar experiência (ADMIN)

## 🔧 Configuração

### Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|---------|
| `SPRING_PROFILES_ACTIVE` | Perfil ativo | `production` |
| `SPRING_DATASOURCE_URL` | URL do banco | `jdbc:postgresql://postgres:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `IRz{aBLPe{@Yk,2v=@YP` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Servidores Kafka | `kafka:9092` |

### Portas

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| Backend | 8080 | Aplicação Spring (interno) |
| PostgreSQL | 5432 | Banco de dados |
| Kafka | 9092, 29092 | Mensageria |
| Kafdrop | 19000 | Interface Kafka |

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
