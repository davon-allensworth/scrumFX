import java.util.List;

import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Scene extends javafx.scene.Scene {
    Color bgColor = null;

    public Scene(Parent arg0, Color bgColor) {
        super(arg0);
        this.bgColor = bgColor;
    }

    List<Entity> entities = null;

    public void setup() {

    }

    public void teardown() {

    }

    public void update() {
        for(Entity e : entities)
            e.update();
    }
        
    public void draw() {
    }

    public void drawBackground(GraphicsContext gc) { 
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(bgColor);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(GameManager.getTextColor());
              
    }
}
