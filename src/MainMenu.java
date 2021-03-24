import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class MainMenu extends Scene {
    Button startGameButton = null;
    Button settingsButton = null;
    GraphicsContext gc;

    public MainMenu(Parent root, GraphicsContext gc, GameManager gm) {
        super(root);
        this.entities = new ArrayList<Entity>();
        this.gc = gc;

        // Iterator<String> fonts = Font.getFamilies().iterator();

        this.setOnMouseClicked(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent e)
                {
                    if ( startGameButton.collidesWith( e.getX(), e.getY() ) )
                    {
                        // Font newFont = new Font(fonts.next(), 50);
                        // System.out.println(newFont.getName());
                        // gc.setFont(newFont);
                        startGameButton.pressed();
                        System.out.println("startgameButton pressed");
                        gm.changeScene("arena");
                    }

                    if(settingsButton.collidesWith(e.getX(), e.getY())){
                        gm.changeScene("settings");
                    }
                }
            });
    }
    
    @Override
    public void setup() {
        double centerx = (double)gc.getCanvas().getWidth() / 2;
        double centery = (double)gc.getCanvas().getHeight() / 2;
        
        startGameButton = new Button(gc, "start button", "start button pressed", centerx /*- centerx/1.5*/, centery /*+ centery/6*/);
        startGameButton.updateX(-(startGameButton.getWidth()/2));
        settingsButton = new Button(gc, "settings button", "settings button", centerx, centery);
        settingsButton.updateX(-(settingsButton.getWidth()/2));
        settingsButton.updateY((startGameButton.getHeight() + 30));
        this.entities.add(startGameButton);
        this.entities.add(settingsButton);

        Entity title = new Entity(gc, centerx - 960*0.5/2, centery - 300, 500, 300, 0.5);
        this.entities.add(title);

    }

    @Override
    public void teardown() {
        
    }

    @Override
    public void draw() {
        this.drawBackground();
        for(Entity e : this.entities)
            e.draw();  
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }
}
