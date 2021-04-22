import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Results extends Scene{
    Button menuButton = null;
    TextField nameField = null;
    javafx.scene.control.Button saveButton = null;
    GraphicsContext gc;
    GameManager gm;
    private Parent root;
    Text text;
    StringBuilder resultsText;

    public Results(Parent root, GraphicsContext gc) {
        super(root, Color.LIGHTCORAL);
        this.root = root;
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.gm = GameManager.getInstance();
        this.nameField = new TextField();
        this.saveButton = new javafx.scene.control.Button("Save");

        // Mouse event handler
        this.setOnMouseClicked(
        e -> {
            if (menuButton.collidesWith(e.getX(), e.getY())) {
                menuButton.pressed();
                // Return to main menu
                gm.changeScene("main menu");
            }
        });
        
        //Setting an action for the Submit button
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                saveButton.requestFocus();
                saveScore(getName());
            }
        });
    }

    private String getName() {
        String name = nameField.getText();
        name = name.replaceAll(":", ""); // sanitize
        
        if (name.length() < 1) {
            System.out.println("User did not enter valid name");
            return null;
        }

        return name;
    }

    private void saveScore(String name) {
        if (name == null) {
            errorNameField();
        } else {

            // Add the score to history
            boolean saved = writeScore(gm.totalScore, name);
            if (saved) {
                System.out.println("Successfully saved score for user: " + name);
                nameField.clear();
                nameField.setPromptText("Saved!");

                // Reset all point/story values
                gm.resetBacklogs();

                saveButton.setDisable(true);
                nameField.setDisable(true);
            } else {
                errorNameField();
            }
        }
    }

    private boolean writeScore(int score, String user){
        boolean result = false;
        try{
            PrintWriter scoreDoc = new PrintWriter(new FileWriter("scores.txt",true));
            scoreDoc.append("").append(String.valueOf(score)).append(":").append(user).append("\n");
            scoreDoc.close();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        gm.scores = new ArrayList<>();
        gm.loadScores();
        return result;
    }

    private void errorNameField() {
        System.out.println("Error adding score to file");
        nameField.clear();
        nameField.setPromptText("Error!");
    }

    @Override
    public void draw() {
        super.drawBackground(gc);
        for(Entity e : this.entities)
            e.draw();
        gc.fillText(this.resultsText.toString(), 60, 90);
    }

    @Override
    public void teardown() {
    }
    
    @Override
    public void setup() {
        double centerx = gc.getCanvas().getWidth() / 2;
        double centery = gc.getCanvas().getHeight() / 2;

        menuButton = new Button(gc, "menu button", "menu button pressed", centerx, centery);
        menuButton.updateX(-(menuButton.getWidth()/2));
        menuButton.updateY((menuButton.getHeight() + 30));
        this.entities.add(menuButton);

        // name enter field
        ((Group) this.root).getChildren().add(nameField);
        nameField.setPromptText("Enter your name!");
        Font font = Font.loadFont( getClass().getResourceAsStream("assets/fonts/nokiafc22.ttf"), 24);
        nameField.setFont(font);
        nameField.setAlignment(Pos.CENTER);
        
        nameField.setMinWidth(316);
        nameField.setMaxWidth(316);
        nameField.setMinHeight(50);
        nameField.relocate(centerx, centery);
        nameField.setTranslateX( -nameField.getMinWidth() / 2 - 75);
        
        // save button
        ((Group) this.root).getChildren().add(saveButton);
        saveButton.setFont(font);

        saveButton.setMinWidth(140);
        saveButton.setMaxWidth(140);
        saveButton.setMinHeight(50);
        saveButton.relocate(centerx, centery);
        saveButton.setTranslateX( -saveButton.getMinWidth() / 2 + nameField.getMinWidth() /2);

        // Text setup
        text = new Text();
        Font resultsFont = Font.loadFont( getClass().getResourceAsStream("assets/fonts/nokiafc22.ttf"), 20);
        text.setFont(resultsFont);

        this.resultsText = new StringBuilder("Congratulations!\n\n\n");
        if(gm.productBacklogDone()){
            resultsText.append("You were able to successfully complete all ").append(gm.productBacklog.size()).append(" stories!\n\n");
        } else{
            resultsText.append("You were able to complete ").append(gm.storiesCompleted()).append(" out of ").append(gm.productBacklog.size()).append(" stories!\n\n");
            resultsText.append("Pacing yourself properly is the key to being Agile.\n\n");
            resultsText.append("Be sure to not take on too many stories at once!\n\n");
        }
        resultsText.append("\nYour final score: ").append(gm.totalScore);

        saveButton.setDefaultButton(true);
    }
}