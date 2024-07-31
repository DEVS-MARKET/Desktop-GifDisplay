package uk.whitedev.desktop.loader;

import uk.whitedev.desktop.displays.GifDisplay;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class LoadStandardGIf {
    public List<File> getFrames(String gif) {
        try {
            File folder = getResourceFolder(gif);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
            if (files == null || files.length == 0) {
                System.err.println("Can't find gif frames");
                System.exit(0);
            }

            return Arrays.stream(files)
                    .sorted((f1, f2) -> {
                        int frameNumber1 = extractFrameNumber(f1.getName());
                        int frameNumber2 = extractFrameNumber(f2.getName());
                        return Integer.compare(frameNumber1, frameNumber2);
                    })
                    .collect(Collectors.toList());
        }catch (IOException e){
            throw new RuntimeException("Can't find this resources");
        }
    }

    public static File getResourceFolder(String gif) throws IOException {
        String resourcePath = "/assets/images/" + gif.toLowerCase();
        URL resourceUrl = GifDisplay.class.getResource(resourcePath);
        if (resourceUrl == null) {
            System.err.println("Can't find " + resourcePath + " resource.");
            return null;
        }
        if ("file".equals(resourceUrl.getProtocol())) {
            return new File(resourceUrl.getPath());
        } else if ("jar".equals(resourceUrl.getProtocol())) {
            return extractResourcesToTempFolder(resourcePath).toFile();
        } else {
            throw new IOException("Not supported URL protocol: " + resourceUrl.getProtocol());
        }
    }

    public static Path extractResourcesToTempFolder(String resourcePath) throws IOException {
        Path tempDir = Files.createTempDirectory("gif_frames_resource");
        String jarPath = LoadStandardGIf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (jarPath.startsWith("file:")) {
            jarPath = jarPath.substring(5);
        }
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(resourcePath.substring(1))) {
                    Path entryPath = tempDir.resolve(entryName.substring(resourcePath.length()));
                    if (entry.isDirectory()) {
                        Files.createDirectories(entryPath);
                    } else {
                        Files.createDirectories(entryPath.getParent());
                        try (InputStream input = jarFile.getInputStream(entry)) {
                            Files.copy(input, entryPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        }
        return tempDir;
    }

    private static int extractFrameNumber(String filename) {
        String numberPart = filename.substring(6, 10);
        if(numberPart.contains("_")) numberPart = numberPart.substring(0, numberPart.length()-1);
        return Integer.parseInt(numberPart);
    }

}
