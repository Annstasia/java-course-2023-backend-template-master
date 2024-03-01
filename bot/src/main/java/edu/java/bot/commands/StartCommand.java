package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand implements Command {
    @Override public String getCommandName() {
        return "/start";
    }

    @Override public String getCommandDescription() {
        return "Начало работы с ботом";
    }

    @Override public SendMessage handle(Update update) {

        return new SendMessage(update.message().chat().id(), "Пользователь зарегистрирован");
    }
}
