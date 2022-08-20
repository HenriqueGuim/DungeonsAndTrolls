package academy.mindswap.ServerElements.GameElements.Obstacles.Monsters;
import academy.mindswap.ServerElements.GameElements.GameInterfaces.Attackable;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;

public abstract class Monsters extends Obstacle implements Attackable{
    private int health;
    private int damage;
    private String mapIdentifier = "X";
    //private MonsterStatus status;
    public Monsters(int health , int damage ) {
        this.health = health;
        this.damage = damage;
    }
    public String getMapIdentifier() {return mapIdentifier;}
    @Override
    public void increaseHealth(int hp) {this.health += hp;}
    @Override
    public void decreaseHealth(int hp) {this.health -= hp;}
    @Override
    public void die() {this.health = 0;}
    @Override
    public boolean isDead() {return health <= 0;}
    public int getHealth() {return health;}
    public int getDamage() { return damage; }
}
