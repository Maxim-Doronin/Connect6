package com.connect6;


import com.connect6.ServerCommunication.RequestThread;
import com.connect6.ServerCommunication.ResponseThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket = null;
    private Board mBoard;
    private int mPlayersCount;
    private int mLastTurnId = 0;

    private ArrayList<ResponseThread> responseThreads = new ArrayList<>();
    private ArrayList<RequestThread> requestThreads = new ArrayList<>();

    public void setStone(int x, int y, int color) {
        Stone.Color stoneColor = getColorById(color);
        if (mBoard.isCellFree(x,y)) {
            mLastTurnId++;
        }
        mBoard.setCell(x, y, stoneColor, mLastTurnId);
    }

    public Stone.Color getColor() {
        if (mPlayersCount == 2) {
            return Stone.Color.FREE;
        }
        mPlayersCount++;
        if (mPlayersCount == 1) {
            return Stone.Color.BLACK;
        } else if (mPlayersCount == 2){
            return Stone.Color.WHITE;
        }
        return Stone.Color.FREE;
    }

    public boolean isMyTurn(Stone.Color color) {
        boolean result;
        if (mPlayersCount != 2) {
            return false;
        }
        if (mLastTurnId == 0) {
            return color == Stone.Color.BLACK;
        }
        if ((mLastTurnId - 1) / 2 % 2 == 0) {
            result = color == Stone.Color.WHITE;
        } else {
            result = color == Stone.Color.BLACK;
        }
        return result;
    }

    public int[] getStoneById(int id) {
        if (id > mLastTurnId) {
            return new int[]{-1, -1};
        }
        return new int[]{mBoard.getStoneById(id).getKey(), mBoard.getStoneById(id).getValue()};
    }

    public Stone.Color getColorOfStone(int x, int y) {
        return mBoard.getColorOfStoneInCell(x, y);
    }

    public Stone.Color whoWon() {
        Stone.Color result = mBoard.whoWon();
        if (mBoard.whoWon() != Stone.Color.FREE) {
            mPlayersCount--;
            if (mPlayersCount == 0) {
                mBoard = new Board();
            }
        }
        return result;
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

    public Server() {
        mBoard = new Board();
        mPlayersCount = 0;

        final int PORT_NUMBER = 59342;
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.toString());
                RequestThread requestThread = new RequestThread(clientSocket);
                ResponseThread responseThread = new ResponseThread(clientSocket, this);
                requestThread.start();
                responseThread.start();

                requestThreads.add(requestThread);
                responseThreads.add(responseThread);


                Stone.Color playerColor = getColor();
                Response response = new Response();
                response.setParameter("type", "color");
                response.setParameter("color", getColorId(playerColor));
                requestThread.sendRequest(response);
                if (mPlayersCount == 2) {
                    break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        while (!serverSocket.isClosed()) {
            int trueTurn = 0;
            int falseTurn = 1;
            if (isMyTurn(Stone.Color.BLACK)) {
                System.out.println("BLACK");
                trueTurn = 0;
                falseTurn = 1;
            } else {
                System.out.println("WHITE");
                trueTurn = 1;
                falseTurn = 0;
            }
            Response responseTrue = new Response();
            responseTrue.setParameter("type", "turn");
            responseTrue.setParameter("turn", true);

            Response responseFalse = new Response();
            responseFalse.setParameter("type", "turn");
            responseFalse.setParameter("turn", false);

            requestThreads.get(trueTurn).sendRequest(responseTrue);
            requestThreads.get(falseTurn).sendRequest(responseFalse);

        }
    }
}
