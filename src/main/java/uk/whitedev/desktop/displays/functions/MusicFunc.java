package uk.whitedev.desktop.displays.functions;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MusicFunc {
    public void playMusic(String filePath, String mainSong) {
        new Thread(() -> {
            try {
                AudioInputStream audioInputStream;
                if (filePath.isEmpty()) {
                    byte[] defaultSongBytes = readAllBytesFromResource(mainSong.equals("GoBang") ? "/assets/music/GoBang Music.wav" : "/assets/music/Default Song.wav");
                    audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(defaultSongBytes)));
                } else {
                    Path audioFilePath = Paths.get(filePath);
                    byte[] fileBytes = Files.readAllBytes(audioFilePath);
                    audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(fileBytes)));
                }

                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                while (true) {
                    clip.setFramePosition(0);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    Thread.sleep(clip.getMicrosecondLength() / 1000);
                }
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private byte[] readAllBytesFromResource(String resourcePath) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        }
    }
}
