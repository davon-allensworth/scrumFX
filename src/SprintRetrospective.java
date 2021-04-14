import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class SprintRetrospective extends Scene{
    Button nextScreen = null;
    GraphicsContext gc;
    GameManager gm;

    public SprintRetrospective(Parent root, GraphicsContext gc, GameManager gm) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.gm = gm;

        // Mouse event handler
        this.setOnMouseClicked(
                e -> {
                    if (nextScreen.collidesWith(e.getX(), e.getY())) {
                        nextScreen.pressed();
                        if (gm.iterationsComplete) {
                            gm.changeScene("results");
                        } else {
                            gm.changeScene("arena");
                        }
                    }
                });

    }
    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            e.draw();
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
        this.entities.add(nextScreen);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTCORAL);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(gm.getTextColor());
    }

}
