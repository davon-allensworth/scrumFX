import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class StorySelect extends Scene {
    private Button startSprintButton = null;
    private GraphicsContext gc;

    public StorySelect(Parent root, GraphicsContext gc) {
        super(root);
        this.entities = new ArrayList<Entity>();
        this.gc = gc;

        this.setOnMouseClicked(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent e)
                {
                    if (startSprintButton.collidesWith( e.getX(), e.getY() ) )
                    {
                        startSprintButton.pressed();
                        GameManager.getInstance().changeScene("arena");
                    }
                }
            });

    }

    @Override
    public void setup() {
        double centerx = (double)gc.getCanvas().getWidth() / 2;
        double centery = (double)gc.getCanvas().getHeight() / 2;
        
        startSprintButton = new Button(gc, "next button", "next button pressed", centerx, centery);
        startSprintButton.updateX(-(startSprintButton.getWidth()/2));
        this.entities.add(startSprintButton);
    }

    @Override
    public void teardown() {
    }
    
    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            e.draw();  
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTCORAL);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(GameManager.getInstance().getTextColor());
    }
}
