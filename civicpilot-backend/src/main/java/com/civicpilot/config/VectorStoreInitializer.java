package com.civicpilot.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.civicpilot.service.GovernmentServiceVectorService;

@Configuration
public class VectorStoreInitializer {

    @Bean
    public CommandLineRunner initializeVectorStore(
            GovernmentServiceVectorService
                    governmentServiceVectorService) {

        return args -> {

            governmentServiceVectorService
                    .indexGovernmentServices();

            System.out.println(
                    "CivicPilot AI: Government services indexed into vector store."
            );
        };
    }
}