package com.newtest.test.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        
    }
    public void buttonOnClick(View v) {
        Button button=(Button) v;
        ((Button) v).setText("clicked");
    }
}
