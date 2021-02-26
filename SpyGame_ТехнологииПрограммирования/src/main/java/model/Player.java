package model;

import client.ClientSideConnection;
import gui.MainScreen;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Player {
    private ClientSideConnection csc;
    private int turnsMade;
    private  MainScreen frame;
    private final String questions = "";
    private String imageName;

    public Player(int w, int h) {
        turnsMade = 0;
    }

    public void connectToServer() {
        csc = new ClientSideConnection();
        imageName = csc.imageName;
    }

    public void createMainScreen(String imageName) {
       frame = new MainScreen(imageName);
       frame.setVisible(true);
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    updateQuestionPanel();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        t.start();
    }

    public void setListenerForButtons() {
        ActionListener al = e -> {
            JButton b = (JButton) e.getSource();
            String textButton = b.getText();
            if (textButton.equals("Правила")) {
                JOptionPane.showMessageDialog(frame,
                        "В комнате собираются игроки. Как только набирается 10 человек, запускается игра. Каждому игроку, кроме одного, раздаются карточки с одной и той же локацией. Одному игроку достается карточка шпион.\n" +
                                "Далее игроки по очереди задают друг другу вопросы о локации, пытаясь догадаться, кто шпион. Цель игры сотоит в том, чтобы найти шпиона и не дать ему раньше понять, какая именно локация.\n" +
                                "Если один из игроков догадался, он может поднять руку, начать обсуждение в чате и устроить голосование. Если игроки догадались, кто шпион, он пробует угадать локацию.\n" +
                                "Шпион также может остановить игру и попробовать угадать локацию раньше.",
                        "Правила",
                        JOptionPane.PLAIN_MESSAGE);
                return;
            }
            String question = "#" + csc.playerId + " : " + frame.textField.getText() + "\n";
            turnsMade++;
            System.out.println("turnsMade = " + turnsMade);
            try {
                csc.sendQuestion(question);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        frame.button_2.addActionListener(al);
        frame.button_3.addActionListener(al);
    }

    private void updateQuestionPanel() throws IOException {
        System.out.println("Try to update panel in Player");
        frame.textArea.setText(frame.textArea.getText() + csc.getQuestion());
    }

    public static void main(String[] args) {
        Player p = new Player(500, 100);
        p.connectToServer();
        p.createMainScreen(p.imageName);
        p.setListenerForButtons();
    }
}
