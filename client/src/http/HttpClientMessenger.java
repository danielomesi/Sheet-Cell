package http;

import javafx.application.Platform;
import javafx.scene.control.Label;
import okhttp3.*;
import okhttp3.JavaNetCookieJar;
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class HttpClientMessenger {
    private static final HttpClientMessenger messenger = new HttpClientMessenger();
    private final OkHttpClient client;
    private static final String SERVER_ADDRESS = "http://localhost:8080/server";

    private HttpClientMessenger() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
    }

    public static HttpClientMessenger getInstance() {return messenger;}

    public static void sendFileToServer(File file,Callback callback) {
        if (file.exists()) {
            try {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(file, MediaType.parse("application/octet-stream")))
                        .build();

                Request request = new Request.Builder()
                        .url(SERVER_ADDRESS + "/upload")
                        .post(requestBody)
                        .build();

                Call call = messenger.client.newCall(request);
                call.enqueue(callback);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendGetRequestWithoutBodyAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = messenger.client.newCall(request);

        call.enqueue(callback);
    }

    public static void genericOnResponseHandler(Runnable runnable, Response response, Label errorLabel) throws IOException {
        if (response.code() != 200) {
            String responseBody = response.body().string();
            Platform.runLater(() ->
                    errorLabel.setText("Something went wrong: " + responseBody)
            );
        } else {
            Platform.runLater(runnable);
        }
    }
}
