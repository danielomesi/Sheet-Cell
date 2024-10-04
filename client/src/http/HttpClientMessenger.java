package http;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class HttpClientMessenger {
    private static final HttpClientMessenger singleton = new HttpClientMessenger();
    private final OkHttpClient client = new OkHttpClient();
    private static final String SERVER_ADDRESS = "http://localhost:8080/server";

    private HttpClientMessenger() {}

    public static HttpClientMessenger getInstance() {return singleton;}

    public void sendFileToServer(File file) {
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

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        System.out.println("Response from server: " + response.body().string());
                    } else {
                        System.err.println("Request failed: " + response.message());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("File does not exist: " + file.getAbsolutePath());
        }
    }

}
