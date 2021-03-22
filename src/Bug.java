import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import javafx.scene.canvas.GraphicsContext;

public class Bug extends Entity{
    public boolean alive = true;
    private boolean move = false;
    private boolean startAbsorb = false;
    private double velocity = 3;
    private long timeCheck = 0; //to make absorb animate slowly

    private double endzone;

    private static final int ABSORB_TIME = 100;

    private static final String MOVE = "assets/Bugs/bug move.gif";
    private static final String DEAD = "assets/Bugs/bug splatter.gif";
    private static final String ABSORB = "assets/Bugs/bug absorb ";

    private int absorbState = 0;

    public Bug(GraphicsContext gc, double x) {
        super(gc, MOVE, x, -300, 1);
        endzone = gc.getCanvas().getHeight() - 500;
    }

    public void startMoving(){
        if(alive) move = true;
    }

    public void stopMoving(){
        move = false;
    }

    public void kill(){
        move = false;
        alive = false;
        this.updateSprite(DEAD);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public boolean isAlive() {
        return alive;
    }

    public void startAbsorb(){
        this.startAbsorb = true;
    }

    private void absorb(){
        if(System.currentTimeMillis() - timeCheck > ABSORB_TIME){
            if(absorbState > 4){
                this.getSprite().visible = false;
                this.alive = false;
            }else{
                this.updateSprite(ABSORB+absorbState+".png");
            }
            stopMoving();
            absorbState++;
            timeCheck = System.currentTimeMillis();
        }
    }

    @Override
    public void update(){
        if(startAbsorb) absorb(); 
        else if(move && alive) updateY(velocity);
    }
}
