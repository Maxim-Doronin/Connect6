package com.connect6;

import java.io.Serializable;

public class ReceivedStone implements Serializable {
    public int x;
    public int y;
    public int color;
    ReceivedStone(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
