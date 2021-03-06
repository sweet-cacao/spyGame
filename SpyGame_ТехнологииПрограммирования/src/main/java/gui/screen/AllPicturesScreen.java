package gui.screen;

import gui.ImageForSpyScreen;
import model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// TODO добавить класс
// TODO гасить все кнопки когда начинается собрание или шпион выбирает
// TODO сделать таймер в целом для всей игры и если за опрееделнное время не успели то шпион отгадывает

public class AllPicturesScreen extends JFrame {
    private final List<String> images = new ArrayList<>();
    private final List<JButton> buttons = new ArrayList<>();
    private JLabel labelGame = new JLabel("SpyGame");
    public JButton join = new JButton("Присоединиться");
    public String pictureName = "";

    public AllPicturesScreen() {
        getImages();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 1));
        topPanel.add(labelGame);
        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel midPanel = createMidPanel();
        contentPane.add(midPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 1));
        bottomPanel.add(join);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        setListenerForButtons();
    }

    private void getImages() {
        try (Stream<Path> paths = Files.walk(Paths.get("./src/main/resources/images"))) {
            paths.filter(Files::isRegularFile).forEach(element -> images.add(element.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel createMidPanel() {
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new GridLayout(2, images.size()));
        images.forEach(k -> {
            JButton b = new JButton();
            buttons.add(b);
            b.setName(k);
            JPanel p = new ImageForSpyScreen(k);
            p.setBorder(new EmptyBorder(5, 5, 5, 5));
            b.add(p);
            midPanel.add(b);
            p.setMinimumSize(new Dimension(400, 200));
            p.setMaximumSize(new Dimension(600, 300));
        });
        return midPanel;
    }

    public void setListenerForButtons() {
        ActionListener al = e -> {
            JButton b = (JButton) e.getSource();
            pictureName = b.getName();
            System.out.println("Spy chosen picture " + pictureName);
            buttons.forEach(btn -> btn.setEnabled(true));
            b.setEnabled(false);
        };
        buttons.forEach(b -> {
            b.addActionListener(al);
        });
    }

    public void setActionListenerForAllPicturesScreenEndBtn(Player player) {
        ActionListener al = e -> {
            if (this.pictureName.length() != 0) {
                try {
                    player.getCsc().sendString("spyAnswer:" + this.pictureName);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                try {
                    player.getCsc().sendString("spyAnswer:not chosen");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        };
        join.addActionListener(al);
    }

}
