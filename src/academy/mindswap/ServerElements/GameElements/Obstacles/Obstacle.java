package academy.mindswap.ServerElements.GameElements.Obstacles;

public abstract class Obstacle {
    private boolean visitedRoom = false;
    private String mapIdentifier = "X" ;
    public String getMapIdentifier() {return mapIdentifier;}
    public boolean isVisitedRoom() {return visitedRoom;}
}
