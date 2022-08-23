package academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles;

/**
 * This class represents the action present in chest obstacle
 */
public abstract class Chest extends SpecialObstacle {
    private boolean isOpen = false;

    /**
     *  Class constructor for the chest object
     * @param healthModifier amount of health modifier
     * @param character player character
     */
    public Chest(int healthModifier, String character) {
        super(healthModifier,character);
    }

    /**
     * Method called when choose to open a chest
     */
    public void open(){
        isOpen = true;
    }

    /**
     *
     * @return true if the chest is already opened
     */
    public boolean isOpen(){
        return isOpen;
    }
}