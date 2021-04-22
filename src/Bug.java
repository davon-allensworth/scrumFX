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
    private boolean inSpray = false;
    private long sprayTimeCheck = 0; //to make bug die in spray slower

    private static final int DESPAWN_TIME = 10000;
    private static final int ABSORB_TIME = 100;
    private static final int SPRAY_DEATH_TIME = 1000;

    private static final double IN_SPRAY_SPEED_RECUCTION = 8;

    private static final String MOVE = "assets/Bugs/bug move ";
    private static final String DEAD = "assets/Bugs/bug splatter ";
    private static final String DEAD2 = "assets/Bugs/buggy dead ";
    private static final String ABSORB = "assets/Bugs/bug absorb ";

    private static final Sound absorbSound = new Sound("assets/sounds/bug absorb.wav", false);

    private static Sprite[] deadSprite;
    private static Sprite[] deadSprite2;

    private int absorbState = 0;

    public Bug(GraphicsContext gc, double x, int type) {
        super(gc, MOVE+type+".gif", x, -300, 1);
        this.type = type;

        //it helps runtime to have some sprites be static
        if(deadSprite == null || deadSprite[type] != null && deadSprite[type].gc != gc) deadSprite = new Sprite[4];
        if(deadSprite[type] == null) deadSprite[type] = new Sprite(gc, DEAD+type+".png");

        if(deadSprite2 == null || deadSprite2[type] != null && deadSprite2[type].gc != gc) deadSprite2 = new Sprite[4];
        if(deadSprite2[type] == null) deadSprite2[type] = new Sprite(gc, DEAD2+type+".png");
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
        if(inSpray) this.updateSprite(deadSprite2[type]);
        else this.updateSprite(deadSprite[type]);
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
        absorbSound.stop();
        absorbSound.play();
    }

    public void inSpray(){
        if(!inSpray)sprayTimeCheck = System.currentTimeMillis();
        inSpray = true;
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

    public boolean canCollide() {
        return isAlive() && !isAbsorbing();
    }

    public void handleSwatterCollision(Player player) {
        if (player.getSwatter() != null && !player.hasSpray()) {
            if (player.moveCode() == Player.SWAT_CODE && collidesWith(player.getSwatter())) {
                kill();
            }
        }
    }

    public void handleStoryCollision(Entity other) {
        if (other instanceof Story) {
            if (collidesWith(other)) {
                startAbsorb();
                ((Story) other).hit();
            }
        }
    }

    @Override
    public void update(){
        if(startAbsorb) absorb(); 
        else if(move && alive){
            if(inSpray){
                updateY((velocity*type)/IN_SPRAY_SPEED_RECUCTION);
            }else{
                updateY(velocity*type);
            }
            //check for spray death
            if(inSpray && System.currentTimeMillis() - sprayTimeCheck > SPRAY_DEATH_TIME){
                this.kill();
            }
        }else{
            if(despawnTimeCheck < 0){
                despawnTimeCheck = System.currentTimeMillis();
            }else if(System.currentTimeMillis() - despawnTimeCheck > DESPAWN_TIME){
                this.despawn = true;
            }
        }
    }
}
