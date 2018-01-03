package menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menuplan implements Serializable {

    private static final long serialVersionUID = -894564355751702594L;

    private String date;
    private String location;
    private List<MenuplanItem> menuplan = new ArrayList<>();

        public Menuplan(String date, MenuplanItem item1,MenuplanItem item2, MenuplanItem item3){
        this.date=date;
        menuplan.add(item1);
        menuplan.add(item2);
        menuplan.add(item3);
    }

    public MenuplanItem get(int i){
        return menuplan.get(i);
    }

    public String getDate(){
            return date;
    }
}
