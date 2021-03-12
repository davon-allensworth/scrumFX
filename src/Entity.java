import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Entity {
    private float x, y;
    private float height, width;
    private Sprite sprite;
    private float xpeed, yspeed;

    public Entity(GraphicsContext gc) {
        this.sprite = new Sprite(gc, "https://cdn.discordapp.com/attachments/801173300415037504/815023513903169546/S.C.R.U.M..gif");
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

    public boolean collidesWith(float x, float y) {
        return false;
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
