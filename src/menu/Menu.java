package menu;

import javax.print.DocFlavor;
import java.io.*;
import java.util.ArrayList;

public class Menu {
    private ArrayList<String> menu;

    public Menu() {
        this.menu = new ArrayList<>();
        readMenu();
    }

    public ArrayList<String> readMenu(){
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(
                new FileInputStream("D:\\Users\\sebas\\Documents\\GithubProjects\\MensaBot\\rs\\menu.txt"),"ISO-8859-1"))) {
            String value = fileReader.readLine();
            while (value != null) {
                System.out.println(value);
                System.out.println(menu);
                this.menu.add(value);
                value = fileReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menu;
    }
    public ArrayList<String> getMenu() {
        return menu;
    }


}
