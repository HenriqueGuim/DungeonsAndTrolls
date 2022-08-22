package academy.mindswap.ServerElements;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {

    ServerSocket serverSocket;
    CopyOnWriteArrayList<ClientHandler> clientsList;
    ExecutorService threadPool;


    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);

    }

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

    private void welcomeMessage(ClientHandler clientHandler) throws IOException {
        File file = new File("resources/Art/WelcomeMessage.txt");
        BufferedReader welcomeReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = welcomeReader.readLine()) != null) {
            clientHandler.sendMessage("\033[0;92m" +line + "\033[0m");
        }
    }

    private void connectPlayers() {
        ClientHandler[] clientHandlers = new ClientHandler[3];
        int playerCount = 0;

        for (ClientHandler clientHandler : clientsList) {
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
            clientsList.forEach(client -> {
                try {
                    client.sendMessage("starting a new game");
                } catch (IOException e) {
                        clientsList.remove(client);
                }
            });
            Game game = new Game(clientHandlers, this);
            threadPool.submit(game);
        }
    }
}
