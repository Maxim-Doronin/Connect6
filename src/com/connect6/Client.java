package com.connect6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends JPanel {
    private static JFrame mFrame = new JFrame("Connect6");
    private static int mClickX, mClickY;
    private static int mX = -1, mY = -1;

    private static BoardDrawable mBoardDrawable;
    private static Stone.Color mMyPlayerColor;
    private static boolean mIsMyTurn = false;

    private static void createGUI() {
        mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mFrame.setBackground(Color.GRAY);
        mFrame.setSize(Resources.boardWidthSize, Resources.boardHeightSize + Resources.infoPanelHeight);
        mFrame.add(mBoardDrawable = new BoardDrawable(mFrame));
        mFrame.setVisible(true);
        mFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mClickX = e.getX();
                mClickY = e.getY();
            }
        });
        mClickX = -100;
        mClickY = -100;
    }

    private static void safelySleep() {
        try {
            Thread.sleep(Resources.sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createGUI();
        safelySleep();

        try {
            boolean untilConnect;
            GameInterface gameInterface = null;
            do {
                try {
                    Registry registry = LocateRegistry.getRegistry(null);
                    gameInterface = (GameInterface) registry.lookup("Server");
                    untilConnect = false;
                } catch (RemoteException e) {
                    untilConnect = true;
                }
            } while (untilConnect);

            mMyPlayerColor = gameInterface.getColor();
            while (mMyPlayerColor == Stone.Color.FREE) {
                safelySleep();
            }
            mBoardDrawable.drawCell(0,Resources.boardCellCount, mMyPlayerColor);
            int lastTurnId = 1;

            while (true) {
                mIsMyTurn = gameInterface.isMyTurn(mMyPlayerColor);
                if (mIsMyTurn) {
                    mBoardDrawable.setTurn(true);
                    mX = (mClickX - Resources.boardBorder + Resources.stoneRadius) / Resources.cellSize;
                    mY = (mClickY - Resources.boardBorder + Resources.stoneRadius) / Resources.cellSize;
                    if (mX >= 0 && mX < Resources.boardCellCount && mY >= 0 && mY < Resources.boardCellCount) {
                        mBoardDrawable.drawCell(mX, mY, mMyPlayerColor);
                        gameInterface.setStone(mX, mY, mMyPlayerColor);
                    }
                } else {
                    mBoardDrawable.setTurn(false);
                    mClickX = -100;
                    mClickY = -100;
                }
                int[] stone = gameInterface.getStoneById(lastTurnId);
                if (stone[0] >= 0 && stone[0] < Resources.boardCellCount
                        && stone[1] >= 0 && stone[1] < Resources.boardCellCount) {
                    mBoardDrawable.drawCell(stone[0], stone[1], gameInterface.getColorOfStone(stone[0], stone[1]));
                    lastTurnId++;
                } else {
                    Stone.Color wonColor = gameInterface.whoWon();
                    if (wonColor != Stone.Color.FREE) {
                        if (wonColor == mMyPlayerColor)
                            JOptionPane.showMessageDialog(null, "Winning victory instead of lunch!");
                        else
                            JOptionPane.showMessageDialog(null, "Will be lucky next time :(");
                        break;
                    }
                }
                safelySleep();
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
