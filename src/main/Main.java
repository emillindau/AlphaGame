package main;

import controller.ScreensController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.GameModel;

public class Main extends Application {

    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_WIDTH = 500;

    public static String MAIN = "MainController";
    public static String MAIN_FXML = "../fxml/game2.fxml";
    public static String GAME = "GameController";
    public static String GAME_FXML = "../fxml/game.fxml";

    private ScreensController mController;

    @Override
    public void start(Stage aPrimaryStage) throws Exception{

        mController = new ScreensController();
        mController.loadScreen(Main.MAIN, Main.MAIN_FXML);
        mController.loadScreen(Main.GAME, Main.GAME_FXML);

        mController.setScreen(Main.MAIN);

        Group root = new Group();
        root.getChildren().addAll(mController);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        aPrimaryStage.setScene(scene);
        aPrimaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        super.stop();
        mController.dispose();
        System.out.println("STOPPING!!!");
    }



    public static void main(String[] args) {
        launch(args);
    }
}
