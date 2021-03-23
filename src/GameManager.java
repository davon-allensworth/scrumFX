import java.util.List;

public class GameManager {
    public static GameManager instance;
    
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

    public GameManager getInstance() {
        return instance;
    }

    public Story[] generateStories() {
        return null;
    }

    public void changeScene() {
        // todo
    }
}
