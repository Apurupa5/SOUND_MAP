package recruitment.iiitd.edu.microphone_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import recruitment.iiitd.edu.mew.HomeScreen;
import recruitment.iiitd.edu.mew.R;
import recruitment.iiitd.edu.utils.LogTimer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button developer;
    private Button user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        developer = (Button) findViewById(R.id.button1);
        user = (Button) findViewById(R.id.button2);
        developer.setOnClickListener(this);
        user.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.button1: //To go to Developer mode.
                           Intent i1 = new Intent(getApplicationContext(), HomeScreen.class);
                           startActivity(i1);
                           break;
            case R.id.button2: // To go to user mode.
                            Intent i = new Intent(getApplicationContext(), ChoiceActivity.class);
                            startActivity(i);
                            break;


        }
    }
}
