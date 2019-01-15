package de.htwg.moco;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import java.net.URI;

/**
 * @author ch721ulr@htwg-konstanz.de (Christoph Ulrich)
 * @author beschaef@htwg-konstanz.de (Benjamin Schaefer)
 */
public class JointControlActivity extends RosActivity {

    private JointTalker nxt_1_talker;
    private JointTalker nxt_2_talker;
    private double calibrate = 0.0;
    private boolean finished = false;

    // TODO: Title still in work. Just changed the tutorial title
    public JointControlActivity() {
    //specify the master uri of your roscore machine here as last param
        super("RosMindStormManipulator", "RosMindStormManipulator", URI.create("http://10.42.0.33:11311"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Toast.makeText(this, Float.toString(sharedPreferences.getFloat(VisualTCPControlActivity.JOINT_1_ANGLE_DEGREES, 45)), Toast.LENGTH_SHORT).show();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_joint_control);
        findViewById(R.id.buttonJointControl);

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

    public class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int motor_positive = 1;
            String motor_name = "";
            double motor_effort = 1.5;
            switch(v.getId()){
                case R.id.b_joint_0_m:
                    motor_positive = -1;
                case R.id.b_joint_0_p:
                    motor_name = "motor_1";
                    break;
                case R.id.b_joint_1_m:
                    motor_positive = -1;
                case R.id.b_joint_1_p:
                    motor_name = "motor_2";
                    break;
                case R.id.b_joint_2_m:
                    motor_positive = -1;
                case R.id.b_joint_2_p:
                    motor_name = "motor_3";
                    motor_effort = 0.6;
                    break;
                case R.id.b_gripper_m:
                    while (!finished) {
                        double range = nxt_1_talker.get_range();
                        Log.i("RANGE ", ""+range);
                        if (range > .185) {
                            nxt_1_talker.loop(-0.6, "motor_2");
                        }
                        if (range < .195) {
                            nxt_1_talker.loop(0.6, "motor_2");
                        }
                        if ((range <= .195) && (range >= .185)) {
                            finished = true;
                            nxt_1_talker.loop(0.0, "motor_2");
                        }
                    }
                    finished = false;
                    return true;
                case R.id.b_gripper_p:
                    break;
        }
        int action = event.getAction();
        if (MotionEvent.ACTION_DOWN == action) {
            v.setPressed(true);
            nxt_1_talker.loop(motor_effort * motor_positive, motor_name);
        }
        else if (MotionEvent.ACTION_UP == action) {
            v.setPressed(false);
            nxt_1_talker.loop(0, motor_name);
        }


      return true;
    }

  }

  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {

    nxt_1_talker = new JointTalker();
    nxt_2_talker = new JointTalker(2);

    NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
    nodeConfiguration.setMasterUri(getMasterUri());
    nodeMainExecutor.execute(nxt_1_talker, nodeConfiguration);


  }


}
