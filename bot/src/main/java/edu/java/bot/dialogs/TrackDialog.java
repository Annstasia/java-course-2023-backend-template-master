package edu.java.bot.dialogs;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;
import edu.java.bot.links.classes.URLInfo;
import edu.java.bot.links.parsers.LinkParserChain;
import edu.java.bot.links.parsers.URLParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackDialog extends Dialog {
    private final static URLParser URL_PARSER = new LinkParserChain();
    private final static String NOT_PARSED = "Ссылка не распознана";
    private final static String CORRECT_ADD = "Ссылка добавлена в наблюдение";
    private final static String UNHANDLED_PARSE_ERROR =
        "При добавлении что-то пошло не так";

    public TrackDialog(StorageManager storage) {
        super(storage);
    }

    @Override
    public SendMessage handle(Update update) {
        String answer;
        URLInfo info;
        info = URL_PARSER.parseLink(update.message().text().strip());
        Long chatId = update.message().chat().id();
        if (info == null) {
            answer = NOT_PARSED;
        } else {
            boolean correctAdd = storage.addLink(chatId, info);
            if (correctAdd) {
                answer = CORRECT_ADD;
            } else {
                answer = UNHANDLED_PARSE_ERROR;
                log.error(
                    "не добавилась ссылка. chatId: " + chatId
                    + " ; ссылка: "
                    + update.message().text());
            }
        }
        finish(chatId);
        return new SendMessage(chatId, answer);
    }
}
