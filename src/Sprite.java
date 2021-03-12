import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Sprite {
    private float scale = 1;
    private String imageName;
    Image image = null;
    GraphicsContext gc;

    public Sprite(GraphicsContext gc, String filename) {
        this.gc = gc;
        image = new Image(filename);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
    }

    public void draw(float x, float y) {
        gc.drawImage(image, x, y, image.getWidth()*scale, image.getHeight()*scale);
    }
}
