package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements ControlledScreen {

    private ScreensController mController;

    // Think this works, even though the documentation says implements Initializeable
    @FXML
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        mController = screenPage;
    }

    @FXML
    private void startGame(ActionEvent event) {
        System.out.println(event.getSource());

        // Set a new screen
        mController.setScreen(Main.GAME);
    }
}
