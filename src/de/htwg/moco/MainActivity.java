package de.htwg.moco;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import nxt_msgs.JointCommand;
import org.ros.node.topic.Publisher;


/**
 * @author ch721ulr@htwg-konstanz.de (Christoph Ulrich)
 * @author beschaef@htwg-konstanz.de (Benjamin Schaefer)
 */
public class MainActivity extends RosActivity {

  private JointTalker joint_0_talker;

  // TODO: Title still in work. Just changed the tutorial title
  public MainActivity() {
    super("RosMindStormManipulator", "RosMindStormManipulator");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);
  }

  public void publishMessage(View view){
    joint_0_talker.loop();
    Toast.makeText(this, "HELLO", Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {

    joint_0_talker = new JointTalker();

    NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
    nodeConfiguration.setMasterUri(getMasterUri());
    nodeMainExecutor.execute(joint_0_talker, nodeConfiguration);




  }

}
