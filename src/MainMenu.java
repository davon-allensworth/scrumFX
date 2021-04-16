import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class MainMenu extends Scene {
    private Button startGameButton = null;
    private Button settingsButton = null;
    private GraphicsContext gc;
    GameManager gm;
    Text text;
    StringBuilder highScores;

    public MainMenu(Parent root, GraphicsContext gc) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;
        gm = GameManager.getInstance();

        // Iterator<String> fonts = Font.getFamilies().iterator();

        this.setOnMouseClicked(
                e -> {
                    if ( startGameButton.collidesWith( e.getX(), e.getY() ) )
                    {
                        // Font newFont = new Font(fonts.next(), 50);
                        // System.out.println(newFont.getName());
                        // gc.setFont(newFont);
                        startGameButton.pressed();
                        System.out.println("startgameButton pressed");
                        GameManager.getInstance().changeScene("story select");
                    }

                    if(settingsButton.collidesWith(e.getX(), e.getY())){
                        settingsButton.pressed();
                        GameManager.getInstance().changeScene("settings");
                    }
                });
    }
    
    @Override
    public void setup() {
        double centerx = gc.getCanvas().getWidth() / 2;
        double centery = gc.getCanvas().getHeight() / 2;
        
        startGameButton = new Button(gc, "start button", "start button pressed", centerx /*- centerx/1.5*/, centery /*+ centery/6*/);
        startGameButton.updateX(-(startGameButton.getWidth()/2));
        settingsButton = new Button(gc, "menu button", "menu button pressed", centerx, centery);
        settingsButton.updateX(-(settingsButton.getWidth()/2));
        settingsButton.updateY((startGameButton.getHeight() + 30));
        this.entities.add(startGameButton);
        this.entities.add(settingsButton);

        Entity title = new Entity(gc, "assets/S.C.R.U.M..gif", 0, (centery/4) + 20, 1);
        title.centerX();
        this.entities.add(title);
        text = new Text();
        this.highScores = new StringBuilder("HIGH SCORES:\n\n");

        // Display top 3 high scores
        for(int i = 0; i < 3; i++){
            if(i < gm.scores.size()){
                Score s = gm.scores.get(i);
                String score = String.format("%-13s%s%n", s.player, s.value);
                switch(i){
                    case 0:
                        highScores.append("1st ").append(score).append("\n");
                        break;
                    case 1:
                        highScores.append("2nd ").append(score).append("\n");
                        break;
                    case 2:
                        highScores.append("3rd ").append(score).append("\n");
                }
            }
        }

    }

    @Override
    public void teardown() {
    }

    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            e.draw();
        gc.fillText(this.highScores.toString(), 10, 20);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(GameManager.getTextColor());
    }
}
