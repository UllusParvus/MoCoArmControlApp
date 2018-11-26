package de.htwg.moco;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

public class JointTalker extends AbstractNodeMain {
    private String topic_name;
    private int joint_number;
    private int sequenceNumber;

    private Publisher<std_msgs.String> publisher;

    public JointTalker() {
        this.topic_name = "joint_0";
        this.joint_number = 0;
    }

    public JointTalker(String topic, int joint_number) {
        this.topic_name = topic;
        this.joint_number = joint_number;
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("app/joint_" + this.joint_number + "_publisher");
    }

    public void onStart(ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher(this.topic_name, "std_msgs/String"); // TODO: change publisher message to nxt message

    }

    // TODO: change the loop to nxt messages!
    public void loop() {
        std_msgs.String str = publisher.newMessage();
        str.setData("Hello world! " + this.sequenceNumber);
        //str = "HELLO " + this.sequenceNumber;
        publisher.publish(str);
        ++this.sequenceNumber;

    }
}

