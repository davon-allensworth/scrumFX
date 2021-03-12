import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;
import javafx.scene.canvas.GraphicsContext;

public class Button extends Entity {
    String text = "placeholder";

    public Button(GraphicsContext gc, String text,
                  float x, float y, float w, float h) {
        super(gc, "button.png", x, y, w, h);
        this.text = text;
    }

    public void draw() {
        super.draw();
        
        gc.fillText(this.text, x, y+height-gc.getFont().getSize()/2);
    }
}
