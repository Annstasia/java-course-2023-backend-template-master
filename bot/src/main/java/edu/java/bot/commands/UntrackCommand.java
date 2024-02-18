package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;
import edu.java.bot.links.classes.URLInfo;
import edu.java.bot.links.parsers.LinkParserChain;
import edu.java.bot.links.parsers.URLParser;

public class UntrackCommand implements Command {
    private final static URLParser URL_PARSER = new LinkParserChain();
    private final StorageManager storage;

    public UntrackCommand(StorageManager storage) {
        this.storage = storage;
    }

    @Override public String getCommandName() {
        return "/untrack";
    }

    @Override public String getCommandDescription() {
        return "Убрать ссылку из наблюдения";
    }

    @Override public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String answer;
        URLInfo info = URL_PARSER.parseLink(update.message().text().substring(getCommandName().length() + 1));
        if (info == null) {
            answer = "Ссылка не распознана";
        } else if (storage.removeLink(chatId, info)) {
            answer = "Ссылка убрана из наблюдения";
        } else {
            answer = "Ссылка не найдена среди находящихся на мониторинге";
        }
        return new SendMessage(update.message().chat().id(), answer);
    }
}
