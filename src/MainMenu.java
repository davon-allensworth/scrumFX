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
    GraphicsContext gc;

    public MainMenu(Parent root, GraphicsContext gc) {
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
                    }
                }
            });
    }
    
    @Override
    public void setup() {
        double centerx = (double)gc.getCanvas().getWidth() / 2;
        double centery = (double)gc.getCanvas().getHeight() / 2;
        
        startGameButton = new Button(gc, "start button", "start button pressed", centerx - centerx/1.5, centery + centery/6);
        this.entities.add(startGameButton);

        Entity title = new Entity(gc, centerx - 960*0.5/2, centery - 300, 500, 300, 0.5);
        this.entities.add(title);

    }

    @Override
    public void teardown() {
        
    }

    @Override
    public void draw() {
        for(Entity e : this.entities)
            e.draw();  
    }
}
