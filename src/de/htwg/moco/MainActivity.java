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
import org.ros.node.topic.Publisher;


/**
 * @author ch721ulr@htwg-konstanz.de (Christoph Ulrich)
 * @author beschaef@htwg-konstanz.de (Benjamin Schaefer)
 */
public class MainActivity extends RosActivity {

  private JointTalker nxt_1_talker;
  private JointTalker nxt_2_talker;

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
    switch(view.getId()){
      case R.id.b_joint_0_p:
        break;
      case R.id.b_joint_0_m:
        break;
      case R.id.b_joint_1_p:
        break;
      case R.id.b_joint_1_m:
        break;
      case R.id.b_joint_2_p:
        break;
      case R.id.b_joint_2_m:
        break;
      case R.id.b_gripper_p:
        break;
      case R.id.b_gripper_m:
        break;
    }
    nxt_1_talker.loop(1.2, "motor_1");
    Toast.makeText(this, "HELLO", Toast.LENGTH_SHORT).show();
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
