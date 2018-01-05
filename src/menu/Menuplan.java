package menu;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Menuplan implements Serializable {

    private static final long serialVersionUID = -894564355751702594L;

    private List<MenuplanItem> menuplan = new ArrayList<>();

        public Menuplan(MenuplanItem item1,MenuplanItem item2, MenuplanItem item3){
        menuplan.add(item1);
        menuplan.add(item2);
        menuplan.add(item3);
    }

    public Menuplan(MenuplanItem item1, MenuplanItem item2){
            menuplan.add(item1);
            menuplan.add(item2);
    }

    public MenuplanItem get(int i){
        return menuplan.get(i);
    }


}
