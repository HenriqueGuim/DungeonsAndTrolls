package academy.mindswap;

import java.io.*;
import java.net.Socket;

public class Player {

    private Socket serverSocket;
    private BufferedReader serverReader;
    private BufferedWriter serverWriter;
    private BufferedReader consoleReader;

    public static void main(String[] args) {
        Player player = new Player();
        player.setConsoleReader();
        player.handleServer();
    }

    private void handleServer() {
        setServer();
        createServerComms();
        serverListener();
    }

    private void serverWriter() {
        try {
            serverWriter.write(consoleReader.readLine());
            serverWriter.newLine();
            serverWriter.flush();
        } catch (IOException e) {
            return;
        }
    }

    private void createServerComms() {
        try {
            serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            serverWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setServer() {
        try {
            serverSocket = new Socket("localhost", 8080);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setConsoleReader() {
        try {
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void serverListener() {
        String message = null;
        try {
            message = serverReader.readLine();
            if (message.equals("-3")) {
                serverListener();
            }
            if (message.equals("-2")) {
                serverWriter();
                serverListener();
            }
            System.out.println(message);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverListener();
    }


    private class ServerListner implements Runnable {
        public void serverListener() {
            String message = null;
            try {
                message = serverReader.readLine();
                System.out.println(message);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serverListener();
        }

        @Override
        public void run() {
            serverListener();
        }
    }
}

