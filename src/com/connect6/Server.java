package com.connect6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements GameInterface {
    private Board mBoard;
    private int mPlayersCount;
    private int mLastTurnId = 0;

    private Server() {
        mBoard = new Board();
        mPlayersCount = 0;
    }

    public void setStone(int x, int y, Stone.Color color) {
        if (mBoard.isCellFree(x,y)) {
            mLastTurnId++;
        }
        mBoard.setCell(x, y, color, mLastTurnId);
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

    public static void main(String args[]) {
        try {
            Server obj = new Server();
            GameInterface gameInterface = (GameInterface) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Server", gameInterface);
            System.out.println("Server is ready!");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
