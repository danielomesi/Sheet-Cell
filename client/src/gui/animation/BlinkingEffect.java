package gui.animation;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.Map;

public class BlinkingEffect {
    private final static Map<Label, FadeTransition> transitions = new HashMap<>();

    public static void startEffect(Label label, String text) {
        label.setText(text);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), label);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);


        transitions.put(label, fadeTransition);

        label.setVisible(true);
        fadeTransition.play();
    }

    public static void finishEffect(Label label, String text) {
        label.setText(text);
        FadeTransition fadeTransition = transitions.get(label);

        if (fadeTransition != null) {
            fadeTransition.stop();
            label.setOpacity(1.0);
            transitions.remove(label);
        }
    }
}

