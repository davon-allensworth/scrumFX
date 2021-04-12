import javafx.scene.canvas.GraphicsContext;

public class Swatter extends Entity{
    private static final String PATH = "assets/Player/player idle.png"; //not necessary cause its invisible
    private static final double SWATTER_HITBOX_OFFSET_X = 125;
    private static final double SWATTER_HITBOX_OFFSET_Y = 10;
    private static final double SWATTER_HITBOX_WIDTH = 100;
    private static final double SWATTER_HITBOX_HEIGHT = 100;
    private static final double SWATTER_Y_OFFSET = 120;

    public Swatter(GraphicsContext gc, double x, double y) {
        super(gc, PATH, x, y, 1, 1, 1, 
            x + SWATTER_HITBOX_OFFSET_X, y + SWATTER_HITBOX_OFFSET_Y,
            SWATTER_HITBOX_WIDTH, SWATTER_HITBOX_HEIGHT);

        this.setVisibility(false);
    }

    public void setPosition(double newX, double newY){
        this.x = newX + SWATTER_HITBOX_OFFSET_X;
        this.hitboxX = newX + SWATTER_HITBOX_OFFSET_X;
        this.y = newY + SWATTER_HITBOX_OFFSET_Y;
        this.hitboxY = newY + SWATTER_HITBOX_OFFSET_Y;
    }
}
