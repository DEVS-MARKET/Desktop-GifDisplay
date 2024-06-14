package uk.whitedev.desktop;

import uk.whitedev.desktop.displays.GifDisplay;

public class Main {
    private static final Config config = Config.getInstance();

    public static void main(String[] args) {
        config.loadConfig(null);
        new Thread(GifDisplay::initGui).start();
    }
}
