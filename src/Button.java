import javafx.scene.canvas.GraphicsContext;

public class Button extends Entity {
    String pressedButton = null;
    String idleButton = null;

    public boolean triggered = false;

    private boolean pressed = false;

    //allow pressed button sprite too show for a little
    private static double BUTTON_DELAY = 100;
    private double buttonTimeCheck = -1;
    
    private static final String ASSET_PATH = "assets/buttons/";
    private static final String FILE_EXT = ".png";
    private static final Sound buttonClick = new Sound("assets/sounds/click.wav", false);

    public Button(GraphicsContext gc, String buttonName,
                  double x, double y, double w, double h) {
        super(gc, ASSET_PATH+buttonName+FILE_EXT, x, y, w, h, 1);
    }

    //this constructor is for if the button has a sprite for its pressed state
    public Button(GraphicsContext gc, String buttonName, String pressedButtonName,
                  double x, double y) {

        //this constructor automatically sets the height and width to match the sprite
        super(gc, ASSET_PATH+buttonName+FILE_EXT, x, y, 1); 
        idleButton = buttonName;
        pressedButton = pressedButtonName;
    }

    public void draw() {
        super.draw();
    }

    @Override
    public void update(){
        if(pressed && buttonTimeCheck < 0){
            buttonTimeCheck = System.currentTimeMillis();
        }else if(pressed && System.currentTimeMillis() - buttonTimeCheck > BUTTON_DELAY){
            triggered = true;
        }
    }

    public boolean isTriggered(){
        return triggered;
    }

    public void pressed() {
        buttonClick.stop();
        buttonClick.play();
        this.updateSprite(ASSET_PATH+pressedButton+FILE_EXT);
        pressed = true;
    }
}