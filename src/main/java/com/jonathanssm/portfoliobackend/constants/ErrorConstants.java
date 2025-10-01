package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes relacionadas a mensagens de erro
 */
public final class ErrorConstants {

    private ErrorConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== MENSAGENS DE ERRO ====================

    public static final String USER_NOT_FOUND = "User not found: ";
    public static final String PROFILE_NOT_FOUND = "Profile not found: ";

    // ==================== SYSTEM ERRORS ====================

    public static final class System {

        private System() {
            throw new UnsupportedOperationException("Utility class");
        }

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
