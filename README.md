# Portfolio Backend

Backend em Java + Spring Boot + Kafka para gerenciamento de portfolio profissional com nginx para produção HTTPS.

**Arquitetura de Produção:**
- **VM Aplicação**: Spring Boot + Nginx (SSL/TLS)
- **VM Infraestrutura**: PostgreSQL + Kafka (externos)

## 🎯 Funcionalidades

- **Autenticação JWT** - Sistema de login seguro com tokens
- **Gestão de Experiências** - CRUD completo de experiências profissionais
- **Sistema de Usuários** - Gerenciamento de usuários e perfis
- **Mensageria Assíncrona** - Eventos via Apache Kafka
- **API REST** - Endpoints documentados com OpenAPI/Swagger
- **Segurança** - Spring Security com autorização baseada em roles
- **Auditoria** - Controle de versão do banco com Liquibase

## 🚀 Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Apache Kafka** - Mensageria assíncrona
- **PostgreSQL 15** - Banco de dados principal
- **Liquibase** - Controle de versão do banco
- **SpringDoc OpenAPI 3** - Documentação automática da API
- **Nginx** - Proxy reverso e SSL/TLS
- **Docker** - Containerização
- **Maven** - Gerenciamento de dependências

## 🏗️ Arquitetura

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Cliente   │───▶│    Nginx    │───▶│   Backend   │
│   (HTTPS)   │    │  (SSL/TLS)  │    │ (Spring)    │
└─────────────┘    └─────────────┘    └─────────────┘
                                              │
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
- Maven 3.6+

### Executando o projeto

1. **Clone o repositório**
   ```bash
   git clone <repository-url>
   cd portfolio-backend
   ```

2. **Execute apenas os serviços de infraestrutura (PostgreSQL + Kafka)**
   ```bash
   docker-compose up -d
   ```

3. **Configure as variáveis de ambiente**
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

4. **Execute a aplicação em modo debug**
   ```bash
   # Via Maven
   ./mvnw spring-boot:run

   # Ou via IDE (IntelliJ, VS Code, etc.)
   # Execute a classe PortfolioBackendApplication
   ```

5. **Acesse a documentação da API**
   - **Swagger UI**: http://localhost:8080/api/swagger-ui.html
   - **OpenAPI JSON**: http://localhost:8080/api/api-docs

### Testes
```bash
# Testes unitários
./mvnw test

# Testes de integração
./mvnw verify
```

## 🚀 Deploy em Produção

### Deploy Automatizado (GitHub Actions)

O deploy em produção é **automatizado** via GitHub Actions quando você faz push para as branches `main` ou `develop`:

1. **Push para `main`** → Deploy em produção (porta 8080)
2. **Push para `develop`** → Deploy em staging (porta 8081)
3. **Pull Request** → Não suportado (apenas staging e produção)

**⚠️ IMPORTANTE**: Staging e produção rodam **simultaneamente** na mesma VM com limites de memória otimizados para 1GB RAM.

### Configuração do GitHub Actions

O workflow está configurado em `.github/workflows/workflow.yml` e:
- Compila o projeto com Maven
- Faz deploy na VM Linux via SSH
- Configura nginx com SSL/TLS
- Gerencia containers com limites de memória
- Faz rolling update sem downtime

### Variáveis de Ambiente (Secrets)

Configure no GitHub Repository Settings → Secrets and Variables → Actions:

**Obrigatórios:**
- `BACKEND_VM_IP` - IP da VM de produção
- `BACKEND_VM_USER` - Usuário SSH da VM
- `BACKEND_VM_SSH_KEY` - Chave privada SSH
- `SPRING_DATASOURCE_URL` - URL do banco de produção
- `SPRING_DATASOURCE_USERNAME` - Usuário do banco
- `SPRING_DATASOURCE_PASSWORD` - Senha do banco
- `SPRING_KAFKA_BOOTSTRAP_SERVERS` - Servidores Kafka

