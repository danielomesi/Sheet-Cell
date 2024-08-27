package console.app;

import console.menu.Menu;

//Daniel Omesi, 207689092
public class Application {

    public static void main(String[] args) {
        System.out.println("Java Version: " + System.getProperty("java.version"));
         Menu.start();
    }
}
