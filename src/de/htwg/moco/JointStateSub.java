package de.htwg.moco;

import android.util.Log;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import sensor_msgs.JointState;

public class JointStateSub extends AbstractNodeMain {
    private String topic_name;
    private double motor1_position;
    private double motor2_position;
    private double motor3_position;

    public double getMotor1_position() {
        return motor1_position;
    }

    public double getMotor2_position() {
        return motor2_position;
    }

    public double getMotor3_position() {
        return motor3_position;
    }

    private Subscriber<JointState> jointStateSubscriber;

    public JointStateSub() {
        this.topic_name = "/nxt_1/joint_state";
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("joint_state_subscriber");
    }

    public void onStart(ConnectedNode connectedNode) {
        jointStateSubscriber = connectedNode.newSubscriber(this.topic_name, "sensor_msgs/JointState");

        jointStateSubscriber.addMessageListener(new MessageListener<JointState>() {
            @Override
            public void onNewMessage(JointState jointState) {
                if (jointState.getName().get(0).equals("motor_1")){
                    motor1_position = jointState.getPosition()[0];
                } else if (jointState.getName().get(0).equals("motor_2")){
                    motor2_position = jointState.getPosition()[0];
                } else if (jointState.getName().get(0).equals("motor_3")){
                    motor3_position = jointState.getPosition()[0];
                }
            }
        });

    }
}

