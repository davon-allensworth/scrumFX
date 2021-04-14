import java.io.File;
import java.nio.file.Files;

import org.mockito.internal.matchers.Null;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
// import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private static final int FONT_SIZE = 10;
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("S.C.R.U.M.");

        Group root = new Group();
        Canvas canvas = new Canvas(600, 600);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font font = Font.loadFont( getClass().getResourceAsStream("assets/fonts/prstartk.ttf"), FONT_SIZE);
        gc.setFont(font);

        GameManager.init(gc);
        GameManager gm = GameManager.getInstance();

        stage.setScene(new MainMenu(root, gc));
        stage.setResizable(false);
        ((Scene) stage.getScene()).setup();

        gm.setStage(stage);

        // Set up game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
                
        KeyFrame kf = new KeyFrame(
            Duration.seconds(0.033),
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent ae)
                {
                    ((Scene) stage.getScene()).update();
                    ((Scene) stage.getScene()).draw();
                }
            });
        
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();

        stage.show();

    }
    
    public static void main(String[] args) {
        launch();
    }
}

