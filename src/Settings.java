import java.util.ArrayList;

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
import javafx.scene.text.Font;

public class Settings extends Scene {
    Button backButton = null;
    Slider vSlider;
    Slider sfxSlider;
    Group root;
    GraphicsContext gc;

    public Settings(Parent root, GraphicsContext gc) {
        super(root);
        this.root = (Group)root;
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

        vSlider = new Slider();
        sfxSlider = new Slider();

        vSlider.setPrefWidth(400);
        sfxSlider.setPrefWidth(400);

        //Create a VBox for sliders
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Font font = Font.loadFont( getClass().getResourceAsStream("assets/fonts/prstartk.ttf"), 10);

        //Music volume label and slider
        Label vLabel = new Label("Music Volume:");
        vLabel.setFont(font);
        vbox.getChildren().add(vLabel);
        vbox.getChildren().add(vSlider);

        //SFX volume label and slider
        Label sfxLabel = new Label("SFX Volume:");
        sfxLabel.setFont(font);
        vbox.getChildren().add(sfxLabel);
        vbox.getChildren().add(sfxSlider);
        
        vbox.setPadding(new Insets(gc.getCanvas().getHeight()/2 - 130, 100, 
        gc.getCanvas().getHeight()/2 - 130, 100));  
        vbox.setSpacing(60);
        root.getChildren().add(vbox);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }
    
}
