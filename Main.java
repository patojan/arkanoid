package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{
        int jumpTo;

        if (args.length ==0) jumpTo = 0; //if no argument, it will start on 1st level
        else jumpTo = Integer.parseInt(args[0]);

        JFrame obj = new JFrame();
        Arkanoid.Gameplay gamePlay = new Arkanoid.Gameplay(jumpTo);
        obj.setBounds(100,100,1024,768);
        obj.setTitle("Arkanoid");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }
}
