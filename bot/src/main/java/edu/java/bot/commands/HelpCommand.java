package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements Command {
    private final String info;

    public HelpCommand(List<Command> otherCommand) {
        StringBuilder builder = new StringBuilder(
            "Приложение для отслеживания обновлений контента по ссылкам. При "
            + "появлении новых событий отправляется уведомление в Telegram.\n");
        List<Command> commands = new ArrayList<>();
        commands.addAll(otherCommand);
        commands.add(this);
        for (Command command : commands) {
            builder.append("Команда ")
                   .append(command.getCommandName())
                   .append("\n")
                   .append("        ")
                   .append(command.getCommandDescription())
                   .append("\n");
        }
        info = builder.toString();

    }

    @Override public String getCommandName() {
        return "/help";
    }

    @Override public String getCommandDescription() {
        return "Информация о командах и боте";
    }

    @Override public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), info);
    }
}
