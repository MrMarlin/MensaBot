package Util;

import com.vdurmont.emoji.EmojiParser;
import menu.MenuplanCrawler;
import menu.MenuplanList;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MensaBotUtil {

    private int weekday = 2;

    public String generateEmojiString(long counter) {
        String emojis = "";
        for (int i = 0; i < counter; i++) {
            emojis += EmojiParser.parseToUnicode(":point_up:");
        }
        return emojis;
    }

    public InlineKeyboardMarkup generateKeyboardMarkup() {
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

    public String generateMenuText() {
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
        if (bistroMenuplanList.size() >= 2) {
            for (int i = 0; i < 2; i++) {
                resultString += "<b>Bistro: </b>\n" + bistroMenuplanList.get(weekday - 2).get(i).getTitle() + "\n" +
                        bistroMenuplanList.get(weekday - 2).get(i).getDescription() + "\n";
            }
        }
        return resultString;
    }
}
