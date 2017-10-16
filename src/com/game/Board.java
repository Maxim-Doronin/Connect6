package com.game;


import javax.swing.*;
import java.awt.*;

public class Board extends JPanel{
    Image img = new ImageIcon("src\\images\\board.png").getImage();

    public void paint(Graphics g){
        g = (Graphics2D)g;
        g.drawImage(img,0,0,null);
    }
}
