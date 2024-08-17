package console;

import engine.Engine;
import engine.EngineImpl;

//to do:
//fix layout thing of spacing - i need to retrieve from STL data the proper width of collums and rows
//add menu
//add versions handling
//refactor
//try to divide the code to more functions, make it more readable
//think about encapsulation - working with types
//
public class Application {

    public static void main(String[] args) throws CloneNotSupportedException {
        Engine engine = new EngineImpl();

        try {
            engine.loadSheetFromXMLFile("C:\\Users\\omesi\\Desktop\\examples\\insurance.xml");
            ConsoleUtils.printSheet(engine.getSheet());
//            System.out.println("Before: ");
//            ConsoleUtils.printSheet(engine.getSheet());
//            engine.updateSpecificCell("E7", "{TIMES,800005,  6.3}");
//            System.out.println("After: ");
//            ConsoleUtils.printSheet(engine.getSheet());

        }
        catch (Exception e) {
            ConsoleUtils.printExceptionInfo(e);
            e.printStackTrace();
        }
    }
}
