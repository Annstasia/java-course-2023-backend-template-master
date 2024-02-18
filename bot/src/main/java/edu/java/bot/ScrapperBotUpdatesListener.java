package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.commands.CommandHandler;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScrapperBotUpdatesListener implements UpdatesListener {
    TelegramBot telegramBot;
    CommandHandler commandHandler;

    public ScrapperBotUpdatesListener(TelegramBot bot, CommandHandler handler) {
        this.telegramBot = bot;
        telegramBot.setUpdatesListener(this);
        this.commandHandler = handler;

    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            process(update);
        }
        return CONFIRMED_UPDATES_ALL;
    }

    private void process(Update update) {
        if (update.message().text().startsWith("/")) {
            SendMessage sendMessage = commandHandler.handle(update);
            SendResponse response = telegramBot.execute(sendMessage);
        } else {
            telegramBot.execute(new SendMessage(
                update.message().chat().id(),
                "我不明白 (я вас не понимаю...)"
            ));
        }
    }

    public void close() {
        telegramBot.removeGetUpdatesListener();
    }
}

