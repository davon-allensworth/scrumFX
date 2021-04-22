import java.util.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class Arena extends Scene {
    Player player = null;
    GraphicsContext gc;
    GameManager gm;
    List<Story> sprintBacklog;
    List<Story> activeStories;
    List<Bug> bugs = new ArrayList<>();
    List<Entity> despawnList = new ArrayList<>();
    List<Spray> sprays = new ArrayList<>();
    Random r = new Random(System.currentTimeMillis());

    //ArenaTimer arenaTimer;
    Timer timer;
    TimerTask timerTask;
    Integer timerCounter;
    Text text;
    private static final double MAX_ACTIVE_STORIES = 4;

    private long bugSpawnTimeCheck = -1;
    private int bugSpawnTime;
    private static final int BUG_SPAWN_TIME_BASE = 5000;
    private static final double BUG_SPAWN_RAND_MAX = 280;
    private static final double BUG_TIME_RAND_MAX = 4500;
    private static final double SPAWN_OFFSET = 65;

    private long itemSpawnTimeCheck = -1;
    private int itemSpawnTime;
    private static final int ITEM_SPAWN_TIME_BASE = 7000;
    private static final double ITEM_SPAWN_RAND_MAX = 180;
    private static final double ITEM_TIME_RAND_MAX = 3000;

    public Arena(Parent root, GraphicsContext gc) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.gm = GameManager.getInstance();
        this.sprintBacklog = GameManager.getInstance().sprintBacklog;
        this.activeStories = new LinkedList<>();
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
        
        for(Story story : gm.getProductBacklog()){
            story.inArena(true); //tell them they should be in arena mode
            story.updateGraphicsContext(gc);
        }

        this.sprintBacklog = gm.getSprintBacklog();
        for(Story story : sprintBacklog){
            if(activeStories.size() >= MAX_ACTIVE_STORIES){
                break; //we have reached our current max
            }
            activeStories.add(story);
        }
        
        double x = 0;
        for(Story story : activeStories){
            if(x==0) x = (screenWidth/activeStories.size()-story.getWidth())/2;
            story.setLocation(x, screenHeight-story.getHeight());
            story.startProgress();
            this.entities.add(story);
            x += screenWidth/activeStories.size();
        }

        for(Bug bug : bugs){
            this.entities.add(bug);
            bug.startMoving();
        }
 
        timerCounter = 60; //Will change this later
        timer = new Timer();
        text = new Text();
        timerTask = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    timerCounter--;
                    if(timerCounter == 0 || gm.storiesDone()){
                        timer.cancel();
                        timerTask.cancel();
                        gm.endSprint();
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
        player = new Player(gc);
        this.entities.add(player);
    }

    @Override
    public void teardown() {
        player.stopPrespraySound();
    }

    @Override
    public void update() {

        updateEntities();

        //despawn things
        for(Entity e : despawnList){
            if(e instanceof SprayParticle){
                Objects.requireNonNull(player.getParticles()).remove(e);
            }else{
                entities.remove(e);
            }
        }

        //check for completed stories
        for(int i = 0; i < activeStories.size(); i++){
            if(activeStories.get(i).shouldSwitchOut()){
                for(Story story : sprintBacklog){ //fill in the stories
                    if(!activeStories.contains(story) && !story.isCompleted()){
                        Story removedStory = activeStories.remove(i);//remove completed story
                        entities.remove(removedStory);
                        double removedStoryX = removedStory.x;
                        double removedStoryY = removedStory.y;
                        story.setLocation(removedStoryX, removedStoryY);
                        activeStories.add(i, story);//add new story
                        entities.add(story);
                        story.inArena(true);
                        story.startProgress();
                    }
                }
            }
        }

        //spawn bugs
        spawnBugs();

        //spawn items
        spawnItems();
    }

    private void spawnItems() {
        if (noItemBeenSpawned()) {
            setInitialItemSpawnTime();
        } else if (itemCanSpawn()) {
            spawnItem();
        }
    }

    private void spawnItem() {
        double screenWidth = gc.getCanvas().getWidth();
        double centerx = screenWidth / 2;
        double randomDouble = getRand(ITEM_SPAWN_RAND_MAX);
        double spawnX = centerx + randomDouble - SPAWN_OFFSET;

        Spray spray = new Spray(gc, spawnX);
        this.entities.add(spray);
        spray.startMoving();

        randomDouble = getRand(ITEM_TIME_RAND_MAX);
        itemSpawnTime = (int) (ITEM_SPAWN_TIME_BASE + randomDouble) * 4;
        resetItemSpawnTimeCheck();
    }

    private void setInitialItemSpawnTime() {
        double randomDouble = getRand(ITEM_TIME_RAND_MAX);
        itemSpawnTime = (int) (ITEM_SPAWN_TIME_BASE + randomDouble) / 2;
        resetItemSpawnTimeCheck();
    }

    private void resetItemSpawnTimeCheck() {
        itemSpawnTimeCheck = System.currentTimeMillis();
    }

    private double getRand(double itemTimeRandMax) {
        return (-itemTimeRandMax) + (itemTimeRandMax - (-itemTimeRandMax)) * r.nextDouble();
    }

    private boolean noItemBeenSpawned() {
        return itemSpawnTimeCheck < 0;
    }

    private boolean itemCanSpawn() {
        return System.currentTimeMillis() - itemSpawnTimeCheck > itemSpawnTime;
    }


    private void spawnBugs() {
        if (noBugBeenSpawned()) {
            setBugSpawnTime();
        } else if (bugCanSpawn()) {
            spawnBug();
        }
    }

    private void spawnBug() {
        double screenWidth = gc.getCanvas().getWidth();
        double centerx = screenWidth / 2;
        double randomDouble = getRand(BUG_SPAWN_RAND_MAX);
        double spawnX = centerx + randomDouble - SPAWN_OFFSET;

        int randomLevel = sprintBacklog.get(r.nextInt(sprintBacklog.size())).getLevel();

        Bug bug = new Bug(gc, spawnX, randomLevel);
        this.entities.add(bug);
        bug.startMoving();

        setBugSpawnTime();
    }

    private void setBugSpawnTime() {
        double randomDouble = getRand(BUG_TIME_RAND_MAX);
        bugSpawnTime = (int) (BUG_SPAWN_TIME_BASE + randomDouble);
        if (!activeStories.isEmpty())
            bugSpawnTime /= sprintBacklog.size();
        bugSpawnTimeCheck = System.currentTimeMillis();
    }

    private boolean noBugBeenSpawned() {
        return bugSpawnTimeCheck < 0;
    }

    private boolean bugCanSpawn() {
        return System.currentTimeMillis() - bugSpawnTimeCheck > bugSpawnTime;
    }

    private void updateEntities() {
        for (Entity e : entities) {
            e.update();
            if (e instanceof Bug) {
                handleBugCollision((Bug) e);
                handleBugDespawn((Bug) e);
            } else if (e instanceof Player) {
                handlePlayerCollision((Player) e);
            }
        }
    }

    private void handleBugDespawn(Bug bug) {
        if (bug.shouldDespawn()) {
            despawnList.add(bug);
        }
    }

    private void handlePlayerCollision(Player e) {
        for (Entity other : entities) {
            if (other instanceof Spray) {
                handlePlayerSprayCollision(e, (Spray) other);
            }
        }
    }

    private void handlePlayerSprayCollision(Player player, Spray spray) {
        if (playerCanPickUpSpray(player, spray)) {
            player.equipSpray();
            spray.stop();
        }
    }

    private boolean playerCanPickUpSpray(Player player, Spray spray) {
        return player.moveCode() != Player.SWAT_CODE && player.moveCode() != Player.PRESWAT_CODE && spray.isActive()
                && player.collidesWith(spray);
    }

    private boolean bugCanCollide(Bug bug) {
        return bug.isAlive() && !bug.isAbsorbing();
    }

    private void handleBugCollision(Bug bug) {
        if (!bugCanCollide(bug)) {
            return;
        }
        handleBugSwatterCollision(bug);
        handleBugParticleCollision(bug);
        for (Entity other : entities) {
            handleBugStoryCollision(bug, other);
        }
    }

    private void handleBugParticleCollision(Entity e) {
        if (player.getParticles() == null) {
            return;
        }

        for (SprayParticle particle : player.getParticles()) {
            if (!particle.isActive()) {
                despawnList.add(particle);
            } else if (particle.collidesWith(e)) {
                ((Bug) e).inSpray();
            }
        }
    }

    private void handleBugSwatterCollision(Entity e) {
        if (player.getSwatter() != null && !player.hasSpray()) {
            if (player.moveCode() == Player.SWAT_CODE && e.collidesWith(player.getSwatter())) {
                ((Bug) e).kill();
            }
        }
    }

    private void handleBugStoryCollision(Entity e, Entity other) {
        if (other instanceof Story) {
            if (e.collidesWith(other)) {
                ((Bug) e).startAbsorb();
                ((Story) other).hit();
            }
        }
    }

    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            if(!(e instanceof Player)) e.draw();  
        player.draw(); //draw player on top
        gc.fillText(timerCounter.toString(), 10, 20);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(GameManager.getTextColor());
    }
}