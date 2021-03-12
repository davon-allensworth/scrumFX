import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;
import javafx.scene.canvas.GraphicsContext;

public class Button extends Entity {
    String text = "placeholder";

    public Button(GraphicsContext gc, String text,
                  double x, double y, double w, double h) {
        super(gc, "assets/button.png", x, y, w, h, 1);
        this.text = text;
    }

    public void draw() {
        super.draw();
        
        gc.fillText(this.text, x, y+height-gc.getFont().getSize()/2, width);
    }
}
