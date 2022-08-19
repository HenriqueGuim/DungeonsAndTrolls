package academy.mindswap.Obstacles.SpecialObstacles;
import academy.mindswap.Obstacles.Obstacles;

public abstract class SpecialObstacle extends Obstacles {

    private int healthModifier;

    public SpecialObstacle(int healthModifier){
        this.healthModifier = healthModifier;
    }

    public int getHealthModifier() {
        return healthModifier;
    }

}
