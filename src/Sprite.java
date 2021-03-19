import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Sprite {
    private double scale = 1;
    private String imageName;
    Image image = null;
    GraphicsContext gc;

    public Sprite(GraphicsContext gc, String filename) {
        this(gc, filename, 1);
    }

    public Sprite(GraphicsContext gc, String fileName, double scale) {
        this.gc = gc;
        this.scale = scale;
        image = new Image(fileName);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
    }

    public void draw(double x, double y) {
        gc.drawImage(image, x, y, image.getWidth()*scale, image.getHeight()*scale);
    }

    public Image getImage(){
        return image;
    }
}
