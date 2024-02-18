package edu.java.bot.commands;

import com.pengrad.telegrambot.model.MenuButton;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StartCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger("start-command");

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public String getCommandDescription() {
        return "Начало работы с ботом";
    }

    @Override
    public SendMessage handle(Update update) {
        SendMessage message = new SendMessage(
            update.message().chat().id(),
            "Пользователь зарегистрирован"
        );
//        MenuButton menuButton = new MenuButton("msenu");
        MenuButton menuButton = new MenuButton("menu");
// Добавляем кнопки в меню
//        ;s
// Устанавливаем текст сообщения
        String text = "Выберите команду:";

        return message;
    }
}
