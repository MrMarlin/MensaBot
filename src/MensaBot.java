import com.vdurmont.emoji.EmojiParser;
import menu.MenuplanCrawler;
import menu.MenuplanList;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.toIntExact;

public class MensaBot extends TelegramLongPollingBot {

    private int mensaCounter;
    private int bistroCounter;
    private int externCounter;
    private int selfCounter;
    private int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

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
        MenuplanList mensaMenuplanList = MenuplanCrawler.crawlMenuplans("Mensa");
        MenuplanList forschungszentrumMenuplanList = MenuplanCrawler.crawlMenuplans("Bistro");

        if (weekday > 5) {
            weekday = 2;
        }

        for (int i = 0; i < 3; i++) {
            message_text += "Mensa: " + mensaMenuplanList.get(weekday - 2).get(i).getTitle() + "\n" +
                    mensaMenuplanList.get(weekday - 2).get(i).getDescription() + "\n\n";
        }
        String divider = "";
        for (int i = 0; i < 17; i++) {
            divider += ":fork_knife_plate: ";
        }
        String emojiDivider = EmojiParser.parseToUnicode(divider);

        message_text += emojiDivider + "\n";

        message_text += "\n";
        for (int i = 0; i < 2; i++) {
            message_text += "Bistro: " + forschungszentrumMenuplanList.get(weekday - 2).get(i).getTitle() + "\n" +
                    forschungszentrumMenuplanList.get(weekday - 2).get(i).getDescription() + "\n";
        }

        return message_text;
    }

    private String createCallbackMessage(String call_data) {
        String answer = "Du hast für " + call_data + " abgestimmt.\n\n";

        MenuplanList mensaMenuplanList = MenuplanCrawler.crawlMenuplans("Mensa");
        MenuplanList forschungszentrumMenuplanList = MenuplanCrawler.crawlMenuplans("Bistro");

        if (weekday > 5) {
            weekday = 2;
        }
        System.out.println(weekday + "" + mensaMenuplanList.size() + "" + forschungszentrumMenuplanList.size());
        for (int i = 0; i < 3; i++) {
            answer += "Mensa: " + mensaMenuplanList.get(weekday - 2).get(i).getTitle() + "\n" +
                    mensaMenuplanList.get(weekday - 2).get(i).getDescription() + "\n" + "\n";
        }
        answer += "\n";
        for (int i = 0; i < 2; i++) {
            answer += "Bistro: " + forschungszentrumMenuplanList.get(weekday - 2).get(i).getTitle() + "\n" +
                    forschungszentrumMenuplanList.get(weekday - 2).get(i).getDescription() + "\n";
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

        answer += "\nMensa: " + "\t" + mensaEmojis + "\nBistro: " + "\t" + bistroEmojis + "\nExtern: " + "\t" + exterbEmojis +
                "\nSelber: \t" + selfEmojis;
        return answer;

        //todo if user voted, disable keyboard markup

        //todo disable /start command if bot is once started
    }

    private String createEmojiString(int counter) {
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
