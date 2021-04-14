import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;

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

    public Results(Parent root, GraphicsContext gc, GameManager gm) {
        super(root);
        this.root = root;
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.gm = gm;
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
        return result;
    }

    private void errorNameField() {
        System.out.println("Error adding score to file");
        nameField.clear();
        nameField.setPromptText("Error!");
    }

    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            e.draw();
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
        
        
        ((Group) this.root).getChildren().add(saveButton);
        saveButton.setFont(font);

        saveButton.setMinWidth(140);
        saveButton.setMaxWidth(140);
        saveButton.setMinHeight(50);
        saveButton.relocate(centerx, centery);
        saveButton.setTranslateX( -saveButton.getMinWidth() / 2 + nameField.getMinWidth() /2);

        saveButton.setDefaultButton(true);
    }
    
    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

}