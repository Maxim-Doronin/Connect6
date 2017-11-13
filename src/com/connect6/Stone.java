package com.connect6;

import javax.swing.*;
import java.awt.*;

public class Stone extends JPanel {
    public enum Color {
        WHITE,
        BLACK,
        FREE
    }
    private int mDrawableX;
    private int mDrawableY;
    private Image mStoneImg;

    Stone(int drawableX, int drawableY, Color color) {
        mDrawableX = drawableX;
        mDrawableY = drawableY;
        if (color == Color.WHITE) {
            mStoneImg = new ImageIcon("src/images/white-stone.gif").getImage();
        } else if (color == Color.BLACK) {
            mStoneImg = new ImageIcon("src/images/black-stone.gif").getImage();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(mStoneImg, mDrawableX, mDrawableY, this);
    }
}
