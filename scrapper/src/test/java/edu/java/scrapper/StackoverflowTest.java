package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import edu.java.sources_interection.client.StackOverflowClient;
import edu.java.sources_interection.response.StackOverflowResponse;
import edu.java.sources_interection.url_info.StackOverflowQuestionURLInfo;
import edu.java.sources_interection.url_info.StackOverflowSearchURLInfo;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Predicate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class StackoverflowTest {
    private final static int PORT = 8099;
    private final static String HOST = "localhost";
    private final static WireMockServer wireMockServer = new WireMockServer(PORT);
    private final static WebClient.Builder wireBuilder = WebClient.builder().baseUrl("http://" + HOST + ":" + PORT);
    private final static StackOverflowClient client = new StackOverflowClient(wireBuilder);
    private final static OffsetDateTime lastActive = OffsetDateTime.of(2024, 2, 20, 18, 0, 0, 0, ZoneOffset.UTC);
    private final static OffsetDateTime prevActive = OffsetDateTime.of(2024, 2, 18, 18, 0, 0, 0, ZoneOffset.UTC);
    private final static long QUESTION_ID = 1;
    private final static String SEARCH_QUERY = "unsupported link";

    @Before public void initServer() {
        wireMockServer.start();
        configureFor(HOST, PORT);
    }

    @Before public void setUpQuestionsMock() {
        String question = "/questions/" + QUESTION_ID;
        wireMockServer.stubFor(get(urlPathEqualTo(question)).andMatching(new FromDateMatcher(lastUpdate -> lastUpdate
                                                                                                           <= lastActive.toEpochSecond()))
                                                            .withQueryParam("site", equalTo("stackoverflow"))
                                                            .willReturn(okJson("{\"items\":[{\"last_activity_date\":"
                                                                               + lastActive.toEpochSecond() + "}]}")));
        wireMockServer.stubFor(get(urlPathEqualTo(question)).andMatching(new FromDateMatcher(lastUpdate -> lastUpdate
                                                                                                           > lastActive.toEpochSecond()))
                                                            .withQueryParam("site", equalTo("stackoverflow"))
                                                            .willReturn(okJson("{\"items\":[]}")));
    }

    @Before public void setUpSearchMock() {
        wireMockServer.stubFor(get(urlPathEqualTo("/search")).withQueryParam("q", equalTo(SEARCH_QUERY))
                                                             .withQueryParam("site", equalTo("stackoverflow"))
                                                             .andMatching(new FromDateMatcher(lastUpdate -> lastUpdate
                                                                                                            <= prevActive.toEpochSecond()))
                                                             .willReturn(okJson("{\"items\":[{\"last_activity_date\":"
                                                                                + lastActive.toEpochSecond() + "},"
                                                                                + "{\"last_activity_date\":"
                                                                                + prevActive.toEpochSecond() + "}]}")));

        wireMockServer.stubFor(get(urlPathEqualTo("/search")).withQueryParam("q", equalTo(SEARCH_QUERY))
                                                             .withQueryParam("site", equalTo("stackoverflow"))
                                                             .andMatching(new FromDateMatcher(lastUpdate -> lastUpdate
                                                                                                            > lastActive.toEpochSecond()))
                                                             .willReturn(okJson("{\"items\":[]}")));
    }

    @Test public void foundQuestionUpdatesTest() {
        OffsetDateTime lastScrapped = lastActive.minusHours(1);
        StackOverflowResponse updates = client.getUpdates(new StackOverflowQuestionURLInfo(QUESTION_ID, lastScrapped));
        Assertions.assertEquals(updates.getLastModified(), lastActive);
    }

    @Test public void notFoundQuestionUpdatesTest() {
        OffsetDateTime lastScrapped = lastActive.plusHours(1);
        StackOverflowResponse updates = client.getUpdates(new StackOverflowQuestionURLInfo(QUESTION_ID, lastScrapped));
        Assertions.assertNull(updates);
    }

    @Test public void foundSearchUpdatesTest() {
        OffsetDateTime lastScrapped = prevActive.minusHours(1);
        StackOverflowResponse updates = client.getUpdates(new StackOverflowSearchURLInfo(SEARCH_QUERY, lastScrapped));
        Assertions.assertEquals(updates.getLastModified(), lastActive);
    }

    @Test public void notFoundSearcgUpdatesTest() {
        OffsetDateTime lastScrapped = lastActive.plusHours(1);
        StackOverflowResponse updates = client.getUpdates(new StackOverflowSearchURLInfo(SEARCH_QUERY, lastScrapped));
        Assertions.assertNull(updates);
    }

    @Test public void errorTest() {
        OffsetDateTime lastScrapped = lastActive.minusHours(1);
        StackOverflowResponse updates =
            client.getUpdates(new StackOverflowQuestionURLInfo(QUESTION_ID + 1, lastScrapped));
        Assertions.assertNull(updates);
    }

    @After public void stopWire() {
        wireMockServer.stop();
    }

    private static class FromDateMatcher extends RequestMatcherExtension {
        private final Predicate<Long> matcher;

        public FromDateMatcher(Predicate<Long> matcher) {
            this.matcher = matcher;
        }

        @Override public String getName() {
            return "long-before";
        }

        @Override public MatchResult match(Request request, Parameters parameters) {
            return MatchResult.of(matcher.test(Long.parseLong(request.queryParameter("fromdate").values().getFirst())));
        }
    }
}
