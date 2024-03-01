package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;
import edu.java.bot.dialogs.TrackDialog;
import edu.java.bot.links.parsers.LinkParserChain;
import edu.java.bot.links.parsers.URLParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackCommand implements Command {
    private final static URLParser URL_PARSER = new LinkParserChain();
    private final static String NOT_PARSED = "Ссылка не распознана";
    private final static String CORRECT_ADD = "Ссылка добавлена в наблюдение";
    private final static String UNHANDLED_PARSE_ERROR =
        "При добавлении что-то пошло не так";
    private final StorageManager storage;

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
        storage.addDialog(chatId, new TrackDialog(storage));
        return new SendMessage(update.message().chat().id(), "Введите ссылку");
    }
}
