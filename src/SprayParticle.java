import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class SprayParticle extends Entity{
    private double xVelocity = 0;
    private double yVelocity = 0;
    private long timeCheck = 0;
    private long speedChangeTimeCheck = 0;
    private int activeTime;

    private static final int BASE_ACTIVE_TIME = 5000;
    private static final int SPEED_CHANGE_TIME = 100;
    
    private static final double SPEED_DIVISOR = 1.1;

    private static final String PATH = "assets/Particles/spray.png";


    public SprayParticle(GraphicsContext gc, double x, double y, double xVel, double yVel, double size, Random r) {
        super(gc, PATH, x, y, size);
        this.startParticle(xVel, yVel);
        timeCheck = System.currentTimeMillis();
        speedChangeTimeCheck = System.currentTimeMillis();
        activeTime = BASE_ACTIVE_TIME + (int)(((double)BASE_ACTIVE_TIME/2) * r.nextDouble());
    }

    private void startParticle(double xVel, double yVel){
        visible = true;
        this.xVelocity = xVel;
        this.yVelocity = yVel;
        timeCheck = 0;
    }

    public boolean isActive(){
        return visible;
    }

    public void end(){
        visible = false;
    }

    @Override
    public void update(){
        if(this.isActive()){
            if(System.currentTimeMillis() - timeCheck > activeTime){
                end(); 
            }else{
                updateX(xVelocity);
                updateY(yVelocity);
            }
            //slow down
            if(System.currentTimeMillis() - speedChangeTimeCheck > SPEED_CHANGE_TIME){
                this.xVelocity /= SPEED_DIVISOR;
                this.yVelocity /= SPEED_DIVISOR;
                speedChangeTimeCheck = System.currentTimeMillis();
            }
        }
    }
}