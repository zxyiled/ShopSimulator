package org.config;

import org.app.SysInventory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Single SysInventory instance (in-memory) shared across all controllers.
     * Replace with a persistent implementation when needed.
     */
    @Bean
    public SysInventory sysInventory() {
        return new SysInventory();
    }
}