package com.elitetechnologies.locktaskmode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LockScreenActivity extends AppCompatActivity {
    private TextView tv_lockmsg;
    private EditText et_lockpassword;
    private Button btn_lockok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);
        tv_lockmsg = (TextView) findViewById(R.id.tv_lockmsg);
        et_lockpassword=(EditText)findViewById(R.id.et_lockpassword);
        btn_lockok=(Button)findViewById(R.id.btn_lockok);

        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_lockmsg.setText("You are locked out of the app: \n " + millisUntilFinished / 1000+"secs \nremaining");
            }

            public void onFinish() {
                finish();
                Intent i = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                startActivity(i);
            }

        }.start();

        btn_lockok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                    editor.putInt("password", Integer.parseInt(et_lockpassword.getText().toString()));
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
