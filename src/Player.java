import javafx.scene.canvas.GraphicsContext;

public class Player extends Entity {
    private int moveCode = 0; // 0 for idle, -1 for left, 1 for right, 2 for preswat, 3 for swat
    private long timeCheck = -1; //to make swat last a while

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

    private static final int SWAT_TIME = 500;
    private static final double VELOCITY = 6;
    private static final double PLAYER_WIDTH = 46;
    private static final double PLAYER_HEIGHT = 32;
    private static final double PLAYER_HITBOX_OFFSET_X = 160;
    private static final double PLAYER_HITBOX_OFFSET_Y = 105;
    private static final double PLAYER_HITBOX_WIDTH = 288;
    private static final double PLAYER_HITBOX_HEIGHT = 32;

    public Player(GraphicsContext gc, double x, double y) {
        super(gc, IDLE, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, 1, 
                x + PLAYER_HITBOX_OFFSET_X, y + PLAYER_HITBOX_OFFSET_Y,
                PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT);
    }

    @Override
    public void draw() {
        super.draw();
    }

    public void idle(){
        moveCode = IDLE_CODE;
        this.updateSprite(IDLE);
    }

    public void moveLeft(){
        moveCode = -1;
        this.updateSprite(MOVE_LEFT);
    }

    public void moveRight(){
        moveCode = 1;
        this.updateSprite(MOVE_RIGHT);
    }

    public void preswat(){
        moveCode = 2;
        this.updateSprite(PRESWAT);
    }

    public void swat(){
        moveCode = 3;
        this.updateSprite(SWAT);
    }

    public int moveCode(){ // 0 for idle, -1 for left, 1 for right, 2 for preswat, 3 for swat
        return moveCode;
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
                idle();
            }
        }
    }
}