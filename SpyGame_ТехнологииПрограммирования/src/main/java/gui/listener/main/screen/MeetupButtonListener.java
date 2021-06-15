package gui.listener.main.screen;

import gui.screen.AllPicturesScreen;
import gui.screen.VoteScreen;
import model.Player;
import model.utils.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MeetupButtonListener implements ActionListener {

    private Player player;

    public MeetupButtonListener(Player player) {
        this.player = player;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (btn.getText().equals("Собрание")) {
            try {
                player.getCsc().sendString("Meeting:" + Constants.CREATE_VOTE_SCREEN);

            } catch (IOException exc) {
                System.out.println("IOException was thrown");
            }
            VoteScreen voteScreen = new VoteScreen(player);
            voteScreen.setVisible(true);
            player.setVoteScreen(voteScreen);
            player.getMainScreen().setVisible(false);
            // TODO здесь по таймеру через 40 секунд собираются результаты и формируется команда победителя
            // TODO Можно еще не выключать кнопки и оставить чат
        } else {
            try {
                player.getCsc().sendString("FindPicture:spy");
            } catch (IOException exc) {
                System.out.println("IOException was thrown");
            }
            AllPicturesScreen allPicturesScreen = new AllPicturesScreen();
            allPicturesScreen.setActionListenerForAllPicturesScreenEndBtn(player);
            allPicturesScreen.setVisible(true);
            player.setAllPicturesScreen(allPicturesScreen);
            player.getMainScreen().setVisible(false);
            // TODO здесь еще надо прикрутить таймер ограничивающий время шпиона на ответ
        }
    }
}
