package gui.listener.main.screen;

import model.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SendQuestionListener implements ActionListener {

    Player player;

    public SendQuestionListener(Player player) {
        this.player = player;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (player.getQueue().getPlayerToAnswerChoosen().length() == 0) {
            JOptionPane.showMessageDialog(player.getMainScreen(),
                    "Выберите игрока, которому хотите задать вопрос",
                    "Warning",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }
        String answer = "#" + player.getCsc().playerId + " Ответ: " + player.getMainScreen().answerTextField.getText() + "\n\n";
        String question = answer + "#" + player.getCsc().playerId + " : " + player.getMainScreen().questionTextField.getText() + "\n";
        try {
            player.getCsc().sendString(question + "#" + player.getQueue().getPlayerToAnswerChoosen());
            player.getQueue().setPlayerToAnswerChoosen("");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
