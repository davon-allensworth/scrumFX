import java.util.ArrayList;
import java.util.List;

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

    public void changeScene() {
        // todo
    }
}
