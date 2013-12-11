package controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Emil
 * Date: 2013-12-07
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class ScreensController extends StackPane {

    private Map<String, Node> mScreens = new HashMap<>();

    public ScreensController() {}

    public void addScreen(String name, Node screen) {
        mScreens.put(name, screen);
    }

    /**
     * Loading screens from fxml files
     * @param name
     * @param resource
     * @return
     */
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenController = (ControlledScreen) myLoader.getController();
            myScreenController.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean setScreen(final String name) {

        // Check if screens been loaded
        if(mScreens.get(name) != null) {
            final DoubleProperty opacity = opacityProperty();

            // Is there more than one screen
            if(!getChildren().isEmpty()) {
                Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)), new KeyFrame(new Duration(1000),

                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent actionEvent) {
                                // Remove displayed screen
                                getChildren().remove(0);
                                // Add new screen
                                getChildren().add(0, mScreens.get(name));

                                Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                        new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));

                                fadeIn.play();
                            }

                        }, new KeyValue(opacity, 0.0)));
                fade.play();
            } else {
                // No one else has been displayed, just show
                setOpacity(0.0);
                getChildren().add(mScreens.get(name));
                Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("Screen hasn't been loaded");
            return false;
        }
    }

    public boolean unloadScreen(String name) {
        if(mScreens.remove(name) == null) {
            System.out.println("Screen did not exist");
            return false;
        }
        return true;
    }
}
