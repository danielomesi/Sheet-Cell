package gui.scenes.login;

import gui.core.ClientApp;
import gui.utils.Utils;
import http.HttpClientMessenger;
import http.MyCallBack;
import constants.Constants;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import okhttp3.HttpUrl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import static constants.Constants.DEFAULT_STYLE;
import static constants.Constants.GENERIC_STYLE_CSS_PATH;

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

    //setters
    public void setClientApp(ClientApp clientApp) {this.clientApp = clientApp;}

    private void switchSceneToDashboard(String username) throws IOException {
        clientApp.switchSceneToDashboard(username);
    }

    public void initialize() {
        Utils.setStyle(componentWrapper, DEFAULT_STYLE);
    }


    @FXML
    void loginButtonClicked(ActionEvent event) {
        String userName = usernameTextField.getText();
        if (userName.isEmpty()) {
            errorLabel.setText("User name is empty. You can't login with empty user name");
            return;
        }

        progressIndicator.setVisible(true);


            String finalUrl = HttpUrl
                            .parse(Constants.LOGIN)
                            .newBuilder()
                            .addQueryParameter("username", userName)
                            .build()
                            .toString();


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(2000);
                HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, new MyCallBack(errorLabel,
                        (body -> {
                            try {
                                Platform.runLater(()->{errorLabel.setText("Im switch scene");});
                                switchSceneToDashboard(userName);
                            } catch (Exception ignored) {}
                        })));

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                progressIndicator.setVisible(false);
            }

            @Override
            protected void failed() {
                super.failed();
                progressIndicator.setVisible(false);
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                progressIndicator.setVisible(false);
            }
        };

        new Thread(task).start();
    }
}

