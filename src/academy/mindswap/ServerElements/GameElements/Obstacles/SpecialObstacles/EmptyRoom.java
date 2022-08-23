package academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;

public class EmptyRoom extends Obstacle {
    private String rubbleRoom = "There is a lot of rubble in this room...";
    private String smellyRoom = "There is a lingering smell of rot in this room.";
    private String trashRoom = "This room is crowded with trash. It's hard to go through it.";

    private String[] roomMessages = {rubbleRoom, smellyRoom, trashRoom};
    public EmptyRoom() {
        super("E");
    }

    public String getRoomMessage() {
        return roomMessages[(int) (Math.random() * roomMessages.length)];
    }

}
