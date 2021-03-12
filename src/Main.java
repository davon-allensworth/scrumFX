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
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("S.C.R.U.M.");

        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        Canvas canvas = new Canvas(1225, 600);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Entity test = new Entity(canvas.getGraphicsContext2D());

        // Image scrum = new Image("https://cdn.discordapp.com/attachments/801173300415037504/815023513903169546/S.C.R.U.M..gif");        

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
                    double t = (System.currentTimeMillis() - timeStart) / 1000.0; 
                                
                    // double x = 232 + 128 * Math.cos(t);
                    // double y = 232 + 128 * Math.sin(t);
                    
                    // Clear the canvas
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    
                    // background image clears canvas
                    // gc.drawImage(scrum, 0, 0 );
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

