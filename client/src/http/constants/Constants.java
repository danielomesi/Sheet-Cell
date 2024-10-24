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
    public final static String GET_SHEET = FULL_SERVER_PATH + "/sheet";
    public final static String GET_CELL_ON_UPDATE = FULL_SERVER_PATH + "/update";
    public final static String RANGE = FULL_SERVER_PATH + "/range";
    public final static String SUB_SHEET = FULL_SERVER_PATH + "/subsheet";
    public final static String SORT = FULL_SERVER_PATH + "/sort";
    public final static String FILTER = FULL_SERVER_PATH + "/filter";
    public final static String DISTINCT_VALUES_OF_COL = FULL_SERVER_PATH + "/filter/distinct";
    public final static String GET_SHEETS_META_DATA = FULL_SERVER_PATH + "/dashboard/sheets";;
    public final static String NUM_OF_VERSIONS = FULL_SERVER_PATH + "/versions";
    public final static String ADD_PERMISSION_REQUEST = FULL_SERVER_PATH + "/permissions/request";
    public final static String DECIDE_PERMISSION_REQUEST = FULL_SERVER_PATH + "/permissions/decide";
}