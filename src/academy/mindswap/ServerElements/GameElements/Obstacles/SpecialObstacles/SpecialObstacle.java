package academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;

/**
 * This class represents the health modification system when the player's character encounter a special obstacle
 */
public abstract class SpecialObstacle extends Obstacle {
    private int healthModifier;

    /**
     * Class constructor for the special obstacle class
     * @param healthModifier amount of health modifier
     * @param character player character
     */
    public SpecialObstacle(int healthModifier, String character){
        super(character);
        this.healthModifier = healthModifier;
    }

    /**
     * @return the amount of health modified when encounter a special obstacle
     */
    public int getHealthModifier() {
        return healthModifier;
    }
}
