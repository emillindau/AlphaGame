package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/**
 * Created with IntelliJ IDEA.
 * User: Emil
 * Date: 2013-12-07
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class GameController implements ControlledScreen {

    @FXML private TextField input;
    @FXML private Text display;

    private ScreensController mController;

    @FXML
    public void initialize() {
        input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {

                }
                System.out.println("Event from keyEvent: " + keyEvent);
                keyEvent.consume();
            }
        });
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        mController = screenPage;
    }

}
