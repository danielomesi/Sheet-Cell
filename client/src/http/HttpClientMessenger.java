package http;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class HttpClientMessenger {
    private final static OkHttpClient client = new OkHttpClient();
    private static final String SERVER_ADDRESS = "http://localhost:8080";

    private HttpClientMessenger() {}

    public static void sendFileToServer(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream"))) // Use application/octet-stream for binary files
                .build();


        Request request = new Request.Builder()
                .url(SERVER_ADDRESS)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle success (e.g., print response)
                    System.out.println("File uploaded successfully: " + response.body().string());
                } else {
                    // Handle error response
                    System.err.println("Upload failed: " + response.code() + " " + response.message());
                }
            }
        });
    }
}
