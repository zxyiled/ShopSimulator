package org.config;

import org.app.SysInventory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * Single SysInventory instance (in-memory) shared across all controllers.
     * Replace with a persistent implementation when needed.
     */
    @Bean
    public SysInventory sysInventory() {
        return new SysInventory();
    }

    /**
     * Global CORS configuration.
     * Only allows requests from the same server (localhost:8080).
     * Change the allowed origin when deploying to production.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PATCH", "DELETE");
    }
}