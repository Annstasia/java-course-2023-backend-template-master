import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.ScrapperBot;
import edu.java.bot.ScrapperBotUpdatesListener;
import edu.java.bot.commands.CommandHandler;
import edu.java.bot.commands.CommandHandlerChain;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;


public class TestBot {
    private final static String STACKOVERFLOW_LINK = "https://stackoverflow.com/users/2387041/fool";
    private final static String GITHUB_LINK = "https://github.com/Annstasia";
    private final static String GITHUB_SOURCE = "GitHub";
    private final static String STACKOVERFLOW_SOURCE = "StackOverflow";
    private final static String REGISTER_RESPONSE = "Пользователь зарегистрирован";
    private final static String NO_LINKS_RESPONSE = "Нет добавленных ссылок";
    private final static String TRACK_RESPONSE = "Ссылка добавлена в наблюдение";
    private final static String UNTRACK_RESPONSE = "Ссылка убрана из наблюдения";
    private final static String HELP_RESPONSE =         """
        Приложение для отслеживания обновлений контента по ссылкам. При появлении новых событий отправляется уведомление в Telegram.
        Команда /start
                Начало работы с ботом
        Команда /track
                Добавить ссылку в наблюдение
        Команда /untrack
                Убрать ссылку из наблюдения
        Команда /list
                Получить все добавленные в мониторинг ссылки
        Команда /help
                Информация о командах и боте
                """;



    record Pair(long chatId, String text) {
    }

    @Test
    void testOneUserBot() {
        CommandHandler handler = new CommandHandlerChain();
        TelegramBot telegramBot =
            Mockito.spy(new ScrapperBot(new ApplicationConfig("token"), handler));
        ScrapperBotUpdatesListener updatesListener =
            new ScrapperBotUpdatesListener(telegramBot, handler);


        List<Update> updates = Stream.of(
            "/start",
            "/list",
            "/track " + GITHUB_LINK
            ,
            "/list"
            ,
            "/untrack " +GITHUB_LINK,
            "/list"
            ,
            "/help"
            ).map(BaseUpdate::update).toList();
        updatesListener.process(updates);
        InOrder inOrder = Mockito.inOrder(telegramBot);
        Stream.of(
            REGISTER_RESPONSE,
            NO_LINKS_RESPONSE,
            TRACK_RESPONSE
            ,
            "Ссылки из " + GITHUB_SOURCE + ":\n" +GITHUB_LINK + "\n"
            ,
            UNTRACK_RESPONSE,
            NO_LINKS_RESPONSE
            ,

            HELP_RESPONSE
        ).forEach(response -> inOrder.verify(telegramBot).execute(Mockito.argThat(arg -> arg.getParameters().get(
            "text").equals(response))));

    }


    @Test
    void testTwoUserBot() {
        CommandHandler handler = Mockito.spy(new CommandHandlerChain());

        TelegramBot telegramBot =
            Mockito.spy(new ScrapperBot(new ApplicationConfig("token2"), handler));
         ScrapperBotUpdatesListener updatesListener = new ScrapperBotUpdatesListener(telegramBot, handler);


        List<Update> updates = Stream.of(
            new Pair(1, "/start"),
            new Pair(2, "/start"),
            new Pair(1, "/track " + GITHUB_LINK),
            new Pair(2, "/track " + STACKOVERFLOW_LINK),
            new Pair(2, "/list"),
            new Pair(1, "/list")
            ).map(pair -> BaseUpdate.update(pair.chatId(), pair.text())).toList();
        updatesListener.process(updates);
        InOrder inOrder = Mockito.inOrder(telegramBot);
        Stream.of(
            new Pair(1, REGISTER_RESPONSE),
            new Pair(2, REGISTER_RESPONSE),
            new Pair(1, TRACK_RESPONSE),
            new Pair(2, TRACK_RESPONSE),
            new Pair(2, "Ссылки из " + STACKOVERFLOW_SOURCE + ":\n" + STACKOVERFLOW_LINK + "\n"),
            new Pair(1, "Ссылки из " + GITHUB_SOURCE + ":\n" + GITHUB_LINK + "\n")
            ).forEach(pair -> inOrder.verify(telegramBot).execute(
                Mockito.argThat(
                    arg -> arg.getParameters().get("chat_id").equals(pair.chatId())
                           && arg.getParameters()
                                 .get("text").equals(pair.text()))));

    }
}
