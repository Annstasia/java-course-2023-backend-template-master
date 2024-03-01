package edu.java.bot.dialogs;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;
import edu.java.bot.links.classes.URLInfo;
import edu.java.bot.links.parsers.LinkParserChain;
import edu.java.bot.links.parsers.URLParser;

public class UntrackDialog extends Dialog {
    private final static URLParser URL_PARSER = new LinkParserChain();

    public UntrackDialog(StorageManager storage) {
        super(storage);
    }

    @Override public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String answer;
        URLInfo info = URL_PARSER.parseLink(update.message().text());
        if (info == null) {
            answer = "Ссылка не распознана";
        } else if (storage.removeLink(chatId, info)) {
            answer = "Ссылка убрана из наблюдения";
        } else {
            answer = "Ссылка не найдена среди находящихся на мониторинге";
        }
        finish(chatId);
        return new SendMessage(update.message().chat().id(), answer);
    }

}
