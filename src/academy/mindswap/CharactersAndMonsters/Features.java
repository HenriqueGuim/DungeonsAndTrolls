package academy.mindswap.CharactersAndMonsters;

public abstract class Features {

    private int hp;

    private int damage;

    private CharactersAndMonsters info;

    public Features(int hp, int damage, CharactersAndMonsters info) {
        this.hp = hp;
        this.damage = damage;
        this.info = info;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public CharactersAndMonsters getInfo() {
        return info;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "Features{" +
                "hp=" + hp +
                ", damage=" + damage +
                ", info=" + info +
                '}';
    }
}
