package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

public interface CommandHandler {
    SendMessage handle(Update update);

    List<Command> getAllCommands();
}
