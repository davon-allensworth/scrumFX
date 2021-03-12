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
                        System.out.println("startgameButton pressed");
                    }
                }
            });
    }
    
    public void setup() {
        // Entity test2 = new Entity(gc);
        
        startGameButton = new Button(gc, "Start Game", 0, 0, 200, 100);
        this.entities.add(startGameButton);
    }

    public void teardown() {
        
    }

    public void draw() {
        for(Entity e : this.entities)
            e.draw();  
    }
}
