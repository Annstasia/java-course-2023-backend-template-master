package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.db.StorageManager;
import edu.java.bot.links.classes.URLInfo;
import edu.java.bot.links.links_comparators.ComparatorByType;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListCommand implements Command {
    private final static Logger LOGGER = LogManager.getLogger("LIST COMMAND");
    private final StorageManager storage;

    public ListCommand(StorageManager storage) {
        this.storage = storage;
    }

    @Override
    public String getCommandName() {
        return "/list";
    }

    @Override
    public String getCommandDescription() {
        return "Получить все добавленные в мониторинг ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        List<URLInfo> allLinksById = storage.getAllLinksById(chatId);
        if (allLinksById == null || allLinksById.isEmpty()) {
            return new SendMessage(chatId, "Нет добавленных ссылок");
        }
        allLinksById.sort(new ComparatorByType());
        StringBuilder builder = new StringBuilder();
        Class<? extends URLInfo> previousLinkClass = null;
        for (URLInfo urlInfo : allLinksById) {
            LOGGER.info(previousLinkClass + " " + urlInfo.getClass());
            if (!urlInfo.getClass().equals(previousLinkClass)) {
                if (previousLinkClass != null) {
                    builder.append("\n");
                }
                previousLinkClass = urlInfo.getClass();
                builder.append("Ссылки из ")
                       .append(urlInfo.getResourceType())
                       .append(":\n");
            }
            builder.append(urlInfo)
                   .append("\n");
        }
        return new SendMessage(chatId, builder.toString());
    }
}
