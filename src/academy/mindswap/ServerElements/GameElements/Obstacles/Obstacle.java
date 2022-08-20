package academy.mindswap.ServerElements.GameElements.Obstacles;

public abstract class Obstacle {
    private boolean visitedRoom = false;
    private final String MAP_IDENTIFIER;

    public Obstacle(String MAP_IDENTIFIER) {
        this.MAP_IDENTIFIER = MAP_IDENTIFIER;
    }

    public String getMAP_IDENTIFIER() {
        if(!visitedRoom){
            return "?";
        }
        return MAP_IDENTIFIER;
    }
    public void visitRoom(){
        visitedRoom = true;
    }
}
