package model;

import client.ClientSideConnection;
import gui.ResultScreen;
import utils.ActionNames;
import utils.InternalMessage;
import utils.PlayerQueue;
import utils.VoteCounter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static model.Player.NO_VOTE;

public class MessageHandler {
    private Player player;
    private ClientSideConnection csc;
    private VoteCounter voteCounter;
    private PlayerQueue queue;

    public MessageHandler(Player player) {
        this.player = player;
        this.csc = player.getCsc();
        this.voteCounter = player.getVoteCounter();
        this.queue = player.getQueue();
    }

    public void handle(String q) {
        InternalMessage message = new InternalMessage(q);
        switch (getAction(message)) {
            case SPY_ANSWER:
                createResultScreen(message);
                break;
            case VOTE:
                player.addVote(message);
                break;
            case FIND_PICTURE:
                findPicture();
                break;
            case MEETING:
                player.initiateMeetingForAllPlayers();
                break;
            case PLAYER_MESSAGE:
                player.handlePlayerMessage(q);
                break;
            default:
                break;
        }
    }

    private void findPicture() {
    }


    private void createResultScreen(InternalMessage message) {
        // TODO надо пробросить еще название нормального изображения в объекте шпиона
        // TODO лучше сделать реализацию проверки до отправки в классе шпиона
        ResultScreen resultScreen;
        if (message.getValue().equals(csc.imageName)) {
            resultScreen = new ResultScreen(csc.spyId, "победил шпион");
        } else {
            resultScreen = new ResultScreen(csc.spyId, "победили горожане");
        }
        resultScreen.setVisible(true);
        // TODO сформировать экран результата
    }

    private ActionNames getAction(InternalMessage message) {
        if (message.getType().equals("spyAnswer")) {
            return ActionNames.SPY_ANSWER;
        }
        else if (message.getType().equals("vote")) {
            return ActionNames.VOTE;
        }
        else if (message.getType().equals("Meeting") && player.getVoteScreen() == null) {
            return ActionNames.MEETING;
        }
        else if (message.getType().equals("FindPicture")) {
            return ActionNames.FIND_PICTURE;
        }
        else if (!message.getType().isEmpty()){
            return ActionNames.PLAYER_MESSAGE;
        }
        return null;
    }

    private void voteMessage() {

    }

    private void votingScreenMessage() {

    }

    private void chatMessage() {

    }
}
