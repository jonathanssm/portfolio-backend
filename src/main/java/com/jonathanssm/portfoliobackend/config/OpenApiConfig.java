package com.jonathanssm.portfoliobackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Portfolio Backend API")
                        .description("API REST para gerenciamento de portfolio profissional")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jonathan Sebastian da Silva Moraes")
                                .email("contato@jonathanssm.com")
                                .url("https://github.com/jonathanssm")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080" + contextPath)
                                .description("Servidor Local"),
                        new Server()
                                .url("https://staging.jonathanssm.com" + contextPath)
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.jonathanssm.com" + contextPath)
                                .description("Servidor de Produção")
                ));
    }
}
