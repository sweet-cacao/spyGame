package gui.screen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DefaultOneLabelScreen extends JFrame {

    public DefaultOneLabelScreen(String labelStrings) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(2, 1));
        JLabel labelGame = new JLabel("SpyGame");
        contentPane.add(labelGame);
        JLabel labelName = new JLabel(labelStrings);
        contentPane.add(labelName);
    }

    public static DefaultOneLabelScreen DefaultOneLableScreen(String spyId, String res) {
        return new DefaultOneLabelScreen(res + "! Это был " + spyId + "!");
    }

}
