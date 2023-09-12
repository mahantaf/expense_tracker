package com.example.expensestracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;


public class CustomButton extends View {

    public interface OnCustomButtonClickListener {
        void onCustomButtonClick();
    }

    private Paint paint;
    private boolean isPressed = false;
    private String buttonText = "More Information";
    private OnCustomButtonClickListener clickListener;


    public void setOnCustomButtonClickListener(OnCustomButtonClickListener listener) {
        this.clickListener = listener;
    }

    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the button background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        float textWidth = paint.measureText(buttonText);

        // Calculate the x and y coordinates to center the text
        float x = (getWidth() - textWidth) / 2f;
        float y = getHeight() / 2f;

        // Draw the text
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(buttonText, x, y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Handle button press state here
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                // Handle button release state here
                if (clickListener != null) {
                    clickListener.onCustomButtonClick();
                }
                invalidate();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        // Handle the button click event here
        // Implement your custom button click logic
        return super.performClick();
    }
}

