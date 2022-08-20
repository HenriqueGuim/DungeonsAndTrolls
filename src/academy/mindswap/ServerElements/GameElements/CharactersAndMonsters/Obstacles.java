package academy.mindswap.ServerElements.GameElements.CharactersAndMonsters;

public enum Obstacles {

    SLIME(CharactersAndMonsters.SLIME),
    GOBLIN(CharactersAndMonsters.GOBLIN),
    TROLL(CharactersAndMonsters.TROLL);

    CharactersAndMonsters monstersAndObstacles;

    Obstacles(CharactersAndMonsters monstersAndObstacles){
        this.monstersAndObstacles = monstersAndObstacles;

    }


    public CharactersAndMonsters getMonstersAndObstacles() {
        return monstersAndObstacles;
    }
}
