import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance = null;
    
    public List<Entity> entities;

    private Group root;
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


    private GameManager() {
        productBacklog = new ArrayList<>();
        sprintBacklog = new ArrayList<>();
    }



    public GameManager(Group root){
        this.root = root;
        this.totalScore = 0;
        this.currentSprint = 0;
        this.amountOfSprints = 4;
        this.sprintTimeLimit = 120;
        this.productBacklog = new ArrayList<>();
        this.sprintBacklog = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public static GameManager getInstance() {
        if (instance == null)
            instance = new GameManager();

        return instance;
    }

    public Story[] generateStories() {
        return null;
    }

    public void changeScene(String sceneName) {
        Group root = new Group();
        Canvas canvas = new Canvas(600, 600);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = null;

        switch(sceneName) {
            case "arena":
                scene = new Arena(root, gc, this);
                break;

            // Yet to be implemented
            case "score":
                System.out.println("User score was: " + totalScore);
                break;
            
            default:
            return;
        }
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
