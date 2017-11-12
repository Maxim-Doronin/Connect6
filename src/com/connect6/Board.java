package com.connect6;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

class Board {
    private Stone.Color[][] mBoard;
    private Map<Integer, Pair<Integer, Integer>> mStonesById;
    private Stone.Color mWhoWon = Stone.Color.FREE;

    Board() {
        mStonesById = new HashMap<>();
        mBoard = new Stone.Color[Resources.boardCellCount][Resources.boardCellCount];
        for (int row = 0; row < Resources.boardCellCount; row ++)
            for (int col = 0; col < Resources.boardCellCount; col++)
                mBoard[row][col] = Stone.Color.FREE;
    }

    void setCell(int x, int y, Stone.Color color, int id) {
        mBoard[x][y] = color;
        mStonesById.put(id, new Pair<>(x, y));
        lookForWin(x, y, color);
    }

    Pair<Integer, Integer> getStoneById(int id) {
        return mStonesById.get(id);
    }

    boolean isCellFree(int x, int y) {
        return mBoard[x][y] == Stone.Color.FREE;
    }

    Stone.Color getColorOfStoneInCell(int x, int y) {
        if (isCellFree(x, y)) {
            return Stone.Color.FREE;
        }
        return mBoard[x][y];
    }

    private void lookForWin(int x, int y, Stone.Color color) {
        int sum = 0;
        for (int currentX = Math.max(x-5, 0); currentX <= Math.min(x+5, Resources.boardCellCount-1); currentX++) {
            if (mBoard[currentX][y] == color) {
                sum++;
            } else {
                sum = 0;
            }
            if (sum == 6) {
                mWhoWon = color;
                return;
            }
        }
        sum = 0;
        for (int currentY = Math.max(y-5, 0); currentY <= Math.min(y+5, Resources.boardCellCount-1); currentY++) {
            if (mBoard[x][currentY] == color) {
                sum++;
            } else {
                sum = 0;
            }
            if (sum == 6) {
                mWhoWon = color;
                return;
            }
        }
        sum = 0;
        for (int i = -5; i <= 5; i++) {
            if (x+i < 0 || x+i >= Resources.boardCellCount || y+i < 0 || y+i >= Resources.boardCellCount) {
                continue;
            }
            if (mBoard[x+i][y+i] == color) {
                sum++;
            } else {
                sum = 0;
            }
            if (sum == 6) {
                mWhoWon = color;
                return;
            }
        }
        sum = 0;
        for (int i = -5; i <= 5; i++) {
            if (x+i < 0 || x+i >= Resources.boardCellCount || y-i < 0 || y-i >= Resources.boardCellCount) {
                continue;
            }
            if (mBoard[x+i][y-i] == color) {
                sum++;
            } else {
                sum = 0;
            }
            if (sum == 6) {
                mWhoWon = color;
                return;
            }
        }

    }

    Stone.Color whoWon() {
        return mWhoWon;
    }
}
