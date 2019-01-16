# MoCoArmControlApp

# Message and Action dependencies
To get the custom messages from the package 'nxt_msgs' working in rosjava, you have to generate the rosjava artifacts manually with the following command:
```genjava_message_artifacts --verbose -p nxt_msgs```

To get the custom action messages from the package 'nxt_action_msgs' working in rosjava, at first there have to be msg-files created manually with:
```rosrun actionlib_msgs genaction.py -o msg/ action/ArmPosition.action```
Then, again, generate the rosjava artifacts:
```genjava_message_artifacts --verbose -p nxt_msgs```


