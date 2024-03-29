package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.sources_interection.client.GitHubClient;
import edu.java.sources_interection.response.GitHubResponse;
import edu.java.sources_interection.url_info.GitHubRepositoryURLInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class GitHubTest {
    private final static int PORT = 8099;
    private final static String HOST = "localhost";
    private final static WireMockServer wireMockServer = new WireMockServer(PORT);
    private final static WebClient.Builder wireBuilder = WebClient.builder().baseUrl("http://" + HOST + ":" + PORT);
    private final static GitHubClient client = new GitHubClient(wireBuilder);
    private final static OffsetDateTime lastModified = OffsetDateTime.of(2024, 2, 20, 18, 0, 0, 0, ZoneOffset.UTC);
    private final static String USER_NAME = "user";
    private final static String REPO_NAME = "repo";

    @BeforeAll public static void initServer() {
        wireMockServer.start();
        configureFor(HOST, PORT);

        String mockedPath = "/repos/" + USER_NAME + "/" + REPO_NAME + "/events";
        String lastModifiedHeader = lastModified.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        stubFor(get(urlEqualTo(mockedPath)).withHeader("If-Modified-Since", WireMock.before(lastModifiedHeader))
                                           .willReturn(ok().withHeader("Last-Modified", lastModifiedHeader)));
        stubFor(get(urlEqualTo(mockedPath)).withHeader("If-Modified-Since",
                                                       WireMock.after(lastModified.format(DateTimeFormatter.RFC_1123_DATE_TIME))
        ).willReturn(WireMock.aResponse().withStatus(304).withHeader("Last-Modified", lastModifiedHeader)));
    }

    @Test public void foundUpdatesTest() {
        OffsetDateTime lastScrapped = lastModified.minusHours(1);
        GitHubResponse updates = client.getUpdates(new GitHubRepositoryURLInfo(USER_NAME, REPO_NAME, lastScrapped));
        Assertions.assertEquals(updates.getLastModified(), lastModified);
    }

    @Test public void notFountUpdatesTest() {
        OffsetDateTime lastScrapped = lastModified.plusHours(1);
        GitHubResponse updates = client.getUpdates(new GitHubRepositoryURLInfo(USER_NAME, REPO_NAME, lastScrapped));
        Assertions.assertNull(updates);
    }

    @Test public void errorTest() {
        OffsetDateTime lastScrapped = lastModified.minusHours(1);
        GitHubResponse updates =
            client.getUpdates(new GitHubRepositoryURLInfo("NOT" + USER_NAME, REPO_NAME, lastScrapped));
        Assertions.assertNull(updates);
    }

    @AfterAll public static void stopWire() {
        wireMockServer.stop();
    }
}
