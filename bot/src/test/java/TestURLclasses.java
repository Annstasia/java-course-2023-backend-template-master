import edu.java.bot.links.classes.GitHubURLInfo;
import edu.java.bot.links.classes.StackOverflowURLInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestURLclasses {
    @Test
    void stackoverflowLinkTest() {
        Assertions.assertEquals("StackOverflow",
                                new StackOverflowURLInfo("https://stackoverflow.com").getResourceType()
                                );
        Assertions.assertEquals("https://stackoverflow.com",
                                new StackOverflowURLInfo("https://stackoverflow.com").toString());
    }

    @Test
    void githubLnkTest() {
        Assertions.assertEquals("GitHub", new GitHubURLInfo("https://github.com").getResourceType());
        Assertions.assertEquals("https://github.com", new GitHubURLInfo("https://github.com").toString());
    }
}
