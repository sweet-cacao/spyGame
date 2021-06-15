package gui.listener.main.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RulesButtonListener implements ActionListener {

    Frame frame;

    public RulesButtonListener(Frame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(frame,
                "В комнате собираются игроки. Как только набирается 10 человек, запускается игра. Каждому игроку, кроме одного, раздаются карточки с одной и той же локацией. Одному игроку достается карточка шпион.\n" +
                        "Далее игроки по очереди задают друг другу вопросы о локации, пытаясь догадаться, кто шпион. Цель игры сотоит в том, чтобы найти шпиона и не дать ему раньше понять, какая именно локация.\n" +
                        "Если один из игроков догадался, он может поднять руку, начать обсуждение в чате и устроить голосование. Если игроки догадались, кто шпион, он пробует угадать локацию.\n" +
                        "Шпион также может остановить игру и попробовать угадать локацию раньше.",
                "Правила",
                JOptionPane.PLAIN_MESSAGE);
    }
}
