package de.htwg.moco;

import android.util.Log;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import nxt_msgs.JointCommand;
import nxt_msgs.Range;

public class JointTalker extends AbstractNodeMain {
    private String topic_name;

    private Publisher<JointCommand> joint_publisher;
    private Subscriber<Range> ultrasonic_sub;

    private Range us_range;

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
        joint_publisher = connectedNode.newPublisher(this.topic_name, "nxt_msgs/JointCommand");
        ultrasonic_sub = connectedNode.newSubscriber("/ultrasonic_sensor", "nxt_msgs/Range");
        ultrasonic_sub.addMessageListener(new MessageListener<Range>() {
            @Override
            public void onNewMessage(Range range) {
                us_range = range;
                Log.i("Sub" , "got range " + us_range.getRange() );
            }
        }, 1);

    }

    // TODO: change the loop to nxt messages!
    public void loop(double effort, String motor_name) {
        JointCommand nxt_msg = joint_publisher.newMessage();
        nxt_msg.setEffort(effort);
        nxt_msg.setName(motor_name);
        joint_publisher.publish(nxt_msg);

    }

    public double get_range() {
        return us_range.getRange();

    }
}

