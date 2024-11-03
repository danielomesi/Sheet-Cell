package http;

import constants.Constants;
import javafx.application.Platform;
import javafx.scene.control.Label;
import json.GsonInstance;
import okhttp3.*;
import okhttp3.JavaNetCookieJar;
import java.io.File;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Optional;

import static constants.Constants.*;

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

    public static void sendDeleteRequestWithoutBodyAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .delete()
                .url(finalUrl)
                .build();

        Call call = messenger.client.newCall(request);

        call.enqueue(callback);
    }

    public static<T> void sendPostRequestWithBodyAsync(String finalUrl,T objectToSerializeInBody, Callback callback) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String jsonBody = GsonInstance.getGson().toJson(objectToSerializeInBody);
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(requestBody)
                .build();

        Call call = messenger.client.newCall(request);

        call.enqueue(callback);
    }

    public static void genericOnResponseHandler(MyResponseHandler responseHandler, Response response, Label statusLabel) {
        try (ResponseBody body = response.body()) {
            String bodyAsStr = body.string();
            if (response.code() != 200) {
                Platform.runLater(() -> {
                            if (statusLabel != null && !statusLabel.textProperty().isBound()) {
                                statusLabel.setStyle(CSS_DEFINITION_FOR_RED_COLOR);
                                statusLabel.setText(GENERAL_ERROR_MESSAGE + "\n" + bodyAsStr);
                            }
                        }
                );
            } else {
                Platform.runLater(() -> { responseHandler.handle(bodyAsStr);
                    if (statusLabel != null && !statusLabel.textProperty().isBound()) {
                        statusLabel.setStyle(CSS_DEFINITION_FOR_GREEN_COLOR);
                        statusLabel.setText(GENERAL_TASK_SUCCESS_MESSAGE);
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
