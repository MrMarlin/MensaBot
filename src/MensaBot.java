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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.toIntExact;

public class MensaBot extends TelegramLongPollingBot {

    //    private int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    private int weekday = 2;
    private Map<Integer, String> votes = new HashMap<>();
    private Map<String, Integer> voteCounter = new HashMap<>();
    private boolean started = false;

    public void onUpdateReceived(Update update) {
        if (!started) {
            voteCounter.put("Mensa", 0);
            voteCounter.put("Bistro", 0);
            voteCounter.put("Extern", 0);
            voteCounter.put("Selber", 0);
            started = true;
        }
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage()
                    .setReplyMarkup(genearteKeyboardMarkup())
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
                    .setReplyMarkup(genearteKeyboardMarkup())
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
        return //insert BotToken here;
    }

    private String createPollMessage() {
        String message_text = "";
        message_text += this.generateMenuText();
        return message_text;
    }

    private String createCallbackMessage(String call_data, int userId) {
        String answer = this.generateMenuText();

        if (!(votes.containsKey(userId))) {
            if (call_data.equals("Mensa")) {
                voteCounter.put("Mensa", voteCounter.get("Mensa") + 1);
            } else if (call_data.equals("Bistro")) {
                voteCounter.put("Bistro", voteCounter.get("Bistro") + 1);
            } else if (call_data.equals("Extern")) {
                voteCounter.put("Extern", voteCounter.get("Extern") + 1);
            } else if (call_data.equals("Selber")) {
                voteCounter.put("Selber", voteCounter.get("Selber") + 1);
            }
            votes.put(userId, call_data);
        } else if (!(votes.get(userId).equals(call_data))) {
            if (voteCounter.get(votes.get(userId)) > 0) {
                voteCounter.put(votes.get(userId), voteCounter.get(votes.get(userId)) - 1);
            }
            votes.put(userId, call_data);
            voteCounter.put(votes.get(userId), voteCounter.get(votes.get(userId)) + 1);

        } else {
            if (voteCounter.get(votes.get(userId)) > 0) {
                voteCounter.put(votes.get(userId), voteCounter.get(votes.get(userId)) - 1);
            } else {
                voteCounter.put(votes.get(userId), voteCounter.get(votes.get(userId)) + 1);
            }
        }


        answer += "\nMensa: " + "\t" + generateEmojiString(voteCounter.get("Mensa")) +
                "\nBistro: " + generateEmojiString(voteCounter.get("Bistro")) +
                "\nExtern: " + generateEmojiString(voteCounter.get("Extern")) +
                "\nSelber: " + generateEmojiString(voteCounter.get("Selber"));

        return answer;
    }

    private String generateEmojiString(int counter) {
        String emojis = "";
        for (int i = 0; i < counter; i++) {
            emojis += EmojiParser.parseToUnicode(":point_up:");
        }
        return emojis;
    }

    private InlineKeyboardMarkup genearteKeyboardMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();

        rowInline.add(new InlineKeyboardButton().setText("Mensa").setCallbackData("Mensa"));
        rowInline.add(new InlineKeyboardButton().setText("Bistro").setCallbackData("Bistro"));
        rowInline1.add(new InlineKeyboardButton().setText("Extern").setCallbackData("Extern"));
        rowInline1.add(new InlineKeyboardButton().setText("Selber mitgnoh..").setCallbackData("Selber"));

        rowsInline.add(rowInline);
        rowsInline.add(rowInline1);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    private String generateEmojiDivider() {
        String divider = "";
        for (int i = 0; i < 17; i++) {
            divider += ":fork_knife_plate: ";
        }
        return EmojiParser.parseToUnicode(divider);
    }

    private String generateMenuText() {
        MenuplanList mensaMenuplanList = MenuplanCrawler.crawlMenuplans("Mensa");
        MenuplanList bistroMenuplanList = MenuplanCrawler.crawlMenuplans("Bistro");
        String resultString = "";

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        resultString += "<b>" + dateFormat.format(cal.getTime()) + "</b>\n\n";

        for (int i = 0; i < 3; i++) {
            resultString += "<b>Mensa: </b>\n" + mensaMenuplanList.get(weekday - 2).get(i).getTitle() + "\n" +
                    mensaMenuplanList.get(weekday - 2).get(i).getDescription() + "\n\n";
        }

        resultString += generateEmojiDivider() + "\n";

        resultString += "\n";
        for (int i = 0; i < 2; i++) {
            resultString += "<b>Bistro: </b>\n" + bistroMenuplanList.get(weekday - 2).get(i).getTitle() + "\n" +
                    bistroMenuplanList.get(weekday - 2).get(i).getDescription() + "\n";
        }
        resultString += "<b>------Voting------</b>";
        resultString += "\nMensa: " + "\t" + "\nBistro: " + "\nExtern: " + "\nSelber: ";

        return resultString;
    }
}
