package de.htwg.moco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RobotUI extends View {
    Paint paint = new Paint();
    public float stopX = 20;
    public float stopY = 20;

    private void init() {
        paint.setColor(Color.BLACK);
    }

    public RobotUI(Context context) {
        super(context);
        init();
    }

    public RobotUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotUI(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(0, 0, this.stopX, this.stopY, paint);
        canvas.drawLine(20, 0, 0, 20, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if(e.getAction()==MotionEvent.ACTION_UP){
            this.stopX = x;
            this.stopY = y;
            this.postInvalidate();

        }

        return true;
    }

}


