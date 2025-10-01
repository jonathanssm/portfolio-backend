package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes relacionadas à segurança e autenticação
 */
public final class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== AUTENTICAÇÃO JWT ====================
    
    public static final class Authentication {
        
        private Authentication() {
            throw new UnsupportedOperationException("Utility class");
        }
        
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String TOKEN_TYPE = "Bearer";
        public static final Long TOKEN_EXPIRATION_HOURS = 24L;
        public static final Long TOKEN_EXPIRATION_SECONDS = 86400L; // 24 horas
    }
    
    // ==================== TOKEN FIELDS ====================
    
    public static final class TokenFields {
        
        private TokenFields() {
            throw new UnsupportedOperationException("Utility class");
        }
        
        public static final String TYPE = "type";
        public static final String REFRESH = "refresh";
    }
}
