import java.util.List;

import javafx.scene.Parent;

public abstract class Scene extends javafx.scene.Scene {
    
    public Scene(Parent arg0) {
        super(arg0);
        //TODO Auto-generated constructor stub
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
}
