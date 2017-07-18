package recruitment.iiitd.edu.microphone_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import recruitment.iiitd.edu.mew.R;


public class TouchView extends View {

    private static final int INVALID_POINTER_ID = -1;
    private Drawable mIcon;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;

    //active pointer to move our object.
    private int mActivePointerId = INVALID_POINTER_ID;

    //To provide scaling
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public TouchView(Context context) {
        super(context);
        init(context);
    }

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mIcon = context.getResources().getDrawable(R.drawable.last);

        mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());


        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        mIcon.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();


                mLastTouchX = x;
                mLastTouchY = y;

                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);



                if (!mScaleDetector.isInProgress()) {
                    //Calculate the distance moved
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    //Move the object
                    mPosX += dx;
                    mPosY += dy;

                    invalidate();
                }


                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {

                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();


            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }
}
