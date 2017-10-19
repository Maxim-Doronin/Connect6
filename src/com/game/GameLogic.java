package com.game;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameLogic extends Remote{
    void setPoint(int x, int y, int color)      throws RemoteException;
    int getColor()                              throws RemoteException;
    int[] getEnemyTurn()                        throws RemoteException;
    int whoWon()                                throws RemoteException;
}
