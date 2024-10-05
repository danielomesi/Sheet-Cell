package gui.scenes.login;

import http.HttpClientMessenger;
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

    @FXML
    private ScrollPane componentWrapper;

    @FXML
    private Label errorLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextField usernameTextField;

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
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorLabel.setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        errorLabel.setText("Success!");
                    });
                }
            }
        });
    }

}

