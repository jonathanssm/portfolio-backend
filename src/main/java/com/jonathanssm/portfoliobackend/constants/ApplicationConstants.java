package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes da aplicação centralizadas para evitar duplicação e facilitar manutenção.
 * Seguindo as melhores práticas de desenvolvimento.
 */
@SuppressWarnings("java:S1118") // Add a private constructor to hide the implicit public one
public final class ApplicationConstants {

    // Construtor privado para evitar instanciação
    private ApplicationConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== MENSAGENS DE ERRO ====================
    
    /**
     * Mensagens de erro para usuários não encontrados
     */
    public static final class ErrorMessages {
        public static final String USER_NOT_FOUND = "User not found: ";
        public static final String PROFILE_NOT_FOUND = "Profile not found: ";
        public static final String ROLE_NOT_FOUND = "Role not found: ";
        
        /**
         * Mensagens de erro para elementos já existentes
         */
        public static final class AlreadyExists {
            public static final String USERNAME_ALREADY_EXISTS = "Username already exists: ";
            public static final String EMAIL_ALREADY_EXISTS = "Email already exists: ";
            public static final String PROFILE_ALREADY_EXISTS = "Profile already exists: ";
            public static final String ROLE_ALREADY_EXISTS = "Role already exists: ";
        }
        
        /**
         * Mensagens de erro do sistema
         */
        public static final class System {
            public static final String INTERNAL_SERVER_ERROR = "Erro interno do servidor";
            public static final String KAFKA_ERROR = "Erro ao processar evento Kafka";
            public static final String KAFKA_LISTENER_ERROR = "Erro no processamento de mensagem Kafka";
            public static final String INVALID_CREDENTIALS = "Credenciais inválidas";
            public static final String INVALID_JSON = "Body da requisição está vazio ou JSON inválido";
            public static final String ENDPOINT_NOT_FOUND = "Endpoint não encontrado";
            public static final String ELEMENT_NOT_FOUND = "Elemento não encontrado";
            public static final String VALIDATION_ERROR = "Dados de entrada inválidos";
            public static final String CONSTRAINT_VIOLATION = "Violação de constraints";
        }
    }

    // ==================== AUTENTICAÇÃO E AUTORIZAÇÃO ====================
    
    /**
     * Constantes relacionadas à autenticação JWT
     */
    public static final class Authentication {
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String TOKEN_TYPE = "Bearer";
        public static final Long TOKEN_EXPIRATION_HOURS = 24L;
        public static final Long TOKEN_EXPIRATION_SECONDS = 86400L; // 24 horas
    }

    // ==================== PERFIS E ROLES ====================
    
    /**
     * Nomes dos perfis padrão do sistema
     */
    public static final class Profiles {
        public static final String USER_BASIC = "USER_BASIC";
        public static final String ADMIN = "ADMIN";
        public static final String MODERATOR = "MODERATOR";
        public static final String SUPER_ADMIN = "SUPER_ADMIN";
    }
    
    /**
     * Nomes das roles padrão do sistema
     */
    public static final class Roles {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
        public static final String MODERATOR = "MODERATOR";
    }

    // ==================== USUÁRIO ADMIN PADRÃO ====================
    
    /**
     * Dados do usuário administrador padrão para desenvolvimento
     */
    public static final class DefaultAdmin {
        public static final String USERNAME = "admin";
        public static final String EMAIL = "admin@portfolio.com";
        public static final String PASSWORD = "admin123";
        public static final String FIRST_NAME = "Admin";
        public static final String LAST_NAME = "User";
    }

    // ==================== BANCO DE DADOS ====================
    
    /**
     * Constantes relacionadas ao banco de dados
     */
    public static final class Database {
        public static final String SCHEMA_PORTFOLIO = "portfolio";
        public static final String SCHEMA_AUDIT = "portfolio_aud";
        
        /**
         * Nomes das tabelas
         */
        public static final class Tables {
            public static final String USERS = "users";
            public static final String PROFILES = "profiles";
            public static final String ROLES = "roles";
            public static final String USER_PROFILES = "user_profiles";
            public static final String PROFILE_ROLES = "profile_roles";
            public static final String TECHNOLOGIES = "technologies";
            public static final String TECHNOLOGY_TYPES = "technology_types";
            public static final String EXPERIENCES = "experiences";
            public static final String EXPERIENCE_TECHNOLOGIES = "experience_technologies";
        }
    }

    // ==================== KAFKA ====================
    
    /**
     * Constantes relacionadas ao Kafka
     */
    public static final class Kafka {
        public static final String EXPERIENCE_TOPIC = "portfolio-experience-events";
        public static final int TOPIC_PARTITIONS = 3;
        public static final short TOPIC_REPLICAS = 1;
    }

    // ==================== HTTP HEADERS ====================
    
    /**
     * Headers HTTP comuns
     */
    public static final class HttpHeaders {
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String AUTHORIZATION = "Authorization";
    }

    // ==================== VALIDAÇÃO ====================
    
    /**
     * Constantes para validação
     */
    public static final class Validation {
        public static final String ALREADY_EXISTS_KEYWORD = "already exists";
        public static final String INVALID_CREDENTIALS_KEYWORD = "Credenciais inválidas";
    }

    // ==================== LOGS ====================
    
    /**
     * Emojis e símbolos para logs estruturados
     */
    public static final class LogEmojis {
        public static final String LOCK = "🔐";
        public static final String USER = "👤";
        public static final String SUCCESS = "✅";
        public static final String ERROR = "❌";
        public static final String WARNING = "⚠️";
        public static final String INFO = "ℹ️";
        public static final String CREATE = "📝";
        public static final String UPDATE = "♻️";
        public static final String DELETE = "🗑️";
        public static final String SEARCH = "🔎";
    }
}
