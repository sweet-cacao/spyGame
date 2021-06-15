package launcher;

import gui.screen.LoginScreen;
import model.Player;

import java.awt.event.ActionListener;


// TODO при нажатии на собрании открывается allPictures Screen
// TODO открывать экран голосования и у шпиона
// TODO нужно блокировать все кнопки когда экран голосования или выбора картинки у шпиона

public class ClientLauncher {
    public static void main(String[] args) {
        LoginScreen loginScreen = new LoginScreen();
        ActionListener al = e -> {
            loginScreen.getJoin().setEnabled(false);
            if (!loginScreen.getJoin().isEnabled()) {
                Player.launch(loginScreen);
            }
        };
        loginScreen.getJoin().addActionListener(al);
        loginScreen.setVisible(true);
    }
}
