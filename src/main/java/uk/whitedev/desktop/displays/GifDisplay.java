package uk.whitedev.desktop.displays;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.whitedev.desktop.Config;
import uk.whitedev.desktop.displays.functions.MusicFunc;
import uk.whitedev.desktop.displays.functions.TrayIconFunc;

import java.util.Objects;

public class GifDisplay extends Application {
    private final TrayIconFunc trayIconFunc = new TrayIconFunc();

    private static final MusicFunc musicFunc = new MusicFunc();
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

    private static void renderGifDisplay(Stage mainStage) {
        mainStage.setScene(null);

        String standardGif = (String) config.getConfig().get("Gif");
        String gifPath = (String) config.getConfig().get("GifPath");
        int gifSize = (int) config.getConfig().get("GifSize");
        String musicPath = (String) config.getConfig().get("MusicPath");
        boolean isMusic = (boolean) config.getConfig().get("Music");
        boolean isAlwaysOnTop = (boolean) config.getConfig().get("OnTop");

        Image gifImage = new Image(gifPath.isEmpty() ? Objects.requireNonNull(GifDisplay.class.getResource("/assets/images/" + standardGif.toLowerCase() + ".gif")).toString() : gifPath);
        Scene scene = getScene(mainStage, gifImage, gifSize);

        mainStage.setAlwaysOnTop(isAlwaysOnTop);
        mainStage.setScene(scene);
        mainStage.show();

        if (isMusic) musicFunc.playMusic(musicPath, standardGif);
    }

    private static Scene getScene(Stage mainStage, Image gifImage, int gifSize) {
        ImageView imageView = new ImageView(gifImage);

        imageView.setFitWidth(gifSize);
        imageView.setPreserveRatio(true);

        HBox hbox = new HBox(imageView);
        hbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");

        StackPane root = new StackPane(hbox);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            if (!ISLOCKED) {
                mainStage.setX(event.getScreenX() - xOffset);
                mainStage.setY(event.getScreenY() - yOffset);
            }
        });

        scene.setFill(Color.TRANSPARENT);
        return scene;
    }

    public static void initGui() {
        launch();
    }
}
