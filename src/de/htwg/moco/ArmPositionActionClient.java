package de.htwg.moco;

import android.util.Log;

import com.github.ekumen.rosjava_actionlib.ActionClient;
import com.github.ekumen.rosjava_actionlib.ActionClientListener;
import com.github.ekumen.rosjava_actionlib.ClientStateMachine;

import org.ros.message.Duration;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;

import java.util.List;

import actionlib_msgs.GoalID;
import actionlib_msgs.GoalStatusArray;
import nxt_action_msgs.ArmPositionActionFeedback;
import nxt_action_msgs.ArmPositionActionGoal;
import nxt_action_msgs.ArmPositionActionResult;
import nxt_action_msgs.ArmPositionFeedback;
import nxt_action_msgs.ArmPositionGoal;
import nxt_action_msgs.ArmPositionResult;

import static android.os.SystemClock.sleep;

/**
 * Implements a ROS conform action client. Handles sending commands,
 * receiving and processing feedback and results from the ROS action server.
 *
 * @author  Christoph Ulrich, Benjamin Schaefer
 * @see <a href="https://wiki.ros.org/actionlib">ROS actionlib</a>
 */

public class ArmPositionActionClient extends AbstractNodeMain implements ActionClientListener<ArmPositionActionFeedback, ArmPositionActionResult> {
    static {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }
    public ActionClient ac = null;

    /**
     * Connects with the ROS action server and defines the type of ROS action to be used.
     *
     * @param node The node parameter is automatically committed on initialization.
     * @return noting
     */
    @Override
    public void onStart(ConnectedNode node){
        ac = new ActionClient<ArmPositionActionGoal, ArmPositionActionFeedback, ArmPositionActionResult>(node, "/ArmPosition", ArmPositionActionGoal._TYPE, ArmPositionActionFeedback._TYPE, ArmPositionActionResult._TYPE);

        boolean serverStarted;
        Duration serverTimeout = new Duration(20);

        // Attach listener for the callbacks
        ac.attachListener(this);
        Log.i("AC", "Waiting for action server to start...");
        serverStarted = ac.waitForActionServerToStart(new Duration(20));
        if (serverStarted) {
            Log.i("AC", "Action server started.");
        }
        else {
            Log.i("AC", "No actionlib server found after waiting for " + serverTimeout.totalNsecs()/1e9 + " seconds!");
            node.shutdown();
        }
    }

    /**
     * Sends a arm position goal message to the ROS action server.
     * @param jointNames List of strings containing the names of the motors defined in the
     *                   hardware configuration
     * @param jointPositions Array of floats specifying the goal position of each according motor
     *                       in radians
     */
    public void sendAction(List<String> jointNames, float[] jointPositions){
        ArmPositionActionGoal goalMessage;
        GoalID gid;

        goalMessage = (ArmPositionActionGoal)ac.newGoalMessage();
        ArmPositionGoal armPositionGoal = goalMessage.getGoal();

        armPositionGoal.setJointNamesGoal(jointNames);
        armPositionGoal.setJointPositionAnglesGoal(jointPositions);
        Log.i("AC", "Sending goal...");
        ac.sendGoal(goalMessage);
        gid = ac.getGoalId(goalMessage);
        Log.i("AC", "Sent goal with ID: " + gid.getId());
        Log.i("AC", "Waiting for goal to complete...");
        while (ac.getGoalState() != ClientStateMachine.ClientStates.DONE) {
            sleep(1);
        }

        Log.i("AC", "Goal completed!");
    }

    /**
     * Handles the result from the finished action
     * @param message
     */
    @Override
    public void resultReceived(ArmPositionActionResult message) {
        ArmPositionResult result = message.getResult();
        // TODO: implement reactions to received result

    }

    /**
     * Permanently handles the feedback provided by the ROS action server
     * @param message
     */
    @Override
    public void feedbackReceived(ArmPositionActionFeedback message) {
        ArmPositionFeedback feedback = message.getFeedback();
        // TODO: implement reactions to received feedback
    }

    /**
     * Permanently handles the status received by the ROS action server.
     * @param status
     */
    @Override
    public void statusReceived(GoalStatusArray status) {
        ;
    }

    /**
     * @return the ROS node name (for ROS-internal communication)
     */
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("arm_position_client");
    }
}
