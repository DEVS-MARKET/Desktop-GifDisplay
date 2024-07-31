package uk.whitedev.desktop.displays.functions;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uk.whitedev.desktop.displays.GifDisplay;
import uk.whitedev.desktop.displays.OptionDisplay;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class TrayIconFunc {

    private final OptionDisplay optionDisplay = new OptionDisplay();

    public void addIconToSysTray(Stage stage) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assets/images/icon.png"))).getScaledInstance(16, 16, Image.SCALE_SMOOTH);

            TrayIcon trayIcon = new TrayIcon(image);
            PopupMenu popup = getPopupMenu(stage, tray, trayIcon);

            trayIcon.setPopupMenu(popup);
            tray.add(trayIcon);
            stage.setOnCloseRequest((WindowEvent t) -> Platform.runLater(stage::hide));
        } catch (IOException | AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public PopupMenu getPopupMenu(Stage stage, SystemTray tray, TrayIcon trayIcon) {
        ActionListener showListener = e -> Platform.runLater(() -> {
            stage.show();
            stage.toFront();
        });

        ActionListener optionsListener = e -> Platform.runLater(() -> optionDisplay.showOptionDisplay(stage));

        ActionListener exitListener = e -> {
            tray.remove(trayIcon);
            Platform.exit();
            System.exit(0);
        };

        CheckboxMenuItem lockCheckBox = new CheckboxMenuItem("Lock Here", GifDisplay.ISLOCKED);
        lockCheckBox.addItemListener(e -> GifDisplay.ISLOCKED = lockCheckBox.getState());

        PopupMenu popup = new PopupMenu();
        MenuItem showItem = new MenuItem("Show");
        MenuItem optionsItem = new MenuItem("Options");
        MenuItem exitItem = new MenuItem("Exit");
        showItem.addActionListener(showListener);
        optionsItem.addActionListener(optionsListener);
        exitItem.addActionListener(exitListener);
        popup.add(showItem);
        popup.add(optionsItem);
        popup.add(lockCheckBox);
        popup.add(exitItem);
        return popup;
    }
}