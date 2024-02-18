import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.utility.BotUtils;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.db.FictiveStorageManager;
import edu.java.bot.db.StorageManager;
import edu.java.bot.dialogs.TrackDialog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestList {
    private final static String untrackCorrect = "Ссылка убрана из наблюдения";
    private final static String untrackIncorrect = "Ссылка не распознана";
    private final static String untrackNotExisting = "Ссылка не найдена среди находящихся на мониторинге";
    StorageManager storage;
    ListCommand listCommand;

    Update listUpdate1;
    Update listUpdate2;
    String stackoverflowLink = "https://stackoverflow.com/users/2387041/fool";
    String githubLink = "https://github.com/Annstasia";
    long filledChatID = 1L;
    long emptyChatID = 2L;
    String list = "/list";

    Update createUpdateMock(long chatID, String query) {
        return BotUtils.parseUpdate("{message:{chat:{id:" + chatID + "},text:\""
                                    + query + "\"}}");
    }

    @BeforeEach
    void reset() {
        storage = new FictiveStorageManager();
        TrackCommand trackCommand = new TrackCommand(storage);
        TrackDialog trackDialog = new TrackDialog(storage);
        String track = "/track";

        trackCommand.handle(createUpdateMock(filledChatID, track));
        trackDialog.handle(createUpdateMock(filledChatID, stackoverflowLink));
        trackCommand.handle(createUpdateMock(filledChatID, track));
        trackDialog.handle(createUpdateMock(filledChatID, githubLink));
        listCommand = new ListCommand(storage);
        listUpdate1 = createUpdateMock(filledChatID, list);
        listUpdate2 = createUpdateMock(emptyChatID, list);
    }

    @Test
    void listName() {
        Assertions.assertEquals(list, listCommand.getCommandName());
    }

    @Test
    void existingListTest() {
        SendMessage answer = listCommand.handle(listUpdate1);
        Assertions.assertEquals(filledChatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals(
            "Ссылки из GitHub:\n" + githubLink + "\n\nСсылки из StackOverflow:\n" + stackoverflowLink +
            "\n",
            answer.getParameters().get("text")
        );
    }

    @Test
    void notExistingList() {
        SendMessage answer = listCommand.handle(listUpdate2);
        Assertions.assertEquals(emptyChatID, answer.getParameters().get("chat_id"));
        Assertions.assertEquals("Нет добавленных ссылок", answer.getParameters().get("text"));
    }
}


