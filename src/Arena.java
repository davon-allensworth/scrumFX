import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.*;

public class Arena extends Scene {
    private Player player = null;
    private GraphicsContext gc;
    private GameManager gm;
    private List<Story> sprintBacklog;
    private List<Story> activeStories;
    private List<Bug> bugs = new ArrayList<>();
    private List<Entity> despawnList = new ArrayList<>();
    private Random r = new Random(System.currentTimeMillis());
    private Timer timer;
    private TimerTask timerTask;
    private Integer timerCounter;

    private Entity overText;
    private long overTextTimeCheck = -1;
    private static final int OVER_TEXT_TIME = 2000;

    private Entity startText;
    private long startTextTimeCheck = -1;
    private static final int START_TEXT_TIME = 2000;
    
    private static final int SPRINT_TIMER = 60;
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
        super(root, Color.LIGHTSKYBLUE);
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.gm = GameManager.getInstance();
        this.sprintBacklog = GameManager.getInstance().sprintBacklog;
        this.activeStories = new LinkedList<>();
        this.bugSpawnTime = BUG_SPAWN_TIME_BASE;

        this.setOnKeyPressed(e -> player.keyPressed(e.getCode()));
        this.setOnKeyReleased(e -> player.keyReleased(e.getCode()));
    }

    @Override
    public void setup() {
        notifyStoriesInProdBacklog();
        initActiveStories();
        initBugs();
        initTimer();

        startText = new Entity(gc, "assets/stories/other/sprint start.png", 0, 0, 1);
        startText.updateY((double)(gc.getCanvas().getHeight()) / 8); //move down screen
        entities.add(startText);
        startTextTimeCheck = System.currentTimeMillis();

        overText = new Entity(gc, "assets/stories/other/sprint over.png", 0, 0, 1);
        overText.updateY((double)(gc.getCanvas().getHeight()) / 8); //move down screen

        player = new Player(gc);
        this.entities.add(player);
    }

    private void initTimer() {
        timerCounter = SPRINT_TIMER;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timerCounter--;
                    if (timerCounter == 0 || gm.storiesDone()) {
                        timer.cancel();
                        timerTask.cancel();

                        //display over text
                        entities.add(overText);
                        overTextTimeCheck = System.currentTimeMillis();
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    private void initBugs() {
        for (Bug bug : bugs) {
            this.entities.add(bug);
            bug.startMoving();
        }
    }

    private void initActiveStories() {
        double screenWidth = gc.getCanvas().getWidth();
        double screenHeight = gc.getCanvas().getHeight();
        double x = 0;

        populateActiveStories();
        for (Story story : activeStories) {
            if (x == 0)
                x = (screenWidth / activeStories.size() - story.getWidth()) / 2;
            story.setLocation(x, screenHeight - story.getHeight());
            story.startProgress();
            this.entities.add(story);
            x += screenWidth / activeStories.size();
        }
    }

    private void populateActiveStories() {
        this.sprintBacklog = gm.getSprintBacklog();
        for (Story story : sprintBacklog) {
            if (activeStories.size() >= MAX_ACTIVE_STORIES) {
                break;
            }
            activeStories.add(story);
        }
    }

    private void notifyStoriesInProdBacklog() {
        for (Story story : gm.getProductBacklog()) {
            story.inArena(true);
            story.updateGraphicsContext(gc);
        }
    }

    @Override
    public void teardown() {
        player.stopPrespraySound();
    }

    @Override
    public void update() {
        if(overTextTimeCheck > 0){ //next scene after displaying text
            if(System.currentTimeMillis() - overTextTimeCheck > OVER_TEXT_TIME){
                gm.endSprint();
            }
        }else{ //otherwise just update everything
            updateEntities();
            despawnEntities();
            updateActiveStories();
            spawnBugs();
            spawnItems();
        }
    }

    private void despawnEntities() {
        for (Entity e : despawnList) {
            if (e instanceof SprayParticle) {
                Objects.requireNonNull(player.getParticles()).remove(e);
            } else {
                entities.remove(e);
            }
        }
    }

    private void updateActiveStories() {
        for (int i = 0; i < activeStories.size(); i++) {
            if (activeStories.get(i).shouldSwitchOut()) {
                attemptToSwitchStory(i);
            }
        }
    }

    private void attemptToSwitchStory(int i) {
        for (Story story : sprintBacklog) {
            if (canBeSwappedIn(story)) {
                swapStory(i, story);
            }
        }
    }

    private void swapStory(int i, Story story) {
        Story removedStory = activeStories.remove(i);
        entities.remove(removedStory);
        double removedStoryX = removedStory.x;
        double removedStoryY = removedStory.y;
        story.setLocation(removedStoryX, removedStoryY);
        activeStories.add(i, story);
        entities.add(story);
        story.inArena(true);
        story.startProgress();
    }

    private boolean canBeSwappedIn(Story story) {
        return !activeStories.contains(story) && !story.isCompleted();
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
        if(startText != null){
            if(System.currentTimeMillis() - startTextTimeCheck > START_TEXT_TIME){
                entities.remove(startText);
                startText = null;
            }
        }
    }

    private void handleBugDespawn(Bug bug) {
        if (bug.shouldDespawn()) {
            despawnList.add(bug);
        }
    }

    private void handlePlayerCollision(Player p) {
        for (Entity other : entities) {
            p.handleCollision(other);
        }
    }

    private void handleBugCollision(Bug bug) {
        if (!bug.canCollide()) {
            return;
        }
        bug.handleSwatterCollision(player);
        handleBugParticleCollision(bug);
        for (Entity other : entities) {
            bug.handleStoryCollision(other);
        }
    }

    private void handleBugParticleCollision(Bug bug) {
        if (player.getParticles() == null) {
            return;
        }

        for (SprayParticle particle : player.getParticles()) {
            if (!particle.isActive()) {
                despawnList.add(particle);
            } else if (particle.collidesWith(bug)) {
                bug.inSpray();
            }
        }
    }

    @Override
    public void draw() {
        super.drawBackground(gc);
        for (Entity e : this.entities)
            if (!(e instanceof Player))
                e.draw();
        player.draw();
        gc.fillText(timerCounter.toString(), 10, 20);
    }
}