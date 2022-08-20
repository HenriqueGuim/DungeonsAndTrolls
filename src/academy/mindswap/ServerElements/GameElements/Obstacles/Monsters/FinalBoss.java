package academy.mindswap.ServerElements.GameElements.Obstacles.Monsters;

public class FinalBoss extends Monsters{
    public FinalBoss() {
        super(200, 40);
    }

    private String mapIdentifier = "B";

    @Override
    public String getMapIdentifier() {
        return mapIdentifier;
    }
}
