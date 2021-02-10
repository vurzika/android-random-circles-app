package com.viktorija.randomcircles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;


//  This class is responsible for drawing random
//  circles on view. It allows to add new circle on canvas
//  and clear all circles from canvas

public class CirclesDrawingView extends View {
    private static final int MIN_CIRCLE_RADIUS = 50;
    private static final int MAX_CIRCLE_RADIUS = 150;

    private final Random random;

    // Extra canvas & bitmap are used to preserve drawings
    // between view refreshes and redraw them
    private Canvas circlesCanvas;
    private Bitmap circlesBitmap;

    // stores how many circles there are on view
    private int numberOfCircles;

    public CirclesDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        random = new Random();
    }

    @Override
    protected void onSizeChanged(int width, int height,
                                 int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        // as we need to create custom bitmap of same
        // size as view size, we need to overwrite this method
        // to create bitmap only when view size is known
        // (or changed due to orientation change)

        clearCircles();
    }

    public void clearCircles() {
        // reset existing canvas to clear it's contents

        // create bitmap of same size as view
        // as it will overlay view's contents
        circlesBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        circlesCanvas = new Canvas(circlesBitmap);

        numberOfCircles = 0;
    }

    public void drawCircle() {
        // draw new circle on existing canvas
        int randomColor = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );

        Paint circlePaint = new Paint();
        circlePaint.setColor(randomColor);

        int randomRadius = MIN_CIRCLE_RADIUS +
                random.nextInt(MAX_CIRCLE_RADIUS - MIN_CIRCLE_RADIUS);

        int width = circlesCanvas.getWidth();
        int height = circlesCanvas.getHeight();

        // make sure circle always within the view
        int randomX = randomRadius +
                random.nextInt(width - 2 * randomRadius);
        int randomY = randomRadius +
                random.nextInt(height - 2 * randomRadius);

        circlesCanvas.drawCircle(randomX, randomY, randomRadius, circlePaint);

        // request view to redraw after changes
        postInvalidate();

        numberOfCircles++;
    }

    public int getNumberOfCircles() {
        return numberOfCircles;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // drawing bitmap on top of view that contains all
        // random circles that were drawn so far
        canvas.drawBitmap(circlesBitmap, 0, 0, null);
    }
}
