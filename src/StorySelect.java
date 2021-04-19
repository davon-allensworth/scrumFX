import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class StorySelect extends Scene {
    private Button startSprintButton = null;
    private GraphicsContext gc;
    private GameManager gm;
    private List<Story> productBacklog;
    private Entity text;

    public StorySelect(Parent root, GraphicsContext gc) {
        super(root);
        this.entities = new ArrayList<Entity>();
        this.gc = gc;
        this.gm = GameManager.getInstance();

        this.productBacklog = gm.getProductBacklog();

        this.setOnMouseClicked(
            new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent e)
                {
                    if (!gm.getSprintBacklog().isEmpty() && startSprintButton.collidesWith( e.getX(), e.getY() ) )
                    {
                        startSprintButton.pressed();
                    }

                    for(Story story : productBacklog){
                        if(story.collidesWith( e.getX(), e.getY())){
                            if(!story.isSelected() && !story.isCompleted()){
                                gm.addToSprintBacklog(story);
                                story.select();
                            }else if(!story.isCompleted()){
                                gm.removeFromSprintBacklog(story);
                                story.deselect();
                            }
                            if(!gm.getSprintBacklog().isEmpty()){
                                text.stopAutoAnimation();
                                text.updateSprite("assets/stories/other/pick stories 0.png");
                            }
                        }
                    }
                }
            });

    }

    @Override
    public void setup() {
        double screenWidth = gc.getCanvas().getWidth();
        double screenHeight = gc.getCanvas().getHeight();
        double centerx = (double)screenWidth / 2;
        double centery = (double)screenHeight / 2;

        text = new Entity(gc, "assets/stories/other/pick stories 0.png", 0, 0, 1);
        entities.add(text);
        text.autoAnimate(12, 100, "assets/stories/other/pick stories ");
        
        startSprintButton = new Button(gc, "start button", "start button pressed", centerx, centery - (centery/1.225));
        startSprintButton.setVisibility(false);
        startSprintButton.updateX(-(startSprintButton.getWidth()/3));
        this.entities.add(startSprintButton);

        for(Story story : gm.getProductBacklog()){
            story.updateGraphicsContext(gc);
            story.inArena(false); //tell them they are not in the arena
            story.setVisibility(true);
            if(story.isCompleted()){
                story.deselect();
                gm.removeFromSprintBacklog(story);
            }
        }

        final int STORIES_PER_ROW = 4;
        final int STORIES_PER_COL = 3;
        double x = 0, y = centery - centery/1.35;
        Story story;
        for(int i = 0; i < productBacklog.size(); i++){
            story = productBacklog.get(i);
            if(i % STORIES_PER_ROW == 0){
                x = (screenWidth/STORIES_PER_ROW-story.getWidth());
                y +=  (screenHeight/STORIES_PER_COL)-(story.getHeight()/1.8);
            }
            story.setLocation(x, y);
            this.entities.add(story);

            if(story.isCompleted()){
                Entity checkmark = new Entity(gc, "assets/stories/other/checkmark.png", x, y, 1);
                this.entities.add(checkmark);
            }

            x += screenWidth/STORIES_PER_ROW + 1;
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
    }

    @Override
    public void update() {
        for(Entity entity : entities){
            entity.update();
        }
        if(gm.getSprintBacklog().isEmpty()){
            if(!startSprintButton.isGhosted()) startSprintButton.ghost(true);
        }else{
            if(startSprintButton.isGhosted()){
                startSprintButton.setVisibility(true);
                startSprintButton.ghost(false);
            }
        }
        if(startSprintButton.isTriggered()) GameManager.getInstance().changeScene("arena");
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTCORAL);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(GameManager.getTextColor());
    }
}
