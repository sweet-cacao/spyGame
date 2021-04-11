package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientSideConnection {
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    public int playerId;
    public String imageName;
    public int id;
    public int spyId;
    public List<Integer> ids;
    public Map<Integer, String> names = new HashMap<>();

    public ClientSideConnection(String nickname) {
        System.out.println("---Client---");
        try {
            Socket socket = new Socket("localhost", 51734);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            playerId = dataIn.readInt();
            id = playerId;
            spyId = dataIn.readInt();
            imageName = dataIn.readUTF();
            int maxTurns = dataIn.readInt();
            int size = dataIn.readInt();
            ids = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ids.add(dataIn.readInt());
            }

            dataOut.writeUTF(nickname);
            dataOut.flush();
            int j = 0;

            for (int i = 0; i < size; i++) {
                int mapId = dataIn.readInt();
                String mapName = dataIn.readUTF();
                names.put(mapId, mapName);
            }
            dataOut.writeUTF("ready");
            dataOut.flush();
            while (j < size) {
                dataIn.readUTF();
                j++;
            }
            System.out.println("Connected to server as player #" + playerId);
            System.out.println("maxturns" + maxTurns);
        } catch (Exception e) {
            System.out.println("IOException from csc constructor");
        }
    }

    public void sendQuestion(String question) throws IOException {
        System.out.println("The question was sent by csc");
        dataOut.writeUTF(question);
        dataOut.flush();
    }

    public void sendPlayerToAnswer(String question) throws IOException {
        System.out.println("The player to answer was sent by csc");
        dataOut.writeUTF(question);
        dataOut.flush();
    }

    public void sendOrder(int order) {
    }

    public int getOrder() throws IOException {
        return dataIn.readInt();
    }

    public String getQuestion() throws IOException {
        System.out.println("The question is got by csc");
        return dataIn.readUTF();
    }
}
