package gui.screen;

import gui.listener.main.screen.MeetupButtonListener;
import gui.MyDrawPanel;
import gui.listener.main.screen.RulesButtonListener;
import gui.listener.main.screen.SendQuestionListener;
import model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainScreen extends JFrame {
    public JTextField questionTextField;
    public JTextField answerTextField;
    public JButton sendQuestionBtn = new JButton("Отправить вопрос");
    public JTextArea textArea = new JTextArea(27, 101);
    public JButton rulesBtn = new JButton("Правила");
    public JLabel nameOfGameLabel = new JLabel("SpyGame");
    public JLabel playerName;
    public List<String> ids;
    public List<JButton> playerButtons = new ArrayList<>();
    public String id;
    public Map<Integer, String> names;
    public JButton meetupBtn ;

    public MainScreen(String imageName, String id, String spyId, List<String> ids, Map<Integer, String> names) {
        this.ids = ids;
        this.id = id;
        this.names = names;
        if (id.equals(spyId)) {
            meetupBtn = new JButton("Угадать картинку");
        } else {
            meetupBtn = new JButton("Собрание");
        }

        nameOfGameLabel.setFont(new Font("Courier", Font.PLAIN, 30));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 700);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        playerName = new JLabel(id);
        playerName.setBounds(127, 27, 80, 48);
        this.getContentPane().add(playerName);

        MyDrawPanel imagePanel = new MyDrawPanel(imageName, id, spyId);
        this.getContentPane().add(imagePanel);

        JButton closeBtn = new JButton("Выход");
        closeBtn.setBounds(743, 49, 117, 25);
        contentPane.add(closeBtn);

        meetupBtn.setBounds(590, 29, 148, 48);
        contentPane.add(meetupBtn);

        sendQuestionBtn.setBounds(626, 480, 249, 25);
        contentPane.add(sendQuestionBtn);

        JLabel answerLabel = new JLabel("Ответ");
        answerLabel.setBounds(626, 511, 249, 19);
        contentPane.add(answerLabel);

        JLabel questionLabel = new JLabel("Вопрос");
        questionLabel.setBounds(626, 560, 249, 19);
        contentPane.add(questionLabel);

        questionTextField = new JTextField();
        questionTextField.setBounds(626, 590, 249, 19);
        contentPane.add(questionTextField);
        questionTextField.setColumns(10);

        answerTextField = new JTextField();
        answerTextField.setBounds(626, 530, 249, 19);
        contentPane.add(answerTextField);
        answerTextField.setColumns(10);


        imagePanel.setBounds(390, 101, 485, 369);
        contentPane.add(imagePanel);

        textArea.setEditable(false); // set textArea non-editable
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(27, 101, 318, 369);
        contentPane.add(scroll);

        JLabel chatLabel = new JLabel("Чат");
        chatLabel.setBounds(27, 27, 318, 48);
        contentPane.add(chatLabel);

        rulesBtn.setBounds(743, 12, 117, 25);
        contentPane.add(rulesBtn);

        nameOfGameLabel.setBounds(350, 27, 196, 47);
        contentPane.add(nameOfGameLabel);
        closeBtn.addActionListener (e -> System.exit(0));

        JPanel buttonsPanel = createButtonsPanelForChoosingPlayers();
        buttonsPanel.setBounds(30, 480, 500, 120);
        contentPane.add(buttonsPanel);
    }

    private JPanel createButtonsPanelForChoosingPlayers() {
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new GridLayout(2,ids.size()));
        ids.forEach(k -> {
            if (!k.equals(id)) {
                JButton button = new JButton(k);
                playerButtons.add(button);
                playersPanel.add(button);
            }
        });
        return playersPanel;
    }

    public void updatePlayerLabel(String playerId, String playerToAnswer) {
        if (playerId.equals(playerToAnswer)) {
            nameOfGameLabel.setText("Your turn");
        } else {
            nameOfGameLabel.setText("Wait for your turn");
        }
    }

    public void updatePlayerButtons(String playerId, String playerToAnswer) {
        sendQuestionBtn.setEnabled(playerId.equals(playerToAnswer));
    }

    public void updateChat(String question) {
        textArea.setText(textArea.getText() + question);
    }

    public void setListenerForMeetingButton(Player player) {
        ActionListener l = new MeetupButtonListener(player);
        meetupBtn.addActionListener(l);
        l = new RulesButtonListener(player.getMainScreen());
        rulesBtn.addActionListener(l);
        l = new SendQuestionListener(player);
        sendQuestionBtn.addActionListener(l);
        ActionListener al = e -> {
            JButton b = (JButton) e.getSource();
            player.getQueue().setPlayerToAnswerChoosen(b.getText());
        };
        for (int i = 0; i < player.getMainScreen().playerButtons.size(); i++) {
            player.getMainScreen().playerButtons.get(i).addActionListener(al);
        }
    }
}
