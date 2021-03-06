import java.io.File;

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
        imageName = filename;
        image = new Image(new File("src/" + filename).toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
    }

    public String getImageName(){
        return imageName;
    }

    public void draw(double x, double y) {
        if(visible) gc.drawImage(image, x, y, image.getWidth()*scale, image.getHeight()*scale);
    }

    public Image getImage(){
        return image;
    }
}
