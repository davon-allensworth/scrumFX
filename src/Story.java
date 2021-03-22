import javafx.scene.canvas.GraphicsContext;

public class Story extends Entity {
    String storyText = null;
    private int completion;
    private boolean green = true;
    private boolean completed = false;
    private boolean progress = true; //should the progress bar go up
    private boolean inArena = true; //is the story in the arena
    private long timeCheck = 0; //to make progress bar move slowly
    private int level;

    private static final int PROGRESS_TIME = 5000;

    private static final String ASSET_PATH = "assets/stories/";
    private String levelPath;
    private static final String FILE_EXT = ".png";

    public Story(GraphicsContext gc, String text, int level,
                  double x, double y) {
        super(gc, ASSET_PATH+"level "+level+"/story "+level+FILE_EXT, x, y, 1);
        this.level = level;
        this.levelPath = "level "+level+"/story "+level;
        storyText = text;
        completion = 0;
    }

    @Override
    public void draw() {
        super.draw();
    }

    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
        this.hitboxX = x;
        this.hitboxY = y;
    }

    public void select(){
        this.updateSprite(ASSET_PATH+levelPath+" selected"+FILE_EXT);
    }

    public void deselect(){
        this.updateSprite(ASSET_PATH+levelPath+FILE_EXT);
    }

    public void startProgress(){
        timeCheck = System.currentTimeMillis();
        progress = true;
        arenaMode();
    }

    public void endProgress(){
        progress = false;
    }

    public void arenaMode(){
        if(green){
            this.updateSprite(ASSET_PATH+levelPath+" green "+completion+FILE_EXT);
        }else{
            this.updateSprite(ASSET_PATH+levelPath+" red "+completion+FILE_EXT);
        }
    }

    public void increase(){
        if(!completed){
            completion++;
            green = true;
            completed = (completion >= 9);
        }
        arenaMode();
    }

    public void decrease(){
        if(!completed && completion > 0){
            completion--;
            green = false;
        }
        arenaMode();
    }

    public boolean isCompleted(){
        return completed;
    }

    public int getLevel() {
        return level;
    }

    @Override 
    public void update(){
        if(inArena && progress){
            if(System.currentTimeMillis() - timeCheck > PROGRESS_TIME){ //if 1 second passes
                timeCheck = System.currentTimeMillis();
                increase();
            }
        }
    }
}