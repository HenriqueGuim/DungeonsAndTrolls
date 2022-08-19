package academy.mindswap.CharactersAndMonsters;

public enum CharactersAndMonsters {

    MAGE ("Mage"),
    KNIGHT("Knight"),
    SQUIRE("Squire"),
    FINAL_BOSS("The Final Boss"),
    MINI_BOSS("A mini boss appears"),
    SLIME("A slime"),
    GOBLIN("A goblin"),
    TROLL("a troll");

    private final String info;

    CharactersAndMonsters(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
