package gui.utils;

/*
* this class is not in use, it was an effort to make my own window header (title bar) and there were too many bugs
* */

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomWindow {
    private static final double BORDER_SIZE = 5; // Thickness of the resizable border
    private static double xOffset, yOffset;
    private static double widthOffset, heightOffset;
    private static double originalWidth, originalHeight;
    private static VBox root; // Store the root VBox for easy updates

    public static void createStylishWindow(Stage stage, Scene contentScene, String title) {
        stage.initStyle(StageStyle.UNDECORATED);

        // Custom title bar
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #2E2E2E; -fx-padding: 8px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        // Spacer to push the close button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Close button
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> stage.close());

        // Fullscreen toggle button (square button)
        Button fullscreenButton = new Button("◻");
        fullscreenButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        fullscreenButton.setOnAction(e -> toggleFullscreen(stage, fullscreenButton));

        // Minimize button
        Button minimizeButton = new Button("_");
        minimizeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        minimizeButton.setOnAction(e -> stage.setIconified(true)); // Minimize the window

        titleBar.getChildren().addAll(titleLabel, spacer, minimizeButton,fullscreenButton, closeButton);

        // Enable window dragging
        enableWindowDragging(stage, titleBar);

        // Wrap content scene in a VBox with the custom title bar
        root = new VBox();
        VBox.setVgrow(contentScene.getRoot(), Priority.ALWAYS);

        root.getChildren().addAll(titleBar, contentScene.getRoot());


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private static void enableWindowDragging(Stage stage, HBox titleBar) {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    private static void toggleFullscreen(Stage stage, Button fullscreenButton) {
        if (stage.isMaximized()) {
            // Return to original size
            stage.setMaximized(false); // Unmaximize the window
            stage.setWidth(originalWidth);
            stage.setHeight(originalHeight);
            fullscreenButton.setText("◻");
        } else {
            // Store original size and set to maximized window
            originalWidth = stage.getWidth();
            originalHeight = stage.getHeight();
            stage.setMaximized(true); // Maximize within the window
            fullscreenButton.setText("⛶");
        }
    }

    public static void loadNewScene(Stage stage, Scene newContentScene) {
        // Remove the old content and set the new content while preserving the title bar
        root.getChildren().set(1, newContentScene.getRoot()); // Replace content part
        stage.setScene(new Scene(root)); // Update the scene with the new content
    }
}


