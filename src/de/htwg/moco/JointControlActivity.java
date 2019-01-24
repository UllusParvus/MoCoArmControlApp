package de.htwg.moco;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

/**
 * This class controls the activity with which the user can control each joint of the robot
 * individually with buttons.
 *
 * @author Christoph Ulrich
 * @author Benjamin Schaefer
 * @see <a href="https://wiki.ros.org/android_core">ROS core library for Android applications</a>
 */
public class JointControlActivity extends RosActivity {

    private JointTalker nxt_talker;

    /**
     * Creates a RosActivity. Important: here you specifiy the ROS_MASTER_URI
     */
    public JointControlActivity() {
        super("RosMindStormManipulator", "RosMindStormManipulator", URI.create("http://10.42.0.33:11311"));
    }

    /**
     * Initializes the graphical components of the activity and sets neccessary listeners.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_joint_control);
        //findViewById(R.id.buttonJointControl);

        Button b_joint_0_p, b_joint_0_m, b_joint_1_p, b_joint_1_m,b_joint_2_p,b_joint_2_m,b_gripper_p,b_gripper_m;
        b_joint_0_p = findViewById(R.id.b_joint_0_p);
        b_joint_0_m = findViewById(R.id.b_joint_0_m);
        b_joint_1_p = findViewById(R.id.b_joint_1_p);
        b_joint_1_m = findViewById(R.id.b_joint_1_m);
        b_joint_2_p = findViewById(R.id.b_joint_2_p);
        b_joint_2_m = findViewById(R.id.b_joint_2_m);
        b_gripper_p = findViewById(R.id.b_gripper_p);
        b_gripper_m = findViewById(R.id.b_gripper_m);
        b_joint_0_p.setId(R.id.b_joint_0_p);
        b_joint_0_m.setId(R.id.b_joint_0_m);
        b_joint_1_p.setId(R.id.b_joint_1_p);
        b_joint_1_m.setId(R.id.b_joint_1_m);
        b_joint_2_p.setId(R.id.b_joint_2_p);
        b_joint_2_m.setId(R.id.b_joint_2_m);
        b_gripper_p.setId(R.id.b_gripper_p);
        b_gripper_m.setId(R.id.b_gripper_m);
        MyTouchListener touchListener = new MyTouchListener();
        b_joint_0_p.setOnTouchListener(touchListener);
        b_joint_0_m.setOnTouchListener(touchListener);
        b_joint_1_p.setOnTouchListener(touchListener);
        b_joint_1_m.setOnTouchListener(touchListener);
        b_joint_2_p.setOnTouchListener(touchListener);
        b_joint_2_m.setOnTouchListener(touchListener);
        b_gripper_p.setOnTouchListener(touchListener);
        b_gripper_m.setOnTouchListener(touchListener);
    }

    /**
     * This nested class handles touch events which are happening in the user interface.
     */
    public class MyTouchListener implements View.OnTouchListener {

        /**
         * This method sets the values (motor name and effort) for the ROS nxt message dependent on
         * the clicked button. This values are then submitted to the ROS node which publishes the
         * message to the corresponding ROS topic.
         * @param v The view.
         * @param event The motion event with information about what element was touched.
         * @return Always returns true.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int motor_positive = 1;
            String motor_name = "";
            double motor_effort = 0.7;
            switch(v.getId()){
                case R.id.b_joint_1_m:
                    motor_positive = -1;
                case R.id.b_joint_1_p:
                    motor_name = "motor_1";
                    motor_effort = 0.8;
                    break;
                case R.id.b_joint_2_m:
                    motor_positive = -1;
                case R.id.b_joint_2_p:
                    motor_name = "motor_2";
                    motor_effort = 0.8;
                    break;
                case R.id.b_gripper_m:
                    motor_positive = -1;
                case R.id.b_gripper_p:
                    motor_name = "motor_3";
                    motor_effort = 0.7;
                    break;
                case R.id.b_joint_0_p:
                    motor_name = "motor_base";
                    motor_effort = -0.7;
                    break;
                case R.id.b_joint_0_m:
                    motor_name = "motor_base";
                    motor_effort = 0.7;
                    break;
        }
        int action = event.getAction();
        if (MotionEvent.ACTION_DOWN == action) {
            v.setPressed(true);
            if (motor_name == "motor_base") {
                nxt_talker.loop_nxt2(motor_effort, motor_name);
            } else {
                nxt_talker.loop_nxt1(motor_effort * motor_positive, motor_name);
            }
        }
        else if (MotionEvent.ACTION_UP == action) {
            v.setPressed(false);
            nxt_talker.loop_nxt1(0, motor_name);
            nxt_talker.loop_nxt2(0, motor_name);
        }
        return true;
    }

  }

    /**
     * Initializes the ROS node configuration for the nxt message publisher node.
     * @param nodeMainExecutor automatically submitted on start of this activity.
     */
    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
    nxt_talker = new JointTalker();

    NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
    nodeConfiguration.setMasterUri(getMasterUri());
    nodeMainExecutor.execute(nxt_talker, nodeConfiguration);
    }


}
