import java.util.LinkedList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity {
    private int moveCode = 0; // 0 for idle, -1 for left, 1 for right, 2 for preswat, 3 for swat
    private long timeCheck = -1; //to make swat last a while
    private boolean spray = false;
    private double sprayPower = 1;
    private SprayParticleGenerator sprayer = null;
    private Swatter swatter = null;
    private long powerTimeCheck = -1;

    private static final int POWER_TIME = 100;
    private static final double POWER_MAX = 3;
    private static final double POWER_INCREASE = 0.1;
    
    public static final int IDLE_CODE = 0;
    public static final int LEFT_CODE = -1;
    public static final int RIGHT_CODE = 1;
    public static final int PRESWAT_CODE = 2;
    public static final int SWAT_CODE = 3;

    private static final String IDLE = "assets/Player/player idle.png";
    private static final String MOVE_LEFT = "assets/Player/player move left.gif";
    private static final String MOVE_RIGHT = "assets/Player/player move right.gif";
    private static final String PRESWAT = "assets/Player/player preswat.gif";
    private static final String SWAT = "assets/Player/player swat.png";

    private static final String IDLE_SPRAY = "assets/Player/player idle spray.png";
    private static final String MOVE_LEFT_SPRAY = "assets/Player/player move left spray.gif";
    private static final String MOVE_RIGHT_SPRAY = "assets/Player/player move right spray.gif";
    private static final String PRESWAT_SPRAY = "assets/Player/player preswat spray.gif";
    private static final String SWAT_SPRAY = "assets/Player/player swat spray.png";

    private static final int SWAT_TIME = 500;
    private static final double VELOCITY = 6;
    private static final double PLAYER_WIDTH = 46;
    private static final double PLAYER_HEIGHT = 32;
    private static final double PLAYER_HITBOX_OFFSET_X = 90;
    private static final double PLAYER_HITBOX_OFFSET_Y = 200;
    private static final double PLAYER_HITBOX_WIDTH = 143;
    private static final double PLAYER_HITBOX_HEIGHT = 38;
    private static final double PLAYER_Y_OFFSET = 120;

    public Player(GraphicsContext gc) {
        this(gc, gc.getCanvas().getWidth()/2, gc.getCanvas().getHeight() / 4);
    }

    private Player(GraphicsContext gc, double x, double y){
        super(gc, IDLE, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, 1, 
            x + PLAYER_HITBOX_OFFSET_X, y + PLAYER_HITBOX_OFFSET_Y,
            PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT);

        swatter = new Swatter(this.gc, x, y);
    }

    public List<SprayParticle> getParticles(){
        if(sprayer == null) return new LinkedList<>();
        return sprayer.getParticles();
    }

    public Swatter getSwatter(){
        return swatter;
    }

    @Override
    public void draw() {
        if(sprayer != null){
            for(SprayParticle particle : sprayer.getParticles()){
                particle.draw();
            }
        }
        if(swatter != null){
            swatter.draw();
        }
        super.draw();
    }

    public void idle(){
        moveCode = IDLE_CODE;
        if(spray) this.updateSprite(IDLE_SPRAY);
        else this.updateSprite(IDLE);
    }

    public void moveLeft(){
        moveCode = -1;
        if(spray) this.updateSprite(MOVE_LEFT_SPRAY);
        else this.updateSprite(MOVE_LEFT);
    }

    public void moveRight(){
        moveCode = 1;
        if(spray) this.updateSprite(MOVE_RIGHT_SPRAY);
        else this.updateSprite(MOVE_RIGHT);
    }

    public void preswat(){
        moveCode = 2;
        if(spray) this.updateSprite(PRESWAT_SPRAY);
        else this.updateSprite(PRESWAT);
    }

    public void swat(){
        moveCode = 3;
        if(spray){
            powerTimeCheck = -1;
            this.updateSprite(SWAT_SPRAY);
            sprayer = new SprayParticleGenerator(this.gc, x, y, sprayPower);
        }
        else this.updateSprite(SWAT);
        sprayPower = 1;
    }

    public int moveCode(){ // 0 for idle, -1 for left, 1 for right, 2 for preswat, 3 for swat
        return moveCode;
    }

    public void equipSpray(){
        spray = true;
        //force update for animation
        if(moveCode == Player.IDLE_CODE) idle();
        else if(moveCode == Player.LEFT_CODE) moveLeft();
        else if(moveCode == Player.RIGHT_CODE) moveRight();
    }

    public void unequipSpray(){
        spray = false;
    }

    public boolean hasSpray(){
        return spray;
    }

    @Override
    public void update(){
        if(moveCode == LEFT_CODE && hitboxX > 0){ //move left
            updateX(-VELOCITY);
        }else if(moveCode == RIGHT_CODE && hitboxX < (gc.getCanvas().getWidth()-hitboxWidth)){ //move right
            updateX(VELOCITY);
        }else if (moveCode == SWAT_CODE){
            if(timeCheck == -1) timeCheck = System.currentTimeMillis();
            if(System.currentTimeMillis() - timeCheck > SWAT_TIME){ //if 1 second passes
                timeCheck = -1;
                unequipSpray(); //unequip spray after spraying
                idle();
            }
        }

        //power up the spray
        if(moveCode == PRESWAT_CODE && spray && sprayPower < POWER_MAX){
            if(powerTimeCheck < 0){
                powerTimeCheck = System.currentTimeMillis();
            }else if(System.currentTimeMillis() - powerTimeCheck > POWER_TIME){
                this.sprayPower += POWER_INCREASE;
                powerTimeCheck = System.currentTimeMillis();
            }
        }

        if(sprayer != null){
            sprayer.update();
        }

        if(swatter != null){
            swatter.setPosition(x, y);
        }
    }
}