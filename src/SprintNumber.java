import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SprintNumber extends Scene {
    Button nextButton = null;
    Group root;
    GraphicsContext gc;
    GameManager gm;
    Entity text;

    public SprintNumber(Parent root, GraphicsContext gc) {
        super(root, Color.CORNSILK);
        this.root = (Group)root;
        this.entities = new ArrayList<Entity>();
        this.gc = gc;
        this.gm = GameManager.getInstance();

        this.setOnMouseClicked(
            new EventHandler<MouseEvent>(){
                public void handle(MouseEvent e){
                    if(nextButton.collidesWith(e.getX(), e.getY())){
                        nextButton.pressed();
                    }
                }
            });
        
    }

    @Override
    public void draw() {
        super.drawBackground(gc);
        for(Entity e : this.entities)
            e.draw();  
    }
    
    @Override
    public void teardown() {

    }

    @Override
    public void update() {
        nextButton.update();

        if(nextButton.isTriggered()) GameManager.getInstance().changeScene("story select");
    }
    
    @Override
    public void setup() {
        double screenWidth = gc.getCanvas().getWidth();
        double screenHeight = gc.getCanvas().getHeight();
        double centerx = (double)screenWidth / 2;
        double centery = (double)screenHeight / 2;

        int sprintNumber = gm.currentSprint+1;
        text = new Entity(gc, "assets/stories/other/sprint number "+sprintNumber+".png", 0, 0, 1);
        entities.add(text);

        nextButton = new Button(gc, "next button", "next button pressed", centerx, centery + (centery/4));
        nextButton.updateX(-(nextButton.getWidth()/2));
        this.entities.add(nextButton);
    }
}
