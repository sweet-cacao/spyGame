package model;

import client.ClientSideConnection;
import gui.screen.*;
import lombok.Getter;
import lombok.Setter;
import model.message.MessageHandler;
import utils.VoteCounter;

import java.io.IOException;
import java.util.List;

@Getter
public class Player {
    public static final String NO_VOTE = "noVote";

    private ClientSideConnection csc;
    private String imageName;
    public List<String> ids;

    // TODO нужно сделать отсчет времени игры и ограничить ее, плюс поставить ограничение на кнопку собрание

    // screens
    private DefaultOneLabelScreen resultScreen;
    @Setter
    public VoteScreen voteScreen = null;
    @Setter
    private AllPicturesScreen allPicturesScreen;
    private MainScreen mainScreen;
    @Setter
    private DefaultOneLabelScreen waitScreen;

    // utils
    private final VoteCounter voteCounter = new VoteCounter();
    private final PlayerQueue queue = new PlayerQueue();
    private MessageHandler handler;

    public Player(int w, int h) {
    }

    public void connectToServer() {
        csc = new ClientSideConnection();
        imageName = csc.imageName;
        ids = csc.ids;
        queue.setPlayerToAnswer(ids.get(0));
        handler = new MessageHandler(this);
    }

    public void createMainScreen(String imageName) {
       mainScreen = new MainScreen(imageName, csc.id, csc.spyId, ids, csc.names);
       mainScreen.setVisible(true);
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Try to update Player");
                    handler.handle(csc.getString());
                    mainScreen.updatePlayerLabel(csc.playerId, queue.getPlayerToAnswer());
                    mainScreen.updatePlayerButtons(csc.playerId, queue.getPlayerToAnswer());
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        t.start();
    }

    public void finalizeGame(String res) {
        if (resultScreen == null) {
            resultScreen = DefaultOneLabelScreen.DefaultOneLableScreen(csc.spyId, res);
            resultScreen.setVisible(true);
        }
        mainScreen.setVisible(false);
        if (voteScreen != null) {
            voteScreen.setVisible(false);
        }
        if (waitScreen != null) {
            waitScreen.setVisible(false);
        }
        if (allPicturesScreen != null) {
            allPicturesScreen.setVisible(false);
        }
    }

    public static void launch(LoginScreen screen) {
        Player p = new Player(500, 100);
        p.connectToServer();
        p.createMainScreen(p.imageName);
        screen.setVisible(false);
        p.mainScreen.setListenerForMeetingButton(p);
    }
}
