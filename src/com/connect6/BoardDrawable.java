package com.connect6;

import javax.swing.*;
import java.awt.*;

public class BoardDrawable extends JPanel {
    private Image img = new ImageIcon("src/images/board.png").getImage();
    private JFrame mFrame;
    private boolean mIsMyTurn;
    private JLabel mText;
    private JPanel mTextPanel;

    BoardDrawable(JFrame frame) {
        mFrame = frame;
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(img,0,0,null);
        mTextPanel = new JPanel();
        mText = new JLabel("Wait...");
    }

    void setTurn(boolean isMyTurn) {
        if (mIsMyTurn == isMyTurn) {
            return;
        }
        mIsMyTurn = isMyTurn;
        mFrame.remove(mText);
        if (isMyTurn) {
            mFrame.add(mText = new JLabel("Your turn"));
        } else {
            mFrame.add(mText = new JLabel("Wait..."));
        }
    }

    void drawCell(int x, int y, Stone.Color color) {
        int drawableX = x * Resources.boardWidthSize / Resources.boardCellCount + 5;
        int drawableY = y * Resources.boardHeightSize / Resources.boardCellCount + 5;
        mFrame.add(new Stone(drawableX, drawableY, color));
        mFrame.setVisible(true);
    }
}
