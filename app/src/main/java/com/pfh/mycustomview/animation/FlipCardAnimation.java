package com.pfh.mycustomview.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Administrator on 2016/8/21.
 */
public class FlipCardAnimation extends Animation {

    private Camera mCamera;

    private final float mFromDegrees;

    private final float mToDegrees;

    private final float mCenterX;

    private final float mCenterY;

    //用于确定内容是否开始变化
    private boolean isContentChange = false;
    private OnContentChangeListener listener;

    public FlipCardAnimation(float fromDegrees, float toDegrees,float centerX, float centerY) {

        mFromDegrees = fromDegrees;

        mToDegrees = toDegrees;

        mCenterX = centerX;

        mCenterY = centerY;
    }

    public void setCanContentChange(){
        this.isContentChange = false;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        final float fromDegrees = mFromDegrees;

        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;

        final float centerY = mCenterY;

        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();

        if (degrees>90 || degrees<-90){
            if (!isContentChange){
                if(listener!=null){
                    listener.contentChange();
                }
                isContentChange = true;
            }

            if (degrees>0) {
                degrees = 270 + degrees - 90;
            }else if (degrees<0){
                degrees = -270+(degrees+90);
            }
        }

        camera.rotateX(degrees);

        camera.getMatrix(matrix);

        camera.restore();

        matrix.preTranslate(-centerX, -centerY);

        matrix.postTranslate(centerX, centerY);
    }

    public interface OnContentChangeListener{
        void contentChange();
    }

    public void setOnContentChangeListener(OnContentChangeListener listener) {
        this.listener = listener;
    }
}

