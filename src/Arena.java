import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Arena extends Scene {
    Player player = null;
    GraphicsContext gc;
    GameManager gm;
    List<Story> sprintBacklog;
    List<Bug> bugs = new ArrayList<>();
    List<Spray> sprays = new ArrayList<>();
    Random r = new Random();

    private long bugSpawnTimeCheck = -1;
    private int bugSpawnTime;
    private static final int BUG_SPAWN_TIME_BASE = 3000;
    private static final double BUG_SPAWN_RAND_MAX = 150;
    private static final double BUG_TIME_RAND_MAX = 3000;

    private long itemSpawnTimeCheck = -1;
    private int itemSpawnTime;
    private static final int ITEM_SPAWN_TIME_BASE = 7000;
    private static final double ITEM_SPAWN_RAND_MAX = 150;
    private static final double ITEM_TIME_RAND_MAX = 3000;

    public Arena(Parent root, GraphicsContext gc, GameManager gm) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.sprintBacklog = gm.sprintBacklog;
        this.gm = gm;
        this.bugSpawnTime = BUG_SPAWN_TIME_BASE;

        this.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    KeyCode keyCode = e.getCode();
                    if(player.moveCode() != Player.SWAT_CODE &&
                            keyCode == KeyCode.SPACE && player.moveCode() != Player.PRESWAT_CODE){
                        player.preswat();
                    }else if(player.moveCode() != Player.PRESWAT_CODE && 
                            player.moveCode() != Player.SWAT_CODE &&
                            player.moveCode() != Player.LEFT_CODE &&
                            keyCode == KeyCode.LEFT){
                        player.moveLeft();
                    }else if(player.moveCode() != Player.PRESWAT_CODE &&
                            player.moveCode() != Player.SWAT_CODE &&
                            player.moveCode() != Player.RIGHT_CODE &&
                            keyCode == KeyCode.RIGHT){
                        player.moveRight();
                    }
                }
            });

            this.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        KeyCode keyCode = e.getCode();
                        if(keyCode == KeyCode.SPACE && player.moveCode() == Player.PRESWAT_CODE){
                            player.swat();
                        }else if((keyCode == KeyCode.LEFT && player.moveCode() == -1) ||
                            (keyCode == KeyCode.RIGHT && player.moveCode() == 1)){
                            player.idle();
                        }
                    }
                });
    }
    
    @Override
    public void setup() {
        double screenWidth = gc.getCanvas().getWidth();
        double screenHeight = gc.getCanvas().getHeight();
        
        player = new Player(gc);

        sprintBacklog = gm.getSprintBacklog();

        double x = 0;
        for(Story story : sprintBacklog){
            if(x==0) x = (screenWidth/sprintBacklog.size()-story.getWidth());
            story.setLocation(x, screenHeight-story.getHeight());
            story.startProgress();
            this.entities.add(story);
            x += screenWidth/sprintBacklog.size();
        }

        this.entities.add(player);
    }

    @Override
    public void teardown() {
        
    }

    @Override
    public void update() {

        // update entities and check for collisions
        for(Entity e : entities){
            e.update();
            if(e instanceof Bug && ((Bug)e).isAlive()){ //check for bug collisions
                for(Entity other : entities){
                    if(other instanceof Story){ //with story
                        if(e.collidesWith(other)){
                            ((Bug)e).startAbsorb();
                            ((Story)other).hit();
                        }
                    }
                    // check if bug is hit by swatter
                    if(player.getSwatter() != null && !player.hasSpray()){
                        if(player.moveCode() == Player.SWAT_CODE && e.collidesWith(player.getSwatter())){
                            ((Bug)e).kill();
                        }
                    }
                    // check if bug hit by particle
                    if(player.getParticles() != null){
                        for(SprayParticle particle : player.getParticles()){
                            if(particle.isActive() && particle.collidesWith(particle)){
                                ((Bug)e).kill();
                            }
                        }
                    }
                }
            }else if(e instanceof Player){
                for(Entity other : entities){
                    //don't equip spray unless you are done swatting
                    if(((Player)e).moveCode() != Player.SWAT_CODE && ((Player)e).moveCode() != Player.PRESWAT_CODE){
                        if(other instanceof Spray){ //with spray
                            if(e.collidesWith(other)){
                                ((Player)e).equipSpray();
                                ((Spray)other).stop();
                            }
                        }
                    }
                }
            }
        }

        // check if the sprint is over
        if(gm.storiesDone()){
            gm.endSprint();
        }

        //spawn bugs
        if(bugSpawnTimeCheck < 0){
            double randomDouble = ((-BUG_TIME_RAND_MAX) + (BUG_TIME_RAND_MAX - (-BUG_TIME_RAND_MAX)) * r.nextDouble());
            bugSpawnTime = (int)(BUG_SPAWN_TIME_BASE + randomDouble);
            bugSpawnTimeCheck = System.currentTimeMillis();
        }else if(System.currentTimeMillis() - bugSpawnTimeCheck > bugSpawnTime){
            double screenWidth = gc.getCanvas().getWidth();
            double centerx = (double)screenWidth / 2;

            double randomDouble = (-BUG_SPAWN_RAND_MAX) + (BUG_SPAWN_RAND_MAX - (-BUG_SPAWN_RAND_MAX)) * r.nextDouble();
            double spawnX = centerx + randomDouble;

            Bug bug = new Bug(gc, spawnX);

            this.entities.add(bug);
            bug.startMoving();

            randomDouble = ((-BUG_TIME_RAND_MAX) + (BUG_TIME_RAND_MAX - (-BUG_TIME_RAND_MAX)) * r.nextDouble());
            bugSpawnTime = (int)(BUG_SPAWN_TIME_BASE + randomDouble);
            bugSpawnTimeCheck = System.currentTimeMillis();
        }

        //spawn items
        if(itemSpawnTimeCheck < 0){
            double randomDouble = ((-ITEM_TIME_RAND_MAX) + (ITEM_TIME_RAND_MAX - (-ITEM_TIME_RAND_MAX)) * r.nextDouble());
            itemSpawnTime = (int)(ITEM_SPAWN_TIME_BASE + randomDouble) / 2;
            itemSpawnTimeCheck = System.currentTimeMillis();
        }else if(System.currentTimeMillis() - itemSpawnTimeCheck > itemSpawnTime){
            double screenWidth = gc.getCanvas().getWidth();
            double centerx = (double)screenWidth / 2;

            double randomDouble = (-ITEM_SPAWN_RAND_MAX) + (ITEM_SPAWN_RAND_MAX - (-ITEM_SPAWN_RAND_MAX)) * r.nextDouble();
            double spawnX = centerx + randomDouble;

            Spray spray = new Spray(gc, spawnX);

            this.entities.add(spray);
            spray.startMoving();

            randomDouble = ((-ITEM_TIME_RAND_MAX) + (ITEM_TIME_RAND_MAX - (-ITEM_TIME_RAND_MAX)) * r.nextDouble());
            itemSpawnTime = (int)(ITEM_SPAWN_TIME_BASE + randomDouble) * 4;
            itemSpawnTimeCheck = System.currentTimeMillis();
        }
    }

    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            if(!(e instanceof Player)) e.draw();  
        player.draw(); //draw player on top
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }
}