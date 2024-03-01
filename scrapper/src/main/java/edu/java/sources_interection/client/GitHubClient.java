package edu.java.sources_interection.client;

import edu.java.sources_interection.dto.GitHubDTO;
import edu.java.sources_interection.response.GitHubResponse;
import edu.java.sources_interection.url_info.GitHubRepositoryURLInfo;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class GitHubClient {
    private final WebClient client;

    public GitHubClient(WebClient.Builder builder) {
        client = builder
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    private static String getRepoPath(String user, String repo) {
        return "repos/" + user + "/" + repo + "/events";
    }

    public GitHubResponse getUpdates(GitHubRepositoryURLInfo gitHubURL) {
        ResponseEntity<GitHubDTO[]> response = client
            .method(HttpMethod.GET)
            .uri(getRepoPath(gitHubURL.getName(), gitHubURL.getRepository()))
            .ifModifiedSince(gitHubURL.getLastUpdate().atZoneSameInstant(ZoneOffset.UTC))
            .retrieve()
            .toEntity(GitHubDTO[].class)
            .doOnError(e -> log.error(e.getMessage()))
            .onErrorResume(e -> Mono.empty()).block();

        if (response == null) {
            return null;
        }
        GitHubResponse gitHubResponse = null;
        if (response.getStatusCode().is2xxSuccessful()
            && response.getHeaders().get("Last-Modified") != null) {
            gitHubResponse = new GitHubResponse(
                OffsetDateTime.ofInstant(
                    Instant.ofEpochMilli(response.getHeaders().getLastModified()),
                    ZoneOffset.UTC
                )
            );
        }
        return gitHubResponse;
    }
}
