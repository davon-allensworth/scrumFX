import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class SprintRetrospective extends Scene {
    Button nextScreen = null;
    GraphicsContext gc;
    GameManager gm;
    Text text;
    StringBuilder victoryText;

    public SprintRetrospective(Parent root, GraphicsContext gc) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;
        gm = GameManager.getInstance();

        // Mouse event handler
        this.setOnMouseClicked(
        e -> {
            if (nextScreen.collidesWith(e.getX(), e.getY())) {
                nextScreen.pressed();
            }
        });
    }

    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            e.draw();

        // Display Sprint Success
        // Current Score
        gc.fillText(this.victoryText.toString(), 10, 20);
    }

    @Override
    public void teardown() {

    }

    @Override
    public void setup() {
        double centerx = gc.getCanvas().getWidth() / 2;
        double centery = gc.getCanvas().getHeight() / 2;

        nextScreen = new Button(gc, "next button", "next button pressed", centerx, centery);
        nextScreen.updateX(-(nextScreen.getWidth()/2));
        nextScreen.updateY((nextScreen.getHeight() + 30));

        // Text setup
        text = new Text();
        this.victoryText = new StringBuilder("Congratulations!\n\nYou have successfully completed sprint " + gm.currentSprint + "!\n\n");
        victoryText.append("Your current score: ").append(gm.totalScore);

        this.entities.add(nextScreen);
    }

    @Override
    public void update() {
        nextScreen.update();

        if(nextScreen.isTriggered()){
            if (gm.iterationsComplete) {
                gm.changeScene("results");
            } else {
                gm.changeScene("story select");
            }
        }
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTCORAL);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(GameManager.getTextColor());
    }
}
