package com.jonathanssm.portfoliobackend.util;

import jakarta.persistence.EntityNotFoundException;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Utility class para operações comuns de repositórios JPA
 * Segue o padrão Utility Class para eliminar duplicação de código
 */
@UtilityClass
public class JpaUtils {

    /**
     * Busca entidade por ID ou lança exceção se não encontrada
     * 
     * @param repository Repositório JPA
     * @param id ID da entidade
     * @param entityName Nome da entidade para mensagem de erro
     * @param <T> Tipo da entidade
     * @param <K> Tipo do ID
     * @return Entidade encontrada
     * @throws EntityNotFoundException se entidade não for encontrada
     * @throws IllegalArgumentException se ID for null
     */
    public static <T, K> T findEntityByIdOrThrow(JpaRepository<T, K> repository, K id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " not found with id: " + id));
    }
}
