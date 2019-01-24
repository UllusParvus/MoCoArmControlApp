package de.htwg.moco;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;


/**
 * This class controls the activity in which the user can control the robot by touching a free point
 * in the robots operational space.
 *
 * @author Christoph Ulrich
 * @author Benjamin Schaefer
 * @see <a href="https://wiki.ros.org/android_core">ROS core library for Android applications</a>
 */
public class VisualTCPControlActivity extends RosActivity {

    static final String JOINT_1_ANGLE_DEGREES = "joint1AngleDegrees";
    static final String JOINT_2_ANGLE_DEGREES = "joint2AngleDegrees";
    static final String JOINT_1_STARTING_RADIANS = "joint1StartingRadians";
    static final String JOINT_2_STARTING_RADIANS = "joint2StartingRadians";
    static final String JOINT_3_STARTING_RADIANS = "joint3StartingRadians";
    public static ArmPositionActionClient armPositionActionClient;
    public static JointStateSub jointStateSub;
    public static JointTalker nxt_talker;
    RobotUI robotUI;

    /**
     * Creates a RosActivity. Important: here you specifiy the ROS_MASTER_URI
     */
    public VisualTCPControlActivity() {
        super("VisualControl", "VisualControl", URI.create("http://10.42.0.33:11311"));
    }

    /**
     * Checks if the activity was started for the first time (regarding to its lifecycle). If so,
     * then the {@link RobotUI} has to be created with standard values for the joint angles and
     * get the calibration values of the arm from the {@link JointStateSub}.
     * If not, the {@link RobotUI} gets the previous joint angles and the calibration values from
     * the shared preferences (set by this activity).
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if (b != null){
            if (b.getBoolean("firstStart")){
                robotUI = new RobotUI(this, 180.0, -45.0);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Log.i("VTCPAct", "onCreate, firstStart");
            } else {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                robotUI = new RobotUI(this, (double) sharedPreferences.getFloat(JOINT_1_ANGLE_DEGREES, 180),
                        (double) sharedPreferences.getFloat(JOINT_2_ANGLE_DEGREES, -45));
                double[] hardwareStartingPositions = {(double) sharedPreferences.getFloat(JOINT_1_STARTING_RADIANS, Float.NaN),
                        (double) sharedPreferences.getFloat(JOINT_2_STARTING_RADIANS, Float.NaN),
                        (double) sharedPreferences.getFloat(JOINT_3_STARTING_RADIANS, Float.NaN)};
                robotUI.setHardwareStartingPositions(hardwareStartingPositions);
                Log.i("VTCPAct", "onCreate, sharedPref, joint 1 start -> " + sharedPreferences.getFloat(JOINT_1_STARTING_RADIANS, 111.0f));
            }
        }

        robotUI.setBackgroundColor(Color.WHITE);
        setContentView(robotUI);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("TCPControl","onDestroy");
        savePreferences();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putDouble(JOINT_1_ANGLE_DEGREES, robotUI.getJoint1AngleDegrees());
        savedInstanceState.putDouble(JOINT_2_ANGLE_DEGREES, robotUI.getJoint2AngleDegrees());
        double[] hardwareStartingPositions = robotUI.getHardwareStartingPositions();
        savedInstanceState.putDouble(JOINT_1_STARTING_RADIANS, hardwareStartingPositions[0]);
        savedInstanceState.putDouble(JOINT_2_STARTING_RADIANS, hardwareStartingPositions[1]);
        savedInstanceState.putDouble(JOINT_3_STARTING_RADIANS, hardwareStartingPositions[2]);
        Log.d("onSaveInstanceState", "saved angle 1 -> " + savedInstanceState.getDouble(JOINT_1_ANGLE_DEGREES));
        Log.d("onSaveInstanceState", "saved angle 2 -> " + savedInstanceState.getDouble(JOINT_2_ANGLE_DEGREES));
        Log.d("onSaveInstanceState", "saved joint 1 start -> " + savedInstanceState.getDouble(JOINT_1_STARTING_RADIANS));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        robotUI.setJoint1AngleDegrees(savedInstanceState.getDouble(JOINT_1_ANGLE_DEGREES));
        robotUI.setJoint2AngleDegrees(savedInstanceState.getDouble(JOINT_2_ANGLE_DEGREES));
        double[] hardwareStartingPositions = {savedInstanceState.getDouble(JOINT_1_STARTING_RADIANS),
                savedInstanceState.getDouble(JOINT_2_STARTING_RADIANS),
                savedInstanceState.getDouble(JOINT_3_STARTING_RADIANS)};
        robotUI.setHardwareStartingPositions(hardwareStartingPositions);
        Log.d("onRestoreInstanceState", "restored angle 1 -> " + savedInstanceState.getDouble(JOINT_1_ANGLE_DEGREES));
        Log.d("onRestoreInstanceState", "restored angle 2 -> " + savedInstanceState.getDouble(JOINT_2_ANGLE_DEGREES));
        Log.d("onRestoreInstanceState", "restored joint 1 start -> " + savedInstanceState.getDouble(JOINT_1_STARTING_RADIANS));
    }

    private void savePreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(JOINT_1_ANGLE_DEGREES, (float) robotUI.getJoint1AngleDegrees());
        editor.putFloat(JOINT_2_ANGLE_DEGREES, (float) robotUI.getJoint2AngleDegrees());
        editor.putFloat(JOINT_1_STARTING_RADIANS, (float) robotUI.getHardwareStartingPositions()[0]);
        editor.putFloat(JOINT_2_STARTING_RADIANS, (float) robotUI.getHardwareStartingPositions()[1]);
        editor.putFloat(JOINT_3_STARTING_RADIANS, (float) robotUI.getHardwareStartingPositions()[2]);
        editor.commit();
        Log.i("VTCPAct", "savePreferences, joint 1 start -> " + sharedPreferences.getFloat(JOINT_1_STARTING_RADIANS, 111.0f));
    }

    /**
     * Initializes the ROS node configuration for the {@link ArmPositionActionClient},
     * the {@link JointStateSub} and the {@link JointTalker}.
     * @param nodeMainExecutor automatically submitted on start of this activity.
     */
    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        armPositionActionClient = new ArmPositionActionClient();

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(armPositionActionClient, nodeConfiguration);

        jointStateSub = new JointStateSub();
        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(jointStateSub, nodeConfiguration);

        nxt_talker = new JointTalker();
        nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(nxt_talker, nodeConfiguration);

    }

}
