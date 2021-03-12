import java.io.File;
import java.nio.file.Files;

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

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("S.C.R.U.M.");

        Group root = new Group();
        
        Canvas canvas = new Canvas(900, 900);
        root.getChildren().add(canvas);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        System.out.println(gc.getFont());
        System.out.println(Font.getFamilies());
        gc.setFont(new Font("Comic Sans MS", 50));

        Scene currentScene = new MainMenu(root, gc);
        stage.setScene(currentScene);
        currentScene.setup();        

        // Set up game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
        
        final long timeStart = System.currentTimeMillis();
        
        KeyFrame kf = new KeyFrame(
            Duration.seconds(0.033),
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent ae)
                {
                    // Clear the canvas
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    
                    currentScene.draw();
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

