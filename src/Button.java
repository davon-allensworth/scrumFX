import java.io.IOException;

import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;
import javafx.scene.canvas.GraphicsContext;

public class Button extends Entity {
    String pressedButton = null;
    String idleButton = null;
    private static final String ASSET_PATH = "assets/buttons/";
    private static final String FILE_EXT = ".png";

    static Sound buttonClick = new Sound("assets/sounds/explosion.wav");

    public Button(GraphicsContext gc, String buttonName,
                  double x, double y, double w, double h) {
        super(gc, ASSET_PATH+buttonName+FILE_EXT, x, y, w, h, 1);
    }

    //this constructor is for if the button has a sprite for its pressed state
    public Button(GraphicsContext gc, String buttonName, String pressedButtonName,
                  double x, double y) {

        //this constructor automatically sets the height and width to match the sprite
        super(gc, ASSET_PATH+buttonName+FILE_EXT, x, y, 1); 
        idleButton = buttonName;
        pressedButton = pressedButtonName;
    }

    public void draw() {
        super.draw();
    }

    public void pressed() {
        buttonClick.stop();
        buttonClick.play();
        this.updateSprite(ASSET_PATH+pressedButton+FILE_EXT);
    }
}