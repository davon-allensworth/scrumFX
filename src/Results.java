import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Results extends Scene{
    Button menuButton = null;
    GraphicsContext gc;
    GameManager gm;

    Sound music = new Sound("assets/music/A_Typical_Ride_Out.mp3", true);

    public Results(Parent root, GraphicsContext gc) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.gm = gm;

        // Mouse event handler
        this.setOnMouseClicked(
        e -> {
            if (menuButton.collidesWith(e.getX(), e.getY())) {
                menuButton.pressed();

                // Here we need to get user's name input and sanitize

                // Add the score to history
                addScore(gm.totalScore, "Tester");

                // Return to main menu
                gm.changeScene("main menu");
            }
        });
    }

    private void addScore(int score, String user){
        try{
            PrintWriter scoreDoc = new PrintWriter(new FileWriter("scores.txt",true));
            scoreDoc.append("").append(String.valueOf(score)).append(":").append(user).append("\n");
            scoreDoc.close();
        } catch (Exception e) {
            System.out.println("Error adding score to file");
            System.exit(2);
        }
    }

    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            e.draw();
    }

    @Override
    public void teardown() {
        music.stop();
    }
    
    @Override
    public void setup() {
        double centerx = gc.getCanvas().getWidth() / 2;
        double centery = gc.getCanvas().getHeight() / 2;

        music.play();

        menuButton = new Button(gc, "menu button", "menu button pressed", centerx, centery);
        menuButton.updateX(-(menuButton.getWidth()/2));
        menuButton.updateY((menuButton.getHeight() + 30));
        this.entities.add(menuButton);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTCORAL);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(gm.getTextColor());
    }

}