package de.htwg.moco;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import nxt_msgs.JointCommand;

public class JointTalker extends AbstractNodeMain {
    private String topic_name;

    private Publisher<JointCommand> joint_publisher;

    public JointTalker() {
        this.topic_name = "/nxt_1/joint_command";
    }

    public JointTalker(int nxt_number) {
        this.topic_name = "/nxt_" + nxt_number + "/joint_command";
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("app/" + topic_name + "_publisher");
    }

    public void onStart(ConnectedNode connectedNode) {
        joint_publisher = connectedNode.newPublisher(this.topic_name, "nxt_msgs/JointCommand"); // TODO: change publisher message to nxt message

    }

    // TODO: change the loop to nxt messages!
    public void loop(double effort, String motor_name) {
        JointCommand nxt_msg = joint_publisher.newMessage();
        nxt_msg.setEffort(effort);
        nxt_msg.setName(motor_name);
        joint_publisher.publish(nxt_msg);
    }
}

