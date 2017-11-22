package by.apatully.blockCrasher;

import javax.swing.*;
import java.awt.*;

public class Launcher {

    public void launch() {
        JFrame frame = new JFrame("Block Crasher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);

        Crasher game = new Crasher(1280, 720);
        frame.add(game, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.launch();
    }
}
