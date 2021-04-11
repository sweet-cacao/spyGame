package model;

import client.ClientSideConnection;
import gui.LoginScreen;
import gui.MainScreen;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class Player {
    private ClientSideConnection csc;
    private int turnsMade;
    private  MainScreen frame;
    private final String questions = "";
    private String imageName;
    private int order = 0;
    public List<Integer> ids;
    private String playerToAnswerChoosen = "";
    private String playerToAnswer;
    private String nickname;

    public Player(int w, int h) {
        turnsMade = 0;
    }

    public void connectToServer() {
        csc = new ClientSideConnection(nickname);
        imageName = csc.imageName;
        ids = csc.ids;
        playerToAnswer = ids.get(0).toString();
    }

    public void createMainScreen(String imageName) {
       frame = new MainScreen(imageName, csc.id, csc.spyId, ids, csc.names);
       frame.setVisible(true);
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    updateQuestionPanel();
                    updateOrder();
                    updatePlayerLabel();
                    updatePlayerButtons();
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
            if (playerToAnswerChoosen.length() == 0) {
                JOptionPane.showMessageDialog(frame,
                        "Выберите игрока, которому хотите задать вопрос",
                        "Warning",
                        JOptionPane.PLAIN_MESSAGE);
                return;
            }
            String answer = "#" + csc.playerId + " Ответ: " + frame.answerTextField.getText() + "\n\n";
            String question = answer + "#" + csc.playerId + " : " + frame.questionTextField.getText() + "\n";
            turnsMade++;
            System.out.println("turnsMade = " + turnsMade);
            try {
                csc.sendQuestion(question);
//                csc.sendOrder(order);
                csc.sendPlayerToAnswer(playerToAnswerChoosen);
                playerToAnswerChoosen = "";
//                updateOrder();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        frame.sendQuestionBtn.addActionListener(al);
        frame.rulesBtn.addActionListener(al);
    }

    public void setListenerForPlayerButtons() {
        ActionListener al = e -> {
            JButton b = (JButton) e.getSource();
            String textButton = b.getText();
            playerToAnswerChoosen = textButton;
        };
        for (int i = 0; i < frame.playerButtons.size(); i++) {
            frame.playerButtons.get(i).addActionListener(al);
        }
    }

    private void updateQuestionPanel() throws IOException {
        System.out.println("Try to update panel in Player");
        frame.textArea.setText(frame.textArea.getText() + csc.getQuestion());
        playerToAnswer = csc.getQuestion();
    }

    private void updatePlayerLabel() {
        if (String.valueOf(csc.playerId).equals(playerToAnswer)) {
            frame.nameOfGameLabel.setText("Your turn");
        } else {
            frame.nameOfGameLabel.setText("Wait for your turn");
        }
    }

    private void updatePlayerButtons() {
//        if (csc.ids.get(order) == csc.playerId) {
//            frame.sendQuestionBtn.setEnabled(true);
//        } else {
//            frame.sendQuestionBtn.setEnabled(false);
//        }
        if (String.valueOf(csc.playerId).equals(playerToAnswer)) {
            frame.sendQuestionBtn.setEnabled(true);
        } else {
            frame.sendQuestionBtn.setEnabled(false);
        }
    }

    private void updateOrder() throws IOException {
//        if (csc.getQuestion().equals("order"))
//            order = csc.getOrder();
//        System.out.println("The order was got by csc and updated: " + order);
    }

    public void createLoginScreen(Player p) {
        LoginScreen loginScreen = new LoginScreen();
        ActionListener al = e -> {
            nickname = loginScreen.getNameField().getText();
            if (nickname.length() == 0) {
                JOptionPane.showMessageDialog(frame,
                        "Введите имя",
                        "Warning",
                        JOptionPane.PLAIN_MESSAGE);
                return;
            } else {
                loginScreen.setVisible(false);
                p.connectToServer();
//                writeNamesToFile(nickname, csc.id);
                p.createMainScreen(p.imageName);
                p.setListenerForButtons();
                p.setListenerForPlayerButtons();

            }
        };
        loginScreen.getJoin().addActionListener(al);
        loginScreen.setVisible(true);
    }

//    private void writeNamesToFile(String nickname, int id) {
//
//    }

    public static void main(String[] args) {
        Player p = new Player(500, 100);
        p.createLoginScreen(p);
    }
}
