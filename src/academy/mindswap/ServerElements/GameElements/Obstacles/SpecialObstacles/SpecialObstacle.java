package academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;

public abstract class SpecialObstacle extends Obstacle {
    private int healthModifier;
    public SpecialObstacle(int healthModifier){
        super("S");
        this.healthModifier = healthModifier;
    }
    public int getHealthModifier() {
        return healthModifier;
    }
}
