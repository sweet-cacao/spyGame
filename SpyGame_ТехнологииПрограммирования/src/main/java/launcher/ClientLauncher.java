package launcher;

import gui.LoginScreen;
import model.Player;

import java.awt.event.ActionListener;

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
