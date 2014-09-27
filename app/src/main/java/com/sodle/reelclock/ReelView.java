package com.sodle.reelclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class ReelView extends View {

    final int color_tick = Color.DKGRAY;
    final int color_label = Color.BLACK;
    final int color_backgroundShadow = Color.DKGRAY;
    final int color_backgroundFront = Color.WHITE;
    final int color_marker = Color.RED;
    private LinearGradient background;
    Paint p = new Paint();

    final int margin = 5;

    private int tickSpacing = 80;
    private int numMinorTicks = 3;

    private int majorTickLength = 50;
    private int minorTickLength = 25;

    private int pixelsPerMajorTick = 320;

    private int halfHeight = 0;

    private int highestTick = 0;

    private int min = 0;
    private int max = 100;
    private double value = 43;

    private ArrayList<Tick> ticks = new ArrayList<Tick>();

    public ReelView(Context c, AttributeSet a) {
        super(c, a);

        rebuildTicks();
    }

    public ReelView(Context c, AttributeSet a, int min, int max, double value, int tickSpacing, int numMinorTicks) {
        super(c, a);

        this.min = min;
        this.max = max;
        this.value = value;

        this.tickSpacing = tickSpacing;
        this.numMinorTicks = numMinorTicks;

        this.pixelsPerMajorTick = tickSpacing * (numMinorTicks + 1);

        rebuildTicks();
    }

    private void rebuildTicks() {
        ticks.clear();
        int height = 0;
        for (int i = min; i < max; i++) {
            ticks.add(new Tick(majorTickLength, height, i + ""));
            height += tickSpacing;
            for (int j = 0; j < numMinorTicks; j++) {
                ticks.add(new Tick(minorTickLength, height, ""));
                height += tickSpacing;
            }
            highestTick += majorTickLength;
        }
    }

    public void setValue(double v) {
        value = v;
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
        rebuildTicks();
        invalidate();
    }

    public void setNumMinorTicks(int numMinorTicks) {
        this.numMinorTicks = numMinorTicks;
        pixelsPerMajorTick = tickSpacing * (numMinorTicks + 1);
        rebuildTicks();
        invalidate();
    }

    private void drawTick(Tick t, Canvas c) {
        p.setColor(color_tick);
        p.setTextSize(50);

        int height = (int) Math.round(t.getvPos() - (value * pixelsPerMajorTick) + halfHeight);
        // Wrap above and below to give the illusion of looping
        int heightNeg = (int) Math.round(t.getvPos() - ((value - max) * pixelsPerMajorTick) + halfHeight);
        int heightPos = (int) Math.round(t.getvPos() - ((value + max) * pixelsPerMajorTick) + halfHeight);

        int hStart = getWidth() - t.getWidth() - margin;
        int hEnd = getWidth() - margin;

        c.drawLine(hStart, height, hEnd, height, p);
        c.drawLine(hStart, heightNeg, hEnd, heightNeg, p);
        c.drawLine(hStart, heightPos, hEnd, heightPos, p);
        c.drawText(t.getLabel(), hStart - 15, height - 5, p);
        c.drawText(t.getLabel(), hStart - 15, heightNeg - 5, p);
        c.drawText(t.getLabel(), hStart - 15, heightPos - 5, p);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        background = new LinearGradient(0, 0, 0, h, new int[] {color_backgroundShadow, color_backgroundFront, color_backgroundShadow}, new float[] {0, (float) 0.5, 1}, Shader.TileMode.MIRROR);

        majorTickLength = (w - (2 * margin)) / 2;
        minorTickLength = majorTickLength / 2;
        halfHeight = h / 2;

        rebuildTicks();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        majorTickLength = (canvas.getWidth() - (2 * margin)) / 2;
        minorTickLength = majorTickLength / 2;

        p.setShader(background);
        canvas.drawRect(margin, 0, getWidth() - margin, getHeight(), p);
        p.setShader(null);

        for (Tick t : ticks) {
            drawTick(t, canvas);
        }

        p.setColor(color_marker);
        p.setStrokeWidth(10);
        canvas.drawLine(margin, getHeight() / 2, getWidth() - margin, getHeight() / 2, p);
    }
}
