package menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MenuplanCrawler {

    private final static String MENSA_MENUPLAN_URL = "http://hochschule-rapperswil.sv-restaurant.ch/de/menuplan/mensa/";
    private final static String BISTRO_MENUPLAN_URL = "http://hochschule-rapperswil.sv-restaurant.ch/de/menuplan/forschungszentrum/";

    private final static String HTML_ELEMENT_MENUPLAN_DATE = "span.date";
    private final static String HTML_ELEMENT_MENU_DESCRIPTION = "p.menu-description";
    private final static String HTML_ELEMENT_MENU_TITLE = "h2.menu-title";

    private final static int MENSA = 1;
    private final static int BISTRO = 2;


    public static MenuplanList crawlMenuplans(String websiteToCrawl) {
        String location;
        if (websiteToCrawl.equals("Mensa")) {
            location = MENSA_MENUPLAN_URL;
        } else if (websiteToCrawl.equals("Bistro")) {
            location = BISTRO_MENUPLAN_URL;
        } else {
            throw new IllegalArgumentException();
        }

        List<String> menuTitles = new ArrayList<>();
        List<String> menuDescriptions = new ArrayList<>();
        List<String> menuDates = new ArrayList<>();
        MenuplanList menuplans = new MenuplanList();

        Document website = null;

        try {
            website = Jsoup.connect(location).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements titles = website.select(HTML_ELEMENT_MENU_TITLE);
        for (Element title : titles) {
            if (checkForNotAvailable(title.text())) {
                menuTitles.add("Not Available");
            } else {
                if (websiteToCrawl.equals("Mensa")) {
                    menuTitles.add(title.text());
                } else {
                    if (!(title.nextElementSibling()
                            .nextElementSibling()
                            .nextElementSibling()
                            .select("div.menu-prices").hasText())) {
                        menuTitles.add(title.text());
                    }
                }
            }
        }
        Elements descriptions = website.select(HTML_ELEMENT_MENU_DESCRIPTION);
        for (Element description : descriptions) {
            if (websiteToCrawl.equals("Mensa")) {
                menuDescriptions.add(replaceLineBreak(description, MENSA));
            } else {
                if (!(description.nextElementSibling().select("div.menu-prices").hasText())) {
                    menuDescriptions.add(replaceLineBreak(description, BISTRO));
                }
            }
        }
        Elements dates = website.select(HTML_ELEMENT_MENUPLAN_DATE);
        for (Element date : dates) {
            menuDates.add(date.text());
        }

        for (int i = 0; i < menuDates.size(); i++) {
            if (websiteToCrawl.equals("Mensa")) {
                menuplans.add(new Menuplan(
                        new MenuplanItem(menuTitles.get(i * 3), menuDescriptions.get(i * 3)),
                        new MenuplanItem(menuTitles.get((i * 3) + 1), menuDescriptions.get((i * 3) + 1)),
                        new MenuplanItem(menuTitles.get((i * 3) + 2), menuDescriptions.get((i * 3) + 2))));
            } else {
                menuplans.add(new Menuplan(
                        new MenuplanItem(menuTitles.get(i * 2), menuDescriptions.get(i * 2)),
                        new MenuplanItem(menuTitles.get((i * 2) + 1), menuDescriptions.get((i * 2) + 1))));
            }
        }
        return menuplans;
    }

    private static String replaceLineBreak(Element htmlToParse, int selector) {
        if (selector == MENSA) {
            return htmlToParse.html()
                    .replaceAll("<br> ", "\n")
                    .replaceAll("&amp;", "&");
        } else if (selector == BISTRO) {
            return htmlToParse.html()
                    .replaceAll("<br> ", "\n")
                    .replaceAll("&amp;", "&")
                    .replaceAll("\n.*\\..*\\..*\n.*\\..*\\..*", "");
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static boolean checkForNotAvailable(String menuTitle) {
        String patternString = "Für diesen Betrieb.*";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(menuTitle);
        boolean matches = matcher.matches();
        return matches;
    }

}