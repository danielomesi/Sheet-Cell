package gui.scenes.login;

import gui.core.ClientApp;
import http.HttpClientMessenger;
import http.MyResponseHandler;
import http.constants.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

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

        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorLabel.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                HttpClientMessenger.genericOnResponseHandler(new MyResponseHandler() {
                         @Override
                         public void handle(String body) {
                             try {
                                 switchSceneToDashboard(userName);
                             } catch (Exception ignored) {}
                         }
                     },
                        response, errorLabel
                );
            }
        });
    }

    private void switchSceneToDashboard(String username) throws IOException {
        clientApp.switchSceneToDashboard(username);
    }

}

