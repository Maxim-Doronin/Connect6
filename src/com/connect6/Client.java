package com.connect6;

import com.connect6.ClientCommunication.RequestThread;
import com.connect6.ClientCommunication.ResponseThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JPanel {
    private static JFrame mFrame = new JFrame("Connect6");
    private static int mClickX, mClickY;
    private static int mX = -1, mY = -1;

    private static BoardDrawable mBoardDrawable;
    private Stone.Color mMyPlayerColor = Stone.Color.FREE;
    private boolean mIsMyTurn = false;
    private ArrayList<ReceivedStone> mStones = new ArrayList<>();

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

    public void setColor(int color) {
        mMyPlayerColor = getColorById(color);
    }

    public void setIsMyTurn(boolean isMyTurn){
        mIsMyTurn = isMyTurn;
    }

    public void setStones(ReceivedStone stone) {
        mStones.add(stone);
    }

    private int getColorId(Stone.Color color) {
        if (color == Stone.Color.WHITE) {
            return 1;
        }
        if (color == Stone.Color.BLACK) {
            return 2;
        }
        return 3;
    }

    private Stone.Color getColorById(int id) {
        if (id ==1) {
            return Stone.Color.WHITE;
        }
        if (id == 2) {
            return Stone.Color.BLACK;
        }
        return Stone.Color.FREE;
    }


    public Client() {
        createGUI();
        safelySleep();

        try {
            Socket clientSocket = null;

            final String SERVER_NAME = "localhost";
            final int PORT_NUMBER = 59342;
            try {
                try {
                    clientSocket = new Socket(SERVER_NAME, PORT_NUMBER);
                } catch (ConnectException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestThread requestThread = new RequestThread(clientSocket);
            ResponseThread responseThread = new ResponseThread(clientSocket, this);
            requestThread.start();
            responseThread.start();


            int lastTurnId = 1;
            while (clientSocket != null && !clientSocket.isClosed()) {
                while (mMyPlayerColor == Stone.Color.FREE) {
                    safelySleep();
                }
                if (lastTurnId == 1) {
                    mBoardDrawable.drawCell(0,Resources.boardCellCount, mMyPlayerColor);
                    lastTurnId++;
                }
                if (mIsMyTurn) {
                    mBoardDrawable.setTurn(true);
                    mX = (mClickX - Resources.boardBorder + Resources.stoneRadius) / Resources.cellSize;
                    mY = (mClickY - Resources.boardBorder + Resources.stoneRadius) / Resources.cellSize;
                    if (mX >= 0 && mX < Resources.boardCellCount && mY >= 0 && mY < Resources.boardCellCount) {
                        mBoardDrawable.drawCell(mX, mY, mMyPlayerColor);
                        Request request = new Request();
                        request.setParameter("type", "stone");
                        request.setParameter("stone", new ReceivedStone(mX, mY, getColorId(mMyPlayerColor)));
                        requestThread.sendRequest(request);
                    }
                } else {
                    mBoardDrawable.setTurn(false);
                    mClickX = -100;
                    mClickY = -100;
                }


                if (!mStones.isEmpty()) {
                    ReceivedStone stone = mStones.remove(0);
                    if (stone != null && stone.x >= 0 && stone.x < Resources.boardCellCount
                            && stone.y >= 0 && stone.y < Resources.boardCellCount){
                        mBoardDrawable.drawCell(stone.x, stone.y, getColorById(stone.color));

                    }
                }

                safelySleep();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
