package console;

import console.menu.Menu;

//to do:
//make sure that I am making a validating check on the XML I get according to the requests
//make the print sheet function to present every row index as 2 digits, for example - 01 instead of 1
//add menu
//add versions handling
//refactor
//try to divide the code to more functions, make it more readable
//think about encapsulation - working with types
//C:\\Users\\omesi\\Desktop\\examples\\insurance.xml

public class Application {

    public static void main(String[] args) throws CloneNotSupportedException {
         Menu.start();
    }
}
