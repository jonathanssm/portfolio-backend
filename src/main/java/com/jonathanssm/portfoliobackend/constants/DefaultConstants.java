package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes com valores padrão
 */
public final class DefaultConstants {

    private DefaultConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== DEFAULT VALUES ====================
    
    public static final String UNKNOWN = "unknown";
    public static final String SYSTEM = "system";
    public static final String EMPTY_STRING = "";
    public static final String PUBLIC_REGISTRATION = "public_registration";

    // ==================== BOOLEAN VALUES ====================
    
    public static final class BooleanValues {
        
        private BooleanValues() {
            throw new UnsupportedOperationException("Utility class");
        }

        public static final String NULL = "null";
    }
    
    // ==================== ENTITY NAMES ====================
    
    public static final class EntityNames {
        
        private EntityNames() {
            throw new UnsupportedOperationException("Utility class");
        }

        public static final String EXPERIENCE = "Experience";
    }
}
