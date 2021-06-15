package model.message;

import client.ClientSideConnection;
import gui.screen.DefaultOneLabelScreen;
import model.Player;
import model.PlayerQueue;
import model.utils.Constants;
import utils.VoteCounter;

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
                player.getVoteCounter().addVote(message, player.getCsc().spyId);
                break;
            case FIND_PICTURE:
                findPicture();
                break;
            case MEETING:
                if (message.getValue().equals(Constants.CREATE_VOTE_SCREEN)) {
                    if (csc.id.equals(csc.spyId)) {
                        if (player.getWaitScreen() == null) {
                            DefaultOneLabelScreen waitScreen = new DefaultOneLabelScreen(Constants.WAIT_SCREEN_OTHER_PLAYERS);
                            waitScreen.setVisible(true);
                            player.getMainScreen().setVisible(false);
                            player.setWaitScreen(waitScreen);
                        }
                    } else {
                        player.getMainScreen().meetupBtn.doClick();
                    }
                }
                if (csc.id.equals(csc.spyId) && message.getValue().equals(Constants.WIN_MESSAGE) || message.getValue().equals(Constants.LOOSE_MESSAGE)) {
                    player.finalizeGame(message.getValue());
                }
                break;
            case PLAYER_MESSAGE:
                PlayerMessage m = new PlayerMessage(q);
                player.getQueue().setPlayerToAnswer(m.getNextPlayerToAnswer());
                player.getMainScreen().updateChat(m.getMessage());
                break;
            default:
                break;
        }
    }

    private void findPicture() {
        if (!csc.id.equals(csc.spyId)) {
            DefaultOneLabelScreen waitScreen = new DefaultOneLabelScreen(Constants.WAIT_SCREEN_SPY);
            waitScreen.setVisible(true);
            player.setWaitScreen(waitScreen);
            player.getMainScreen().setVisible(false);
        }
    }

    private void createResultScreen(InternalMessage message) {
        // TODO надо пробросить еще название нормального изображения в объекте шпиона
        // TODO лучше сделать реализацию проверки до отправки в классе шпиона
        if (message.getValue().equals(csc.imageName)) {
            player.finalizeGame(Constants.LOOSE_MESSAGE);
        } else {
            player.finalizeGame(Constants.WIN_MESSAGE);
        }
        // TODO сформировать экран результата
    }

    private ActionNames getAction(InternalMessage message) {
        if (message.getType().equals("spyAnswer")) {
            return ActionNames.SPY_ANSWER;
        }
        else if (message.getType().equals("vote")) {
            return ActionNames.VOTE;
        }
        else if (message.getType().equals("Meeting")) {
            if (player.getVoteScreen() == null) {
                return ActionNames.MEETING;
            } else {
                return ActionNames.NO_ACTION;
            }
        }
        else if (message.getType().equals("FindPicture")) {
            return ActionNames.FIND_PICTURE;
        }
        else if (!message.getType().isEmpty()){
            return ActionNames.PLAYER_MESSAGE;
        }
        return ActionNames.NO_ACTION;
    }
}
