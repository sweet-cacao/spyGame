package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public boolean areAllClientsReady = false;

    public ClientSideConnection(String nickname) {
        System.out.println("---Client---");
        try {
            Socket socket = new Socket("localhost", 51734);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            playerId = dataIn.readUTF();
            id = playerId;
            spyId = dataIn.readUTF();
            imageName = dataIn.readUTF();
            int maxTurns = dataIn.readInt();
            int size = dataIn.readInt();
            ids = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ids.add(dataIn.readUTF());
            }

//            dataOut.writeUTF(nickname);
//            dataOut.flush();
//            int j = 0;
//            int i = 0;
//            while (i < size) {
////                int mapId = dataIn.readInt();
//                String mapFull = dataIn.readUTF();
//                if (mapFull != null) {
//                    String mapName = mapFull.split(" ")[1];
//                    int mapId = Integer.parseInt(mapFull.split(" ")[0]);
//                    System.out.println("The name was added" + mapId + " " + mapName);
//                    names.put(mapId, mapName);
//                    i++;
//                }
//            }
//            dataOut.writeUTF("ready");
//            dataOut.flush();
//            if (dataIn.readUTF().equals("allClientsConnected")) {
//                areAllClientsReady = true;
//            }
//            dataOut.flush();
//            while (j < size) {
//                dataIn.readUTF();
//                j++;
//            }

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
