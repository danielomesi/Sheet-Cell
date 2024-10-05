package http.constants;

public class Constants {

    // global constants
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations


    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/server";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN = FULL_SERVER_PATH + "/login";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
}