import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class MensaBot extends TelegramLongPollingBot {

    int mensaCounter;
    int bistroCounter;
    int externCounter;
    int selfCounter;

    //    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            long chat_id = update.getMessage().getChatId();


            SendMessage message = new SendMessage();
            message.setReplyMarkup(createKeyboardMarkup());
            message.setChatId(chat_id);
            message.setText(createPollMessage());

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.hasCallbackQuery()) {
            // Set variables
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            String answer = createCallbackMessage(update.getCallbackQuery().getData());

            EditMessageText new_message = new EditMessageText()
                    .setChatId(chat_id)
                    .setMessageId(toIntExact(message_id))
                    .setText(answer)
                    .setReplyMarkup(createKeyboardMarkup());
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
        return "532424566:AAHqQT4xGvq4OFHeJE6crufRA9IFDsXQeZw";
    }

    private String createPollMessage() {
        String message_text = "";
        Menu menu = new Menu();
        for (String s : menu.getMenu()) {
            message_text += "\n" + s;
        }

        return message_text;
    }

    private String createCallbackMessage(String call_data) {
        String answer = "Du hast für " + call_data + " abgestimmt.\n\n";
        Menu menu = new Menu();
        for (String s : menu.getMenu()) {
            answer += s + "\n";
        }

        if (call_data.equals("Mensa")) {
            mensaCounter++;
        } else if (call_data.equals("Bistro")) {
            bistroCounter++;
        } else if (call_data.equals("Extern")) {
            externCounter++;
        } else {
            selfCounter++;
        }

        String mensaEmojis = createEmojiString(mensaCounter);

        String bistroEmojis = createEmojiString(bistroCounter);

        String exterbEmojis = createEmojiString(externCounter);

        String selfEmojis = createEmojiString(selfCounter);

        answer += "\nMensa: "+"\t" + mensaEmojis + "\nBistro: "+"\t" + bistroEmojis + "\nExtern: "+"\t" + exterbEmojis +
                "\nSelber: \t" + selfEmojis;
        return answer;

        //todo if user voted, disable keyboard markup

        //todo disable /start command if bot is once started
    }

    private String createEmojiString(int counter){
        String emojis = "";
        for (int i = 0; i < counter; i++) {
             emojis += EmojiParser.parseToUnicode(":point_up:");
        }
        return emojis;
    }

    private InlineKeyboardMarkup createKeyboardMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();

        rowInline.add(new InlineKeyboardButton().setText("Mensa").setCallbackData("Mensa"));
        rowInline.add(new InlineKeyboardButton().setText("Bistro").setCallbackData("Bistro"));
        rowInline1.add(new InlineKeyboardButton().setText("Extern").setCallbackData("Extern"));
        rowInline1.add(new InlineKeyboardButton().setText("Selber mitgnoh..").setCallbackData("Selber mitgnoh.."));

        rowsInline.add(rowInline);
        rowsInline.add(rowInline1);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}
