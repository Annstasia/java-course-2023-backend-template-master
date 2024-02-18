import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.utility.BotUtils;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.db.FictiveStorageManager;
import edu.java.bot.db.StorageManager;
import edu.java.bot.links.classes.GitHubURLInfo;
import edu.java.bot.links.classes.StackOverflowURLInfo;
import edu.java.bot.links.classes.URLInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.bouncycastle.oer.its.etsi102941.Url;

public class TestTrack {
    StorageManager storage;
    TrackCommand trackCommand;
    Update stackoverflowUpdateMessage;
    Update githubUpdateMessage;
    Update unidentifiedUpdateMessage;

    String stackoverflowLink = "https://stackoverflow.com/users/2387041/fool";
    String githubLink = "https://github.com/Annstasia";
    long chatID = 1L;

    String track = "/track";

    private final static String trackCorrect = "Ссылка добавлена в наблюдение";

    private final static String trackIncorrect = "Ссылка не распознана";

    Update createUpdateMock(String query) {
        return BotUtils.parseUpdate("{message:{chat:{id:" + chatID + "},text:\""
            + query + "\"}}");
    }

@BeforeEach
    void reset() {
        storage = new FictiveStorageManager();
        trackCommand = new TrackCommand(storage);

        stackoverflowUpdateMessage = createUpdateMock(track + " " + stackoverflowLink);
        githubUpdateMessage = createUpdateMock(track + " " + githubLink);
        unidentifiedUpdateMessage = createUpdateMock(track + " " + "https://en.wikipedia.org/wiki/42_(number)");
    }


    @Test
    void trackName() {
        Assertions.assertEquals(track, trackCommand.getCommandName());
    }
    @Test
    void addStackoverflowToTrack() {
        SendMessage answer = trackCommand.handle(stackoverflowUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(trackCorrect, answer.getParameters().get("text"));
        Assertions.assertArrayEquals(new URLInfo[]{new StackOverflowURLInfo(stackoverflowLink)},
                                     storage.getAllLinksById(1).toArray());
    }

    @Test
    void addGitHubToTrack() {
        SendMessage answer = trackCommand.handle(githubUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(trackCorrect, answer.getParameters().get("text"));
        Assertions.assertArrayEquals(new URLInfo[]{new GitHubURLInfo(githubLink)},
                                     storage.getAllLinksById(1).toArray());
    }

    @Test
    void addIdentifiedToTrack() {
        SendMessage answer = trackCommand.handle(unidentifiedUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(trackIncorrect, answer.getParameters().get("text"));
        Assertions.assertTrue(storage.getAllLinksById(chatID).isEmpty());
    }
}
