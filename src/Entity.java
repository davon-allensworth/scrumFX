import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Entity {
    float x, y;
    float height, width;
    private Sprite sprite;
    private float xpeed, yspeed;
    GraphicsContext gc;

    public Entity(GraphicsContext gc) {
        this(gc, 0, 0, 0, 0);
    }

    public Entity(GraphicsContext gc, float x, float y, float w, float h) {
        this(gc, "https://cdn.discordapp.com/attachments/801173300415037504/815023513903169546/S.C.R.U.M..gif",
             x, y, h, w);
        System.out.println("default entity asset constructor called");
    }
    
    public Entity(GraphicsContext gc, String filename, float x, float y, float w, float h) {
        this.gc = gc;
        this.sprite = new Sprite(gc, filename);
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

    public boolean collidesWith(float x2, float y2) {
        return (x2 > x && x2 < (x+width)) &&
               (y2 > y && y2 < (y+height));
    }

    public boolean collidesWith(double x2, double y2) {
        return this.collidesWith((float) x2, (float) y2);
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
