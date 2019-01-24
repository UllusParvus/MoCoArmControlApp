package de.htwg.moco;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import nxt_msgs.JointCommand;

/**
 * This class provides two ROS publishers for publishing the nxt commands to the corresponding topic.
 *
 * @author Christoph Ulrich
 * @author Benjamin Schaefer
 * @see <a href="https://wiki.ros.org/Topics">ROS topics</a>
 * @see <a href="http://wiki.ros.org/roscpp/Overview/Publishers%20and%20Subscribers">ROS Subscriber and Publisher</a>
 */

public class JointTalker extends AbstractNodeMain {
    private Publisher<JointCommand> nxt1_publisher;
    private Publisher<JointCommand> nxt2_publisher;

    public GraphName getDefaultNodeName() {
        return GraphName.of("app/nxt_publisher");
    }

    /**
     * Creates two ROS publishers.
     * @param connectedNode
     */
    public void onStart(ConnectedNode connectedNode) {
        nxt1_publisher = connectedNode.newPublisher("/nxt_1/joint_command", "nxt_msgs/JointCommand");
        nxt2_publisher = connectedNode.newPublisher("/nxt_2/joint_command", "nxt_msgs/JointCommand");
    }

    /**
     * Publishes a nxt joint command message to the NXT brick controlling the upper joints.
     * @param effort Double between -1.5 and 1.5 which specifies the "power" with which the corresponding
     *               motor is driven.
     * @param motor_name The corresponding motor name.
     */
    public void loop_nxt1(double effort, String motor_name) {
        JointCommand nxt_msg = nxt1_publisher.newMessage();
        nxt_msg.setEffort(effort);
        nxt_msg.setName(motor_name);
        nxt1_publisher.publish(nxt_msg);
    }

    /**
     * Publishes a nxt joint command message to the NXT brick controlling the base.
     * @param effort Double between -1.5 and 1.5 which specifies the "power" with which the corresponding
     *               motor is driven.
     * @param motor_name The corresponding motor name.
     */
    public void loop_nxt2(double effort, String motor_name) {
        JointCommand nxt_msg = nxt2_publisher.newMessage();
        nxt_msg.setEffort(effort);
        nxt_msg.setName(motor_name);
        nxt2_publisher.publish(nxt_msg);
    }

}

