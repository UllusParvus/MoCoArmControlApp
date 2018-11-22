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


/**
 * @author ch721ulr@htwg-konstanz.de (Christoph Ulrich)
 * @author beschaef@htwg-konstanz.de (Benjamin Schaefer)
 */
public class MainActivity extends RosActivity {

  private int cameraId;
  private RosCameraPreviewView rosCameraPreviewView;

  public MainActivity() {
    super("CameraTutorial", "CameraTutorial");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);
    //rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.ros_camera_pre//view_view);
  }

  /*@Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      int numberOfCameras = Camera.getNumberOfCameras();
      final Toast toast;
      if (numberOfCameras > 1) {
        cameraId = (cameraId + 1) % numberOfCameras;
        rosCameraPreviewView.releaseCamera();
        rosCameraPreviewView.setCamera(getCamera());
        toast = Toast.makeText(this, "Switching cameras.", Toast.LENGTH_SHORT);
      } else {
        toast = Toast.makeText(this, "No alternative cameras to switch to.", Toast.LENGTH_SHORT);
      }
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          toast.show();
        }
      });
    }
    return true;
  }
  */

  public void publishMessage(View view){
    Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {
    cameraId = 0;

    /*rosCameraPreviewView.setCamera(getCamera());

    try {
      java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
      java.net.InetAddress local_network_address = socket.getLocalAddress();
      socket.close();
      NodeConfiguration nodeConfiguration =
              NodeConfiguration.newPublic(local_network_address.getHostAddress(), getMasterUri());
      nodeMainExecutor.execute(rosCameraPreviewView, nodeConfiguration);
    } catch (IOException e) {
      // Socket problem
      Log.e("Camera Tutorial", "socket error trying to get networking information from the master uri");
    }
    */

  }
  /*
  private Camera getCamera() {
    Camera cam = Camera.open(cameraId);
    Camera.Parameters camParams = cam.getParameters();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      if (camParams.getSupportedFocusModes().contains(
              Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
        camParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
      } else {
        camParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
      }
    }
    cam.setParameters(camParams);
    return cam;
  }
  */
}
