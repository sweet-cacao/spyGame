package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VoteScreen extends JFrame {
    private SimpleDateFormat df = new SimpleDateFormat("mm:ss:S");
    private Double timeLeft = 20000.0;
    private JLabel join = new JLabel("Осталось времени: " + df.format(timeLeft));
    private JButton exit = new JButton("Выход");
    //    private JTextField nameField = new JTextField();
    private JLabel labelName = new JLabel("Я думаю шпион ...");
    private JLabel labelGame = new JLabel("Кто по вашему мнению шпион?");
    private List<String> ids;
    public List<JButton> playerButtons = new ArrayList<>();
    private Timer timer;
    public String playerIsSpy = "";

    public VoteScreen(List ids) {
        this.ids = ids;
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
//        midPanel.setLayout(new GridLayout(1, 1));
//        midPanel.add(labelName);
//        midPanel.add(nameField);
        contentPane.add(midPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.add(labelName);
        bottomPanel.add(join);
        contentPane.add(bottomPanel);
        setListenerForButtons();
        createTimer();
//        setListenerForVoteButton();
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

//    public void setListenerForVoteButton() {
//        ActionListener al = e -> {
//            JButton b = (JButton) e.getSource();
//        };
//        join.addActionListener(al);
//    }

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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException exc) {
                    System.out.println("Interrupted exception was thrown");
                }

            }
        };
        timer = new Timer(100, countDown);
        timer.start();
    }

    public static void main(String args[]) {
        List<String> ids = new ArrayList<>();
        ids.add("hi");
        ids.add("may");
        ids.add("bay");
        JFrame frame = new VoteScreen(ids);
        frame.setVisible(true);
    }
}