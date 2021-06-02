package utils;

import lombok.Getter;

@Getter
public class PlayerQueue {
    private String playerToAnswerChoosen = "";
    private String playerToAnswer = "";
    private static int turn = 0;

    public void setPlayerToAnswerChoosen(String playerToAnswerChoosen) {
        this.playerToAnswerChoosen = playerToAnswerChoosen;
    }

    public void setPlayerToAnswer(String playerToAnswer) {
        this.playerToAnswer = playerToAnswer;
    }
}
