package menu;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Menuplan implements Serializable {

    private static final long serialVersionUID = -894564355751702594L;

//    private String date;
//    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private List<MenuplanItem> menuplan = new ArrayList<>();

        public Menuplan(String date, MenuplanItem item1,MenuplanItem item2, MenuplanItem item3){
//        this.date=date.concat(Integer.toString(year));
//            System.out.println(this.date);
        menuplan.add(item1);
        menuplan.add(item2);
        menuplan.add(item3);
    }

    public Menuplan(String date, MenuplanItem item1, MenuplanItem item2){
            menuplan.add(item1);
            menuplan.add(item2);
    }

    public MenuplanItem get(int i){
        return menuplan.get(i);
    }


}
