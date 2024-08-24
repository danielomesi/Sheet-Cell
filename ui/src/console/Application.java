package console;

import console.menu.Menu;

//to do:
//add a name when presenting a version (needed to add to a sheet)
//think wether a new sheet loaded should have numOfCellsChanged = 0 ?
//make dto an interface and make the core implement it?
//make it a map from coordinates to cells instead of
//make sure that I am making a validating check on the XML I get according to the requests
//make the print sheet function to present every row index as 2 digits, for example - 01 instead of 1
//add menu
//add versions handling
//refactor
//try to divide the code to more functions, make it more readable
//think about encapsulation - working with types
//C:\\Users\\omesi\\Desktop\\examples\\basic.xml

public class Application {

    public static void main(String[] args) {
         Menu.start();
    }
}
