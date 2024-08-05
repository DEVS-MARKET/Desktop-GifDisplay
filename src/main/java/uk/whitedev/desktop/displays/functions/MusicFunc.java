package uk.whitedev.desktop.displays.functions;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MusicFunc {
    private MediaPlayer mediaPlayer;

    public void playMusic(String musicPath, String defaultMusic) {
        String path = musicPath.isEmpty()
                ? getClass().getResource(defaultMusic.equals("GoBang") ? "/assets/music/GoBang Music.wav" : "/assets/music/Default Song.wav").toString()
                : new File(musicPath).toURI().toString();

        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
    }
}

