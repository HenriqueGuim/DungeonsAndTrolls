package academy.mindswap.Obstacles.Monsters;
import academy.mindswap.GameInterfaces.Attackable;
import academy.mindswap.Obstacles.Obstacles;

public abstract class Monsters extends Obstacles implements Attackable{
    private int health;
    private int damage;

    //private MonsterStatus status;

    public Monsters(int health , int damage ) {
        this.health = health;
        this.damage = damage;
    }

    @Override
    public void looseHealth(int damageReceived) {
        this.health -= damageReceived;
    }

    @Override
    public void die() {
        this.health = 0;
    }

    @Override
    public boolean isdead() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() { return damage; }
}
