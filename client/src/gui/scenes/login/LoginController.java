package gui.scenes.login;

import gui.core.ClientApp;
import http.HttpClientMessenger;
import http.MyCallBack;
import constants.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import okhttp3.HttpUrl;

import java.io.IOException;

public class LoginController {
    private ClientApp clientApp;

    @FXML
    private ScrollPane componentWrapper;

    @FXML
    private Label errorLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextField usernameTextField;

    public void setClientApp(ClientApp clientApp) {this.clientApp = clientApp;}

    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = usernameTextField.getText();
        if (userName.isEmpty()) {
            errorLabel.setText("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(Constants.LOGIN)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, new MyCallBack(errorLabel,
                (body -> {
                    try {
                        switchSceneToDashboard(userName);
                    } catch (Exception ignored) {}
                })));
    }

    private void switchSceneToDashboard(String username) throws IOException {
        clientApp.switchSceneToDashboard(username);
    }

}

