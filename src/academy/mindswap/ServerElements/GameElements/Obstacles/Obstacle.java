package academy.mindswap.ServerElements.GameElements.Obstacles;

public abstract class Obstacle {
    private String mapIdentifier = "X" ;
    public String getMapIdentifier() {return mapIdentifier;}
    @Override
    public String toString() {
        return  mapIdentifier;
    }
}
