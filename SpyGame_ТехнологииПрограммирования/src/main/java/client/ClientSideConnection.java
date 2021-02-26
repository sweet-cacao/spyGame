package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSideConnection {
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    public int playerId;
    public String imageName;

    public ClientSideConnection() {
        System.out.println("---Client---");
        try {
            Socket socket = new Socket("localhost", 51734);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            playerId = dataIn.readInt();
            imageName = dataIn.readUTF();
            int maxTurns = dataIn.readInt();
            int size = dataIn.readInt();
            for (int i = 0; i < size; i++) {
                List<Integer> ids = new ArrayList<>();
                ids.add(dataIn.readInt());
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

    public String getQuestion() throws IOException {
        System.out.println("The question is got by csc");
        return dataIn.readUTF();
    }
}
