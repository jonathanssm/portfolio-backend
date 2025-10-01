package com.jonathanssm.portfoliobackend.constants;

/**
 * Constantes para configuração de transações
 * <p>
 * OCP: Facilita extensão sem modificação
 * SRP: Responsabilidade única - apenas constantes de transação
 */
public final class TransactionConstants {

    private TransactionConstants() {
        // Utility class
    }

    /**
     * Timeout para operações de autenticação (em segundos)
     */
    public static final int AUTH_TIMEOUT = 30;

}
