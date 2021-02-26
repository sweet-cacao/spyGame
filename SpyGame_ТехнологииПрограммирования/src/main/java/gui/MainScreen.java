package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainScreen extends JFrame {
    public JTextField textField;
    public JButton button_2 = new JButton("Отправить вопрос");
    public JTextArea textArea = new JTextArea(27, 101);
    public JButton button_3 = new JButton("Правила");

    public MainScreen(String imageName) {
        JLabel lblNewLabel_1 = new JLabel("SpyGame");
        lblNewLabel_1.setFont(new Font("Courier", Font.PLAIN, 30));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        MyDrawPanel panel = new MyDrawPanel(imageName);
        this.getContentPane().add(panel);

        JButton button = new JButton("Выход");
        button.setBounds(743, 49, 117, 25);
        contentPane.add(button);

        JButton button_1 = new JButton("Собрание");
        button_1.setBounds(437, 482, 148, 48);
        contentPane.add(button_1);

        button_2.setBounds(626, 480, 249, 25);
        contentPane.add(button_2);

        textField = new JTextField();
        textField.setBounds(626, 511, 249, 19);
        contentPane.add(textField);
        textField.setColumns(10);


        panel.setBounds(390, 101, 485, 369);
        contentPane.add(panel);

        textArea.setEditable(false); // set textArea non-editable
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(27, 101, 318, 369);
        contentPane.add(scroll);

        JLabel lblNewLabel = new JLabel("Чат");
        lblNewLabel.setBounds(27, 27, 318, 48);
        contentPane.add(lblNewLabel);

        button_3.setBounds(743, 12, 117, 25);
        contentPane.add(button_3);

        lblNewLabel_1.setBounds(350, 27, 196, 47);
        contentPane.add(lblNewLabel_1);
        button.addActionListener (e -> System.exit(0));
    }
}
