package de.htwg.moco;

import de.htwg.moco.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This class controls and updates the graphical user interface in which the user can control the
 * robot by touching a free point in the robots operational space.
 *
 * @author Christoph Ulrich
 * @author Benjamin Schaefer
 */

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
    private int gripperBaseLength = 100;
    private float armPart1StartX;
    private float armPart1StartY;
    private double armPart1StopX;
    private double armPart1StopY;
    private double armPart2StartX;
    private double armPart2StartY;
    private double armPart2StopX;
    private double armPart2StopY;

    private double joint1AngleDegrees;
    private double joint2AngleDegrees;

    private boolean init;

    private nxtRoboInverseKinematics nxtRoboInverseKinematics;
    private nxtRoboForwardKinematics nxtRoboForwardKinematics;

    private HardwareToUIUnitConverter hardwareToUIUnitConverter;

    private Matrix baseTransformationMatrix;

    private double[] hardwareStartingPositions = new double[3];

    public void setHardwareStartingPositions(double[] hardwareStartingPositions) {
        this.hardwareStartingPositions = hardwareStartingPositions;
    }

    public double[] getHardwareStartingPositions() {
        return hardwareStartingPositions;
    }

    Toast impossibleConfigToast;
    Toast impossibleHWConfigToast;

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

    /**
     * Initializes values for the user interface. Creates Paint objects to draw the arm elements,
     * the {@link nxtRoboForwardKinematics} and {@link nxtRoboInverseKinematics} to calculate
     * get the kinematic values, the {@link HardwareToUIUnitConverter} to get motor encoder
     * positions (in radians), a transformation matrix to transform points to the coordinates of
     * the base origin in the user interface and finally initial calibration values. The latter
     * are initialized with a NaN value to determine if they have to be obtained from the {@link JointStateSub}
     * for the first start.
     */
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

        hardwareToUIUnitConverter = new HardwareToUIUnitConverter();

        baseTransformationMatrix = new Matrix();

        impossibleConfigToast = Toast.makeText(this.getContext(), "Keine Konfiguration für diesen Punkt möglich", Toast.LENGTH_SHORT);
        impossibleHWConfigToast = Toast.makeText(this.getContext(), "Mit der aktuellen Hardware-Konfiguration nicht erreichbar", Toast.LENGTH_SHORT);

        hardwareStartingPositions[0] = Double.NaN;
        hardwareStartingPositions[1] = Double.NaN;
        hardwareStartingPositions[2] = Double.NaN;
    }

    /**
     * Initializes coordinate values.
     */
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
        Log.i("UI - Constructor", "joint 1 -> " + this.joint1AngleDegrees + ", joint 2 -> " + this.joint2AngleDegrees);
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

    /**
     * This method draws all elements on the user interface canvas.
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        if (init){
            initGeometry();
        }
        Log.i("UI", "joint 1 -> " + this.joint1AngleDegrees + ", joint 2 -> " + this.joint2AngleDegrees);
        canvas.drawCircle(baseCenterX, baseCenterY, 20, basePaint);
        canvas.drawCircle(baseCenterX, baseCenterY, armPartLengths + gripperBaseLength, operationRadiusPaint);
        canvas.drawLine(armPart1StartX, armPart1StartY, (float) armPart1StopX, (float) armPart1StopY, armPart1Paint);
        canvas.drawLine((float) armPart2StartX, (float) armPart2StartY, (float) armPart2StopX, (float) armPart2StopY, armPart2Paint);

        Paint rectPaint = new Paint();
        rectPaint.setColor(Color.GRAY);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(20);

        Rect rotateBaseClockwise = new Rect(0, getHeight() - 100, getWidth()/4 - 1, getHeight());
        Rect rotateBaseCounterClockwise = new Rect(getWidth()/4 + 1, getHeight() - 100, 2 * (getWidth()/4) - 1, getHeight());
        Rect gripperOpen = new Rect(2 * (getWidth()/4) + 1, getHeight() - 100, 3 * (getWidth()/4) - 1, getHeight());
        Rect gripperClose = new Rect(3 * (getWidth()/4) + 1, getHeight() - 100, getWidth(), getHeight());

        canvas.drawRect(rotateBaseClockwise, rectPaint);
        canvas.drawRect(rotateBaseCounterClockwise, rectPaint);
        canvas.drawRect(gripperOpen, rectPaint);
        canvas.drawRect(gripperClose, rectPaint);
        canvas.drawText("Rotate base +", 5, getHeight() - 50, textPaint);
        canvas.drawText("Rotate base -", getWidth()/4 + 5, getHeight() - 50, textPaint);
        canvas.drawText("Open gripper", 2 * (getWidth()/4) + 5, getHeight() - 50, textPaint);
        canvas.drawText("Close gripper", 3 * (getWidth()/4) + 5, getHeight() - 50, textPaint);
    }

    /**
     * Handles reactions to touch events.
     * At first it is checked if there are already calibration values available or if they have to
     * be fetched from the {@link JointStateSub}. Then it is determined whether and which of the
     * control buttons (base rotation / gripper control) was activated. If so, the corresponding
     * motor control values are transmitted to the {@link JointTalker}, which then sends the comands
     * to the correct ROS topic. If none of the buttons was touched, it is checked whether the
     * operational space of the robot was touched. If so, the {@link nxtRoboInverseKinematics}
     * calculates the joint angles. If we get a valid solution for the UI and the hardware configuration,
     * the {@link HardwareToUIUnitConverter} delivers the corresponding motor position radians. These
     * are then send via the {@link ArmPositionActionClient} to the server-side action server.
     * @param e The event containing information about the position and the type of the touch event.
     * @return Always returns true.
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if (Double.isNaN(hardwareStartingPositions[0])){
            hardwareStartingPositions[0] = VisualTCPControlActivity.jointStateSub.getMotor1_position();
            hardwareStartingPositions[1] = VisualTCPControlActivity.jointStateSub.getMotor2_position();
            hardwareStartingPositions[2] = VisualTCPControlActivity.jointStateSub.getMotor3_position();
            Log.i("UI", "start joint 1 -> " + hardwareStartingPositions[0] + ", start joint 2 -> " + hardwareStartingPositions[1] + ", start gripper -> " + hardwareStartingPositions[2]);
        }

            // check if one and which of the buttons was pressed
            if (x >= 0 && x <= getWidth()/4 - 1 && y >= getHeight() - 100 && y <= getHeight()){
                if (e.getAction()==MotionEvent.ACTION_DOWN){
                    VisualTCPControlActivity.nxt_talker.loop_nxt2(0.7, "motor_base");
                } else if (e.getAction() == MotionEvent.ACTION_UP ){
                    VisualTCPControlActivity.nxt_talker.loop_nxt2(0.0, "motor_base");
                }
            } else if (x >= getWidth()/4 + 1 && x <= 2 * (getWidth()/4) - 1 && y >= getHeight() - 100 && y <= getHeight()) {
                if (e.getAction()==MotionEvent.ACTION_DOWN){
                    VisualTCPControlActivity.nxt_talker.loop_nxt2(-0.7, "motor_base");
                } else if (e.getAction() == MotionEvent.ACTION_UP ){
                    VisualTCPControlActivity.nxt_talker.loop_nxt2(0.0, "motor_base");
                }
            } else if (x >= 2 * (getWidth()/4) + 1 && x <= 3 * (getWidth()/4) - 1 && y >= getHeight() - 100 && y <= getHeight()) {
                if (e.getAction()==MotionEvent.ACTION_DOWN){
                    VisualTCPControlActivity.nxt_talker.loop_nxt1(0.7, "motor_3");
                } else if (e.getAction() == MotionEvent.ACTION_UP ){
                    VisualTCPControlActivity.nxt_talker.loop_nxt1(0.0, "motor_3");
                }
            }else if (x >= 3 * (getWidth()/4) + 1 && x <= getWidth() && y >= getHeight() - 100 && y <= getHeight()) {
                if (e.getAction()==MotionEvent.ACTION_DOWN){
                    VisualTCPControlActivity.nxt_talker.loop_nxt1(-0.7, "motor_3");
                } else if (e.getAction() == MotionEvent.ACTION_UP ){
                    VisualTCPControlActivity.nxt_talker.loop_nxt1(0.0, "motor_3");
                }
            } else {
                if(e.getAction()==MotionEvent.ACTION_MOVE || e.getAction() == MotionEvent.ACTION_DOWN){
                    float[] touchPoint = {x,y};
                    baseTransformationMatrix.setTranslate(-baseCenterX, -baseCenterY);
                    this.baseTransformationMatrix.mapPoints(touchPoint);

                    nxtRoboInverseKinematics.doInverseKinematics(touchPoint[1], touchPoint[0]);
                    this.joint1AngleDegrees = Math.toDegrees(nxtRoboInverseKinematics.getQ1());
                    this.joint2AngleDegrees = Math.toDegrees(nxtRoboInverseKinematics.getQ2());

                    if (Double.isNaN(this.joint1AngleDegrees) || Double.isNaN(this.joint2AngleDegrees)){
                        impossibleConfigToast.show();
                    } else {
                        impossibleConfigToast.cancel();
                        updateCoordinates();
                        this.postInvalidate();

                        if (this.joint1AngleDegrees > -95 || this.joint2AngleDegrees > 90 || this.joint2AngleDegrees < -40){
                            impossibleHWConfigToast.show();
                        } else {
                            impossibleHWConfigToast.cancel();
                            float joint1Goal = (float) hardwareToUIUnitConverter.getMotor1RotationRadians(this.joint1AngleDegrees + 180);
                            Log.i("UI", "joint1Goal w/o current position -> " + joint1Goal);
                            joint1Goal = (float) hardwareStartingPositions[0] - joint1Goal;

                            Log.i("UI", "joint1Goal -> " + joint1Goal);
                            float joint2Goal = (float) hardwareToUIUnitConverter.getMotor2RotationRadians(this.joint2AngleDegrees + 45);
                            joint2Goal = (float) hardwareStartingPositions[1] + joint2Goal;
                            float[] jointPositions = new float[]{joint1Goal, joint2Goal};
                            Log.i("UI", "joint2Goal -> " + joint2Goal);
                            List<String> jointNames = new ArrayList<String>();
                            jointNames.add("motor_1"); jointNames.add("motor_2"); //jointNames.add("motor_3");
                            VisualTCPControlActivity.armPositionActionClient.sendAction(jointNames, jointPositions);
                        }

                    }

                    Log.i("onTouchEvent", String.valueOf(x) + ", " + String.valueOf(y));
                }
            }
        return true;
    }

    /**
     * Takes the calculated inverse kinematics solution, propagates it through the {@link nxtRoboForwardKinematics}
     * module and transforms the resulting coordinates to the base of the robot arm in the UI.
     */
    private void updateCoordinates() {
        Log.i("UI", "joint 1 -> " + this.joint1AngleDegrees + ", joint 2 -> " + this.joint2AngleDegrees);
        nxtRoboForwardKinematics.doForwardKinematics(Math.toRadians(this.joint1AngleDegrees), Math.toRadians(this.joint2AngleDegrees));

        double[] endPointArm1FromKinematics = nxtRoboForwardKinematics.getEndPointArm1();
        double[] endPointArm2FromKinematics = nxtRoboForwardKinematics.getEndPointArm2();

        /* change x and y because of flipped axis in visualization */
        float[] endPointArm1 = {(float)endPointArm1FromKinematics[1], (float)endPointArm1FromKinematics[0]};
        float[] endPointArm2 = {(float)endPointArm2FromKinematics[1], (float)endPointArm2FromKinematics[0]};

        /* transform the results to the "translationed" base (to the middle of the screen)*/
        this.baseTransformationMatrix.setTranslate(baseCenterX, baseCenterY);
        this.baseTransformationMatrix.mapPoints(endPointArm1);
        this.baseTransformationMatrix.mapPoints(endPointArm2);

        this.armPart1StopX = endPointArm1[0];
        this.armPart1StopY = endPointArm1[1];
        this.armPart2StartX = this.armPart1StopX;
        this.armPart2StartY = this.armPart1StopY;
        this.armPart2StopX = endPointArm2[0];
        this.armPart2StopY = endPointArm2[1];
    }

}


