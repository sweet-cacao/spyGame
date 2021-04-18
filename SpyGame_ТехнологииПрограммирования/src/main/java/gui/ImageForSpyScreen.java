package gui;

import javax.swing.*;
import java.awt.*;

public class ImageForSpyScreen extends JPanel {
    private final String imageName;


    public ImageForSpyScreen(String imageName) {
            this.imageName = imageName;
    }

    @Override
    public void paintComponent(Graphics g) {
        Image image = new ImageIcon(imageName).getImage();
        g.drawImage(image, 0, 0, 400, 200,this);
    }
}
