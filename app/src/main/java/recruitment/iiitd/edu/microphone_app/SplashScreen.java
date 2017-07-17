package recruitment.iiitd.edu.microphone_app;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import recruitment.iiitd.edu.mew.R;


public class SplashScreen extends AppCompatActivity {

    // Splash screen time
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);


                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
