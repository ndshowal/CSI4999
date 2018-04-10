package com.newtest.test.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IdentificationSettings extends AppCompatActivity {
    private final String TAG = "Identification Settings";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification_settings);

        user = getIntent().getParcelableExtra("UserKey");

        //to create a save changes button
        Button saveChangesBtn = (Button)findViewById(R.id.save_changes_button);

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IdentificationSettings.this, Settings.class));


            }
        });

    }
}