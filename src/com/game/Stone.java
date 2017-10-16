package com.game;


import javax.swing.*;
import java.awt.*;

public class Stone extends JPanel {
    private int color;
    private int x, y;
    Image img;

    Stone (int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void paint(Graphics g) {
        if(color ==1 ) {
            img= new ImageIcon("src\\images\\white-stone.gif").getImage();
            g.drawImage(img, x, y, this);
        }else {
            img = new ImageIcon("src\\images\\black-stone.gif").getImage();
            g.drawImage(img, x, y, this);
        }
    }
}
