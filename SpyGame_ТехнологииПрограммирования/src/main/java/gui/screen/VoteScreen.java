package gui.screen;

import model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static model.Player.NO_VOTE;

public class VoteScreen extends JFrame {
    private SimpleDateFormat df = new SimpleDateFormat("mm:ss:S");
    private Double timeLeft = 20000.0;
    private JLabel join = new JLabel("Осталось времени: " + df.format(timeLeft));
    private JButton exit = new JButton("Выход");
    private JLabel labelName = new JLabel("Я думаю шпион ...");
    private JLabel labelGame = new JLabel("Кто по вашему мнению шпион?");
    private List<String> ids;
    public List<JButton> playerButtons = new ArrayList<>();
    private Timer timer;
    public String playerIsSpy = "";
    private Player player;

    public VoteScreen(Player player) {
        this.ids = player.ids;
        this.player = player;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(3, 1));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 1));
        topPanel.add(labelGame);
        contentPane.add(topPanel);

        JPanel midPanel = createButtonsPanelForChoosingPlayers();
        contentPane.add(midPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.add(labelName);
        bottomPanel.add(join);
        contentPane.add(bottomPanel);
        setListenerForButtons();
        createTimer();
    }

    private JPanel createButtonsPanelForChoosingPlayers() {
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new GridLayout(ids.size(), 1));
        ids.forEach(k -> {
                JButton button = new JButton(k);
                playerButtons.add(button);
                playersPanel.add(button);
        });
        return playersPanel;
    }

    public void setListenerForButtons() {
        ActionListener al = e -> {
            JButton b = (JButton) e.getSource();
            playerIsSpy = b.getText();
            labelName.setText("Я думаю шпион " + playerIsSpy);
        };
        playerButtons.forEach(b -> {
            b.addActionListener(al);
        });
    }

    private void createTimer() {
        ActionListener countDown = e -> {
            timeLeft -= 100;
            join.setText("Осталось времени: " + df.format(timeLeft));
            if (timeLeft <= 0) {
                timer.stop();
                join.setText("Время истекло");
                playerButtons.forEach(b -> {
                    b.setEnabled(false);
                });
                formResult();
            }
        };
        timer = new Timer(100, countDown);
        timer.start();
    }

    private void formResult() {
        if (playerIsSpy.length() == 0) {
            try {
                player.getCsc().sendString("vote:" + NO_VOTE);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            try {
                player.getCsc().sendString("vote:" + playerIsSpy);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exc) {
            System.out.println("Interrupted exception was thrown");
        }
        while (true) {
            String res = player.getVoteCounter().countResult(ids);
            if (!res.equals("no")) {
                try {
                    player.getCsc().sendString("Meeting:" + res);
                } catch (Exception e) {
                    System.out.println("exc was thrown in form result");
                }

                player.finalizeGame(res);
                break;
            }
        }

    }
}