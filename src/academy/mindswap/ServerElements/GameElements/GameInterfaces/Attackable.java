package academy.mindswap.ServerElements.GameElements.GameInterfaces;

public interface Attackable {

    void increaseHealth(int hp);

    void decreaseHealth(int hp);

    void die();

    boolean isDead();
}

