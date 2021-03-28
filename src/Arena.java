import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Arena extends Scene {
    Player player = null;
    GraphicsContext gc;
    GameManager gm;
    List<Story> sprintBacklog;
    List<Bug> bugs = new ArrayList<>();
    List<Entity> entities;

    public Arena(Parent root, GraphicsContext gc, GameManager gm) {
        super(root);
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.sprintBacklog = gm.sprintBacklog;
        this.gm = gm;

        this.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    KeyCode keyCode = e.getCode();
                    if(player.moveCode() != Player.SWAT_CODE &&
                            keyCode == KeyCode.SPACE && player.moveCode() != Player.PRESWAT_CODE){
                        player.preswat();
                    }else if(player.moveCode() != Player.PRESWAT_CODE && 
                            player.moveCode() != Player.SWAT_CODE &&
                            player.moveCode() != Player.LEFT_CODE &&
                            keyCode == KeyCode.LEFT){
                        player.moveLeft();
                    }else if(player.moveCode() != Player.PRESWAT_CODE &&
                            player.moveCode() != Player.SWAT_CODE &&
                            player.moveCode() != Player.RIGHT_CODE &&
                            keyCode == KeyCode.RIGHT){
                        player.moveRight();
                    }
                }
            });

            this.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        KeyCode keyCode = e.getCode();
                        if(keyCode == KeyCode.SPACE && player.moveCode() == Player.PRESWAT_CODE){
                            player.swat();
                        }else if((keyCode == KeyCode.LEFT && player.moveCode() == -1) ||
                            (keyCode == KeyCode.RIGHT && player.moveCode() == 1)){
                            player.idle();
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
        
        player = new Player(gc);

        sprintBacklog = gm.getSprintBacklog();

        //replace this with adding bugs based on the stories !!!
        bugs.add(new Bug(gc, centerx));
        bugs.add(new Bug(gc, centerx/3));

        double x = 0;
        for(Story story : sprintBacklog){
            if(x==0) x = (screenWidth/sprintBacklog.size()-story.getWidth());
            story.setLocation(x, screenHeight-story.getHeight());
            story.startProgress();
            this.entities.add(story);
            x += screenWidth/sprintBacklog.size();
        }

        for(Bug bug : bugs){
            this.entities.add(bug);
            bug.startMoving();
        }

        this.entities.add(player);
    }

    @Override
    public void teardown() {
        
    }

    @Override
    public void update() {

        // update entities and check for collisions
        for(Entity e : entities){
            e.update();
            if(e instanceof Bug && ((Bug)e).isAlive()){ //check for bug collisions
                for(Entity other : entities){
                    if(other instanceof Story){ //with story
                        if(e.collidesWith(other)){
                            ((Bug)e).startAbsorb();
                            ((Story)other).hit();
                        }
                    } else if (other instanceof Story) {
                        System.out.println("updated story");
                    }
                    // Check if bug is hit by swatter
                    if(player.moveCode() == 3 && e.collidesWith(player)){
                        ((Bug)e).kill();
                    }
                }
            }
        }

        // check if the sprint is over
        if(gm.storiesDone()){
            gm.endSprint();
        }
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