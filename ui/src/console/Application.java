package console;

import engine.Engine;
import engine.EngineImpl;

public class Application {

    public static void main(String[] args) throws CloneNotSupportedException {
        Engine engine = new EngineImpl();
        engine.loadSheetFromDummyData();
        try {
            System.out.println("Before: ");
            ConsoleUtils.printSheet(engine.getSheet());
            engine.updateSpecificCell("E7", "{CONCAT,HELLO,  WORLD}");
            System.out.println("After: ");
            ConsoleUtils.printSheet(engine.getSheet());

        }
        catch (Exception e) {
            ConsoleUtils.printExceptionInfo(e);
        }
    }
}
