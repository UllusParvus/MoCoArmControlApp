package de.htwg.moco;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.android.view.camera.RosCameraPreviewView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import android.util.Log;
import java.io.IOException;
import nxt_msgs.JointCommand;
import org.ros.node.topic.Publisher;


/**
 * @author ch721ulr@htwg-konstanz.de (Christoph Ulrich)
 * @author beschaef@htwg-konstanz.de (Benjamin Schaefer)
 */
public class MainActivity extends RosActivity {

  private int cameraId;
  private JointTalker joint_0_talker;

  public MainActivity() {
    super("CameraTutorial", "CameraTutorial");
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
    cameraId = 0;

    joint_0_talker = new JointTalker();

    NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
    nodeConfiguration.setMasterUri(getMasterUri());
    nodeMainExecutor.execute(joint_0_talker, nodeConfiguration);




  }

}
