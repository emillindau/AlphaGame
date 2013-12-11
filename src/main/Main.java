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

    public static String MAIN = "MainController";
    public static String MAIN_FXML = "../fxml/main.fxml";
    public static String GAME = "GameController";
    public static String GAME_FXML = "../fxml/game.fxml";

    @Override
    public void start(Stage aPrimaryStage) throws Exception{

        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Main.MAIN, Main.MAIN_FXML);
        mainContainer.loadScreen(Main.GAME, Main.GAME_FXML);

        mainContainer.setScreen(Main.MAIN);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root, 500, 500);
        aPrimaryStage.setScene(scene);
        aPrimaryStage.show();

        //GameModel model = new GameModel(GameModel.Difficulty.HARDEST);
       // model.start();

        /*
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));

        final Stage primaryStage = aPrimaryStage;
        final StackPane secondRoot = new StackPane();
        Button btn = new Button();
        btn.setText("This is second button");
        secondRoot.getChildren().add(btn);

        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println(keyEvent);
            }
        });

        root.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {
            @Override
            public void handle(InputEvent inputEvent) {
                if(inputEvent.getEventType().equals("KEY_PRESSED")) {
                    System.out.println("true");
                }
            }
        });

        TextField tf = new TextField();

        root.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("ActionEvent: " + actionEvent.getTarget());
            }
        });

        root.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Event: " + mouseEvent.getTarget());
                // primaryStage.hide();
                primaryStage.setScene(new Scene(secondRoot, 400, 200));
                primaryStage.show();
            }
        });

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();   */
    }


    public static void main(String[] args) {
        launch(args);
    }
}
