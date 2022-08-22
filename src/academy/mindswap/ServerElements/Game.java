package academy.mindswap.ServerElements;

import academy.mindswap.ServerElements.GameElements.Obstacles.Monsters.*;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.BadChest;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.EmptyRoom;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.Fairy;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.GoodChest;
import academy.mindswap.ServerElements.GameElements.PlayerCharacters.Character;
import academy.mindswap.ServerElements.GameElements.PlayerCharacters.Knight;
import academy.mindswap.ServerElements.GameElements.PlayerCharacters.Mage;
import academy.mindswap.ServerElements.GameElements.PlayerCharacters.Squire;

import java.io.*;
import java.util.*;

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


    public Game(ClientHandler[] clientHandlers, Server server) {
        player1 = clientHandlers[0];
        player2 = clientHandlers[1];
        player3 = clientHandlers[2];
        player1Name = player1.getName();
        player2Name = player2.getName();
        player3Name = player3.getName();
        this.server = server;
        playersPosition = new int[]{0, 0};
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

    private Collection<Obstacle> createObstacles() {
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
        broadcast("\033[1;31m" + "::::::::MAP::::::::" + "\033[0m");
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
            broadcast(message);
        }
        broadcast("You are at the position of " + "\033[42m" + "  " + "\033[0m" + " background.");
    }


    @Override
    public void run() {

        //TODO say to the players that the game is starting
        //TODO ask the players to choose a character
        //TODO show the players the map
        //TODO ask the players to choose the moving direction and vote, if the vote isn't unanimous they will move in a random direction
        //TODO if the players are on the edge of the map, they have to vote again
        //TODO check which type of obstacle is on the players position and act accordingly
        //TODO if is a chest obstacle, the players have to vote to open it.
        //TODO if is a fairy obstacle, all the players receive a boost in his health
        //TODO if is a monster obstacle, the players have to attack and defend the monster until it dies (the monster should attack the player with less health)
        //TODO the players have to choose his action for the round
        //TODO show the players the character stats
        //TODO show the players the map and repeat until they move to the final boss room

        createMap();
        playersChooseCharacters();
        startGame();
                //askToPlayAgain();
    }

    private void startGame() {
        sendIntro();
        playGame();



    }

    private void playGame() {
        showMap();
        voteToMove();
        playGame();
    }

    private void voteToMove() {
        broadcast("\033[1;31m" + "::::::::VOTE TO MOVE::::::::" + "\033[0m");
        broadcast("You must vote to move in a direction.");
        broadcast("Type 'N' to move north, 'S' to move south, 'E' to move east, 'W' to move west.");
        move(countVotes());



    }

    private void move(char direction) {
        switch (direction){
            case 'N':
                playersPosition[0]--;
                break;
            case 'S':
                playersPosition[0]++;
                break;
            case 'E':
                playersPosition[1]++;
                break;
            case 'W':
                playersPosition[1]--;
                break;
        }
        map[playersPosition[0]][playersPosition[1]].visitRoom();
    }

    private char countVotes() {
        int[] votesCounter = new int[]{0, 0, 0, 0};

        char[] votes = new char[3];

        int deadPlayers = 0;

        try {
            votes[0] = player1.getVote();
            votes[1] = player2.getVote();
            votes[2] = player3.getVote();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (char x :votes) {
            switch (x) {
                case 'N':
                    votesCounter[0]++;
                    break;
                case 'S':
                    votesCounter[1]++;
                    break;
                case 'E':
                    votesCounter[2]++;
                    break;
                case 'W':
                    votesCounter[3]++;
                    break;
                default:deadPlayers++;
                    break;
            }
        }

        int max = -1;

        if(deadPlayers < 2) {
        for (int i = 0; i < votesCounter.length; i++) {
                if(votesCounter[i] >= 2)
                max = i;
            }
        }

        if(max == -1) {
            max = randomDirection();
        }

        return checkIfEdge(max);
       }

    private char checkIfEdge(int direction) {
        if(direction == 0 && playersPosition[0] !=0){
            return 'N';
        }
        else if(direction == 1 && playersPosition[0] != 5){
            return 'S';
        }
        else if(direction == 2 && playersPosition[1] != 0){
            return 'E';
        }
        else if(direction == 3 && playersPosition[1] != 5){
            return 'W';
        }

        return checkIfEdge(randomDirection());
    }

    private int randomDirection() {
        return new Random().nextInt(4);
    }


    private void sendIntro() {
        readFile();
        broadcast("----------------------------------------------------");
    }

    private void readFile() {
        File file = new File("resources/Narrator/Intro.txt");
        String message = "";
        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(file));
            while ((message = fileReader.readLine()) != null) {
                broadcast(message);
                Thread.sleep(10);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void broadcast(String message) {
        try {
            player1.sendMessage(message);
            player2.sendMessage(message);
            player3.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Character chooseCharacter(ClientHandler clientHandler) {
        String characterNumber ="";
        Character character = null;
        try {
            clientHandler.sendMessage("Choose you character from the above:");
            clientHandler.sendMessage("1.Mage  2.Knight 3.Squire\nplease insert the number of the character");
            characterNumber = clientHandler.readMessage();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            switch (characterNumber){
                case "1": character = new Mage();
                break;
                case "2": character= new Knight();
                break;
                case "3": character = new Squire();
                break;
                default: character = chooseCharacter(clientHandler);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return character;
    }
    private void playersChooseCharacters() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                player1Character = chooseCharacter(player1);
                player1.setCharacter(player1Character);
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                player2Character = chooseCharacter(player2);
                player2.setCharacter(player2Character);
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                player3Character = chooseCharacter(player3);
                player3.setCharacter(player3Character);
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        broadcast("----------------------------------------------------");
    }
}


