package uk.whitedev.desktop.displays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import uk.whitedev.desktop.Config;
import uk.whitedev.desktop.displays.functions.MusicFunc;
import uk.whitedev.desktop.displays.functions.TrayIconFunc;
import uk.whitedev.desktop.loader.LoadStandardGIf;

import java.io.File;
import java.util.List;

public class GifDisplay extends Application {
    private final TrayIconFunc trayIconFunc = new TrayIconFunc();
    private static final MusicFunc musicFunc = new MusicFunc();
    private final LoadStandardGIf gifLoader = new LoadStandardGIf();
    private static final Config config = Config.getInstance();
    public static boolean ISLOCKED = false;
    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setOpacity(0);
        primaryStage.setHeight(0);
        primaryStage.setWidth(0);
        primaryStage.show();
        showGifDisplay(primaryStage);
    }

    private void showGifDisplay(Stage primaryStage) {
        Stage mainStage = new Stage();
        mainStage.initOwner(primaryStage);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        renderGifDisplay(mainStage);
        trayIconFunc.addIconToSysTray(mainStage);
    }

    private void renderGifDisplay(Stage mainStage) {
        String standardGif = (String) config.getConfig().get("Gif");
        String gifPath = (String) config.getConfig().get("GifPath");
        int gifSize = (int) config.getConfig().get("GifSize");
        String musicPath = (String) config.getConfig().get("MusicPath");
        boolean isMusic = (boolean) config.getConfig().get("Music");
        boolean isAlwaysOnTop = (boolean) config.getConfig().get("OnTop");

        Scene scene;
        if(gifPath.isEmpty()) {
            scene = getScene(mainStage, null, standardGif, gifSize);
        }else{
            Image gifImage = new Image(gifPath);
            scene = getScene(mainStage, gifImage, null, gifSize);
        }

        mainStage.setAlwaysOnTop(isAlwaysOnTop);
        mainStage.setScene(scene);
        mainStage.show();

        if (isMusic) musicFunc.playMusic(musicPath, standardGif);
    }

    private Scene getScene(Stage mainStage, Image gifImage, String gifName, int gifSize) {
        ImageView imageView = new ImageView();
        if(gifImage == null) {
            List<File> images = gifLoader.getFrames(gifName);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis((double) 1000 / 30), e -> updateFrame(images, imageView)));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }else{
            imageView.setImage(gifImage);
        }

        imageView.setFitWidth(gifSize);
        imageView.setPreserveRatio(true);
        HBox hbox = new HBox(imageView);
        hbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");

        StackPane root = new StackPane(hbox);
        Scene scene = new Scene(root, Color.TRANSPARENT);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            if (!ISLOCKED) {
                mainStage.setX(event.getScreenX() - xOffset);
                mainStage.setY(event.getScreenY() - yOffset);
            }
        });

        return scene;
    }

    private int currentFrameIndex = 0;
    private void updateFrame(List<File> frames, ImageView imageView) {
        if (!frames.isEmpty()) {
            imageView.setImage(new Image(frames.get(currentFrameIndex).getAbsolutePath()));
            currentFrameIndex = (currentFrameIndex + 1) % frames.size();
        }
    }

    public static void initGui() {
        launch();
    }
}
