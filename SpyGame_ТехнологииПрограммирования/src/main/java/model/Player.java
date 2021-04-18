package model;

import client.ClientSideConnection;
import gui.LoginScreen;
import gui.MainScreen;
import gui.ResultScreen;
import gui.VoteScreen;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    public static final String NO_VOTE = "noVote";
    private ClientSideConnection csc;
    private int turnsMade;
    private MainScreen frame;
    private final String questions = "";
    private String imageName;
    private int order = 0;
    public List<String> ids;
    private String playerToAnswerChoosen = "";
    private String playerToAnswer;
    private String nickname;
    // TODO нужно сделать отсчет времени игры и ограничить ее, плюс поставить ограничение на кнопку собрание

    public VoteScreen voteScreen = null;
    private Double timeLeft = 23000.0;
    private javax.swing.Timer timer;

    int noVote = 0;
    int spyVotes = 0;
    int otherVotes = 0;

    public Player(int w, int h) {
        turnsMade = 0;
    }

    public void connectToServer() {
        csc = new ClientSideConnection(nickname);
        imageName = csc.imageName;
        ids = csc.ids;
        playerToAnswer = ids.get(0);
    }

    public void createMainScreen(String imageName) {
       frame = new MainScreen(imageName, csc.id, csc.spyId, ids, csc.names);
       frame.setVisible(true);
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    updateQuestionPanel();
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
                csc.sendQuestion(question + "#" + playerToAnswerChoosen);
//                csc.sendOrder(order);
//                csc.sendPlayerToAnswer(playerToAnswerChoosen);
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
            playerToAnswerChoosen = b.getText();
        };
        for (int i = 0; i < frame.playerButtons.size(); i++) {
            frame.playerButtons.get(i).addActionListener(al);
        }
    }

    public void setListenerForMeetingButton() {
        ActionListener al = e -> {
            try {
                csc.sendQuestion("Meeting");
            } catch (IOException exc) {
                System.out.println("IOException was thrown");
            }
            voteScreen = new VoteScreen(ids);
            voteScreen.setVisible(true);
            createTimer();
//            frame.turnOffAllButtons();
            // TODO здесь по таймеру через 40 секунд собираются результаты и формируется команда победителя
            // TODO Можно еще не выключать кнопки и оставить чат
        };
        frame.meetupBtn.addActionListener(al);
    }

    private void createTimer() {
        ActionListener countDown = e -> {
            timeLeft -= 100;
            if (timeLeft <= 0) {
                timer.stop();
                if (voteScreen.playerIsSpy.length() == 0) {
                    try {
                        csc.sendQuestion("vote:" + NO_VOTE);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    try {
                        csc.sendQuestion("vote:" + voteScreen.playerIsSpy);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                voteScreen.setVisible(false);
                while (true) {
                    String res = countResult();
                    if (!res.equals("no")) {
                        createResultScreen(res);
                        break;
                    }
                }

            }
        };
        timer = new javax.swing.Timer(100, countDown);
        timer.start();
    }

    private void createResultScreen(String res) {
        ResultScreen resultScreen = new ResultScreen(csc.spyId, res);
        resultScreen.setVisible(true);
        voteScreen.setVisible(false);
        frame.setVisible(false);
    }

    private String countResult() {
        int allVotes = noVote + spyVotes + otherVotes;
        if (allVotes == ids.size()) {
            if (noVote == ids.size() || otherVotes >= spyVotes) {
                return "Победил шпион";
            } else {
                return "Победили горожане";
            }
        } else {
            return "no";
        }
    }

    private void updateQuestionPanel() throws IOException {
        System.out.println("Try to update panel in Player");
        try {
            String q = csc.getQuestion();
            if (q != null) {
                String[] l = q.split(":");
                if (l.length == 2 && l[0].equals("vote")) {
                    if (l[1].equals(NO_VOTE)) {
                        noVote++;
                        System.out.println(noVote);
                    } else if (l[1].equals(csc.spyId)) {
                        spyVotes++;
                        System.out.println(spyVotes);
                    } else {
                        otherVotes++;
                        System.out.println(otherVotes);
                    }
                    return;
                }
                else if (q.equals("Meeting") && voteScreen == null) {
                    frame.meetupBtn.doClick();
                    return;
                }
                List<String> list = Arrays.stream(q.split("#")).collect(Collectors.toList());
                playerToAnswer = list.get(list.size() - 1);
                int len = playerToAnswer.length() + 1;
                int all = q.length();
                int last = all - len;
                String real_q = q.substring(0, last);
                frame.textArea.setText(frame.textArea.getText() + real_q);
            }
        }
        catch (Exception exc) {
            System.out.println("exc as thrown");
        }
    }

    private void updatePlayerLabel() {
        if (csc.playerId.equals(playerToAnswer)) {
            frame.nameOfGameLabel.setText("Your turn");
        } else {
            frame.nameOfGameLabel.setText("Wait for your turn");
        }
    }

    private void updatePlayerButtons() {
        frame.sendQuestionBtn.setEnabled(csc.playerId.equals(playerToAnswer));
    }

    public static void launch(LoginScreen screen) {
        Player p = new Player(500, 100);
        p.connectToServer();

//                writeNamesToFile(nickname, csc.id);
        p.createMainScreen(p.imageName);
        screen.setVisible(false);
        p.setListenerForButtons();
        p.setListenerForPlayerButtons();
        p.setListenerForMeetingButton();
    }
}
