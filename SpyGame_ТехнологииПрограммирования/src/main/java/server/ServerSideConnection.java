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
    private int playerId;
    private int maxTurns;
    private List<Integer> ids;
    public Map<Integer, ServerSideConnection> serverSideConnections;
    private String imageName;
    private int spyId;
    private int  order = 0;
    private String nickname;

    public ServerSideConnection(Socket socket, int playerId, int maxTurns, List<Integer> ids, int spyId, Map<Integer, ServerSideConnection> serverSideConnections, String imageName) {
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
            dataOut.writeInt(playerId);
            dataOut.writeInt(spyId);
            dataOut.writeUTF(imageName);
            dataOut.writeInt(maxTurns);
            dataOut.writeInt(ids.size());
            for (Integer i : ids) {
                dataOut.writeInt(i);
            }
//            dataOut.writeInt(0);
            dataOut.flush();
            nickname = dataIn.readUTF();
            sendNicknames(nickname);
            if (dataIn.readUTF().equals("ready")) {
                sendQuestions("ready");
            }
            int readies = 0;
            while (true) {
                if (dataIn.readUTF().equals("ready")) {
                    readies++;
                }
                if (readies == ids.size()) {
                    break;
                }
            }
            while (true) {
                String questionsString = dataIn.readUTF();
                String playerToAnswerString = dataIn.readUTF();
                if (questionsString != null &&  !questionsString.equals("order")) {
//                    if (serverSideConnections.size() < order) {
//                        order++;
//                        System.out.println("The order was upgraded in ssc to" + order);
//                    } else {
//                        order = 0;
//                    }
                    sendQuestions(questionsString);
                    sendQuestions(playerToAnswerString);
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
            System.out.println("QuestionString: " + questionsString);
            s2.sendString(questionsString);
        }
    }

    private void sendNicknames(String questionsString) throws IOException {
        for (ServerSideConnection s2 : serverSideConnections.values()) {
            System.out.println("QuestionString: " + questionsString);
            s2.dataOut.writeInt(playerId);
            s2.sendString(questionsString);
        }
    }

}
