package academy.mindswap.ServerElements.GameElements.Obstacles.Monsters;
import academy.mindswap.ServerElements.GameElements.GameInterfaces.Attackable;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;

/**
 * This class represents the common attributes and methods for all the game's monsters
 *
 */

public abstract class Monsters extends Obstacle implements Attackable{
    private int health;
    private int damage;

    //private MonsterStatus status;

    /**
     * Class constructor with the given health, damage and
     * @param health amount of health
     * @param damage amount of damage (attack value)
     * @param mapIdentifier location of the monsters
     */
    public Monsters(int health , int damage, String mapIdentifier ) {
        super(mapIdentifier);
        this.health = health;
        this.damage = damage;
    }

    /**
     * Method called when health is incremented
     * @param hp (health value)
     */
    @Override
    public void increaseHealth(int hp) {this.health += hp;}

    /**
     * Method called when health is decremented
     * @param hp (health value)
     */
    @Override
    public void decreaseHealth(int hp) {this.health -= hp;}

    /**
     * Method to determine if the monster is dead based on health value (when health reaches zero)
     */
    @Override
    public void die() {this.health = 0;}

    /**
     * Method to determine if the monster is dead
     * @return true if the monster is dead
     */
    @Override
    public boolean isDead() {return health <= 0;}
    public int getHealth() {return health;}
    public int getDamage() { return damage; }

    /**
     * Method to set the defend action when the monster is attacked
     * @param damage represents amount of health decremented when the monster is defending
     *
     */

    public void defend(int damage) {
        decreaseHealth(damage);
    }
}
