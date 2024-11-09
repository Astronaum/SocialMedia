package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SocialNetworkApplication  extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SocialNetworkApplication.class); // Your main application class
    }

    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication.class, args); // For running via Spring Boot CLI or in a standalone mode
    }
}
