import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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

        this.setOnKeyPressed(e -> {
            player.keyPressed(e.getCode());
        });

        this.setOnKeyReleased(e -> {
            player.keyReleased(e.getCode());
        });
    }

    @Override
    public void setup() {
        updateProductBacklog();
        initActiveStories();
        initBugs();
        initTimer();
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
                        gm.endSprint();
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

        this.sprintBacklog = gm.getSprintBacklog();
        for (Story story : sprintBacklog) {
            if (activeStories.size() >= MAX_ACTIVE_STORIES) {
                break;
            }
            activeStories.add(story);
        }

        double x = 0;
        for (Story story : activeStories) {
            if (x == 0)
                x = (screenWidth / activeStories.size() - story.getWidth()) / 2;
            story.setLocation(x, screenHeight - story.getHeight());
            story.startProgress();
            this.entities.add(story);
            x += screenWidth / activeStories.size();
        }
    }

    private void updateProductBacklog() {
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
        updateEntities();
        despawnEntities();
        updateActiveStories();
        spawnBugs();
        spawnItems();
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
        super.drawBackground(gc);
        for (Entity e : this.entities)
            if (!(e instanceof Player))
                e.draw();
        player.draw();
        gc.fillText(timerCounter.toString(), 10, 20);
    }
}