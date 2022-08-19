package academy.mindswap.Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private Socket playerSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;
    private boolean isPlaying = false;
    public ClientHandler(Socket accept) {
        this.playerSocket = accept;
        startBuffers();
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
        return playerSocket.isClosed();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String readMessage() throws IOException {
            String message = reader.readLine();
            if (message == null) {
                playerSocket.close();
            }
            return message;
    }
    public void sendMessage(String message) throws IOException {
            writer.write(message);
            writer.newLine();
            writer.flush();
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
}
