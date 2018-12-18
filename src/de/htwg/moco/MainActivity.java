package de.htwg.moco;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.mainTextView);
    }

    public void startControls (View view){
        String button_text;
        button_text =((Button)view).getText().toString();
        if(button_text.equals("Start Joint Control"))
        {
            Intent jointControl = new Intent(this, JointControlActivity.class);
            startActivity(jointControl);
        }
        else if (button_text.equals("Start visual tcp control"))
        {
            Intent visualTCPControl = new Intent(this, VisualTCPControlActivity.class);
            startActivity(visualTCPControl);

        }
    }

}
