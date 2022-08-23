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


/**
 * This class represents the game process and handles the players and the obstacles.
 */
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

    /**
     * Constructor for the game.
     * @param server The server that the game is running on.
     * @param clientHandlers An array with of the three client handlers of the players.
     */
    public Game(ClientHandler[] clientHandlers, Server server) {
        player1 = clientHandlers[0];
        player2 = clientHandlers[1];
        player3 = clientHandlers[2];
        player1Name = player1.getName();
        player2Name = player2.getName();
        player3Name = player3.getName();
        this.server = server;
        playersPosition = new int[]{0, 0};
        player1.startGame();
        player2.startGame();
        player3.startGame();
    }

    /**
     * This method creates a randomized map for the game with a 6x6 size. The number of obstacles is manged by the method createObstacles.
     */
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

    /**
     * This method creates a list of obstacles for the game and randomizes them before returning them.
     */
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

    /**
     * This method broadcasts the map to the players with visual representation of the obstacles case have been visited and indicates the player's position.
     */
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

    /**
     * this method overrides the run method of the Runnable interface to start the game.
     */
    @Override
    public void run() {
        createMap();
        playersChooseCharacters();
        startGame();
    }

    /**
     * This method broadcasts the intro of the game to the players and starts the game.
     */
    private void startGame() {
        sendIntro();
        playGame();
    }

    /**
     * this method is responsible for the game loop in a recursive way.
     */
    private void playGame() {
        showMap();
        voteToMove();
        handleRoom();
        sendStatus();

        if(!checkIfBossDefeated()){
            playGame();
        }
        winGame();
    }

    /**
     * this method presents the game over panel to the players and asks them if they want to play again.
     */
    private void gameOver() {
        try {
            readFileRed("resources/Art/GameOver.txt");
            Thread.sleep(500);
            readFile("resources/Narrator/OutroLose.txt");
            Thread.sleep(500);
            broadcast("-".repeat(40));
            Thread.sleep(500);
            verifyIfWantToPlay();
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method presents the win game panel to the players and asks them if they want to play again.
     */
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
        verifyIfWantToPlay();
        Thread.currentThread().interrupt();
    }

    /**
     * This method verifies if the players want to play again.
     */

    private void verifyIfWantToPlay() {
        broadcast("Want to continue playing?");
        Thread thread1 = new Thread(()->endGameResponse(player1));
        Thread thread2 = new Thread(()->endGameResponse(player2));
        Thread thread3 = new Thread(()->endGameResponse(player3));
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * This method is responsible to get the end game response of the players.
     */
    private void endGameResponse(ClientHandler player) {
        String response = player.readMessage();
        response.toLowerCase();
        switch (response){
            case "yes": player.sendMessage("Wait while we search for a new opponent");
                player.endGame();
                server.connectPlayers();
                return;
            case "no": player.sendMessage("Thank you for playing with us.");
                server.clientsList.remove(player);
                return;
            case "-1": server.clientsList.remove(player);
                return;
            default: player.sendMessage("Please insert a valid message: Yes or No");
                endGameResponse(player);
        }
    }

    /**
     * This method verifies the room that the players entered and call the appropriate method to handle the room.
     */
    private void handleRoom() {
        if (map[playersPosition[0]][playersPosition[1]].getClass() == EmptyRoom.class) {
            handleEmptyRoom();
            return;
        }
        if (map[playersPosition[0]][playersPosition[1]] instanceof Attackable) {
            handleMonster();
            return;
        }
        if (map[playersPosition[0]][playersPosition[1]].getClass() == GoodChest.class || map[playersPosition[0]][playersPosition[1]].getClass() == BadChest.class){
            handleChest();
            return;
        }

        if (map[playersPosition[0]][playersPosition[1]].getClass() == Fairy.class) {
            handleFairy();
            return;
        }
    }

    /**
     * This method handles the monsters rooms and initiates the fight.
     */
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

    /**
     * This method verify if the player has defeated the final boss usually used to win the game.
     * @return true if the final boss has been defeated and false otherwise.
     */
    private boolean checkIfBossDefeated() {
        Monsters finalBoss = (Monsters) map[5][5];
        if (finalBoss.isDead()) {
            return true;
        }
        return false;
    }

    /**
     * Method responsible to broadcast that the monster has been defeated.
     */
    private void monsterDefeated() {
        broadcast("Well done! You destroyed the monster.");
    }
    /**
     * This method introduces the monster to the players' case the monster already have been defeated.
     */
    private void introDeadMonster(Monsters monster) {
        broadcast ("You encounter a dead " + monster.getClass().getSimpleName());
    }

    /**
     * This method introduces the monster to the players.
     * @param monsters the monster to be introduced.
     */

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
            broadcast("\033[1;35"+ "::::::::::: FINAL BOSS :::::::::::" +"\033[0;m");
            monsterFile = "resources/Art/Boss";
        }
        readFileRed(monsterFile);

    }

    /**
     * this method executes the fight between the players and the monster in the room that the players are in.
     */
    private void fight() {
        Monsters monster = (Monsters) map[playersPosition[0]][playersPosition[1]];
        chooseMove();
        monsterAttack(monster);


        if(!player1Character.isDead()){
            monster.defend(player1Character.attack());
        }
        if (monster.isDead()) {
            monster.die();
            return;
        }
        if(!player2Character.isDead()){
            monster.defend(player2Character.attack());
        }
        if (monster.isDead()) {
            monster.die();
            return;
        }
        if(!player3Character.isDead()){
            monster.defend(player3Character.attack());
        }
        if (monster.isDead()) {
            monster.die();
            return;
        }
        sendStatus();
        fight();
    }

    /**
     * This method indicates to the players their personal status.
     */
    private void sendStatus() {
            player1.sendMessage("STATUS: " + player1Character.getHealth()+ " HP");
            player2.sendMessage("STATUS: " + player2Character.getHealth()+ " HP");
            player3.sendMessage("STATUS: " + player3Character.getHealth()+ " HP");
    }

    /**
     * This method is responsible to choose the player that the monster will attack and execute the attack.
     * Verifies if the player was killed by the monster after the attack.
     * @param monster the monster that will attack.
     */
    private void monsterAttack(Monsters monster) {
        ClientHandler playerToAttack;
        if (!player1.getCharacter().isDead()){playerToAttack = player1;} else if (!player2.getCharacter().isDead())
        {playerToAttack = player2;} else {playerToAttack = player3;}


        if (playerToAttack.getCharacter().getHealth() > player2Character.getHealth() && !player2Character.isDead()) {
            playerToAttack = player2;
        }
        if (playerToAttack.getCharacter().getHealth() > player3Character.getHealth() && !player3Character.isDead()) {
            playerToAttack = player3;
        }

        broadcast(monster.getClass().getSimpleName() + " Attack " + playerToAttack.getName());
        playerToAttack.getCharacter().sufferAttack(monster.getDamage());
        checkIfDead(playerToAttack.getCharacter());
    }

    /**
     * This method is responsible to check if the player is dead and all players are dead calls the endGame method.
     * If the player in question is dead calls the method die
     * @param playerToAttack the player to be checked.
     */
    private void checkIfDead(Character playerToAttack) {
        if (playerToAttack.isDead()) {
            playerToAttack.die();
        }
        if(player1Character.isDead()&& player2Character.isDead() && player3Character.isDead()){
            gameOver();
        }
    }

    /**
     * This method asks the players to choose a move.
     */
    private void chooseMove() {
        broadcast("Choose your move from list bellow:");
        broadcast("1. Attack 2. Dodge 3. Defend");
        playersChooseMove();
    }

    /**
     * This method asks tho the PlayerHandler deal with the move chosen.
     */
    private void playersChooseMove() {
        player1.chooseMove();
        player2.chooseMove();
        player3.chooseMove();
    }

    /**
     * This method is responsible to handle the chest room.
     */
    private void handleChest() {
        broadcast("\033[1;31m" + "::::::::CHEST::::::::" + "\033[0m");
        broadcast("You found a chest!");
        readFileGreen("resources/Art/Chest.txt");
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

    /**
     * This method is responsible to count the votes of the players to open the chest and execute their choice.
     * If they didn't get an majority of votes they will be asked to vote again.
     */
    private void countChestVotes() throws IOException {
        int openChestVotes = 0;
        int dontOpenChestVotes = 0;

        broadcast("Please vote to open the chest or not with the commands: 'yes' to open or 'no' to not open.");
       if(!player1Character.isDead()){
           if(player1.getChestVote() == 'y'){
               openChestVotes++;
           }
           else{
               dontOpenChestVotes++;
           }
       }
       if (!player2Character.isDead()){
            if(player2.getChestVote() == 'y'){
             openChestVotes++;
            }
            else{
            dontOpenChestVotes++;
            }
        }
       if(!player3Character.isDead()) {
           if (player3.getChestVote() == 'y') {
               openChestVotes++;
           } else {
               dontOpenChestVotes++;
           }
       }
        if (openChestVotes > dontOpenChestVotes) {
            openChest();
            return;
        }
        if(openChestVotes == dontOpenChestVotes){
            broadcast("Please get to an agreement");
            countChestVotes();
        }

        dontOpenChest();
    }

    /**
     * This method is responsible to not open the chest.
     */
    private void dontOpenChest() {
        broadcast("The chest remains closed.");
    }

    /**
     * This method is responsible to open the chest and execute the consequences of open it.
     */
    private void openChest() {
        broadcast("The chest is open!");
        broadcast("you have found a warm meal!\nYou ate it.");
        Chest chest = (Chest) map[playersPosition[0]][playersPosition[1]];
        chest.open();
        if(map[playersPosition[0]][playersPosition[1]].getClass() == GoodChest.class){
            if(!player1Character.isDead()) {
                player1.sendMessage("you have got a boost in your health!");
                player1Character.increaseHealth(chest.getHealthModifier());
            }
            if(!player2Character.isDead()) {
                player2.sendMessage("you have got a boost in your health!");
                player2Character.increaseHealth(chest.getHealthModifier());
            }
            if(!player3Character.isDead()) {
                player3.sendMessage("you have got a boost in your health!");
                player3Character.increaseHealth(chest.getHealthModifier());
            }

            return;
        }
        if(!player1Character.isDead()) {
            player1.sendMessage("The meal was spoiled! you lose some health!");
            player1Character.increaseHealth(chest.getHealthModifier());
        }
        if(!player2Character.isDead()) {
            player2.sendMessage("The meal was spoiled! you lose some health!");
            player2Character.increaseHealth(chest.getHealthModifier());
        }
        if(!player3Character.isDead()) {
            player3.sendMessage("The meal was spoiled! you lose some health!");
            player3Character.increaseHealth(chest.getHealthModifier());
        }


    }

    /**
     * This method is responsible to handle the fairy room.
     */
    private void handleFairy() {
        Fairy fairy = (Fairy) map[playersPosition[0]][playersPosition[1]];
        readFileGreen("resources/Art/Fairy");
        if(!fairy.hasCured()){
            fairy.visitRoom();
            broadcast("\033[0;92m" + "The players have found a fairy!" + "\033[0m");
            broadcast("\033[0;92m" + "The players that reach this point had his life restored in " + fairy.getHealthModifier() + " points!" + "\033[0m");
            
            if(!player1Character.isDead()){player1Character.increaseHealth(fairy.getHealthModifier());}
            if(!player2Character.isDead()){player2Character.increaseHealth(fairy.getHealthModifier());}
            if(!player3Character.isDead()){player3Character.increaseHealth(fairy.getHealthModifier());}

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            fairy.cure();
            return;
        }
        broadcast("\033[0;92m" + "You have found a fairy!" + "\033[0m");
        broadcast("But already have healed you once!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * This method is responsible to handle the empty room.
     */
    private void handleEmptyRoom() {
        EmptyRoom emptyRoom = (EmptyRoom) map[playersPosition[0]][playersPosition[1]];
        broadcast(emptyRoom.getRoomMessage());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is responsible to start the moving of the players by voting process.
     */
    private void voteToMove() {
        broadcast("\033[1;31m" + "::::::::VOTE TO MOVE::::::::" + "\033[0m");
        broadcast("You must vote to move in a direction.");
        broadcast("Type 'N' to move north, 'S' to move south, 'E' to move east, 'W' to move west.");
        move(countVotes());



    }

    /**
     * This method is responsible to move the players in the direction they voted.
     * @param direction the direction the players voted.
     */
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

    /**
     * This method is responsible to count the votes of the players to move.
     * If the players get a majority of votes, the players will move to the most voted direction, otherwise the players will move randomly.
     * If the players try to move to a wall, the players will move randomly.
     * @return the direction that the players voted or the random move direction if appropriate.
     */
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
        }else {for(int i = 0; i < votesCounter.length; i++) {
            if(votesCounter[i] > max)
                max = i;
        }
        }

        if(max == -1) {
            max = randomDirection();
        }

        return checkIfEdge(max);
       }

    /**
     * This method is responsible to check if the players want to move to a wall.
     * @param direction
     * @return the direction the players want to move to if appropriate, otherwise return a random direction.
     */
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
    /**
     * This method is responsible to get a random direction.
     * @return a random direction.
     */
    private int randomDirection() {
        return new Random().nextInt(4);
    }

    /**
     * This method is responsible to broadcast the introduction of the game.
     */
    private void sendIntro() {
        try {
            readFile("resources/Narrator/Intro.txt");
            Thread.sleep(50);
            broadcast("-".repeat(40));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is responsible to broadcast the content of the file introduced in the parameter.
     * @param path the path of the file to read.
     */
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
    /**
     * This method is responsible to broadcast the content in green of the file introduced in the parameter.
     * @param path the path of the file to read.
     */
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
    /**
     * This method is responsible to broadcast the content in Red of the file introduced in the parameter.
     * @param path the path of the file to read.
     */
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

    /**
     * this method is responsible to broadcast the message to all the players.
     * @param message the message to broadcast.
     */
    private void broadcast(String message) {
            player1.sendMessage(message);
            player2.sendMessage(message);
            player3.sendMessage(message);
    }

    /**
     * This method asks the players to choose a character.
     * @param clientHandler the clientHandler of the player.
     * @return the character the player chose.
     */
    public Character chooseCharacter(ClientHandler clientHandler) {
        String characterNumber ="";
        Character character = null;
        clientHandler.sendMessage("Choose you character from the above:");
        clientHandler.sendMessage("1.Mage  2.Knight 3.Squire\nplease insert the number of the character");
        characterNumber = clientHandler.readMessage();

        try {
            switch (characterNumber){
                case "1": character = new Mage();
                break;
                case "2": character= new Knight();
                break;
                case "3": character = new Squire();
                break;
                case "-1": character = new Squire();
                break;
                default: character = chooseCharacter(clientHandler);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return character;
    }

    /**
     * This method starts the phase of choosing the character and associates the player with the character.
     */
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