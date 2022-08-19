package academy.mindswap.Server;

import java.io.IOException;
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
            try {
                System.out.println("Waiting for players");
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                clientHandler.sendMessage("Welcome to the server\n Please enter your name");
                clientHandler.setName(clientHandler.readMessage());
                clientHandler.sendMessage("Welcome" + " to the server " + clientHandler.getName());
                clientHandler.sendMessage("waiting for other players");
                clientsList.add(clientHandler);
                System.out.println("Player " + clientHandler.getName() + " has joined the server");
                connectPlayers();
                acceptPlayers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        
    }

    private void connectPlayers() {
        ClientHandler[] clientHandlers = new ClientHandler[3];

        clientsList.forEach(client -> {
            if(clientHandlers[2] == null){
                return;}

            if(!client.isPlaying() && !client.isOffline()){
                for (ClientHandler position:clientHandlers) {
                    if (position == null){
                        position = client;
                    }
                }

            }
        });
        if(clientHandlers[2] != null){
            System.out.println("starting a new game");
            //Game game = new Game(clientHandlers, this);
            // threadPool.submit(game);
        }
    }


}
