import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance = null;

    private GraphicsContext gc;

    public static final boolean DEBUG = true;

    private int totalScore;
    private int currentSprint;
    private int amountOfSprints;
    private int sprintTimeLimit;
    private int currentSprintTime;

    public List<Story> productBacklog;
    public List<Story> sprintBacklog;

    private Stage stage;

    private int musicVolume = 100;
    private int soundVolume = 100;

    private Font font = null;

    private GameManager() {
        productBacklog = new ArrayList<>();
        sprintBacklog = new ArrayList<>();
    }

    public GameManager(GraphicsContext gc){
        this.gc = gc;
        this.totalScore = 0;
        this.currentSprint = 0;
        this.amountOfSprints = 4;
        this.sprintTimeLimit = 120;
        this.productBacklog = new ArrayList<>();
        this.sprintBacklog = new ArrayList<>();
        this.font = gc.getFont();
    }

    public static GameManager getInstance() {
        if (instance == null)
            instance = new GameManager();

        return instance;
    }

    public Story[] generateStories() {
        return null;
    }

    public List<Story> getSprintBacklog() {
        // replace with values set by storyselect screen
        sprintBacklog = new ArrayList<Story>();
        sprintBacklog.add(new Story(gc, "take\n\na nice\n\nnap", 1, 0, 0));
        sprintBacklog.add(new Story(gc, "goof\n\naround\n\non\n\nreddit", 2, 0, 0));
        sprintBacklog.add(new Story(gc, "code\n\nup a\n\npretty\n\nhello\n\nworld", 3, 0, 0));

        return sprintBacklog;
    }

    public void changeScene(String sceneName) {
        Group root = new Group();
        
        Canvas canvas = new Canvas(600, 600);
        root.getChildren().add(canvas);
        this.gc = canvas.getGraphicsContext2D();

        gc.setFont(font); //retain the same font

        Scene scene = null;

        switch(sceneName) {
            case "arena":
                scene = new Arena(root, gc, this);
                break;

            case "story select":
                scene = new StorySelect(root, gc, this);
                break;

            // Yet to be implemented
            case "score":
                System.out.println("User score was: " + totalScore);
                break;
            
            case "main menu":
                scene = new MainMenu(root, gc, this);
                break;

            case "settings":
                scene = new Settings(root, gc, this);
                break;
                
            default:
            return;
        }

        ((Scene) stage.getScene()).teardown();

        scene.setup();
        stage.setScene(scene);
    }

    public void setStage(Stage _stage) {
        stage = _stage;
    }

    public boolean storiesDone(){
        boolean result = true;
        for (Story s: sprintBacklog){
            if (!s.isCompleted()){
                result = false;
                break;
            }
        }
        return result;
    }

    // End of a typical iteration, should show the user the score screen
    // and check to see if last iteration.
    public void endSprint(){

        // add sprint score to user total.
        for(Story s : sprintBacklog){
            if (s.isCompleted()){
                totalScore += s.getLevel();
            }
        }

        System.out.println("End of the sprint. User score was: " + this.totalScore);

        // Check if last iteration
        if(currentSprint < amountOfSprints) {
            currentSprint++;
            changeScene("arena");
        } else {
            System.out.println("The game should end at this point.");
            System.exit(0);
        }
    }
}
