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
    private String playerId;
    private List<String> ids;
    public Map<String, ServerSideConnection> serverSideConnections;
    private String imageName;
    private String spyId;


    public ServerSideConnection(Socket socket, String playerId, List<String> ids, String spyId, Map<String, ServerSideConnection> serverSideConnections, String imageName) {
        this.playerId = playerId;
        this.spyId = spyId;
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
            dataOut.writeUTF(playerId);
            dataOut.writeUTF(spyId);
            dataOut.writeUTF(imageName);
        //    dataOut.writeInt(maxTurns);
            dataOut.writeInt(ids.size());
            for (String i : ids) {
                dataOut.writeUTF(i);
            }
            dataOut.flush();

            while (true) {
                String questionsString = dataIn.readUTF();
          //      if (questionsString != null &&  !questionsString.equals("order")) {
                    sendQuestions(questionsString);
              //  }
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
            System.out.println("QuestionString was sent be ssc: " + questionsString);
            s2.sendString(questionsString);
        }
    }

}
