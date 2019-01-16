package de.htwg.moco;

import android.util.Log;

import com.github.ekumen.rosjava_actionlib.ActionClient;
import com.github.ekumen.rosjava_actionlib.ActionClientListener;
import com.github.ekumen.rosjava_actionlib.ClientStateMachine;

import org.ros.message.Duration;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;

import java.util.ArrayList;
import java.util.List;

import actionlib_msgs.GoalID;
import actionlib_msgs.GoalStatus;
import actionlib_msgs.GoalStatusArray;
import nxt_action_msgs.ArmPositionActionFeedback;
import nxt_action_msgs.ArmPositionActionGoal;
import nxt_action_msgs.ArmPositionActionResult;
import nxt_action_msgs.ArmPositionFeedback;
import nxt_action_msgs.ArmPositionGoal;
import nxt_action_msgs.ArmPositionResult;


import static android.os.SystemClock.sleep;

//TODO: change sysouts to android standard logging/output (e.g. Toast)

public class ArmPositionActionClient extends AbstractNodeMain implements ActionClientListener<ArmPositionActionFeedback, ArmPositionActionResult> {
    static {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }
    public ActionClient ac = null;
    private volatile boolean resultReceived = false;
    private Log log;

    @Override
    public void onStart(ConnectedNode node){
        ac = new ActionClient<ArmPositionActionGoal, ArmPositionActionFeedback, ArmPositionActionResult>(node, "/ArmPosition", ArmPositionActionGoal._TYPE, ArmPositionActionFeedback._TYPE, ArmPositionActionResult._TYPE);

        ArmPositionActionGoal goalMessage;
        GoalID gid;
        Duration serverTimeout = new Duration(20);
        boolean serverStarted;

        // Attach listener for the callbacks
        ac.attachListener(this);
        Log.i("AC", "Waiting for action server to start...");
        serverStarted = ac.waitForActionServerToStart(new Duration(20));
        if (serverStarted) {
            Log.i("AC", "Action server started.");
        }
        else {
            Log.i("AC", "No actionlib server found after waiting for " + serverTimeout.totalNsecs()/1e9 + " seconds!");
            System.exit(1);
        }

        goalMessage = (ArmPositionActionGoal)ac.newGoalMessage();
        ArmPositionGoal armPositionGoal = goalMessage.getGoal();

        List<String> jointNames = new ArrayList<String>();
        jointNames.add("motor_1"); jointNames.add("motor_2"); jointNames.add("motor_3");
        armPositionGoal.setJointNamesGoal(jointNames);
        float[] jointPositions = new float[]{4,5,6};
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
        System.out.println("Sending a new goal...");
        ac.sendGoal(goalMessage);
        gid = ac.getGoalId(goalMessage);
        Log.i("AC", "Sent goal with ID: " + gid.getId());
        Log.i("AC", "Cancelling this goal...");
        ac.sendCancel(gid);
        while (ac.getGoalState() != ClientStateMachine.ClientStates.DONE) {
            sleep(1);
        }
        Log.i("AC", "Goal cancelled succesfully.");
        System.exit(0);
    }

    @Override
    public void resultReceived(ArmPositionActionResult message) {
        ArmPositionResult result = message.getResult();
        // TODO: implement reactions to received result

    }

    @Override
    public void feedbackReceived(ArmPositionActionFeedback message) {
        ArmPositionFeedback feedback = message.getFeedback();
        // TODO: implement reactions to received feedback
    }

    @Override
    public void statusReceived(GoalStatusArray status) {
        List<GoalStatus> statusList = status.getStatusList();
        for(GoalStatus gs:statusList) {
            Log.i("AC","GoalID: " + gs.getGoalId().getId() + " -- GoalStatus: " + gs.getStatus() + " -- " + gs.getText());
        }
        Log.i("AC","Current state of our goal: " + ClientStateMachine.ClientStates.translateState(ac.getGoalState()));
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("arm_position_client");
    }
}
