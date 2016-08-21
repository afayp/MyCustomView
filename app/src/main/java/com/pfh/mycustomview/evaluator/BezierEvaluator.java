package com.pfh.mycustomview.evaluator;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

import com.pfh.mycustomview.utils.BezierUtil;

/**
 * Created by Administrator on 2016/8/21.
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mControlPoint;

    public BezierEvaluator(PointF controlPoint) {
        this.mControlPoint = controlPoint;
    }

    @Override
    public PointF evaluate(float t, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForQuadratic(t, startValue, mControlPoint, endValue);
    }
}
