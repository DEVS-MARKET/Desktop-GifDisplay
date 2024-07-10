package uk.whitedev.desktop.displays.functions;

import uk.whitedev.desktop.Config;

import java.io.File;
import java.io.IOException;

public class OptionsFunc {
    private final Config config = Config.getInstance();

    public void saveConfig(String gif, String musicPath, String gifPath, int gifSize, boolean music, boolean onTop){
        config.updateConfig("Gif", gif);
        config.updateConfig("MusicPath", musicPath);
        config.updateConfig("GifPath", gifPath);
        config.updateConfig("GifSize", (gifSize >= 50 && gifSize <= 1000) ? gifSize : 400);
        config.updateConfig("Music", music);
        config.updateConfig("OnTop", onTop);
        config.saveConfig();
        runGifDisplay();
        System.exit(0);
    }

    private void runGifDisplay(){
        ProcessBuilder processBuilder = new ProcessBuilder("Start.bat");
        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
