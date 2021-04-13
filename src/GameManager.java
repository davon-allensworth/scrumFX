import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance = null;

    private GraphicsContext gc;

    public static final boolean DEBUG = false;

    public int totalScore;
    private int currentSprint;
    private int amountOfSprints;
    private int sprintTimeLimit;
    private int currentSprintTime;
    public boolean iterationsComplete;

    public List<Story> productBacklog;
    public List<Story> sprintBacklog;
    public ArrayList<Score> scores;

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
        this.iterationsComplete = false;
        this.sprintTimeLimit = 120;
        this.productBacklog = new ArrayList<>();
        this.sprintBacklog = new ArrayList<>();
        this.font = gc.getFont();
        this.scores = new ArrayList<>();
        loadScores();
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
        sprintBacklog = new ArrayList<>();
        sprintBacklog.add(new Story(gc, "take\n\na nice\n\nnap", 1, 0, 0));
        sprintBacklog.add(new Story(gc, "goof\n\naround\n\non\n\nreddit", 2, 0, 0));
        sprintBacklog.add(new Story(gc, "code\n\nup a\n\npretty\n\nhello\n\nworld", 3, 0, 0));
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

            case "retrospective":
                System.out.println("User score was: " + totalScore);
                scene = new SprintRetrospective(root, gc, this);
                break;

            case "results":
                scene = new Results(root, gc, this);
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

    private void loadScores(){
        try {
        File scoreFile = new File("scores.txt");
        if(scoreFile.createNewFile())
            System.out.println("New score history created in directory.");
        } catch (Exception e) {
            System.out.println("Could not create file.");
            System.exit(2);
        }
        try {
            Scanner infile = new Scanner(new File("scores.txt"));
            while (infile.hasNext()) {
                String[] splitLine = infile.nextLine().split(":");
                Score s = new Score(Integer.parseInt(splitLine[0]), splitLine[1]);
                scores.add(s);
            }
            infile.close();
        } catch (Exception e) {
            System.out.println("Error in score file formatting");
            System.exit(2);
        }
        // Sort scores in descending order
        Collections.sort(scores);

        // TESTING: Print every value
        System.out.println("*** HIGH SCORES ***");
        for(Score s : scores){
            System.out.println(s.player + " " + s.value);
        }
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

        // Check if last iteration
        if(currentSprint < amountOfSprints) {
            currentSprint++;
        } else {
            iterationsComplete = true;
        }
        changeScene("retrospective");
    }
}
