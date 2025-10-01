package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes relacionadas ao Apache Kafka
 */
public final class KafkaConstants {

    private KafkaConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== KAFKA TOPICS ====================

    public static final String AUTH_TOPIC = "portfolio-auth-events";
    public static final String USER_TOPIC = "portfolio-user-events";
    public static final String EXPERIENCE_TOPIC = "portfolio-experience-events";
    public static final String ADMIN_TOPIC = "portfolio-admin-events";

    // ==================== TOPIC CONFIGURATION ====================

    public static final int TOPIC_PARTITIONS = 3;
    public static final short TOPIC_REPLICAS = 1;

    // ==================== CONSUMER GROUPS ====================

    public static final String AUTH_SECURITY_HANDLER = "auth-security-handler";
    public static final String USER_ROUTER = "user-router";
    public static final String EXPERIENCE_ROUTER = "experience-router";

    // ==================== EVENT KEYS ====================

    public static final class EventKeys {

        private EventKeys() {
            throw new UnsupportedOperationException("Utility class");
        }

        // Auth events
        public static final String AUTH_LOGIN_SUCCESS = "auth.login.success";
        public static final String AUTH_LOGIN_FAILURE = "auth.login.failure";
        public static final String AUTH_LOGOUT = "auth.logout";
        public static final String AUTH_TOKEN_REFRESH = "auth.token.refresh";
        public static final String AUTH_TOKEN_INVALID = "auth.token.invalid";

        // User events
        public static final String USER_CREATED = "user.created";
        public static final String USER_UPDATED = "user.updated";
        public static final String USER_DELETED = "user.deleted";
        public static final String USER_REGISTERED = "user.registered";
        public static final String USER_PROFILE_ADDED = "user.profile.added";
        public static final String USER_PROFILE_REMOVED = "user.profile.removed";

        // Experience events
        public static final String EXPERIENCE_CREATED = "experience.created";
        public static final String EXPERIENCE_UPDATED = "experience.updated";
        public static final String EXPERIENCE_DELETED = "experience.deleted";
        public static final String EXPERIENCE_FETCHED = "experience.fetched";

        // Admin events
        public static final String ADMIN_USER_CREATED = "admin.user.created";
    }

    // ==================== EVENT FIELDS ====================

    public static final class EventFields {

        private EventFields() {
            throw new UnsupportedOperationException("Utility class");
        }

        public static final String USER_ID = "userId";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String PROFILE_NAME = "profileName";
        public static final String REGISTRATION_TYPE = "registrationType";
        public static final String TIMESTAMP = "timestamp";
        public static final String IP_ADDRESS = "ipAddress";
        public static final String USER_AGENT = "userAgent";
        public static final String REASON = "reason";
        public static final String CREATED_BY = "createdBy";
        public static final String ADMIN_USER_ID = "adminUserId";
        public static final String ADMIN_USERNAME = "adminUsername";
        public static final String ADMIN_EMAIL = "adminEmail";
        public static final String ATTEMPTED_USERNAME = "attemptedUsername";
    }
}
