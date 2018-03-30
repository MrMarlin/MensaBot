import Util.MensaBotUtil;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import voting.Voting;

import static java.lang.Math.toIntExact;

public class MensaBot extends TelegramLongPollingBot {

    private MensaBotUtil mensaBotUtil = new MensaBotUtil();
    private Voting voting = new Voting();

    public void onUpdateReceived(Update update) {

        MensaBotUtil mensaBotUtil = new MensaBotUtil();
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage()
                    .setReplyMarkup(mensaBotUtil.generateKeyboardMarkup())
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
                    .setReplyMarkup(mensaBotUtil.generateKeyboardMarkup())
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
        MensaBotUtil mensaBotUtil = new MensaBotUtil();
        String message_text = "";
        message_text += mensaBotUtil.generateMenuText();
        return message_text;
    }

    private String createCallbackMessage(String call_data, long userId) {
        String answer = mensaBotUtil.generateMenuText();
        String result = voting.voteHandler(userId, call_data);
        return answer + result;
    }

}
