package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes relacionadas a HTTP
 */
public final class HttpConstants {

    private HttpConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== HTTP HEADERS ====================
    
    public static final class Headers {
        
        private Headers() {
            throw new UnsupportedOperationException("Utility class");
        }
        
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String AUTHORIZATION = "Authorization";
        public static final String USER_AGENT = "User-Agent";
        public static final String X_FORWARDED_FOR = "X-Forwarded-For";
        public static final String X_REAL_IP = "X-Real-IP";
    }
}
