package controller;

import event.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import model.GameModel;

import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Emil
 * Date: 2013-12-07
 * Time: 21:59
 * To change this template use File | Settings | File Templates.
 */
public class GameController implements ControlledScreen, ChangeListener {

    @FXML private TextField input;
    @FXML private Text display;

    private ScreensController mController;

    private GameModel mGame;

    @FXML
    public void initialize() {

        input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                    if(keyEvent.getCode().isLetterKey()) {
                        if(mGame != null) {
                            mGame.input(keyEvent.getText());
                        }
                        System.out.println("TEXT: " + keyEvent.getText());
                    }
                }
                input.clear();
                keyEvent.consume();
            }
        });
    }

    @Override
    public void setScreenParent(ScreensController screenPage) {
        mController = screenPage;
    }

    @Override
    public void show() {
        System.out.println("SHOW!!");
        // Binding
        mGame = new GameModel(GameModel.Difficulty.HARD, Executors.newSingleThreadScheduledExecutor());
        mGame.bind(this);
        mGame.start();
    }

    @Override
    public void dispose() {
        mGame.unbind(this);
        mGame.stop();
    }

    @Override
    public void change(char newChar) {
        display.setText("" + newChar);
    }

    @Override
    public void gameOver() {
        display.setText("GAME OVER!!!");
    }
}
