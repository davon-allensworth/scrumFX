import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance = null;
    
    public Entity[] entities;

    private int totalScore;
    private int currentSprint;
    private int amountOfSprints;
    private int sprintTimeLimit;
    private int currentSprintTime;

    private List<Story> productBacklog;
    private List<Story> sprintBacklog;

    private Stage stage;

    private int musicVolume = 100;
    private int soundVolume = 100;

    private GameManager() {
        productBacklog = new ArrayList<Story>();
        sprintBacklog = new ArrayList<Story>();
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
            scene = new Arena(root, gc);
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
}
