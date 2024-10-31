package constants;

public class Constants {

    // global constants in the app
    public static final String NON_EXISTING_CELL_NAME = "[EMPTY-CELL]";
    public static final String NO_LAST_EDITOR_FOUND_MESSAGE = "No Last Editor Found";
    public final static double REFRESH_RATE_IN_SECONDS = 0.5;

    //text messages in app
    public static final String NEW_VERSION_DETECTED_MESSAGE = "New Version Detected! Writing is disabled, click Sync to update";


    public static String USER_WELCOME_MESSAGE(String username) {return String.format("Hi %s! Welcome to the app!",username);}

    // FXML locations
    public static final String LOGIN_FXML = "/gui/scenes/login/login.fxml";
    public static final String DASHBOARD_MAIN_FXML = "/gui/scenes/dashboard/main/main.fxml";
    public static final String DASHBOARD_HEADER_FXML = "/gui/scenes/dashboard/header/header.fxml";
    public static final String DASHBOARD_SHEETS_TABLE_FXML = "/gui/scenes/dashboard/sheetsTable/sheetsTable.fxml";
    public static final String DASHBOARD_PERMISSIONS_TABLE_FXML = "/gui/scenes/dashboard/permissionsTable/permissionsTable.fxml";
    public static final String WORKSPACE_MAIN_FXML = "/gui/scenes/workspace/main/main.fxml";
    public static final String WORKSPACE_HEADER_FXML = "/gui/scenes/workspace/header/header.fxml";
    public static final String WORKSPACE_SHEET_FXML = "/gui/scenes/workspace/sheet/sheet.fxml";
    public static final String WORKSPACE_COMMANDS_FXML = "/gui/scenes/workspace/commands/commands.fxml";
    public static final String WORKSPACE_APPEARANCE_FXML = "/gui/scenes/workspace/appearance/appearance.fxml";
    public static final String WORKSPACE_SORT_FXML = "/gui/scenes/workspace/sort/sort.fxml";
    public static final String WORKSPACE_FILTER_FXML = "/gui/scenes/workspace/filter/filter.fxml";
    public static final String WORKSPACE_ANALYZE_FXML = "/gui/scenes/workspace/analyze/analyze.fxml";

    //Style Names
    public static final String GENERIC_STYLE = "Generic Style";
    public static final String DEFAULT_STYLE = "Default";
    public static final String DARK_STYLE = "Dark";
    public static final String MACCABI_STYLE = "Maccabi";

    //paths to css files
    public static final String GENERIC_STYLE_CSS_PATH = "/gui/scenes/workspace/styles/Generic Style.css";
    public static final String DEFAULT_STYLE_CSS_PATH = "/gui/scenes/workspace/styles/Default.css";
    public static final String DARK_STYLE_CSS_PATH = "/gui/scenes/workspace/styles/Dark.css";
    public static final String MACCABI_STYLE_CSS_PATH = "/gui/scenes/workspace/styles/Maccabi.css";

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
    public final static String CELL_UPDATE_PREVIEW = FULL_SERVER_PATH + "/update/preview";
}