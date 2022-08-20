package academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;

public abstract class SpecialObstacle extends Obstacle {
    private int healthModifier;
    private String mapIdentifier = "S";
    public SpecialObstacle(int healthModifier){
        this.healthModifier = healthModifier;
    }
    public int getHealthModifier() {
        return healthModifier;
    }
    public String getMapIdentifier() {return mapIdentifier;}
}
