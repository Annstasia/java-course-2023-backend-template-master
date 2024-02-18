package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements Command {
    private final String info;

//    private final String info =
//        """
//            Приложение для отслеживания обновлений контента по ссылкам.\s
//            При появлении новых событий отправляется уведомление в Telegram.
//            Команда /start
//            Команда /track [link] добавляется ссылку в мониторинг. \s
//            На данный момент поддерживаются только ссылки из stackoverflow и \s
//            github. Например: /track \s
//            https://stackoverflow.com/search?q=unsupported%20link
//            Команда /untrack [link] убирает ссылку из мониторинга. Например: \s
//            /untrack https://stackoverflow.com/search?q=unsupported%20link
//            Команда /list возвращает все добавленные ссылки
//            Команда /help выводит инструкцию""";

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
