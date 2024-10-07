package users;

import java.util.List;

public class UserData {
    private final String username;
    private List<String> sheetsNames;


    UserData(String username) {
        this.username = username;
    }

    public String getUsername() {return username;}
}
