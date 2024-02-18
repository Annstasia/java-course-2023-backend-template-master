package edu.java.bot.dialogs;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;

public abstract class Dialog {
    protected final StorageManager storage;

    protected Dialog(StorageManager storage) {
        this.storage = storage;
    }

    public abstract SendMessage handle(Update update);

    public void finish(long id) {
        storage.removeDialog(id);
    }
}
