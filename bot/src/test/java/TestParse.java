import edu.java.bot.links.classes.GitHubURLInfo;
import edu.java.bot.links.classes.StackOverflowURLInfo;
import edu.java.bot.links.parsers.GitHubURLParser;
import edu.java.bot.links.parsers.LinkParserChain;
import edu.java.bot.links.parsers.StackOverflowURLParser;
import edu.java.bot.links.parsers.URLParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestParse {
    @Test
    void stackoverflowLinkTest() {
        Assertions.assertEquals(
            new StackOverflowURLInfo("https://stackoverflow.com/questions/3338865/how-to-master-java"),
            new StackOverflowURLParser().parseLink("https://stackoverflow.com/questions/3338865/how-to-master-java")
        );
    }

    @Test
    void githubLinkTest() {
        Assertions.assertEquals(
            new GitHubURLInfo("https://github.com/Annstasia"),
            new GitHubURLParser().parseLink("https://github.com/Annstasia")
        );
    }

    @Test
    void chainParserTest() {
        URLParser parser = new LinkParserChain();
        Assertions.assertEquals(
            new StackOverflowURLInfo("https://stackoverflow.com/questions/3338865/how-to-master-java"),
            parser.parseLink("https://stackoverflow.com/questions/3338865/how-to-master-java")
        );
        Assertions.assertEquals(
            new GitHubURLInfo("https://github.com/Annstasia"),
            parser.parseLink("https://github.com/Annstasia")
        );
    }

    @Test
    void wrongLinkTest() {
        Assertions.assertNull(new GitHubURLParser().parseLink(
            "https://stackoverflow.com/questions/3338865/how-to-master-java"));
        Assertions.assertNull(
            new StackOverflowURLParser().parseLink("https://github.com/Annstasia"));
        Assertions.assertNull(new LinkParserChain().parseLink("https://www.wikipedia.org/"));

    }

}
