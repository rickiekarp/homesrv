package net.rickiekarp.botlib.runner;

import net.rickiekarp.core.view.MainScene;
import net.rickiekarp.botlib.BotLauncher;
import net.rickiekarp.botlib.PluginConfig;
import net.rickiekarp.botlib.model.PluginData;
import net.rickiekarp.botlib.plugin.BotSetting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.GeckoDriverService;
//import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

public class BotRunnerBuilder {
    public BotRunner build(PluginData plugin) {
        switch (PluginConfig.botPlatform) {
            case WEB:
                return new BotRunner<RemoteWebDriver, DriverService>() {
                    @Override
                    public void start() {
                        switch (PluginConfig.botType) {
//                            case FIREFOX: set(new MarionetteDriver((GeckoDriverService) getDriverService(), BotLauncher.getCapabilities(plugin))); break;
                            case CHROME: set(new ChromeDriver((ChromeDriverService) getDriverService(), BotLauncher.getCapabilities(plugin))); break;
                        }
                    }

                    @Override
                    public void setLayout(Node node) {
                        Platform.runLater(() -> MainScene.Companion.getMainScene().getBorderPane().setCenter(node));
                    }

                    @Override
                    public void addSetting(String title, String description, boolean isVisible, Node settingNode) {
                        Platform.runLater(() -> {
                            PluginConfig.settingsList.add(
                                    BotSetting.Builder.create().setName(title).setDescription(description).setVisible(isVisible).setNode(settingNode).build()
                            );
                        });
                    }

                    @Override
                    public void addControlButton(Button... node) {
                        Platform.runLater(() -> {
                            AnchorPane pane = (AnchorPane) MainScene.Companion.getMainScene().getBorderPane().getBottom();
                            HBox controls = (HBox) pane.getChildren().get(0);
                            controls.getChildren().addAll(node);
                        });
                    }

                    @Override
                    public void removeControlButton(int index) {
                        Platform.runLater(() -> {
                            AnchorPane pane = (AnchorPane) MainScene.Companion.getMainScene().getBorderPane().getBottom();
                            HBox controls = (HBox) pane.getChildren().get(0);
                            controls.getChildren().remove(index);
                        });
                    }
                };

            case ANDROID:
                return new BotRunner<AndroidDriver, DriverService>() {
                    @Override
                    public void start() {
                        set(new AndroidDriver((AppiumDriverLocalService) getDriverService(), BotLauncher.getCapabilities(plugin)));
                    }

                    @Override
                    public void setLayout(Node node) {
                        Platform.runLater(() -> MainScene.Companion.getMainScene().getBorderPane().setCenter(node));
                    }

                    @Override
                    public void addSetting(String title, String description, boolean isVisible, Node settingNode) {
                        Platform.runLater(() -> PluginConfig.settingsList.add(
                                BotSetting.Builder.create().setName(title).setDescription(description).setVisible(isVisible).setNode(settingNode).build()
                        ));
                    }

                    @Override
                    public void addControlButton(Button... node) {
                        Platform.runLater(() -> {
                            AnchorPane pane = (AnchorPane) MainScene.Companion.getMainScene().getBorderPane().getBottom();
                            HBox controls = (HBox) pane.getChildren().get(0);
                            controls.getChildren().addAll(node);
                        });
                    }

                    @Override
                    public void removeControlButton(int index) {
                        Platform.runLater(() -> {
                            AnchorPane pane = (AnchorPane) MainScene.Companion.getMainScene().getBorderPane().getBottom();
                            HBox controls = (HBox) pane.getChildren().get(0);
                            controls.getChildren().remove(index);
                        });
                    }
                };

            default:
                System.out.println("no browser selected");
                return null;
        }
    }
}