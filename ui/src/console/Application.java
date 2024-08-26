package console;

import console.menu.Menu;

//to do:
//test the references cases - im not sure if it works well
//think wether a new sheet loaded should have numOfCellsChanged = 0 ?
//make dto an interface and make the core implement it?
//make it a map from coordinates to cells instead of
//make sure that I am making a validating check on the XML I get according to the requests
//add menu
//add versions handling
//refactor
//try to divide the code to more functions, make it more readable
//think about encapsulation - working with types
//C:\\Users\\omesi\\Desktop\\examples\\insurance
//save/load:
//C:\\Users\\omesi\\Desktop\\examples\\data

public class Application {

    public static void main(String[] args) {
         Menu.start();
    }
}
