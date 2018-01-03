package menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.attribute.standard.MediaName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MenuplanCrawler {

    //TODO Crawl correct data, current crawler crawls through different days

    private final static String MENSA_MENUPLAN_URL = "http://hochschule-rapperswil.sv-restaurant.ch/de/menuplan/mensa/";
    private final static String FORSCHUNGSZENTRUM_MENUPLAN_URL = "http://hochschule-rapperswil.sv-restaurant.ch/de/menuplan/forschungszentrum/";

    private final static String HTML_ELEMENT_MENUPLAN_DATE = "span.date";
    private final static String HTML_ELEMENT_MENU_DESCRIPTION = "p.menu-description";
    private final static String HTML_ELEMENT_MENU_TITLE = "h2.menu-title";

    public static MenuplanList crawlMenuplans(String locationToCrawl) {
        String location;
        if (locationToCrawl.equals("Mensa")) {
            location = MENSA_MENUPLAN_URL;
        } else if (locationToCrawl.equals("Forschungszentrum")) {
            location = FORSCHUNGSZENTRUM_MENUPLAN_URL;
        } else {
            throw new IllegalArgumentException();
        }
        List<String> menuTitles = new ArrayList<>();
        List<String> menuDescriptions = new ArrayList<>();
        List<String> days = new ArrayList<>();

        MenuplanList menuplans = new MenuplanList();

        Document website = null;

        try {
            website = Jsoup.connect(location).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements titles = website.select(HTML_ELEMENT_MENU_TITLE);
        for (Element title : titles) {
            menuTitles.add(title.text());
        }
        Elements descriptions = website.select(HTML_ELEMENT_MENU_DESCRIPTION);
        for (Element description : descriptions) {
            if (locationToCrawl.equals("Forschungszentrum")) {
                menuDescriptions.add(description.html()
                        .replaceAll("<br> ", "\n")
                        .replaceAll("&amp;", "&")
                        .replaceAll("\n.*\\..*\\..*\n.*\\..*\\..*", ""));
            }
            menuDescriptions.add(description.html()
                    .replaceAll("<br> ", "\n")
                    .replaceAll("&amp;", "&"));
        }
        Elements dates = website.select(HTML_ELEMENT_MENUPLAN_DATE);
        for (Element date : dates) {
            days.add(date.text());
        }

        for (int i = 0; i < days.size(); i++) {
            menuplans.add(new Menuplan(days.get(i),
                    new MenuplanItem(menuTitles.get(i * 3), menuDescriptions.get(i * 3)),
                    new MenuplanItem(menuTitles.get((i * 3) + 1), menuDescriptions.get((i * 3) + 1)),
                    new MenuplanItem(menuTitles.get((i * 3) + 2), menuDescriptions.get((i * 3) + 2))));
        }

        return menuplans;
    }

}