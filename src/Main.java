import java.io.File;
import java.nio.file.Files;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

        final long startNanoTime = System.nanoTime();
 
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // background image clears canvas
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                test.update();
                test.draw();
            }
        }.start();


        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }
}

