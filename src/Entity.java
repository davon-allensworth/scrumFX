import java.time.Year;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Entity {
    double x, y; //for sprite
    double height, width; //for sprite
    double hitboxX, hitboxY; //for hitbox
    double hitboxHeight, hitboxWidth; //for hitbox
    private Sprite sprite;
    private double xspeed, yspeed;
    private double scale;
    protected boolean visible = true;
    GraphicsContext gc;

    /*
    public Entity(GraphicsContext gc) {
        this(gc, 0, 0, 0, 0, 1);
    }

     */

    //for hitbox set to same as sprite dimensions
    public Entity(GraphicsContext gc, String filename, double x, double y, double scale) {
        this(gc, filename, x, y, 1, 1, scale);
        setDimensionsToSprite();
        this.hitboxWidth *= scale;
        this.hitboxHeight *= scale;
    }

    /*
    public Entity(GraphicsContext gc, double x, double y, double w, double h, double scale) {
        this(gc, "https://cdn.discordapp.com/attachments/801173300415037504/815023513903169546/S.C.R.U.M..gif",
             x, y, h, w, scale);
        System.out.println("default entity asset constructor called");
    }
     */
    
    //for hitbox set same as sprite explicitly
    public Entity(GraphicsContext gc, String filename, double x, double y, double w, double h, 
            double scale) {
        this.gc = gc;
        this.sprite = new Sprite(gc, filename, scale);
        this.x = x;
        this.y = y;
        this.hitboxX = x;
        this.hitboxY = y;
        this.width = w*scale;
        this.height = h*scale;
        this.hitboxWidth = width*scale;
        this.hitboxHeight = height*scale;
        this.scale = scale;
    }

    //for specifying hitbox separate from sprite
    public Entity(GraphicsContext gc, String filename, double x, double y, double w, double h, 
            double scale, double hX, double hY, double hW, double hH) {
        this(gc, filename, x, y, w, h, scale);
        this.hitboxX = hX;
        this.hitboxY = hY;
        this.hitboxHeight = hH*scale;
        this.hitboxWidth = hW*scale;
    }
    
    public void draw() {
        if(this.visible) sprite.draw(x, y);
        if(GameManager.DEBUG){
            gc.strokeRect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
        }
    }

    public void update() {
        move();
    }

    private void move() {
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisibility(boolean newVis){
        visible = newVis;
    }

    public void updateX(double amount){
        this.x += amount;
        this.hitboxX += amount;
    }

    public void updateY(double amount){
        this.y += amount;
        this.hitboxY += amount;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

    public boolean collidesWith(Entity other) {
        if(this.hitboxX+this.hitboxWidth < other.hitboxX ||
            other.hitboxX+other.hitboxWidth < this.hitboxX ||
            this.hitboxY+this.hitboxHeight < other.hitboxY ||
            other.hitboxY+other.hitboxHeight < this.hitboxY){
            return false;
        }else{
            return true;
        }
    }

    public boolean collidesWith(double x2, double y2) {
        return (x2 > (hitboxX) && x2 < (width+hitboxX)) &&
               (y2 > (hitboxY) && y2 < (height+hitboxY));
    }

    public boolean changeSprite(Image image) {
        return false;
    }

    public void updateSprite(String filename){
        this.sprite = new Sprite(gc, filename, scale);
    }

    public boolean changeSprite(Sprite newSprite) {
        return false;
    }

    public void setDimensionsToSprite(){
        Image image = sprite.getImage();
        width = image.getWidth() * Sprite.SCALE_MULTIPLIER;
        height = image.getHeight() * Sprite.SCALE_MULTIPLIER;
        hitboxWidth = width;
        hitboxHeight = height;
    }
}
