package academy.mindswap.ServerElements.GameElements.Obstacles;

/**
 * This class represent all the obstacle present in the game's map
 */
public abstract class Obstacle {

    private boolean visitedRoom = false;
    private final String MAP_IDENTIFIER;

    /**
     * Class constructor for identifying the obstacle in the map
     * @param MAP_IDENTIFIER location of the obstacle
     */
    public Obstacle(String MAP_IDENTIFIER) {
        this.MAP_IDENTIFIER = MAP_IDENTIFIER;
    }

    /**
     * Method to hide the obstacle's location in unexplored coordinates
     * @return identification of the obstacle in visited room coordinates
     */
    public String getMAP_IDENTIFIER() {
        if(!visitedRoom){
            return "?";
        }
        return MAP_IDENTIFIER;
    }

    /**
     * Method called when visiting an unexplored room
     */

    public void visitRoom(){
        visitedRoom = true;
    }

    /**
     *
     * @return true when the room is already visited
     */
    public boolean isVisitedRoom() {
        return visitedRoom;
    }
}