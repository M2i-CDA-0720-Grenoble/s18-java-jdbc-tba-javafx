package tbajfx;

import tbajfx.game.Game;
import tbajfx.ui.Console;
import tbajfx.ui.GameUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Console console = new Console();
        GameUI gameUI = new GameUI();
        Game game = new Game();

        console.log("Initializing GUI...");
        // The scene which is the "root" of our application
        Scene scene = new Scene(gameUI.render(), 640, 480);

        // The top level JavaJX container
        stage.setTitle("Text-based adventure game");
        stage.setScene(scene);
        stage.show();

        console.log("Setting subscriptions up...");
        gameUI.attach( console );
        gameUI.attach( game );
        game.attach( console );
        game.attach( gameUI );

        game.init();
    }

    public static void main(String[] args) {
        launch();
    }
}