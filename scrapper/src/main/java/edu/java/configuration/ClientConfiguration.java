package edu.java.configuration;

import edu.java.sources_interection.client.GitHubClient;
import edu.java.sources_interection.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Value("${app.url.github}")
    private String github;
    @Value("${app.url.stackoverflow}")
    private String stackoverflow;

    @Bean
    public StackOverflowClient stackOverflowClient(WebClient.Builder builder) {
        return new StackOverflowClient(builder.baseUrl(stackoverflow));
    }

    @Bean
    public GitHubClient gitHubClient(WebClient.Builder builder) {
        return new GitHubClient(builder.baseUrl(github));
    }

}
