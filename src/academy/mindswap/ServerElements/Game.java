package academy.mindswap.ServerElements;

import academy.mindswap.ServerElements.GameElements.Obstacles.Monsters.*;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.BadChest;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.EmptyRoom;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.Fairy;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.GoodChest;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game implements Runnable {
    private ClientHandler player1;
    private ClientHandler player2;
    private ClientHandler player3;

    private Character player1Character;
    private Character player2Character;
    private Character player3Character;
    private String player1Name;
    private String player2Name;
    private String player3Name;
    private Server server;
    private Obstacle[][] map;
    private int[] playersPosition;
    private ExecutorService threadPool;

    public Game(ClientHandler[] clientHandlers, Server server) {
        player1 = clientHandlers[0];
        player2 = clientHandlers[1];
        player3 = clientHandlers[2];
        player1Name = player1.getName();
        player2Name = player2.getName();
        player3Name = player3.getName();
        this.server = server;
        playersPosition = new int[]{0,0};
        threadPool = Executors.newCachedThreadPool();
        createMap();
    }

    private void createMap() {

        map = new Obstacle[][]{new Obstacle[6], new Obstacle[6], new Obstacle[6], new Obstacle[6], new Obstacle[6], new Obstacle[6]};
        map[0][0] = new EmptyRoom();
        map[5][5] = new FinalBoss();
        map[0][0].visitRoom();
        map[5][5].visitRoom();

        LinkedList<Obstacle> obstaclesList = new LinkedList<>(createObstacles());


        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == null) {
                    map[i][j] = obstaclesList.poll();
                }
            }
        }


    }

    private Collection<? extends Obstacle> createObstacles() {
        int goblinNumber = 6;
        int slimeNumber = 6;
        int trollNumber = 6;
        int badChestNumber = 3;
        int goodChestNumber = 3;
        int emptyRoomNumber = 5;
        int fairyNumber = 3;
        int miniBossNumber = 2;

        ArrayList<Obstacle> obstaclesList = new ArrayList<>();
        for (int i = 0; i < goblinNumber; i++) {
            obstaclesList.add(new Goblin());
        }
        for (int i = 0; i < slimeNumber; i++) {
            obstaclesList.add(new Slime());
        }
        for (int i = 0; i < trollNumber; i++) {
            obstaclesList.add(new Troll());
        }
        for (int i = 0; i < badChestNumber; i++) {
            obstaclesList.add(new BadChest());
        }
        for (int i = 0; i < goodChestNumber; i++) {
            obstaclesList.add(new GoodChest());
        }
        for (int i = 0; i < emptyRoomNumber; i++) {
            obstaclesList.add(new EmptyRoom());
        }
        for (int i = 0; i < fairyNumber; i++) {
            obstaclesList.add(new Fairy());
        }
        for (int i = 0; i < miniBossNumber; i++) {
            obstaclesList.add(new MiniBoss());
        }
        Collections.shuffle(obstaclesList);
        return obstaclesList;
    }


    public void showMap(){
        try {
        for (int i = 0; i < 6; i++) {
            String message = "";
            for (int j = 0; j < 6; j++) {
                if(i == playersPosition[0] && j ==playersPosition[1]) {
                    message = message.concat("\033[42m" + "[" + map[i][j].getMAP_IDENTIFIER() + "]" + "\033[0m");
                }
                else
                {
                    message = message.concat("[" + map[i][j].getMAP_IDENTIFIER() + "]" );
                }
            }
            player1.sendMessage(message);
            player2.sendMessage(message);
            player3.sendMessage(message);
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {

        //TODO say to the players that the game is starting
        //TODO ask the players to choose a character
        //TODO show the players the map
        //TODO ask the players to choose the moving direction and vote,
        // if the vote isn't unanimous they will move in a random direction
        //TODO if the players are on the edge of the map, they have to vote again
        //TODO check which type of obstacle is on the players position and act accordingly
              //TODO if is a chest obstacle, the players have to vote to open it.
              //TODO if is a fairy obstacle, all the players receive a boost in his health
              //TODO if is a monster obstacle, the players have to attack and defend the monster
              // until it dies, (the monster should attack the player with less health)
              //TODO the players have to choose his action for the round
        //TODO show the players the character stats
        //TODO show the players the map and repeat until they move to the final boss room

        createMap();
        showMap();
        // chooseCharacters();
        // playGame();
        // askToPlayAgain();
        }

}