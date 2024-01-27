package br.com.fiap.soat1.t32.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateFactory {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
