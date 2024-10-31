package http;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import okhttp3.Callback;
import static constants.Constants.*;

public class RequestScheduler {

    private static Timeline requestTimeline;

    public static void startHttpRequestScheduler(String finalUrl, Callback callback) {
        if (requestTimeline != null) {
            requestTimeline.stop();
        }

        requestTimeline = new Timeline(
                new KeyFrame(Duration.seconds(REFRESH_RATE_IN_SECONDS), event -> {
                    HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, callback);
                })
        );

        requestTimeline.setCycleCount(Timeline.INDEFINITE);
        requestTimeline.play();
    }

    public static void stopHttpRequestScheduler() {
        if (requestTimeline != null) {
            requestTimeline.stop();
        }
    }
}

