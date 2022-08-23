package academy.mindswap.ServerElements;

import academy.mindswap.ServerElements.GameElements.PlayerCharacters.Character;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * This class is the client handler for the server.
 */
public class ClientHandler {
    private Socket playerSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;
    private boolean isPlaying = false;
    private boolean isOffline = false;

    public Character getCharacter() {
        return character;
    }

    private Character character;

    /**
     * Constructor for the client handler.
     * @param accept The socket that the client is connected to.
     */
    public ClientHandler(Socket accept) {
        this.playerSocket = accept;
        startBuffers();
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Starts the buffers for the client handler.
     */
    private void startBuffers() {
        try {
            reader = new BufferedReader( new InputStreamReader(playerSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the client is offline.
     * @return True if the client is offline, false otherwise.
     */
    public boolean isOffline(){

       InetAddress inetAddress = playerSocket.getInetAddress();
       int portNumber = playerSocket.getPort();
        try {
            Socket testSocket = new Socket(inetAddress, portNumber);
            testSocket.getInputStream();
            isOffline = !testSocket.isConnected();

        } catch (IOException ignored) {
        }

        return isOffline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Reads a message from the client.
     * @return
     */
    public String readMessage() {
        String message = null;
        try {
            sendMessage("-2");
            message = reader.readLine();
            if (message == null) {
                playerSocket.close();
                isPlaying = true;
                isOffline = true;
                return "-1";
            }
        } catch (IOException e) {
            isOffline = true;
        }
            return message;
    }
    /**
     * Sends a message to the client.
     * @param message The message to send.
     */
    public void sendMessage(String message)  {

        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            isOffline = true;
        }
    }

    /**
     * Checks if the client is playing.
     * @return True if the client is playing, false otherwise.
     */
    public boolean isPlaying() {
        return isPlaying;
    }
    /**
     * Changes the playing status of the client to true.
     */
    public void startGame(){
        isPlaying = true;
    }
    /**
     * Changes the playing status of the client to false.
     */
    public void endGame(){
        isPlaying = false;
    }

    /**
     * Receives the player vote to move and confirms the vote.
     * If the client is offline, it returns -1 and kills the character.
     * @return The vote.
     *
     */
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

    /**
     * Receives the player vote to open a chest and confirms the vote.
     * If the client is offline, it returns -1 and kills the character.
     * @return The vote.
     */
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
    /**
     * Receives the player desired move to the turn and executes the needed procedures to the move.
     * If the client is offline, it returns -1 and kills the character.
     */

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
