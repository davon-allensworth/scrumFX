import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class Results extends Scene{
    Button menuButton = null;
    GraphicsContext gc;

    public Results(Parent root, GraphicsContext gc, GameManager gm) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;

        // Mouse event handler
        this.setOnMouseClicked(
                e -> {
                    //
                    if (menuButton.collidesWith(e.getX(), e.getY())) {
                        menuButton.pressed();
                        // Here is where the score needs to be added.

                        // Return to main menu
                        gm.changeScene("main menu");
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
        menuButton = new Button(gc, "menu button", "menu button pressed", centerx, centery);
        menuButton.updateX(-(menuButton.getWidth()/2));
        menuButton.updateY((menuButton.getHeight() + 30));
        this.entities.add(menuButton);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

}