package com.jonathanssm.portfoliobackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
class PortfolioBackendApplicationTests {

	@Test
	void contextLoads() {
        // teste
	}

}
