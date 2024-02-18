package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandHandler;
import edu.java.bot.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component @Slf4j public class ScrapperBot extends TelegramBot {
    public ScrapperBot(
        ApplicationConfig config, CommandHandler commandHandler
    ) {
        super(config.telegramToken());
        this.execute(new SetMyCommands(commandHandler.getAllCommands().stream()
                                                     .map(Command::asBotCommand)
                                                     .toList()
                                                     .toArray(new BotCommand[] {})));

    }

}
