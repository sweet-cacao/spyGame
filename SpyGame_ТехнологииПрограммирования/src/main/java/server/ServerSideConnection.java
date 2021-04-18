package server;

import gui.LoginScreen;

import javax.swing.*;
import java.awt.event.ActionListener;
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
    private int maxTurns;
    private List<String> ids;
    public Map<String, ServerSideConnection> serverSideConnections;
    private String imageName;
    private String spyId;
    private int  order = 0;
    private String nickname;
    public boolean isServerReady = false;
    public String reaadyString = "";

    public ServerSideConnection(Socket socket, String playerId, int maxTurns, List<String> ids, String spyId, Map<String, ServerSideConnection> serverSideConnections, String imageName) {
        this.playerId = playerId;
        this.maxTurns = maxTurns;
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
            dataOut.writeInt(maxTurns);
            dataOut.writeInt(ids.size());
            for (String i : ids) {
                dataOut.writeUTF(i);
            }
//            dataOut.writeInt(0);
            dataOut.flush();
//            nickname = dataIn.readUTF();
//            sendNicknames(nickname);
//            reaadyString = dataIn.readUTF();
//            while (true) {
//                if (isServerReady){
//                    sendString("allClientsConnected");
//                    break;
//                }
//            }
//            if (dataIn.readUTF().equals("ready")) {
//                sendQuestions("ready");
//            }
//            int readies = 0;
//            while (true) {
//                if (dataIn.readUTF().equals("ready")) {
//                    readies++;
//                }
//                if (readies == ids.size()) {
//                    break;
//                }
//                readies++;
//            }

            while (true) {
                String questionsString = dataIn.readUTF();
//                String playerToAnswerString = dataIn.readUTF();
                if (questionsString != null &&  !questionsString.equals("order")) {
//                    if (serverSideConnections.size() < order) {
//                        order++;
//                        System.out.println("The order was upgraded in ssc to" + order);
//                    } else {
//                        order = 0;
//                    }
                    sendQuestions(questionsString);
//                    sendQuestions(playerToAnswerString);
                }
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

    private void sendNicknames(String questionsString) throws IOException {
        for (ServerSideConnection s2 : serverSideConnections.values()) {
            System.out.println("nickname: " + playerId + " " + questionsString);
//            s2.dataOut.writeInt(playerId);
            s2.sendString(playerId + " " + questionsString);
        }
    }

}
