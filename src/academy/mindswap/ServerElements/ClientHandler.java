package academy.mindswap.ServerElements;

import academy.mindswap.ServerElements.GameElements.PlayerCharacters.Character;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private Socket playerSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;
    private boolean isPlaying = false;

    public Character getCharacter() {
        return character;
    }

    private Character character;
    public ClientHandler(Socket accept) {
        this.playerSocket = accept;
        startBuffers();
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    private void startBuffers() {
        try {
            reader = new BufferedReader( new InputStreamReader(playerSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOffline(){
        return !playerSocket.isConnected();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



        public String readMessage() {
        sendMessage("-2");
        String message = null;
        try {
            message = reader.readLine();
            if (message == null) {
                playerSocket.close();
                isPlaying = true;
                return "-1";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            return message;
    }
    public void sendMessage(String message)  {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException ignored) {
        }
    }


    public boolean isPlaying() {
        return isPlaying;
    }
    public void startGame(){
        isPlaying = true;
    }
    public void endGame(){
        isPlaying = false;
    }

    public char getMoveVote() throws IOException {
        if (character.isDead()){
            return ' ';
        }

        String message = null;
        message = readMessage();
        if (message.equals("-1")){
            character.die();
            return ' ';
        }

        if(message.trim().equalsIgnoreCase("N")){
            return 'N';
        }
        if(message.trim().equalsIgnoreCase("S")){
            return 'S';
        }
        if(message.trim().equalsIgnoreCase("E")){
            return 'E';
        }
        if(message.trim().equalsIgnoreCase("W")){
            return 'W';
        }
        sendMessage("Invalid vote entered. Please enter a valid vote.");

        return getMoveVote();
    }
    public char getChestVote() throws IOException {
        if (character.isDead()){
            return ' ';
        }
        String message = null;
        message = readMessage();

        if (message.equals("-1")){
            character.die();
            return ' ';
        }

        if(message.trim().equalsIgnoreCase("yes")){
            return 'y';
        }
        if(message.trim().equalsIgnoreCase("no")){
            return 'n';
        }

        sendMessage("Invalid vote entered. Please enter a valid vote.");

        return getChestVote();
    }

    public void chooseMove() {
        if(character.isDead()){
            return;
        }
        String message = null;
        message = readMessage();
        if (message.equals("-1")){
            character.die();
            return;
        }

        if (message.equals("2")){
            character.chooseDodge();
            return;
        }
        if(message.equals("3")){
            character.chooseDefend();
            return;
        }
        if(!message.equals("1")){
                sendMessage("Invalid move entered. Please enter a valid move.");

            chooseMove();
        }
    }
}
