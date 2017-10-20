package com.game;

import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Client extends JPanel{

    private static JFrame frame = new JFrame("Connect6");
    private static int click_x, click_y;
    private static int x,y;
    private static int myColor;
    private static boolean myTurn = false;
    private static int []enemyTurn;
    private static int [][]board = new int[19][19];
    private static int turnCounter=1;

    private Client() {}

    public static void createGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(528, 550);
        frame.add(new Board());
        frame.setVisible(true);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                click_x = e.getX();
                click_y = e.getY();
            }
        });
    }

    public static void boardInit(){
        for(int i=0; i<18; i++)
            for(int j=0; j<18; j++)
                board[i][j] = 2;
    }

    public static void main(String[] args) {
        createGUI();
        boardInit();

        try {
            Registry registry = LocateRegistry.getRegistry(null);
            GameLogic stub = (GameLogic) registry.lookup("Server");

            myColor =   stub.getColor();
            if(myColor == 1)
                frame.setTitle("Connect6 - White");
            else
                frame.setTitle("Connect6 - Black");
            enemyTurn = new int[2];
            if(myColor==0)
                myTurn = true;

            while (true) {
                try {
                    if(myTurn) {
                        x = (click_x - 14 + 10) / 27;                                                        //Номер строки
                        y = (click_y - 14 + 10) / 27 - 1;                                                    //Номер столбца

                        if (x >= 0 && x <= 18 && y >= 0 && y <= 18 && board[x][y] == 2) {
                            board[x][y] = myColor;
                            stub.setPoint(x, y, myColor);

                            frame.add(new Stone(14 + 27 * x - 10, 14 + 27 * y - 10, myColor));    //14 - начальное смещение, 27 - расстояние между ячейками, 10 - половина размера камня
                            frame.setVisible(true);

                            click_x = -100;
                            click_y = -100;
                            turnCounter++;
                            if(turnCounter==2) {
                                myTurn = false;
                                turnCounter = 0;
                            }
                        }
                        Thread.sleep(15);
                    }
                    else{
                        enemyTurn = stub.getEnemyTurn();
                        if(enemyTurn[0] != -1){
                            frame.add(new Stone(14 + 27 * enemyTurn[0] - 10, 14 + 27 * enemyTurn[1] - 10, 1-myColor));
                            frame.setVisible(true);
                            board[enemyTurn[0]][enemyTurn[1]] = 1-myColor;
                            turnCounter++;
                            if(turnCounter==2) {
                                myTurn = true;
                                turnCounter = 0;
                            }
                        }
                        click_x = -100;
                        click_y = -100;
                        Thread.sleep(10);
                    }
                    int win = stub.whoWon();
                    if(win != 2) {
                        myTurn = false;
                        if(win == myColor)
                            JOptionPane.showMessageDialog(null, "Поздравляем! Вы победили!");
                        else
                            JOptionPane.showMessageDialog(null, "Соперник победил!");
                    }
                }
                catch (Exception e) {
                    System.err.println("SetPoint exception: " + e.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}