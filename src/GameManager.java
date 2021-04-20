import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.Axis;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameManager {
    private static GameManager instance = null;

    private GraphicsContext gc;

    public static final boolean DEBUG = false;

    public int totalPoints;     // Story points completed by the user
    public int totalScore;      // Score accumulated throughout game
    public int currentSprint;
    public int amountOfSprints;
    public int totalStoryPoints;
    private int sprintTimeLimit;
    private int currentSprintTime;
    public boolean iterationsComplete;

    private static Color TEXT_COLOR = Color.BLACK;

    public List<Story> productBacklog;
    public List<Story> sprintBacklog;

    public ArrayList<Integer> velocities;
    public ArrayList<Score> scores;

    private Stage stage;

    private static double musicVolume = 0.1;
    private static double soundVolume = 0.1;
    private static Sound menuMusic;
    private static Sound arenaMusic;
    private static Sound victoryMusic;

    private Font font = null;

    private GameManager() {
        productBacklog = new ArrayList<>();
        sprintBacklog = new ArrayList<>();
    }

    private GameManager(GraphicsContext gc){
        this.gc = gc;
        this.totalPoints = 0;
        this.totalScore = 0;
        this.currentSprint = 0;
        this.amountOfSprints = 4;
        this.iterationsComplete = false;
        this.sprintTimeLimit = 120;
        this.productBacklog = new ArrayList<>();
        this.sprintBacklog = new ArrayList<>();
        this.font = gc.getFont();
        this.scores = new ArrayList<>();
        this.velocities = new ArrayList<Integer>(this.amountOfSprints);
        // this.velocities = new int[] {12, 9, 12}; // TODO demo
        
        this.totalStoryPoints = 0;

        loadScores();
    }

    public static void init(GraphicsContext gc) {
        instance = new GameManager(gc);

        // Initialize menu music
        menuMusic = new Sound("assets/music/Main_Menu.mp3", true, true);
        arenaMusic = new Sound("assets/music/arena.wav", true, true);
        victoryMusic = new Sound("assets/music/sprint complete.wav", true, false);
    }

    public static GameManager getInstance() {
        if (instance == null) {
            System.err.println("ERROR: Gamemanager requested before init");
            System.exit(1);
        }

        return instance;
    }

    public List<Story> generateStories() {
        //replace with grabbing random stories from a json file or something
        productBacklog.add(new Story(gc, "think\n\nof cool\n\nvar\n\nnames", 1, 0, 0));
        productBacklog.add(new Story(gc, "stare\n\nat\n\ncode", 2, 0, 0));
        productBacklog.add(new Story(gc, "hack\n\nthe\n\nmain-\n\nframe", 3, 0, 0));
        productBacklog.add(new Story(gc, "speak\n\nin\n\nbinary", 3, 0, 0));
        productBacklog.add(new Story(gc, "impress\n\nfriends\n\nby\n\nediting\n\n html", 1, 0, 0));
        productBacklog.add(new Story(gc, "drink\n\ntoo\n\nmuch\n\ncoffee", 2, 0, 0));
        productBacklog.add(new Story(gc, "explain\n\nhashmap\n\nto mom\n\nfor no\n\n reason", 3, 0, 0));
        productBacklog.add(new Story(gc, "google\n\nhow to\n\ncode", 3, 0, 0));
        productBacklog.add(new Story(gc, "talk\n\nin\n\nepic\n\nbuzz\n\n words", 1, 0, 0));
        productBacklog.add(new Story(gc, "code\n\nup a\n\npretty\n\nhello\n\n world", 2, 0, 0));
        productBacklog.add(new Story(gc, "take\n\na nice\n\nnap", 3, 0, 0));
        productBacklog.add(new Story(gc, "goof\n\naround\n\non\n\nreddit", 1, 0, 0));
        Collections.shuffle(productBacklog);

        for (Story s : productBacklog)
            this.totalStoryPoints += s.getLevel();
        // this.totalStoryPoints = 70; // TODO demo

        return productBacklog;
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

    public List<Story> getProductBacklog(){
        if(productBacklog.isEmpty()){
            productBacklog = generateStories();
        }
        return productBacklog;
    }

    public void resetBacklogs(){
        productBacklog = new ArrayList<>();
        sprintBacklog = new ArrayList<>();
        this.currentSprint = 0; // and reset the sprint count
        this.totalScore = 0;    // reset the score
        this.totalPoints = 0;   // reser the points
    }

    public List<Story> getSprintBacklog() {
        return sprintBacklog;
    }

    public void addToSprintBacklog(Story story){
        sprintBacklog.add(story);
    }

    public void removeFromSprintBacklog(Story story){
        sprintBacklog.remove(story);
    }

    public void changeScene(String sceneName) {
        Group root = new Group();
        
        Canvas canvas = new Canvas(640, 600);
        root.getChildren().add(canvas);
        this.gc = canvas.getGraphicsContext2D();

        gc.setFont(font); //retain the same font

        Scene scene = null;

        switch(sceneName) {
            case "arena":
                if(victoryMusic.isPlaying()) victoryMusic.stop();
                if(menuMusic.isPlaying()) menuMusic.stop();
                if(!arenaMusic.isPlaying()) arenaMusic.play();
                scene = new Arena(root, gc);
                break;

            case "story select":
                if(victoryMusic.isPlaying()) victoryMusic.stop();
                if(arenaMusic.isPlaying()) arenaMusic.stop();
                if(!menuMusic.isPlaying()) menuMusic.play();
                scene = new StorySelect(root, gc);
                break;

            case "retrospective":
                if(arenaMusic.isPlaying()) arenaMusic.stop();
                if(!victoryMusic.isPlaying()) victoryMusic.play();
                System.out.println("User score was: " + totalScore);
                scene = new SprintRetrospective(root, gc);
                break;

            case "results":
                if(victoryMusic.isPlaying()) victoryMusic.stop();
                if(!menuMusic.isPlaying()) menuMusic.play();
                scene = new Results(root, gc);
                break;
            
            case "main menu":
                resetBacklogs();
                if(victoryMusic.isPlaying()) victoryMusic.stop();
                if(!menuMusic.isPlaying()) menuMusic.play();
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

    public static Sound getMusic(String name){
        if(name.equals("menu")){
            return menuMusic;
        }else if(name.equals("arena")){
            return arenaMusic;
        }else if(name.equals("victory")){
            return victoryMusic;
        }
        return null;
    }

    public void loadScores(){
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

    public boolean productBacklogDone(){
        boolean result = true;
        for (Story s: productBacklog){
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
        int score = 0;
        // add point values
        for(Story s : sprintBacklog){
            if (s.isCompleted()){
                score += s.getLevel();
            }
        }
        totalScore += score;
        velocities.add(score);

        // add score to overall
        int sprintMultiplier = (amountOfSprints - currentSprint);
        int workloadMultiplier = sprintBacklog.size();
        for(Story s : sprintBacklog){
            if (s.isCompleted()){
                totalScore += s.getLevel() + sprintMultiplier + workloadMultiplier;
            } else {
                totalScore -= s.getLevel();
            }
        }
        

        // Check if last iteration
        if(currentSprint < amountOfSprints && !productBacklogDone()) {
            currentSprint++;
            iterationsComplete = false;
        } else {
            iterationsComplete = true;
        }
        changeScene("retrospective");
    }
}
