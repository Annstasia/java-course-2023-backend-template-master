import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.utility.BotUtils;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.db.FictiveStorageManager;
import edu.java.bot.db.StorageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestUntrack {
    private final static String untrackCorrect = "Ссылка убрана из наблюдения";
    private final static String untrackIncorrect = "Ссылка не распознана";
    private final static String untrackNotExisting = "Ссылка не найдена среди находящихся на мониторинге";
    StorageManager storage;
    UntrackCommand untrackCommand;
    Update stackoverflowUpdateMessage;
    Update githubUpdateMessage;
    Update unidentifiedUpdateMessage;
    String stackoverflowLink = "https://stackoverflow.com/users/2387041/fool";
    String githubLink = "https://github.com/Annstasia";
    long chatID = 1L;
    String untrack = "/untrack";

    Update createUpdateMock(String query) {
        return BotUtils.parseUpdate("{message:{chat:{id:" + chatID + "},text:\""
            + query + "\"}}");
    }

    @BeforeEach
    void reset() {
        storage = new FictiveStorageManager();
        TrackCommand trackCommand = new TrackCommand(storage);
        String track = "/track";

        trackCommand.handle(createUpdateMock(track + " " + stackoverflowLink));
        trackCommand.handle(createUpdateMock(track + " " + githubLink));

        untrackCommand = new UntrackCommand(storage);
        stackoverflowUpdateMessage = createUpdateMock(untrack + " " + stackoverflowLink);
        githubUpdateMessage = createUpdateMock(untrack + " " + githubLink);
        unidentifiedUpdateMessage = createUpdateMock(untrack + " " + "https://en.wikipedia.org/wiki/42_(number)");

    }

    @Test
    void untrackName() {
        Assertions.assertEquals(untrack, untrackCommand.getCommandName());
    }

    @Test
    void untrackExistingLink() {
        SendMessage answer = untrackCommand.handle(stackoverflowUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(untrackCorrect, answer.getParameters().get("text"));
        Assertions.assertEquals(1, storage.getAllLinksById(chatID).size());
        answer = untrackCommand.handle(githubUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(untrackCorrect, answer.getParameters().get("text"));
        Assertions.assertEquals(0, storage.getAllLinksById(chatID).size());
        answer = untrackCommand.handle(githubUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(untrackNotExisting, answer.getParameters().get("text"));
        Assertions.assertEquals(0, storage.getAllLinksById(chatID).size());
    }

    @Test
    void testNotExisting() {
        SendMessage answer = untrackCommand.handle(unidentifiedUpdateMessage);
        Assertions.assertEquals(chatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(untrackIncorrect, answer.getParameters().get("text"));
        Assertions.assertEquals(2, storage.getAllLinksById(chatID).size());
    }
}