**Opcionais (para certificados Let's Encrypt):**
- `SSL_EMAIL` - Email para certificados Let's Encrypt (padrão: admin@[VM_IP])

### Verificação do Deploy

Após o deploy, verifique se os serviços estão funcionando:

- **Produção**: `https://api.jonathanssm.com/actuator/health`
- **Staging**: `https://staging.jonathanssm.com/actuator/health`
- **API Produção**: `https://api.jonathanssm.com/api/`
- **API Staging**: `https://staging.jonathanssm.com/api/`

**🔧 Correções Implementadas**:
- ✅ Nginx configurado com upstreams corretos para staging e produção
- ✅ Containers com nomes específicos por ambiente (`portfolio-backend-prod`, `portfolio-backend-staging`)
- ✅ Deploy otimizado para VM de 1GB RAM
- ✅ Workflow GitHub Actions otimizado para VM de 1GB RAM

### 🔐 Certificados SSL

**Geração Automática**: Os certificados SSL são gerados automaticamente a cada deploy.

**Características**:
- ✅ **Let's Encrypt** para domínios reais (certificados válidos)
- ✅ **Auto-assinados** para localhost/desenvolvimento
- ✅ **Renovação automática** (diária às 12:00)
- ✅ **Válidos por 90 dias** (Let's Encrypt) ou 10 anos (auto-assinados)
- ✅ **Sem aviso de segurança** (Let's Encrypt)
- ✅ **Compatível com Mixed Content** (HTTPS completo)

**Como Funciona**:
1. **Domínio real** → Gera certificado Let's Encrypt automaticamente
2. **Localhost** → Usa certificado auto-assinado
3. **Renovação** → Automática via cron job
4. **Fallback** → Se Let's Encrypt falhar, usa auto-assinado

**Configuração**:
- Configure `SSL_EMAIL` no GitHub Secrets para Let's Encrypt
- O domínio deve apontar para o IP da VM
- Certificados são renovados automaticamente

## 📁 Estrutura do Projeto

```
portfolio-backend/
├── src/main/java/com/jonathanssm/portfoliobackend/
│   ├── config/          # Configurações (Security, CORS, Kafka)
│   ├── controller/      # Controllers REST
│   ├── dto/            # Data Transfer Objects
│   ├── messaging/      # Kafka producers/consumers
│   ├── model/          # Entidades JPA
│   ├── repository/     # Repositórios JPA
│   ├── service/        # Lógica de negócio
│   └── util/           # Utilitários
├── src/main/resources/
│   ├── application.yml              # Configuração padrão
│   ├── application-production.yml   # Configuração de produção
│   └── db/changelog/               # Scripts Liquibase
├── nginx/              # Configuração do nginx (produção)
│   ├── Dockerfile      # Imagem nginx customizada
│   └── nginx.conf      # Configuração proxy reverso
├── ssl/               # Certificados SSL (produção)
│   └── generate-ssl.sh # Script geração certificados
├── docker-compose.yml # Desenvolvimento local (infraestrutura)
├── docker-compose.prod.yml # Produção (VM Linux)
├── Dockerfile         # Imagem Spring Boot
├── pom.xml           # Dependências Maven
├── mvnw / mvnw.cmd   # Maven Wrapper
└── logs/             # Logs da aplicação
```

## 🔧 Configurações

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
| Nginx HTTP | 80 | Redirecionamento para HTTPS |
| Nginx HTTPS | 443 | API principal |
| Backend Produção | 8080 | Aplicação Spring (interno) |
| Backend Staging | 8081 | Aplicação Spring (interno) |
| PostgreSQL | 5432 | Banco de dados |
| Kafka | 9092, 29092 | Mensageria |
| Kafdrop | 19000 | Interface Kafka |
| Swagger UI | 8080/api/swagger-ui.html | Documentação da API |

### 🏷️ Ambientes

| Ambiente | Branch | Porta | Domínio | Descrição |
|----------|--------|-------|---------|-----------|
| **Produção** | `main` | 8080 | `api.jonathanssm.com` | Ambiente estável |
| **Staging** | `develop` | 8081 | `staging.jonathanssm.com` | Ambiente de testes |

## 📚 Documentação da API

A API possui documentação automática gerada pelo **SpringDoc OpenAPI 3**:

- **Swagger UI**: Interface interativa para testar os endpoints
- **OpenAPI JSON**: Especificação da API em formato JSON
- **Documentação completa**: Todos os endpoints, parâmetros, respostas e exemplos

### Acessando a Documentação

- **Local**: http://localhost:8080/api/swagger-ui.html
- **Desenvolvimento**: https://staging.jonathanssm.com/api/swagger-ui.html
- **Produção**: https://api.jonathanssm.com/api/swagger-ui.html

### 📋 Endpoints Principais

#### 🔐 Autenticação (`/auth`)
- `POST /auth/login` - Login com username/password
- `POST /auth/validate` - Validar token JWT

#### 💼 Experiências (`/experiences`)
- `GET /experiences` - Listar todas as experiências
- `GET /experiences/{id}` - Buscar experiência por ID
- `POST /experiences` - Criar nova experiência (ADMIN)
- `PUT /experiences/{id}` - Atualizar experiência (ADMIN)
- `DELETE /experiences/{id}` - Deletar experiência (ADMIN)

#### 👤 Administração (`/admin`)
- `POST /admin/create-admin` - Criar usuário admin (desenvolvimento)

#### 🔍 Monitoramento
- `GET /actuator/health` - Status da aplicação
- `GET /actuator/metrics` - Métricas do sistema

## 🔒 Segurança

- **HTTPS obrigatório** em produção
- **Headers de segurança** configurados no nginx
- **Rate limiting** para proteção contra ataques
- **Validação de entrada** com Spring Validation
- **Auditoria** com Spring Data Envers

## 📊 Monitoramento

- **Health Checks**: `/actuator/health`
- **Métricas**: `/actuator/metrics`
- **Logs**: `logs/application.log`
- **Kafka UI**: `http://localhost:19000`

## 🐛 Troubleshooting

### Problemas Comuns

1. **Certificados SSL inválidos**
   ```bash
   # Os certificados são regenerados automaticamente a cada deploy
   # Para forçar regeneração, faça um novo push para main/develop
   ```

2. **Backend não inicia**
   ```bash
   # Verificar logs
   docker-compose logs portfolio-backend
   ```

3. **Nginx não consegue conectar ao backend**
   ```bash
   # Verificar se o backend está rodando
   docker-compose ps
   ```

### Logs

```bash
# Todos os serviços
docker-compose logs -f

# Serviço específico
docker-compose logs -f nginx
docker-compose logs -f portfolio-backend
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
