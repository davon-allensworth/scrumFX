import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class Settings extends Scene {
    Button backButton = null;
    GraphicsContext gc;

    public Settings(Parent root, GraphicsContext gc) {
        super(root);
        this.entities = new ArrayList<Entity>();
        this.gc = gc;

        //Mouse event handler
        this.setOnMouseClicked(
            new EventHandler<MouseEvent>(){
                public void handle(MouseEvent e){
                    if(backButton.collidesWith(e.getX(), e.getY())){
                        backButton.pressed();
                        GameManager.getInstance().changeScene("main menu");
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
        backButton = new Button(gc, "exit button", "exit button pressed", 0, 0);
        this.entities.add(backButton);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }
    
}
