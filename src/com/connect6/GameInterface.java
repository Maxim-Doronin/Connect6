package com.connect6;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {
    void setStone(int x, int y, Stone.Color color) throws RemoteException;
    Stone.Color getColor() throws RemoteException;
    boolean isMyTurn(Stone.Color color) throws RemoteException;
    int[] getStoneById(int id) throws RemoteException;
    Stone.Color getColorOfStone(int x, int y) throws RemoteException;
    Stone.Color whoWon() throws RemoteException;
}
