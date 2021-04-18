package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResultScreen extends JFrame {
    //    private JTextField nameField = new JTextField();
    private JLabel labelName;
    private JLabel labelGame = new JLabel("SpyGame");

    public ResultScreen(String spyId, String res) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(2, 1));
        contentPane.add(labelGame);
        labelName = new JLabel(res +"! Это был " + spyId + "!");
        contentPane.add(labelName);
    }

//    public static void main(String args[]) {
//        JFrame frame = new LoginScreen();
//        frame.setVisible(true);
//    }
}
