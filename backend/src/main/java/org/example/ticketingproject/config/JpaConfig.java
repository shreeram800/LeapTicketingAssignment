package org.example.ticketingproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // This enables JPA auditing which automatically sets @CreatedDate and @LastModifiedDate
}
