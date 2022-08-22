package academy.mindswap.ServerElements;

import academy.mindswap.ServerElements.GameElements.GameInterfaces.Attackable;
import academy.mindswap.ServerElements.GameElements.Obstacles.Monsters.*;
import academy.mindswap.ServerElements.GameElements.Obstacles.Obstacle;
import academy.mindswap.ServerElements.GameElements.Obstacles.SpecialObstacles.*;
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
        handleRoom();
        sendStatus();
        if(!checkIfBossDefeated()){playGame();}
        winGame();
    }
    private void gameOver() {
        try {
            readFileRed("resources/Art/GameOver.txt");
            Thread.sleep(500);
            readFile("resources/Narrator/OutroLose.txt");
            Thread.sleep(500);
            broadcast("-".repeat(40));
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void winGame() {
        try {
            readFileGreen("resources/Art/YouWin");
            Thread.sleep(500);
            readFile("resources/Narrator/OutroWin.txt");
            Thread.sleep(500);
            broadcast("-".repeat(40));
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleRoom() {
        if (map[playersPosition[0]][playersPosition[1]].getClass() == EmptyRoom.class) {
            handleEmptyRoom();
            return;
        }
        if (map[playersPosition[0]][playersPosition[1]] instanceof Attackable) {
            handleMonster();
            return;
        }
        if (map[playersPosition[0]][playersPosition[1]].getClass() == Chest.class) {
            handleChest();
            return;
        }

        if (map[playersPosition[0]][playersPosition[1]].getClass() == Fairy.class) {
            handleFairy();
            return;
        }
    }
    private void handleMonster() {
        Monsters monster = (Monsters) map[playersPosition[0]][playersPosition[1]];
        if(monster.isDead()){
            introDeadMonster(monster);
            return;
        }
        introduceMonster(monster);
        fight();
        monsterDefeated();

    }
    private boolean checkIfBossDefeated() {
        Monsters finalBoss = (Monsters) map[5][5];
        if (finalBoss.isDead()) {
            return true;
        }
        return false;
    }
    private void monsterDefeated() {
        broadcast("Well done! You destroyed the monster.");
    }
    private void introDeadMonster(Monsters monster) {
        broadcast ("You encounter a dead " + monster.getClass().getSimpleName());
    }
    private void introduceMonster(Monsters monsters) {
        broadcast("You encounter a " + monsters.getClass().getSimpleName());
        String monsterFile = "";
        if (Slime.class.equals(monsters.getClass())) {
            monsterFile = "resources/Art/Slime.txt";
        } if (Goblin.class.equals(monsters.getClass())) {
            monsterFile = "resources/Art/Goblin.txt";
        } if (Troll.class.equals(monsters.getClass())) {
            monsterFile = "resources/Art/Troll.txt";
        } if (MiniBoss.class.equals(monsters.getClass())) {
            monsterFile = "resources/Art/MiniBoss";
        } if (FinalBoss.class.equals(monsters.getClass())) {
            monsterFile = "resources/Art/Boss";
        }
        readFileRed(monsterFile);
    }
    private void fight() {
        Monsters monster = (Monsters) map[playersPosition[0]][playersPosition[1]];
        chooseMove();
        monsterAttack(monster);

        monster.defend(player1Character.attack());
        if (monster.isDead()) {
            monster.die();
            return;
        }
        monster.defend(player2Character.attack());
        if (monster.isDead()) {
            monster.die();
            return;
        }
        monster.defend(player3Character.attack());
        if (monster.isDead()) {
            monster.die();
            return;
        }
        sendStatus();
        fight();
    }
    private void sendStatus() {
        try {
            player1.sendMessage("STATUS: " + player1Character.getHealth()+ " HP");
            player2.sendMessage("STATUS: " + player2Character.getHealth()+ " HP");
            player3.sendMessage("STATUS: " + player3Character.getHealth()+ " HP");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void monsterAttack(Monsters monster) {
        Character playerToAttack = player1Character;


        if (playerToAttack.getHealth() > player2Character.getHealth()) {
            playerToAttack = player2Character;
        }
        if (playerToAttack.getHealth() > player3Character.getHealth()) {
            playerToAttack = player3Character;
        }
        playerToAttack.sufferAttack(monster.getDamage());

        checkIfDead(playerToAttack);
    }
    private void checkIfDead(Character playerToAttack) {
        if (playerToAttack.isDead()) {
            playerToAttack.die();
        }
        if(player1Character.isDead()&& player2Character.isDead() && player3Character.isDead()){
            gameOver();
        }
    }
    private void chooseMove() {
        broadcast("Choose your move from list bellow:");
        broadcast("1. Attack 2. Dodge 3. Defend");
        playersChooseMove();
    }
    private void playersChooseMove() {
        player1.chooseMove();
        player2.chooseMove();
        player3.chooseMove();
    }
    private void handleChest() {
        broadcast("\033[1;31m" + "::::::::CHEST::::::::" + "\033[0m");
        Chest chest = (Chest) map[playersPosition[0]][playersPosition[1]];
        if(!chest.isOpen()) {
            try {
                countChestVotes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        broadcast("The chest is already open.");
    }
    private void countChestVotes() throws IOException {
        int openChestVotes = 0;
        int dontOpenChestVotes = 0;

        broadcast("Please vote to open the chest or not with the commands: 'yes' to open or 'no' to not open.");
        if(player1.getChestVote() == 'y'){
            openChestVotes++;
        }
        else{
            dontOpenChestVotes++;
        }
        if(player2.getChestVote() == 'y'){
            openChestVotes++;
        }
        else{
            dontOpenChestVotes++;
        }
        if(player3.getChestVote() == 'y'){
            openChestVotes++;
        }
        else{
            dontOpenChestVotes++;
        }

        if (openChestVotes > dontOpenChestVotes) {
            openChest();
            return;
        }

        dontOpenChest();
    }
    private void dontOpenChest() {
        broadcast("The chest remains closed.");
    }
    private void openChest() {
        broadcast("The chest is open!");
        broadcast("you have found a warm meal!\nYou ate it.");
        Chest chest = (Chest) map[playersPosition[0]][playersPosition[1]];
        chest.open();
        if(map[playersPosition[0]][playersPosition[1]].getClass() == GoodChest.class){
            broadcast("you have got a boost in your health!");
            player1Character.increaseHealth(chest.getHealthModifier());
            player2Character.increaseHealth(chest.getHealthModifier());
            player3Character.increaseHealth(chest.getHealthModifier());
            return;
        }

        broadcast("The meal was spoiled! you lose some health!");
        player1Character.decreaseHealth(chest.getHealthModifier());
        player2Character.decreaseHealth(chest.getHealthModifier());
        player3Character.decreaseHealth(chest.getHealthModifier());

    }
    private void handleFairy() {
        Fairy fairy = (Fairy) map[playersPosition[0]][playersPosition[1]];
        if(!fairy.hasCured()){
            fairy.visitRoom();
            broadcast("\033[1;31m" + "You have found a fairy!" + "\033[0m");
            broadcast("\033[1;31m" + "You have been healed by " + fairy.getHealthModifier() + " points!" + "\033[0m");
            
            player1Character.increaseHealth(fairy.getHealthModifier());
            player2Character.increaseHealth(fairy.getHealthModifier());
            player3Character.increaseHealth(fairy.getHealthModifier());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            fairy.cure();
            return;
        }
        broadcast("\033[1;31m" + "You have found a fairy!" + "\033[0m");
        broadcast("But already have healed you once!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    private void handleEmptyRoom() {
        EmptyRoom emptyRoom = (EmptyRoom) map[playersPosition[0]][playersPosition[1]];
        broadcast(emptyRoom.getRoomMessage());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            votes[0] = player1.getMoveVote();
            votes[1] = player2.getMoveVote();
            votes[2] = player3.getMoveVote();
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
        else if(direction == 2 && playersPosition[1] != 5){
            return 'E';
        }
        else if(direction == 3 && playersPosition[1] != 0){
            return 'W';
        }

        return checkIfEdge(randomDirection());
    }
    private int randomDirection() {
        return new Random().nextInt(4);
    }
    private void sendIntro() {
        try {
            readFile("resources/Narrator/Intro.txt");
            Thread.sleep(50);
            broadcast("-".repeat(40));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void readFile(String path) {
        File file = new File(path);
        String message = "";
        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(file));
            while ((message = fileReader.readLine()) != null) {
                broadcast(message);
                Thread.sleep(50);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void readFileGreen(String path) {
        File file = new File(path);
        String message = "";
        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(file));
            while ((message = fileReader.readLine()) != null) {
                broadcast("\033[0;92m"+message+ "\033[0m");
                Thread.sleep(50);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void readFileRed(String path) {
        File file = new File(path);
        String message = "";
        BufferedReader fileReader;
        try {
            fileReader = new BufferedReader(new FileReader(file));
            while ((message = fileReader.readLine()) != null) {
                broadcast("\033[1;31m"+message+ "\033[0m");
                Thread.sleep(50);
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
        broadcast("-".repeat(40));
    }
}