package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerSideConnection implements Runnable{
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private final int playerId;
    private final int maxTurns;
    private final List<Integer> ids;
    public Map<Integer, ServerSideConnection> serverSideConnections;
    private final String imageName;

    public ServerSideConnection(Socket socket, int playerId, int maxTurns, List<Integer> ids, int spyId, Map<Integer, ServerSideConnection> serverSideConnections, String imageName) {
        this.playerId = playerId;
        this.maxTurns = maxTurns;
        this.ids = ids;
        this.serverSideConnections = serverSideConnections;
        this.imageName = imageName;
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException from ServerSideConnection constructor");
        }
    }

    public void run() {
        try {
            dataOut.writeInt(playerId);
            dataOut.writeUTF(imageName);
            dataOut.writeInt(maxTurns);
            dataOut.writeInt(ids.size());
            for (Integer i : ids) {
                dataOut.writeInt(i);
            }
            dataOut.writeInt(0);
            dataOut.flush();
            while (true) {
                String questionsString = dataIn.readUTF();
                sendQuestions(questionsString);
            }
        } catch (IOException e) {
            System.out.println("IOException from ServerSideConnection run()");
        }
    }

    private void sendString(String string) throws IOException {
        dataOut.writeUTF(string);
        dataOut.flush();
    }

    private void sendQuestions(String questionsString) throws IOException {
        for (ServerSideConnection s2 : serverSideConnections.values()) {
                System.out.println("QuestionString: " + questionsString);
                s2.sendString(questionsString);
          }
    }
}
