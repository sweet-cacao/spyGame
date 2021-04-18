package gui;

import lombok.Data;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/*
    TODO провалидировать имя, что нет повторяющихся в данной комнате
 */

@Data
public class LoginScreen extends JFrame {
    private JButton join = new JButton("Присоединиться");
    private JButton exit = new JButton("Выход");
//    private JTextField nameField = new JTextField();
    private JLabel labelName = new JLabel("Присоединяйтесь, как только остальные игроки будут готовы, начнется игра!");
    private JLabel labelGame = new JLabel("SpyGame");

    public LoginScreen() {
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

        JPanel midPanel = new JPanel();
        midPanel.setLayout(new GridLayout(2, 1));
        midPanel.add(labelName);
//        midPanel.add(nameField);
        contentPane.add(midPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 1));
        bottomPanel.add(join);
        contentPane.add(bottomPanel);
    }

//    public static void main(String args[]) {
//        JFrame frame = new LoginScreen();
//        frame.setVisible(true);
//    }
}
