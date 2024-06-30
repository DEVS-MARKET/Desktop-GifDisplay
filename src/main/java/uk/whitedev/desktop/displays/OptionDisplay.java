package uk.whitedev.desktop.displays;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uk.whitedev.desktop.Config;
import uk.whitedev.desktop.displays.functions.OptionsFunc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OptionDisplay {
    private final OptionsFunc optionsFunc = new OptionsFunc();
    private TextField gifPathTextField;
    private TextField musicPathTextField;
    private final Config config = Config.getInstance();
    private final List<String> gifsName = List.of("Konata", "Konosuba", "GoBang", "Chicka", "NekoMain");

    public void showOptionDisplay(Stage stage) {
        setStageLocation(stage);

        GridPane gridPane = generateOptions(stage);

        Scene scene = new Scene(gridPane);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/assets/styles/theme.css")).toString());

        stage.setScene(scene);
        stage.show();
    }

    private GridPane generateOptions(Stage stage){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Region blackBox = new Region();
        blackBox.setMaxSize(stage.getMaxWidth(), 50);
        GridPane.setMargin(blackBox, new Insets(0, 0, 45, 0));
        blackBox.setId("title-background");
        gridPane.add(blackBox, 0, 0, 3, 2);

        Text title = new Text("GifDisplay Options");
        title.setId("gui-title");
        GridPane.setMargin(title, new Insets(5, 0, 0, 10));
        gridPane.add(title, 0, 0);

        ChoiceBox<String> gifChoiceBox = new ChoiceBox<>();
        gifChoiceBox.getItems().addAll(gifsName);
        gifChoiceBox.setValue(config.getConfig().get("Gif").toString());
        GridPane.setMargin(gifChoiceBox, new Insets(5, 0, 0, 5));
        gridPane.add(gifChoiceBox, 0, 1);

        gifPathTextField = new TextField();
        gifPathTextField.setPromptText("Enter path to GIF (leave it empty if u want use standard gifs)");
        gifPathTextField.setText(config.getConfig().get("GifPath").toString());
        Button gifPathButton = new Button("Browse");
        gifPathButton.setOnAction(e -> browseFile(gifPathTextField));

        gridPane.add(gifPathTextField, 0, 2);
        gridPane.add(gifPathButton, 1, 2);

        GridPane.setMargin(gifPathTextField, new Insets(5, 0, 0, 5));
        GridPane.setMargin(gifPathButton, new Insets(5, 0, 0, 5));

        musicPathTextField = new TextField();
        musicPathTextField.setPromptText("Enter path to music");
        musicPathTextField.setText(config.getConfig().get("MusicPath").toString());
        Button musicPathButton = new Button("Browse");
        musicPathButton.setOnAction(e -> browseFile(musicPathTextField));

        gridPane.add(musicPathTextField, 0, 3);
        gridPane.add(musicPathButton, 1, 3);

        GridPane.setMargin(musicPathTextField, new Insets(5, 0, 0, 5));
        GridPane.setMargin(musicPathButton, new Insets(5, 0, 0, 5));

        TextField gifSizeTextField = new TextField();
        gifSizeTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) return change;
            return null;
        }));
        gifSizeTextField.setText(config.getConfig().get("GifSize").toString());
        gifSizeTextField.setPromptText("Enter gif size (min 50 - max 1000)");
        GridPane.setMargin(gifSizeTextField, new Insets(5, 0, 0, 5));
        gridPane.add(gifSizeTextField, 0, 4);

        CheckBox useDefaultMusicCheckbox = new CheckBox("Music (leave the music path empty to use default music)");
        gridPane.add(useDefaultMusicCheckbox, 0, 5);
        useDefaultMusicCheckbox.setSelected((Boolean) config.getConfig().get("Music"));
        GridPane.setMargin(useDefaultMusicCheckbox, new Insets(5, 0, 0, 5));

        HBox buttonBox = new HBox(10);
        Button saveConfigButton = new Button("Save Config");
        saveConfigButton.setOnMouseClicked(e -> optionsFunc.saveConfig(gifChoiceBox.getSelectionModel().getSelectedItem(), musicPathTextField.getText(), gifPathTextField.getText(), Integer.parseInt(gifSizeTextField.getText()), useDefaultMusicCheckbox.isSelected()));
        Button loadConfigButton = new Button("Exit");
        loadConfigButton.setOnMouseClicked(e -> System.exit(0));
        buttonBox.getChildren().addAll(saveConfigButton, loadConfigButton);

        gridPane.add(buttonBox, 0, 6, 2, 1);
        GridPane.setMargin(buttonBox, new Insets(5, 0, 0, 5));

        Text authorText = new Text("Authors: github.com/DEVS-MARKET\n" +
                "Order your own app here: discord.gg/KhExwvqZb5");
        authorText.setId("authors-text");
        GridPane.setMargin(authorText, new Insets(5, 0, 0, 5));
        gridPane.add(authorText, 0, 7);

        gridPane.getStyleClass().add("main-container");
        return gridPane;
    }

    private void setStageLocation(Stage stage){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        double sceneWidth = stage.getWidth();
        double sceneHeight = stage.getHeight();

        double centerX = (screenWidth - sceneWidth) / 2;
        double centerY = (screenHeight - sceneHeight) / 2;

        stage.setX(centerX);
        stage.setY(centerY);
    }

    private void browseFile(TextField pathTextField) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            pathTextField.setText(selectedFile.getAbsolutePath());
        }
    }
}
