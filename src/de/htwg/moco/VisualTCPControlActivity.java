package de.htwg.moco;

import android.graphics.Color;
import android.os.Bundle;
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

  RobotUI robotUI;

  public VisualTCPControlActivity() {
    //specify the master uri of your roscore machine here as last param
    super("VisualControl", "VisualControl", URI.create("http://10.42.0.33:11311"));
  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        robotUI = new RobotUI(this);
        robotUI.setBackgroundColor(Color.WHITE);
        setContentView(robotUI);


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
