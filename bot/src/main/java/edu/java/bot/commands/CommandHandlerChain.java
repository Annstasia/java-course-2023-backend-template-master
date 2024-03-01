package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.FictiveStorageManager;
import edu.java.bot.db.StorageManager;
import edu.java.bot.dialogs.Dialog;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component public class CommandHandlerChain implements CommandHandler {
    private final static StorageManager STORAGE = new FictiveStorageManager();
    private final static CommandHandlerChainNode CHAIN;

    private final static List<Command> ALL_COMMANDS;

    static {
        List<Command> allCommandsExceptHelp = List.of(
            new StartCommand(),
            new TrackCommand(STORAGE),
            new UntrackCommand(STORAGE),
            new ListCommand(STORAGE)
        );
        ALL_COMMANDS = new ArrayList<>(allCommandsExceptHelp);
        ALL_COMMANDS.add(new HelpCommand(allCommandsExceptHelp));

        CHAIN = new CommandHandlerChainNode(ALL_COMMANDS.get(0));
        CommandHandlerChainNode tail = CHAIN;
        for (int i = 1; i < ALL_COMMANDS.size(); ++i) {
            tail = tail.setNext(ALL_COMMANDS.get(i));
        }
        tail.setNext(new HelpCommand(allCommandsExceptHelp));

    }

    @Override public SendMessage handle(Update update) {
        if (update.message().text().startsWith("/")) {
            return CHAIN.handle(update);
        } else {
            Dialog dialog = STORAGE.getDialog(update.message().chat().id());
            if (dialog == null) {
                return new SendMessage(update.message().chat().id(), "我不明白 (я вас не понимаю...)");
            }
            return dialog.handle(update);
        }
    }

    @Override public List<Command> getAllCommands() {
        return ALL_COMMANDS;
    }
}
