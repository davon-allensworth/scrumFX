import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Sprite {
    public boolean visible = true;
    private double scale = 1;
    private String imageName;
    Image image = null;
    GraphicsContext gc;

    public static final double SCALE_MULTIPLIER = 0.5;

    public Sprite(GraphicsContext gc, String filename) {
        this(gc, filename, 1);
    }

    public Sprite(GraphicsContext gc, String filename, double scale) {
        this.gc = gc;
        this.scale = scale * SCALE_MULTIPLIER;
        image = new Image(filename);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
    }

    public void draw(double x, double y) {
        if(visible) gc.drawImage(image, x, y, image.getWidth()*scale, image.getHeight()*scale);
    }

    public Image getImage(){
        return image;
    }
}
