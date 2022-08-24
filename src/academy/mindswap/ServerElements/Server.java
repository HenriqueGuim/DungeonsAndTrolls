package academy.mindswap.ServerElements;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the server operations
 */

public class Server {

    ServerSocket serverSocket;
    CopyOnWriteArrayList<ClientHandler> clientsList;
    ExecutorService threadPool;



    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);

    }

    /**
     * This method is responsible to execute the server
     * @param portNumber
     */

    private void start(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            clientsList = new CopyOnWriteArrayList<ClientHandler>();
            threadPool = Executors.newCachedThreadPool();

            System.out.println("Server is Running");
            acceptPlayers();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method called to accept and connect clients to the server
     */
    private void acceptPlayers() {
        ClientHandler clientHandler = null;
            try {
                System.out.println("Waiting for players");
                clientHandler = new ClientHandler(serverSocket.accept());
                welcomeMessage(clientHandler);
                clientHandler.sendMessage("Welcome to the server\nPlease enter your name");
                clientHandler.setName(clientHandler.readMessage());
                clientHandler.sendMessage("Welcome to the server " + clientHandler.getName());
                clientHandler.sendMessage("waiting for other players");
                clientsList.add(clientHandler);
                System.out.println("Player " + clientHandler.getName() + " has joined the server");
                connectPlayers();
                acceptPlayers();
            } catch (IOException e) {
                clientsList.remove(clientHandler);
                acceptPlayers();
            }
        
    }

    /**
     * This method is used to implement a welcome message to the clients connected to the server
     * @param clientHandler
     * @throws IOException
     */

    private void welcomeMessage(ClientHandler clientHandler) throws IOException {
        File file = new File("resources/Art/WelcomeMessage.txt");
        BufferedReader welcomeReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = welcomeReader.readLine()) != null) {
            clientHandler.sendMessage("\033[0;92m" +line + "\033[0m");
        }
    }

    /**
     * This method is responsible to connect a limited number of clients to the server
     */
    void connectPlayers() {
        //removeOfflinePlayers();
        ClientHandler[] clientHandlers = new ClientHandler[3];
        int playerCount = 0;

        for (ClientHandler clientHandler : clientsList) {
            clientHandler.sendMessage("-3");

            if (playerCount == 3) {
                break;
            }



            if (!clientHandler.isPlaying() && !clientHandler.isOffline()) {
                clientHandlers[playerCount] = clientHandler;

                playerCount++;
            }
        }

        if (playerCount == 3) {
            System.out.println("starting a new game");
            clientHandlers.forEach(client -> {
                client.sendMessage("starting a new game");
            });
            Game game = new Game(clientHandlers, this);
            threadPool.submit(game);
        }
    }

    /**
     * This method is responsible for remove all the clients that are currently disconnected
     */

    private void removeOfflinePlayers() {
        ArrayList<ClientHandler> offlinePlayers = new ArrayList<ClientHandler>();
        for (ClientHandler clientHandler : clientsList) {
            if (clientHandler.isOffline()) {
                offlinePlayers.add(clientHandler);
            }
        }
        clientsList.removeAll(offlinePlayers);
    }
}
