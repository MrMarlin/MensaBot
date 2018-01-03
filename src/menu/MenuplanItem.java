package menu;

import java.io.Serializable;

public class MenuplanItem implements Serializable {

    private static final long serialVersionUID = 7971992559958239790L;

    private String title;
    private String description;

    public MenuplanItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


}