package academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles;

public class Fairy extends SpecialObstacle{
    private boolean hascured = false;
    public Fairy() {
        super(20,"F");
    }
    public void cure(){
        hascured = true;
    }
    public boolean hasCured(){
        return hascured;
    }
}
