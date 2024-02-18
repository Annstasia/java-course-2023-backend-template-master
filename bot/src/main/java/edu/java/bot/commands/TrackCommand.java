package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;
import edu.java.bot.links.classes.URLInfo;
import edu.java.bot.links.parsers.LinkParserChain;
import edu.java.bot.links.parsers.URLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrackCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger("TRACK COMMAND");
    private final static URLParser URL_PARSER = new LinkParserChain();
    private final StorageManager storage;
    private final static String NOT_PARSED = "Ссылка не распознана";
    private final static String CORRECT_ADD = "Ссылка добавлена в наблюдение";
    private final static String UNHANDLED_PARSE_ERROR =
        "При добавлении что-то пошло не так";

    public TrackCommand(StorageManager storage) {
        this.storage = storage;
    }

    @Override public String getCommandName() {
        return "/track";
    }

    @Override public String getCommandDescription() {
        return "Добавить ссылку в наблюдение";
    }

    @Override public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String answer;
        URLInfo info;
        if ((update.message().text().length()
             < getCommandName().length() + 1)) {
            answer = NOT_PARSED;
        } else {
            info = URL_PARSER.parseLink(update.message().text().substring(
                getCommandName().length() + 1));
            if (info == null) {
                answer = NOT_PARSED;
            } else {
                boolean correctAdd = storage.addLink(chatId, info);
                if (correctAdd) {
                    answer = CORRECT_ADD;
                } else {
                    answer = UNHANDLED_PARSE_ERROR;
                    LOGGER.error(
                        "не добавилась ссылка. chatId: " + chatId
                        + " ; ссылка: "
                        + update.message().text());
                }

            }
        }
        return new SendMessage(update.message().chat().id(), answer);
    }
}
