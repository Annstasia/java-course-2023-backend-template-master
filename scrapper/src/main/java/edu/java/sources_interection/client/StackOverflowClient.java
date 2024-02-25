package edu.java.sources_interection.client;

import edu.java.sources_interection.dto.StackOverflowDTO;
import edu.java.sources_interection.response.StackOverflowResponse;
import edu.java.sources_interection.url_info.StackOverflowQuestionURLInfo;
import edu.java.sources_interection.url_info.StackOverflowSearchURLInfo;
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

@Slf4j public class StackOverflowClient {
    WebClient client;

    public StackOverflowClient(WebClient.Builder builder) {
        client = builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    private static void addDateAndSite(StringBuilder builder, long fromdate) {
        builder.append("fromdate=")
               .append(fromdate)
               .append("&site=stackoverflow");
    }

    private static String getQuestionPath(long questionId, long fromdate) {
        StringBuilder builder = new StringBuilder("/questions/")
            .append(questionId)
            .append("?");
        addDateAndSite(builder, fromdate);
        return builder.toString();
    }

    private static String getSearchPath(String searchQuery, long fromdate) {
        StringBuilder builder = new StringBuilder("/search?q=")
            .append(searchQuery)
            .append("&");
        addDateAndSite(builder, fromdate);
        return builder.toString();
    }

    private StackOverflowResponse getUpdates(String uri) {
        ResponseEntity<StackOverflowDTO> response =
            client.method(HttpMethod.GET).uri(uri).retrieve().toEntity(StackOverflowDTO.class)
                  .doOnError(e -> log.error(e.getMessage())).onErrorResume(e -> Mono.empty()).block();
        if (response == null) {
            return null;
        }
        StackOverflowResponse soResponse = null;
        if (response.getBody() != null) {
            if (!response.getBody().items().isEmpty()) {
                soResponse =
                    new StackOverflowResponse(OffsetDateTime.ofInstant(
                        Instant.ofEpochSecond(response.getBody().items()
                                                      .get(0)
                                                      .lastActivityDate()),
                        ZoneOffset.UTC
                    ));
            }

        }
        return soResponse;
    }

    public StackOverflowResponse getUpdates(StackOverflowQuestionURLInfo questionURL) {
        return getUpdates(getQuestionPath(questionURL.getQuestionID(), questionURL.getLastUpdate().toEpochSecond()));
    }

    public StackOverflowResponse getUpdates(StackOverflowSearchURLInfo searchURLInfo) {
        return getUpdates(getSearchPath(searchURLInfo.getSearchQuery(), searchURLInfo.getLastUpdate().toEpochSecond()));
    }

}
