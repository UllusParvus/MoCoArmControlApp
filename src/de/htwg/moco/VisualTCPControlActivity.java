package de.htwg.moco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;


/**
 * @author ch721ulr@htwg-konstanz.de (Christoph Ulrich)
 * @author beschaef@htwg-konstanz.de (Benjamin Schaefer)
 */
public class VisualTCPControlActivity extends RosActivity {

    static final String JOINT_1_ANGLE_DEGREES = "joint1AngleDegrees";
    static final String JOINT_2_ANGLE_DEGREES = "joint2AngleDegrees";
    RobotUI robotUI;
    private GLSurfaceView mGLView;

    public VisualTCPControlActivity() {
        //specify the master uri of your roscore machine here as last param
        super("VisualControl", "VisualControl", URI.create("http://10.42.0.33:11311"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        robotUI = new RobotUI(this, (double) sharedPreferences.getFloat(JOINT_1_ANGLE_DEGREES, 45), (double) sharedPreferences.getFloat(JOINT_2_ANGLE_DEGREES, 90));

        //robotUI = new RobotUI(this, 45, 90);

        // recovering the instance state
        if (savedInstanceState != null) {
            robotUI.setJoint1AngleDegrees(savedInstanceState.getDouble(JOINT_1_ANGLE_DEGREES));
            robotUI.setJoint2AngleDegrees(savedInstanceState.getDouble(JOINT_2_ANGLE_DEGREES));
        }

        robotUI.setBackgroundColor(Color.WHITE);
        setContentView(robotUI);
        //Toast.makeText(this, "lifecycle, onCreate invoked", Toast.LENGTH_SHORT).show();
        //mGLView = new MyGLSurfaceView(this);
        //setContentView(mGLView);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putDouble(JOINT_1_ANGLE_DEGREES, robotUI.getJoint1AngleDegrees());
        savedInstanceState.putDouble(JOINT_2_ANGLE_DEGREES, robotUI.getJoint2AngleDegrees());
        Log.d("onSaveInstanceState", "saved angle 1 -> " + savedInstanceState.getDouble(JOINT_1_ANGLE_DEGREES));
        Log.d("onSaveInstanceState", "saved angle 2 -> " + savedInstanceState.getDouble(JOINT_2_ANGLE_DEGREES));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        robotUI.setJoint1AngleDegrees(savedInstanceState.getDouble(JOINT_1_ANGLE_DEGREES));
        robotUI.setJoint2AngleDegrees(savedInstanceState.getDouble(JOINT_2_ANGLE_DEGREES));
        Log.d("onRestoreInstanceState", "restored angle 1 -> " + savedInstanceState.getDouble(JOINT_1_ANGLE_DEGREES));
        Log.d("onRestoreInstanceState", "restored angle 2 -> " + savedInstanceState.getDouble(JOINT_2_ANGLE_DEGREES));
    }

    /*
    @Override
    public void onStop(){
        super.onStop();
        Log.d("VisualTCPControlAct", "onStop after super.onStop");
        Intent data = new Intent();
        data.putExtra(JOINT_1_ANGLE_DEGREES, robotUI.getJoint1AngleDegrees());
        data.putExtra(JOINT_2_ANGLE_DEGREES, robotUI.getJoint2AngleDegrees());
        setResult(RESULT_OK, data);
        finish();
        //Toast.makeText(this, "lifecycle, onStop invoked", Toast.LENGTH_SHORT).show();
    }*/

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Log.d("VisualTCPControlAct", "onKeyDown started");
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Log.d("VisualTCPControlAct", "keyCode == BACK");
            Intent data = new Intent();
            data.putExtra(JOINT_1_ANGLE_DEGREES, robotUI.getJoint1AngleDegrees());
            data.putExtra(JOINT_2_ANGLE_DEGREES, robotUI.getJoint2AngleDegrees());
            setResult(RESULT_OK, data);
            finish();
            return true;
        }
        return false;
    }*/

    @Override
    public void onBackPressed() {
        Log.d("LOGD", "onBackPressed");
        /*Intent data = new Intent();
        data.putExtra(JOINT_1_ANGLE_DEGREES, robotUI.getJoint1AngleDegrees());
        data.putExtra(JOINT_2_ANGLE_DEGREES, robotUI.getJoint2AngleDegrees());
        setResult(RESULT_OK, data);
        super.onBackPressed();
        finish();*/
        savePreferences();
        super.onBackPressed();
    }

    private void savePreferences(){
        Log.d("LOGD", "savePreferences");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(JOINT_1_ANGLE_DEGREES, (float) robotUI.getJoint1AngleDegrees());
        editor.putFloat(JOINT_2_ANGLE_DEGREES, (float) robotUI.getJoint2AngleDegrees());
        editor.commit();
    }


  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {
    /*
    nxt_1_talker = new JointTalker();
    nxt_2_talker = new JointTalker(2);

    NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
    nodeConfiguration.setMasterUri(getMasterUri());
    nodeMainExecutor.execute(nxt_1_talker, nodeConfiguration);
    */
  }

}
