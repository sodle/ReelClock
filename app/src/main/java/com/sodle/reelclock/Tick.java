package com.sodle.reelclock;

public class Tick {

    public static final int MAJOR_TICK_LENGTH = 15;
    public static final int MINOR_TICK_LENGTH = 10;

    private int width = MINOR_TICK_LENGTH;
    private int vPos = MAJOR_TICK_LENGTH;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getvPos() {
        return vPos;
    }

    public void setvPos(int vPos) {
        this.vPos = vPos;
    }

    private String label = "";

    public Tick(int width, int vertical, String label) {
        this.width = width;
        this.vPos = vertical;
        this.label = label;
    }

}
