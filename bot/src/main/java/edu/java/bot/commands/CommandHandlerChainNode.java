package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class CommandHandlerChainNode {
    Command command;
    CommandHandlerChainNode next;

    public CommandHandlerChainNode(Command command) {
        this.command = command;
    }

    public CommandHandlerChainNode setNext(CommandHandlerChainNode next) {
        this.next = next;
        return this.next;
    }

    public CommandHandlerChainNode setNext(Command next) {
        return setNext(new CommandHandlerChainNode(next));
    }

    public SendMessage handle(Update update) {
        if (update.message().text().startsWith(command.getCommandName())) {
            return command.handle(update);
        } else if (next != null) {
            return next.handle(update);
        }
        return new SendMessage(update.message().chat().id(), "некорректная команда");
    }
}
