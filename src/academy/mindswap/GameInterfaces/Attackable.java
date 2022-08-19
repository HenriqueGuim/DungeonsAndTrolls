package academy.mindswap.GameInterfaces;

public interface Attackable {

    void increaseHealth(int hp);

    void decreaseHealth(int hp);

    void die();

    boolean isdead();
}

