package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;
import edu.java.bot.dialogs.UntrackDialog;
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
        storage.addDialog(update.message().chat().id(), new UntrackDialog(storage));
        return new SendMessage(update.message().chat().id(), "Введите ссылку");
    }
}
