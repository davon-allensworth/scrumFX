import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class SprayParticleGenerator extends Entity{
    private boolean active = true;
    private double power = 1;
    private List<SprayParticle> particles;

    private static final String PATH = "assets/Particles/spray.png";
    private static final double POWER_MULTIPLIER = 10;
    private static final double X_OFFSET = 210;
    private static final double Y_OFFSET = 100;
    private static final double MIN_RANDOM = -1;
    private static final double MAX_RANDOM = 1;
    private static final double BASE_SPEED = 2;
    private static final double BASE_SIZE = 1;

    public SprayParticleGenerator(GraphicsContext gc, double x, double y, double power) {
        super(gc, PATH, x+X_OFFSET, y+Y_OFFSET, 1);
        visible = false;
        this.active = true;
        this.power = power;
        this.initParticles();
    }

    private void initParticles(){
        particles = new ArrayList<>();
        visible = true;
        Random r = new Random();
        double xVel, yVel, size, randomDouble;
        for(int i = 0; i < power*POWER_MULTIPLIER; i++){

            randomDouble = power*(MIN_RANDOM + (MAX_RANDOM - MIN_RANDOM) * r.nextDouble());
            xVel = randomDouble;

            randomDouble = Math.pow(power,2)*(MIN_RANDOM + (MAX_RANDOM - MIN_RANDOM) * r.nextDouble());
            yVel = -(Math.abs(BASE_SPEED+randomDouble));

            randomDouble = power*(MIN_RANDOM + (MAX_RANDOM - MIN_RANDOM) * r.nextDouble());
            size = BASE_SIZE+randomDouble;

            particles.add(new SprayParticle(this.gc, this.x, this.y, xVel, yVel, size, r));
        }
    }

    public List<SprayParticle> getParticles(){
        return particles;
    }

    public boolean isActive(){
        return active;
    }

    public void deactivate(){
        active = false;
        for(SprayParticle particle : particles){
            particle.end();
        }
    }

    public void activate(){
        active = true;
    }

    @Override
    public void update(){
        if(active){
            for(SprayParticle particle : particles){
                particle.update();
            }
        }
    }
}
