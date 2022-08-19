package academy.mindswap.CharactersAndMonsters;

public enum CharactersAndMonsters {

    MAGE ("Mage"),
    KNIGHT("Knight"),
    SQUIRE("Squire"),
    FINAL_BOSS("Final Boss"),
    MINI_BOSS("Mini Boss "),
    SLIME("Slime"),
    GOBLIN("Goblin"),
    TROLL("Troll");

    private final String info;

    CharactersAndMonsters(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
