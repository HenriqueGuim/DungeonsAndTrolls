package academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles;

public abstract class Chest extends SpecialObstacle {
    private boolean isOpen = false;
    public Chest(int healthModifier, String character) {
        super(healthModifier,character);
    }
    public void open(){
        isOpen = true;
    }
    public boolean isOpen(){
        return isOpen;
    }
}
