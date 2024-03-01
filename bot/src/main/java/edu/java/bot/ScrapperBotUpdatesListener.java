package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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
        SendMessage sendMessage = commandHandler.handle(update);
        telegramBot.execute(sendMessage);
    }
}

