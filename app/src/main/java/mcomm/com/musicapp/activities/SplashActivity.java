package mcomm.com.musicapp.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }*/

        Intent startApp = new Intent(this, AuthActivity.class);
        startActivity(startApp);
        finish();
    }
}
