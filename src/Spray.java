import javafx.scene.canvas.GraphicsContext;

public class Spray extends Entity{
    private boolean move = false;
    private double velocity = 2;

    private static final int END_ZONE = 1000;

    private static final String PATH = "assets/PowerUps/bug spray.png";

    public Spray(GraphicsContext gc, double x) {
        super(gc, PATH, x, -300, 1);
    }

    public void startMoving(){
        if(visible) move = true;
    }

    public void stopMoving(){
        move = false;
    }

    public void stop(){
        move = false;
        this.setVisibility(false);
    }

    public boolean isActive(){
        return visible;
    }

    @Override
    public void update(){
        if(y > END_ZONE) stop();
        else if(visible && move) updateY(velocity);
    }
}