import javafx.scene.canvas.GraphicsContext;

public class Bug extends Entity{
    public boolean alive = true;
    private boolean move = false;
    private boolean startAbsorb = false;
    private double velocity = 2;
    private long timeCheck = 0; //to make absorb animate slowly
    private int type;
    private boolean despawn = false;
    private long despawnTimeCheck = -1;

    private static final int DESPAWN_TIME = 10000;
    private static final int ABSORB_TIME = 100;

    private static final String MOVE = "assets/Bugs/bug move ";
    private static final String DEAD = "assets/Bugs/bug splatter ";
    private static final String ABSORB = "assets/Bugs/bug absorb ";

    private static Sprite[] deadSprite;

    private int absorbState = 0;

    public Bug(GraphicsContext gc, double x, int type) {
        super(gc, MOVE+type+".gif", x, -300, 1);
        this.type = type;

        //it helps runtime to have some sprites be static
        if(deadSprite == null || deadSprite[type] != null && deadSprite[type].gc != gc) deadSprite = new Sprite[4];
        if(deadSprite[type] == null) deadSprite[type] = new Sprite(gc, DEAD+type+".png");
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
        this.updateSprite(deadSprite[type]);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isAbsorbing(){
        return startAbsorb;
    }

    public void startAbsorb(){
        this.startAbsorb = true;
    }

    private void absorb(){
        if(System.currentTimeMillis() - timeCheck > ABSORB_TIME){
            if(absorbState > 4){
                this.despawn = true;
            }else{
                this.updateSprite(ABSORB+absorbState+".png");
            }
            stopMoving();
            absorbState++;
            timeCheck = System.currentTimeMillis();
        }
    }

    public boolean shouldDespawn(){
        return this.despawn;
    }

    @Override
    public void update(){
        if(startAbsorb) absorb(); 
        else if(move && alive) updateY(velocity*type);
        else{
            if(despawnTimeCheck < 0){
                despawnTimeCheck = System.currentTimeMillis();
            }else if(System.currentTimeMillis() - despawnTimeCheck > DESPAWN_TIME){
                this.despawn = true;
            }
        }
    }
}
