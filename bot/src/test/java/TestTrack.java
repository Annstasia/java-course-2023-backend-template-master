import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.utility.BotUtils;
import edu.java.bot.db.FictiveStorageManager;
import edu.java.bot.db.StorageManager;
import edu.java.bot.dialogs.TrackDialog;
import edu.java.bot.links.classes.GitHubURLInfo;
import edu.java.bot.links.classes.StackOverflowURLInfo;
import edu.java.bot.links.classes.URLInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTrack {
    private final static String trackCorrect = "Ссылка добавлена в наблюдение";
    private final static String trackIncorrect = "Ссылка не распознана";
    StorageManager storage;
    TrackDialog trackDialog;
    Update stackoverflowUpdateMessage;
    Update githubUpdateMessage;
    Update unidentifiedUpdateMessage;
    Update trackUpdateMessage;
    String stackoverflowLink = "https://stackoverflow.com/users/2387041/fool";
    String githubLink = "https://github.com/Annstasia";
    long chatID = 1L;
    String track = "/track";

    Update createUpdateMock(String query) {
        return BotUtils.parseUpdate("{message:{chat:{id:" + chatID + "},text:\""
                                    + query + "\"}}");
    }

    @BeforeEach
    void reset() {
        storage = new FictiveStorageManager();
        trackDialog = new TrackDialog(storage);

        trackUpdateMessage = createUpdateMock(track);
        stackoverflowUpdateMessage = createUpdateMock(stackoverflowLink);
        githubUpdateMessage = createUpdateMock(githubLink);
        unidentifiedUpdateMessage = createUpdateMock("https://en.wikipedia.org/wiki/42_(number)");
    }

    @Test
    void addStackoverflowToTrack() {
        SendMessage answer = trackDialog.handle(stackoverflowUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(trackCorrect, answer.getParameters().get("text"));
        Assertions.assertArrayEquals(
            new URLInfo[] {new StackOverflowURLInfo(stackoverflowLink)},
            storage.getAllLinksById(1).toArray()
        );
    }

    @Test
    void addGitHubToTrack() {
        SendMessage answer = trackDialog.handle(githubUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(trackCorrect, answer.getParameters().get("text"));
        Assertions.assertArrayEquals(
            new URLInfo[] {new GitHubURLInfo(githubLink)},
            storage.getAllLinksById(1).toArray()
        );
    }

    @Test
    void addIdentifiedToTrack() {
        SendMessage answer = trackDialog.handle(unidentifiedUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(trackIncorrect, answer.getParameters().get("text"));
        Assertions.assertTrue(storage.getAllLinksById(chatID).isEmpty());
    }
}
