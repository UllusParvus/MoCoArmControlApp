package de.htwg.moco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class RobotUI extends View {
    Paint basePaint = new Paint();
    Paint armPart1Paint = new Paint();
    Paint armPart2Paint = new Paint();
    Paint armPart3Paint = new Paint();
    Paint armPart4Paint = new Paint();
    Paint operationRadiusPaint = new Paint();

    private float baseCenterX;
    private float baseCenterY;
    private int armPartLengths = 200;
    private int gripperMountPartLength = 10;
    private int gripperMountRightLength = 30;
    private int gripperBaseLength = 100;
    private int gripperSidePartsLength = 10;
    private float armPart1StartX;
    private float armPart1StartY;
    private double armPart1StopX;
    private double armPart1StopY;
    private double armPart2StartX;
    private double armPart2StartY;
    private double armPart2StopX;
    private double armPart2StopY;
    private float gripperMountLeftStartX;
    private float gripperMountLeftStartY;
    private float gripperMountLeftStopX;
    private float gripperMountLeftStopY;
    private float gripperMountRightStartX;
    private float gripperMountRightStartY;
    private float gripperMountRightStopX;
    private float gripperMountRightStopY;
    private float gripperPartLeftStartX;
    private float gripperPartLeftStartY;
    private float gripperPartLeftStopX;
    private float gripperPartLeftStopY;
    private float gripperPartRightStartX;
    private float gripperPartRightStartY;
    private float gripperPartRightStopX;
    private float gripperPartRightStopY;

    private double joint1AngleDegrees;
    private double joint2AngleDegrees;
    private float gripperMountLeftAngleDegrees = 90;
    private float gripperPartLeftAngleDegrees = 270;
    private float gripperPartRightAngleDegrees = 90;
    private float gripperMountRightAngleDegrees = 270;

    private boolean init;

    private nxtRoboInverseKinematics nxtRoboInverseKinematics;
    private nxtRoboForwardKinematics nxtRoboForwardKinematics;

    private Matrix baseTransformationMatrix;

    Toast toast;

    public double getJoint1AngleDegrees() {
        return joint1AngleDegrees;
    }

    public void setJoint1AngleDegrees(double joint1AngleDegrees) {
        this.joint1AngleDegrees = joint1AngleDegrees;
    }

    public double getJoint2AngleDegrees() {
        return joint2AngleDegrees;
    }

    public void setJoint2AngleDegrees(double joint2AngleDegrees) {
        this.joint2AngleDegrees = joint2AngleDegrees;
    }

    private void init() {
        init = true;
        basePaint.setColor(Color.BLACK);
        armPart1Paint.setColor(Color.BLUE);
        armPart2Paint.setColor(Color.RED);
        armPart3Paint.setColor(Color.GREEN);
        armPart4Paint.setColor(Color.GRAY);
        operationRadiusPaint.setColor(Color.GRAY);
        armPart1Paint.setStrokeWidth(5);
        armPart2Paint.setStrokeWidth(5);
        armPart3Paint.setStrokeWidth(5);
        armPart4Paint.setStrokeWidth(5);
        operationRadiusPaint.setStrokeWidth(2);
        operationRadiusPaint.setStyle(Paint.Style.STROKE);

        nxtRoboInverseKinematics = new nxtRoboInverseKinematics(this.armPartLengths, this.gripperBaseLength);
        nxtRoboForwardKinematics = new nxtRoboForwardKinematics(this.armPartLengths, this.gripperBaseLength);

        baseTransformationMatrix = new Matrix();

        toast = Toast.makeText(this.getContext(), "Keine Konfiguration für diesen Punkt möglich", Toast.LENGTH_SHORT);
    }

    private void initGeometry(){
        baseCenterX = getWidth()/2;
        baseCenterY = getHeight()/2;
        armPart1StartX = baseCenterX;
        armPart1StartY = baseCenterY;

        updateCoordinates();

        init = false;
    }

    public RobotUI(Context context, double joint1AngleDegrees, double joint2AngleDegrees) {
        super(context);
        this.joint1AngleDegrees = joint1AngleDegrees;
        this.joint2AngleDegrees = joint2AngleDegrees;
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
        if (init){
            initGeometry();
        }

        canvas.drawCircle(baseCenterX, baseCenterY, 20, basePaint);
        canvas.drawCircle(baseCenterX, baseCenterY, armPartLengths + gripperBaseLength, operationRadiusPaint);
        canvas.drawLine(armPart1StartX, armPart1StartY, (float) armPart1StopX, (float) armPart1StopY, armPart1Paint);
        canvas.drawLine((float) armPart2StartX, (float) armPart2StartY, (float) armPart2StopX, (float) armPart2StopY, armPart2Paint);
        //canvas.drawLine(gripperMountLeftStartX, gripperMountLeftStartY, gripperMountLeftStopX, gripperMountLeftStopY, armPart3Paint);
        //canvas.drawLine(gripperMountRightStartX, gripperMountRightStartY, gripperMountRightStopX, gripperMountRightStopY, armPart3Paint);
        //canvas.drawLine(gripperPartLeftStartX, gripperPartLeftStartY, gripperPartLeftStopX, gripperPartLeftStopY, armPart3Paint);
        //canvas.drawLine(gripperPartRightStartX, gripperPartRightStartY, gripperPartRightStopX, gripperPartRightStopY, armPart3Paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if(e.getAction()==MotionEvent.ACTION_MOVE || e.getAction() == MotionEvent.ACTION_DOWN){
            float[] touchPoint = {x,y};
            baseTransformationMatrix.setTranslate(-baseCenterX, -baseCenterY);
            this.baseTransformationMatrix.mapPoints(touchPoint);

            // inverse kinematics
            nxtRoboInverseKinematics.doInverseKinematics(touchPoint[1], touchPoint[0]);
            this.joint1AngleDegrees = Math.toDegrees(nxtRoboInverseKinematics.getQ1());
            this.joint2AngleDegrees = Math.toDegrees(nxtRoboInverseKinematics.getQ2());

            if (Double.isNaN(this.joint1AngleDegrees) || Double.isNaN(this.joint2AngleDegrees)){
                toast.show();
            } else {
                toast.cancel();
                updateCoordinates();
                this.postInvalidate();
            }

            // TODO: figure out joint constraints and implement a check here
            // if q_n_min <= q_n <= q_n_max ok else Toast -> constraints harmed

            Log.i("onTouchEvent", String.valueOf(x) + ", " + String.valueOf(y));
        }
        return true;
    }

    private void updateCoordinates() {
        // forward kinematics
        nxtRoboForwardKinematics.doForwardKinematics(Math.toRadians(this.joint1AngleDegrees), Math.toRadians(this.joint2AngleDegrees));

        double[] endPointArm1FromKinematics = nxtRoboForwardKinematics.getEndPointArm1();
        double[] endPointArm2FromKinematics = nxtRoboForwardKinematics.getEndPointArm2();

        // change x and y because of flipped axis in visualization (compared to
        float[] endPointArm1 = {(float)endPointArm1FromKinematics[1], (float)endPointArm1FromKinematics[0]};
        float[] endPointArm2 = {(float)endPointArm2FromKinematics[1], (float)endPointArm2FromKinematics[0]};

        // transform the results to the "translationed" base (to the middle of the screen)
        this.baseTransformationMatrix.setTranslate(baseCenterX, baseCenterY);
        this.baseTransformationMatrix.mapPoints(endPointArm1);
        this.baseTransformationMatrix.mapPoints(endPointArm2);

        // set coordinates
        this.armPart1StopX = endPointArm1[0];
        this.armPart1StopY = endPointArm1[1];
        this.armPart2StartX = this.armPart1StopX;
        this.armPart2StartY = this.armPart1StopY;
        this.armPart2StopX = endPointArm2[0];
        this.armPart2StopY = endPointArm2[1];
    }

}


