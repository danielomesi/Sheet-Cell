package http;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;

public class RequestScheduler {

    private static Timeline requestTimeline;

    public static void startHttpRequestScheduler(String finalUrl, Callback callback) {
        if (requestTimeline != null) {
            requestTimeline.stop(); // Stop the previous timeline if it exists
        }

        // Create a Timeline to repeat the HTTP request every 2 seconds
        requestTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> {
                    // This code will be called every 2 seconds
                    HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, callback);
                })
        );

        // Repeat indefinitely
        requestTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timeline
        requestTimeline.play();
    }

    public static void stopHttpRequestScheduler() {
        if (requestTimeline != null) {
            requestTimeline.stop(); // Stop the timeline when you no longer need to send requests
        }
    }
}

