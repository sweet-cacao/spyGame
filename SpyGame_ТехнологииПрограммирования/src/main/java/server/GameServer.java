package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.UID;
import java.util.*;
import java.util.stream.Stream;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    private final int MAX_PLAYERS = 2;

    private final List<Integer> ids = new ArrayList<>();
    private final Map<Integer, Socket> sockets = new HashMap<>();
    private final Map<Integer, ServerSideConnection> serverSideConnections = new HashMap<>();
    private final int maxTurns;

    private int spyId;
    private String imageName;
    private final List<String> images = new ArrayList<>();

    public GameServer() {
        System.out.println("-----Game Server-----");
        numPlayers = 0;
        maxTurns = 4;
        try {
            ss = new ServerSocket(51734);
        } catch (IOException e) {
            System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            while (numPlayers < MAX_PLAYERS) {
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected.");
                int id = new UID().hashCode();
                if (id < 1) { id *= -1; }
                System.out.println("Player number " + numPlayers + " has id #" + id);
                ids.add(id);
                sockets.put(id, s);
            }
            System.out.println("We now have" + MAX_PLAYERS + "players. No longer accepting connections");
        } catch (IOException e) {
            System.out.println("IOException from acceptConnections() in GameServer");
        }
    }

    public void createServerSideConnections() {
        for (Integer id : ids) {
            serverSideConnections.put(id, new ServerSideConnection(sockets.get(id), id, maxTurns, ids, spyId, serverSideConnections, imageName));
            Thread t = new Thread(serverSideConnections.get(id));
            t.start();
        }
    }

    private void generateSpy() {
        Random r = new Random();
        int num = r.nextInt(MAX_PLAYERS);
        spyId = ids.get(num);
    }

    private void generatePictureForPlayers() {
        getImages();
        Random rnd = new Random();
        imageName = images.get(rnd.nextInt(images.size() - 1));
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
        gs.generateSpy();
        gs.generatePictureForPlayers();
        gs.createServerSideConnections();
    }

    private void getImages() {
        try (Stream<Path> paths = Files.walk(Paths.get("./src/main/resources/images"))) {
            paths.filter(Files::isRegularFile).forEach(element -> images.add(element.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
