package uk.whitedev.desktop.displays.functions;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.*;

public class MusicFunc {
    public void playMusic(String filePath, String mainSong) {
        new Thread(() -> {
            Media hit;
            if(filePath.isEmpty()) {
                hit = new Media(getClass().getResource(mainSong.equals("GoBang") ? "/assets/music/GoBang Music.wav" : "/assets/music/Default Song.wav").toString());
            }else{
                hit = new Media(new File(filePath).toURI().toString());
            }
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
            mediaPlayer.play();
            while (true) {
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.seek(Duration.ZERO);
                });
            }
        }).start();
    }
}
