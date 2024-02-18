import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.utility.BotUtils;

public class BaseUpdate {
    public static Update update(long chatID, String messageText) {
        return BotUtils.parseUpdate("{message:{chat:{id:" + chatID + "},text:\""
                                    + messageText + "\"}}");
    }

    public static Update update(String messageText) {
        return update(1, messageText);
    }

}
