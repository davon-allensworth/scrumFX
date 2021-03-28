import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class Story extends Entity {
    String text = null;
    private int completion;
    private boolean green = true;
    private boolean completed = false;
    private boolean progress = true; //should the progress bar go up
    private boolean inArena = true; //is the story in the arena
    private long timeCheck = 0; //to make progress bar move slowly
    private long decreaseTimeCheck = 0; //to make progress bar move slowly
    private int level;

    private static final int PROGRESS_TIME = 1000;
    private static final int DECREASE_TIME = 4000;

    private static final int TEXT_OFFSET_X = 20;
    private static final int TEXT_OFFSET_Y = 60;

    private static final String ASSET_PATH = "assets/stories/";
    private String levelPath;
    private static final String FILE_EXT = ".png";

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
        gc.fillText(this.text, x + TEXT_OFFSET_X, y + TEXT_OFFSET_Y);
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
    }
}