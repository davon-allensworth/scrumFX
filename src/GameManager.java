import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance = null;

    private GraphicsContext gc;

    public static final boolean DEBUG = true;

    public int totalScore;
    private int currentSprint;
    private int amountOfSprints;
    private int sprintTimeLimit;
    private int currentSprintTime;
    public boolean iterationsComplete;

    private static Color TEXT_COLOR = Color.BLACK;

    public List<Story> productBacklog;
    public List<Story> sprintBacklog;
    public ArrayList<Score> scores;

    private Stage stage;

    private static double musicVolume = 0.1;
    private static double soundVolume = 0.1;
    private static Sound menuMusic;

    private Font font = null;

    private GameManager() {
        productBacklog = new ArrayList<>();
        sprintBacklog = new ArrayList<>();
    }

    private GameManager(GraphicsContext gc){
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

    public static void init(GraphicsContext gc) {
        instance = new GameManager(gc);

        // Initialize menu music
        menuMusic = new Sound("assets/music/Main_Menu.mp3", true, true);
    }

    public static GameManager getInstance() {
        if (instance == null) {
            System.err.println("ERROR: Gamemanager requested before init");
            System.exit(1);
        }

        return instance;
    }

    public Story[] generateStories() {
        return null;
    }

    public static double getMusicVolume(){
        return musicVolume;
    }

    public static void setMusicVolume(int volume){
        if(volume > 0)
            musicVolume = (100.0 / volume);
        else
            musicVolume = 0;
    }

    public static void setMusicVolume(double volume){
        musicVolume = volume;
    }

    public static double getSoundVolume(){
        return soundVolume;
    }

    public static void setSoundVolume(int volume){
        if(volume > 0)
            soundVolume = (100.0 / volume);
        else
            soundVolume = 0;
    }
    
    public static void setSoundVolume(double volume){
        soundVolume = volume;
    }

    public static Color getTextColor(){
        return TEXT_COLOR;
    }

    public List<Story> getSprintBacklog() {
        // replace with values set by storyselect screen
        sprintBacklog = new ArrayList<>();
        sprintBacklog.add(new Story(gc, "take\n\na nice\n\nnap", 1, 0, 0));
        sprintBacklog.add(new Story(gc, "goof\n\naround\n\non\n\nreddit", 2, 0, 0));
        sprintBacklog.add(new Story(gc, "code\n\nup a\n\npretty\n\nhello\n\nworld", 3, 0, 0));
        sprintBacklog.add(new Story(gc, "take\n\na nice\n\nnap", 3, 0, 0));
        sprintBacklog.add(new Story(gc, "goof\n\naround\n\non\n\nreddit", 1, 0, 0));
        sprintBacklog.add(new Story(gc, "code\n\nup a\n\npretty\n\nhello\n\nworld", 2, 0, 0));

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
                scene = new Arena(root, gc);
                break;

            case "story select":
                if(menuMusic.isPlaying()) menuMusic.stop();
                scene = new StorySelect(root, gc);
                break;

            case "retrospective":
                System.out.println("User score was: " + totalScore);
                scene = new SprintRetrospective(root, gc);
                break;

            case "results":
                scene = new Results(root, gc);
                break;
            
            case "main menu":
                if(menuMusic.isPlaying() == false) menuMusic.play();
                scene = new MainMenu(root, gc);
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

    public Sound getMenuMusic(){
        return menuMusic;
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
