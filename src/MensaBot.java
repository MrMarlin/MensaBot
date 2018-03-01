import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import static java.lang.Math.toIntExact;

public class MensaBot extends TelegramLongPollingBot {

    Util util = new Util();
    Voting voting = new Voting();

    public void onUpdateReceived(Update update) {

        Util util = new Util();
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage()
                    .setReplyMarkup(util.generateKeyboardMarkup())
                    .setChatId(chat_id)
                    .setText(createPollMessage())
                    .setParseMode("HTML");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            int userId = update.getCallbackQuery().getFrom().getId();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            String answer = createCallbackMessage(update.getCallbackQuery().getData(), userId);

            EditMessageText new_message = new EditMessageText()
                    .setChatId(chat_id)
                    .setMessageId(toIntExact(message_id))
                    .setText(answer)
                    .setReplyMarkup(util.generateKeyboardMarkup())
                    .setParseMode("HTML");
            try {
                execute(new_message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Hsr_MensaBot";
    }

    @Override
    public String getBotToken() {
        return "Enter bot token here";
    }

    private String createPollMessage() {
        Util util = new Util();
        String message_text = "";
        message_text += util.generateMenuText();
        return message_text;
    }

    private String createCallbackMessage(String call_data, long userId) {
        String answer = util.generateMenuText();
        System.out.println(userId);
        String result = voting.voteHandler(userId, call_data);
        System.out.println(result);
        return answer + result;
    }

}
