import java.util.ArrayList;

import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SprintRetrospective extends Scene {
    Button nextScreen = null;
    GraphicsContext gc;
    GameManager gm;
    Parent root;
    LineChart<Number, Number> chart = null;
    Text text;
    StringBuilder victoryText;

    public SprintRetrospective(Parent root, GraphicsContext gc) {
        super(root, Color.LIGHTCORAL);
        this.entities = new ArrayList<>();
        this.gc = gc;
        this.root = root;
        gm = GameManager.getInstance();

        // Mouse event handler
        this.setOnMouseClicked(
        e -> {
            if (nextScreen.collidesWith(e.getX(), e.getY())) {
                nextScreen.pressed();
            }
        });

    }

    private void setupCharts() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Sprint");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        // xAxis.setUpperBound(gm.amountOfSprints);
        xAxis.setUpperBound(4);
        xAxis.setTickUnit(1.0);
        xAxis.setMinorTickVisible(false);

        yAxis.setLabel("Velocity");
        yAxis.setForceZeroInRange(true);
        yAxis.setTickUnit(5.0);
        yAxis.setMinorTickVisible(false);

        chart = new LineChart<Number, Number>(xAxis, yAxis);
        // velocityChart.setLegendVisible(false);
        chart.setLegendSide(Side.TOP);

        Series<Number, Number> velocity = new Series<Number, Number>();
        velocity.setName("Sprint Velocity");
        Series<Number, Number> burndown = new Series<Number, Number>();
        burndown.setName("Product Burndown");

        int remainingStoryPoints = gm.totalStoryPoints;
        burndown.getData().add(new Data<Number, Number>(0, remainingStoryPoints)); // add sprint 0 backlog size
        for (int i = 1; i <= gm.velocities.size(); i++) {
            velocity.getData().add(new Data<Number, Number>(i, gm.velocities.get(i-1)));
            remainingStoryPoints -= gm.velocities.get(i-1);
            burndown.getData().add(new Data<Number, Number>(i, remainingStoryPoints));
        }

        chart.getData().add(velocity);
        chart.getData().add(burndown);
    }

    @Override
    public void draw() {
        super.drawBackground(gc);
        for (Entity e : this.entities)
            e.draw();

        // Display Sprint Success
        // Current Score
        gc.fillText(this.victoryText.toString(), 120, 70);
    }

    @Override
    public void teardown() {

    }

    @Override
    public void setup() {
        double centerx = gc.getCanvas().getWidth() / 2;
        double centery = gc.getCanvas().getHeight() / 2;

        nextScreen = new Button(gc, "next button", "next button pressed", centerx, centery);
        nextScreen.updateX(-(nextScreen.getWidth() / 2));
        nextScreen.updateY((nextScreen.getHeight() + 80));


        // Text setup
        text = new Text();
        Font resultsFont = Font.loadFont( getClass().getResourceAsStream("assets/fonts/nokiafc22.ttf"), 24);
        text.setFont(resultsFont);
        this.victoryText = new StringBuilder("Congratulations!\n\nYou have successfully completed sprint " + gm.currentSprint + "!\n\n");
        victoryText.append("Your current score: ").append(gm.totalScore);

        this.entities.add(nextScreen);

        setupCharts();
        chart.setMinWidth(centerx * 1.5); // 75% width
        chart.setMaxWidth(centerx * 1.5);
        chart.setMinHeight(centery); // 50% height
        chart.setMaxHeight(centery);
        chart.relocate(centerx * .20, centery * 0.5);
        ((Group) this.root).getChildren().add(this.chart);
    }

    @Override
    public void update() {
        nextScreen.update();

        if (nextScreen.isTriggered()) {
            if (gm.iterationsComplete) {
                gm.changeScene("results");
            } else {
                gm.changeScene("sprint number");
            }
        }
    }
}
