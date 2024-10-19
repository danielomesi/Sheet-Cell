package http;

import javafx.application.Platform;
import javafx.scene.control.Label;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MyCallBack implements Callback {
    private final MyResponseHandler myResponseHandler;
    private final Label label;

    public MyCallBack(Label label, MyResponseHandler myResponseHandler) {
        this.myResponseHandler = myResponseHandler;
        this.label = label;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Platform.runLater(() ->
                label.setText("Something went wrong: " + e.getMessage())
        );
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        HttpClientMessenger.genericOnResponseHandler(myResponseHandler,response,label);
    }
}
