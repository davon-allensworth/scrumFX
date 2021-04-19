import javafx.scene.canvas.GraphicsContext;

public class Story extends Entity {
    String text = null;
    private int completion;
    private boolean green = true;
    private boolean completed = false;
    private boolean shouldSwitchOut = false;
    private boolean progress = true; //should the progress bar go up
    private boolean inArena = false; //is the story in the arena
    private long timeCheck = 0; //to make progress bar move slowly
    private long decreaseTimeCheck = 0; //to make progress bar move slowly
    private long switchTimeCheck = -1; //pause before switching
    private int level;
    private boolean selected = false;

    private static final int PROGRESS_TIME = 2500;
    private static final int DECREASE_TIME = 4000;
    private static final int SWITCH_TIME = 2000;

    private static final int TEXT_OFFSET_X = 15;
    private static final int ARENA_TEXT_OFFSET_Y = 60;
    private static final int NORMAL_TEXT_OFFSET_Y = 28;

    private static final String ASSET_PATH = "assets/stories/";
    private String levelPath;
    private static final String FILE_EXT = ".png";

    private static final Sound storyClick = new Sound("assets/sounds/click.wav", false);

    public Story(GraphicsContext gc, String words, int level,
                  double x, double y) {
        super(gc, ASSET_PATH+"level "+level+"/story "+level+FILE_EXT, x, y, 1);
        this.text = words;
        this.level = level;
        this.levelPath = "level "+level+"/story "+level;
        completion = 0;
    }

    @Override
    public void draw() {
        super.draw();
        if(inArena) gc.fillText(this.text, x + TEXT_OFFSET_X, y + ARENA_TEXT_OFFSET_Y);
        else gc.fillText(this.text, x + TEXT_OFFSET_X, y + NORMAL_TEXT_OFFSET_Y);
    }

    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
        this.hitboxX = x;
        this.hitboxY = y;
    }

    public void inArena(boolean answer){
        inArena = answer;
        if(answer == false){
            progress = false;
            if(selected){
                this.updateSprite(ASSET_PATH+levelPath+" selected"+FILE_EXT,true);
            }else{
                this.updateSprite(ASSET_PATH+levelPath+FILE_EXT,true);
            }
        }else{
            this.updateSprite(ASSET_PATH+levelPath+" green "+completion+FILE_EXT, true);
        }
    }

    public boolean shouldSwitchOut(){
        return shouldSwitchOut;
    }

    public void select(){
        if(!selected){
            storyClick.stop();
            storyClick.play();
        }
        selected = true;
        this.updateSprite(ASSET_PATH+levelPath+" selected"+FILE_EXT);
    }

    public void deselect(){
        if(selected){
            storyClick.stop();
            storyClick.play();
        }
        selected = false;
        this.updateSprite(ASSET_PATH+levelPath+FILE_EXT);
    }

    public boolean isSelected(){
        return selected;
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
        setDimensionsToSprite();
        this.y = gc.getCanvas().getHeight()-this.height;
        this.hitboxY = this.y;
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
        if(!completed && completion > 0 &&
                System.currentTimeMillis() - decreaseTimeCheck < DECREASE_TIME){
            completion--;
        }else{
            green = true;
        }
        arenaMode();
    }

    public boolean isCompleted(){
        return completed;
    }

    public int getLevel() {
        return level;
    }

    public void hit(){
        green = false;
        decreaseTimeCheck = System.currentTimeMillis();
        timeCheck = 0; //this will make the color switch quickly
    }

    @Override 
    public void update(){
        if(inArena && progress){
            if(System.currentTimeMillis() - timeCheck > PROGRESS_TIME){ //if 1 second passes
                timeCheck = System.currentTimeMillis();
                if(green) increase();
                else decrease();
            }
        }
        if(this.isCompleted() && !this.shouldSwitchOut()){
            if(switchTimeCheck < 0){
                switchTimeCheck = System.currentTimeMillis();
            }else if(System.currentTimeMillis() - switchTimeCheck > SWITCH_TIME){
                shouldSwitchOut = true;
            }
        }
    }
}