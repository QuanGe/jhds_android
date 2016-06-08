package com.quange.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class CircularProgressButton extends Button {
	private CircularAnimatedDrawable mAnimatedDrawable;
	private CircularProgressDrawable mProgressDrawable;
	private int mPaddingProgress = 0;
	public CircularProgressButton(Context context) {
        super(context);
        init(context, null);
    }

    public CircularProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
    	
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawIndeterminateProgress(canvas);
        drawProgress(canvas);
            
    }
    private void drawIndeterminateProgress(Canvas canvas) {
        if (mAnimatedDrawable == null) {
            int offset = (getWidth() - getHeight()) / 2;
            mAnimatedDrawable = new CircularAnimatedDrawable(0xffff0000, 3);
            int left = offset + mPaddingProgress;
            int right = getWidth() - offset - mPaddingProgress;
            int bottom = getHeight() - mPaddingProgress;
            int top = mPaddingProgress;
            mAnimatedDrawable.setBounds(left, top, right, bottom);
            mAnimatedDrawable.setCallback(this);
            mAnimatedDrawable.start();
        } else {
            mAnimatedDrawable.draw(canvas);
        }
    }
    
    private void drawProgress(Canvas canvas) {
    	if (mProgressDrawable == null) {
            int offset = (getWidth() - getHeight()) / 2;
            mProgressDrawable = new CircularProgressDrawable(0xffff0000, 3);
            int left = offset + mPaddingProgress;
            int right = getWidth() - offset - mPaddingProgress;
            int bottom = getHeight() - mPaddingProgress;
            int top = mPaddingProgress;
            mProgressDrawable.setBounds(left, top, right, bottom);
            mProgressDrawable.setCallback(this);
            mProgressDrawable.start();
        } else {
        	mProgressDrawable.draw(canvas);
        }
    }

    
    @Override
    protected void drawableStateChanged() {
        
        super.drawableStateChanged();
        
    }

    
    @Override
    protected boolean verifyDrawable(Drawable who) {
    	if(who == mProgressDrawable && mProgressDrawable.isfinish())
    	{
    		//动画结束后执行啥
    	}
    		
        return who == mProgressDrawable || super.verifyDrawable(who);
    }
    
    public void cancel()
    {
    	mProgressDrawable.stop();
    }
}
