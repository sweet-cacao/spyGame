package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientSideConnection {
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    public String playerId;
    public String imageName;
    public String id;
    public String spyId;
    public List<String> ids;
    public ConcurrentMap<Integer, String> names = new ConcurrentHashMap<>();

    public ClientSideConnection() {
        System.out.println("---Client---");
        try {
            Socket socket = new Socket("localhost", 51734);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            playerId = dataIn.readUTF();
            id = playerId;
            spyId = dataIn.readUTF();
            imageName = dataIn.readUTF();
      //      int maxTurns = dataIn.readInt();
            int size = dataIn.readInt();
            ids = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ids.add(dataIn.readUTF());
            }
            System.out.println("Connected to server as player #" + playerId);
        } catch (Exception e) {
            System.out.println("IOException from csc constructor");
        }
    }

    public void sendString(String str) throws IOException {
        dataOut.writeUTF(str);
        dataOut.flush();
        System.out.println("The question was sent by csc: " + str);
    }

    public String getString() throws IOException {
        String str = dataIn.readUTF();
        System.out.println("The question is got by csc: " + str);
        return str;
    }
}
