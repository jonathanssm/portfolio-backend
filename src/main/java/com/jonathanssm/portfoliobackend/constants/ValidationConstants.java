package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes relacionadas à validação
 */
public final class ValidationConstants {

    private ValidationConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== VALIDAÇÃO ====================

    public static final String ALREADY_EXISTS_KEYWORD = "already exists";
    
    // ==================== MENSAGENS DE VALIDAÇÃO ====================
    
    public static final class ValidationMessages {
        
        private ValidationMessages() {
            throw new UnsupportedOperationException("Utility class");
        }

        public static final String USER_NOT_FOUND_BY_USERNAME = "User not found with username: ";
    }
}
