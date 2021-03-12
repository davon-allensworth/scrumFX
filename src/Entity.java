import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Entity {
    double x, y;
    double height, width;
    private Sprite sprite;
    private double xpeed, yspeed;
    GraphicsContext gc;

    public Entity(GraphicsContext gc) {
        this(gc, 0, 0, 0, 0, 1);
    }

    public Entity(GraphicsContext gc, double x, double y, double w, double h, double scale) {
        this(gc, "https://cdn.discordapp.com/attachments/801173300415037504/815023513903169546/S.C.R.U.M..gif",
             x, y, h, w, scale);
        System.out.println("default entity asset constructor called");
    }
    
    public Entity(GraphicsContext gc, String filename, double x, double y, double w, double h, double scale) {
        this.gc = gc;
        this.sprite = new Sprite(gc, filename, scale);
        this.x = x;
        this.y = y;
        this.height = h;
        this.width = w;
    }
    
    public void draw() {
        sprite.draw(x, y);
    }

    public void update() {
        move();
    }

    private void move() {
        this.x += 0.1;
    }

    public boolean collidesWith(Entity other) {
        return false;
    }

    public boolean collidesWith(double x2, double y2) {
        return (x2 > x && x2 < (x+width)) &&
               (y2 > y && y2 < (y+height));
    }

    public boolean changeSprite(Image image) {
        return false;
    }

    public boolean changeSprite(String imageName) {
        return false;
    }

    public boolean changeSprite(Sprite newSprite) {
        return false;
    }
}
