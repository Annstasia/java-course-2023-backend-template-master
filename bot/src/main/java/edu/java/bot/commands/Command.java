package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String getCommandName();

    String getCommandDescription();

    default BotCommand asBotCommand() {
        return new BotCommand(getCommandName(), getCommandDescription());
    }

    SendMessage handle(Update update);
}
