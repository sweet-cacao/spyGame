package gui;

import javax.swing.*;
import java.awt.*;

public class MyDrawPanel extends JPanel {
    private final String imageName;

    public MyDrawPanel(String imageName, int id, int spyId) {
        if (id == spyId) {
            this.imageName = "src/main/resources/spy.png";
        } else {
            this.imageName = imageName;
        }
    }

    public void paintComponent(Graphics g) {
        Image image = new ImageIcon(imageName).getImage();
        g.drawImage(image, 0, 0, 600, 400,this);
    }
}
