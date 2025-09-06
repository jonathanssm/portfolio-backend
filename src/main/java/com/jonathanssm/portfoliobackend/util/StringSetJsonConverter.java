package com.jonathanssm.portfoliobackend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Converter
public class StringSetJsonConverter implements AttributeConverter<Set<String>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]"; // Retorna um JSON vazio
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Erro ao serializar Set<String> para JSON", e);
        }
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty() || "[]".equals(dbData)) {
            return new HashSet<>();
        }
        try {
            CollectionType setType = objectMapper.getTypeFactory()
                    .constructCollectionType(Set.class, String.class);
            return objectMapper.readValue(dbData, setType);
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao desserializar JSON para Set<String>", e);
        }
    }
}
