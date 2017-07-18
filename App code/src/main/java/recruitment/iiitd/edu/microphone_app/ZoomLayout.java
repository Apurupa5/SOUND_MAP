package recruitment.iiitd.edu.microphone_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;

//To support zooming of floor image
public class ZoomLayout extends RelativeLayout {

    private static final int INVALID_POINTER_ID =  -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    // For Scaling
    private float mScaleFactor = 1.0f;
    private float mFocusX;
    private float mFocusY;
    private ScaleGestureDetector mScaleDetector;
    private Matrix mScaleMatrix;
    private Matrix mScaleMatrixInverse;

    // For Moving
    private float mTranslateX;
    private float mTranslateY;
    private Matrix mTranslateMatrix;
    private Matrix mTranslateMatrixInverse;

    private float mLastTouchX;
    private float mLastTouchY;

    public ZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mScaleMatrix = new Matrix();
        mScaleMatrixInverse = new Matrix();
        mTranslateMatrix = new Matrix();
        mTranslateMatrixInverse = new Matrix();
        mTranslateMatrix.setTranslate(0, 0);
        mScaleMatrix.setScale(1, 1);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public void scale(float scaleFactor, float focusX, float focusY) {
        mScaleFactor = scaleFactor;
        mFocusX = focusX;
        mFocusY = focusY;
        invalidate();
    }

    public void restore() {
        mScaleFactor = 1.0f;
        mTranslateX = 0f;
        mTranslateY = 0f;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // To draw the child view
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(mTranslateX, mTranslateY);
        canvas.scale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
        super.dispatchDraw(canvas);     //pass to child view
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Listener to access all touchevent
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
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mTranslateX += dx;
                    mTranslateY += dy;


                    mTranslateMatrix.preTranslate(dx, dy);
                    mTranslateMatrix.invert(mTranslateMatrixInverse);

                    mLastTouchX = x;
                    mLastTouchY = y;
                }

                invalidate();
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

            if (detector.isInProgress()) {

                mFocusX = detector.getFocusX();
                mFocusY = detector.getFocusY();
            }
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            mScaleMatrix.setScale(mScaleFactor, mScaleFactor, mFocusX, mFocusY);
            mScaleMatrix.invert(mScaleMatrixInverse);

            scale(mScaleFactor, mFocusX, mFocusY);

            return true;
        }
    }
}
