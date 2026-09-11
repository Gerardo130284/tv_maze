package tv.maze.config;

import org.springframework.boot.restclient.autoconfigure.RestClientBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
	
	
    @Bean
    RestClient restClient(RestClientBuilderConfigurer configurer) {
        RestClient.Builder builder = RestClient.builder();
        configurer.configure(builder);
        return builder
                .baseUrl("https://api.tvmaze.com") 
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}